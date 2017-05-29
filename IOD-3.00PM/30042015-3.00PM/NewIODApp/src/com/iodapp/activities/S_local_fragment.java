package com.iodapp.activities;


import com.iodapp.adapter.S_CustomListAdapter;

import com.iodapp.model.Movie;
import com.iodapp.util.URLSuppoter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.R.integer;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Typeface;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;


import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;


public class S_local_fragment extends Fragment {

	// Log tag
	private static final String TAG = ScheduleAppintment.class
			.getSimpleName();

	// Movies json url
//	private static final String url = "http://jsoncdn.webcodeplus.com/TokenData.svc/GetAvailableDoctorList/"+Sharedpref.getstatecode()+"/"+Sharedpref.getcustomerid();
	private static final String url = "http://jsoncdn.webcodeplus.com/VideoStreamData.svc/CheckAppointmentAvailability/"+Sharedpref.getstatecode()+"/"+Sharedpref.getcustomerid();
	private final String getApplist = "http://jsoncdn.webcodeplus.com/AppointmentData.svc/GetAppointmentsList/"+Sharedpref.getcustomerid();
	private ProgressDialog pDialog;
	private List<Movie> movieList = new ArrayList<Movie>();
	private List<Integer> b_d_ID = new ArrayList<Integer>();
	
	
	private ListView listView;
	private S_CustomListAdapter adapter;
	Timer timer = new Timer();
	TextView UserName;
	int i = 0;
	private String img_path = "http://cms.ionlinedoctor.com/AdminData/";
	private String msg,msgTag = "";
	private String dr_name,dr_id="";
	private String dr_rate="0";
	private String imgUrl="";
	private String Q_no="";

	public PersonDetail person;

	private Typeface sysFont;

	private TextView name;

	private TextView avilibity;
	private String Credit_url  = "http://jsoncdn.webcodeplus.com/CkeckCreditData.svc/GetCreditAvailable/"+Sharedpref.getcustomerid()+"/0";
	private int l_cradit,i_cradit;

	protected int LocalAppcount,IntAppCount=0;

	protected String dialogMSG;

	private TextView speci;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_s_local_fragment, container,
				false);
		UserName = (TextView) rootView.findViewById(R.id.txtfullname);
		listView = (ListView) rootView.findViewById(R.id.list);
		adapter = new S_CustomListAdapter(getActivity(), movieList);
		name = (TextView) rootView.findViewById(R.id.title);
		
		
		speci = (TextView) rootView.findViewById(R.id.specializationtext);
		
		avilibity = (TextView) rootView.findViewById(R.id.rating);
//		setSystemFont();
		listView.setAdapter(adapter);

		pDialog = new ProgressDialog(getActivity());
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();

		UserName.setText(Sharedpref.getfirstname() + " "
				+ Sharedpref.getlastname());
		// changing action bar color
		
		
		
		
		
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				Log.i("testing", "repeat testing");// display the data
				i = i + 1;
				getDoctorsList();
				new getCredit().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Credit_url);
				getBookedSchedualList();
			}
		}, 0, 5000); //Available doctore list refresh ecery 10 second
		
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			

			

			

			private String sp_lization;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				int book_count=0;
				
				Movie m1 = (Movie) movieList.get(position);
				dr_name = m1.getTitle();
				dr_id = m1.getDoctore_id();
				imgUrl = m1.getThumbnailUrl();
				Q_no = m1.getQueue_no();
				sp_lization  = m1.getSpeciality();
				
				if(m1.getRating() != null)
				{
					dr_rate = m1.getRating();
				}
				
				
