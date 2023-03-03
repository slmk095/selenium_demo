package demotestngproject;


import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


@Listeners({ ITestListenerImpl.class })
public class BaseTest {
	
	 String baseUrl = "https://www.amazon.in/";
	 
	@Test
	 public void Testmethod() throws InterruptedException {
	  WebDriverManager.firefoxdriver().setup();
      WebDriver driver = new FirefoxDriver();
      
      
      driver.get(baseUrl);
      
      Actions actions = new Actions(driver);
      
      Thread.sleep(100);
      
      WebElement mouseOver=driver.findElement(By.cssSelector("nav-link-accountList"));
      actions.moveToElement(mouseOver).perform();
      
      WebElement signInButton = driver.findElement(By.id("nav-flyout-ya-signin > .nav-action-button"));
      signInButton.click();
      System.out.println("signin clicked");
      Thread.sleep(100);
      
      
      
      WebElement username=driver.findElement(By.id("ap_email"));
      username.isDisplayed();
      username.isEnabled();
      username.sendKeys("username123@test.com");
      
      WebElement password=driver.findElement(By.id("ap_password"));
      password.isDisplayed();
      password.isEnabled();
      password.sendKeys("password123test");
      
      WebElement loginButton=driver.findElement(By.id("signInSubmit"));

      loginButton.isDisplayed();
      loginButton.isEnabled();
      loginButton.click();
      
      System.out.println("Logged into Amazon successfully");
	}

}
