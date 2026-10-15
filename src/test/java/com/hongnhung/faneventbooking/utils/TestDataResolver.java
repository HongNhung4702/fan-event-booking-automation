package com.hongnhung.faneventbooking.utils;

public final class TestDataResolver {

    private TestDataResolver() {
    }

    public static String resolve(String value) {

        if (value == null) {
            return "";
        }

        if (value.startsWith("ENV_")) {

            String environmentValue =
                    System.getenv(value);

            if (environmentValue == null
                    || environmentValue.isBlank()) {

                throw new IllegalStateException(
                        "Không tìm thấy biến môi trường: "
                                + value
                );
            }

            return environmentValue;
        }

        return switch (value) {

            case "LONG_256_A" ->
                    "A".repeat(256);

            default ->
                    value;
        };
    }
}