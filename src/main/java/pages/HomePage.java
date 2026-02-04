package pages;

import base.BasePage;
import org.openqa.selenium.*;

public class HomePage extends BasePage {
    
    private By dashboardHeader = By.xpath("//div[@class='app_logo']");
    private By userDropdown = By.id("react-burger-menu-btn");
    private By logoutLink = By.linkText("Logout");
    
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    public boolean isDashboardDisplayed() {
        return isDisplayed(dashboardHeader);
    }
    
    public String getDashboardHeaderText() {
        return getText(dashboardHeader);
    }
    
    public void logout() {
        click(userDropdown);
        click(logoutLink);
    }
}
