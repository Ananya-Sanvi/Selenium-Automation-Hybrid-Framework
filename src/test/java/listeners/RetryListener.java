package listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import utils.LogUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * RetryListener - Automatically applies RetryAnalyzer to all tests
 * 
 * Purpose: Transform @Test annotations at runtime to add retry logic
 * Implements: IAnnotationTransformer interface from TestNG
 * 
 * Usage: Add this listener to testng.xml or use @Listeners annotation
 * 
 * @author Automation Team
 * @version 1.0
 */
public class RetryListener implements IAnnotationTransformer {
    
    /**
     * Transform test annotation to add RetryAnalyzer
     * This method is called by TestNG before executing any test
     * 
     * @param annotation Test annotation object
     * @param testClass Test class
     * @param testConstructor Test constructor
     * @param testMethod Test method
     */
    @Override
    public void transform(ITestAnnotation annotation, 
                         Class testClass, 
                         Constructor testConstructor, 
                         Method testMethod) {
        
        // Set RetryAnalyzer for this test
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
        
        if (testMethod != null) {
            LogUtil.debug("RetryAnalyzer applied to: " + 
                        testMethod.getName());
        }
    }
}
