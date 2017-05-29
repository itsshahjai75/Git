package com.iodapp.activities;

public class PaymentDetailName {

	private String GATEWAY_ID;
    private String PASSWORD;
    private String HMAC_KEY;
    private String key_id;
    private String cardholder_name;
    private String cc_number;
    private String cc_expiry;
    private String cvd_code;
    private String amount;
    private String transaction_type;
    
	

	public String getGATEWAY_ID()
	{
	    return this.GATEWAY_ID;
	}
	public void setGATEWAY_ID(String value)
	{
	     this.GATEWAY_ID = value;
	}
	//---------------------------------
	public String getPASSWORD()
	{
	    return this.PASSWORD;
	}
	public void setPASSWORD(String value)
	{
	     this.PASSWORD = value;
	}
	//---------------------------------
	
	public String getkey_id()
	{
	    return this.key_id;
	}
	public void setkey_id(String value)
	{
	     this.key_id = value;
	}
	//---------------------------------
	public String getHMAC_KEY()
	{
	    return this.HMAC_KEY;
	}
	public void setHMAC_KEY(String value)
	{
	     this.HMAC_KEY = value;
	}
	//---------------------------------
	
	public String getcardholder_name()
	{
	    return this.cardholder_name;
	}
	public void setcardholder_name(String value)
	{
	     this.cardholder_name = value;
	}
	//---------------------------------
	public String getcc_number()
	{
	    return this.cc_number;
	}
	public void setcc_number(String value)
	{
	     this.cc_number = value;
	}
	
	public String getcc_expiry()
	{
	    return this.cc_expiry;
	}
	public void setcc_expiry(String value)
	{
	     this.cc_expiry = value;
	}
	
	public String getcvd_code()
	{
	    return this.cvd_code;
	}
	public void setcvd_code(String value)
	{
	     this.cvd_code = value;
	}
	
	public String getamount()
	{
	    return this.amount;
	}
	public void setamount(String value)
	{
	     this.amount = value;
	}
	
	public String gettransaction_type()
	{
	    return this.transaction_type;
	}
	public void settransaction_type(String value)
	{
	     this.transaction_type = value;
	}
//getters & setters....
}
