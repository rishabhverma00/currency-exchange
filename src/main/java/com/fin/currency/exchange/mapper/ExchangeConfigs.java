package com.fin.currency.exchange.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("exchange")
public class ExchangeConfigs {

  @Value("maxExchangeAmountAllowed")
  private String maxExchangeAmountAllowed;

  public String getMaxExchangeAmountAllowed() {
    return maxExchangeAmountAllowed;
  }

  public void setMaxExchangeAmountAllowed(String maxExchangeAmountAllowed) {
    this.maxExchangeAmountAllowed = maxExchangeAmountAllowed;
  }
}
