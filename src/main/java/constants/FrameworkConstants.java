package constants;

import java.io.File;

/**
 * FrameworkConstants - Central repository for all framework constants
 * 
 * Purpose: Avoid hardcoding values throughout the framework
 * Usage: Reference constants as FrameworkConstants.PROPERTY_NAME
 * 
 * @author Automation Team
 * @version 1.0
 */
public final class FrameworkConstants {
    
    // Private constructor to prevent instantiation
    private FrameworkConstants() {
        throw new AssertionError("FrameworkConstants class cannot be instantiated");
    }
    
    // ==================== PROJECT PATHS ====================
    
    private static final String PROJECT_PATH = System.getProperty("user.dir");
    private static final String RESOURCES_PATH = PROJECT_PATH + File.separator + "src" + 
                                                  File.separator + "test" + 
                                                  File.separator + "resources";
    
    // ==================== CONFIGURATION FILES ====================
    
    public static final String CONFIG_FILE_PATH = RESOURCES_PATH + File.separator + 
                                                   "config" + File.separator + 
                                                   "config.properties";
    
    // ==================== TEST DATA PATHS ====================
    
    public static final String TEST_DATA_PATH = RESOURCES_PATH + File.separator + "testdata/";
    public static final String EXCEL_FILE_PATH = TEST_DATA_PATH + File.separator + "TestData.xlsx";
    public static final String JSON_FILE_PATH = TEST_DATA_PATH + File.separator + "TestData.json";
    
    // ==================== OUTPUT PATHS ====================
    
    public static final String OUTPUT_PATH = PROJECT_PATH + File.separator + "test-output";
    public static final String SCREENSHOTS_PATH = OUTPUT_PATH + File.separator + "screenshots";
    public static final String REPORTS_PATH = OUTPUT_PATH + File.separator + "reports";
    public static final String EXTENT_REPORT_PATH = REPORTS_PATH + File.separator + "ExtentReport.html";
    public static final String LOGS_PATH = OUTPUT_PATH + File.separator + "logs";
    
    // ==================== TIMEOUTS (in seconds) ====================
    
    public static final int IMPLICIT_WAIT = 10;
    public static final int EXPLICIT_WAIT = 20;
    public static final int PAGE_LOAD_TIMEOUT = 30;
    public static final int SCRIPT_TIMEOUT = 15;
    public static final int FLUENT_WAIT_TIMEOUT = 30;
    public static final int FLUENT_WAIT_POLLING = 2;
    
    // ==================== RETRY CONFIGURATION ====================
    
    public static final int MAX_RETRY_COUNT = 1;
    
    // ==================== BROWSER SETTINGS ====================
    
    public static final boolean HEADLESS_MODE = false;
    public static final boolean MAXIMIZE_WINDOW = true;
    public static final String WINDOW_SIZE = "1920,1080";
    
    // ==================== EXTENT REPORT CONFIGURATION ====================
    
    public static final String REPORT_TITLE = "Automation Test Report";
    public static final String REPORT_NAME = "Test Execution Results";
    public static final String DOCUMENT_TITLE = "Automation Report";
    
    // ==================== SCREENSHOT SETTINGS ====================
    
    public static final boolean CAPTURE_SCREENSHOT_ON_PASS = false;
    public static final boolean CAPTURE_SCREENSHOT_ON_FAIL = true;
    
    // ==================== LOGGING SETTINGS ====================
    
    public static final String LOG_FILE_NAME = "automation.log";
    public static final String LOG_FILE_PATH = LOGS_PATH + File.separator + LOG_FILE_NAME;
    
    // ==================== EXECUTION SETTINGS ====================
    
    public static final String DEFAULT_BROWSER = "chrome";
    public static final String DEFAULT_ENVIRONMENT = "qa";
    public static final String EXECUTION_MODE = "local"; // local or remote
   
    
    // ==================== SELENIUM GRID SETTINGS ====================
    
    public static final String GRID_URL = "http://localhost:4444";
    public static final int PARALLEL_THREAD_COUNT = 5;
    
    // ==================== APPLICATION CONSTANTS ====================
    
    public static final String DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    public static final String SCREENSHOT_FORMAT = ".png";
    public static final String CHROME = "chrome";
    public static final String FIREFOX = "firefox";
    public static final String EDGE = "edge";
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Get full file path by appending to project path
     * @param relativePath relative path from project root
     * @return absolute file path
     */
    public static String getAbsolutePath(String relativePath) {
        return PROJECT_PATH + File.separator + relativePath;
    }
    
    /**
     * Create directories if they don't exist
     * @param path directory path to create
     */
    public static void createDirectoryIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    
    /**
     * Initialize all required directories
     */
    public static void initializeDirectories() {
        createDirectoryIfNotExists(SCREENSHOTS_PATH);
        createDirectoryIfNotExists(REPORTS_PATH);
        createDirectoryIfNotExists(LOGS_PATH);
        createDirectoryIfNotExists(TEST_DATA_PATH);
    }
}