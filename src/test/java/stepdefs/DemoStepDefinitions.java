package stepdefs;


import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.log4j.Log4j2;
import matchers.WebDriverContainsElementWithTextBy;
import org.openqa.selenium.By;

import static matchers.WebDriverContainsElementBy.containsElementBy;
import static matchers.WebDriverContainsElementWithTextBy.containsElementWithExactTextBy;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Created by JK on 09/04/2018.
 */
@Log4j2
public class DemoStepDefinitions extends AbstractStepDefinition {

    @Given("^This is a dummy test, to ensure all cucumber features are okay$")
    public void test1 ()  {
        assertThat (tools.getWebDriver(), not(nullValue()));
    }

    @Given("^User getting wp-admin using (.+?) configuration$")
    public void userGettingUsingParticularWPConfiguration(String envLable)  {
        tools.setConfig(tools.getConfig().getConfig(envLable));
        TestRoot.addToExtraRep("Using env: " + envLable);
        assertThat("Expected " + envLable + " is enlisted in config file, actually not", tools.getConfig(), not(nullValue()));
        assertThat("Expected WebDriver is initialized", tools.getWebDriver(), not(nullValue()));

        tools.getWebDriver().navigate().to(tools.getConfig().getString("baseUrl")+"wp-login.php");
        assertThat(tools.getWebDriver(), containsElementBy(By.id("loginform")));
    }

    @When("^User is logged as (.+)$")
    public void userIsLoggedAsIncsubqa(String login) throws Throwable {
        tools.getWebDriver().findElement(By.id("user_login")).sendKeys(login);
        tools.getWebDriver().findElement(By.id("user_pass")).sendKeys(tools.getConfig().getString(login));
        tools.getWebDriver().findElement(By.id("wp-submit")).click();

        assertThat(tools.getWebDriver(), containsElementBy(By.id("wp-admin-bar-logout")));
        TestRoot.addToExtraRep("Logged in as : " + login);
    }

    @Then("^User should see '(.+?)' in top left corner$")
    public void userShouldSeePHPSingleInTopLeftCorner(String msg) throws Throwable {
        assertThat(tools.getWebDriver(), containsElementBy(By.xpath("//*[@id='wp-admin-bar-site-name']/a[@class='ab-item']")));
        assertThat(tools.getWebDriver(), WebDriverContainsElementWithTextBy.containsElementWithExactTextBy(By.xpath("//*[@id='wp-admin-bar-site-name']/a[@class='ab-item']"), msg));
    }

}