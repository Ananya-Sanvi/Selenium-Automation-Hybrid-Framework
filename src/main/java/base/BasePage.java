package base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import utils.WaitHelper;

public class BasePage {
    
    protected WebDriver driver;
    protected WaitHelper waitHelper;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
    }
    
    protected void click(By locator) {
        waitHelper.waitForElementClickable(locator).click();
    }
    
    protected void type(By locator, String text) {
        WebElement element = waitHelper.waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }
    
    protected String getText(By locator) {
        return waitHelper.waitForElementVisible(locator).getText();
    }
    
    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
