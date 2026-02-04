# Selenium Automation Hybrid Framework

A robust, scalable Test Automation Framework built with Selenium WebDriver, Java, TestNG, and Extent Reports.

## 🚀 Features

- **Hybrid Framework Architecture** - Combines Data-Driven, Keyword-Driven, and Page Object Model
- **Cross-Browser Testing** - Chrome, Firefox, Edge, Safari support
- **Parallel Execution** - Run tests concurrently using TestNG
- **Extent Reports** - Rich HTML reports with screenshots
- **Retry Mechanism** - Automatic retry for flaky tests
- **Screenshot Capture** - On failure and configurable for pass
- **Logging** - Comprehensive log4j2 logging
- **Configuration Management** - Externalized configs via properties files
- **Reusable Utilities** - Screenshot, Wait, Config, Log utilities

---

## 📋 Prerequisites

- **Java JDK** 11 or higher
- **Maven** 3.6+
- **IDE** - IntelliJ IDEA / Eclipse / VS Code
- **Git** (for version control)
- **WebDrivers** - ChromeDriver, GeckoDriver (handled by WebDriverManager)

---

## 🛠️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/Ananya-Sanvi/Selenium-Automation-Hybrid-Framework.git
cd Selenium-Automation-Hybrid-Framework
```

### 2. Install Dependencies

```bash
mvn clean install -DskipTests
```

### 3. Configure the Framework

Update **`src/main/resources/config.properties`**:

```properties
# Browser Configuration
browser=chrome
headless=false
implicit_wait=10
explicit_wait=20
page_load_timeout=30

# Application URL
base_url=https://your-application-url.com

# Screenshot Configuration
capture_screenshot_on_pass=true
```

Update **`src/main/resources/extent.properties`**:

```properties
extent.reporter.spark.start=true
extent.reporter.spark.out=reports/extent-report.html
basefolder.name=reports
basefolder.datetimepattern=d-MMM-YY HH-mm-ss
```

### 4. Project Structure

```
selenium-tests/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── constants/      # Framework constants
│   │   │   ├── pages/          # Page Object classes
│   │   │   └── utils/          # Utility classes
│   │   └── resources/
│   │       ├── config.properties
│   │       ├── extent.properties
│   │       └── log4j2.xml
│   └── test/
│       ├── java/
│       │   ├── listeners/      # TestNG listeners
│       │   └── tests/          # Test classes
│       └── resources/
│           └── testng.xml
├── reports/                    # Test reports
├── screenshots/                # Test screenshots
├── logs/                       # Application logs
├── .gitignore
├── pom.xml
└── README.md
```

---

## ▶️ Running Tests

### Run All Tests

```bash
mvn clean test
```

### Run Specific Test Suite

```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

### Run Tests by Groups

```bash
mvn clean test -Dgroups=smoke
mvn clean test -Dgroups=regression
```

### Run Tests in Headless Mode

Update `config.properties`:
```properties
headless=true
```

Then run:
```bash
mvn clean test
```

### Run with Different Browser

```bash
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge
```

---

## 📊 Reports

### Extent Reports
- Location: `reports/extent-report.html`
- Open in browser after test execution
- Contains test results, screenshots, logs, and execution details

### TestNG Reports
- Location: `test-output/index.html`
- Default TestNG HTML reports

### Logs
- Location: `logs/automation.log`
- Detailed execution logs with timestamps

---

## 🧪 Writing Tests

### Example Test Class

```java
package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {
    
    @Test(description = "Verify login with valid credentials", groups = "smoke")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("user@example.com", "password123");
        // Add assertions
    }
}
```

### Example Page Object

```java
package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    
    @FindBy(id = "username")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "login-btn")
    private WebElement loginButton;
    
    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
    
    public void login(String username, String password) {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
    }
}
```

---

## 🔧 Framework Components

### Utilities
- **ConfigReader** - Read configuration from properties files
- **ScreenshotUtil** - Capture screenshots on failure/pass
- **WaitUtil** - Explicit waits for elements
- **LogUtil** - Structured logging

### Listeners
- **TestListener** - Extent Report integration
- **RetryAnalyzer** - Retry failed tests automatically
- **RetryListener** - Apply retry logic to all tests

### Base Classes
- **BaseTest** - Common setup/teardown, driver management
- **BasePage** - Common page actions and waits

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit changes (`git commit -m 'Add some feature'`)
4. Push to branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## 📝 License

This project is licensed under the MIT License.

---

## 👥 Author

**Automation Team**

---

## 📧 Contact

For questions or support, please open an issue on GitHub.

---

## 🔍 Troubleshooting

### Common Issues

**Screenshots not captured:**
- Verify `capture_screenshot_on_pass=true` in config.properties
- Check `screenshots/` directory permissions

**WebDriver not found:**
- WebDriverManager handles drivers automatically
- Ensure internet connection for first run

**Tests not running:**
- Verify Java and Maven versions
- Run `mvn clean install` to download dependencies

**Reports not generated:**
- Check `extent.properties` configuration
- Verify `reports/` directory exists

---

## 📚 Documentation

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [TestNG Documentation](https://testng.org/doc/)
- [Extent Reports](https://www.extentreports.com/)
