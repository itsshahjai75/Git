package com.iodapp.activities;


import com.iodapp.util.LruBitmapCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class Sharedpref extends Application {
	static SharedPreferences pref;
	static SharedPreferences.Editor edit;

	public static final String TAG = Sharedpref.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private static Sharedpref mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		pref = getSharedPreferences("toast", MODE_PRIVATE);
		edit = pref.edit();
		
		edit.putBoolean("firstrun", true);
		edit.commit();
		
		mInstance = this;
	}
	
	
	public static synchronized Sharedpref getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public static void setcall(Boolean value) {
		edit.putBoolean("iscall", value);
		edit.commit();
	}

	public static Boolean getcall() {
		return pref.getBoolean("iscall", false);
	}

	public static void setregcheck(Boolean value) {
		edit.putBoolean("isregcheck", value);
		edit.commit();
	}

	public static Boolean getregcheck() {
		return pref.getBoolean("isregcheck", false);
	}

	public static void setloginStatusCheck(Boolean value) {
		edit.putBoolean("islogincheck", value);
		edit.commit();
	}

	public static Boolean getloginStatusCheck() {
		return pref.getBoolean("islogincheck", true);
	}

	public static void setlogoutStatus(Boolean value) {
		edit.putBoolean("islogout", value);
		edit.commit();
	}

	public static Boolean getlogoutStatus() {
		return pref.getBoolean("islogout", true);
	}

	public static void setloginStatus(Boolean value) {
		edit.putBoolean("islogin", value);
		edit.commit();
	}

	public static Boolean getloginStatus() {
		return pref.getBoolean("islogin", true);
	}

	public static void setcustomerid(String value) {
		edit.putString("iscid", value);
		edit.commit();
	}

	public static String getcustomerid() {
		return pref.getString("iscid", "");
	}

	public static void setstatecode(String value) {
		edit.putString("isstateid", value);
		edit.commit();
	}

	public static String getstatecode() {
		return pref.getString("isstateid", "");
	}

	public static void setmail(String value) {
		edit.putString("ismail", value);
		edit.commit();
	}

	public static String getmail() {
		return pref.getString("ismail", "");
	}

	public static void setfirstname(String value) {
		edit.putString("isfname", value);
		edit.commit();
	}

	public static String getfirstname() {
		return pref.getString("isfname", "");
	}

	public static void setlastname(String value) {
		edit.putString("islname", value);
		edit.commit();
	}

	public static String getlastname() {
		return pref.getString("islname", "");
	}

	public static void setpass(String value) {
		edit.putString("ispass", value);
		edit.commit();
	}

	public static String getpass() {
		return pref.getString("ispass", "");
	}

	public static void setamount(String value) {
		edit.putString("isamount", value);
		edit.commit();
	}

	public static String getamount() {
		return pref.getString("isamount", "");
	}

	public static void setcc_number(String value) {
		edit.putString("iscc_number", value);
		edit.commit();
	}

	public static String getcc_number() {
		return pref.getString("iscc_number", "");
	}

	public static void setautho_num(String value) {
		edit.putString("isautho_num", value);
		edit.commit();
	}

	public static String getautho_num() {
		return pref.getString("isautho_num", "");
	}

	public static void setexact_msg(String value) {
		edit.putString("isexact_msg", value);
		edit.commit();
	}

	public static String getexact_msg() {
		return pref.getString("isexact_msg", "");
	}

	public static void setcvv_code(String value) {
		edit.putString("iscvv_code", value);
		edit.commit();
	}

	public static String getcvv_code() {
		return pref.getString("iscvv_code", "");
	}

	public static void setexp_month(String value) {
		edit.putString("isexp_month", value);
		edit.commit();
	}

	public static String getexp_month() {
		return pref.getString("isexp_month", "");
	}

	public static void setexp_year(String value) {
		edit.putString("isexp_year", value);
		edit.commit();
	}

	public static String getexp_year() {
		return pref.getString("isexp_year", "");
	}

	public static void setsessionkey(String value) {
		edit.putString("issessionkey", value);
		edit.commit();
	}

	public static String getsessionkey() {
		return pref.getString("issessionkey", "");
	}

	public static void setTokenkey(String value) {
		edit.putString("isTokenkey", value);
		edit.commit();
	}

	public static String getTokenkey() {
		return pref.getString("isTokenkey", "");
	}
	
	public static void setTokenId(String value) {
		edit.putString("isTokenId", value);
		edit.commit();
	}
	
	public static String getTokenId() {
		return pref.getString("isTokenkId", "");
	}

	
	public static void settimerval(String value) {
		edit.putString("istimerval", value);
		edit.commit();
	}

	public static String gettimerval() {
		return pref.getString("istimerval", "");
	}
	
	public static void setLocalCredit(String value) {
		edit.putString("localCredit", value);
		edit.commit();
	}

	public static String getLocalCredit() {
		return pref.getString("intCredit", "");
	}
	public static void setIntCredit(String value) {
		edit.putString("localCredit", value);
		edit.commit();
	}

	public static String getIntCredit() {
		return pref.getString("intCredit", "");
	}
	
	
	public static void setFirstRunFalse()
	{
		edit.putBoolean("firstrun", false);
		edit.commit();
	}
	
	public static void setImagePath(String imgPath)
	{
		edit.putString("ImgPath", imgPath);
		edit.commit();
	}
	
	public static String getImagePath()
	{
		return pref.getString("ImgPath", "");
	}
	
	
	
}
