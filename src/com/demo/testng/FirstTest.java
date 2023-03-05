package com.demo.testng;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class FirstTest extends BaseTest {
	
	@BeforeMethod
	@Parameters(value={"baseUrl", "userName", "password"})
	public void setup(String baseUrl, String userName, String password) {
		super.setup(baseUrl, userName, password);
	}


	@Test
	public void testSearchProduct() throws InterruptedException {
		
		loginStep();
		
		searchProduct();
		
		logoutStep();

	}
	
	private void searchProduct() {
		
		new WebDriverWait(driver, Duration.ofSeconds(10));

		String searchString = "Sandisk SSD";

		WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
		searchBox.sendKeys(searchString);

		driver.findElement(By.xpath("//*[@id='nav-search-submit-button']")).click();

		new WebDriverWait(driver, Duration.ofSeconds(20));

		String resultSelector = ".s-result-list > :is(.s-result-item):not(.s-result-item.AdHolder)[data-component-type='s-search-result']";

		WebElement matchedResult = driver.findElement(By.cssSelector(resultSelector));
		String firstItem = matchedResult.getAttribute("data-asin");
		WebElement matchedLink = matchedResult.findElement(By.cssSelector("a.a-link-normal.a-text-normal"));
		// Assert.assertTrue(matchedLink.findElement(By.cssSelector("span.a-text-normal")).getText().matches("(?i).*"
		// + searchString + ".*"));
		
		String productUrl = matchedLink.getAttribute("href");

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", matchedResult);

		WebDriverWait itemIntoViewWait = new WebDriverWait(driver, Duration.ofMillis(2000));
		itemIntoViewWait.until(ExpectedConditions.visibilityOf(matchedResult));

		//((JavascriptExecutor) driver).executeScript("arguments[0].click();", matchedLink);
		driver.navigate().to(productUrl);

		/*
		 * Set<String> handles = driver.getWindowHandles();
		 * 
		 * String recentTab = handles.stream().skip(handles.size() -
		 * 1).findFirst().get(); driver.switchTo().window(recentTab);
		 * 
		 * new WebDriverWait(driver, Duration.ofMillis(5000));
		 */

		WebElement spanAddToCart = driver.findElement(By.cssSelector("form#addToCart input#add-to-cart-button"));

		spanAddToCart.click();

		WebElement cartProductLink = driver
				.findElement(By.cssSelector("div#add-to-cart-confirmation-image a.sc-product-link"));

		cartProductLink.click();

		WebElement similarItemsTable = driver
				.findElement(By.cssSelector("div#HLCXComparisonWidget_feature_div table#HLCXComparisonTable"));

		Pair<Integer, String> itemWithMaxRating = similarItemsTable
				.findElements(By.cssSelector("tr#comparison_custormer_rating_row > td.comparison_sim_items_column"))
				.stream().filter(td -> !td.getCssValue("display").equals("none")).map(this::extractCssByRating)
				.max(Comparator.comparing(Pair::getKey)).orElseGet(() -> {
					WebElement firstCompareItem = similarItemsTable
							.findElements(By
									.cssSelector("tr#comparison_custormer_rating_row > td.comparison_sim_items_column"))
							.stream().filter(td -> !td.getCssValue("display").equals("none")).findFirst()
							.orElseThrow(NoSuchElementException::new);
					return extractCssByRating(firstCompareItem);
				});

		System.out.println("Item with max rating found: " + itemWithMaxRating);

		Integer itemIndex = Integer.valueOf(itemWithMaxRating.getValue().replaceAll("[^0-9]", ""));

		String addToCartItemSelector = new StringBuffer(
				"tr.comparison_add_to_cart_row > td.comparison_add_to_cart_button.")
				.append(itemWithMaxRating.getValue())
				.append(" span#comparison_add_to_cart_button" + itemIndex + " input.a-button-input").toString();

		WebElement compareItemAddToCart = similarItemsTable.findElement(By.cssSelector(addToCartItemSelector));

		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", compareItemAddToCart);

		WebDriverWait similarItemsIntoViewWait = new WebDriverWait(driver, Duration.ofMillis(2000));
		similarItemsIntoViewWait.until(ExpectedConditions.visibilityOf(compareItemAddToCart));

		((JavascriptExecutor) driver).executeScript("arguments[0].click();", compareItemAddToCart);


		WebElement goToCart = driver.findElement(By.cssSelector("span#sw-gtc a.a-button-text"));
		goToCart.click();

		driver.findElements(By.cssSelector("form#activeCartViewForm div.sc-list-body div.sc-list-item")).stream()
				.filter(item -> item.getAttribute("data-asin").equals(firstItem)).findFirst()
				.ifPresent(matchedItem -> {
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", matchedItem);

					WebDriverWait originalCartItemIntoViewWait = new WebDriverWait(driver, Duration.ofMillis(2000));
					originalCartItemIntoViewWait.until(ExpectedConditions.visibilityOf(matchedItem));
					
					matchedItem.findElement(By.cssSelector(
							"div.sc-item-content-group div.a-row.sc-action-links span.sc-action-delete input.a-color-link"))
							.click();
				});
	}

	private Pair<Integer, String> extractCssByRating(WebElement td) {
		String content = td.getText();
		String dynamicClass = td.getAttribute("class").split(" ")[1];
		Integer reviewCount = Integer.valueOf(content.substring(content.indexOf("(") + 1, content.indexOf(")")));
		return Pair.of(reviewCount, dynamicClass);
	}
	
}
