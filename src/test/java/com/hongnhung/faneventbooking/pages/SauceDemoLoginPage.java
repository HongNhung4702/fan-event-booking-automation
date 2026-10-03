package com.hongnhung.faneventbooking.pages;

import com.hongnhung.faneventbooking.config.ConfigManager;
import com.hongnhung.faneventbooking.driver.DriverManager;
import org.openqa.selenium.By;

public class SauceDemoLoginPage extends BasePage {

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");

    private final By loginError =
            By.cssSelector("[data-test='error']");

    private final By productsTitle =
            By.cssSelector(".title");

    public SauceDemoLoginPage(DriverManager driverManager) {
        super(driverManager);
    }

    public void open() {
        navigateTo(
                ConfigManager.get("saucedemo.url")
        );
    }

    public boolean isLoginFormDisplayed() {
        return isDisplayed(usernameInput)
                && isDisplayed(passwordInput)
                && isDisplayed(loginButton);
    }

    public void enterUsername(String username) {
        type(usernameInput, username);
    }

    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    public void clickLogin() {
        click(loginButton);
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public boolean isProductsPageDisplayed() {
        return isDisplayed(productsTitle)
                && "Products".equalsIgnoreCase(
                getText(productsTitle)
        );
    }

    public String getErrorMessage() {
        return getText(loginError);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(loginError);
    }

    public boolean isStillOnLoginPage() {
        return isDisplayed(loginButton)
                && !currentUrlContains("/inventory.html");
    }

    public boolean isInventoryUrlDisplayed() {
        waitUntilUrlContains("/inventory.html");
        return currentUrlContains("/inventory.html");
    }

    public String currentUrl() {
        return getCurrentUrl();
    }
}