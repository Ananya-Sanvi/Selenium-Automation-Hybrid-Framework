package utils;

//import constants.FrameworkConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.NoSuchElementException;

/**
 * WaitHelper - Provides various wait strategies
 * Helps handle dynamic elements and synchronization issues
 */
public class WaitHelper {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 
            Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }
    
    /**
     * Wait for element to be visible
     * @param locator - By locator
     * @return WebElement when visible
     */
    public WebElement waitForElementVisible(By locator) {
        LogUtil.debug("Waiting for element to be visible: " + locator);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be clickable
     * @param locator - By locator
     * @return WebElement when clickable
     */
    public WebElement waitForElementClickable(By locator) {
        LogUtil.debug("Waiting for element to be clickable: " + locator);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for element to be present in DOM
     * @param locator - By locator
     * @return WebElement when present
     */
    public WebElement waitForElementPresent(By locator) {
        LogUtil.debug("Waiting for element to be present: " + locator);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Wait for element to become invisible
     * @param locator - By locator
     * @return true when invisible
     */
    public boolean waitForElementInvisible(By locator) {
        LogUtil.debug("Waiting for element to be invisible: " + locator);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for page to load completely
     */
    public void waitForPageLoad() {
        wait.until(webDriver -> 
            ((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
        LogUtil.debug("Page loaded completely");
    }
    
    /**
     * Fluent wait with custom timeout and polling
     * @param locator - By locator
     * @param timeoutSeconds - Maximum wait time
     * @param pollingSeconds - Polling interval
     * @return WebElement when found
     */
    public WebElement fluentWait(By locator, int timeoutSeconds, int pollingSeconds) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(timeoutSeconds))
            .pollingEvery(Duration.ofSeconds(pollingSeconds))
            .ignoring(NoSuchElementException.class);
        
        LogUtil.debug("Applying fluent wait for: " + locator);
        return fluentWait.until(driver -> driver.findElement(locator));
    }
}
