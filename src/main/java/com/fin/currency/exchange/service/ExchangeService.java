package com.fin.currency.exchange.service;

import com.fin.currency.exchange.wrapper.ExchangeRequest;
import com.fin.currency.exchange.wrapper.ExchangeResponse;

public interface ExchangeService {

  public ExchangeResponse exchange(ExchangeRequest exchangeRequest);
}
