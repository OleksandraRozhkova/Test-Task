package org.example;

import jdk.jfr.Name;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;
import java.util.Set;


public class UITests extends BaseTest {

    @Test
    @Name("1")
    public void checkHomePageOpened() {
        openUrl("https://useinsider.com/");

        String ActualTitle = driver.getTitle();
        String ExpectedTitle = "#1 Leader in Individualized, Cross-Channel CX — Insider";
        Assert.assertEquals(ExpectedTitle, ActualTitle);
    }

    @Test
    @Name("2")
    public void checkLocationsTeamsLifeAtInsiderBlocksAreOpenedOnCareerPage() {
        openUrl("https://useinsider.com/");

        WebElement companyTab = getElement("//a[@id='navbarDropdownMenuLink' and contains(text(), 'Company')]");
        companyTab.click();

        WebElement careersTab = getElement("//a[@href='https://useinsider.com/careers/'][text()='Careers']");
        careersTab.click();

        boolean teams = getElement("//a[@class='btn btn-outline-secondary rounded text-medium mt-5 mx-auto py-3 loadmore' and text()='See all teams']").isDisplayed();
        boolean location = getElement("//*[@id=\"location-slider\"]").isDisplayed();
        boolean lifeAtInsider = getElement("//div[contains(@class, 'elementor-main-swiper')]").isDisplayed();

        Assert.assertTrue(teams && location && lifeAtInsider);
    }

    @Test
    @Name("3")
    public void checkJobListPresence() {
        openUrl("https://useinsider.com/careers/quality-assurance/");
        sleep();
        openJobList();

        boolean jobList = getElement("//*[@id=\"jobs-list\"]").isDisplayed();

        Assert.assertTrue(jobList);
    }

    @Test
    @Name("4")
    public void checkJobPositionDepartmentAndLocation() {
        boolean result = true;

        openUrl("https://useinsider.com/careers/quality-assurance/");
        sleep();
        openJobList();

        WebElement jobsList = getElement("//*[@id=\"jobs-list\"]");


        for (WebElement job : jobsList.findElements(By.xpath("//div[contains(@class, \"position-list-item\")]"))) {
            String jobInfo = job.getAttribute("textContent");

            if (jobInfo.contains("Quality Assurance") && jobInfo.indexOf("Quality Assurance") != jobInfo.lastIndexOf("Quality Assurance")) {
                result = jobInfo.contains("Istanbul, Turkey");
            } else {
                result = false;
            }
        }

        Assert.assertTrue(result);
    }

    @Test
    @Name("5")
    public void checkLeverApplicationFormPage() {
        String newTabURL = "";

        openUrl("https://useinsider.com/careers/quality-assurance/");
        sleep();
        openJobList();

        WebElement elementToHover = getElement("//*[@class=\"position-list-item-wrapper bg-light\"]");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementToHover);
        sleep();

        Actions actions = new Actions(driver);

        actions.moveToElement(elementToHover).perform();

        WebElement viewRoleButton = getElement("//a[contains(@href, 'https://jobs.lever.co/useinsider/') and contains(text(), 'View Role')]");
        viewRoleButton.click();

        String parentWindow = driver.getWindowHandle();

        Set<String> allWindows = driver.getWindowHandles();

        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(parentWindow)) {

                driver.switchTo().window(windowHandle);

                newTabURL = driver.getCurrentUrl();
                System.out.println("URL новой вкладки: " + newTabURL);
            }
        }

        Assert.assertTrue(newTabURL.contains("jobs.lever.co"));
    }

}
