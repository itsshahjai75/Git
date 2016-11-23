package com.iodapp.activities;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TooManyListenersException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class ViewOurPlansFragment extends Fragment {

	private Button btnplaceorder;
	public static String productid;
	public static String local;
	public static String international;
	public static String validite;
	
	TextView etResponse;
	private ProgressBar progressBar;
	
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	List<String> listDataHeader1;
	List<String> listDataHeader2;
	
	HashMap<String, List<String>> listDataChild;
	public static List<String> listItems1 = new ArrayList<String>();
	public static List<String> listItems2 = new ArrayList<String>();
	public static List<String> listItems3 = new ArrayList<String>();
	
	public ViewOurPlansFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_view_our_plans,
				container, false);
		etResponse = (TextView) rootView.findViewById(R.id.etResponse);
		progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		btnplaceorder=(Button)rootView.findViewById(R.id.btnplaceorder);
		
		btnplaceorder.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(Sharedpref.getamount()==null || Sharedpref.getamount().isEmpty() )
				{
					showDialog();
//					Toast.makeText(getActivity(), "Please Select Any One Product", Toast.LENGTH_LONG).show();
					
				}else
				{
					
				Intent it_login = new Intent(getActivity(),
						AdditionalDetails.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getActivity(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_login, bndlanimation);
				getActivity().finish();
				
				}
			}
		});
		
		
		new HttpAsyncTask()
		.execute("http://jsoncdn.webcodeplus.com/ProductData.svc/ProductAttributesValues/30/408/fdg");
		
		new MonthlyHttpAsyncTask()
		.execute("http://jsoncdn.webcodeplus.com/ProductData.svc/ProductAttributesValues/30/510//Monthly-Membership");
		
		new onetime409HttpAsyncTask()
		.execute("http://jsoncdn.webcodeplus.com/ProductData.svc/ProductAttributesValues/30/409/fdg");
		// get the listview
				expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
				
				// preparing list data
				prepareListData();
				
				listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataHeader1,listDataHeader2, listDataChild);

				// setting list adapter
				expListView.setAdapter(listAdapter);
				
				// Listview Group click listener
				expListView.setOnGroupClickListener(new OnGroupClickListener() {

					@Override
					public boolean onGroupClick(ExpandableListView parent, View v,
							int groupPosition, long id) {
//						 Toast.makeText(getActivity(),
//						 "Group Clicked " + listDataHeader.get(groupPosition),
//						 Toast.LENGTH_SHORT).show();
						if(btnplaceorder.getVisibility() == View.VISIBLE)
						{
							btnplaceorder.setVisibility(View.INVISIBLE);
						}
						else if(btnplaceorder.getVisibility() == View.INVISIBLE)
						{
							btnplaceorder.setVisibility(View.VISIBLE);
						}
						return false;
					}
				});

				// Listview Group expanded listener
				expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
					int previousItem = -1;
					
			
					@Override
					public void onGroupExpand(int groupPosition) {
//						Toast.makeText(getActivity(),
//								listDataHeader2.get(groupPosition) + " Expanded",
//								Toast.LENGTH_SHORT).show();
						btnplaceorder.setVisibility(0);
						
//						Sharedpref.setamount(listDataHeader2.get(groupPosition));
						if(listDataHeader1.get(groupPosition) == "Certified US Doctor")
						{
							productid="408";
							local="1";
							international="0";
							validite="7";
							Sharedpref.setamount("49.99");
							
						}else if(listDataHeader1.get(groupPosition) == "International Doctor")
						{

							productid="409";
							international="1";
							local="0";
							validite="7";
							Sharedpref.setamount("14.99");
						}else
						{

							productid="510";
							local="1";
							international="1";
							validite="7";
							Sharedpref.setamount("49.99");
						}
						
						 if(groupPosition != previousItem )
					            expListView.collapseGroup(previousItem );
					        previousItem = groupPosition;
					    
				            
					}
					
					
					
				});

				// Listview Group collasped listener
				expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
					
					
					@Override
					public void onGroupCollapse(int groupPosition) {
//						Toast.makeText(getActivity(),
//								listDataHeader.get(groupPosition) + " Collapsed",
//								Toast.LENGTH_SHORT).show();
					
						
					}
					
				});

				// Listview on child click listener
				expListView.setOnChildClickListener(new OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent, View v,
							int groupPosition, int childPosition, long id) {
						// TODO Auto-generated method stub
//						Toast.makeText(
//								getActivity(),
//								listDataHeader1.get(groupPosition)
//										+ " : "
//										+ listDataChild.get(
//												listDataHeader1.get(groupPosition)).get(
//												childPosition), Toast.LENGTH_SHORT)
//								.show();
						return false;
					}
				});
