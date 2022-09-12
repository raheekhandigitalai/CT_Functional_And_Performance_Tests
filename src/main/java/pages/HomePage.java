package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

public class HomePage extends PageObject {

    public HomePage(RemoteWebDriver driver) {
        super(driver);
    }

    // Mobile Selectors
    By navigationToggle = By.xpath("//*[@class='navbar-mobile-toggle collapsed']");

    @FindAll({
            @FindBy(xpath = "//button[@class='onetrust-close-btn-handler onetrust-close-btn-ui banner-close-button ot-mobile ot-close-icon']"),
            @FindBy(xpath = "//*[@class='onetrust-close-btn-handler onetrust-close-btn-ui banner-close-button onetrust-lg ot-close-icon']"),
            @FindBy(xpath = "//*[@class='onetrust-close-btn-handler onetrust-close-btn-ui banner-close-button ot-close-icon']")
    })
    WebElement cookiesPopup;

    public void clickToCloseAllCookiesPopup() {
        waitForElementToBeClickableAndClick(cookiesPopup);
    }

    public void clickOnNavigationToggle() {
        waitForElementToBePresentAndClick(navigationToggle);
    }

}
