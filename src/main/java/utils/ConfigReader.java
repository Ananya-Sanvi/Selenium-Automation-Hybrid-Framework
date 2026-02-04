package utils;

import constants.FrameworkConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Reads configuration from properties file
 * 
 * Purpose: Centralized configuration management
 * Pattern: Singleton
 * Thread-Safe: Yes
 * 
 * @author Automation Team
 * @version 1.0
 */
public final class ConfigReader {
    
    private static Properties properties;
    private static final Object lock = new Object();
    
    // Private constructor to prevent instantiation
    private ConfigReader() {
        throw new AssertionError("ConfigReader class cannot be instantiated");
    }
    
    /**
     * Load properties file (called once during initialization)
     */
    static {
        try {
            loadProperties();
        } catch (IOException e) {
            LogUtil.error("Failed to load configuration file: " + e.getMessage());
            throw new RuntimeException("Configuration file not loaded", e);
        }
    }
    
    /**
     * Load properties from config file
     * @throws IOException if file not found or cannot be read
     */
    private static void loadProperties() throws IOException {
        synchronized (lock) {
            if (properties == null) {
                properties = new Properties();
                FileInputStream fis = new FileInputStream(FrameworkConstants.CONFIG_FILE_PATH);
                properties.load(fis);
                fis.close();
                LogUtil.info("Configuration loaded successfully from: " + 
                            FrameworkConstants.CONFIG_FILE_PATH);
            }
        }
    }
    
    /**
     * Reload properties (useful for runtime changes)
     */
    public static void reloadProperties() {
        synchronized (lock) {
            properties = null;
            try {
                loadProperties();
            } catch (IOException e) {
                LogUtil.error("Failed to reload properties: " + e.getMessage());
            }
        }
    }
    
    /**
     * Get property value by key
     * @param key property key
     * @return property value or null if not found
     */
    private static String getProperty(String key) {
        // First check system property (command line override)
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isEmpty()) {
            return systemValue;
        }
        
        // Then check properties file
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            LogUtil.warn("Property '" + key + "' not found in config file");
        }
        return value;
    }
    
    /**
     * Get property with default value
     * @param key property key
     * @param defaultValue default value if key not found
     * @return property value or default
     */
    private static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
    
    // ==================== APPLICATION URL ====================
    
    /**
     * Get application URL based on environment
     * @return application URL
     */
    public static String getUrl() {
        String environment = getEnvironment();
        String url = getProperty(environment + ".url");
        
        if (url == null) {
            LogUtil.warn("URL not found for environment: " + environment + 
                        ". Using default URL.");
            url = getProperty("url", "https://www.saucedemo.com/");
        }
        
        return url;
    }
    
    // ==================== BROWSER CONFIGURATION ====================
    
    /**
     * Get browser name
     * @return browser name (chrome, firefox, edge)
     */
    public static String getBrowser() {
        return getProperty("browser", FrameworkConstants.DEFAULT_BROWSER);
    }
    
    /**
     * Check if headless mode is enabled
     * @return true if headless, false otherwise
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", 
               String.valueOf(FrameworkConstants.HEADLESS_MODE)));
    }
    
    // ==================== ENVIRONMENT CONFIGURATION ====================
    
    /**
     * Get current environment
     * @return environment name (qa, stage, prod)
     */
    public static String getEnvironment() {
        return getProperty("environment", FrameworkConstants.DEFAULT_ENVIRONMENT);
    }
    
    // ==================== EXECUTION MODE ====================
    
    /**
     * Get execution mode
     * @return local or remote
     */
    public static String getExecutionMode() {
        return getProperty("execution.mode", FrameworkConstants.EXECUTION_MODE);
    }
    
    /**
     * Check if execution is remote (Grid)
     * @return true if remote execution
     */
    public static boolean isRemoteExecution() {
        return "remote".equalsIgnoreCase(getExecutionMode());
    }
    
    /**
     * Get Selenium Grid URL
     * @return Grid hub URL
     */
    public static String getGridUrl() {
        return getProperty("grid.url", FrameworkConstants.GRID_URL);
    }
    
    // ==================== TIMEOUT CONFIGURATION ====================
    
    /**
     * Get implicit wait timeout
     * @return timeout in seconds
     */
    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait", 
               String.valueOf(FrameworkConstants.IMPLICIT_WAIT)));
    }
    
    /**
     * Get explicit wait timeout
     * @return timeout in seconds
     */
    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait", 
               String.valueOf(FrameworkConstants.EXPLICIT_WAIT)));
    }
    
    /**
     * Get page load timeout
     * @return timeout in seconds
     */
    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout", 
               String.valueOf(FrameworkConstants.PAGE_LOAD_TIMEOUT)));
    }
    
    // ==================== RETRY CONFIGURATION ====================
    
    /**
     * Get retry count for failed tests
     * @return retry count
     */
    public static int getRetryCount() {
        String retryEnabled = getProperty("retry.enabled", "true");
        if ("false".equalsIgnoreCase(retryEnabled)) {
            return 0; // Retry disabled
    }
    
        return Integer.parseInt(getProperty("retry.count", 
           String.valueOf(FrameworkConstants.MAX_RETRY_COUNT)));
    }
    
    // ==================== SCREENSHOT CONFIGURATION ====================
    
    /**
     * Check if screenshot should be captured on pass
     * @return true if enabled
     */
    public static boolean captureScreenshotOnPass() {
        return Boolean.parseBoolean(getProperty("screenshot.on.pass", 
               String.valueOf(FrameworkConstants.CAPTURE_SCREENSHOT_ON_PASS)));
    }
    
    /**
     * Check if screenshot should be captured on fail
     * @return true if enabled
     */
    public static boolean captureScreenshotOnFail() {
        return Boolean.parseBoolean(getProperty("screenshot.on.fail", 
               String.valueOf(FrameworkConstants.CAPTURE_SCREENSHOT_ON_FAIL)));
    }
    
    // ==================== PARALLEL EXECUTION ====================
    
    /**
     * Get parallel thread count
     * @return thread count
     */
    public static int getThreadCount() {
        return Integer.parseInt(getProperty("parallel.threads", 
               String.valueOf(FrameworkConstants.PARALLEL_THREAD_COUNT)));
    }
    
    // ==================== CREDENTIALS (OPTIONAL - USE WITH CAUTION) ====================
    
    /**
     * Get username (if stored in config - better to use environment variables)
     * @return username
     */
    public static String getUsername() {
        return getProperty("username", "");
    }
    
    /**
     * Get password (if stored in config - better to use environment variables)
     * @return password
     */
    public static String getPassword() {
        return getProperty("password", "");
    }

    /**
     * Check if retry is enabled
     * @return true if enabled
     */
    public static boolean isRetryEnabled() {
        return Boolean.parseBoolean(getProperty("retry.enabled", "true"));
    }
}