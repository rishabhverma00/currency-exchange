package com.fin.currency.exchange.dto;

import com.fin.currency.exchange.model.ExchangeRates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateDto extends JpaRepository<ExchangeRates, Long> {

  public ExchangeRates getExchangeRatesByExchangedCurrencyTypeAndAndProvidedCurrencyType(
      String exchangeCurrencyType, String providedCurrencyType);

}
