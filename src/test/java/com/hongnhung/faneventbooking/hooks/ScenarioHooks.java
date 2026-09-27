package com.hongnhung.faneventbooking.hooks;

import com.hongnhung.faneventbooking.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;

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
    public void afterScenario() {
        driverManager.quitDriver();
    }
}