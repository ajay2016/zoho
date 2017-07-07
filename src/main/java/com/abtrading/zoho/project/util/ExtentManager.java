package com.abtrading.zoho.project.util;

//http://relevantcodes.com/Tools/ExtentReports2/javadoc/index.html?com/relevantcodes/extentreports/ExtentReports.html


import java.io.File;

import java.util.Date;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
	private static ExtentReports extent;
	
	//returns the object of extent report
	public static ExtentReports getInstance() {
		if (extent == null) {
			//SimpleDateFormat df =  new SimpleDateFormat("dd/MM/yyyy");
			//Date d = df.parse(date);
			Date d=new Date();
		//	System.out.println(d);
			
			//get the file name through date
			//replace : and blank space with _
			String fileName=d.toString().replace(":", "_").replace(" ", "_")+".html";
			extent = new ExtentReports(System.getProperty("user.dir")+"//Report//"+fileName, true, DisplayOrder.NEWEST_FIRST);

			//load configuration file
			extent.loadConfig(new File(System.getProperty("user.dir")+"//ReportsConfig.xml"));
			// optional
			extent.addSystemInfo("Selenium Version", "3.0.1").addSystemInfo(
					"Environment", "QA");
		}
		return extent;
	}
}
