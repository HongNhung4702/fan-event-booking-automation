
package com.hongnhung.faneventbooking.driver;

import com.hongnhung.faneventbooking.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {

        String browser = System.getProperty(
                "browser",
                ConfigManager.get("browser")
        ).trim().toLowerCase(Locale.ROOT);

        boolean headless = ConfigManager.getBoolean(
                "browser.headless"
        );

        return switch (browser) {

            case "chrome" -> createChromeDriver(headless);

            case "firefox" -> createFirefoxDriver(headless);

            case "edge" -> createEdgeDriver(headless);

            default -> throw new IllegalArgumentException(
                    "Browser không được hỗ trợ: " + browser
            );
        };
    }

    // =====================================================
    // CHROME DRIVER
    // =====================================================

    private static WebDriver createChromeDriver(
            boolean headless
    ) {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        // Chặn các domain quảng cáo
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

        Map<String, Object> prefs = new HashMap<>();

        prefs.put(
                "profile.default_content_setting_values.notifications",
                2
        );

        prefs.put(
                "profile.default_content_setting_values.popups",
                2
        );

        options.setExperimentalOption("prefs", prefs);

        return new ChromeDriver(options);
    }

    // =====================================================
    // FIREFOX DRIVER
    // =====================================================

    private static WebDriver createFirefoxDriver(
            boolean headless
    ) {

        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();

        /*
         * EAGER:
         * Selenium không cần đợi toàn bộ hình ảnh,
         * quảng cáo và tài nguyên phụ tải hoàn tất.
         *
         * Giúp giảm nguy cơ Navigation Timeout.
         */
        options.setPageLoadStrategy(
                PageLoadStrategy.EAGER
        );

        if (headless) {
            options.addArguments("-headless");
        }

        // Tắt thông báo
        options.addPreference(
                "dom.webnotifications.enabled",
                false
        );

        // Chặn cửa sổ popup
        options.addPreference(
                "dom.disable_open_during_load",
                true
        );

        // Enhanced Tracking Protection
        options.addPreference(
                "browser.contentblocking.category",
                "strict"
        );

        options.addPreference(
                "privacy.trackingprotection.enabled",
                true
        );

        // Chặn tracking cookies
        options.addPreference(
                "privacy.trackingprotection.socialtracking.enabled",
                true
        );

        return new FirefoxDriver(options);
    }

    // =====================================================
    // EDGE DRIVER
    // =====================================================

    private static WebDriver createEdgeDriver(
            boolean headless
    ) {

        WebDriverManager.edgedriver().setup();

        EdgeOptions options = new EdgeOptions();

        if (headless) {
            options.addArguments("--headless=new");
        }

        // Chặn domain quảng cáo
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

        return new EdgeDriver(options);
    }
}
