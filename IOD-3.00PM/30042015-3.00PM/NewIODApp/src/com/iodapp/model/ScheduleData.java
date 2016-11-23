package com.iodapp.model;

import java.util.ArrayList;

public class ScheduleData {

	private String endDate,
				   startDate,
				   schedualID,
				   fromTime,
				   toTime,
				   day,
				   month,
				   appointmentID,
				   drName;
	
	public String getDay() {
		return day;
	}


	public String getDrName() {
		return drName;
	}


	public void setDrName(String drName) {
		this.drName = drName;
	}


	public String getMonth() {
		return month;
	}


	public void setMonth(String month) {
		this.month = month;
	}


	public String getAppointmentID() {
		return appointmentID;
	}


	public void setAppointmentID(String appointmentID) {
		this.appointmentID = appointmentID;
	}


	public void setDay(String day) {
		this.day = day;
	}


	public ScheduleData(){
		
	}
	

	public String getFromTime() {
		return fromTime;
	}


	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}


	public String getToTime() {
		return toTime;
	}


	public void setToTime(String toTime) {
		this.toTime = toTime;
	}


	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getSchedualID() {
		return schedualID;
	}

	public void setSchedualID(String schedualID) {
		this.schedualID = schedualID;
	}

	public void setAvailable(String str) {
		// TODO Auto-generated method stub
		
	}

	public void setDoctore_id(String string) {
		// TODO Auto-generated method stub
		
	} 
	
}
