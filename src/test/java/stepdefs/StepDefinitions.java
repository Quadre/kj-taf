package stepdefs;

import cucumber.api.java.en.*;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Created by JK on 09/04/2018.
 */
public class StepDefinitions {
    @Given("^Jeff has bought a microwave for$")
    public void test1 ()  {
        assertThat (ServiceHooks.getDriver(), not(nullValue()));
    }
}