package com.iodapp.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

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
import com.iodapp.adapter.ScheduleListAdapter;
import com.iodapp.model.BookAppointmentBean;
import com.iodapp.model.DateFormater;
import com.iodapp.model.Movie;
import com.iodapp.model.ScheduleData;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BookAppointment extends Activity {

	
	private static final String bookUlr = "http://jsoncdn.webcodeplus.com/AppointmentData.svc/CreateAppointment";
	private String drid;
	private List<ScheduleData> movieItemslist = new ArrayList<ScheduleData>();
	private ListView schd_list;
	private ScheduleListAdapter schAdapter;
	private Timer timer = new Timer();
	private int i=0;
	private ProgressDialog pDialog;
	String url;
	private TextView btnBook;
	private Typeface sysFont;
	private String msg="";
	private BookAppointmentBean bookApp;
	private String sch_id,BookMsg;
	private String getApplist;
	private int AppCount=0;
	private ImageButton b_back;
	private TextView b_title;
	protected Bundle bndlanimation;
	SharedPreferences data ;
	
	int pending_intent ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
		setContentView(R.layout.activity_book_appointment);
		
		
		data = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		
		
		
	//===this is coding for increment pending intend id every time when it calles=====	
		
		pending_intent = data.getInt("appointment_id", 1);
		System.out.println("got pending id====="+pending_intent);
		
		
		
		//=====================================================================================
		
		drid = getIntent().getExtras().getString("drId");
		
		
		url = "http://jsoncdn.webcodeplus.com/ScheduleData.svc/GetScheduleList/"+drid;
		  getApplist = "http://jsoncdn.webcodeplus.com/AppointmentData.svc/GetAppointmentsList/"+Sharedpref.getcustomerid();
		
		
		schd_list = (ListView) findViewById(R.id.bookList);
		schAdapter = new ScheduleListAdapter(BookAppointment.this, movieItemslist);
		schd_list.setAdapter(schAdapter);
		
		 b_back = (ImageButton) findViewById(R.id.book_btn);
		 b_title = (TextView) findViewById(R.id.book_title);

		
		
		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();
		
		
		 b_back.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) {
			 // TODO Auto-generated method stub

			 finish();
			 }
			 });
		
		
		
		timer.scheduleAtFixedRate(new TimerTask() {
			
			public void run() {
				Log.i("testing", "repeat testing");// display the data
				i = i + 1;
				//getDoctorsList();
				getSchedualList();
			}
		}, 0, 5000); //Available doctore list refresh ecery 10 second
		
		
	
		schd_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				
				
				// TODO Auto-generated method stub
				
				ScheduleData sd = (ScheduleData) movieItemslist.get(pos);
				//msg = sd.getDay() + ", "+sd.getMonth() + ", From : "+sd.getFromTime()+" , To : "+sd.getToTime();
			
				msg = sd.getFromTime()+",   "+sd.getDay()+" \t"+sd.getMonth();
				
				
				sch_id = sd.getSchedualID();
				
				
				
				showDialog();
				
				//Toast.makeText(BookAppointment.this, "Countinue?", Toast.LENGTH_LONG).show();
			}
		});
		
	}

	
	
	private void showDialog()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				BookAppointment.this);
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage("Appointment should be  "+msg +" onwards ?").setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						
						new BookAsyncTaskpost().execute(bookUlr) ;
						
					
						
					}
				  }).setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							
							Intent sch =new Intent(getApplicationContext(),DeleteAppointment.class);
							 bndlanimation = 
									ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
						
							startActivity(sch,bndlanimation);
							finish();
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
		
		b_title.setTypeface(sysFont);
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.book_appointment, menu);
		return true;
	}

	public void getSchedualList() {
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
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
//								Movie movie = new Movie();
								
								ScheduleData sData = new ScheduleData();
								String str1 = obj.getString("StartTime").toString();
								sData.setStartDate(obj.getString("StartTime"));
								sData.setEndDate(obj.getString("EndTime"));
								sData.setSchedualID(obj.getString("ScheduleID"));
								
								String fromTime = DateFormater.formateTime(obj.getString("StartTime").toString());
								sData.setFromTime(fromTime);
								sData.setToTime(DateFormater.formateTime(obj.getString("EndTime").toString()));
								sData.setDay(DateFormater.getDay(obj.getString("StartTime").toString()));
								sData.setMonth(DateFormater.formateMonth(obj.getString("StartTime").toString()));
								
								// adding movie to movies array
								movieItemslist.add(sData);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						// notifying list adapter about data changes
						// so that it renders the list view with updated data
						schAdapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d("BookNow", "Error: " + error.getMessage());
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
	
	
private class BookAsyncTaskpost extends AsyncTask<String, Void, String> {
		
		

		@Override
		protected String doInBackground(String... urls) {

		bookApp = new BookAppointmentBean();

		bookApp.setAppointmentID("0");
		bookApp.setCustomerID(Sharedpref.getcustomerid());
		bookApp.setNote("IODApp");
		bookApp.setAttanded("N");
		bookApp.setScheduleID(sch_id);
			

			return POST(urls[0], bookApp);
		}
		
		@Override
		protected void onPostExecute(String result) {
//			
			
			Log.d("appintmnt_response", result);
			
		
			
			
			if(!(result==null) || result.isEmpty())
			{
				BookMsg = "Appointment Booked!";
			
				String date_time=result;

				date_time= date_time.replace("\"", "");
				date_time= date_time.replace("\\", "");
				
				String date=date_time.substring(0,date_time.indexOf(date_time.valueOf("2015"))+4);
				System.out.println("date is=============================="+date);
				
				String Time= date_time.substring(date_time.indexOf(date_time.valueOf("2015"))+4);
				System.out.println("Time is=============================="+Time);
				
				
				SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
			       SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm:ss a");
			       Date final_time = null;
				try {
					final_time = parseFormat.parse(Time);
					 System.out.println(parseFormat.format(final_time) + " = " + displayFormat.format(final_time));
						
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			      
				
				
				
				if(pending_intent>=1){

					System.out.println("New pending id beforre alarm set ===is==="+pending_intent);

					pending_intent++;
					data.edit().putInt("appointment_id", pending_intent).commit();
					pending_intent = data.getInt("appointment_id", 1);
					System.out.println("New pending id on alarm set ===is==="+pending_intent);
					//========here code for reminder=============

					
							//	Log.d("got times",mHour+":"+mMinutes);
								
								
					
					Intent i = new Intent(BookAppointment.this,Demo_Apointment_alert.class);
					i.putExtra("appoint_time", final_time);
					i.putExtra("appoint_date", date_time);
				//	i.putExtra("appoint Id", et_note_medi.getText().toString());
					i.putExtra("pendindintent_id", pending_intent);
					
					/** Creating a Pending Intent */
					PendingIntent operation2 = PendingIntent.getActivity(getBaseContext(),pending_intent , i, Intent.FLAG_ACTIVITY_NEW_TASK);
					
					/** Getting a reference to the System Service ALARM_SERVICE */
					AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
					Log.d("1", "1");
					/** Setting an alarm, which invokes the operation at alart_time */
			       // alarmManager.set(AlarmManager.RTC_WAKEUP ,cal.getTimeInMillis(),operation2);
					
				
					
//					String alarm_time =displayFormat.format(final_time);
//				long milliseconds = Long.parseLong(alarm_time);
//				
					String month_date =date.substring(0,date.indexOf(date.valueOf("/")));
					int month = Integer.parseInt(month_date);
					  System.out.println("month of date==="+month);
						
					  String pre_day_date = date.substring(date.indexOf(date.valueOf("/"))+1);
					 String day_date= pre_day_date.substring(0,pre_day_date.indexOf(pre_day_date.valueOf("/")));
					  int day = Integer.parseInt(day_date);
					  System.out.println("day of date==="+day);
					 
//					  String year_date = date.substring(date.indexOf(date.valueOf("/"))+2,date.indexOf(date.valueOf("/")));
//					  System.out.println("month of date==="+day_date);
				
					 
					  
				final Calendar cal = Calendar.getInstance();
				
			    cal.setTime(final_time) ;
			    cal.add(cal.MINUTE, -10);
			    
			    
			    if(month==1){
				    cal.set(2015, month, day);
			    	
			    }else{
			    cal.set(2015, month-1, day);
			    }
				
				
			        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
			                operation2);
			       
			        /** Alert is set successfully */
			        Toast.makeText(getBaseContext(), "Appointment Reminder is set successfully on "+ cal.getTime(),Toast.LENGTH_LONG).show();
			        
			        System.out.println("alarm time is==="+cal.getTime());
					
				
					
					
					
					
					
					
					
					
					
					
					
					
				}else{
					System.out.println("pending not changed====="+pending_intent);
					
				}
				showConformationDailog();
			}
			else
			{
				BookMsg = "Appointment is not Confirm, hhgTry agian";
				showConformationDailog();
			}
			
			
			
		
			// etResponse.setText(json.toString());
		}
		}
	
	public static String POST(String url, BookAppointmentBean bookApp) {
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
			
			jsonObject.accumulate("AppointmentID", bookApp.getAppointmentID());
			jsonObject.accumulate("CustomerID", bookApp.getCustomerID());
			jsonObject.accumulate("ScheduleID", bookApp.getScheduleID());
			jsonObject.accumulate("Note", bookApp.getNote());
			jsonObject.accumulate("Attanded", bookApp.getAttanded());
			
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
				BookAppointment.this);
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage(BookMsg).setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					

					public void onClick(DialogInterface dialog,int id) {
						
						Intent sch =new Intent(getApplicationContext(),DeleteAppointment.class);
						 bndlanimation = 
								ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
					
						startActivity(sch,bndlanimation);
						finish();
						
					dialog.cancel();
					
						
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
	
}
