package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class NavigationBarPage extends PageObject {

    String executionPlatform = null;

    public NavigationBarPage(RemoteWebDriver driver) {
        super(driver);
        executionPlatform = getParameterFromXMLFile("executionPlatform");
    }

    // Mobile Selectors
    By searchFieldInput = By.xpath("//*[@class='fake-search']//input");
    By enterButton = By.xpath("//*[@class='submit-fake-search']");

    // Web Selectors
    By searchButtonWeb = By.xpath("//*[@class='search-link']");
    By searchFieldInputWeb = By.xpath("//*[@name='search']");
    By enterButtonWeb = By.xpath("//*[@type='submit']");

    // Common Selectors for Mobile and Web
    By firstItemInResults = By.xpath("(//*[@class='views-row odd'])[1]");

    public void clickOnSearchButton(WebElement element) {
        waitForElementToBeClickableAndClick(element);
    }

    public void searchForItemInSearchField(String input) {

        if (executionPlatform.equalsIgnoreCase("Mobile")) {
            waitForElementToBeClickableAndSendKeys(searchFieldInput, input);
        } else if (executionPlatform.equalsIgnoreCase("Web")) {
            waitForElementToBeClickableAndClick(searchButtonWeb);
            waitForElementToBeClickableAndSendKeys(searchFieldInputWeb, input);
        }

    }

    public void clickOnEnterButton() {

        if (executionPlatform.equalsIgnoreCase("Mobile")) {
            waitForElementToBeClickableAndClick(enterButton);
        } else if (executionPlatform.equalsIgnoreCase("Web")) {
            waitForElementToBeClickableAndClick(enterButtonWeb);
        }

    }

    public boolean verifyResultsAppearingAfterSearch() {
        return waitForElementToBeClickableAssertion(firstItemInResults);
    }

}
