package com.iodapp.model;

public class OrderHistoryBean {

	private String ord_date,
					ord_id,
					plan,
					paid,
					day,
					month;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getOrd_date() {
		return ord_date;
	}

	public void setOrd_date(String ord_date) {
		this.ord_date = ord_date;
	}

	public String getOrd_id() {
		return ord_id;
	}

	public void setOrd_id(String ord_id) {
		this.ord_id = ord_id;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}
	
}
