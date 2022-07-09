package com.fin.currency.exchange.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ExchangeRates")
public class ExchangeRates {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "provided_currency")
  private String providedCurrencyType;
  @Column(name = "exchange_currency")
  private String exchangedCurrencyType;
  @Column(name = "exchange_rate")
  private Double exchangeRate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getProvidedCurrencyType() {
    return providedCurrencyType;
  }

  public void setProvidedCurrencyType(String providedCurrencyType) {
    this.providedCurrencyType = providedCurrencyType;
  }

  public String getExchangedCurrencyType() {
    return exchangedCurrencyType;
  }

  public void setExchangedCurrencyType(String exchangedCurrencyType) {
    this.exchangedCurrencyType = exchangedCurrencyType;
  }

  public Double getExchangeRate() {
    return exchangeRate;
  }

  public void setExchangeRate(Double exchnageRate) {
    this.exchangeRate = exchnageRate;
  }

  @Override
  public String toString() {
    return "ExchangeRates{" +
        "id=" + id +
        ", providedCurrencyType='" + providedCurrencyType + '\'' +
        ", exchangedCurrencyType='" + exchangedCurrencyType + '\'' +
        ", exchangeRate=" + exchangeRate +
        '}';
  }
}
