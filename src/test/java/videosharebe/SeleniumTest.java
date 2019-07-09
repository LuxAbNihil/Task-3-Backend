package videosharebe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SeleniumTest {
	
	private Properties properties;
	private final String propertyFilePath = "src/test/resources/selenium.properties";
	
	
	
	private WebDriver driver;
	private WebDriverWait wait;
	
	@BeforeClass
	public void initializer() throws IOException, FileNotFoundException {
		
		try(BufferedReader reader = new BufferedReader(new FileReader(propertyFilePath))) {
			properties = new Properties();
			properties.load(reader);
		}
		
		String driverLocation = properties.getProperty("driverLocation");
		System.setProperty("webdriver.chrome.driver", driverLocation);
		
		driver = new ChromeDriver();
	    wait = new WebDriverWait(driver, 10);	
	}
	
	@AfterClass
	public void cleanUp() {
		driver.close();
	}
	
	@Test(priority=0)
	public void userRegistersAsNewUser() {
		driver.get("http://localhost:8081/videosharingsite/");
		driver.findElement(By.id("user")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
		driver.findElement(By.id("username")).sendKeys("Username");
		driver.findElement(By.id("password")).sendKeys("Username");
		driver.findElement(By.id("address")).sendKeys("13 Testing Drive");
		driver.findElement(By.id("phoneNumber")).sendKeys("7778889999");
		driver.findElement(By.id("age")).sendKeys("57");
		driver.findElement(By.id("submit")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")));
		String pageTitle = driver.getTitle();
		Assert.assertEquals("Spring MVC Form Handling", pageTitle);
	}
	
	@Test(priority = 1)
	public void login() {
		wait.until(ExpectedConditions.elementToBeClickable(By.id("login")));
		driver.findElement(By.id("login")).click();
		driver.findElement(By.id("username")).sendKeys("Username");
		driver.findElement(By.id("password")).sendKeys("Username");
		driver.findElement(By.id("login-submit")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("EditUser-0")));
		String username = driver.findElement(By.xpath("//*[@id=\"UserUsername-0\"]")).getText();
		Assert.assertEquals(username, "Username");
	}
	
	@Test(priority = 2)
	public void newUserShowsUpInListOfUsers() {
		login();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("UserRow")));
		String username = driver.findElement(By.xpath("//*[@id=\"UserUsername-0\"]")).getText();
		Assert.assertEquals(username, "Username");
	}
	
	@Test(priority=3)
	public void userUpdatesUserInfoSuccessfully() {

		wait.until(ExpectedConditions.elementToBeClickable(By.id("EditUser-0")));
		WebElement editUserButton = driver.findElement(By.id("EditUser-0"));
		editUserButton.click();
		editUserButton.click(); //necessary because it takes two clicks to display update fields
		wait.until(ExpectedConditions.elementToBeClickable(By.id("edit-submit")));
		WebElement editPassword = driver.findElement(By.id("editPassword"));
		WebElement editEmail = driver.findElement(By.id("editEmail"));
		WebElement editAddress = driver.findElement(By.id("editAddress"));
		WebElement editPhoneNumber = driver.findElement(By.id("editPhoneNumber"));
		WebElement editAge = driver.findElement(By.id("editAge"));
		enterDataIntoForm(editPassword, "password");
		enterDataIntoForm(editEmail, "user@email.com");
		enterDataIntoForm(editAddress, "123 User Lane");
		enterDataIntoForm(editPhoneNumber, "0001110000");
		enterDataIntoForm(editAge, "34");
		driver.findElement(By.id("edit-submit")).click();
		wait.until(ExpectedConditions.textToBe(By.id("UserAddress-0"), "123 User Lane"));
		String phoneNumber = driver.findElement(By.xpath("//*[@id=\"UserPhoneNumber-0\"]")).getText();
		Assert.assertEquals(phoneNumber, "0001110000");
	}
	
	@Test(priority=4)
	public void userIsDeleted() {
		wait.until(ExpectedConditions.elementToBeClickable(By.id("DeleteUser-0")));
		driver.findElement(By.id("DeleteUser-0")).click();
		wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.id("UserAddress-0"), "123 User Lane")));
		String username = driver.findElement(By.id("UserUsername-0")).getText();
		System.out.println(username);
		Assert.assertNotEquals(username, "Username");
	}
	
	public void enterDataIntoForm(WebElement element, String dataToBeEntered) {
		element.clear(); //Make this pass a string and extract web element in helper function
		element.sendKeys(dataToBeEntered);
	}
}
