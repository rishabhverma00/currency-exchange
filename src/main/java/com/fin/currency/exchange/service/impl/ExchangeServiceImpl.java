package com.fin.currency.exchange.service.impl;

import com.fin.currency.exchange.configuration.RedisCacheConfiguration;
import com.fin.currency.exchange.dto.ExchangeRateDto;
import com.fin.currency.exchange.enums.CurrencyType;
import com.fin.currency.exchange.mapper.ExchangeConfigs;
import com.fin.currency.exchange.model.ExchangeRates;
import com.fin.currency.exchange.service.ExchangeService;
import com.fin.currency.exchange.utils.ValidateRequestUtil;
import com.fin.currency.exchange.wrapper.ExchangeRequest;
import com.fin.currency.exchange.wrapper.ExchangeResponse;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    try {
      ValidateRequestUtil.validateRequest(exchangeRequest);
      ExchangeRates exchangeRate = exchangeRateDto.getExchangeRatesByExchangedCurrencyTypeAndAndProvidedCurrencyType(
          exchangeRequest.getExchangeCurrencyType(), exchangeRequest.getProvidedCurrencyType());
      System.out.println("ExchangeRates " + exchangeRate);
      Double exchangeValue = exchangeRequest.getProvidedAmount() / exchangeRate.getExchangeRate();
      exchangeResponse.setExchangedAmount(exchangeValue);
      exchangeResponse.setExchangedCurrencyType(exchangeRequest.getExchangeCurrencyType());
      exchangeResponse.setExchangeRate(exchangeRate.getExchangeRate());
      exchangeResponse.setProvidedAmount(exchangeRequest.getProvidedAmount());
      exchangeResponse.setProvidedCurrencyType(exchangeRequest.getProvidedCurrencyType());
      status.setCode("Success");
      status.setMessage("Exchange calculation successful");
    } catch (InvalidParameterException e) {
      System.out.println("Error found with  : {}  " + e.getMessage());
      status.setCode("Failure");
      status.setMessage(e.getMessage());
    }
    exchangeResponse.setStatus(status);
    return exchangeResponse;
  }

  @Scheduled(fixedRate = 60000)
  public void pushLatestExchangeRatesInRedis() throws IOException {
    System.out.println("going to execute the pushLatestExchangeRatesInRedis");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    Stream.of(CurrencyType.values()).forEach(c -> {
      try {
        createCacheInRedis(client, formatter, c.getCurrencyName());
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
      // LiveExchangeRates liveRates = new ObjectMapper().readValue(response.body().string(),
      //   LiveExchangeRates.class);
      ValueOperations valueOperations = redisTemplate.opsForValue();
      String responseBody = response.body().string();
      valueOperations.set(hashKey, responseBody, Duration.ofHours(24));
    } else {
      System.out.println("already present hashkey : {}  " + hashKey);
    }
  }
}
