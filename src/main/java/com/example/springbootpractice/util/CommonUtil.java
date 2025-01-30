package com.example.springbootpractice.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CommonUtil {

  public static String getRequestId() {
    String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    return now + "-" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
  }

}
