package com.iodapp.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

public class OneTimeLocal extends Activity {


	ListView onetimeLocal;
	ArrayAdapter<String> adapter;
	SharedPreferences data;
	
	public static List<String> listItems1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
        
		setContentView(R.layout.activity_one_time_local);
		Log.d("TAG","111111");
		
		 View titleView = getWindow().findViewById(android.R.id.title);
		    if (titleView != null) {
		      ViewParent parent = titleView.getParent();
		      if (parent != null && (parent instanceof View)) {
		        View parentView = (View)parent;
		        parentView.setBackgroundColor(Color.parseColor("#ffffff"));
		      }
		    }
		    
		Log.d("TAG","222");
		
		ImageButton A_back = (ImageButton)this.findViewById(R.id.btn_back_onetimelocal);
		A_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
				onBackPressed();
			finish();
			}
			});
		
		onetimeLocal =(ListView)this.findViewById(R.id.ontimelocal_list);
		
		listItems1 = new ArrayList<String>();
	adapter = new ArrayAdapter<String>(OneTimeLocal.this,
	              R.layout.textview_white,R.id.tv, listItems1);
		
	Log.d("TAG","33333");
		
		 new OnetimelocalHttpAsyncTask()
			.execute("http://jsoncdn.webcodeplus.com/ProductData.svc/ProductAttributesValues/30/409");

			
			Button btn_aditional_details = (Button)this.findViewById(R.id.btn_aditional_detials_onelocal);
			btn_aditional_details.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					

					data =  PreferenceManager.getDefaultSharedPreferences(getBaseContext());
					data.edit().putString("ProductID", "409").commit();
					data.edit().putString("LocalCredit", "0").commit();
					data.edit().putString("InternationalCredit", "1").commit();
					data.edit().putString("Validity", "7").commit();
					data.edit().putString("checkboxcheck", "no").commit();
					
					
					
					Intent it_login = new Intent(OneTimeLocal.this,
							AdditionalDetails.class);
					Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
							OneTimeLocal.this, R.anim.animation,
							R.anim.animation2).toBundle();
					startActivity(it_login, bndlanimation);
					finish();
					
				}
			});
			
	}

	private class OnetimelocalHttpAsyncTask extends AsyncTask<String, Void, String> {
		ProgressBar progressBar;
		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();    
		

			Log.d("TAG","pre starts");
			progressBar = new ProgressBar(OneTimeLocal.this);
			progressBar.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected String doInBackground(String... urls) {


			Log.d("TAG","back gound starts");
			return GET(urls[0]);
		}

	

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Log.d("TAG","post gound starts");
		   
			//listItems1.clear();
			progressBar.setVisibility(View.INVISIBLE);
			
			
			try {
				// JSONObject json = new JSONObject(result);
				JSONArray ja = new JSONArray(result);

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					listItems1.add(jo.getString("AttributeValue"));
					
				}
				onetimeLocal.setAdapter(adapter);
				Sharedpref.setamount("14.99");
				 
				//
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onCancelled() {

			super.onCancelled();
//			Toast toast = Toast.makeText(getActivity(),
//					"Error is occured due to some probelm", Toast.LENGTH_LONG);
//			toast.show();

		}
	}
	
	
	public static String GET(String url) {
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}
	
	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}
}

