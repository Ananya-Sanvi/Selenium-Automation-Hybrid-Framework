package utils;


import constants.FrameworkConstants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.time.Duration;

/**
 * DriverManager - Manages WebDriver lifecycle
 * Uses ThreadLocal for thread-safe parallel execution
 * Singleton pattern ensures proper driver management
 */
public class DriverManager {
    
    // ThreadLocal ensures each thread has its own WebDriver instance
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    
    /**
     * Get WebDriver instance for current thread
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driver.get();
    }
    
    /**
     * Set WebDriver instance for current thread
     * @param driverInstance - WebDriver to set
     */
    private static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }
    
    /**
     * Initialize WebDriver based on config.properties
     * Sets up browser with options and timeouts
     */
    public static void initializeDriver() {
        String browser = ConfigReader.getBrowser().toLowerCase();
        boolean headless = ConfigReader.isHeadless();
        
        WebDriver webDriver = null;
        
        switch (browser) {
            case FrameworkConstants.CHROME:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--no-sandbox");
                webDriver = new ChromeDriver(chromeOptions);
                break;
                
            case FrameworkConstants.FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }
                webDriver = new FirefoxDriver(firefoxOptions);
                webDriver.manage().window().maximize();
                break;
                
            case FrameworkConstants.EDGE:
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless=new");
                }
                edgeOptions.addArguments("--start-maximized");
                webDriver = new EdgeDriver(edgeOptions);
                break;
                
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        
        // Set timeouts
        webDriver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(ConfigReader.getImplicitWait()));
        webDriver.manage().timeouts().pageLoadTimeout(
            Duration.ofSeconds(FrameworkConstants.PAGE_LOAD_TIMEOUT));
        
        setDriver(webDriver);
        LogUtil.info("Browser initialized: " + browser + " | Headless: " + headless);
    }
    
    /**
     * Quit WebDriver and remove from ThreadLocal
     * Should be called in @AfterMethod or @AfterClass
     */
    public static void quitDriver() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
            LogUtil.info("Browser closed successfully");
        }
    }
}
