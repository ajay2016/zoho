package com.abtrading.zoho.project.testcases;

import java.io.IOException;
import java.util.Hashtable;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.abtrading.zoho.project.base.BaseTest;
import com.abtrading.zoho.project.util.DataUtil;
import com.abtrading.zoho.project.util.Xls_Reader;
import com.relevantcodes.extentreports.LogStatus;

public class LeadTest extends BaseTest {

	String testCaseName = "CreateLeadTest";
	SoftAssert softAssert;
	Xls_Reader xls;

	@Test(priority = 1, dataProvider = "getData")
	public void createLeadTest(Hashtable<String, String> data) {
		test = rep.startTest("Starting the createLeadTest");

		if (!DataUtil.isRunnable(testCaseName, xls) || data.get("Runmode").equals("N")) {
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}

		// init();
		openBrowser(data.get("Browser"));
		navigate("appurl");
		//use env properties file for data
		doLogin(envProp.getProperty("username"), envProp.getProperty("password"));
		waitForPageToLoad();
		click("crmLink_xpath");
		// click("leadsTab_xpath");
		clickAndWait("leadsTab_xpath", "newLeadButton_xpath");
		click("newLeadButton_xpath");
		type("leadsCompany_xpath", data.get("LeadCompany"));
		type("leadsLastName_xpath", data.get("LeadLastName"));
		click("leadsSaveButton_xpath");
		clickAndWait("leadsTab_xpath", "newLeadButton_xpath");
		// click("leadsTab_xpath");

		// Validate
		int rnum = getLeadsRowNum(data.get("LeadLastName"));
		if (rnum == -1)
			reportFail("Lead not found   " + data.get("LeadLastName"));
		reportPass("Lead found   " + data.get("LeadLastName"));

	}

	@Test(priority = 2, dataProvider = "getData")
	public void convertLeadTest(Hashtable<String, String> data) {
		test = rep.startTest("Starting convert Lead test");

		if (!DataUtil.isRunnable("ConvertLeadTest", xls) || data.get("Runmode").equals("N")) {
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}

		openBrowser(data.get("Browser"));
		navigate("appurl");
		doLogin(prop.getProperty("username"), prop.getProperty("password"));
		waitForPageToLoad();
		click("crmLink_xpath");
		// click("leadsTab_xpath");
		clickAndWait("leadsTab_xpath", "newLeadButton_xpath");
		clickOnLead(data.get("LeadLastName"));
		click("leadsConvertButton_xpath");
		click("convertSaveButton_xpath");
		clickAndWait("accountTab_xpath", "newLeadButton_xpath");

		// Validate
		int rnum = getAccountRowNum(data.get("LeadCompany"));
		if (rnum == -1)
			reportFail("Lead Account not found   " + data.get("LeadCompany"));
		reportPass("Lead  Account found   " + data.get("LeadCompany"));

	}

	@Test(priority = 3, dataProvider = "getDataDeleteLead")
	public void deleteLeadAccountTest(Hashtable<String, String> data) {
		test.log(LogStatus.INFO, "Deleting Lead Account");

		if (!DataUtil.isRunnable("DeleteLeadAccountTest", xls) || data.get("Runmode").equals("N")) {
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		openBrowser(data.get("Browser"));
		navigate("appurl");
		doLogin(prop.getProperty("username"), prop.getProperty("password"));
		waitForPageToLoad();
		click("crmLink_xpath");
		// click("leadsTab_xpath");
		clickAndWait("leadsTab_xpath", "newLeadButton_xpath");
		clickOnLead(data.get("LeadLastName"));
		click("customizeToolsTab_xpath");
		Wait(2);
		//driver.findElement(By.xpath("//*[@id='customizedd']/ul/li[2]/a")).click();
//Actions act = new Actions(driver);
		//act.moveToElement(driver.findElement(By.xpath("//*[@id='customizedd']/ul/li[2]/a"))).perform();
		click("deleteTab_xpath");
		Wait(2);
		
		acceptAlert();
		Wait(3);
		click("backArrow_xpath");
		// Validate
		int rnum = getLeadsRowNum(data.get("LeadLastName"));
		// Check lead is in the list
		if (rnum != -1)
			reportFail("Could not delete the lead");
		reportPass(data.get("LeadLastName") + "  Lead is deleted");
		takeScreenShot();

	}

	
	/*************************************DataProvider***********************************/
	//invoke data parallel
	@DataProvider(parallel = true)
	public Object[][] getData() throws IOException {
		super.init();
		xls = new Xls_Reader(prop.getProperty("xlspath"));
		return DataUtil.getTestData(xls, testCaseName);

	}

	@DataProvider (parallel = true)
	public Object[][] getDataDeleteLead() throws IOException {
		super.init();
		xls = new Xls_Reader(prop.getProperty("xlspath"));
		return DataUtil.getTestData(xls, "DeleteLeadAccountTest");

	}
	
	/**********************TestNG*******************************************/
	@BeforeMethod
	public void init() {
		softAssert = new SoftAssert();

	}

	@AfterMethod
	public void quit() {
		try {
			softAssert.assertAll();
		} catch (Error e) {
			test.log(LogStatus.FAIL, e.getMessage());
		}
		if (rep != null) {
			rep.endTest(test);
			rep.flush();
		}
		 if (driver != null)
		 driver.quit();

	}

}
