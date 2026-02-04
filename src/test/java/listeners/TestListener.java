package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import reports.ExtentReportManager;
import utils.LogUtil;
import utils.ScreenshotUtil;

import java.util.Arrays;

/**
 * TestListener - TestNG listener for Extent Reports
 * 
 * Updated to work with extent.properties configuration
 * All report settings are now loaded from extent.properties file
 * 
 * @author Automation Team
 * @version 2.0
 */
public class TestListener implements ITestListener {
    
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    /**
     * Called before any test starts
     * Initialize ExtentReports (configuration loaded from extent.properties)
     */
    @Override
    public void onStart(ITestContext context) {
        // ExtentReportManager.getInstance() now automatically loads extent.properties
        extent = ExtentReportManager.getInstance();
        
        LogUtil.info("==============================================");
        LogUtil.info("Test Suite Started: " + context.getName());
        LogUtil.info("==============================================");
    }
    
    /**
     * Called when test method starts
     */
    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        
        // Create test in Extent Report
        ExtentTest extentTest;
        if (description != null && !description.isEmpty()) {
            extentTest = extent.createTest(testName, description);
        } else {
            extentTest = extent.createTest(testName);
        }
        
        test.set(extentTest);
        
        // Assign categories/groups if any
        String[] groups = result.getMethod().getGroups();
        if (groups.length > 0) {
            for (String group : groups) {
                test.get().assignCategory(group);
            }
        }
        
        // Log test start
        test.get().info("Test Started: " + testName);
        LogUtil.logTestStart(testName);
    }
    
    /**
     * Called when test method succeeds
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        
        // Log success in Extent Report
        test.get().log(Status.PASS, 
            MarkupHelper.createLabel("TEST PASSED: " + testName, ExtentColor.GREEN));
        
        // Capture screenshot on pass if configured
        if (shouldCaptureScreenshotOnPass()) {
            captureScreenshot(result, "PASS");
        }
        
        LogUtil.logTestPass(testName);
    }
    
    /**
     * Called when test method fails
     */
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();
        
        // Log failure in Extent Report
        test.get().log(Status.FAIL, 
            MarkupHelper.createLabel("TEST FAILED: " + testName, ExtentColor.RED));
        
        // Log exception details
        if (throwable != null) {
            test.get().fail(throwable);
            LogUtil.logTestFail(testName, throwable.getMessage());
        }
        
        // Capture screenshot on failure
        captureScreenshot(result, "FAIL");
    }
    
    /**
     * Called when test method is skipped
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Throwable throwable = result.getThrowable();
        
        // Log skip in Extent Report
        test.get().log(Status.SKIP, 
            MarkupHelper.createLabel("TEST SKIPPED: " + testName, ExtentColor.YELLOW));
        
        if (throwable != null) {
            test.get().skip(throwable);
            LogUtil.logTestSkip(testName, throwable.getMessage());
        } else {
            LogUtil.logTestSkip(testName, "Test was skipped");
        }
    }
    
    /**
     * Called when test method fails but within success percentage
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        test.get().log(Status.WARNING, 
            "Test failed but within success percentage: " + testName);
    }
    
    /**
     * Called after all tests have finished
     * Flush and write the report
     */
    @Override
    public void onFinish(ITestContext context) {
        LogUtil.info("==============================================");
        LogUtil.info("Test Suite Finished: " + context.getName());
        LogUtil.info("Total Tests: " + context.getAllTestMethods().length);
        LogUtil.info("Passed: " + context.getPassedTests().size());
        LogUtil.info("Failed: " + context.getFailedTests().size());
        LogUtil.info("Skipped: " + context.getSkippedTests().size());
        LogUtil.info("==============================================");
        
        // Flush Extent Report (writes to file)
        if (extent != null) {
            extent.flush();
            LogUtil.info("Extent Report generated successfully");
        }
    }
    
    /**
     * Capture screenshot and attach to report
     */
    private void captureScreenshot(ITestResult result, String status) {
        try {
            // Get WebDriver from test class
            Object testInstance = result.getInstance();
            WebDriver driver = getDriverFromTestInstance(testInstance);
            
            if (driver != null) {
                String screenshotPath = ScreenshotUtil.captureScreenshot(
                    driver, 
                    result.getMethod().getMethodName() + "_" + status
                );
                
                // Attach screenshot to Extent Report
                test.get().addScreenCaptureFromPath(screenshotPath);
                LogUtil.info("Screenshot captured: " + screenshotPath);
            }
        } catch (Exception e) {
            LogUtil.warn("Failed to capture screenshot: " + e.getMessage());
        }
    }
    
    /**
     * Get WebDriver instance from test class
     * Assumes test class has a getDriver() method or driver field
     */
    private WebDriver getDriverFromTestInstance(Object testInstance) {
        try {
            // Try to call getDriver() method
            return (WebDriver) testInstance.getClass()
                .getMethod("getDriver")
                .invoke(testInstance);
        } catch (Exception e) {
            try {
                // Try to access driver field
                return (WebDriver) testInstance.getClass()
                    .getDeclaredField("driver")
                    .get(testInstance);
            } catch (Exception ex) {
                LogUtil.debug("Could not retrieve WebDriver from test instance");
                return null;
            }
        }
    }
    
    /**
     * Check if screenshot should be captured on pass
     * This can be read from ConfigReader if configured
     */
    private boolean shouldCaptureScreenshotOnPass() {
        // You can read this from config.properties
        // For now, returning false (only capture on failure)
        return false;
    }
}