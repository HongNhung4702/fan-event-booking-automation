package com.hongnhung.faneventbooking.pages;

import com.hongnhung.faneventbooking.config.ConfigManager;
import com.hongnhung.faneventbooking.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AutomationExerciseCartPage extends BasePage {

    private final By homeMenu =
            By.cssSelector("header a[href='/']");

    private final By allProductsTitle =
            By.xpath(
                    "//h2[contains(@class,'title') " +
                            "and normalize-space()='All Products']"
            );

    private final By cartModal =
            By.id("cartModal");

    private final By continueShoppingButton =
            By.cssSelector(
                    "#cartModal button.close-modal"
            );

    private final By cartRows =
            By.cssSelector(
                    "#cart_info_table tbody tr"
            );

    private final By quantityInput =
            By.id("quantity");

    private final By addToCartDetailButton =
            By.cssSelector(
                    ".product-information button.cart"
            );

    private final By productDetailName =
            By.cssSelector(
                    ".product-information h2"
            );

    private final By proceedToCheckoutButton =
            By.cssSelector(
                    "a.check_out"
            );

    private final By checkoutLoginModal =
            By.id("checkoutModal");

    private final By registerLoginLink =
            By.cssSelector(
                    "#checkoutModal a[href='/login']"
            );

    public AutomationExerciseCartPage(
            DriverManager driverManager
    ) {
        super(driverManager);
    }

    public void openHomePage() {

        navigateTo(
                getBaseUrl()
        );
    }

    public boolean isHomePageDisplayed() {

        try {

            WebElement home =
                    waitUntilVisible(
                            homeMenu
                    );

            return home.isDisplayed()
                    && driver.getCurrentUrl()
                    .contains(
                            "automationexercise.com"
                    );

        } catch (TimeoutException e) {

            return false;
        }
    }

    public void openProductsPage() {

        navigateToWithReadyCheck(
                getBaseUrl() + "/products",
                allProductsTitle,
                "Products"
        );
    }

    public boolean isProductsPageDisplayed() {

        try {

            return waitUntilVisible(
                    allProductsTitle
            ).isDisplayed();

        } catch (TimeoutException e) {

            return false;
        }
    }

    public void addProductToCart(
            String productName
    ) {

        ensureProductsPage();

        By productCard =
                getProductCardLocator(
                        productName
                );

        WebElement card =
                waitUntilVisible(
                        productCard
                );

        scrollToElement(card);

        try {

            new Actions(driver)
                    .moveToElement(card)
                    .perform();

        } catch (Exception ignored) {
        }

        By addButton =
                By.xpath(
                        getProductCardXPath(
                                productName
                        )
                                + "//div[contains(@class,'productinfo')]"
                                + "//a[contains(@class,'add-to-cart')]"
                );

        WebElement button =
                waitUntilVisible(
                        addButton
                );

        safeClick(button);

        waitUntilVisible(
                cartModal
        );

        closeCartModal();
    }

    public void openProductDetails(
            String productName
    ) {

        ensureProductsPage();

        By productCard =
                getProductCardLocator(
                        productName
                );

        WebElement card =
                waitUntilVisible(
                        productCard
                );

        scrollToElement(card);

        By viewProduct =
                By.xpath(
                        getProductCardXPath(
                                productName
                        )
                                + "//a[contains(@href,"
                                + "'/product_details/')]"
                );

        WebElement link =
                waitUntilVisible(
                        viewProduct
                );

        String detailUrl =
                link.getAttribute(
                        "href"
                );

        if (detailUrl == null
                || detailUrl.isBlank()) {

            throw new IllegalStateException(
                    "Không lấy được URL Product Detail của sản phẩm: "
                            + productName
            );
        }

        navigateToWithReadyCheck(
                detailUrl,
                productDetailName,
                "Product Detail"
        );

        waitUntilVisible(
                quantityInput
        );
    }

    public boolean isProductDetailDisplayed(
            String expectedProductName
    ) {

        try {

            String actual =
                    normalizeText(
                            getText(
                                    productDetailName
                            )
                    );

            String expected =
                    normalizeText(
                            expectedProductName
                    );

            return actual.equalsIgnoreCase(
                    expected
            );

        } catch (TimeoutException e) {

            return false;
        }
    }

    public void setQuantity(
            int quantity
    ) {

        if (quantity <= 0) {

            throw new IllegalArgumentException(
                    "Quantity phải lớn hơn 0."
            );
        }

        WebElement input =
                waitUntilVisible(
                        quantityInput
                );

        input.clear();

        input.sendKeys(
                String.valueOf(
                        quantity
                )
        );
    }

    public void addCurrentProductToCart() {

        WebElement button =
                waitUntilVisible(
                        addToCartDetailButton
                );

        safeClick(button);

        waitUntilVisible(
                cartModal
        );

        closeCartModal();
    }

    public void openCart() {

        navigateTo(
                getBaseUrl()
                        + "/view_cart"
        );

        waitForCartPage();
    }

    public boolean isCartPageDisplayed() {

        String currentUrl =
                driver.getCurrentUrl();

        return currentUrl != null
                && currentUrl.contains(
                "/view_cart"
        );
    }

    public List<String> getCartProductNames() {

        return driver.findElements(
                        By.cssSelector(
                                "#cart_info_table "
                                        + ".cart_description "
                                        + "h4 a"
                        )
                )
                .stream()
                .filter(
                        WebElement::isDisplayed
                )
                .map(
                        WebElement::getText
                )
                .map(
                        this::normalizeText
                )
                .filter(
                        name -> !name.isBlank()
                )
                .toList();
    }

    public int getCartProductCount() {

        return getCartProductNames()
                .size();
    }

    public boolean isProductInCart(
            String productName
    ) {

        String expected =
                normalizeText(
                        productName
                );

        return getCartProductNames()
                .stream()
                .anyMatch(
                        name ->
                                name.equalsIgnoreCase(
                                        expected
                                )
                );
    }

    public int getProductQuantity(
            String productName
    ) {

        By quantity =
                By.xpath(
                        getCartRowXPath(
                                productName
                        )
                                + "//td[contains(@class,"
                                + "'cart_quantity')]"
                                + "//*[self::button or self::a]"
                );

        String quantityText =
                normalizeText(
                        getText(quantity)
                );

        return Integer.parseInt(
                quantityText
        );
    }

    public void removeProduct(
            String productName
    ) {

        By row =
                By.xpath(
                        getCartRowXPath(
                                productName
                        )
                );

        waitUntilVisible(row);

        By removeButton =
                By.xpath(
                        getCartRowXPath(
                                productName
                        )
                                + "//a[contains(@class,"
                                + "'cart_quantity_delete')]"
                );

        WebElement button =
                waitUntilVisible(
                        removeButton
                );

        safeClick(button);

        getWait().until(
                ExpectedConditions
                        .invisibilityOfElementLocated(
                                row
                        )
        );
    }

    public boolean isCartEmpty() {

        return driver.findElements(
                cartRows
        ).isEmpty();
    }

    public void proceedToCheckout() {

        WebElement button =
                waitUntilVisible(
                        proceedToCheckoutButton
                );

        scrollToElement(button);

        safeClick(button);
    }

    public boolean isLoginRequiredDisplayed() {

        try {

            return waitUntilVisible(
                    checkoutLoginModal
            ).isDisplayed()

                    && waitUntilVisible(
                    registerLoginLink
            ).isDisplayed();

        } catch (TimeoutException e) {

            return false;
        }
    }

    private void navigateToWithReadyCheck(
            String url,
            By readyLocator,
            String pageName
    ) {

        int maxAttempts =
                Math.toIntExact(
                        ConfigManager.getLong(
                                "navigation.retry.max-attempts"
                        )
                );

        long retryDelay =
                ConfigManager.getLong(
                        "navigation.retry.delay-ms"
                );

        TimeoutException lastException =
                null;

        for (
                int attempt = 1;
                attempt <= maxAttempts;
                attempt++
        ) {

            navigateTo(url);

            try {

                waitUntilVisible(
                        readyLocator
                );

                return;

            } catch (TimeoutException exception) {

                lastException =
                        exception;

                System.err.printf(
                        "[PAGE READY RETRY] "
                                + "Trang %s chưa sẵn sàng. "
                                + "URL=%s, lần=%d/%d%n",
                        pageName,
                        url,
                        attempt,
                        maxAttempts
                );
            }

            if (attempt < maxAttempts) {

                sleepBeforePageRetry(
                        retryDelay
                );
            }
        }

        throw new IllegalStateException(
                "Trang "
                        + pageName
                        + " không sẵn sàng sau "
                        + maxAttempts
                        + " lần thử: "
                        + url,
                lastException
        );
    }

    private void sleepBeforePageRetry(
            long milliseconds
    ) {

        try {

            Thread.sleep(
                    milliseconds
            );

        } catch (InterruptedException exception) {

            Thread.currentThread()
                    .interrupt();

            throw new IllegalStateException(
                    "Page Ready Retry bị gián đoạn.",
                    exception
            );
        }
    }

    private void closeCartModal() {

        WebElement button =
                waitUntilVisible(
                        continueShoppingButton
                );

        safeClick(button);

        getWait().until(
                ExpectedConditions
                        .invisibilityOfElementLocated(
                                cartModal
                        )
        );
    }

    private void ensureProductsPage() {

        if (!driver.getCurrentUrl()
                .contains("/products")) {

            openProductsPage();
        }
    }

    private void waitForCartPage() {

        getWait().until(
                driver ->
                        driver.getCurrentUrl()
                                .contains(
                                        "/view_cart"
                                )
        );
    }

    private By getProductCardLocator(
            String productName
    ) {

        return By.xpath(
                getProductCardXPath(
                        productName
                )
        );
    }

    private String getProductCardXPath(
            String productName
    ) {

        return "//div[contains(@class,"
                + "'product-image-wrapper')]"
                + "[.//div[contains(@class,"
                + "'productinfo')]"
                + "//p[normalize-space()="
                + toXPathLiteral(
                productName
        )
                + "]]";
    }

    private String getCartRowXPath(
            String productName
    ) {

        return "//table[@id='cart_info_table']"
                + "//tbody/tr"
                + "[.//td[contains(@class,"
                + "'cart_description')]"
                + "//h4/a[normalize-space()="
                + toXPathLiteral(
                productName
        )
                + "]]";
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
                        "arguments[0].scrollIntoView("
                                + "{block:'center'});",
                        element
                );
    }

    private WebDriverWait getWait() {

        int timeout =
                Integer.parseInt(
                        ConfigManager.get(
                                "timeout.explicit"
                        )
                );

        return new WebDriverWait(
                driver,
                Duration.ofSeconds(
                        timeout
                )
        );
    }

    private String getBaseUrl() {

        String url =
                ConfigManager.get(
                        "automationexercise.url"
                );

        while (url.endsWith("/")) {

            url =
                    url.substring(
                            0,
                            url.length() - 1
                    );
        }

        return url;
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

    private String toXPathLiteral(
            String value
    ) {

        if (!value.contains("'")) {

            return "'"
                    + value
                    + "'";
        }

        if (!value.contains("\"")) {

            return "\""
                    + value
                    + "\"";
        }

        String[] parts =
                value.split(
                        "'",
                        -1
                );

        StringBuilder xpath =
                new StringBuilder(
                        "concat("
                );

        for (
                int i = 0;
                i < parts.length;
                i++
        ) {

            if (i > 0) {

                xpath.append(
                        ", \"'\", "
                );
            }

            xpath.append("'")
                    .append(
                            parts[i]
                    )
                    .append("'");
        }

        xpath.append(")");

        return xpath.toString();
    }
}