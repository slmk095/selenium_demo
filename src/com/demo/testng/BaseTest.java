package com.demo.testng;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;

@Listeners({ ITestListenerImpl.class })
public class BaseTest {
	
	private String userName;
	private String password;
	
	protected WebDriver driver;

	public void setup(String baseUrl, String userName, String password) {
		driver = new FirefoxDriver();
		driver.get(baseUrl);
		this.userName = userName;
		this.password = password;
	}
	
	protected void loginStep() {
		
		driver.manage().window().maximize();

		new WebDriverWait(driver, Duration.ofSeconds(2));

		WebElement mouseOver = driver.findElement(By.cssSelector("span#nav-link-accountList-nav-line-1"));
		new Actions(driver).moveToElement(mouseOver).build().perform();
		
		new WebDriverWait(driver, Duration.ofSeconds(1));

		WebElement signInButton = driver.findElement(By.cssSelector(".nav-action-button"));
		String loginUrl = signInButton.getAttribute("href");
		driver.navigate().to(loginUrl);
		System.out.println("signin clicked");
		
		new WebDriverWait(driver, Duration.ofSeconds(1));

		WebElement userNameElement = driver.findElement(By.id("ap_email"));
		userNameElement.isDisplayed();
		userNameElement.isEnabled();
		userNameElement.sendKeys(userName);

		WebElement continueNext = driver.findElement(By.id("continue"));
		continueNext.click();

		WebElement passwordElement = driver.findElement(By.id("ap_password"));
		passwordElement.isDisplayed();
		passwordElement.isEnabled();
		passwordElement.sendKeys(password);

		WebElement loginButton = driver.findElement(By.id("signInSubmit"));

		loginButton.isDisplayed();
		loginButton.isEnabled();
		loginButton.click();

		System.out.println("Logged into Amazon successfully");
	}
	
	protected void logoutStep() {
		WebElement signOutElement = driver.findElement(By.id("nav-item-signout"));
		signOutElement.click();
	}

}
