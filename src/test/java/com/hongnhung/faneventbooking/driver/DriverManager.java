package com.hongnhung.faneventbooking.driver;

import com.hongnhung.faneventbooking.config.ConfigManager;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class DriverManager {

    private WebDriver driver;

    public void startDriver() {
        if (driver != null) {
            return;
        }

        driver = DriverFactory.createDriver();

        driver.manage()
                .timeouts()
                .implicitlyWait(Duration.ZERO);

        driver.manage()
                .timeouts()
                .pageLoadTimeout(
                        Duration.ofSeconds(
                                ConfigManager.getLong("timeout.page-load")
                        )
                );

        if (ConfigManager.getBoolean("browser.maximize")) {
            driver.manage().window().maximize();
        }
    }

    public WebDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver chưa được khởi tạo."
            );
        }

        return driver;
    }

    public boolean isStarted() {
        return driver != null;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}