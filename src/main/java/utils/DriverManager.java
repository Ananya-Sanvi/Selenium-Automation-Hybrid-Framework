package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.ConfigReader;
import utils.LogUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * DriverManager - Manages WebDriver instances with Grid support
 * 
 * Supports both local and remote (Selenium Grid) execution
 * Thread-safe for parallel test execution
 * 
 * @author Automation Team
 * @version 2.0
 */
public class DriverManager {
    
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    
    /**
     * Get WebDriver instance for current thread
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driver.get();
    }
    
    /**
     * Initialize WebDriver based on configuration
     * Supports both local and remote execution
     */
    public static void initializeDriver() {
        String browser = ConfigReader.getBrowser().toLowerCase();
        boolean isRemote = ConfigReader.isRemoteExecution();
        
        LogUtil.info("Initializing driver - Browser: " + browser + 
                    ", Execution Mode: " + (isRemote ? "Remote (Grid)" : "Local"));
        
        WebDriver webDriver;
        
        if (isRemote) {
            webDriver = createRemoteDriver(browser);
        } else {
            webDriver = createLocalDriver(browser);
        }
        
        // Set implicit wait
        webDriver.manage().timeouts()
            .implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        
        // Set page load timeout
        webDriver.manage().timeouts()
            .pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));
        
        // Maximize window if configured
        if (!ConfigReader.isHeadless()) {
            webDriver.manage().window().maximize();
        }
        
        driver.set(webDriver);
        LogUtil.info("Driver initialized successfully");
    }
    
    /**
     * Create local WebDriver instance
     * @param browser browser name
     * @return WebDriver instance
     */
    private static WebDriver createLocalDriver(String browser) {
        WebDriver webDriver;
        
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                webDriver = new ChromeDriver(getChromeOptions());
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                webDriver = new FirefoxDriver(getFirefoxOptions());
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                webDriver = new EdgeDriver(getEdgeOptions());
                break;
                
            default:
                LogUtil.warn("Invalid browser: " + browser + ". Using Chrome as default.");
                WebDriverManager.chromedriver().setup();
                webDriver = new ChromeDriver(getChromeOptions());
        }
        
        return webDriver;
    }
    
    /**
     * Create remote WebDriver instance for Selenium Grid
     * @param browser browser name
     * @return RemoteWebDriver instance
     */
    private static WebDriver createRemoteDriver(String browser) {
        String gridUrl = ConfigReader.getGridUrl();
        
        try {
            URL hubUrl = new URL(gridUrl + "/wd/hub");
            RemoteWebDriver remoteDriver;
            
            switch (browser.toLowerCase()) {
                case "chrome":
                    remoteDriver = new RemoteWebDriver(hubUrl, getChromeOptions());
                    break;
                    
                case "firefox":
                    remoteDriver = new RemoteWebDriver(hubUrl, getFirefoxOptions());
                    break;
                    
                case "edge":
                    remoteDriver = new RemoteWebDriver(hubUrl, getEdgeOptions());
                    break;
                    
                default:
                    LogUtil.warn("Invalid browser: " + browser + ". Using Chrome as default.");
                    remoteDriver = new RemoteWebDriver(hubUrl, getChromeOptions());
            }
            
            LogUtil.info("Remote driver created - Grid URL: " + gridUrl + 
                        ", Browser: " + browser);
            return remoteDriver;
            
        } catch (MalformedURLException e) {
            LogUtil.error("Invalid Grid URL: " + gridUrl, e);
            throw new RuntimeException("Failed to create remote driver", e);
        }
    }
    
    /**
     * Get Chrome options with common configurations
     * @return ChromeOptions
     */
    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        
        // Headless mode
        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless=new");
        }
        
        // Common arguments
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        
        // Disable notifications
        options.addArguments("--disable-notifications");
        
        // Performance improvements
        options.setExperimentalOption("excludeSwitches", 
            new String[]{"enable-automation", "enable-logging"});
        
        LogUtil.debug("Chrome options configured");
        return options;
    }
    
    /**
     * Get Firefox options with common configurations
     * @return FirefoxOptions
     */
    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        
        // Headless mode
        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless");
        }
        
        // Common arguments
        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        
        // Disable notifications
        options.addPreference("dom.webnotifications.enabled", false);
        
        LogUtil.debug("Firefox options configured");
        return options;
    }
    
    /**
     * Get Edge options with common configurations
     * @return EdgeOptions
     */
    private static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        
        // Headless mode
        if (ConfigReader.isHeadless()) {
            options.addArguments("--headless=new");
        }
        
        // Common arguments
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-extensions");
        
        LogUtil.debug("Edge options configured");
        return options;
    }
    
    /**
     * Quit and remove driver from current thread
     */
    public static void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            try {
                webDriver.quit();
                LogUtil.info("Driver quit successfully");
            } catch (Exception e) {
                LogUtil.warn("Error while quitting driver: " + e.getMessage());
            } finally {
                driver.remove();
            }
        }
    }
}