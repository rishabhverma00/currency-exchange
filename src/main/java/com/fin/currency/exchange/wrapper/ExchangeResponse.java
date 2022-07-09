package com.fin.currency.exchange.wrapper;

public class ExchangeResponse {

  private String exchangedCurrencyType;
  private Double exchangedAmount;
  private Double exchangeRate;
  private String providedCurrencyType;
  private Double providedAmount;
  private Status status;

  public String getExchangedCurrencyType() {
    return exchangedCurrencyType;
  }

  public void setExchangedCurrencyType(String exchangedCurrencyType) {
    this.exchangedCurrencyType = exchangedCurrencyType;
  }

  public Double getExchangedAmount() {
    return exchangedAmount;
  }

  public void setExchangedAmount(Double exchangedAmount) {
    this.exchangedAmount = exchangedAmount;
  }

  public Double getExchangeRate() {
    return exchangeRate;
  }

  public void setExchangeRate(Double exchangeRate) {
    this.exchangeRate = exchangeRate;
  }

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

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public static class Status{
    private String code;
    private String message;

    public String getCode() {
      return code;
    }

    public void setCode(String code) {
      this.code = code;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }
}
