package stepdefs;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by JK on 09/04/2018.
 */
public class ServiceHooks {

    private static WebDriver driver;
    public static WebDriver getDriver(){
        return driver;
    }

    @Before
    public void initializeTest(){
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");
        driver = new ChromeDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (driver != null)
            driver.quit();
    }
}