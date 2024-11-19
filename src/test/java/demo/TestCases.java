package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;


public class TestCases {
    ChromeDriver driver;

    /*
     * TODO: Write your tests here with testng @Test annotation. 
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */

     @Test
     public void testCase01() throws InterruptedException{
        // testCase01: Go to www.flipkart.com. Search "Washing Machine". 
        // Sort by popularity and print the count of items with rating less than or equal to 4 stars.
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        System.out.println("TestCase01 started");
        driver.get("https://www.flipkart.com/");
        Wrappers.searchProducts(driver,"Washing Machine");
        
        WebElement popularityTabElement = driver.findElement(By.xpath("//div[text()=\"Popularity\"]"));
        Wrappers.clickOnElement(driver, popularityTabElement);

        //Both the below xpaths can be used
        //parent::div[@class="DOjaWF gdgoEp"]//div[@class="XQDdHH" and text()>="4.0"]
        //div[contains(@class,'gdgoEp')]//div[@class="XQDdHH" and text()<="4.0"]
        List<WebElement>  washingMachineRatingElement = driver.findElements(By.xpath("//div[contains(@class,'gdgoEp')]//div[@class=\"XQDdHH\" and text()<=\"4.0\"]"));
        int items = Wrappers.getCountOfItems(washingMachineRatingElement);
        Assert.assertNotNull(items);
        System.err.println("The count of items with rating less than or equal to 4 stars is : " + items);
        System.out.println("TestCase01 ended");

     }

     @Test
     public void testCase02() throws InterruptedException{
        // testCase02: Search "iPhone", print the Titles and discount % of items with more than 17% discount
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        System.out.println("TestCase02 started");
        driver.get("https://www.flipkart.com/");
        Wrappers.searchProducts(driver,"iPhone");
        List<WebElement> productsRows=driver.findElements(By.xpath("//div[@class='yKfJKb row']"));
        Boolean success = Wrappers.getTitleAndDiscountOfiPhone(driver, productsRows, 17);

        Assert.assertTrue(success);
        System.out.println("TestCase02 ended");

     }

     @Test
     public void testCase03() throws InterruptedException{
        // testCase03: Search "Coffee Mug", select 4 stars and above, 
        // and print the Title and image URL of the 5 items with highest number of reviews
        // 4* checkbox - (//div[@class="XqNaEv"])[1]
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        System.out.println("TestCase03 started");
        driver.get("https://www.flipkart.com/");
        Wrappers.searchProducts(driver,"Coffee Mug");
        WebElement fourStarRatingElement = driver.findElement(By.xpath("(//div[@class=\"XqNaEv\"])[1]"));
        Boolean success =Wrappers.clickOnElement(driver, fourStarRatingElement);
        Thread.sleep(3000);
        Assert.assertTrue(success);
        Boolean successPrint = Wrappers.printTitleandImageURLOfCoffeeMug(driver, By.xpath("//div[@class=\"slAVV4\"]//span[@class=\"Wphh3N\"]"));
        Assert.assertTrue(successPrint);
        System.out.println("TestCase03 ended");

    }
     
    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */
    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest()
    {
        driver.close();
        driver.quit();

    }
}