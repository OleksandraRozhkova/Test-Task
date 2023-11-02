package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterSuite;

import java.time.Duration;


public class BaseTest {

    protected WebDriver driver;

    protected void initDriver() {
        driver = new ChromeDriver();
    }

    protected void closeCookie(){
        WebElement cookieMessage = driver.findElement(By.cssSelector("#cookie-law-info-bar")); // Пример CSS-селектора для элемента с сообщением о куки
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].style.display='none';", cookieMessage);
    }
    protected void openUrl(String url) {
        WebDriverManager.chromedriver().setup();

        initDriver();
        driver.navigate().to(url);

        closeCookie();
    }

    protected void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread was interrupted while sleeping: " + e.getMessage());
        }
    }

    @AfterSuite(alwaysRun = true)
    public void closeChrome() {
        driver.close();
    }

    protected WebElement getElement(String xPath) {
        return driver.findElement(By.xpath(xPath));
    };

    protected void openJobList() {
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        Actions actions = new Actions(driver);

        WebElement seeAllQaJobs = getElement("//a[contains(text(), 'See all QA jobs')]");
        executor.executeScript("arguments[0].click();", seeAllQaJobs);
        sleep();

        driver.manage().window().fullscreen();
        closeCookie();
        sleep();


        WebElement location = getElement("//*[@id=\"select2-filter-by-location-container\"]");
        location.click();
        sleep();

        WebElement locationSelected = getElement("//*[contains(@id, 'select2-filter-by-location-result-') and contains(@id, '-Istanbul, Turkey')]");
        actions.moveToElement(locationSelected).click().perform();
        sleep();

        String department = getElement("//*[@id=\"select2-filter-by-department-container\"]").getText();

        if (!department.equals("Quality Assurance")){
            getElement("//*[@id=\"select2-filter-by-department-container\"]").click();
            WebElement departmentSelected = getElement("//*[contains(@id, 'select2-filter-by-department-result-') and contains(@id, '-Quality Assurance')]");
            executor.executeScript("arguments[0].click();", departmentSelected);
            sleep();
        }
    }

}