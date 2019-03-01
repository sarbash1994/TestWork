package Tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.model.RunnerScheduler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(BaseTest.Parallelized.class)
public class BaseTest {

    public void setPropertyWindow() {
        currentDriver.get().manage().window().maximize();
    }

    public void setPropertyTimeOut(int seconds) {
        currentDriver.get().manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    enum WebDriverType {
        CHROME,
        FIREFOX
    }

    static class WebDriverFactory {
        static WebDriver create(BaseTest.WebDriverType type) {
            WebDriver driver;
            switch (type) {
                case FIREFOX:
                    driver = new FirefoxDriver();
                    break;
                case CHROME:
                    driver = new ChromeDriver();
                    break;
                default:
                    throw new IllegalStateException();
            }
            log(driver, "created");
            return driver;
        }
    }

    @Parameterized.Parameter
    public BaseTest.WebDriverType currentDriverType;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> driverTypes() {
        return Arrays.asList(new Object[][]{
                {BaseTest.WebDriverType.CHROME},
                {BaseTest.WebDriverType.FIREFOX}
        });
    }

    public static ThreadLocal<WebDriver> currentDriver = new ThreadLocal<>();
    private static List<WebDriver> driversToCleanup = Collections.synchronizedList(new ArrayList<>());

    @BeforeClass
    public static void initChromeVariables() {
        WebDriverManager.chromedriver().setup();
        WebDriverManager.firefoxdriver().setup();
    }

    @Before
    public void driverInit() {
        if (currentDriver.get() == null) {
            WebDriver driver = BaseTest.WebDriverFactory.create(currentDriverType);
            driversToCleanup.add(driver);
            currentDriver.set(driver);
        }
        setPropertyWindow();
        setPropertyTimeOut(30);

    }

    public WebDriver getDriver() {
        return BaseTest.currentDriver.get();
    }

    @AfterClass
    public static void driverCleanup() {
        Iterator<WebDriver> iterator = driversToCleanup.iterator();
        while (iterator.hasNext()) {
            WebDriver driver = iterator.next();
            log(driver, "about to quit");
            driver.quit();
            iterator.remove();
        }
    }

    public static void log(WebDriver driver, String message) {
        String driverShortName = StringUtils.substringAfterLast(driver.getClass().getName(), ".");
        System.out.println(String.format("%15s, %15s: %s", Thread.currentThread().getName(), driverShortName, message));
    }

    public static class Parallelized extends Parameterized {

        private static class ThreadPoolScheduler implements RunnerScheduler {
            private ExecutorService executor;

            public ThreadPoolScheduler() {
                String threads = System.getProperty("junit.parallel.threads", "16");
                int numThreads = Integer.parseInt(threads);
                executor = Executors.newFixedThreadPool(numThreads);
            }

            @Override
            public void finished() {
                executor.shutdown();
                try {
                    executor.awaitTermination(10, TimeUnit.MINUTES);
                } catch (InterruptedException exc) {
                    throw new RuntimeException(exc);
                }
            }

            @Override
            public void schedule(Runnable childStatement) {
                executor.submit(childStatement);
            }
        }

        public Parallelized(Class klass) throws Throwable {
            super(klass);
            setScheduler(new BaseTest.Parallelized.ThreadPoolScheduler());
        }
    }


}
