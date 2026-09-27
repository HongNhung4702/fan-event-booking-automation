package com.hongnhung.faneventbooking.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "Không tìm thấy file config.properties."
                );
            }

            PROPERTIES.load(input);

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Không thể đọc file config.properties.",
                    exception
            );
        }
    }

    private ConfigManager() {
    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);

        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }

        String environmentKey = key
                .toUpperCase()
                .replace(".", "_");

        String environmentValue = System.getenv(environmentKey);

        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue.trim();
        }

        String propertyValue = PROPERTIES.getProperty(key);

        if (propertyValue == null || propertyValue.isBlank()) {
            throw new IllegalStateException(
                    "Không tìm thấy cấu hình: " + key
            );
        }

        return propertyValue.trim();
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static long getLong(String key) {
        try {
            return Long.parseLong(get(key));
        } catch (NumberFormatException exception) {
            throw new IllegalStateException(
                    "Cấu hình phải là số: " + key,
                    exception
            );
        }
    }
}