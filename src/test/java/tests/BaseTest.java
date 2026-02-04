package tests;

import constants.FrameworkConstants;
import utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.LogUtil;

/**
 * BaseTest - Base class for all test classes
 * Supports both local and Selenium Grid execution
 * 
 * @author Automation Team
 * @version 2.0
 */
public class BaseTest {
    
    @BeforeSuite
    public void setupFramework() {
        // Initialize framework directories
        FrameworkConstants.initializeDirectories();
        LogUtil.info("Framework initialized successfully");
        LogUtil.info("Execution Mode: " + ConfigReader.getExecutionMode());
        LogUtil.info("Grid URL: " + ConfigReader.getGridUrl());
    }
    
    /**
     * Setup method - runs before each test
     * Accepts browser parameter from testng.xml
     * 
     * @param browser browser name from TestNG parameter (optional)
     */
    @BeforeMethod
    @Parameters({"browser"})
    public void setup(@Optional String browser) {
        // Override browser if provided via TestNG parameter
        if (browser != null && !browser.isEmpty()) {
            System.setProperty("browser", browser);
            LogUtil.info("Browser overridden by TestNG parameter: " + browser);
        }
        
        // Initialize driver (local or remote based on config)
        DriverManager.initializeDriver();
        
        // Get driver instance
        WebDriver driver = getDriver();
        
        // Navigate to application URL
        String url = ConfigReader.getUrl();
        driver.get(url);
        LogUtil.info("Navigated to: " + url);
    }
    
    /**
     * Teardown method - runs after each test
     */
    @AfterMethod
    public void teardown() {
        DriverManager.quitDriver();
    }
    
    /**
     * Get WebDriver instance for current thread
     * @return WebDriver
     */
    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }
    
    @AfterSuite
    public void cleanupFramework() {
        LogUtil.info("Framework cleanup completed");
    }
}