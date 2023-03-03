package demotestngproject;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class FirstTestNGclass {
	
	public String baseUrl = "https://github.com/";
   
	
  @Test
  public void verifyPageTitle() {
	 
      WebDriverManager.firefoxdriver().setup();
      WebDriver driver= new FirefoxDriver(); 
	  
      driver.get(baseUrl);
     String expectedTitle = "GitHub: Let’s build from here · GitHub";
      String actualTitle = driver.getTitle();
      Assert.assertEquals(actualTitle, expectedTitle);
      
      System.out.println("the actual title is "+actualTitle);
      
      driver.close();
	  
  }
}
