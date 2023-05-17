package com.mindhub.homebanking.utils;

public final class AccountUtils {
    public static <T extends Enum<T>> boolean validEnum(String valor, Class<T> enumClass) {
        try {
            Enum.valueOf(enumClass, valor);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