//		new HttpAsyncTask()
//				.execute("http://jsoncdn.webcodeplus.com/ContentData.svc/ContentDetailsFromContentID/30/823");
		return rootView;
	}

	
	
	
	private void showDialog()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage("Please select your plan...").setCancelable(false)
				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						
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
				
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00bfff"));
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(19.0f);
				
				
				
	}
	/*
	 * Preparing the list data
	 */
	private void prepareListData() {
		new TagnameHttpAsyncTask()
		.execute("http://jsoncdn.webcodeplus.com/ProductData.svc/searchProduct/30/%20and%201=1/1");
		
		listDataHeader = new ArrayList<String>();
		listDataHeader1 = new ArrayList<String>();
		listDataHeader2 = new ArrayList<String>();
	
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add("Monthly Membership");
		listDataHeader.add("One Time Video Call");
		listDataHeader.add("One Time Video Call");
		// Adding child data
		
				listDataHeader1.add("Most Popular");
				listDataHeader1.add("Certified US Doctor");
				listDataHeader1.add("International Doctor");
//				
				listDataHeader2.add("$49.99");//USD create heder title for Plans in three header
				listDataHeader2.add("$49.99");
				listDataHeader2.add("$14.99");
		// Adding child data
				

		// Adding child data
		List<String> MonthlyMembership = new ArrayList<String>();
//		MonthlyMembership.add("The Shawshank Redemption");
//		MonthlyMembership.add("The Godfather");
//		MonthlyMembership.add("The Godfather: Part II");
//		MonthlyMembership.add("Pulp Fiction");
//		MonthlyMembership.add("The Good, the Bad and the Ugly");
//		MonthlyMembership.add("The Dark Knight");
//		MonthlyMembership.add("12 Angry Men");

		List<String> OneTimeVideoCall = new ArrayList<String>();
//		OneTimeVideoCall.add("The Conjuring");
//		OneTimeVideoCall.add("Despicable Me 2");
//		OneTimeVideoCall.add("Turbo");
//		OneTimeVideoCall.add("Grown Ups 2");
//		OneTimeVideoCall.add("Red 2");
//		OneTimeVideoCall.add("The Wolverine");
		OneTimeVideoCall=listItems2;

		List<String> OneTimeVideoCall1 = new ArrayList<String>();
//		OneTimeVideoCall1.add("2 Guns");
//		OneTimeVideoCall1.add("The Smurfs 2");
//		OneTimeVideoCall1.add("The Spectacular Now");
//		OneTimeVideoCall1.add("The Canyons");
//		OneTimeVideoCall1.add("Europa Report");
		listItems1.clear();

		listDataChild.put(listDataHeader1.get(0), listItems1); // Header, Child data
		listDataChild.put(listDataHeader1.get(1), listItems2);
		listDataChild.put(listDataHeader1.get(2), listItems3);
		
//		listDataChild.put(listDataHeader1.get(0), listItems1); // Header, Child data
//		listDataChild.put(listDataHeader1.get(1), listItems2);
//		listDataChild.put(listDataHeader1.get(2), listItems3);
		
		
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

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			return GET(urls[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			listItems2.clear();
			progressBar.setVisibility(View.INVISIBLE);
			try {
				// JSONObject json = new JSONObject(result);
				JSONArray ja = new JSONArray(result);

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					listItems2.add(jo.getString("AttributeValue"));
				}
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
	
	private class MonthlyHttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			return GET(urls[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			listItems1.clear();
			progressBar.setVisibility(View.INVISIBLE);
			try {
				// JSONObject json = new JSONObject(result);
				JSONArray ja = new JSONArray(result);

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					listItems1.add(jo.getString("AttributeValue"));
				}
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
	
	private class onetime409HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			return GET(urls[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			listItems3.clear();
			progressBar.setVisibility(View.INVISIBLE);
			try {
				// JSONObject json = new JSONObject(result);
				JSONArray ja = new JSONArray(result);

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					listItems3.add(jo.getString("AttributeValue"));
				}
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
	
	private class TagnameHttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			return GET(urls[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			progressBar.setVisibility(View.INVISIBLE);
//			Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
			try {
//				listDataHeader.removeAll(listDataHeader);
//				listDataHeader1.removeAll(listDataHeader1);
//				listDataHeader2.removeAll(listDataHeader2);
				// JSONObject json = new JSONObject(result);
				JSONArray ja = new JSONArray(result);

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
//					listDataHeader.add(jo.getString("ShortDescription"));
//					listDataHeader1.add(jo.getString("ProductTags"));
//					listDataHeader2.add(jo.getString("Price"));
//					Toast.makeText(getActivity(), jo.getString("ProductName"), Toast.LENGTH_LONG).show();
				}
				//
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onCancelled() {

			super.onCancelled();
			Toast toast = Toast.makeText(getActivity(),
					"Error is occured due to some probelm", Toast.LENGTH_LONG);
			toast.show();

		}
	}
}
