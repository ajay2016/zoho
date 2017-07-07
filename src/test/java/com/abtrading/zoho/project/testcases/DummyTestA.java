package com.abtrading.zoho.project.testcases;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.abtrading.zoho.project.base.BaseTest;

public class DummyTestA extends BaseTest{
	 
	//dependent test cases on the same java files for the GRID
	//output of one test case can input for other
	@Test(priority = 1)
	public void testA1(){
		test=rep.startTest("Dummy TestA Starting");
		Assert.fail();
		
	}
	
	@Test(priority = 2, dependsOnMethods ={"testA1"})
	public void testA2(){
		
	}
	
	@Test(priority = 3, dependsOnMethods ={"testA1","testA2"} )
	public void testA3(){
		
	}
	@AfterMethod
	public void quit(){
		rep.endTest(test);
		rep.flush();
}
}
