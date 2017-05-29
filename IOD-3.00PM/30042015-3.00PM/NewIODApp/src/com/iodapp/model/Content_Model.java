package com.iodapp.model;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class Content_Model {
	
	String content_Url = "http://jsoncdn.webcodeplus.com/ContentData.svc/ChildContentListFromContentID/30/816";
	
	private HashMap<String, String> map = new HashMap<String, String>();
	
	
	
	public void getDoctorsList() {
		JsonArrayRequest movieReq = new JsonArrayRequest(content_Url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						
					
						String str = "";
						// Parsing json
//					
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
								Log.e("ContentTitle"+i, obj.getString("ContentTitle"));
								
								map.put("ContentTitle"+i, obj.getString("ContentTitle"));
								map.put("LargeImage"+i, obj.getString("LargeImage"));
								
								map.put("Tagline"+i, obj.getString("Tagline"));
							

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
						
						
						
						// notifying list adapter about data changes
						// so that it renders the list view with updated data
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						
					

					}
				});

		System.out.println("Contetnt =" +map.get("ContentTitle1"));

	}
	
	public static void main(String[] args)
	{
		Content_Model cm = new Content_Model();
		cm.getDoctorsList();
		
	}
	
}
