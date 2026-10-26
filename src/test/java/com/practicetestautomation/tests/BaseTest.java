package com.practicetestautomation.tests;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {
    protected static final String BASE_URL = "https://practicetestautomation.com";
    protected static final String HOME_URL = BASE_URL + "/";
    protected static final String PRACTICE_URL = BASE_URL + "/practice/";
    protected static final String LOGIN_URL = BASE_URL + "/practice-test-login/";
    protected static final String TABLE_URL = BASE_URL + "/practice-test-table/";

    protected WebDriver driver;

    @BeforeMethod
    public void startBrowser() {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("browser.headless", "true"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,1000");
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    @AfterMethod(alwaysRun = true)
    public void stopBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
