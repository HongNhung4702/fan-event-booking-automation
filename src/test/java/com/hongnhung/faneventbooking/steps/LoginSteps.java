package com.hongnhung.faneventbooking.steps;

import com.hongnhung.faneventbooking.driver.DriverManager;
import com.hongnhung.faneventbooking.pages.SauceDemoLoginPage;
import com.hongnhung.faneventbooking.utils.CsvDataReader;
import com.hongnhung.faneventbooking.utils.TestDataResolver;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class LoginSteps {

    private static final String LOGIN_DATA =
            "testdata/login-data.csv";

    private final SauceDemoLoginPage loginPage;

    public LoginSteps(DriverManager driverManager) {
        this.loginPage =
                new SauceDemoLoginPage(driverManager);
    }

    @Given("người dùng mở trang đăng nhập SauceDemo")
    public void openSauceDemoLoginPage() {
        loginPage.open();
    }

    @Then("biểu mẫu đăng nhập SauceDemo phải được hiển thị")
    public void verifyLoginFormDisplayed() {

        Assert.assertTrue(
                loginPage.isLoginFormDisplayed(),
                "Biểu mẫu đăng nhập SauceDemo không được hiển thị."
        );
    }

    @When("người dùng đăng nhập bằng bộ dữ liệu {string}")
    public void loginWithTestData(String testCaseId) {

        Map<String, String> data =
                CsvDataReader.getRow(
                        LOGIN_DATA,
                        testCaseId
                );

        String username =
                TestDataResolver.resolve(
                        data.get("username")
                );

        String password =
                TestDataResolver.resolve(
                        data.get("password")
                );

        loginPage.login(
                username,
                password
        );
    }

    @Then("kết quả đăng nhập phải đúng với bộ dữ liệu {string}")
    public void verifyLoginResult(String testCaseId) {

        Map<String, String> data =
                CsvDataReader.getRow(
                        LOGIN_DATA,
                        testCaseId
                );

        String expectedResult =
                data.get("expectedResult");

        if ("SUCCESS".equalsIgnoreCase(expectedResult)) {

            Assert.assertTrue(
                    loginPage.isProductsPageDisplayed(),
                    "Đăng nhập thành công nhưng trang Products không hiển thị."
            );

            Assert.assertTrue(
                    loginPage.isInventoryUrlDisplayed(),
                    "URL không chứa /inventory.html."
            );

        } else if ("ERROR".equalsIgnoreCase(expectedResult)) {

            String expectedError =
                    data.get("expectedError");

            Assert.assertTrue(
                    loginPage.isStillOnLoginPage(),
                    "Người dùng không còn ở trang Login."
            );

            Assert.assertTrue(
                    loginPage.isErrorDisplayed(),
                    "Thông báo lỗi không được hiển thị."
            );

            Assert.assertEquals(
                    loginPage.getErrorMessage(),
                    expectedError,
                    "Thông báo lỗi không đúng."
            );

        } else {

            Assert.fail(
                    "expectedResult không hợp lệ trong CSV: "
                            + expectedResult
            );
        }
    }

    @Then("người dùng kiểm tra các thông tin sau:")
    public void verifyLoginInformation(DataTable dataTable) {

        List<String> attributes =
                dataTable.asList();

        for (String attribute : attributes) {

            if ("thuộc tính".equalsIgnoreCase(attribute)) {
                continue;
            }

            switch (attribute.trim().toLowerCase()) {

                case "url" ->
                        Assert.assertTrue(
                                loginPage.currentUrl()
                                        .contains("/inventory.html"),
                                "URL trang Products không đúng."
                        );

                case "tiêu đề" ->
                        Assert.assertTrue(
                                loginPage.isProductsPageDisplayed(),
                                "Tiêu đề Products không được hiển thị."
                        );

                default ->
                        Assert.fail(
                                "Thuộc tính kiểm tra chưa được hỗ trợ: "
                                        + attribute
                        );
            }
        }
    }
}