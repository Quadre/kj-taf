package matchers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;


@RequiredArgsConstructor()
public class WebDriverContainsElementWithTextBy extends TypeSafeDiagnosingMatcher<WebDriver> {
    public enum MatchType {exactTextMatchOneElement, exactTextMatchAnyElement, partialTextMatchOneElement, partialTextMatchAnyElement};

    @NonNull private By by;
    @NonNull private String expText;
    @NonNull private MatchType matchType;

    @Override
    protected boolean matchesSafely(WebDriver webDrv, Description description) {
        List<WebElement> lst = webDrv.findElements(by);
        int count = lst.size();
        if (count == 0)
            description.appendText("found 0 elements with given By");
        else if (count == 1 && matchType == MatchType.exactTextMatchOneElement
                && lst.get(0).getText().contentEquals(expText) )
            return  true;
        else if (count == 1 && matchType == MatchType.partialTextMatchOneElement
                && lst.get(0).getText().contains(expText) )
            return  true;
        else if (count > 1 && matchType == MatchType.partialTextMatchAnyElement
                && lst.stream().anyMatch(webElement -> webElement.getText().contains(expText)))
            return  true;
        else if (count > 1 && matchType == MatchType.exactTextMatchAnyElement
                && lst.stream().anyMatch(webElement -> webElement.getText().equals(expText)))
            return  true;
        else
            description.appendText("Found text: " + lst.get(0).getText());
        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("webDriver to contain 1 or more element " + by.toString() + ", with text " + expText);
    }

    public static WebDriverContainsElementWithTextBy containsElementByCustom(By by, String expText, MatchType customMatch) {
        return new WebDriverContainsElementWithTextBy(by, expText, customMatch);
    }

    public static WebDriverContainsElementWithTextBy containsElementWithExactTextBy(By by, String expText) {
        return new WebDriverContainsElementWithTextBy(by, expText, MatchType.exactTextMatchOneElement);
    }

    public static WebDriverContainsElementWithTextBy containsElementWithPartialTextBy(By by, String expText) {
        return new WebDriverContainsElementWithTextBy(by, expText, MatchType.partialTextMatchOneElement);
    }

    public static WebDriverContainsElementWithTextBy containsAnyElementWithExactTextBy(By by, String expText) {
        return new WebDriverContainsElementWithTextBy(by, expText, MatchType.exactTextMatchAnyElement);
    }

    public static WebDriverContainsElementWithTextBy containsAnyElementWithPartialTextBy(By by, String expText) {
        return new WebDriverContainsElementWithTextBy(by, expText, MatchType.partialTextMatchAnyElement);
    }
}