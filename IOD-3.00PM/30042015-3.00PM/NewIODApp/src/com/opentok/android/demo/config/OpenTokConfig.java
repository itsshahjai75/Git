package com.opentok.android.demo.config;


import com.iodapp.activities.Sharedpref;

public class OpenTokConfig {
	
	// *** Fill the following variables using your own Project info from the OpenTok dashboard  ***
	// ***                      https://dashboard.tokbox.com/projects                           ***
	// Replace with a generated Session ID
	public static final String SESSION_ID= Sharedpref.getsessionkey();
	// Replace with a generated token (from the dashboard or using an OpenTok server SDK)
	public static final String TOKEN = Sharedpref.getTokenkey(); 
	// Replace with your OpenTok API key
	public static final String API_KEY= "45002332";
	
	// Subscribe to a stream published by this client. Set to false to subscribe
    // to other clients' streams only.
    public static final boolean SUBSCRIBE_TO_SELF = false;
    
   
}
