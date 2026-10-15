package com.hongnhung.faneventbooking.pages;

import com.hongnhung.faneventbooking.config.ConfigManager;
import com.hongnhung.faneventbooking.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Locale;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(
            DriverManager driverManager
    ) {

        this.driver =
                driverManager.getDriver();

        PageFactory.initElements(
                driver,
                this
        );

        this.wait =
                new WebDriverWait(
                        driver,
                        Duration.ofSeconds(
                                ConfigManager.getLong(
                                        "timeout.explicit"
                                )
                        )
                );
    }

    protected WebElement waitUntilVisible(
            By locator
    ) {

        return wait.until(
                ExpectedConditions
                        .visibilityOfElementLocated(
                                locator
                        )
        );
    }

    protected WebElement waitUntilClickable(
            By locator
    ) {

        return wait.until(
                ExpectedConditions
                        .elementToBeClickable(
                                locator
                        )
        );
    }

    protected void click(
            By locator
    ) {

        waitUntilClickable(
                locator
        ).click();
    }

    protected void type(
            By locator,
            String value
    ) {

        WebElement element =
                waitUntilVisible(
                        locator
                );

        element.clear();

        element.sendKeys(
                value
        );
    }

    protected String getText(
            By locator
    ) {

        return waitUntilVisible(
                locator
        ).getText().trim();
    }

    protected boolean isDisplayed(
            By locator
    ) {

        try {

            return waitUntilVisible(
                    locator
            ).isDisplayed();

        } catch (RuntimeException exception) {

            return false;
        }
    }

    protected WebElement waitUntilVisible(
            WebElement element
    ) {

        return wait.until(
                ExpectedConditions
                        .visibilityOf(
                                element
                        )
        );
    }

    protected WebElement waitUntilClickable(
            WebElement element
    ) {

        return wait.until(
                ExpectedConditions
                        .elementToBeClickable(
                                element
                        )
        );
    }

    protected void click(
            WebElement element
    ) {

        waitUntilClickable(
                element
        ).click();
    }

    protected void type(
            WebElement element,
            String value
    ) {

        WebElement visible =
                waitUntilVisible(
                        element
                );

        visible.clear();

        visible.sendKeys(
                value
        );
    }

    protected String getText(
            WebElement element
    ) {

        return waitUntilVisible(
                element
        ).getText().trim();
    }

    protected boolean isDisplayed(
            WebElement element
    ) {

        try {

            return waitUntilVisible(
                    element
            ).isDisplayed();

        } catch (
                TimeoutException
                | NoSuchElementException exception
        ) {

            return false;
        }
    }

    protected void navigateTo(
            String url
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

        WebDriverException lastException =
                null;

        for (
                int attempt = 1;
                attempt <= maxAttempts;
                attempt++
        ) {

            try {

                driver.get(url);

                if (!isTransientServerErrorPage()) {
                    return;
                }

                System.err.printf(
                        "[NAVIGATION RETRY] "
                                + "Phát hiện lỗi server tạm thời. "
                                + "URL=%s, lần=%d/%d%n",
                        url,
                        attempt,
                        maxAttempts
                );

            } catch (
                    WebDriverException exception
            ) {

                if (!isRetryableNavigationException(
                        exception
                )) {

                    throw exception;
                }

                lastException =
                        exception;

                System.err.printf(
                        "[NAVIGATION RETRY] "
                                + "Navigation lỗi tạm thời: %s. "
                                + "URL=%s, lần=%d/%d%n",
                        getShortMessage(
                                exception
                        ),
                        url,
                        attempt,
                        maxAttempts
                );
            }

            stopCurrentPageLoad();

            if (attempt < maxAttempts) {

                sleepBeforeRetry(
                        retryDelay
                );
            }
        }

        if (lastException != null) {

            throw new IllegalStateException(
                    "Không thể mở URL sau "
                            + maxAttempts
                            + " lần thử: "
                            + url,
                    lastException
            );
        }

        throw new IllegalStateException(
                "Website liên tục trả lỗi server "
                        + "sau "
                        + maxAttempts
                        + " lần thử: "
                        + url
        );
    }

    /*
     * Lớp bảo vệ thứ hai sau navigateTo().
     *
     * navigateTo() xử lý:
     * - navigation timeout
     * - Cloudflare 520/521/522/523/524
     *
     * Phương thức này xử lý trường hợp:
     * - driver.get() đã trả về thành công
     * - nhưng trang thực tế chưa sẵn sàng
     * - element đặc trưng của trang không xuất hiện
     */
    protected void navigateToAndWaitUntilReady(
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

            } catch (
                    TimeoutException exception
            ) {

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

                sleepBeforeRetry(
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

    protected String getCurrentUrl() {

        return driver.getCurrentUrl();
    }

    protected boolean currentUrlContains(
            String value
    ) {

        return getCurrentUrl()
                .contains(
                        value
                );
    }

    protected void waitUntilUrlContains(
            String value
    ) {

        wait.until(
                ExpectedConditions
                        .urlContains(
                                value
                        )
        );
    }

    private boolean isRetryableNavigationException(
            WebDriverException exception
    ) {

        if (exception instanceof TimeoutException) {
            return true;
        }

        String message =
                getShortMessage(
                        exception
                )
                        .toLowerCase(
                                Locale.ROOT
                        );

        return message.contains(
                "navigation timed out"
        )
                || message.contains(
                "connection reset"
        )
                || message.contains(
                "connection refused"
        )
                || message.contains(
                "connection timed out"
        )
                || message.contains(
                "timed out receiving message from renderer"
        )
                || message.contains(
                "neterror"
        );
    }

    private boolean isTransientServerErrorPage() {

        String title =
                safeGetTitle()
                        .toLowerCase(
                                Locale.ROOT
                        );

        String source =
                safeGetPageSource()
                        .toLowerCase(
                                Locale.ROOT
                        );

        boolean cloudflarePage =
                title.contains(
                        "cloudflare"
                )
                        || source.contains(
                        "cloudflare"
                );

        if (!cloudflarePage) {
            return false;
        }

        return containsTransientError(
                title
        )
                || containsTransientError(
                source
        );
    }

    private boolean containsTransientError(
            String content
    ) {

        return content.contains(
                "error code 520"
        )
                || content.contains(
                "error code 521"
        )
                || content.contains(
                "error code 522"
        )
                || content.contains(
                "error code 523"
        )
                || content.contains(
                "error code 524"
        )
                || content.contains(
                "web server is returning an unknown error"
        )
                || content.contains(
                "web server is down"
        )
                || content.contains(
                "connection timed out"
        )
                || content.contains(
                "origin is unreachable"
        )
                || content.contains(
                "a timeout occurred"
        );
    }

    private String safeGetTitle() {

        try {

            return driver.getTitle();

        } catch (
                WebDriverException exception
        ) {

            return "";
        }
    }

    private String safeGetPageSource() {

        try {

            return driver.getPageSource();

        } catch (
                WebDriverException exception
        ) {

            return "";
        }
    }

    private void stopCurrentPageLoad() {

        try {

            ((JavascriptExecutor) driver)
                    .executeScript(
                            "window.stop();"
                    );

        } catch (
                WebDriverException ignored
        ) {
        }
    }

    private void sleepBeforeRetry(
            long milliseconds
    ) {

        try {

            Thread.sleep(
                    milliseconds
            );

        } catch (
                InterruptedException exception
        ) {

            Thread.currentThread()
                    .interrupt();

            throw new IllegalStateException(
                    "Navigation retry bị gián đoạn.",
                    exception
            );
        }
    }

    private String getShortMessage(
            Throwable throwable
    ) {

        String message =
                throwable.getMessage();

        if (message == null
                || message.isBlank()) {

            return throwable
                    .getClass()
                    .getSimpleName();
        }

        int lineBreak =
                message.indexOf(
                        '\n'
                );

        if (lineBreak >= 0) {

            return message.substring(
                    0,
                    lineBreak
            );
        }

        return message;
    }
}