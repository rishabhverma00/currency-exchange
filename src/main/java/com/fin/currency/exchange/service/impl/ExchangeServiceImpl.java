package com.fin.currency.exchange.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fin.currency.exchange.configuration.RedisCacheConfiguration;
import com.fin.currency.exchange.dto.ExchangeRateDto;
import com.fin.currency.exchange.enums.CurrencyType;
import com.fin.currency.exchange.mapper.ExchangeConfigs;
import com.fin.currency.exchange.model.ExchangeRates;
import com.fin.currency.exchange.service.ExchangeService;
import com.fin.currency.exchange.utils.CommonUtils;
import com.fin.currency.exchange.utils.ValidateRequestUtil;
import com.fin.currency.exchange.wrapper.ExchangeRequest;
import com.fin.currency.exchange.wrapper.ExchangeResponse;
import com.fin.currency.exchange.wrapper.ExchangeResponse.Status;
import com.fin.currency.exchange.wrapper.LiveExchangeRates;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.stream.Stream;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExchangeServiceImpl implements ExchangeService {

  private ExchangeRateDto exchangeRateDto;
  private ExchangeConfigs exchangeConfigs;
  private RedisCacheConfiguration redisCacheConfiguration;

  @Autowired
  public ExchangeServiceImpl(ExchangeRateDto exchangeRateDto, ExchangeConfigs exchangeConfigs,
      RedisCacheConfiguration redisCacheConfiguration) {
    this.exchangeRateDto = exchangeRateDto;
    this.exchangeConfigs = exchangeConfigs;
    this.redisCacheConfiguration = redisCacheConfiguration;
  }

  @Override
  public ExchangeResponse exchange(ExchangeRequest exchangeRequest) {
    ExchangeResponse exchangeResponse = new ExchangeResponse();
    ExchangeResponse.Status status = new ExchangeResponse.Status();
    Double exchangeRate = 0D;
    try {
      ValidateRequestUtil.validateRequest(exchangeRequest);
      System.out.println(exchangeConfigs.getIsRedisEnable());
      if (Boolean.valueOf(exchangeConfigs.getIsRedisEnable())) {
        getExchangeDetailsFromCache(exchangeRequest, exchangeResponse, status);
      } else {
        getExchangeDetailsFromDb(exchangeRequest, exchangeResponse, status);
      }
    } catch (InvalidParameterException | JsonProcessingException e) {
      System.out.println("Error found with  : {}  " + e.getMessage());
      status.setCode("Failure");
      status.setMessage(e.getMessage());
    }
    exchangeResponse.setStatus(status);
    return exchangeResponse;
  }

  private void getExchangeDetailsFromDb(ExchangeRequest exchangeRequest, ExchangeResponse exchangeResponse,
      Status status) {
    Double exchangeRate;
    ExchangeRates exchangeRateDb = exchangeRateDto.getExchangeRatesByExchangedCurrencyTypeAndAndProvidedCurrencyType(
        exchangeRequest.getExchangeCurrencyType(), exchangeRequest.getProvidedCurrencyType());
    System.out.println("ExchangeRates in DB " + exchangeRateDb);
    if (Objects.nonNull(exchangeRateDb)) {
      exchangeRate = exchangeRateDb.getExchangeRate();
      createExchangeResponse(exchangeRequest, exchangeResponse, status, exchangeRate);
    }
  }

  private void getExchangeDetailsFromCache(ExchangeRequest exchangeRequest, ExchangeResponse exchangeResponse,
      Status status) throws JsonProcessingException {
    Double exchangeRate;
    LiveExchangeRates rates = getExchangeRatesFromCache(
        exchangeRequest.getProvidedCurrencyType());
    System.out.println("ExchangeRates in cache" + rates);
    if (rates.getRates().containsKey(exchangeRequest.getExchangeCurrencyType())) {
      exchangeRate = rates.getRates().get(exchangeRequest.getExchangeCurrencyType());
      createExchangeResponse(exchangeRequest, exchangeResponse, status, exchangeRate);
    } else {
      status.setCode("Failure");
      status.setMessage("Exchange not supported");
    }
  }

  private void createExchangeResponse(ExchangeRequest exchangeRequest,
      ExchangeResponse exchangeResponse,
      Status status, Double rates) {
    Double exchangeValue = exchangeRequest.getProvidedAmount() / rates;
    exchangeResponse.setExchangedAmount(exchangeValue);
    exchangeResponse.setExchangedCurrencyType(exchangeRequest.getExchangeCurrencyType());
    exchangeResponse.setExchangeRate(rates);
    exchangeResponse.setProvidedAmount(exchangeRequest.getProvidedAmount());
    exchangeResponse.setProvidedCurrencyType(exchangeRequest.getProvidedCurrencyType());
    status.setCode("Success");
    status.setMessage("Exchange calculation successful");
  }

  private LiveExchangeRates getExchangeRatesFromCache(String baseCurrencyType)
      throws JsonProcessingException {
    String liveExchangeRate = getLiveExchangeRateString(LocalDateTime.now(), baseCurrencyType);
    if (liveExchangeRate == null) {
      System.out.println("Record not found in cache");
    } else {
      return new ObjectMapper().readValue(liveExchangeRate,
          LiveExchangeRates.class);
    }
    return null;
  }

  private String getLiveExchangeRateString(LocalDateTime dateTime, String baseCurrencyType) {
    System.out.println("Going to get live exchange rates with time : {} " + dateTime.toString());
    System.out.println(
        "Current difference " + ChronoUnit.DAYS.between(LocalDateTime.now(), dateTime));
    if (ChronoUnit.DAYS.between(LocalDateTime.now(), dateTime) >= 5) {
      return null;
    } else {
      RedisTemplate redisTemplate = redisCacheConfiguration.redisTemplate();
      ValueOperations valueOperations = redisTemplate.opsForValue();
      String hashKey = baseCurrencyType + "_" + dateTime.format(CommonUtils.formatter);
      return redisTemplate.hasKey(hashKey) ? String.valueOf(valueOperations.get(hashKey))
          : getLiveExchangeRateString(LocalDateTime.now().minusDays(1), baseCurrencyType);
    }
  }

  @Scheduled(fixedRate = 60000)
  public void pushLatestExchangeRatesInRedis() throws IOException {
    System.out.println("going to execute the pushLatestExchangeRatesInRedis");
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    Stream.of(CurrencyType.values()).forEach(c -> {
      try {
        createCacheInRedis(client, CommonUtils.formatter, c.getCurrencyName());
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private void createCacheInRedis(OkHttpClient client, DateTimeFormatter formatter,
      String baseCurrencyType) throws IOException {
    String dateTimeString = LocalDateTime.now().format(formatter);
    String hashKey = baseCurrencyType + "_" + dateTimeString;
    RedisTemplate redisTemplate = redisCacheConfiguration.redisTemplate();
    if (!redisTemplate.hasKey(hashKey)) {
      Request request = new Request.Builder()
          .url(exchangeConfigs.getExchangeRatesUrl() + "base=" + baseCurrencyType)
          .addHeader(exchangeConfigs.getExchangeApiKey(), exchangeConfigs.getExchangeApiValue())
          .method("GET", null).build();
      Response response = client.newCall(request).execute();
      ValueOperations valueOperations = redisTemplate.opsForValue();
      String responseBody = response.body().string();
      valueOperations.set(hashKey, responseBody, Duration.ofHours(24));
    } else {
      System.out.println("already present hashkey : {}  " + hashKey);
    }
  }
}
