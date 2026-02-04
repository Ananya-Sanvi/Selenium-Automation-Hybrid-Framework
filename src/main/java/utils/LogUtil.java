package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * LogUtil - Wrapper for Log4j2 logging
 * 
 * Purpose: Centralized logging utility for the framework
 * Usage: LogUtil.info("message"), LogUtil.error("message", exception)
 * 
 * Benefits:
 * - Consistent logging format
 * - Easy to switch logging framework if needed
 * - Automatic caller class detection
 * 
 * @author Automation Team
 * @version 1.0
 */
public final class LogUtil {
    
    // Private constructor to prevent instantiation
    private LogUtil() {
        throw new AssertionError("LogUtil class cannot be instantiated");
    }
    
    /**
     * Get logger for the calling class
     * @return Logger instance
     */
    private static Logger getLogger() {
        // Get the caller's class name from stack trace
        // Index 3: Thread.currentThread().getStackTrace()[0] = getStackTrace()
        //          [1] = getLogger(), [2] = info/debug/etc, [3] = actual caller
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String callerClassName = stackTrace[3].getClassName();
        return LogManager.getLogger(callerClassName);
    }
    
    /**
     * Get logger for specific class
     * @param clazz class for which logger is needed
     * @return Logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
    
    /**
     * Log INFO level message
     * @param message log message
     */
    public static void info(String message) {
        getLogger().info(message);
    }
    
    /**
     * Log INFO level message with exception
     * @param message log message
     * @param throwable exception/error
     */
    public static void info(String message, Throwable throwable) {
        getLogger().info(message, throwable);
    }
    
    /**
     * Log DEBUG level message
     * @param message log message
     */
    public static void debug(String message) {
        getLogger().debug(message);
    }
    
    /**
     * Log DEBUG level message with exception
     * @param message log message
     * @param throwable exception/error
     */
    public static void debug(String message, Throwable throwable) {
        getLogger().debug(message, throwable);
    }
    
    /**
     * Log WARN level message
     * @param message log message
     */
    public static void warn(String message) {
        getLogger().warn(message);
    }
    
    /**
     * Log WARN level message with exception
     * @param message log message
     * @param throwable exception/error
     */
    public static void warn(String message, Throwable throwable) {
        getLogger().warn(message, throwable);
    }
    
    /**
     * Log ERROR level message
     * @param message log message
     */
    public static void error(String message) {
        getLogger().error(message);
    }
    
    /**
     * Log ERROR level message with exception
     * @param message log message
     * @param throwable exception/error
     */
    public static void error(String message, Throwable throwable) {
        getLogger().error(message, throwable);
    }
    
    /**
     * Log FATAL level message
     * @param message log message
     */
    public static void fatal(String message) {
        getLogger().fatal(message);
    }
    
    /**
     * Log FATAL level message with exception
     * @param message log message
     * @param throwable exception/error
     */
    public static void fatal(String message, Throwable throwable) {
        getLogger().fatal(message, throwable);
    }
    
    // ==================== CONVENIENCE METHODS ====================
    
    /**
     * Log test step
     * @param stepDescription description of test step
     */
    public static void logStep(String stepDescription) {
        info("STEP: " + stepDescription);
    }
    
    /**
     * Log test start
     * @param testName name of the test
     */
    public static void logTestStart(String testName) {
        info("========== TEST STARTED: " + testName + " ==========");
    }
    
    /**
     * Log test end
     * @param testName name of the test
     */
    public static void logTestEnd(String testName) {
        info("========== TEST ENDED: " + testName + " ==========");
    }
    
    /**
     * Log test pass
     * @param testName name of the test
     */
    public static void logTestPass(String testName) {
        info("✓ TEST PASSED: " + testName);
    }
    
    /**
     * Log test fail
     * @param testName name of the test
     * @param reason failure reason
     */
    public static void logTestFail(String testName, String reason) {
        error("✗ TEST FAILED: " + testName + " | Reason: " + reason);
    }
    
    /**
     * Log test skip
     * @param testName name of the test
     * @param reason skip reason
     */
    public static void logTestSkip(String testName, String reason) {
        warn("⊘ TEST SKIPPED: " + testName + " | Reason: " + reason);
    }
}