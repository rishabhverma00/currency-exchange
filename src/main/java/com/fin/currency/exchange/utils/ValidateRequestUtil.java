package com.fin.currency.exchange.utils;

import com.fin.currency.exchange.enums.CurrencyType;
import com.fin.currency.exchange.mapper.ExchangeConfigs;
import com.fin.currency.exchange.wrapper.ExchangeRequest;
import java.security.InvalidParameterException;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateRequestUtil {

  private static ExchangeConfigs exchangeConfigs;

  @Autowired
  public ValidateRequestUtil(ExchangeConfigs exchangeConfigs) {
    this.exchangeConfigs = exchangeConfigs;
  }

  public static void validateRequest(ExchangeRequest exchangeRequest) {
    Stream.of(CurrencyType.values()).filter(
            p -> p.getCurrencyName().equalsIgnoreCase(exchangeRequest.getExchangeCurrencyType()))
        .findAny().orElseThrow(() -> new InvalidParameterException("Invalid exchange currency type"));
    Stream.of(CurrencyType.values()).filter(
            p -> p.getCurrencyName().equalsIgnoreCase(exchangeRequest.getProvidedCurrencyType()))
        .findAny().orElseThrow(() -> new InvalidParameterException("Invalid provided currency type"));
    if (exchangeRequest.getProvidedAmount() == null
        || exchangeRequest.getProvidedAmount() > Double.valueOf(
        exchangeConfigs.getMaxExchangeAmountAllowed())) {
      throw new InvalidParameterException("Invalid request amount");
    }
  }

}
