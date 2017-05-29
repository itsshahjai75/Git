package billbook.smart.com.crickbat;

import android.os.Environment;

public class Const {

	public static String SD_CARD_PATH = Environment
			.getExternalStorageDirectory() + "/" + "Ovenues";
	public static String MyPREFERENCES = "OvenuesPref";

	// public String faciComp;
	// public String address;
	// public String country;
	// public String zipCode;
	// public String phoneNo;
	// public String ext;
	// public String website;
	// public String aboutRef;

	public static String PREF_LOGINKEY= "PREF_LOGINKEY";
	public static String INTRO_DONE= "INTRO_DONE";
	public static String PREF_USER_ID = "PREF_USER_ID";
	public static String PREF_FACEBOOK_ID = "PREF_FACEBOOK_ID";
	public static String PREF_USER_MOBILE_NO = "PREF_USER_MOBILE_NO";
	public static String PREF_USER_FULL_NAME = "PREF_USER_FULL_NAME";
	public static String PREF_USER_PROFILE_PIC_URL = "PREF_USER_PROFILE_PIC_URL";
	public static String PREF_PASSWORD = "PREF_PASSWORD";
	public static String PREF_USER_EMAIL = "PREF_USER_EMAIL";
	public static String PREF_USER_TOKEN = "PREF_USER_TOKEN";



	//====Pref List for search venues acording filters
	public static String PREF_CITY_ID= "CITY_ID";
	public static String PREF_CITY_NAME= "PREF_CITY_NAME";
	public static String PREF_EVENT_TYPE_ID= "EVENT_TYPE_ID";
	public static String PREF_VENUE_TYPE_ID = "VENUE_TYPE_ID";
	public static String PREF_AMENITIES_ID = "PREF_AMENITIES_ID";
	public static String PREF_GUEST_COUNT_MIN = "GUEST_COUNT_MIN";
	public static String PREF_GUEST_COUNT_MAX = "GUEST_COUNT_MAX";
	public static String PREF_PRICE_MIN = "PRICE_MIN";
	public static String PREF_PRICE_MAX = "PRICE_MAX";
	public static String PREF_SORT_BY = "SORT_BY";

	//====Pref List for search services acording filters

	public static String PREF_STR_SERVICE_ID= "EVENT_TYPE_ID";
	public static String PREF_STR_SERVICE_NAME= "PREF_STR_SERVICE_NAME";




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
	public static String SERVER_URL = "http://54.153.127.215/api/";
	public static String WEBSITE_PIC_URL = "https://www.ovenues.com/";// assets/images/";


	public static final String LOG_TAG = "Google Places Autocomplete";
	public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	public static final String OUT_JSON = "/json";
}
