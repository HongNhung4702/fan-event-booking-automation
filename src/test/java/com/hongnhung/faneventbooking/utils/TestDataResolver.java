package com.hongnhung.faneventbooking.utils;

public final class TestDataResolver {

    private TestDataResolver() {
    }

    public static String resolve(String value) {

        if (value == null) {
            return "";
        }

        return switch (value) {

            case "LONG_256_A" ->
                    "A".repeat(256);

            default ->
                    value;
        };
    }
}