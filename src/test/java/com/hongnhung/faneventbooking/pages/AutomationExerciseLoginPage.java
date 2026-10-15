package com.hongnhung.faneventbooking.pages;

import com.hongnhung.faneventbooking.config.ConfigManager;
import com.hongnhung.faneventbooking.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

public class AutomationExerciseLoginPage
        extends BasePage {

    private final By homeReady =
            By.cssSelector(
                    "a[href='/products']"
            );

    private final By loginTitle =
            By.xpath(
                    "//h2[normalize-space()='Login to your account']"
            );

    private final By emailInput =
            By.cssSelector(
                    "input[data-qa='login-email']"
            );

    private final By passwordInput =
            By.cssSelector(
                    "input[data-qa='login-password']"
            );

    private final By loginButton =
            By.cssSelector(
                    "button[data-qa='login-button']"
            );

    private final By loggedInAs =
            By.xpath(
                    "//a[contains(., 'Logged in as')]"
            );

    private final By loginError =
            By.xpath(
                    "//form[@action='/login']//p"
            );

    public AutomationExerciseLoginPage(
            DriverManager driverManager
    ) {

        super(driverManager);
    }

    public void openHomePage() {

        navigateToAndWaitUntilReady(
                getBaseUrl(),
                homeReady,
                "Home"
        );
    }

    public void openLoginPage() {

        navigateToAndWaitUntilReady(
                getBaseUrl()
                        + "/login",
                loginTitle,
                "Login"
        );
    }

    public boolean isLoginPageDisplayed() {

        try {

            return waitUntilVisible(
                    loginTitle
            ).isDisplayed();

        } catch (
                TimeoutException exception
        ) {

            return false;
        }
    }

    public void enterEmail(
            String email
    ) {

        type(
                emailInput,
                email
        );
    }

    public void enterPassword(
            String password
    ) {

        type(
                passwordInput,
                password
        );
    }

    public void clickLogin() {

        click(
                loginButton
        );
    }

    public void login(
            String email,
            String password
    ) {

        openLoginPage();

        enterEmail(
                email
        );

        enterPassword(
                password
        );

        clickLogin();
    }

    public boolean isLoggedIn() {

        try {

            return waitUntilVisible(
                    loggedInAs
            ).isDisplayed();

        } catch (
                TimeoutException exception
        ) {

            return false;
        }
    }

    public String getLoggedInText() {

        return getText(
                loggedInAs
        );
    }

    public boolean isLoginErrorDisplayed() {

        try {

            return waitUntilVisible(
                    loginError
            ).isDisplayed();

        } catch (
                TimeoutException exception
        ) {

            return false;
        }
    }

    public String getLoginErrorMessage() {

        return getText(
                loginError
        );
    }

    private String getBaseUrl() {

        String url =
                ConfigManager.get(
                        "automationexercise.url"
                );

        while (
                url.endsWith("/")
        ) {

            url =
                    url.substring(
                            0,
                            url.length() - 1
                    );
        }

        return url;
    }
}