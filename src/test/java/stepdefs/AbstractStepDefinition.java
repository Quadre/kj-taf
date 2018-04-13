package stepdefs;

import lombok.extern.log4j.Log4j2;
import utils.TestTools;

/**
 * Created by JK on 13/04/2018.
 */
@Log4j2
class AbstractStepDefinition {
    TestTools tools = TestRoot.getTools();

    void NavigateTo(String urlSuffix) {
        tools.getWebDriver().navigate().to(tools.getConfig().getString("baseUrl") + urlSuffix);
    }
}
