package com.example.springbootpractice.util;

import org.springframework.util.StringUtils;

public class CustomStringUtils {

    public static boolean hasNotText(String str) {
        return !StringUtils.hasText(str);
    }

    public static String getMaskedData(String data) {
        if (hasNotText(data)) {
            return "";
        }
        return "*".repeat(data.length());
    }
}
