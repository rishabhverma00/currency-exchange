package com.fin.currency.exchange.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtils {
  public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
  public static final String UNDER_SCORE = "_";

  public static String getHashKey(String baseCurrencyType,int days){
    String dateTimeString = LocalDateTime.now().minusDays(days).format(CommonUtils.formatter);
    return baseCurrencyType + UNDER_SCORE + dateTimeString;
  }

}
