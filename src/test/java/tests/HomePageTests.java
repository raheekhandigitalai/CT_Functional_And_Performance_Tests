package tests;

import base.BaseUtil;
import helpers.PerformanceTransactions;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.NavigationBarPage;
import pages.PageObject;

import java.time.Duration;
import java.util.*;

public class HomePageTests extends BaseUtil {

    protected PageObject pageObject;
    protected HomePage homePage;
    protected NavigationBarPage navigationBarPage;

    protected String executionPlatform = null;
    protected String platform = null;

    @BeforeMethod
    public void setUpPriorToTest() {
        pageObject = new PageObject(getDriver());
        homePage = new HomePage(getDriver());
        navigationBarPage = new NavigationBarPage(getDriver());

        executionPlatform = pageObject.getParameterFromXMLFile("executionPlatform");
        platform = pageObject.getParameterFromXMLFile("platform");
    }

    @Test
    @Parameters({"NVProfile"})
    public void searchItemResponsiveness(@Optional String NVProfile) {
        pageObject.navigateToURL("https://digital.ai");

        if (executionPlatform.equalsIgnoreCase("Web")) {
            pageObject.maximizeBrowserWindow();
        } else if (executionPlatform.equalsIgnoreCase("Mobile")) {
            homePage.setContextToWeb();
            homePage.clickToCloseAllCookiesPopup();
        }

        performanceTransactions.startPerformanceTransaction(NVProfile);

        if (executionPlatform.equalsIgnoreCase("Mobile")) {
            homePage.clickOnNavigationToggle();
        }

        navigationBarPage.searchForItemInSearchField("Continuous Testing");

        pageObject.closeKeyboardIfAndroid(platform);

        navigationBarPage.clickOnEnterButton();
        navigationBarPage.verifyResultsAppearingAfterSearch();

        performanceTransactions.endPerformanceTransaction(getQaBuild() + " - Search Item Responsiveness");
    }

}
