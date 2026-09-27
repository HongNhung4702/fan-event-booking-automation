package com.hongnhung.faneventbooking.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.hongnhung.faneventbooking",
        plugin = {
                "pretty"
        }
)
public class RunCucumberTest extends AbstractTestNGCucumberTests {
}