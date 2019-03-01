package Helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static Helpers.WaiterExtensions.FindElementWithWaiter;
import static Tests.BaseTest.log;


public class SeleniumFunction {

    public static void GetTextOnElement(By locator, String _name, WebDriver driver) throws Exception {

        String result = FindElementWithWaiter(driver, locator, 10).getText();
        Thread.sleep(1000);

        if (result.equals(_name)) {
            log(driver, result + " " + "Verify");
        } else {

            throw new NullPointerException(result + " " + "Not Verify");
        }

    }


}
