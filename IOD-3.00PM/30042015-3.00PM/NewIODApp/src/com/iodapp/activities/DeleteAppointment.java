package com.iodapp.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.iodapp.adapter.DeleteScheduleAdapter;
import com.iodapp.model.BookAppointmentBean;
import com.iodapp.model.DateFormater;
import com.iodapp.model.ScheduleData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DeleteAppointment extends Activity {

	private String url;
	private ListView schd_list;
	private DeleteScheduleAdapter schAdapter;
	private List<ScheduleData> movieItemslist = new ArrayList<ScheduleData>();
	private ProgressDialog pDialog;
	private Typeface sysFont;
	private String appontmentID;
	private String delUrl;
	private String BookMsg;
	public String res;
	private TextView title,noappointment_tv;
	private ImageButton A_back;
	
	LinearLayout appointmnt_main;
	FrameLayout main_lyout;
	TextView book_new_appointment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
		setContentView(R.layout.activity_delete_appointment);
		
		
          url = "http://jsoncdn.webcodeplus.com/AppointmentData.svc/GetAppointmentsList/"+Sharedpref.getcustomerid();
          delUrl = "http://jsoncdn.webcodeplus.com/AppointmentData.svc/DeleteAppointment";
		
          appointmnt_main =(LinearLayout)this.findViewById(R.id.appointment_main);
          
          title = (TextView) findViewById(R.id.A_title);
         

		schd_list = (ListView) findViewById(R.id.BookedList);
		schAdapter = new DeleteScheduleAdapter(DeleteAppointment.this, movieItemslist);
		schd_list.setAdapter(schAdapter);
		
		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();
		getBookedSchedualList();
		
		 A_back = (ImageButton) findViewById(R.id.d_back);
		A_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
				onBackPressed();
			finish();
			}
			});
		noappointment_tv =(TextView)this.findViewById(R.id.tv_noappointment);
		
		
		
		
		schd_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				ScheduleData sd = (ScheduleData) movieItemslist.get(pos);
				appontmentID = sd.getAppointmentID();
				
				showDialog();
				
			}
		});
		
		book_new_appointment=(TextView)this.findViewById(R.id.btn_booknew_appoinyment);

		main_lyout =  (FrameLayout)this.findViewById(R.id.frame_deletappointment);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete_appointment, menu);
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
	
	private void showDialog()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				DeleteAppointment.this);
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage("Cancel appointment ?").setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						
					new CancelAsyncTaskpost().execute(delUrl);
						
					
						
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
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
		
		
		title.setTypeface(sysFont);
		
	}
	

	public void getBookedSchedualList() {
		JsonArrayRequest movieReq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d("BookNow", response.toString());
						hidePDialog();
						String str = "";
						Bitmap mBitmap = null;
						// Parsing json
						movieItemslist.clear();
						
	if(response.length()<=0){
							
		
				//schd_list.removeAllViews();		
		
			//	appointmnt_main.removeView(schd_list);
			
				noappointment_tv.setVisibility(View.VISIBLE);
			book_new_appointment.setVisibility(View.VISIBLE);
			noappointment_tv.setText("Sorry!\nNo Appointment Booked.");
				
				book_new_appointment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Intent book_appointmet = new Intent(DeleteAppointment.this,ScheduleAppintment.class);
					startActivity(book_appointmet);
					finish();
					
					
				}
			});
							

							
							
							
		}
		else{
						
			noappointment_tv.setVisibility(View.GONE);
			book_new_appointment.setVisibility(View.GONE);
			
			
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
//								Movie movie = new Movie();
								
								
								ScheduleData sData = new ScheduleData();
//								String str1 = obj.getString("StartTime").toString();
//								sData.setStartDate(obj.getString("StartTime"));
//								sData.setEndDate(obj.getString("EndTime"));
								sData.setSchedualID(obj.getString("ScheduleID"));
								sData.setAppointmentID(obj.getString("AppointmentID"));
								sData.setDrName(obj.getString("FullName"));
								
								String fromTime = DateFormater.formateTime(obj.getString("AppointmentTime").toString());
								sData.setFromTime(fromTime);
								sData.setToTime(DateFormater.formateTime(obj.getString("AppointmentTime").toString()));
								sData.setDay(DateFormater.getDay(obj.getString("AppointmentTime").toString()));
								sData.setMonth(DateFormater.formateMonth(obj.getString("AppointmentTime").toString()));
								
								
								
								
								
								
								
//								
								// adding movie to movies array
								movieItemslist.add(sData);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
						}

						// notifying list adapter about data changes
						// so that it renders the list view with updated data
						schAdapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d("Cancel Appointment", "Error: " + error.getMessage());
						hidePDialog();

					}
				});

		// Adding request to request queue
	Sharedpref.getInstance().addToRequestQueue(movieReq);

	}

	
	private String formateTime(String date)
	{
		String fTime="";
		
		try {
			
			SimpleDateFormat sm = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			Date df = sm.parse(date);
			SimpleDateFormat time = new SimpleDateFormat("h:mm a");
			fTime = time.format(df);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
		return fTime;
	}
	private String formateMonth(String date)
	{
		String fTime="";
		
		try {
			
			SimpleDateFormat sm = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			Date df = sm.parse(date);
			SimpleDateFormat time = new SimpleDateFormat("MMM");
			fTime = time.format(df);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
		return fTime;
	}
	private String getDay(String date)
	{
		String fTime="";
		
		try {
			
			SimpleDateFormat sm = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
			Date df = sm.parse(date);
			
			fTime = String.valueOf(df.getDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	
		return fTime;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}

private class CancelAsyncTaskpost extends AsyncTask<String, Void, String> {
		
		

		

		@Override
		protected String doInBackground(String... urls) {

			
		BookAppointmentBean ba = new BookAppointmentBean();
		ba.setAppointmentID(appontmentID);
		ba.setCustomerID(Sharedpref.getcustomerid());
		

			return POST(urls[0], ba);
		}
		@Override
		protected void onPreExecute() {
		// TODO Auto-generated method stub
			
		super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(String result) {
//			
			
			if(Integer.parseInt(result) == 1)
			{
				res = "Yes";
				BookMsg = "Appointment Cancelled.";
				
				showConformationDailog();
			}
			else
			{
				res = "No";
				BookMsg = "Your Appointment Not Canceled.";
				showConformationDailog();
			}
		
			// etResponse.setText(json.toString());
		}
		
		@Override
		protected void onCancelled() {
		// TODO Auto-generated method stub
			
		super.onCancelled();
		}
		
		}
	
	public static String POST(String url, BookAppointmentBean ba) {
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
			
			jsonObject.accumulate("AppointmentID", ba.getAppointmentID());
			
			
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
	
	
	private void showConformationDailog()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				DeleteAppointment.this);
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage(BookMsg).setCancelable(false)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						if(res.equalsIgnoreCase("Yes"))
						{
							dialog.cancel();
							finish();
						}
						
					
					
						
					}
				  });
			
 
			
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

 @Override
public void onBackPressed() {
	// TODO Auto-generated method stub
	 finish();
	super.onBackPressed();
}

}
