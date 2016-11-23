package com.iodapp.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import com.iodapp.model.BookAppointmentBean;
import com.iodapp.model.DateFormater;
import com.iodapp.model.HistoryBean;
import com.iodapp.util.URLSuppoter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class UnsuscribePlan extends Activity {

	private String Geturl = "http://jsoncdn.webcodeplus.com/OrderData.svc/GetUnSubscribeData/"+Sharedpref.getcustomerid();
	private String unsusStr ="http://jsoncdn.webcodeplus.com/OrderData.svc/UnsubscribeData";
	private ProgressDialog pDialog;
	private TextView suscribe_date;
	private TextView suscribe_plan;
	private TextView suscribe_time;
	private Button unsuscribe,suscribe_now;
	private TextView no_suscribe;
	public String BookMsg;
	private Typeface sysFont;
	private ImageButton back_btn;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
		setContentView(R.layout.activity_unsuscribe_plan);
		
		suscribe_date = (TextView) findViewById(R.id.sus_date);
		suscribe_plan = (TextView) findViewById(R.id.sus_plan);
		suscribe_time = (TextView) findViewById(R.id.sus_time);
		unsuscribe = (Button) findViewById(R.id.unsuscribeBtn);

		suscribe_now = (Button) findViewById(R.id.unsuscribeBtn_now);
		no_suscribe = (TextView) findViewById(R.id.No_plan);
		title = (TextView) findViewById(R.id.sus_title);
		
		back_btn = (ImageButton) findViewById(R.id.sus_back_btn);
		
		viewInvisible();
		
		setSystemFont();
		
		back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		
		
		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();
		new GetSuscribeList().execute(Geturl);
		
		
		unsuscribe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
		
		suscribe_now.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				startActivity(new Intent(UnsuscribePlan.this, MonthlyPlanActivity.class));
				
			}
		});
		
		
	}
	
	
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
		
		suscribe_date.setTypeface(sysFont);
		suscribe_plan.setTypeface(sysFont);
		suscribe_time.setTypeface(sysFont);
		unsuscribe.setTypeface(sysFont);
		no_suscribe.setTypeface(sysFont);
		title.setTypeface(sysFont);

		
	}

	private void showDialog()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				UnsuscribePlan.this);
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage("Unsubcribe This Plan?").setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						
						new UnSuscribePlan().execute(unsusStr);
						
					
						
					}
				  }).setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							
							
							dialog.cancel();
							
						}});
			
 
			
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				
//				alertDialog.getButton(0).setBackgroundColor(Color.BLACK);
				//alertDialog.getButton(1).setBackgroundColor(Color.BLACK);
			
				
 
				// show it
				alertDialog.show();
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#ffffff"));
				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(Color.parseColor("#ffffff"));
				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00bfff"));
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00bfff"));
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(19.0f);
				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(19.0f);
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(sysFont);
				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(sysFont);
				
	}
	private void viewVisisble()
	{
		suscribe_date.setVisibility(View.VISIBLE);
		suscribe_plan.setVisibility(View.VISIBLE);
		suscribe_time.setVisibility(View.VISIBLE);
		unsuscribe.setVisibility(View.VISIBLE);
		no_suscribe.setVisibility(View.INVISIBLE);
		suscribe_now.setVisibility(View.GONE);
		
	}
	private void viewInvisible()
	{
	
		suscribe_date.setVisibility(View.INVISIBLE);
		suscribe_plan.setVisibility(View.INVISIBLE);
		suscribe_time.setVisibility(View.INVISIBLE);
		unsuscribe.setVisibility(View.GONE);
		no_suscribe.setVisibility(View.VISIBLE);
		suscribe_now.setVisibility(View.VISIBLE);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.unsuscribe_plan, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	

								
								
private class GetSuscribeList extends AsyncTask<String, Void, String> {
		
		private URLSuppoter SuppURL;
			@Override
		protected String doInBackground(String... urls) {
		  SuppURL = new URLSuppoter();
				  return SuppURL.GET(urls[0]);
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		
		@Override
		protected void onPostExecute(String result) {
			

			try {
				
				
				JSONObject obj = new JSONObject(result);
				
				suscribe_date.setText(DateFormater.getDay(obj.getString("CreatedDate"))+"\n"+DateFormater.formateMonth(obj.getString("CreatedDate")));
				suscribe_time.setText(DateFormater.formateTime(obj.getString("CreatedDate")));
				suscribe_plan.setText(obj.getString("ProductName"));
				String str = obj.getString("ProductName");
				
				if(str.equalsIgnoreCase("null"))
				{
					viewInvisible();
				}
				else
				{
					viewVisisble();
				}
				
				
				hidePDialog();
				
//				 etResponse.setText(json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onCancelled() {

			super.onCancelled();
			Toast toast = Toast.makeText(getApplicationContext(),
					"Error is occured due to some probelm", Toast.LENGTH_LONG);
			toast.show();
			hidePDialog();

		}
	}


@Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	finish();
	super.onBackPressed();
}
private class UnSuscribePlan extends AsyncTask<String, Void, String> {
	
	private URLSuppoter SuppURL;
		@Override
	protected String doInBackground(String... urls) {
			
			
			return POST(urls[0]);
		
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	
	@Override
	protected void onPostExecute(String result) {
		

		if(Integer.parseInt(result) > 0)
		{
			
			BookMsg = "Unsubscribed Succsessfully.";
			
			showConformationDailog();
		}
		else
		{
		
			BookMsg = "Unsubscribed Canceled.";
			showConformationDailog();
		}
	}

	@Override
	protected void onCancelled() {

		super.onCancelled();
		Toast toast = Toast.makeText(getApplicationContext(),
				"Error is occured due to some probelm", Toast.LENGTH_LONG);
		toast.show();
		hidePDialog();

	}
}
		
private void showConformationDailog()
{
	
	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			UnsuscribePlan.this);

	
		// set title
//		alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
		
		// set dialog message
		alertDialogBuilder
			.setMessage(BookMsg).setCancelable(false)
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					
					onBackPressed();
					dialog.cancel();
					
				
				
					
				}
			  });
		

		
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			
//			alertDialog.getButton(0).setBackgroundColor(Color.BLACK);
			//alertDialog.getButton(1).setBackgroundColor(Color.BLACK);
		
			

			// show it
			alertDialog.show();
			alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#ffffff"));
			alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(Color.parseColor("#ffffff"));
			alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00bfff"));
			alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00bfff"));
			alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(19.0f);
			alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(19.0f);
			alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(sysFont);
			alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(sysFont);
			
}



