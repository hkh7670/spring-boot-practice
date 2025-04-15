package com.example.springbootpractice.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CommonUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String getRequestId() {
        String now = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        return now + "-" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

}
