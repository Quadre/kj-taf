package stepdefs;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.TestTools;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static matchers.WebDriverContainsElementWithTextBy.containsElementWithPartialTextBy;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayContaining.hasItemInArray;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

/**
 * Created by JK on 13/04/2018.
 */
@Log4j2
public class AdminStepDefinitions extends AbstractStepDefinition {
    final String PLUGIN_FOLDER = "/plugins_zip/";

    private Map<String,String> pageToUrlMap = new HashMap<String,String>() {{
        put("Plugins->Add New", "wp-admin/plugin-install.php");
        put("Plugins->Installed Plugins", "wp-admin/plugins.php");
    }};

    @When("^User opens \"(.+?)\" page$")
    public void userOpensPreDefinedPage (String page)  {
        assertThat("Pre-defined url map should contain '"+ page +"' mapping.", pageToUrlMap, hasKey(page));
        NavigateTo(pageToUrlMap.get(page));
    }

    @When("^User installs \"(.+?)\" using Upload Plugin function$")
    public void test1 (String pluginName) throws URISyntaxException {
        String pluginPath = TestTools.getResourceAbsolutePath (PLUGIN_FOLDER + pluginName);
        assertThat(pluginPath, not(isEmptyString()));
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(pluginPath), null);
        log.debug("Get plugin absolute path & copy it to clipboard, done.");

        tools.getWebDriver().findElement(By.xpath("//a[@class='upload-view-toggle page-title-action']")).click();
        tools.getWebDriverWait().until(elementToBeClickable(By.id("pluginzip")));
        tools.getWebDriver().findElement(By.id("pluginzip")).click();
        log.debug("wp-admin open plugins-zip, done");
        log.debug("Native key strokes for CTRL, V and ENTER keys for current windows folder dialogbox");

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            log.debug("windows folder dialogbox interaction should be finished.");
        } catch (AWTException e) {
            log.error(e);
        }

        tools.getWebDriverWait().until(elementToBeClickable(By.id("install-plugin-submit"))).click();
        assertThat(tools.getWebDriver(), containsElementWithPartialTextBy(By.xpath("//*[@id='wpbody-content']/div[2]/h1"), pluginName ));
        TestRoot.addToExtraRep("Plugin: " + pluginName + ", loaded");
    }

    @Then("^User will observe following lines:$")
    public void userWillObserveFollowingLines(DataTable data)  {
        List<String> expValues = data.raw().stream().map(list->list.get(0)).collect(Collectors.toList());
        String actValues = tools.getWebDriver()
                .findElements(By.xpath("//div[@id='wpbody-content']/div[@class='wrap']/p"))
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.joining(";"));

        expValues.forEach(expVal->assertThat("Expecting that expected Value '"+expVal+"' will be in actual list: "+actValues, actValues.contains(expVal), is(true)));
    }


    @And("^User activates \"([^\"]*)\" plugin version (.+)$")
    public void userActivatesPluginVersion(String pluginShortName, String pluginVersion) {
        Map<String, WebElement> pluginsAndVersions = tools.getWebDriver()
                .findElements(By.xpath("//tbody/tr[td[1]//strong]"))
                .stream()
                .map(webElement -> {
                    String key =  webElement.findElement(By.xpath(".//td[1]//strong")).getText().trim() + ">" +
                            webElement.findElement(By.xpath(".//td[2]//div[2]")).getText().replaceFirst("\\| By.+", "").trim();
                    WebElement value = webElement;
                    return new AbstractMap.SimpleEntry<>(key, value);
                })
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        String pluginUnderTest = pluginShortName + ">Version " + pluginVersion;
        assertThat(pluginsAndVersions.keySet().toArray(), hasItemInArray(pluginUnderTest));

        pluginsAndVersions.get(pluginUnderTest).findElement(By.xpath(".//span[@class='activate']")).click();
        assertThat(tools.getWebDriver().getCurrentUrl(), containsString("wp-defender"));

        TestRoot.addToExtraRep(String.format("Plugin: {0} [{1}]  found and activated", pluginShortName, pluginVersion));
    }


}
