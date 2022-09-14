package helpers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;

public class PerformanceTransactions {

    /**
     * To learn more about Performance Transactions, the following pages can be referenced:
     *
     * Start / EndPerformanceTransaction:
     * https://github.com/raheekhandigitalai/SeeTest-Specific-Implementation/blob/master/src/test/java/appium/seetest_specific/performance/PerformanceTransaction.java
     *
     * Compare Transactions:
     * https://github.com/raheekhandigitalai/SeeTest-Specific-Implementation/blob/master/src/test/java/appium/seetest_specific/performance/CompareTransactions.java
     *
     */

    protected RemoteWebDriver driver;

    public PerformanceTransactions(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void startPerformanceTransaction(String NVProfile) {
        String platform = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("platform");

        if (platform.equalsIgnoreCase("iOS")) {
            ((IOSDriver) driver).executeScript("seetest:client.startPerformanceTransaction(\"" + NVProfile + "\")");
        } else if (platform.equalsIgnoreCase("Android")) {
            ((AndroidDriver) driver).executeScript("seetest:client.startPerformanceTransaction(\"" + NVProfile + "\")");
        } else {
            driver.executeScript("seetest:client.startPerformanceTransaction(\"" + getQaBuild() + "\")");
        }
    }

    public void startPerformanceTransactionForApplication(String applicationName, String NVProfile) {
        String platform = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("platform");

        if (platform.equalsIgnoreCase("iOS")) {
            ((IOSDriver) driver).executeScript("seetest:client.startPerformanceTransactionForApplication(\"" + applicationName + "\", \"" + NVProfile + "\")");
        } else if (platform.equalsIgnoreCase("Android")) {
            ((AndroidDriver) driver).executeScript("seetest:client.startPerformanceTransactionForApplication(\"" + applicationName + "\", \"" + NVProfile + "\")");
        }

    }

    public void startPerformanceTransaction() {
        driver.executeScript("seetest:client.startPerformanceTransaction(\"" + getQaBuild() + "\")");
    }

    public String endPerformanceTransaction(String transactionName) {
        String platform = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("platform");
        Object transaction = null;

        if (platform.equalsIgnoreCase("iOS")) {
            transaction = ((IOSDriver) driver).executeScript("seetest:client.endPerformanceTransaction(\"" + transactionName + "\")");
        } else if (platform.equalsIgnoreCase("Android")) {
            transaction = ((AndroidDriver) driver).executeScript("seetest:client.endPerformanceTransaction(\"" + transactionName + "\")");
        } else {
            transaction = driver.executeScript("seetest:client.endPerformanceTransaction(\"" + transactionName + "\")");
        }

        assert transaction != null;
        return transaction.toString();
    }

    public void compareTransactions(String appVersion, int compareCount, String transactionName) {
        String request = "{\n" +
                "\t\"filter\": [\"appName\", \"=\", \"com.experitest.ExperiBank\"],\n" +
                "\t\"baseKey\": \"appVersion\",\n" +
                "\t\"baseKeyValue\": \"" + appVersion + "\",\n" +
                "\t\"compareCount\": " + compareCount + ",\n" +
                "\t\"comparisonTargets\": [\n" +
                "\t\t{ \"name\": \"" + transactionName + "\", \"measure\": \"cpuMax\", \"acceptedChange\": 10.0 },\n" +
                "\t\t{ \"name\": \"" + transactionName + "\", \"measure\": \"memMax\", \"acceptedChange\": 10.0 },\n" +
                "\t\t{ \"name\": \"" + transactionName + "\", \"measure\": \"batteryMax\", \"acceptedChange\": 10.0 },\n" +
                "\t\t{ \"name\": \"" + transactionName + "\", \"measure\": \"totalDownloadedBytes\", \"acceptedChange\": 10.0 }\n" +
                "\t]\n" +
                "}";
        HttpResponse<JsonNode> response = Unirest.post(getBaseUrl() + "?token=" + new PropertiesReader().getProperty("seetest.accesskey"))
                .header("Content-Type", "application/json")
                .body(request)
                .asJson();
        int status = response.getStatus();
        Assert.assertEquals(200, status);
        if (status == 200) {
            System.out.println(response.getBody());
        } else {
            System.err.println(response.getBody());
        }
    }

    public String getBaseUrl() {
        return new PropertiesReader().getProperty("seetest.baseurl") + "/reporter/api/transactions/compare";
    }

    public String getQaBuild() {
        return new PropertiesReader().getProperty("QABuild");
    }

}
