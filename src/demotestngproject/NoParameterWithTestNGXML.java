package demotestngproject;

import org.testng.annotations.Test;
import org.testng.Assert;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.firefox.FirefoxDriver;

public class NoParameterWithTestNGXML {
    
    @Test
    public void withoutParameter() {
    	
    	  WebDriverManager.firefoxdriver().setup();
          WebDriver driver = new FirefoxDriver();
        String order = "First";
        String keyword = "Automation Testing";
        
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
        driver.get("https://www.amazon.com/");
        
        WebElement searchbox = driver.findElement(By.name("q"));
        //Enter keyword into search box 
        searchbox.sendKeys(keyword);
        
        System.out.println("The "+ order+" search key is = "+keyword);
        System.out.println("Value in Google Search Box = "+searchbox.getAttribute("value") +" ::: Value given by input = "+keyword);
        //verifying the keyword in google search box
        Assert.assertTrue(searchbox.getAttribute("value").equalsIgnoreCase(keyword));
        
        driver.quit();
}
}