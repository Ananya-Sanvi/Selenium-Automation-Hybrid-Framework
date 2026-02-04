package reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import constants.FrameworkConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * ExtentManager - Manages Extent Reports configuration
 * Loads settings from extent.properties file
 */
public class ExtentReportManager {
    
    private static ExtentReports extent;
    private static ExtentSparkReporter sparkReporter;
    private static Properties properties;
    private static final String EXTENT_PROPERTIES_PATH = "src/test/resources/extent.properties";
    
    /**
     * Get or create ExtentReports instance
     * @return ExtentReports instance
     */
    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }
    
    /**
     * Create ExtentReports instance with configuration from properties file
     */
    private static void createInstance() {
        // Load properties
        loadProperties();
        
        // Get report path from properties or use default
        String reportPath = getProperty("extent.reporter.spark.out", 
                                       FrameworkConstants.EXTENT_REPORT_PATH);
        
        // Generate timestamped report path
        String timestampedReportPath = generateTimestampedReportPath(reportPath);
        
        // Create directory if not exists
        new File(timestampedReportPath).getParentFile().mkdirs();
        
        // Initialize Spark Reporter
        sparkReporter = new ExtentSparkReporter(timestampedReportPath);
        
        // Configure Spark Reporter from properties
        configureSparkReporter();
        
        // Create ExtentReports instance
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        
        // Set system information from properties
        setSystemInformation();
        
        // Log report location
        System.out.println("Extent Report will be generated at: " + timestampedReportPath);
    }
    
    /**
     * Generate timestamped report path
     * Format: reports/ExtentReport_dd-MM-yyyy_HH-mm-ss.html
     */
    private static String generateTimestampedReportPath(String basePath) {
        // Get timestamp format from properties or use default
        String timestampFormat = getProperty("extent.reporter.timestamp.format", "dd-MM-yyyy_HH-mm-ss");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(timestampFormat);
        String timestamp = dateFormat.format(new Date());
        
        // Extract directory and filename
        File file = new File(basePath);
        String directory = file.getParent();
        String filename = file.getName();
        
        // Remove extension
        String nameWithoutExtension = filename.substring(0, filename.lastIndexOf('.'));
        String extension = filename.substring(filename.lastIndexOf('.'));
        
        // Create timestamped filename
        String timestampedFilename = nameWithoutExtension + "_" + timestamp + extension;
        
        return directory + File.separator + timestampedFilename;
    }
    
    // ...existing code...
    
    /**
     * Load extent.properties file
     */
    private static void loadProperties() {
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(EXTENT_PROPERTIES_PATH);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            System.err.println("Warning: extent.properties not found. Using default configuration.");
            properties = new Properties(); // Use empty properties
        }
    }
    
    /**
     * Get property value with default
     */
    private static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Configure Spark Reporter from properties
     */
    private static void configureSparkReporter() {
        // Document Title
        String documentTitle = getProperty("extent.reporter.spark.config.documentTitle", 
                                          FrameworkConstants.DOCUMENT_TITLE);
        sparkReporter.config().setDocumentTitle(documentTitle);
        
        // Report Name
        String reportName = getProperty("extent.reporter.spark.config.reportName", 
                                       FrameworkConstants.REPORT_NAME);
        sparkReporter.config().setReportName(reportName);
        
        // Theme
        String theme = getProperty("extent.reporter.spark.config.theme", "standard");
        if ("dark".equalsIgnoreCase(theme)) {
            sparkReporter.config().setTheme(Theme.DARK);
        } else {
            sparkReporter.config().setTheme(Theme.STANDARD);
        }
        
        // Timestamp Format
        String timeStampFormat = getProperty("extent.reporter.spark.config.timeStampFormat", 
                                            "dd MMM yyyy HH:mm:ss");
        sparkReporter.config().setTimeStampFormat(timeStampFormat);
        
        // Encoding
        String encoding = getProperty("extent.reporter.spark.config.encoding", "UTF-8");
        sparkReporter.config().setEncoding(encoding);
        
        // Offline mode
        String offline = getProperty("extent.reporter.spark.config.offline", "false");
        sparkReporter.config().setOfflineMode(Boolean.parseBoolean(offline));
        
        // Timeline
        String enableTimeline = getProperty("extent.reporter.spark.config.enableTimeline", "true");
        sparkReporter.config().setTimelineEnabled(Boolean.parseBoolean(enableTimeline));
        
        // CSS (if provided)
        String cssPath = getProperty("extent.reporter.spark.config.css", null);
        if (cssPath != null && !cssPath.isEmpty()) {
            sparkReporter.config().setCss(cssPath);
        }
        
        // JavaScript (if provided)
        String jsPath = getProperty("extent.reporter.spark.config.js", null);
        if (jsPath != null && !jsPath.isEmpty()) {
            sparkReporter.config().setJs(jsPath);
        }
    }
    
    /**
     * Set system information from properties
     */
    private static void setSystemInformation() {
        // Iterate through all properties starting with "systeminfo."
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("systeminfo.")) {
                String systemKey = key.replace("systeminfo.", "");
                String systemValue = properties.getProperty(key);
                extent.setSystemInfo(systemKey, systemValue);
            }
        }
        
        // Add runtime information
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User Directory", System.getProperty("user.dir"));
    }
    
    /**
     * Flush and write report
     */
    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}