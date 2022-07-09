package com.fin.currency.exchange.controller;

import com.fin.currency.exchange.wrapper.ExchangeRequest;
import com.fin.currency.exchange.service.ExchangeService;
import com.fin.currency.exchange.wrapper.ExchangeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeController {

  private ExchangeService exchangeService;

  @Autowired
  public ExchangeController(ExchangeService exchangeService) {
    this.exchangeService = exchangeService;
  }

  @PostMapping("exchange")
  public ExchangeResponse exchange(@RequestBody ExchangeRequest exchangeRequest) {
    return exchangeService.exchange(exchangeRequest);
  }

}
