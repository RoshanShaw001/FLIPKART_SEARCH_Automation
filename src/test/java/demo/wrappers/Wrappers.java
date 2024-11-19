package demo.wrappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.NumberFormat;
import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    //ChromeDriver driver;
    public static Boolean searchProducts(WebDriver driver, String Product){
        Boolean success;
        try {
            By searchBoxLocator = By.xpath("//input[@title=\"Search for Products, Brands and More\"]");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxLocator));

            WebElement searchBarElement = driver.findElement(searchBoxLocator);
            searchBarElement.clear();
            searchBarElement.sendKeys(Product);
            searchBarElement.sendKeys(Keys.ENTER);
            // We can use the below action also for performing enter using the searchIcon. 
            // WebElement searchIcoElement = driver.findElement(By.xpath("//button[@type=\"submit\"]"));
            // searchIcoElement.click();
            success = true;
        } catch (Exception e) {
            System.out.println("Exception Occured! while searching product " + e.getMessage());
            success = false;
        }
        return success;
    }

    public static Boolean clickOnElement(ChromeDriver driver, WebElement element){
        Boolean success;
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(element));
            JavascriptExecutor js= (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click()", element);
            // The above sytax for clicking an element with the help JavaScript is used to ensure 
            // that the element will be clicked irrespective of it's position in the web page.
            // We can also use the normal way of clicking element using selenium webdriver.
            // element.click();
            success = true;
        } catch (Exception e) {
            System.out.println("Exception Occured! while clicking an element " + e.getMessage());
            success = false;
        }
        return success;
    }

    public static int getCountOfItems(List<WebElement> element){
        int count=0;
        
        try {
             count = element.size();
        } catch (Exception e) {
            System.out.println("Exception Occured! in getting the count " + e.getMessage());
        }    
        return count;
    }

    public static Boolean getTitleAndDiscountOfiPhone(ChromeDriver driver, List<WebElement> productRows, int discount){
        LinkedHashMap<String, String> iPhoneTitleAndDiscountMap = new LinkedHashMap<>();
        Boolean success;
        try {
            //div[@class="yKfJKb row"]//div[@class="UkUFwK"]/span -- Discount percent element
            //div[@class="yKfJKb row"]//div[@class="KzDlHZ"] -- Title Element
            

            for(WebElement productRow : productRows){
                String discountPercentText = productRow.findElement(By.xpath("//div[@class='UkUFwK']/span")).getText();
                int discountPercentValue = Integer.parseInt(discountPercentText.replaceAll("[^\\d]", ""));
                if(discountPercentValue>discount){
                    String titleText = productRow.findElement(By.xpath("//div[@class='KzDlHZ']")).getText();
                    iPhoneTitleAndDiscountMap.put(discountPercentText, titleText);
                }
            }
            for(Map.Entry<String,String> iPhoneTitleAndDisount : iPhoneTitleAndDiscountMap.entrySet()){
                System.out.println("The iPhone title is : " + iPhoneTitleAndDisount.getValue() + " And it's discount is : " + iPhoneTitleAndDisount.getKey());
            }
            success=true;
        } catch (Exception e) {
            System.out.println("Exception Occured! in getting the title and discount of iphone " + e.getMessage());
            success=false;
        }
        return success;
    }

    public static Boolean printTitleandImageURLOfCoffeeMug(WebDriver driver, By locator){
        Boolean success;

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            List<WebElement> userReviewElements = driver.findElements(locator);
            Set<Integer> userReviewSet = new HashSet<>();

            for(WebElement userReviewElement : userReviewElements){
                int userReview = Integer.parseInt((userReviewElement.getText()).replaceAll("[^\\d]",""));
                userReviewSet.add(userReview);
            }
            List<Integer> userReviewCountList = new ArrayList<>(userReviewSet);
            Collections.sort(userReviewCountList,Collections.reverseOrder());
            System.out.println(userReviewCountList);

            NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
            LinkedHashMap<String, String> productDetailsMap = new LinkedHashMap<>();

            // Product Title xpath -- //div[@class="slAVV4"]//span[text()="(7,201)"]/../..//a[@class="wjcEIp"]
            // Product image URL xpath -- //div[@class="slAVV4"]//span[text()="(7,201)"]/../..//img[@class="DByuf4"]
            for(int i=0; i<5; i++){
                String formattedUserReviewCount = "("+numberFormat.format(userReviewCountList.get(i))+")";
                String productTitle = driver.findElement(By.xpath("//div[@class='slAVV4']//span[text()='"
                +formattedUserReviewCount+"']/../..//a[@class='wjcEIp']")).getText();
                String productImageURL = driver.findElement(By.xpath("//div[@class='slAVV4']//span[text()='"
                +formattedUserReviewCount+"']/../..//img[@class='DByuf4']")).getText();

                String highestReviewCountAndTitle = String.valueOf(i+1) + " highest review count : "
                +formattedUserReviewCount+" Title : "+productTitle; 
                productDetailsMap.put(highestReviewCountAndTitle, productImageURL);
            }
            // print title and image url of coffee mug
            for(Map.Entry<String,String> productDetails : productDetailsMap.entrySet()){
                System.out.println("Product Review count and Product Title"+productDetails.getKey()+" and Product image URL : "+productDetails.getValue());
            }
            success=true;
        } catch (Exception e) {
                System.out.println("Exception occured : while getting the product review count, title and image url "+e.getMessage());
            success=false;
        }

        return success;
    }
}
