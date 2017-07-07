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

public class LoginTest extends BaseTest {
	
	String testCaseName="LoginTest";
	SoftAssert softAssert;
	Xls_Reader xls;
	
	@Test(dataProvider = "getData")
	public void doLogin(Hashtable<String, String>data){
		
		//for log and reports
		test = rep.startTest("LoginTest");
		test.log(LogStatus.INFO, "Starting the LoginTest");
		test.log(LogStatus.INFO,data.toString());
		
		//to check is test need to be skip
		if(!DataUtil.isRunnable(testCaseName, xls) ||  data.get("Runmode").equals("N")){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		
		//open Browser
		openBrowser(data.get("Browser"));
		
		
		//Navigate to URL
		navigate("appurl");
		
				
		
		//Login		
        boolean actualResult=doLogin(data.get("Username"), data.get("Password"));
		
		boolean expectedResult=false;
		if(data.get("ExpectedResult").equals("Y"))
			expectedResult=true;
		else
			expectedResult=false;
		
		if(expectedResult!=actualResult)
		reportFail("Login Test Failed.");
	
		reportPass("Login Test Passed");
		
		
		
	}
	
	
	@BeforeMethod
	public void init(){
		softAssert = new SoftAssert();
	}
	
	
	@AfterMethod
	public void quit(){
		try{
			softAssert.assertAll();
		}catch(Error e){
			test.log(LogStatus.FAIL, e.getMessage());
		}
		
		rep.endTest(test);
		rep.flush();
		if(driver!=null)
			driver.quit();
	}
	
	@DataProvider(parallel = true)
	public Object[][] getData() throws IOException{
		super.init();
		xls = new Xls_Reader(prop.getProperty("xlspath"));
		return DataUtil.getTestData(xls, testCaseName);
		
	}
	
	
}
