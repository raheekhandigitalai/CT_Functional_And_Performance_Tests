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
    protected ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();
    protected PerformanceTransactions performanceTransactions;

    public RemoteWebDriver getDriver() {
        return driver.get();
    }

    @BeforeMethod
    @Parameters({"executionPlatform", "platform", "deviceQuery"})
    public void setUp(String executionPlatform, String platform, @Optional String deviceQuery, @Optional Method method) throws MalformedURLException {

//        desiredCapabilities.setCapability("experitest:accessKey", new PropertiesReader().getProperty("seetest.accessKey"));
//        desiredCapabilities.setCapability("experitest:QA_Build", getQaBuild());
        desiredCapabilities.setCapability("experitest:accessKey", System.getenv("ACCESS_KEY"));
        desiredCapabilities.setCapability("experitest:QA_Build", System.getenv("QA_BUILD"));
        desiredCapabilities.setCapability("Jenkins_Build_Number", System.getenv("BUILD_NUMBER"));
        desiredCapabilities.setCapability("experitest:testName", getQaBuild() + " - " + method.getName());

        if (executionPlatform.equalsIgnoreCase("Mobile")) {

            desiredCapabilities.setCapability("appiumVersion", "1.22.2");

            if (platform.equalsIgnoreCase("iOS")) {

                desiredCapabilities.setCapability("automationName", "XCUITest");
                desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.IOS);
                desiredCapabilities.setCapability("deviceQuery", deviceQuery);
                desiredCapabilities.setCapability("autoAcceptAlerts", true);
                desiredCapabilities.setBrowserName(MobileBrowserType.SAFARI);
                driver.set(new IOSDriver(new URL(new PropertiesReader().getProperty("seetest.cloudURL")), desiredCapabilities));

            } else if (platform.equalsIgnoreCase("Android")) {

                desiredCapabilities.setCapability("automationName", "UIAutomator2");
                desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
                desiredCapabilities.setCapability("deviceQuery", deviceQuery);
                desiredCapabilities.setBrowserName(MobileBrowserType.CHROME);
                driver.set(new AndroidDriver(new URL(new PropertiesReader().getProperty("seetest.cloudURL")), desiredCapabilities));

            }

        } else if (executionPlatform.equalsIgnoreCase("Web")) {

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

            driver.set(new RemoteWebDriver(new URL(new PropertiesReader().getProperty("seetest.cloudURL")), desiredCapabilities));

        }

        if (platform.equalsIgnoreCase("iOS")) {
            performanceTransactions = new PerformanceTransactions((IOSDriver) getDriver());
        } else if (platform.equalsIgnoreCase("Android")) {
            performanceTransactions = new PerformanceTransactions((AndroidDriver) getDriver());
        } else {
            performanceTransactions = new PerformanceTransactions(getDriver());
        }

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        getDriver().quit();
        driver.remove();
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
