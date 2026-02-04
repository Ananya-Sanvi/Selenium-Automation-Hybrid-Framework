package tests;

import org.testng.annotations.*;
import utils.*;

public class BaseTest {
    
    @BeforeMethod
    public void setUp() {
        LogUtil.info("========== Test Setup Started ==========");
        DriverManager.initializeDriver();
        DriverManager.getDriver().get(ConfigReader.getUrl());
        LogUtil.info("Navigated to: " + ConfigReader.getUrl());
    }
    
    @AfterMethod
    public void tearDown() {
        LogUtil.info("========== Test Teardown Started ==========");
        DriverManager.quitDriver();
    }
}
