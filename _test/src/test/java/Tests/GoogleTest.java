package Tests;

import Pages.GooglePage;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class GoogleTest extends BaseTest {

    private final String GoogleURL = "http://www.google.com";
    private final String AutomationGameUrl = "https://www.automationgame.com/";
    private final String TextAutomation = "Automation";
    private final String TextAutomationSmall = "automation";
    private final String FindText = "testautomationday.com";

    @Test
    public void searchForAutomation() throws Exception {

        GooglePage googlePage = new GooglePage(getDriver());

        googlePage.openUrl(GoogleURL);
        googlePage.searchText(TextAutomationSmall).openFirstLinkAndCheckTitle(TextAutomation);
        assertEquals(getDriver().getCurrentUrl(), AutomationGameUrl);
    }

    @Test
    public void searchTextOnPages(){

        GooglePage googlePage = new GooglePage(getDriver());

        googlePage.openUrl(GoogleURL);
        googlePage.searchText(TextAutomationSmall);
        googlePage.SearchAutomationDayOnPages(FindText);
        assertTrue(googlePage.PageNumber != -1);
    }


}
