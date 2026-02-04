package utils;

import constants.FrameworkConstants;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtil - Handles screenshot capture
 * Takes screenshots on test failure for debugging
 */
public class ScreenshotUtil {
    
    /**
     * Capture screenshot and save to file
     * @param driver - WebDriver instance
     * @param screenshotName - Name for the screenshot
     * @return Full path of saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String fileName = screenshotName + "_" + timestamp + ".png";
        String destination = FrameworkConstants.SCREENSHOTS_PATH + File.separator + fileName;
        
        try {
            // Create screenshots directory if not exists
            File screenshotDir = new File(FrameworkConstants.SCREENSHOTS_PATH);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File finalDestination = new File(destination);
            FileUtils.copyFile(source, finalDestination);
            LogUtil.info("Screenshot captured: " + fileName);
            return destination;
        } catch (IOException e) {
            LogUtil.error("Failed to capture screenshot", e);
            return null;
        }
    }
    
    /**
     * Get screenshot as Base64 string for embedding in reports
     * @param driver - WebDriver instance
     * @return Base64 encoded screenshot
     */
    public static String getBase64Screenshot(WebDriver driver) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        return ts.getScreenshotAs(OutputType.BASE64);
    }
}