package com.hongnhung.faneventbooking.pages;

import com.hongnhung.faneventbooking.config.ConfigManager;
import com.hongnhung.faneventbooking.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class AutomationExerciseProductsPage
        extends BasePage {

    private static final By HOME_READY_LOCATOR =
            By.cssSelector(
                    "a[href='/products']"
            );

    private static final By ALL_PRODUCTS_READY_LOCATOR =
            By.xpath(
                    "//h2[contains(@class,'title') "
                            + "and normalize-space()='All Products']"
            );

    private static final By PRODUCT_DETAIL_READY_LOCATOR =
            By.cssSelector(
                    ".product-information h2"
            );

    @FindBy(css = "a[href='/products']")
    private WebElement productsMenu;

    @FindBy(
            xpath =
                    "//h2[contains(@class,'title') "
                            + "and normalize-space()='All Products']"
    )
    private WebElement allProductsTitle;

    @FindBy(id = "search_product")
    private WebElement searchInput;

    @FindBy(id = "submit_search")
    private WebElement searchButton;

    @FindBy(
            xpath =
                    "//h2[contains(@class,'title') "
                            + "and normalize-space()='Searched Products']"
    )
    private WebElement searchedProductsTitle;

    @FindBy(
            css =
                    ".features_items "
                            + ".productinfo p"
    )
    private List<WebElement> productNames;

    @FindBy(
            css =
                    ".product-information h2"
    )
    private WebElement productDetailName;

    public AutomationExerciseProductsPage(
            DriverManager driverManager
    ) {

        super(driverManager);
    }

    public void openHomePage() {

        navigateToAndWaitUntilReady(
                getBaseUrl(),
                HOME_READY_LOCATOR,
                "Home"
        );
    }

    public void openProductsPage() {

        /*
         * Vẫn click menu thật để giữ đúng hành vi
         * của test Search.
         *
         * Chỉ khi trang sau click không sẵn sàng
         * mới fallback sang navigation có retry.
         */
        try {

            click(
                    productsMenu
            );

            waitUntilVisible(
                    allProductsTitle
            );

        } catch (
                TimeoutException exception
        ) {

            navigateToAndWaitUntilReady(
                    getProductsUrl(),
                    ALL_PRODUCTS_READY_LOCATOR,
                    "Products"
            );
        }
    }

    public boolean isAllProductsDisplayed() {

        try {

            return waitUntilVisible(
                    allProductsTitle
            ).isDisplayed();

        } catch (
                TimeoutException exception
        ) {

            return false;
        }
    }

    public void enterSearchKeyword(
            String keyword
    ) {

        waitUntilVisible(
                searchInput
        );

        type(
                searchInput,
                keyword
        );
    }

    public void clickSearch() {

        click(
                searchButton
        );
    }

    public void search(
            String keyword
    ) {

        enterSearchKeyword(
                keyword
        );

        clickSearch();
    }

    public boolean isSearchedProductsDisplayed() {

        try {

            return waitUntilVisible(
                    searchedProductsTitle
            ).isDisplayed();

        } catch (
                TimeoutException exception
        ) {

            return false;
        }
    }

    public List<String> getProductNames() {

        return productNames
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
                        name ->
                                !name.isBlank()
                )
                .toList();
    }

    public int getProductCount() {

        return getProductNames()
                .size();
    }

    public boolean isProductDisplayed(
            String productName
    ) {

        By product =
                By.xpath(
                        "//div[contains(@class,'features_items')]"
                                + "//div[contains(@class,'productinfo')]"
                                + "//p[normalize-space()="
                                + toXPathLiteral(
                                productName
                        )
                                + "]"
                );

        try {

            WebElement element =
                    waitUntilVisible(
                            product
                    );

            return element.isDisplayed();

        } catch (
                TimeoutException exception
        ) {

            return false;
        }
    }

    public boolean containsProductKeyword(
            String keyword
    ) {

        if (keyword == null
                || keyword.isBlank()) {

            return false;
        }

        String expected =
                normalizeText(
                        keyword
                )
                        .toLowerCase();

        return getProductNames()
                .stream()
                .map(
                        String::toLowerCase
                )
                .anyMatch(
                        productName ->
                                productName.contains(
                                        expected
                                )
                );
    }

    public void openProductDetails(
            String productName
    ) {

        By viewProduct =
                By.xpath(
                        "//div[contains(@class,'product-image-wrapper')]"
                                + "[.//div[contains(@class,'productinfo')]"
                                + "//p[normalize-space()="
                                + toXPathLiteral(
                                productName
                        )
                                + "]]"
                                + "//a[contains(@href,"
                                + "'/product_details/')]"
                );

        WebElement element =
                waitUntilVisible(
                        viewProduct
                );

        String detailUrl =
                element.getAttribute(
                        "href"
                );

        JavascriptExecutor js =
                (JavascriptExecutor) driver;

        js.executeScript(
                "arguments[0].scrollIntoView("
                        + "{block:'center'});",
                element
        );

        try {

            try {

                element.click();

            } catch (
                    ElementClickInterceptedException exception
            ) {

                js.executeScript(
                        "arguments[0].click();",
                        element
                );
            }

            waitUntilVisible(
                    productDetailName
            );

        } catch (
                TimeoutException exception
        ) {

            if (detailUrl == null
                    || detailUrl.isBlank()) {

                throw exception;
            }

            navigateToAndWaitUntilReady(
                    detailUrl,
                    PRODUCT_DETAIL_READY_LOCATOR,
                    "Product Detail"
            );
        }
    }

    public String getProductDetailName() {

        return normalizeText(
                getText(
                        productDetailName
                )
        );
    }

    public boolean isProductDetailDisplayed(
            String expectedProductName
    ) {

        String expected =
                normalizeText(
                        expectedProductName
                );

        String actual =
                getProductDetailName();

        return expected.equalsIgnoreCase(
                actual
        );
    }

    public boolean isPageWorkingNormally() {

        String currentUrl =
                driver.getCurrentUrl();

        String title =
                driver.getTitle();

        return currentUrl != null
                && currentUrl.contains(
                "automationexercise.com"
        )
                && title != null
                && !title.isBlank();
    }

    private String getProductsUrl() {

        return getBaseUrl()
                + "/products";
    }

    private String getBaseUrl() {

        String baseUrl =
                ConfigManager.get(
                        "automationexercise.url"
                );

        while (
                baseUrl.endsWith("/")
        ) {

            baseUrl =
                    baseUrl.substring(
                            0,
                            baseUrl.length() - 1
                    );
        }

        return baseUrl;
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