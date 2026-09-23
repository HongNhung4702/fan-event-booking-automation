package com.hongnhung.faneventbooking.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class FrameworkSmokeSteps {

    private boolean frameworkInitialized;

    @Given("framework kiểm thử đã được khởi tạo")
    public void frameworkKiemThuDaDuocKhoiTao() {
        frameworkInitialized = true;
    }

    @Then("Cucumber có thể chạy với TestNG")
    public void cucumberCoTheChayVoiTestNG() {
        Assert.assertTrue(
                frameworkInitialized,
                "Framework chưa được khởi tạo."
        );
    }
}