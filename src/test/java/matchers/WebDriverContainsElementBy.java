package matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class WebDriverContainsElementBy extends TypeSafeDiagnosingMatcher<WebDriver> {

    private final By by;


    public WebDriverContainsElementBy(By by) {
        this.by = by;
    }

    @Override
    protected boolean matchesSafely(WebDriver webDrv, Description description) {
        int count = webDrv.findElements(by).size();
        if (count == 0)
            description.appendText("found 0 elements with given By");
        return count > 0;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("webDriver to contain 1 or more element " + by.toString());
    }

    public static WebDriverContainsElementBy containsElementBy(By by) {
        return new WebDriverContainsElementBy(by);
    }
}