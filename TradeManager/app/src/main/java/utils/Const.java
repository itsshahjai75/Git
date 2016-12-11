package utils;

import android.app.Activity;
import android.os.Environment;

import java.util.ArrayList;

public class Const {

	public static String SD_CARD_PATH = Environment
			.getExternalStorageDirectory() + "/" + "Medsearch";
	public static String MyPREFERENCES = "MedsearchPref";

	// public String faciComp;
	// public String address;
	// public String country;
	// public String zipCode;
	// public String phoneNo;
	// public String ext;
	// public String website;
	// public String aboutRef;

	public static String PREF_LOGINKEY= "PREF_LOGINKEY";
	public static String PREF_USER_ID = "PREF_USER_ID";
	public static String PREF_FACEBOOK_ID = "PREF_FACEBOOK_ID";
	public static String PREF_MOBILE_NO = "PREF_MOBILE_NO";
	public static String PREF_FULL_NAME = "PREF_FULL_NAME";
	public static String PREF_PROFILE_PIC_URL = "PREF_PROFILE_PIC_URL";
	public static String PREF_PASSWORD = "PREF_PASSWORD";
	public static String PREF_EMAIL = "PREF_EMAIL";
	public static String PREF_USER_TOKEN = "PREF_USER_TOKEN";

	/** for pic */
	public static String PREF_TEMP_PIC_ID = "PREF_TEMP_PIC_ID";

	// public static String PREF_PUSH_ID = "PREF_PUSH_ID";
	// public static String PREF_PROFILE_PIC_URL = "PREF_PROFILE_PIC_URL";
	// public static String PREF_BIRTHDAY = "PREF_BIRTHDAY";
	// public static String PREF_GENDER = "PREF_GENDER";
	// public static String PREF_CONTACT_NO = "PREF_CONTACT_NO";

	/*
	 * Cookie and SESSION
	 */
	// connection timeout is set to 30 seconds
	public static int TIMEOUT_CONNECTION = 30000;
	// SOCKET TIMEOUT IS SET TO 30 SECONDS
	public static int TIMEOUT_SOCKET = 30000;

	public static String PREF_SESSION_COOKIE = "sessionid";
	public static String SET_COOKIE_KEY = "Set-Cookie";
	public static String COOKIE_KEY = "Cookie";
	public static String SESSION_COOKIE = "sessionid";

	public static int API_SUCCESS = 0;
	public static int API_FAIL = 1;

	public static String API_RESULT_SUCCESS = "200";
	public static String API_RESULT_FAIL = "400";

	/*** BACKEND VARIABLES */
	public static String SERVER_URL = "http://websitetestingbox.com/php/lottery/api/";
	public static String WEBSITE_PIC_URL = "http://websitetestingbox.com/php/lottery/";// assets/images/";


	public static final String LOG_TAG = "Google Places Autocomplete";
	public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	public static final String OUT_JSON = "/json";
}
