package tests;

import actions.LoginAction;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.*;

/**
 * LoginTest - Test cases for login functionality
 */
public class LoginTest extends BaseTest {
    
    @Test(priority = 1, description = "Verify login with valid credentials")
    public void testValidLogin() {
        LogUtil.info("Test: Valid Login");
        
        LoginAction loginAction = new LoginAction(DriverManager.getDriver());
        boolean loginSuccess = loginAction.performLogin("standard_user", "secret_sauce");
        Assert.assertTrue(loginSuccess, "Login should be successful");
        LogUtil.info("Valid login test passed");
    }
    
    @Test(priority = 2, description = "Verify login with invalid credentials")
    public void testInvalidLogin() {
        LogUtil.info("Test: Invalid Login");
        
        LoginAction loginAction = new LoginAction(DriverManager.getDriver());
        boolean loginSuccess = loginAction.performLogin("Admin", "wrongpassword");
        
        Assert.assertFalse(loginSuccess, "Login should fail");
        String errorMsg = loginAction.getLoginErrorMessage();
        Assert.assertTrue(errorMsg.contains("Epic sadface"), "Error message should be displayed");
        LogUtil.info("Invalid login test passed");
    }
    
    @Test(priority = 3, dataProvider = "loginData")
    public void testLoginWithDataProvider(String username, String password) {
        LoginAction loginAction = new LoginAction(DriverManager.getDriver());
        loginAction.performLogin(username, password);
    }
    
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return ExcelReader.getTestData("TestData.xlsx", "Sheet1");
    }
}
