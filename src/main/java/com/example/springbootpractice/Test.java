package com.example.springbootpractice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        BigDecimal bigDecimal1 = new BigDecimal("10.421512421453113");
        BigDecimal bigDecimal2 = new BigDecimal("10.421512421453113000");
        BigDecimal bigDecimal3 = BigDecimal.valueOf(10.421512421453113000);
        BigDecimal bigDecimal5 = new BigDecimal("10.421512421453113000");
        System.out.println(bigDecimal3);
        System.out.println(bigDecimal5);
        float f = 1.1F;
        BigDecimal bigDecimal4 = BigDecimal.valueOf(f);
        System.out.println(bigDecimal4);
        System.out.println(bigDecimal1.equals(bigDecimal2));
        System.out.println(bigDecimal1.compareTo(bigDecimal2));

        Object a = new Object();
        List<Integer> al1 = new ArrayList<>();
        al1.add(1);
        List<Integer> al2 = new ArrayList<>();
        al2.add(1);

        System.out.println(al1.equals(al2)); // -> true

    }

}
