package com.iodapp.activities;

import java.io.BufferedReader;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;





import com.iodapp.adapter.S_CustomListAdapter;

import com.iodapp.model.Movie;
import com.iodapp.util.URLSuppoter;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;




public class S_International_fragment extends Fragment {

	// Log tag
	private static final String TAG = ScheduleAppintment.class.getSimpleName();

	// Movies json url
	private static final String url = "http://jsoncdn.webcodeplus.com/VideoStreamData.svc/CheckAppointmentAvailability/0/"+Sharedpref.getcustomerid();
	private String Credit_url  = "http://jsoncdn.webcodeplus.com/CkeckCreditData.svc/GetCreditAvailable/"+Sharedpref.getcustomerid()+"/0";
	private final String getApplist = "http://jsoncdn.webcodeplus.com/AppointmentData.svc/GetAppointmentsList/"+Sharedpref.getcustomerid();
	private ProgressDialog pDialog;
	private List<Movie> movieList = new ArrayList<Movie>();
	private List<Integer> b_d_ID = new ArrayList<Integer>();
	private ListView listView;
	private S_CustomListAdapter adapter;
	private String img_path = "http://cms.ionlinedoctor.com/AdminData/";
	
	TextView UserName;
	private String msg,msgTag = "";
	private String dr_name,dr_id="";
	private String imgUrl="";
	private String dr_rate="0";
	private String Q_no="";
	public PersonDetail person;
	private int l_cradit,i_cradit;

	Timer timer = new Timer();

	private Typeface sysFont;

	private TextView name;

	private TextView avilibity;

	protected int LocalAppcount,IntAppCount=0;

	protected String dialogMSG;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_s__international_fragment, container, false);
		
//		UserName=(TextView)rootView.findViewById(R.id.txtfullname);
		listView = (ListView) rootView.findViewById(R.id.list);
		name = (TextView) rootView.findViewById(R.id.title);
		avilibity = (TextView) rootView.findViewById(R.id.rating);
		adapter = new S_CustomListAdapter(getActivity(), movieList);
		listView.setAdapter(adapter);
		
		pDialog = new ProgressDialog(getActivity());
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();

		
		
		
		
		//UserName.setText(Sharedpref.getfirstname()+" "+Sharedpref.getlastname());
		// changing action bar color
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				Log.i("testing", "repeat testing");// display the data
//				i = i + 1;
				getDoctorsList();
				new getCredit().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Credit_url);
				getBookedSchedualList();
			}
		}, 0, 10000); // Creating volley request obj
		// getDoctorsList();
		
listView.setOnItemClickListener(new OnItemClickListener() {

			

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				int book_count=0;
				
				Movie m1 = (Movie) movieList.get(position);
				dr_name = m1.getTitle();
				dr_id = m1.getDoctore_id();
				imgUrl = m1.getThumbnailUrl();
				dr_rate = m1.getRating();
				Q_no = m1.getQueue_no();
				
				
				
//				for(int i=0;i< b_d_ID.size();i++)
//				{
//					if(Integer.parseInt(dr_id) == b_d_ID.get(i))
//					{
//						book_count++;
//					}
//				}

				
				if(i_cradit > 0)
				{
					if(i_cradit > IntAppCount)
					{
						
//						if(i_cradit == IntAppCount+1)
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
					}
					else
					{
						dialogMSG = "You already have booked an Appointment with the credit you have. Please purchase more Credit.";
						showDialog();
					}
					
					
				}
				else
				{
					dialogMSG = "You do not have Enough Credit To Book an Appointment. Please purchase more Credit.";
					showDialog();
				}
				
				
				
				
			}
		});

//		setSystemFont();
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
				
				
				Intent plan = new Intent(getActivity(), ParentForFragment.class);
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
 
				// show it
				alertDialog.show();
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#ffffff"));
				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(Color.parseColor("#ffffff"));
				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00bfff"));
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00bfff"));
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(19.0f);
				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(19.0f);
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
	}
	public void getDoctorsList() {
		JsonArrayRequest movieReq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());
						hidePDialog();
						String str = "";
						// Parsing json
						movieList.clear();
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
								Movie movie = new Movie();
								movie.setTitle(obj.getString("FirstName") + " "
										+ obj.getString("LastName"));
								movie.setThumbnailUrl(img_path+obj.getString("SmallImage"));
								
								if (obj.getString("Available")
										.equalsIgnoreCase("y")) {
//									str = "Available";
								} else {
//									str = "Busy";
								}
//								movie.setAvailable(str);
								movie.setDoctore_id(obj.getString("DoctorID"));
								movie.setYear(obj.getString("DoctorID"));
								movie.setSpeciality(obj.getString("Specialization"));
								movie.setExp(obj.getString("Experience"));
								movie.setRating(obj.getString("AvgRate"));
								movie.setQueue_no(obj.getString("QueNumber"));
								//
								// // Genre is json array
								// JSONArray genreArry =
								// obj.getJSONArray("genre");
								// ArrayList<String> genre = new
								// ArrayList<String>();
								// for (int j = 0; j < genreArry.length(); j++)
								// {
								// genre.add((String) genreArry.get(j));
								// }
								// movie.setGenre(genre);

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
								
								sid = obj.getString("StateID").toString();
								if(Integer.parseInt(sid) > 0)
								{
									LocalAppcount++;
								}
								else
								{
									b_d_ID.add(Integer.parseInt(obj.getString("DoctorId").toString()));
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
