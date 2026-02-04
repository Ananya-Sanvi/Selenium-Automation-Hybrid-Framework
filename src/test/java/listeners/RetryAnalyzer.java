package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utils.ConfigReader;
import utils.LogUtil;

/**
 * RetryAnalyzer - Automatically retry failed tests
 * 
 * Purpose: Handle flaky tests by retrying them before marking as failed
 * Implements: IRetryAnalyzer interface from TestNG
 * 
 * Configuration: Retry count is read from config.properties (retry.count)
 * 
 * @author Automation Team
 * @version 1.0
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    
    private int retryCount = 0;
    private int maxRetryCount;
    
    /**
     * Constructor - Initialize max retry count from config
     */
    public RetryAnalyzer() {
        this.maxRetryCount = ConfigReader.getRetryCount();
    }
    
    /**
     * Retry logic for failed tests
     * 
     * @param result Test result object
     * @return true if test should be retried, false otherwise
     */
    @Override
    public boolean retry(ITestResult result) {
        // Only retry if test failed (not skipped)
        if (!result.isSuccess()) {
            if (retryCount < maxRetryCount) {
                retryCount++;
                
                String testName = result.getMethod().getMethodName();
                LogUtil.warn("Retrying test: " + testName + 
                           " | Attempt: " + retryCount + 
                           " of " + maxRetryCount);
                
                // Mark test as failed for this attempt
                result.setStatus(ITestResult.FAILURE);
                
                return true; // Retry the test
            } else {
                // Max retry count reached
                LogUtil.error("Test failed after " + maxRetryCount + 
                            " retries: " + result.getMethod().getMethodName());
                result.setStatus(ITestResult.FAILURE);
            }
        } else {
            // Test passed
            result.setStatus(ITestResult.SUCCESS);
        }
        
        return false; // Don't retry
    }
    
    /**
     * Get current retry count
     * @return current retry count
     */
    public int getRetryCount() {
        return retryCount;
    }
    
    /**
     * Get max retry count
     * @return max retry count
     */
    public int getMaxRetryCount() {
        return maxRetryCount;
    }
}
