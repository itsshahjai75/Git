package com.iodapp.util;

import org.json.JSONException;
import org.json.JSONObject;

import com.iodapp.activities.Sharedpref;

import android.os.AsyncTask;
import android.util.Log;

public class SetDoctoreAvailable extends AsyncTask<String, Void, String>{

	private URLSuppoter suppoter;

	@Override
	protected String doInBackground(String... urls) {
		// TODO Auto-generated method stub
		suppoter = new URLSuppoter();
		
		return suppoter.GET(urls[0]);
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		Log.d("Result of Doctor", result.toString());
		
		Sharedpref.setTokenId("");
		Sharedpref.setTokenkey("");
		Sharedpref.setsessionkey("");
		
		
		
//		try {
//			
////			JSONObject jsonObject = new JSONObject(result);
////			Log.d("Json Data", jsonObject.toString());
//			
//		} catch (JSONException e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		super.onPostExecute(result);
	}
	

}
