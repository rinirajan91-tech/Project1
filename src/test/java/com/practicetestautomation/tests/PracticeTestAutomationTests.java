package com.practicetestautomation.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PracticeTestAutomationTests extends BaseTest {
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(10);
    private static final By HOME_LINK = By.cssSelector("header a[href='https://practicetestautomation.com/']");
    private static final By PRACTICE_HUB_LINK = By.cssSelector("a[href='https://practicetestautomation.com/practice/']");

    @Test(description = "Log in with the valid student credentials")
    public void validLoginSucceeds() {
        driver.get(LOGIN_URL);
        loginWithStudentCredentials();
        assertSuccessfulLogin();
    }

    @Test(description = "Open Home and scroll to the bottom of the page")
    public void homePageCanBeScrolledToTheEnd() {
        driver.get(LOGIN_URL);
        waitForClick(HOME_LINK).click();
        new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.urlToBe(HOME_URL));

        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, document.documentElement.scrollHeight)");

        Boolean reachedBottom = new WebDriverWait(driver, WAIT_TIMEOUT).until(webDriver ->
                (Boolean) ((JavascriptExecutor) webDriver).executeScript(
                        "return window.innerHeight + window.scrollY >= "
                                + "document.documentElement.scrollHeight - 1"));
        Assert.assertTrue(reachedBottom, "The Home page should scroll to its bottom.");
    }

    @Test(description = "Open Practice, follow Test Login Page, and log in")
    public void loginWorksFromPracticeTestLoginLink() {
        openPracticeHub();
        waitForClick(By.linkText("Test Login Page")).click();
        new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.urlContains("/practice-test-login/"));

        loginWithStudentCredentials();
        assertSuccessfulLogin();
    }

    @Test(description = "Filter the Test Table courses to Java only")
    public void javaFilterShowsOnlyJavaCourses() {
        openPracticeHub();
        waitForClick(By.linkText("Test Table")).click();
        new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.urlContains("/practice-test-table/"));
        Assert.assertTrue(driver.getCurrentUrl().startsWith(TABLE_URL),
                "The Test Table page should be open.");

        By javaRadio = By.cssSelector("input[name='lang'][value='Java']");
        waitForClick(javaRadio).click();
        Assert.assertTrue(driver.findElement(javaRadio).isSelected(),
                "The Java language radio button should be selected.");
        new WebDriverWait(driver, WAIT_TIMEOUT).until(webDriver -> {
            List<WebElement> visibleRows = visibleCourseRows(webDriver);
            return !visibleRows.isEmpty()
                    && visibleRows.stream().allMatch(PracticeTestAutomationTests::isJavaCourse);
        });

        List<WebElement> visibleRows = visibleCourseRows(driver);
        Assert.assertFalse(visibleRows.isEmpty(), "At least one Java course should be visible.");
        Assert.assertTrue(visibleRows.stream().allMatch(PracticeTestAutomationTests::isJavaCourse),
                "Every visible course should have Java as its language.");
    }

    private void openPracticeHub() {
        driver.get(HOME_URL);
        waitForClick(PRACTICE_HUB_LINK).click();
        new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.urlToBe(PRACTICE_URL));
    }

    private void loginWithStudentCredentials() {
        waitForClick(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("Password123");
        waitForClick(By.id("submit")).click();
    }

    private void assertSuccessfulLogin() {
        new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.urlContains("/logged-in-successfully/"));

        String pageText = driver.findElement(By.tagName("body")).getText().toLowerCase();
        Assert.assertTrue(
                pageText.contains("congratulations") || pageText.contains("successfully logged in"),
                "The success page should confirm the user logged in.");
        Assert.assertTrue(waitForClick(By.linkText("Log out")).isDisplayed(),
                "A Log out link should be visible after login.");
    }

    private WebElement waitForClick(By locator) {
        return new WebDriverWait(driver, WAIT_TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    private static List<WebElement> visibleCourseRows(WebDriver webDriver) {
        return webDriver.findElements(By.cssSelector("table tbody tr")).stream()
                .filter(WebElement::isDisplayed)
                .toList();
    }

    private static boolean isJavaCourse(WebElement row) {
        List<WebElement> cells = row.findElements(By.cssSelector("td"));
        return cells.size() >= 3 && "Java".equals(cells.get(2).getText().trim());
    }
}
