package com.abtrading.zoho.project.testcases;

import java.io.IOException;
import java.util.Hashtable;

import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.abtrading.zoho.project.base.BaseTest;
import com.abtrading.zoho.project.util.DataUtil;
import com.abtrading.zoho.project.util.ExtentManager;
import com.abtrading.zoho.project.util.Xls_Reader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class DummyTestC extends BaseTest {
	ExtentReports rep = ExtentManager.getInstance();
	ExtentTest test;
	String testCaseName = "TestC";
	Xls_Reader xls;
	SoftAssert softAssert;
	WebDriver driver; 

	
	@Test(dataProvider="getData")
	public void testC(Hashtable<String, String>data) throws IOException {
		if(!DataUtil.isRunnable(testCaseName, xls)||data.get("Runmode").equals("N")){
			test.log(LogStatus.SKIP, "Skipping the test since Runmode is NO");
			throw new SkipException("Skipping the test since Runmode is NO");
			
		}
		test =	rep.startTest("DummyTestC");
		test.log(LogStatus.INFO, "Test C ");
		test.log(LogStatus.INFO, "Test c has failed");
		takeScreenShot(); 
		//ScreenShotUtiltity.captureScreenshot(driver);
		test.log(LogStatus.FAIL,"Screenshot-> "+ test.addScreenCapture(System.getProperty("user.dir")+"//screenshots//"));
		
	}
	@AfterMethod
	public void quit(){
		rep.endTest(test);
		rep.flush();
		
	}
	@DataProvider
	public Object[][] getData() throws IOException{
		//initialize prop before data is extracted call init() from parent class so super is used
		super.init();
		//prop object should be initialized before test begin i.e @BeforeMethod is needed
	 xls = new Xls_Reader(prop.getProperty("xlspath"));
		
		return DataUtil.getTestData(xls,testCaseName);
	}

}
