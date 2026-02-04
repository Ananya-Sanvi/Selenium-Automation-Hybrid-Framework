package actions;

import org.openqa.selenium.WebDriver;
import pages.*;
import utils.LogUtil;

/**
 * LoginAction - Business logic for login functionality
 * Combines multiple page actions into workflows
 */
public class LoginAction {
    
    private WebDriver driver;
    private LoginPage loginPage;
    private HomePage homePage;
    
    public LoginAction(WebDriver driver) {
        this.driver = driver;
        this.loginPage = new LoginPage(driver);
        this.homePage = new HomePage(driver);
    }
    
    public boolean performLogin(String username, String password) {
        LogUtil.info("Performing login with username: " + username);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
        
        boolean loginSuccess = homePage.isDashboardDisplayed();
        LogUtil.info("Login " + (loginSuccess ? "successful" : "failed"));
        return loginSuccess;
    }
    
    public String getLoginErrorMessage() {
        return loginPage.getErrorMessage();
    }
}
