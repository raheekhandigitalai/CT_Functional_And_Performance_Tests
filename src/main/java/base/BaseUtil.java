package base;

import com.experitest.reporter.testng.Listener;
import helpers.PerformanceTransactions;
import helpers.PropertiesReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileBrowserType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

@Listeners(Listener.class)
public class BaseUtil {

    protected DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
    protected RemoteWebDriver driver = null;
    protected PerformanceTransactions performanceTransactions;

    @BeforeMethod
    @Parameters({"executionPlatform", "platform", "deviceQuery"})
    public void setUp(String executionPlatform, String platform, @Optional String deviceQuery, @Optional Method method) throws MalformedURLException {

        if (executionPlatform.equalsIgnoreCase("Mobile")) {

            desiredCapabilities.setCapability("QA_Build", getQaBuild());
            desiredCapabilities.setCapability("testName", getQaBuild() + " - " + method.getName());
            desiredCapabilities.setCapability("accessKey", new PropertiesReader().getProperty("seetest.accessKey"));
            desiredCapabilities.setCapability("appiumVersion", "1.22.2");

            if (platform.equalsIgnoreCase("iOS")) {

                desiredCapabilities.setCapability("automationName", "XCUITest");
                desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.IOS);
                desiredCapabilities.setCapability("deviceQuery", deviceQuery);
                desiredCapabilities.setCapability("autoAcceptAlerts", true);
                desiredCapabilities.setBrowserName(MobileBrowserType.SAFARI);
                driver = new IOSDriver(new URL(new PropertiesReader().getProperty("seetest.cloudURL")), desiredCapabilities);

            } else if (platform.equalsIgnoreCase("Android")) {

                desiredCapabilities.setCapability("automationName", "UIAutomator2");
                desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
                desiredCapabilities.setCapability("deviceQuery", deviceQuery);
                desiredCapabilities.setBrowserName(MobileBrowserType.CHROME);
                driver = new AndroidDriver(new URL(new PropertiesReader().getProperty("seetest.cloudURL")), desiredCapabilities);

            }

        } else if (executionPlatform.equalsIgnoreCase("Web")) {

            desiredCapabilities.setCapability("experitest:QA_Build", getQaBuild());
            desiredCapabilities.setCapability("experitest:testName", getQaBuild() + " - " + method.getName());
            desiredCapabilities.setCapability("experitest:accessKey", new PropertiesReader().getProperty("seetest.accessKey"));
            desiredCapabilities.setCapability("experitest:useNV", true);

            if (platform.equalsIgnoreCase("Chrome")) {
                desiredCapabilities.setCapability(CapabilityType.BROWSER_NAME, "chrome");
            } else if (platform.equalsIgnoreCase("MicrosoftEdge")) {
                desiredCapabilities.setCapability(CapabilityType.BROWSER_NAME, "MicrosoftEdge");
            } else if (platform.equalsIgnoreCase("Firefox")) {
                desiredCapabilities.setCapability(CapabilityType.BROWSER_NAME, "firefox");
            } else if (platform.equalsIgnoreCase("Safari")) {
                desiredCapabilities.setCapability(CapabilityType.BROWSER_NAME, "safari");
            }

            driver = new RemoteWebDriver(new URL(new PropertiesReader().getProperty("seetest.cloudURL")), desiredCapabilities);

        }

        if (platform.equalsIgnoreCase("iOS")) {
            performanceTransactions = new PerformanceTransactions((IOSDriver) driver);
        } else if (platform.equalsIgnoreCase("Android")) {
            performanceTransactions = new PerformanceTransactions((AndroidDriver) driver);
        } else {
            performanceTransactions = new PerformanceTransactions(driver);
        }

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.quit();
    }

//    @AfterClass
//    public void compareTransactions() {
//        performanceTransactions.compareTransactions("1.2742", 4, getQaBuild() + " - verifyUserLoggedIn");
//        performanceTransactions.compareTransactions("1.2742", 4, getQaBuild() + " - verifyInvalidCredentialsInput");
//    }

    public String getQaBuild() {
        return new PropertiesReader().getProperty("QABuild");
    }

}
