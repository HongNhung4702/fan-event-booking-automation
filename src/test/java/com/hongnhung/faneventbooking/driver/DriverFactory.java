package com.hongnhung.faneventbooking.driver;

import com.hongnhung.faneventbooking.config.ConfigManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {

        String browser =
                ConfigManager.get("browser")
                        .toLowerCase();

        boolean headless =
                ConfigManager.getBoolean(
                        "browser.headless"
                );

        return switch (browser) {

            case "chrome" ->
                    createChromeDriver(headless);

            case "firefox" ->
                    createFirefoxDriver(headless);

            case "edge" ->
                    createEdgeDriver(headless);

            default ->
                    throw new IllegalArgumentException(
                            "Browser không được hỗ trợ: "
                                    + browser
                    );
        };
    }

    private static WebDriver createChromeDriver(
            boolean headless
    ) {

        ChromeOptions options =
                new ChromeOptions();

        if (headless) {

            options.addArguments(
                    "--headless=new"
            );
        }

        /*
         * Chặn các domain quảng cáo thường xuất hiện
         * trên Automation Exercise.
         */
        options.addArguments(
                "--host-resolver-rules=" +
                        "MAP googleads.g.doubleclick.net 0.0.0.0, " +
                        "MAP securepubads.g.doubleclick.net 0.0.0.0, " +
                        "MAP pagead2.googlesyndication.com 0.0.0.0, " +
                        "MAP googlesyndication.com 0.0.0.0, " +
                        "MAP tpc.googlesyndication.com 0.0.0.0, " +
                        "MAP adservice.google.com 0.0.0.0, " +
                        "MAP adservice.google.com.vn 0.0.0.0"
        );

        options.addArguments(
                "--disable-notifications",
                "--disable-extensions"
        );

        Map<String, Object> prefs =
                new HashMap<>();

        prefs.put(
                "profile.default_content_setting_values.notifications",
                2
        );

        prefs.put(
                "profile.default_content_setting_values.popups",
                2
        );

        options.setExperimentalOption(
                "prefs",
                prefs
        );

        return new ChromeDriver(
                options
        );
    }

    private static WebDriver createFirefoxDriver(
            boolean headless
    ) {

        FirefoxOptions options =
                new FirefoxOptions();

        if (headless) {

            options.addArguments(
                    "-headless"
            );
        }

        options.addPreference(
                "dom.webnotifications.enabled",
                false
        );

        options.addPreference(
                "dom.disable_open_during_load",
                true
        );

        return new FirefoxDriver(
                options
        );
    }

    private static WebDriver createEdgeDriver(
            boolean headless
    ) {

        EdgeOptions options =
                new EdgeOptions();

        if (headless) {

            options.addArguments(
                    "--headless=new"
            );
        }

        options.addArguments(
                "--host-resolver-rules=" +
                        "MAP googleads.g.doubleclick.net 0.0.0.0, " +
                        "MAP securepubads.g.doubleclick.net 0.0.0.0, " +
                        "MAP pagead2.googlesyndication.com 0.0.0.0, " +
                        "MAP googlesyndication.com 0.0.0.0, " +
                        "MAP tpc.googlesyndication.com 0.0.0.0, " +
                        "MAP adservice.google.com 0.0.0.0, " +
                        "MAP adservice.google.com.vn 0.0.0.0"
        );

        options.addArguments(
                "--disable-notifications",
                "--disable-extensions"
        );

        return new EdgeDriver(
                options
        );
    }
}