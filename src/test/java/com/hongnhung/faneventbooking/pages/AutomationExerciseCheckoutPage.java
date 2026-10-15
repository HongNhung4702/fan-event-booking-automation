package com.hongnhung.faneventbooking.pages;

import com.hongnhung.faneventbooking.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

public class AutomationExerciseCheckoutPage extends BasePage {

    private final By deliveryAddress =
            By.id("address_delivery");

    private final By billingAddress =
            By.id("address_invoice");

    private final By reviewOrderTitle =
            By.xpath(
                    "//h2[contains(@class,'heading') " +
                            "and contains(normalize-space(),'Review Your Order')]"
            );

    private final By placeOrderButton =
            By.cssSelector(
                    "a[href='/payment']"
            );

    private final By paymentTitle =
            By.xpath(
                    "//h2[contains(normalize-space(),'Payment')]"
            );

    private final By cardNameInput =
            By.cssSelector(
                    "input[data-qa='name-on-card']"
            );

    private final By cardNumberInput =
            By.cssSelector(
                    "input[data-qa='card-number']"
            );

    private final By cvcInput =
            By.cssSelector(
                    "input[data-qa='cvc']"
            );

    private final By expiryMonthInput =
            By.cssSelector(
                    "input[data-qa='expiry-month']"
            );

    private final By expiryYearInput =
            By.cssSelector(
                    "input[data-qa='expiry-year']"
            );

    private final By payButton =
            By.cssSelector(
                    "button[data-qa='pay-button']"
            );

    private final By orderPlacedTitle =
            By.cssSelector(
                    "[data-qa='order-placed']"
            );

    private final By orderSuccessMessage =
            By.xpath(
                    "//*[contains(normalize-space()," +
                            "'Congratulations! Your order has been confirmed!')]"
            );

    private final By downloadInvoiceButton =
            By.xpath(
                    "//a[contains(normalize-space()," +
                            "'Download Invoice')]"
            );

    private final By continueButton =
            By.cssSelector(
                    "a[data-qa='continue-button']"
            );

    public AutomationExerciseCheckoutPage(
            DriverManager driverManager
    ) {
        super(driverManager);
    }

    public boolean isCheckoutPageDisplayed() {

        try {

            return driver.getCurrentUrl()
                    .contains("/checkout")

                    && waitUntilVisible(
                    deliveryAddress
            ).isDisplayed()

                    && waitUntilVisible(
                    billingAddress
            ).isDisplayed();

        } catch (TimeoutException e) {

            return false;
        }
    }

    public boolean isDeliveryAddressDisplayed() {

        try {

            return waitUntilVisible(
                    deliveryAddress
            ).isDisplayed();

        } catch (TimeoutException e) {

            return false;
        }
    }

    public boolean isBillingAddressDisplayed() {

        try {

            return waitUntilVisible(
                    billingAddress
            ).isDisplayed();

        } catch (TimeoutException e) {

            return false;
        }
    }

    public boolean isReviewOrderDisplayed() {

        try {

            return waitUntilVisible(
                    reviewOrderTitle
            ).isDisplayed();

        } catch (TimeoutException e) {

            return false;
        }
    }

    public String getDeliveryAddressText() {

        return normalizeText(
                getText(
                        deliveryAddress
                )
        );
    }

    public String getBillingAddressText() {

        return normalizeText(
                getText(
                        billingAddress
                )
        );
    }

    public boolean deliveryAddressContains(
            String expectedValue
    ) {

        return containsNormalized(
                getDeliveryAddressText(),
                expectedValue
        );
    }

    public boolean billingAddressContains(
            String expectedValue
    ) {

        return containsNormalized(
                getBillingAddressText(),
                expectedValue
        );
    }

    public void clickPlaceOrder() {

        WebElement button =
                waitUntilVisible(
                        placeOrderButton
                );

        safeClick(button);

        waitUntilVisible(
                cardNameInput
        );
    }

    public boolean isPaymentPageDisplayed() {

        try {

            return driver.getCurrentUrl()
                    .contains("/payment")

                    && waitUntilVisible(
                    cardNameInput
            ).isDisplayed();

        } catch (TimeoutException e) {

            return false;
        }
    }

    public void enterPaymentInformation(
            String cardName,
            String cardNumber,
            String cvc,
            String expiryMonth,
            String expiryYear
    ) {

        type(
                cardNameInput,
                cardName
        );

        type(
                cardNumberInput,
                cardNumber
        );

        type(
                cvcInput,
                cvc
        );

        type(
                expiryMonthInput,
                expiryMonth
        );

        type(
                expiryYearInput,
                expiryYear
        );
    }

    public void confirmPayment() {

        WebElement button =
                waitUntilVisible(
                        payButton
                );

        safeClick(button);

        waitUntilVisible(
                orderPlacedTitle
        );
    }

    public boolean isOrderPlacedSuccessfully() {

        try {

            return waitUntilVisible(
                    orderPlacedTitle
            ).isDisplayed();

        } catch (TimeoutException e) {

            return false;
        }
    }

    public boolean isOrderSuccessMessageDisplayed() {

        try {

            return waitUntilVisible(
                    orderSuccessMessage
            ).isDisplayed();

        } catch (TimeoutException e) {

            return false;
        }
    }

    public String getOrderPlacedText() {

        return normalizeText(
                getText(
                        orderPlacedTitle
                )
        );
    }

    public void downloadInvoice() {

        WebElement button =
                waitUntilVisible(
                        downloadInvoiceButton
                );

        safeClick(button);
    }

    public boolean isDownloadInvoiceButtonDisplayed() {

        try {

            return waitUntilVisible(
                    downloadInvoiceButton
            ).isDisplayed();

        } catch (TimeoutException e) {

            return false;
        }
    }

    public void clickContinue() {

        WebElement button =
                waitUntilVisible(
                        continueButton
                );

        safeClick(button);
    }

    private boolean containsNormalized(
            String actual,
            String expected
    ) {

        if (expected == null
                || expected.isBlank()) {

            return true;
        }

        String normalizedActual =
                normalizeText(actual)
                        .toLowerCase();

        String normalizedExpected =
                normalizeText(expected)
                        .toLowerCase();

        return normalizedActual.contains(
                normalizedExpected
        );
    }

    private void safeClick(
            WebElement element
    ) {

        scrollToElement(element);

        try {

            element.click();

        } catch (
                ElementClickInterceptedException e
        ) {

            ((JavascriptExecutor) driver)
                    .executeScript(
                            "arguments[0].click();",
                            element
                    );
        }
    }

    private void scrollToElement(
            WebElement element
    ) {

        ((JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].scrollIntoView(" +
                                "{block:'center'});",
                        element
                );
    }

    private String normalizeText(
            String text
    ) {

        if (text == null) {

            return "";
        }

        return text
                .replace(
                        '\u00A0',
                        ' '
                )
                .replaceAll(
                        "[\\u200B-\\u200D\\uFEFF]",
                        ""
                )
                .replaceAll(
                        "\\s+",
                        " "
                )
                .trim();
    }
}