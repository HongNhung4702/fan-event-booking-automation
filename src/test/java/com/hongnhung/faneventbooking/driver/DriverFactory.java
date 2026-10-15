package com.hongnhung.faneventbooking.driver;

import com.hongnhung.faneventbooking.config.ConfigManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        /*
         * Automation Exercise có nhiều tài nguyên
         * quảng cáo và tài nguyên bên thứ ba.
         *
         * EAGER cho phép Selenium tiếp tục sau khi
         * DOMContentLoaded thay vì chờ toàn bộ tài nguyên.
         *
         * Page Object vẫn dùng Explicit Wait và
         * Page Ready Retry để chờ đúng element cần thiết.
         */
        options.setPageLoadStrategy(
                PageLoadStrategy.EAGER
        );

        if (headless) {

            options.addArguments(
                    "--headless=new"
            );
        }

        options.addArguments(
                "--disable-notifications",
                "--disable-extensions"
        );

        options.addArguments(
                "--host-resolver-rules="
                        + "MAP googleads.g.doubleclick.net 0.0.0.0, "
                        + "MAP securepubads.g.doubleclick.net 0.0.0.0, "
                        + "MAP pagead2.googlesyndication.com 0.0.0.0, "
                        + "MAP googlesyndication.com 0.0.0.0, "
                        + "MAP tpc.googlesyndication.com 0.0.0.0, "
                        + "MAP adservice.google.com 0.0.0.0, "
                        + "MAP adservice.google.com.vn 0.0.0.0"
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

        prefs.put(
                "download.default_directory",
                getDownloadDirectory("chrome")
        );

        prefs.put(
                "download.prompt_for_download",
                false
        );

        prefs.put(
                "download.directory_upgrade",
                true
        );

        prefs.put(
                "safebrowsing.enabled",
                true
        );

        options.setExperimentalOption(
                "prefs",
                prefs
        );

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(
            boolean headless
    ) {

        FirefoxOptions options =
                new FirefoxOptions();

        options.setPageLoadStrategy(
                PageLoadStrategy.EAGER
        );

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

        options.addPreference(
                "browser.download.folderList",
                2
        );

        options.addPreference(
                "browser.download.useDownloadDir",
                true
        );

        options.addPreference(
                "browser.download.dir",
                getDownloadDirectory(
                        "firefox"
                )
        );

        options.addPreference(
                "browser.download.manager.showWhenStarting",
                false
        );

        options.addPreference(
                "browser.helperApps.neverAsk.saveToDisk",
                "text/plain,"
                        + "application/octet-stream,"
                        + "application/pdf"
        );

        options.addPreference(
                "pdfjs.disabled",
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

        options.setPageLoadStrategy(
                PageLoadStrategy.EAGER
        );

        if (headless) {

            options.addArguments(
                    "--headless=new"
            );
        }

        options.addArguments(
                "--disable-notifications",
                "--disable-extensions"
        );

        options.addArguments(
                "--host-resolver-rules="
                        + "MAP googleads.g.doubleclick.net 0.0.0.0, "
                        + "MAP securepubads.g.doubleclick.net 0.0.0.0, "
                        + "MAP pagead2.googlesyndication.com 0.0.0.0, "
                        + "MAP googlesyndication.com 0.0.0.0, "
                        + "MAP tpc.googlesyndication.com 0.0.0.0, "
                        + "MAP adservice.google.com 0.0.0.0, "
                        + "MAP adservice.google.com.vn 0.0.0.0"
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

        prefs.put(
                "download.default_directory",
                getDownloadDirectory(
                        "edge"
                )
        );

        prefs.put(
                "download.prompt_for_download",
                false
        );

        prefs.put(
                "download.directory_upgrade",
                true
        );

        prefs.put(
                "safebrowsing.enabled",
                true
        );

        options.setExperimentalOption(
                "prefs",
                prefs
        );

        return new EdgeDriver(
                options
        );
    }

    public static String getDownloadDirectory(
            String browser
    ) {

        Path downloadDirectory =
                Paths.get(
                                "target",
                                "downloads",
                                browser.toLowerCase()
                        )
                        .toAbsolutePath()
                        .normalize();

        try {

            Files.createDirectories(
                    downloadDirectory
            );

        } catch (
                IOException e
        ) {

            throw new IllegalStateException(
                    "Không thể tạo thư mục download: "
                            + downloadDirectory,
                    e
            );
        }

        return downloadDirectory.toString();
    }
}