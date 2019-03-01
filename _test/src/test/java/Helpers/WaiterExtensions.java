package Helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaiterExtensions {


    public static WebElement FindElementWithWaiter(WebDriver driver, By by, int timeoutInSeconds) {
        try {
            if (timeoutInSeconds > 0) {
                WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
                return wait.until(ExpectedConditions.visibilityOf(driver.findElement(by)));
            }
            return driver.findElement(by);
        } catch (Exception e) {
            return null;
        }
    }


}



