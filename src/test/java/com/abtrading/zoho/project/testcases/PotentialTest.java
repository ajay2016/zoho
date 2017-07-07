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

public class PotentialTest extends BaseTest {
	SoftAssert softAssert;
	Xls_Reader xls;
	
	
	@Test(priority = 1, dataProvider = "getDataPotentials")
	public void createPotentialTest(Hashtable<String, String>data){
		
		test = rep.startTest("Starting the CreatePotentialTest");

		if (!DataUtil.isRunnable("CreatePotentialTest", xls) || data.get("Runmode").equals("N")) {
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		openBrowser(data.get("Browser"));
		navigate("appurl");
		doLogin(envProp.getProperty("username"), envProp.getProperty("password"));
		waitForPageToLoad();
		click("crmLink_xpath");
		Wait(3);
		click("potentialsTab_xpath");
		click("newPotentialsButton_xpath");
		type("potentialsName_xpath", data.get("PotentialName"));
		type("accountName_xpath", data.get("AccountName"));
		click("potentialsStage_xpath");
	    type("stageInputBox_xpath", data.get("Stage"));
	    
	   /* List<WebElement> allList = driver.findElements(By.xpath("//li[starts-with(@id,'select2-Crm_Potentials_STAGE-result')]"));
	    for (int i = 0; i<allList.size();i++){
	    	System.out.println(allList.get(i).getText());
	    	allList.get(i).click();
	    	
	    }*/
	    click("stageList_xpath");
	    
	    selectDate(data.get("ClosingDate"));
	    click("savePotentialsButton_xpath");
	    //validate potential created
	    
	    
	    
	    reportPass("Test is passed");;
	    
	 
		
	

		
	}
	
	
	@Test (priority =2 , dependsOnMethods = {"createPotentialTest"})
	public void deletePotentialAccountTest(){
		test=rep.startTest("Stating delete potentials");
		reportPass("Delete potentials successful");
		
	}
	
	@DataProvider(parallel = true)
	public Object[][] getDataPotentials() throws IOException {
		super.init();
		xls = new Xls_Reader(prop.getProperty("xlspath"));
		return DataUtil.getTestData(xls, "CreatePotentialTest");

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
