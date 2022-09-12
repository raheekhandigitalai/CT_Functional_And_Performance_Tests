package tests;

import base.BaseUtil;
import com.experitest.reporter.testng.Listener;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;

@Listeners(Listener.class)
public class BrowserTest {

    private static final String ACCESS_KEY = "eyJ4cC51Ijo3MzU0MjQsInhwLnAiOjIsInhwLm0iOiJNVFUzT0RZd016ZzFOek16TVEiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE4OTM5NjM4NTcsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.GP0hK0o0j2WEKt-J0aXsVbu1tmt-PhWUryqluokszJk";
    private RemoteWebDriver driver;
    private URL url;
    private DesiredCapabilities dc = new DesiredCapabilities();

    @BeforeMethod
    public void setUp(Method method) throws Exception {
        dc.setCapability("experitest:testName", method.getName());
        dc.setCapability("experitest:accessKey", ACCESS_KEY);
        dc.setCapability(CapabilityType.BROWSER_NAME, "safari"); // chrome / MicrosoftEdge / firefox / safari
        dc.setCapability("useNV", true);
        driver = new RemoteWebDriver(new URL("https://uscloud.experitest.com/wd/hub"), dc);
    }

    @Test
    public void browsertest() {
        driver.manage().window().maximize();
        driver.get("https://digital.ai");

        driver.executeScript("seetest:client.startPerformanceTransaction(\"2.22.0\")");

        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='search-link']"))).click();
        driver.findElement(By.xpath("//*[@name='search']")).sendKeys("Continuous Testing");
        driver.findElement(By.xpath("//*[@type='submit']")).click();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(By.xpath("(//*[@class='views-row odd'])[1]")));

        Object o = driver.executeScript("seetest:client.endPerformanceTransaction(\"2.22.0 - Search Item Responsiveness\")");
        System.out.println(o.toString());
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

}

