package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static Helpers.SeleniumFunction.GetTextOnElement;
import static Helpers.WaiterExtensions.FindElementWithWaiter;
import static Tests.BaseTest.log;

public class GooglePage {

    private By _googleSearch = By.name("q");
    private By _automationGameLink = By.cssSelector("a[href='https://www.automationgame.com/'] > .LC20lb");
    private By _logo = By.cssSelector("[alt='Automation']");
    private By _nextPage = By.xpath("//span[.='Следующая']");

    public int PageNumber = -1;

    private final WebDriver driver;

    public GooglePage(WebDriver driver) {
        this.driver = driver;
    }

    public GooglePage openUrl(String url) {
        log(driver, "open for: " + url);
        driver.get(url);
        return this;
    }

    public GooglePage searchText(String phraseToSearch) {
        log(driver, "search for: " + phraseToSearch);
        FindElementWithWaiter(driver, _googleSearch, 15).sendKeys(phraseToSearch);
        FindElementWithWaiter(driver, _googleSearch, 15).submit();
        return this;
    }

    public GooglePage openFirstLinkAndCheckTitle(String _text) throws Exception {
        log(driver, "openFirstLinkAndCheckTitle ");
        GetTextOnElement(_automationGameLink, _text, driver);
        FindElementWithWaiter(driver, _automationGameLink, 15).click();
        FindElementWithWaiter(driver, _logo, 15).isDisplayed();
        return this;
    }

    public GooglePage SearchAutomationDayOnPages(String _text) {
        int i;
        for (i = 0; i < 15; i++) {
            boolean searchElement = driver.getPageSource().contains(_text);

            if (searchElement == true) {
                PageNumber = i;
                log(driver, "page number: " + PageNumber);
                return this;
            } else {
                FindElementWithWaiter(driver, _nextPage, 10).click();
            }
        }
        return this;
    }

}
