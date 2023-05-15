package com.mindhub.homebanking.utils;

import java.util.Random;

public final class CardUtils {
    private CardUtils() {
    }
    //    Number generator methods
    public static int randomCvv() {
        int num = (int) ((Math.random() * 899) + 100);
        return num;
    }

    public static String randomNumber() {
        Random randomNum = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            int num = randomNum.nextInt(10000);
            sb.append(String.format("%04d", num));
            if (i < 3) {
                sb.append("-");
            }
        }
        return sb.toString();
    }
}
