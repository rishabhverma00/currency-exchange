package com.fin.currency.exchange.wrapper;

import java.util.Map;

public class LiveExchangeRates {

  private String base;
  private String date;
  private Map<String,Double> rates;
  private Boolean success;
  private Long timestamp;

  public String getBase() {
    return base;
  }

  public void setBase(String base) {
    this.base = base;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public Map<String, Double> getRates() {
    return rates;
  }

  public void setRates(Map<String, Double> rates) {
    this.rates = rates;
  }

  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "LiveExchangeRates{" +
        "base='" + base + '\'' +
        ", date='" + date + '\'' +
        ", rates=" + rates +
        ", success=" + success +
        ", timestamp=" + timestamp +
        '}';
  }
}
