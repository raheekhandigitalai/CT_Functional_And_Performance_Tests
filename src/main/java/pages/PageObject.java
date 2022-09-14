package pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import java.time.Duration;
import java.util.*;

public class PageObject {

    protected RemoteWebDriver driver;
    protected WebDriverWait wait;

    public PageObject(RemoteWebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void navigateToURL(String url) {
        driver.navigate().to(url);
    }

    public void maximizeBrowserWindow() {
        driver.manage().window().maximize();
    }

    public void sendTextToInputField(WebElement element, String input) {
        element.sendKeys(input);
    }

    public void clickOn(WebElement element) {
        element.click();
    }

    public void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public boolean waitForElementToBeClickableAssertion(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element)).isDisplayed();
    }

    public boolean waitForElementToBeClickableAssertion(By by) {
        return wait.until(ExpectedConditions.elementToBeClickable(by)).isDisplayed();
    }

    public boolean isDisplayed(WebElement element) {
        waitForElementToBeClickable(element);
        return element.isDisplayed();
    }

    public void waitForElementToBeClickableAndClick(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    public void waitForElementToBeClickableAndClick(By by) {
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    public void waitForElementToBeClickableAndSendKeys(WebElement element, String input) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).sendKeys(input);
    }

    public void waitForElementToBeClickableAndSendKeys(By by, String input) {
        wait.until(ExpectedConditions.elementToBeClickable(by)).sendKeys(input);
    }

    public void waitForElementToBePresentAndClick(By by) {
        wait.until(ExpectedConditions.presenceOfElementLocated(by)).click();
    }

    public boolean waitUntilElementIsClickableAndClick(WebElement element) {
        if (wait.until(ExpectedConditions.elementToBeClickable(element)).isDisplayed()) {
            element.click();
        }
        return true;
    }

    public void setContextToWeb() {
        String platform = getParameterFromXMLFile("platform");

        if (platform.equalsIgnoreCase("iOS")) {
            Set<String> contexts = ((IOSDriver)driver).getContextHandles();

            for (String context : contexts) {
                if (context.contains("WEB")) {
                    ((IOSDriver)driver).context(context);
                    break;
                }
            }
        } else if (platform.equalsIgnoreCase("Android")) {
            Set<String> contexts = ((AndroidDriver)driver).getContextHandles();

            for (String context : contexts) {
                if (context.contains("WEB")) {
                    ((AndroidDriver)driver).context(context);
                    break;
                }
            }
        }
    }

    public void closeKeyboardIfAndroid(String platform) {
        if (platform.equalsIgnoreCase("Android")) {
            Map<String, Object> args = new HashMap<>();
            args.put("command", "input");
            List<String> newArrayList = new ArrayList<String>();
            newArrayList.add("keyevent");
            newArrayList.add("111");
            args.put("args", newArrayList);
            String output = (String) ((AndroidDriver) driver).executeScript("mobile: shell", args);
            System.out.println(output);
        }
    }

    public String getParameterFromXMLFile(String parameterName) {
        parameterName = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter(parameterName);
        return parameterName;
    }

}
