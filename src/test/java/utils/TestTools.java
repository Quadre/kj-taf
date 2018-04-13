package utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import stepdefs.AdminStepDefinitions;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * Created by JK on 13/04/2018.
 */
@Log4j2
public class TestTools {
    @Getter @Setter Config config;
    @Getter WebDriver webDriver;
    @Getter WebDriverWait webDriverWait;
    @Getter JavascriptExecutor javaScript;

    public TestTools(){
        config = ConfigFactory.load("config/application.conf");
        System.setProperty("webdriver.chrome.driver", "./src/test/resources/drivers/chromedriver.exe");
        webDriver = new ChromeDriver();
        webDriverWait = new WebDriverWait(webDriver, 10);
        javaScript = (JavascriptExecutor) webDriver ;
    }


    public static String getResourceAbsolutePath (String resourceRelativePath){
        URL resource = AdminStepDefinitions.class.getResource(resourceRelativePath);
        try {
            return Paths.get(resource.toURI()).toString();
        } catch (URISyntaxException e) {
            log.error(e);
            return "";
        }
    }
}
