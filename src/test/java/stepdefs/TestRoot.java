package stepdefs;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import utils.TestTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JK on 09/04/2018.
 */
@Log4j2
public class TestRoot {
    private static List<String> extraRep;
    @Getter private static TestTools tools;

    static void addToExtraRep (String msg) {
        log.info(msg);
        extraRep.add(msg+"\r\n");
    }

    @Before
    public void initializeTest() {
        extraRep = new ArrayList<>();
        tools = new TestTools();
    }

    @After
    public void tearDownTest(Scenario scenario) {
        if (tools.getWebDriver() != null)
            tools.getWebDriver().quit();

        extraRep.forEach(scenario::write);
    }
}