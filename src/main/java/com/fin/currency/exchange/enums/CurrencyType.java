package com.fin.currency.exchange.enums;

public enum CurrencyType {
  INR("INR"),
  EUR("EUR"),
  AUD("AUD"),
  USD("USD"),
  KWD("KWD"),
  SEK("SEK");

  private String name;

  public String getCurrencyName() {
    return this.name;
  }

  private CurrencyType(String name) {
    this.name = name;
  }
}
