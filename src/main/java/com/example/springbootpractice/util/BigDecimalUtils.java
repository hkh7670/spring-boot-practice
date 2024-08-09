package com.example.springbootpractice.util;

import java.math.BigDecimal;

public class BigDecimalUtils {


  public static boolean isGreaterThan(BigDecimal a, BigDecimal b) {
    return a.compareTo(b) > 0;
  }

  public static boolean isLessThan(BigDecimal a, BigDecimal b) {
    return a.compareTo(b) < 0;
  }

  public static boolean isEqual(BigDecimal a, BigDecimal b) {
    return a.compareTo(b) == 0;
  }

  public static boolean isGreaterOrEqual(BigDecimal a, BigDecimal b) {
    return a.compareTo(b) >= 0;
  }

  public static boolean isLessOrEqual(BigDecimal a, BigDecimal b) {
    return a.compareTo(b) <= 0;
  }

}
