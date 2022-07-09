package com.fin.currency.exchange.service.impl;

import com.fin.currency.exchange.dto.ExchangeRateDto;
import com.fin.currency.exchange.model.ExchangeRates;
import com.fin.currency.exchange.wrapper.ExchangeRequest;
import com.fin.currency.exchange.service.ExchangeService;
import com.fin.currency.exchange.utils.ValidateRequestUtil;
import com.fin.currency.exchange.wrapper.ExchangeResponse;
import java.security.InvalidParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeServiceImpl implements ExchangeService {

  private ExchangeRateDto exchangeRateDto;

  @Autowired
  public ExchangeServiceImpl(ExchangeRateDto exchangeRateDto) {
    this.exchangeRateDto = exchangeRateDto;
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
      status.setMessage("Exchange calculation succesful");
    } catch (InvalidParameterException e) {
      System.out.println("Error found with  : {}  " + e.getMessage());
      status.setCode("Failure");
      status.setMessage(e.getMessage());
    }
    exchangeResponse.setStatus(status);
    return exchangeResponse;
  }
}
