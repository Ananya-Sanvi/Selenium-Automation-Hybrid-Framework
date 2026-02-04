package pages;

import base.BasePage;
import org.openqa.selenium.*;
import utils.LogUtil;

/**
 * LoginPage - Page Object for Login functionality
 * Contains all locators and methods for login page
 */
public class LoginPage extends BasePage {
    
    // Locators
    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.xpath("//div[@class='error-message-container error']/h3");
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    public void enterUsername(String username) {
        type(usernameField, username);
        LogUtil.info("Entered username: " + username);
    }
    
    public void enterPassword(String password) {
        type(passwordField, password);
        LogUtil.info("Entered password");
    }
    
    public void clickLoginButton() {
        click(loginButton);
        LogUtil.info("Clicked login button");
    }
    
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }
    
    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