public static String POST(String url) {
	InputStream inputStream = null;
	String result = "";
	try {

		// 1. create HttpClient
		HttpClient httpclient = new DefaultHttpClient();
		// 2. make POST request to the given URL
		HttpPost httpPost = new HttpPost(url);
		// String json = "";

		// 3. build jsonObject
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.accumulate("CustomerID", Sharedpref.getcustomerid());
		
		
		Log.d("Post JsonObject", jsonObject.toString());
		
		// 4. convert JSONObject to JSON to String
		// json = jsonObject.toString();

		// ** Alternative way to convert Person object to JSON string usin
		// Jackson Lib
		// ObjectMapper mapper = new ObjectMapper();
		// json = mapper.writeValueAsString(person);

		// 5. set json to StringEntity
		StringEntity se = new StringEntity(jsonObject.toString());

		// 6. set httpPost Entity
		httpPost.setEntity(se);

		// // 7. Set some headers to inform server about the type of the
		// content
		// httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader(HTTP.CONTENT_TYPE,
				"application/json; charset=utf-8");
		// se.setContentType("application/json;charset=UTF-8");
		// se.setContentEncoding(new
		// BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
		// 8. Execute POST request to the given URL
		HttpResponse httpResponse = httpclient.execute(httpPost);

		// 9. receive response as inputStream
		inputStream = httpResponse.getEntity().getContent();

		// 10. convert inputstream to string
		if (inputStream != null)
		{
			result = convertInputStreamToString(inputStream);
		
		}
		else
			result = "Did not work!";
		
		
		
	} catch (Exception e) {
		Log.d("InputStream", e.getLocalizedMessage());
	}

	// 11. return result
	Log.d("result",""+result);
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
	
	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}
}
