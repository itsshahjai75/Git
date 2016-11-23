package com.example.grandma1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class placedetails extends Activity {
	TextView name,phone,ino,web,addr,txt1,txt2,txt4,txt6;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.placedetail);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#790B28")));
		// Getting reference to WebView ( wv_place_details ) of the layout activity_place_details
		name=(TextView)findViewById(R.id.hospname);
		addr=(TextView)findViewById(R.id.addr);
		phone=(TextView)findViewById(R.id.phone);
		ino=(TextView)findViewById(R.id.ino);
		web=(TextView)findViewById(R.id.website);
		txt1=(TextView)findViewById(R.id.textView1);
		txt2=(TextView)findViewById(R.id.textView2);
		txt4=(TextView)findViewById(R.id.textView4);
		txt6=(TextView)findViewById(R.id.textView6);
		
	
		
		// Getting place reference from the map	
		String reference = getIntent().getStringExtra("reference");
		
		
		StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
		sb.append("reference="+reference);
		sb.append("&sensor=true");
		sb.append("&key=AIzaSyApu_hTO6pztTZeJrHhG5P2RuINw4fx_Rs");
		
		
		// Creating a new non-ui thread task to download Google place details 
        PlacesTask placesTask = new PlacesTask();		        			        
        
		// Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());	
		
	};
	
	
	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);                
                

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();                

                // Connecting to url 
                urlConnection.connect();                

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }

                data = sb.toString();
                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }

        return data;
    }         

	
	/** A class, to download Google Place Details */
	private class PlacesTask extends AsyncTask<String, Integer, String>{
		
		private ProgressDialog dialog;
		@Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            
            dialog = new ProgressDialog(placedetails.this);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        } 

		String data = null;
		
		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try{
				data = downloadUrl(url[0]);
			}catch(Exception e){
				 Log.d("Background Task",e.toString());
			}
			return data;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result){
			dialog.dismiss();
			ParserTask parserTask = new ParserTask();
			
			// Start parsing the Google place details in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			parserTask.execute(result);
		}
	}
	
	
	/** A class to parse the Google Place Details in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, HashMap<String,String>>{

		JSONObject jObject;
		private ProgressDialog dialog;
		@Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            
            dialog = new ProgressDialog(placedetails.this);
            dialog.setCancelable(false);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }
		
		// Invoked by execute() method of this object
		@Override
		protected HashMap<String,String> doInBackground(String... jsonData) {
		
			HashMap<String, String> hPlaceDetails = null;
			PlacedetailsJSONParser placeDetailsJsonParser = new PlacedetailsJSONParser();
        
	        try{
	        	jObject = new JSONObject(jsonData[0]);
	        	
	            // Start parsing Google place details in JSON format
	            hPlaceDetails = placeDetailsJsonParser.parse(jObject);
	            
	        }catch(Exception e){
	                Log.d("Exception",e.toString());
	        }
	        return hPlaceDetails;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(HashMap<String,String> hPlaceDetails){			
			
			dialog.dismiss();
			txt1.setVisibility(View.VISIBLE);
			txt2.setVisibility(View.VISIBLE);
			txt4.setVisibility(View.VISIBLE);
			txt6.setVisibility(View.VISIBLE);
			String hospname = hPlaceDetails.get("name");
			String icon = hPlaceDetails.get("icon");
			String vicinity = hPlaceDetails.get("vicinity");
			String lat = hPlaceDetails.get("lat");
			String lng = hPlaceDetails.get("lng");
			String formatted_address = hPlaceDetails.get("formatted_address");
			String formatted_phone = hPlaceDetails.get("formatted_phone");
			String website = hPlaceDetails.get("website");
			String rating = hPlaceDetails.get("rating");
			String international_phone_number = hPlaceDetails.get("international_phone_number");
			String url = hPlaceDetails.get("url");
			
			name.setText(hospname);
			phone.setText(formatted_phone);
			addr.setText(formatted_address);
			ino.setText(international_phone_number);
			web.setText(website);
			
			// Setting the data in WebView
						
		}
	}
}