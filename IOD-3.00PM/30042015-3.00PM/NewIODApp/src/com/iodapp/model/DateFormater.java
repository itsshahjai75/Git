package com.iodapp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormater {

	public static String formateTime(String date)
	{
		String fTime="";
		
		try {
			
			SimpleDateFormat sm = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			Date df = sm.parse(date);
			SimpleDateFormat time = new SimpleDateFormat("h:mm a");
			fTime = time.format(df);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
		return fTime;
	}
	public static String formateMonth(String date)
	{
		String fTime="";
		
		try {
			
			SimpleDateFormat sm = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			Date df = sm.parse(date);
			SimpleDateFormat time = new SimpleDateFormat("MMM");
			fTime = time.format(df);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
		return fTime;
	}
	public static String getDay(String date)
	{
		String fTime="";
		
		try {
			
			SimpleDateFormat sm = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			Date df = sm.parse(date);
			
			fTime = String.valueOf(df.getDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
		return fTime;
	}
	
	public static String getDate(String date)
	{
String fTime="";
		
		try {
			
			SimpleDateFormat sm = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			Date df = sm.parse(date);
			SimpleDateFormat time = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			fTime = time.format(df);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
		return fTime;
	}
}
