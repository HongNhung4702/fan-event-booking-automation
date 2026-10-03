package com.hongnhung.faneventbooking.steps;

import com.hongnhung.faneventbooking.driver.DriverManager;
import com.hongnhung.faneventbooking.pages.AutomationExerciseProductsPage;
import com.hongnhung.faneventbooking.utils.CsvDataReader;
import com.hongnhung.faneventbooking.utils.TestDataResolver;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class SearchSteps {

    private static final String SEARCH_DATA =
            "testdata/search-data.csv";

    private final AutomationExerciseProductsPage productsPage;

    public SearchSteps(DriverManager driverManager) {
        this.productsPage =
                new AutomationExerciseProductsPage(
                        driverManager
                );
    }

    @Given("người dùng mở trang chủ Automation Exercise")
    public void openAutomationExerciseHomePage() {
        productsPage.openHomePage();
    }

    @When("người dùng chọn menu Products")
    public void openProductsMenu() {
        productsPage.openProductsPage();
    }

    @Then("trang ALL PRODUCTS phải được hiển thị")
    public void verifyAllProductsPage() {

        Assert.assertTrue(
                productsPage.isAllProductsDisplayed(),
                "Trang ALL PRODUCTS không được hiển thị."
        );
    }

    @When("người dùng tìm kiếm bằng bộ dữ liệu {string}")
    public void searchWithTestData(String testCaseId) {

        Map<String, String> data =
                CsvDataReader.getRow(
                        SEARCH_DATA,
                        testCaseId
                );

        String keyword =
                TestDataResolver.resolve(
                        data.get("keyword")
                );

        productsPage.search(keyword);
    }

    @Then("kết quả tìm kiếm phải đúng với bộ dữ liệu {string}")
    public void verifySearchResult(String testCaseId) {

        Map<String, String> data =
                CsvDataReader.getRow(
                        SEARCH_DATA,
                        testCaseId
                );

        String expectedResult =
                data.get("expectedResult");

        String expectedProduct =
                TestDataResolver.resolve(
                        data.get("expectedProduct")
                );

        switch (expectedResult.toUpperCase()) {

            case "HAS_RESULT" -> {

                Assert.assertTrue(
                        productsPage.isSearchedProductsDisplayed(),
                        "SEARCHED PRODUCTS không được hiển thị."
                );

                Assert.assertTrue(
                        productsPage.getProductCount() > 0,
                        "Không có sản phẩm nào trong kết quả tìm kiếm."
                );

                String keyword = data.get("keyword");

                Assert.assertTrue(
                        productsPage.containsProductKeyword(keyword),
                        "Không tìm thấy sản phẩm phù hợp với từ khóa: "
                                + keyword
                );
            }

            case "NO_RESULT" -> {

                Assert.assertTrue(
                        productsPage.isSearchedProductsDisplayed(),
                        "SEARCHED PRODUCTS không được hiển thị."
                );

                Assert.assertEquals(
                        productsPage.getProductCount(),
                        0,
                        "Vẫn xuất hiện sản phẩm dù từ khóa không tồn tại."
                );
            }

            case "STABLE" ->

                    Assert.assertTrue(
                            productsPage.isPageWorkingNormally(),
                            "Trang tìm kiếm không hoạt động bình thường."
                    );

            default ->

                    Assert.fail(
                            "expectedResult không hợp lệ trong CSV: "
                                    + expectedResult
                    );
        }
    }

    @Then("hệ thống tìm kiếm phải ổn định với bộ dữ liệu {string}")
    public void verifySearchStable(String testCaseId) {

        CsvDataReader.getRow(
                SEARCH_DATA,
                testCaseId
        );

        Assert.assertTrue(
                productsPage.isPageWorkingNormally(),
                "Website không hoạt động bình thường với dữ liệu biên."
        );
    }

    @Then("người dùng kiểm tra các thành phần:")
    public void verifySearchComponents(DataTable dataTable) {

        List<String> components =
                dataTable.asList();

        for (String component : components) {

            if ("thành phần".equalsIgnoreCase(component)) {
                continue;
            }

            switch (component.trim().toLowerCase()) {

                case "searched products" ->

                        Assert.assertTrue(
                                productsPage.isSearchedProductsDisplayed(),
                                "SEARCHED PRODUCTS không hiển thị."
                        );

                case "danh sách sản phẩm" ->

                        Assert.assertTrue(
                                productsPage.getProductCount() > 0,
                                "Danh sách sản phẩm đang rỗng."
                        );

                default ->

                        Assert.fail(
                                "Thành phần chưa được hỗ trợ: "
                                        + component
                        );
            }
        }
    }

    @When("người dùng mở sản phẩm từ bộ dữ liệu {string}")
    public void openProductFromTestData(String testCaseId) {

        Map<String, String> data =
                CsvDataReader.getRow(
                        SEARCH_DATA,
                        testCaseId
                );

        String productName =
                data.get("expectedProduct");

        productsPage.openProductDetails(
                productName
        );
    }

    @Then("trang chi tiết của sản phẩm trong bộ dữ liệu {string} phải được hiển thị")
    public void verifyProductDetails(String testCaseId) {

        Map<String, String> data =
                CsvDataReader.getRow(
                        SEARCH_DATA,
                        testCaseId
                );

        String productName =
                data.get("expectedProduct");

        Assert.assertTrue(
                productsPage.isProductDetailDisplayed(
                        productName
                ),
                "Trang chi tiết sản phẩm không đúng: "
                        + productName
        );
    }
}