//				for(int i=0;i< b_d_ID.size();i++)
//				{
//					if(Integer.parseInt(dr_id) == b_d_ID.get(i))
//					{
//						book_count++;
//					}
//				}
			
				
				if(l_cradit > 0)
				{
					if(l_cradit > LocalAppcount)
					{
//						if(l_cradit == LocalAppcount+1)
//						{
//							if(book_count > 0)
//							{
//								dialogMSG = "You alredy have booked an appointment for this Doctor.";
//								showDialog();
//							}
//							else
//							{
								Intent bookNow = new Intent(getActivity(), BookAppointment.class);
								Bundle bndlanimation = 
										ActivityOptions.makeCustomAnimation(getActivity(), R.anim.animation,R.anim.animation2).toBundle();
							
							
								bookNow.putExtra("drId", dr_id);
							
							
								getActivity().startActivity(bookNow, bndlanimation);
//							}
//						}
						
					}
					else
					{
						dialogMSG = "You already have booked an Appointment with the credit you have. Please purchase more credit.";
						showDialog();
					}
					
					
				}
				else
				{
					dialogMSG = "You do not have Enough Credit To Book an Appointment. Please purchase more credit.";
					showDialog();
				}
				
				
				
				
			}
		});
		
	
		
		return rootView;
	}

	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getActivity().getAssets(),"font/Leelawadee.ttf");
		name.setTypeface(sysFont);
		avilibity.setTypeface(sysFont);
		
		
		
	}
	
	private void showDialog()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage(dialogMSG).setCancelable(false)
				.setPositiveButton("Buy Credit",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						
						Intent plan = new Intent(getActivity(), PricingPlan.class);
						plan.putExtra("Fragment_Code", R.id.list);
						
						startActivity(plan);
						
						
						dialog.cancel();
						
						
						
					
						
					}
				  }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
	
	public void getDoctorsList() {
		JsonArrayRequest movieReq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());
						hidePDialog();
						String str = "";
						Bitmap mBitmap = null;
						// Parsing json
						movieList.clear();
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
								Movie movie = new Movie();
								movie.setTitle(obj.getString("FirstName") + " "
										+ obj.getString("LastName"));
								movie.setThumbnailUrl(img_path+obj.getString("SmallImage"));
								movie.setSpeciality(obj.getString("Specialization"));
								if (obj.getString("Available")
										.equalsIgnoreCase("y")) {
//									str = "Available";
								} else {
//									str = "Busy";
								}
								movie.setAvailable(str);
								movie.setDoctore_id(obj.getString("DoctorID"));
								movie.setYear(obj.getString("DoctorID"));
								movie.setSpeciality(obj.getString("Specialization"));
								movie.setExp(obj.getString("Experience"));
								movie.setRating(obj.getString("AvgRate"));
								movie.setQueue_no(obj.getString("QueNumber"));
								
//								
								// adding movie to movies array
								movieList.add(movie);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						// notifying list adapter about data changes
						// so that it renders the list view with updated data
						adapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						hidePDialog();

					}
				});

		// Adding request to request queue
		Sharedpref.getInstance().addToRequestQueue(movieReq);

	}
	
	private class getCredit extends AsyncTask<String, Void, String> {
		private URLSuppoter suppoter;
		
		
		
		
		@Override
		protected String doInBackground(String... urls) {

			suppoter = new URLSuppoter();
			return suppoter.GET(urls[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// progressBar.setVisibility(View.VISIBLE);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			// progressBar.setVisibility(View.INVISIBLE);
			try {
				// result = jsonstr;
				
//				JSONArray ja = new JSONArray(result);
				JSONObject ja = new JSONObject(result);
				
				l_cradit = Integer.parseInt(ja.getString("RemianLoacalCredit"));
				i_cradit = Integer.parseInt(ja.getString("RemainInternationalCredit"));
				
				
				hidePDialog();
				
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			       
		}

		@Override
		protected void onCancelled() {

			hidePDialog();
			super.onCancelled();
//			Toast toast = Toast.makeText(DashboardActivity.this,
//					"Error is occured due to some probelm", Toast.LENGTH_LONG);
//			toast.show();

		}
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
	
	public void getBookedSchedualList() {
		JsonArrayRequest movieReq = new JsonArrayRequest(getApplist,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d("BookNow", response.toString());
						hidePDialog();
						LocalAppcount=0;
						IntAppCount=0;
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
								String sid="";
//								Integer.parseInt(obj.getString("DoctorId").toString());
								
								
								
								sid = obj.getString("StateID").toString();
								if(Integer.parseInt(sid) > 0)
								{
									b_d_ID.add(Integer.parseInt(obj.getString("DoctorId").toString()));
									LocalAppcount++;
								}
								else
								{
									IntAppCount++;
								}
								
								
								
								
							} catch (JSONException e) {
								e.printStackTrace();
							}
					}
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


}

