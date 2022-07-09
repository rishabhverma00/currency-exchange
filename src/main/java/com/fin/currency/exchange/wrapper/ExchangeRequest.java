package com.fin.currency.exchange.wrapper;

public class ExchangeRequest {

  public String providedCurrencyType;
  public Double providedAmount;
  public String exchangeCurrencyType;

  public String getProvidedCurrencyType() {
    return providedCurrencyType;
  }

  public void setProvidedCurrencyType(String providedCurrencyType) {
    this.providedCurrencyType = providedCurrencyType;
  }

  public Double getProvidedAmount() {
    return providedAmount;
  }

  public void setProvidedAmount(Double providedAmount) {
    this.providedAmount = providedAmount;
  }

  public String getExchangeCurrencyType() {
    return exchangeCurrencyType;
  }

  public void setExchangeCurrencyType(String exchangeCurrencyType) {
    this.exchangeCurrencyType = exchangeCurrencyType;
  }

  @Override
  public String toString() {
    return "ExchangeRequest{" +
        "providedCurrencyType='" + providedCurrencyType + '\'' +
        ", providedAmount=" + providedAmount +
        ", exchangeCurrencyType='" + exchangeCurrencyType + '\'' +
        '}';
  }
}
