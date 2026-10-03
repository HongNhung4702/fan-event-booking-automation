package com.hongnhung.faneventbooking.hooks;

import com.hongnhung.faneventbooking.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScenarioHooks {

    private final DriverManager driverManager;

    public ScenarioHooks(DriverManager driverManager) {
        this.driverManager = driverManager;
    }

    @Before
    public void beforeScenario() {
        driverManager.startDriver();
    }

    @After
    public void afterScenario(Scenario scenario) {

        try {

            if (scenario.isFailed()
                    && driverManager.isStarted()) {

                WebDriver driver =
                        driverManager.getDriver();

                byte[] screenshot =
                        ((TakesScreenshot) driver)
                                .getScreenshotAs(
                                        OutputType.BYTES
                                );

                scenario.attach(
                        screenshot,
                        "image/png",
                        "Screenshot - "
                                + scenario.getName()
                );
            }

        } finally {

            driverManager.quitDriver();
        }
    }
}