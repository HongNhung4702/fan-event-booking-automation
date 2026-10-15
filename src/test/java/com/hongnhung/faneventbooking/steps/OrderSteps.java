package com.hongnhung.faneventbooking.steps;

import com.hongnhung.faneventbooking.config.ConfigManager;
import com.hongnhung.faneventbooking.driver.DriverFactory;
import com.hongnhung.faneventbooking.driver.DriverManager;
import com.hongnhung.faneventbooking.pages.AutomationExerciseCartPage;
import com.hongnhung.faneventbooking.pages.AutomationExerciseCheckoutPage;
import com.hongnhung.faneventbooking.pages.AutomationExerciseLoginPage;
import com.hongnhung.faneventbooking.utils.CsvDataReader;
import com.hongnhung.faneventbooking.utils.TestDataResolver;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class OrderSteps {

    private static final String ORDER_DATA =
            "testdata/order-data.csv";

    private final AutomationExerciseLoginPage loginPage;
    private final AutomationExerciseCartPage cartPage;
    private final AutomationExerciseCheckoutPage checkoutPage;

    private String currentTestCaseId;
    private Path downloadedInvoice;

    public OrderSteps(
            DriverManager driverManager
    ) {

        this.loginPage =
                new AutomationExerciseLoginPage(
                        driverManager
                );

        this.cartPage =
                new AutomationExerciseCartPage(
                        driverManager
                );

        this.checkoutPage =
                new AutomationExerciseCheckoutPage(
                        driverManager
                );
    }

    @Then(
            "trang chủ Automation Exercise phải được hiển thị"
    )
    public void verifyHomePageDisplayed() {

        Assert.assertTrue(
                cartPage.isHomePageDisplayed(),
                "Trang chủ Automation Exercise không được hiển thị."
        );
    }

    @Given(
            "người dùng đăng nhập Automation Exercise bằng bộ dữ liệu {string}"
    )
    public void loginAutomationExercise(
            String testCaseId
    ) {

        currentTestCaseId =
                testCaseId;

        Map<String, String> data =
                getData(testCaseId);

        String email =
                TestDataResolver.resolve(
                        data.get("email")
                );

        String password =
                TestDataResolver.resolve(
                        data.get("password")
                );

        loginPage.login(
                email,
                password
        );

        Assert.assertTrue(
                loginPage.isLoggedIn(),
                "Đăng nhập Automation Exercise thất bại với Test Case: "
                        + testCaseId
        );
    }

    @When(
            "người dùng thêm sản phẩm bằng bộ dữ liệu {string} vào giỏ hàng"
    )
    public void addProductsToCart(
            String testCaseId
    ) {

        currentTestCaseId =
                testCaseId;

        Map<String, String> data =
                getData(testCaseId);

        String product1 =
                TestDataResolver.resolve(
                        data.get("product1")
                );

        String product2 =
                TestDataResolver.resolve(
                        data.get("product2")
                );

        if (!product1.isBlank()) {

            cartPage.addProductToCart(
                    product1
            );
        }

        if (!product2.isBlank()) {

            cartPage.addProductToCart(
                    product2
            );
        }
    }

    @When(
            "người dùng mở chi tiết sản phẩm bằng bộ dữ liệu {string}"
    )
    public void openProductDetails(
            String testCaseId
    ) {

        currentTestCaseId =
                testCaseId;

        Map<String, String> data =
                getData(testCaseId);

        String productName =
                TestDataResolver.resolve(
                        data.get("product1")
                );

        cartPage.openProductDetails(
                productName
        );

        Assert.assertTrue(
                cartPage.isProductDetailDisplayed(
                        productName
                ),
                "Trang chi tiết sản phẩm không hiển thị đúng: "
                        + productName
        );
    }

    @And(
            "người dùng chọn số lượng theo bộ dữ liệu {string}"
    )
    public void selectQuantity(
            String testCaseId
    ) {

        currentTestCaseId =
                testCaseId;

        Map<String, String> data =
                getData(testCaseId);

        int quantity =
                Integer.parseInt(
                        data.get("quantity")
                );

        cartPage.setQuantity(
                quantity
        );
    }

    @And(
            "người dùng thêm sản phẩm từ trang chi tiết vào giỏ hàng"
    )
    public void addCurrentProductToCart() {

        cartPage.addCurrentProductToCart();
    }

    @And(
            "người dùng mở giỏ hàng"
    )
    public void openCart() {

        cartPage.openCart();

        Assert.assertTrue(
                cartPage.isCartPageDisplayed(),
                "Trang giỏ hàng không được hiển thị."
        );
    }

    @Then(
            "kết quả giỏ hàng phải đúng với bộ dữ liệu {string}"
    )
    public void verifyCartResult(
            String testCaseId
    ) {

        Map<String, String> data =
                getData(testCaseId);

        String expectedResult =
                data.get("expectedResult");

        String product1 =
                TestDataResolver.resolve(
                        data.get("product1")
                );

        String product2 =
                TestDataResolver.resolve(
                        data.get("product2")
                );

        switch (
                expectedResult.toUpperCase()
        ) {

            case "CART_HAS_PRODUCT" -> {

                Assert.assertTrue(
                        cartPage.isProductInCart(
                                product1
                        ),
                        "Không tìm thấy sản phẩm trong giỏ hàng: "
                                + product1
                );
            }

            case "CART_HAS_MULTIPLE_PRODUCTS" -> {

                Assert.assertTrue(
                        cartPage.isProductInCart(
                                product1
                        ),
                        "Không tìm thấy sản phẩm thứ nhất: "
                                + product1
                );

                Assert.assertTrue(
                        cartPage.isProductInCart(
                                product2
                        ),
                        "Không tìm thấy sản phẩm thứ hai: "
                                + product2
                );

                Assert.assertTrue(
                        cartPage.getCartProductCount()
                                >= 2,
                        "Giỏ hàng không có đủ nhiều sản phẩm."
                );
            }

            case "CART_QUANTITY_MATCH" -> {

                int expectedQuantity =
                        Integer.parseInt(
                                data.get("quantity")
                        );

                int actualQuantity =
                        cartPage.getProductQuantity(
                                product1
                        );

                Assert.assertEquals(
                        actualQuantity,
                        expectedQuantity,
                        "Số lượng sản phẩm trong giỏ hàng không đúng."
                );
            }

            case "PRODUCT_REMOVED" -> {

                Assert.assertFalse(
                        cartPage.isProductInCart(
                                product1
                        ),
                        "Sản phẩm vẫn còn trong giỏ hàng sau khi xóa: "
                                + product1
                );
            }

            default -> Assert.fail(
                    "expectedResult không được hỗ trợ: "
                            + expectedResult
            );
        }
    }

    @And(
            "người dùng xóa sản phẩm trong bộ dữ liệu {string}"
    )
    public void removeProduct(
            String testCaseId
    ) {

        currentTestCaseId =
                testCaseId;

        Map<String, String> data =
                getData(testCaseId);

        String productName =
                TestDataResolver.resolve(
                        data.get("product1")
                );

        cartPage.removeProduct(
                productName
        );
    }

    @And(
            "người dùng chọn Proceed To Checkout"
    )
    public void proceedToCheckout() {

        cartPage.proceedToCheckout();
    }

    @Then(
            "hệ thống phải yêu cầu đăng ký hoặc đăng nhập"
    )
    public void verifyLoginRequired() {

        Assert.assertTrue(
                cartPage.isLoginRequiredDisplayed(),
                "Hệ thống không hiển thị yêu cầu Register/Login khi Checkout chưa đăng nhập."
        );
    }

    @Then(
            "trang Checkout phải được hiển thị"
    )
    public void verifyCheckoutPage() {

        Assert.assertTrue(
                checkoutPage.isCheckoutPageDisplayed(),
                "Trang Checkout không được hiển thị."
        );
    }

    @Then(
            "người dùng kiểm tra các khu vực Checkout:"
    )
    public void verifyCheckoutSections(
            DataTable dataTable
    ) {

        List<String> sections =
                dataTable.asList();

        for (String section : sections) {

            if (section.equalsIgnoreCase(
                    "khu vực"
            )) {
                continue;
            }

            switch (section) {

                case "Delivery Address" ->
                        Assert.assertTrue(
                                checkoutPage
                                        .isDeliveryAddressDisplayed(),
                                "Delivery Address không hiển thị."
                        );

                case "Billing Address" ->
                        Assert.assertTrue(
                                checkoutPage
                                        .isBillingAddressDisplayed(),
                                "Billing Address không hiển thị."
                        );

                case "Review Your Order" ->
                        Assert.assertTrue(
                                checkoutPage
                                        .isReviewOrderDisplayed(),
                                "Review Your Order không hiển thị."
                        );

                default ->
                        Assert.fail(
                                "Khu vực Checkout không được hỗ trợ: "
                                        + section
                        );
            }
        }

        verifyRegisteredAddress();
    }

    @And(
            "người dùng chọn Place Order"
    )
    public void clickPlaceOrder() {

        checkoutPage.clickPlaceOrder();

        Assert.assertTrue(
                checkoutPage.isPaymentPageDisplayed(),
                "Trang Payment không được hiển thị."
        );
    }

    @And(
            "người dùng nhập thông tin thanh toán bằng bộ dữ liệu {string}"
    )
    public void enterPaymentInformation(
            String testCaseId
    ) {

        currentTestCaseId =
                testCaseId;

        Map<String, String> data =
                getData(testCaseId);

        checkoutPage.enterPaymentInformation(
                TestDataResolver.resolve(
                        data.get("cardName")
                ),
                TestDataResolver.resolve(
                        data.get("cardNumber")
                ),
                TestDataResolver.resolve(
                        data.get("cvc")
                ),
                TestDataResolver.resolve(
                        data.get("expiryMonth")
                ),
                TestDataResolver.resolve(
                        data.get("expiryYear")
                )
        );
    }

    @And(
            "người dùng xác nhận thanh toán"
    )
    public void confirmPayment() {

        checkoutPage.confirmPayment();
    }

    @Then(
            "kết quả đặt hàng phải đúng với bộ dữ liệu {string}"
    )
    public void verifyOrderResult(
            String testCaseId
    ) {

        Map<String, String> data =
                getData(testCaseId);

        String expectedResult =
                data.get("expectedResult");

        switch (
                expectedResult.toUpperCase()
        ) {

            case "ORDER_SUCCESS" -> {

                Assert.assertTrue(
                        checkoutPage
                                .isOrderPlacedSuccessfully(),
                        "ORDER PLACED không được hiển thị."
                );

                Assert.assertTrue(
                        checkoutPage
                                .isOrderSuccessMessageDisplayed(),
                        "Thông báo đặt hàng thành công không được hiển thị."
                );
            }

            case "INVOICE_DOWNLOAD" -> {

                Assert.assertTrue(
                        checkoutPage
                                .isOrderPlacedSuccessfully(),
                        "Đơn hàng chưa được xác nhận thành công."
                );

                Assert.assertTrue(
                        checkoutPage
                                .isDownloadInvoiceButtonDisplayed(),
                        "Nút Download Invoice không được hiển thị."
                );
            }

            default -> Assert.fail(
                    "expectedResult của Order không được hỗ trợ: "
                            + expectedResult
            );
        }
    }

    @When(
            "người dùng tải Invoice"
    )
    public void downloadInvoice() {

        Path downloadDirectory =
                getCurrentDownloadDirectory();

        clearDownloadDirectory(
                downloadDirectory
        );

        downloadedInvoice = null;

        checkoutPage.downloadInvoice();
    }

    @Then(
            "file Invoice phải được tải xuống thành công"
    )
    public void verifyInvoiceDownloaded() {

        Path downloadDirectory =
                getCurrentDownloadDirectory();

        downloadedInvoice =
                waitForDownloadedFile(
                        downloadDirectory,
                        Duration.ofSeconds(20)
                );

        Assert.assertNotNull(
                downloadedInvoice,
                "Không tìm thấy file Invoice trong thư mục: "
                        + downloadDirectory
        );

        Assert.assertTrue(
                Files.exists(
                        downloadedInvoice
                ),
                "File Invoice không tồn tại."
        );

        try {

            Assert.assertTrue(
                    Files.size(
                            downloadedInvoice
                    ) > 0,
                    "File Invoice được tải xuống nhưng rỗng."
            );

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Không thể kiểm tra kích thước file Invoice: "
                            + downloadedInvoice,
                    e
            );
        }
    }

    private void verifyRegisteredAddress() {

        if (currentTestCaseId == null
                || currentTestCaseId.isBlank()) {

            throw new IllegalStateException(
                    "Không xác định được Test Case hiện tại để kiểm tra Address."
            );
        }

        Map<String, String> data =
                getData(
                        currentTestCaseId
                );

        String[] expectedValues = {
                data.get("firstName")
                        + " "
                        + data.get("lastName"),
                data.get("company"),
                data.get("address1"),
                data.get("address2"),
                data.get("country"),
                data.get("state"),
                data.get("city"),
                data.get("zipcode"),
                data.get("mobile")
        };

        for (String value : expectedValues) {

            String expected =
                    TestDataResolver.resolve(
                            value
                    );

            if (expected.isBlank()) {
                continue;
            }

            Assert.assertTrue(
                    checkoutPage
                            .deliveryAddressContains(
                                    expected
                            ),
                    "Delivery Address không chứa dữ liệu: "
                            + expected
            );

            Assert.assertTrue(
                    checkoutPage
                            .billingAddressContains(
                                    expected
                            ),
                    "Billing Address không chứa dữ liệu: "
                            + expected
            );
        }
    }

    private Map<String, String> getData(
            String testCaseId
    ) {

        return CsvDataReader.getRow(
                ORDER_DATA,
                testCaseId
        );
    }

    private Path getCurrentDownloadDirectory() {

        String browser =
                ConfigManager.get(
                        "browser"
                );

        return Paths.get(
                        DriverFactory.getDownloadDirectory(
                                browser
                        )
                )
                .toAbsolutePath()
                .normalize();
    }

    private void clearDownloadDirectory(
            Path directory
    ) {

        try {

            Files.createDirectories(
                    directory
            );

            try (
                    Stream<Path> files =
                            Files.list(
                                    directory
                            )
            ) {

                files
                        .filter(
                                Files::isRegularFile
                        )
                        .forEach(
                                file -> {

                                    try {

                                        Files.deleteIfExists(
                                                file
                                        );

                                    } catch (
                                            IOException e
                                    ) {

                                        throw new IllegalStateException(
                                                "Không thể xóa file download cũ: "
                                                        + file,
                                                e
                                        );
                                    }
                                }
                        );
            }

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Không thể làm sạch thư mục download: "
                            + directory,
                    e
            );
        }
    }

    private Path waitForDownloadedFile(
            Path directory,
            Duration timeout
    ) {

        long endTime =
                System.currentTimeMillis()
                        + timeout.toMillis();

        while (
                System.currentTimeMillis()
                        < endTime
        ) {

            try (
                    Stream<Path> files =
                            Files.list(
                                    directory
                            )
            ) {

                Path file =
                        files
                                .filter(
                                        Files::isRegularFile
                                )
                                .filter(
                                        path -> {

                                            String name =
                                                    path
                                                            .getFileName()
                                                            .toString()
                                                            .toLowerCase();

                                            return !name.endsWith(
                                                    ".crdownload"
                                            )
                                                    && !name.endsWith(
                                                    ".part"
                                            )
                                                    && !name.endsWith(
                                                    ".tmp"
                                            );
                                        }
                                )
                                .max(
                                        Comparator.comparingLong(
                                                this::getLastModifiedTime
                                        )
                                )
                                .orElse(null);

                if (file != null) {

                    return file;
                }

            } catch (IOException e) {

                throw new IllegalStateException(
                        "Không thể kiểm tra thư mục download: "
                                + directory,
                        e
                );
            }

            try {

                Thread.sleep(
                        250
                );

            } catch (
                    InterruptedException e
            ) {

                Thread.currentThread()
                        .interrupt();

                throw new IllegalStateException(
                        "Quá trình chờ Invoice bị gián đoạn.",
                        e
                );
            }
        }

        return null;
    }

    private long getLastModifiedTime(
            Path path
    ) {

        try {

            return Files
                    .getLastModifiedTime(
                            path
                    )
                    .toMillis();

        } catch (IOException e) {

            return 0L;
        }
    }
}