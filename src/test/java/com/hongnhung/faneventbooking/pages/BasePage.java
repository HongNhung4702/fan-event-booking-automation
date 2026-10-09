package com.hongnhung.faneventbooking.pages;

import com.hongnhung.faneventbooking.config.ConfigManager;
import com.hongnhung.faneventbooking.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(DriverManager driverManager) {
        this.driver = driverManager.getDriver();

        PageFactory.initElements(driver, this);

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(
                        ConfigManager.getLong("timeout.explicit")
                )
        );
    }

    protected WebElement waitUntilVisible(By locator) {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    protected WebElement waitUntilClickable(By locator) {
        return wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        );
    }

    protected void click(By locator) {
        waitUntilClickable(locator).click();
    }

    protected void type(By locator, String value) {
        WebElement element = waitUntilVisible(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected String getText(By locator) {
        return waitUntilVisible(locator).getText().trim();
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitUntilVisible(locator).isDisplayed();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    protected WebElement waitUntilVisible(WebElement element) {
        return wait.until(
                ExpectedConditions.visibilityOf(element)
        );
    }

    protected WebElement waitUntilClickable(WebElement element) {
        return wait.until(
                ExpectedConditions.elementToBeClickable(element)
        );
    }

    protected void click(WebElement element) {
        waitUntilClickable(element).click();
    }

    protected void type(WebElement element, String value) {
        WebElement visible = waitUntilVisible(element);
        visible.clear();
        visible.sendKeys(value);
    }

    protected String getText(WebElement element) {
        return waitUntilVisible(element).getText().trim();
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return waitUntilVisible(element).isDisplayed();
        } catch (org.openqa.selenium.TimeoutException |
                 org.openqa.selenium.NoSuchElementException exception) {
            return false;
        }
    }

    protected void navigateTo(String url) {
        driver.get(url);
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected boolean currentUrlContains(String value) {
        return getCurrentUrl().contains(value);
    }

    protected void waitUntilUrlContains(String value) {
        wait.until(
                ExpectedConditions.urlContains(value)
        );
    }
}