package com.abtrading.zoho.project.testcases.temp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Temp {

	public static void main(String[] args) throws ParseException {
		Date d = new Date();
	System.out.println(d.toString().replace(" ", "_").replace(":", "_"));	
	String date ="5/8/2016";
	//Simple date format
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	//converting to date object
	Date d1 = sdf.parse(date);
	//current date is greater than our date returns 1
	//if less than than -1
	//if equal then  0
	System.out.println(d.compareTo(d1));
	sdf = new SimpleDateFormat("dd");
	//print day only
	System.out.println(sdf.format(d));
	System.out.println(sdf.format(d1));
	sdf = new SimpleDateFormat("MMMM");
	String month = sdf.format(d1);
	System.out.println(month);
	System.out.println(sdf.format(d1));
	sdf = new SimpleDateFormat("yyyy");
	String year = sdf.format(d1);
	System.out.println(year);
	System.out.println(sdf.format(d1));
	System.out.println(month+" "+year);
	
	}

}
