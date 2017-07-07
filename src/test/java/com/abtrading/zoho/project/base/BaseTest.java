package com.abtrading.zoho.project.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.abtrading.zoho.project.util.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class BaseTest {

	public WebDriver driver;
	public Properties prop;
	//environment properties object
	public Properties envProp;
	public ExtentReports rep = ExtentManager.getInstance();
	public ExtentTest test;
	//to decide grid run or not	
	boolean gridrun = false;

	// properties file initialization
	/*2 properties files are initialized one prop properties and envProp properties*/
	public void init() {

		if (prop == null) {
			prop = new Properties();
			envProp = new Properties();
			FileInputStream fs = null;
			
			
		
				try {
					fs = new FileInputStream(System.getProperty("user.dir") + "//src//test//resources//projectconfig.properties");
					prop.load(fs);
					String env=prop.getProperty("env");
					fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//"+env+".properties");
					envProp.load(fs);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
		}

	}

	// UI interaction
	public void openBrowser(String bType) {

		test.log(LogStatus.INFO, "Opening browser " + bType);
		if(!gridrun){
		if (bType.equals("Mozilla")) {
			System.setProperty("webdriver.gecko.driver", prop.getProperty("geckodriver_exe"));
			driver = new FirefoxDriver();

		} else if (bType.equals("Chrome")) {
			System.setProperty("webdriver.chrome.driver", prop.getProperty("chromedriver_exe"));
			driver = new ChromeDriver();

		} else if (bType.equals("IE")) {
			System.setProperty("webdriver.ie.driver", prop.getProperty("iedriver_exe"));
			driver = new InternetExplorerDriver();
		}
		}else//for Grid run
		{
			DesiredCapabilities cap = null;
			if(bType.equals("Mozilla")){
							
				cap = DesiredCapabilities.firefox();
				cap.setBrowserName("firefox");
				cap.setJavascriptEnabled(true);
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);				
				
			}else if(bType.equals("Chrome")){
				
				cap = DesiredCapabilities.chrome();
				cap.setBrowserName("chrome");
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
								
			}
		
				try {
					driver =new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		// implicit wait and maximize
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		test.log(LogStatus.INFO, "Browser opened successfully  " + bType);

	}
	
	//url key is in env properties file
	public void navigate(String urlKey) {
		test.log(LogStatus.INFO, "Navigating to " + prop.getProperty(urlKey));
		driver.get(envProp.getProperty(urlKey));

	}

	public void click(String locatorKey) {
		test.log(LogStatus.INFO, "Clicking on " + locatorKey);
		getElement(locatorKey).click();
		test.log(LogStatus.INFO, "Clicked successfully on " + locatorKey);

	}
	
	public String getText(String locatorKey){
		test.log(LogStatus.INFO, "Getting the text from   "+locatorKey);
		return getElement(locatorKey).getText();
		
	}

	public void type(String locatorKey, String data) {
		test.log(LogStatus.INFO, "Typing in " + locatorKey + "Data  " + data);
		getElement(locatorKey).sendKeys(data);	
		test.log(LogStatus.INFO, "Clicked successfully in " + locatorKey);

	}

	// Reusable and centralized getElement function
	public WebElement getElement(String locatorKey) {
		WebElement e = null;
		try {
			if (locatorKey.endsWith("_id"))
				e = driver.findElement(By.id(prop.getProperty(locatorKey)));
			else if (locatorKey.endsWith("_xpath"))
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			else if (locatorKey.endsWith("_name"))
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));

			// if locator is not correct
			else {
				reportFail("Locator not correct" + locatorKey);
				Assert.fail("Locator not correct" + locatorKey);
			}

		} catch (Exception ex) {
			reportFail(ex.getMessage());
			ex.printStackTrace();
			Assert.fail("Failed the test  " + ex.getMessage());

		}
		return e;
	}

	// **********************Validation
	// functions********************************//

	public boolean verifyTitle() {
		return false;

	}

	public boolean verifyText() {
		return false;

	}

	public boolean isElementPresent(String locatorKey) {
		List<WebElement> elementList = null;
		if (locatorKey.endsWith("_id"))
			elementList = driver.findElements(By.id(prop.getProperty(locatorKey)));
		else if (locatorKey.endsWith("_name"))
			elementList = driver.findElements(By.name(prop.getProperty(locatorKey)));
		else if (locatorKey.endsWith("_xpath"))
			elementList = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		else {
			reportFail("Locator not correct - " + locatorKey);
			Assert.fail("Locator not correct - " + locatorKey);
		}

		if (elementList.size() == 0)
			return false;
		else
			return true;
	}

	public void waitForPageToLoad() {

		// wait for the page to load by using document.readyState
		// to use document.readyState we have to cast driver to
		// JavascriptExecutor
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String state = (String) js.executeScript("return document.readyState ");

		// wait for page to load completely
		while (!state.equals("complete")) {

			Wait(2);
			state = (String) js.executeScript("return document.readyState ");

		}

	}

	public void Wait(int timeToWaitInSeconds) {
		try {
			// since it is in milliseconds
			Thread.sleep(timeToWaitInSeconds * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void clickAndWait(String locator_tobeclicked, String locator_present) {
		test.log(LogStatus.INFO, "Waiting for locator " + locator_tobeclicked);

		int count = 5;
		for (int i = 0; i < count; i++) {
			getElement(locator_tobeclicked).click();
			Wait(2);
			if (isElementPresent(locator_present))
				break;

		}

	}

	public void acceptAlert() {

		try {

			WebDriverWait wait = new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			System.out.println(alert.getText());
			alert.accept();

		} catch (Exception e) {
			// exception handling
		}

	}

	/************************************
	 * Reporting Functions
	 ********************************************/

	public void reportPass(String msg) {
		test.log(LogStatus.PASS, msg);

	}

	public void reportFail(String msg) {
		test.log(LogStatus.FAIL, msg);
		takeScreenShot();
		Assert.fail(msg);

	}

	public void takeScreenShot() {
		// fileName of the screenshot
		Date d = new Date();
		String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
		// store screenshot in that file
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		// FileUtils.copyFile(scrFile, new
		// File(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "//screenshots//" + screenshotFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// put screenshot file in reports
		test.log(LogStatus.INFO, "Screenshot-> "
				+ test.addScreenCapture(System.getProperty("user.dir") + "//screenshots//" + screenshotFile));

	}

	/**********************************
	 * Application Functions
	 *********************************/

	public boolean doLogin(String username, String password) {

		test.log(LogStatus.INFO, "Trying to login " + username + "   " + password);

		click("loginLink_xpath");
		Wait(2);
		waitForPageToLoad();
		// switch to the frame 1
		// to find how many frames in the page by tag name iframe
		driver.switchTo().frame(0);

		type("emailField_xpath", username);
		type("passwordField_xpath", password);
		click("signInButton_xpath");
		driver.switchTo().defaultContent();

		Wait(3);
		if (isElementPresent("crmLink_xpath")) {
			test.log(LogStatus.INFO, "Login  Successful");
			return true;
		}

		else {
			test.log(LogStatus.INFO, "Login  Unsuccessful");
			return false;
		}

	}

	public int getLeadsRowNum(String leadslastname) {
		test.log(LogStatus.INFO, "Finding list last name  " + leadslastname);
		List<WebElement> leadsLastNames = driver.findElements(By.xpath(prop.getProperty("leadsLastNameCol_xpath")));
		for (int i = 0; i < leadsLastNames.size(); i++) {
			System.out.println(leadsLastNames.get(i).getText());
			if (leadsLastNames.get(i).getText().trim().equals(leadslastname)) {
				// row number on which Last name is found
				// i+1 cause it is starting from 0
				test.log(LogStatus.INFO, "Lead found in row  " + (i + 1));
				// return true;
				return (i + 1);

			}

		}
		test.log(LogStatus.INFO, "Lead not found in row ");
		// return false;
		return -1;
	}

	public int getAccountRowNum(String leadscompany) {
		test.log(LogStatus.INFO, "Finding list last name  " + leadscompany);
		List<WebElement> leadsCompanyNames = driver.findElements(By.xpath(prop.getProperty("accountNameCol_xpath")));
		for (int i = 0; i < leadsCompanyNames.size(); i++) {
			System.out.println(leadsCompanyNames.get(i).getText());
			if (leadsCompanyNames.get(i).getText().trim().equals(leadscompany)) {
				// row number on which Last name is found
				// i+1 cause it is starting from 0
				test.log(LogStatus.INFO, "Lead found in row  " + (i + 1));
				// return true;
				return (i + 1);

			}

		}
		test.log(LogStatus.INFO, "Lead not found in row ");
		// return false;
		return -1;
	}

	public void clickOnLead(String leadslastname) {
		test.log(LogStatus.INFO, "Clicking on the lead" + leadslastname);
		// get the row where the lead is lying
		int rnum = getLeadsRowNum(leadslastname);
		// find the lead using that row and click
		driver.findElement(By.xpath(prop.getProperty("leadsPart1_xpath") + rnum + prop.getProperty("leadsPart2_xpath")))
				.click();
		test.log(LogStatus.INFO, "Clicked on the lead" + leadslastname);
	}
	
	public void selectDropList(String locatorKey, String data){
	WebElement list =	getElement(locatorKey);
		Select s = new Select(list);
		s.selectByVisibleText(data);
		
			}
	
	
	public void  selectDate(String d){
		click("closingdate_xpath");
		
		//convert string date (input date into date object)
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    try {
			Date inputdate = 	sdf.parse(d);
			Date currentdate = new Date();
			sdf = new SimpleDateFormat("d");
			String day = sdf.format(inputdate);
			System.out.println(day);
			sdf = new SimpleDateFormat("MMMM");
			String month = sdf.format(inputdate);
			sdf = new SimpleDateFormat("yyyy");
			String year = sdf.format(inputdate);
			System.out.println(month+" "+year);
			String monthyearTobeSelected = (month+" "+year);
			
			while(true){
				//current date is greater than our date returns 1
				//if less than than -1
				//if equal then  0
				if(currentdate.compareTo(inputdate)==1){
					//click on back button
					click("backButton_xpath");
					
				}else if(currentdate.compareTo(inputdate)==-1){
					//click on front button
					click("forwardButton_xpath");
				}else if(currentdate.compareTo(inputdate)==0){
					break;
					
				}
				//when equal break from the loop
				if(monthyearTobeSelected.equals(getText("monthyearDisplayed_xpath")))
					//this Wait works like charm when selecting same month as of current month
					Wait(2);
				{
				break;					
				}
				
			
			}
			//when the month year matches click on the date
           WebElement d1 =	driver.findElement(By.xpath("//td[text() = '"+day+"']"));					
		  d1.click();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	}
}
