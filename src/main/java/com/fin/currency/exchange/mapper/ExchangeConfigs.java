package com.fin.currency.exchange.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("exchange")
public class ExchangeConfigs {

  @Value("maxExchangeAmountAllowed")
  private String maxExchangeAmountAllowed;
  @Value("exchangeRatesUrl")
  private String exchangeRatesUrl;
  @Value("exchangeApiKey")
  private String exchangeApiKey;
  @Value("exchangeApiValue")
  private String exchangeApiValue;

  public String getExchangeRatesUrl() {
    return exchangeRatesUrl;
  }

  public void setExchangeRatesUrl(String exchangeRatesUrl) {
    this.exchangeRatesUrl = exchangeRatesUrl;
  }

  public String getExchangeApiKey() {
    return exchangeApiKey;
  }

  public void setExchangeApiKey(String exchangeApiKey) {
    this.exchangeApiKey = exchangeApiKey;
  }

  public String getExchangeApiValue() {
    return exchangeApiValue;
  }

  public void setExchangeApiValue(String exchangeApiValue) {
    this.exchangeApiValue = exchangeApiValue;
  }

  public String getMaxExchangeAmountAllowed() {
    return maxExchangeAmountAllowed;
  }

  public void setMaxExchangeAmountAllowed(String maxExchangeAmountAllowed) {
    this.maxExchangeAmountAllowed = maxExchangeAmountAllowed;
  }
}
