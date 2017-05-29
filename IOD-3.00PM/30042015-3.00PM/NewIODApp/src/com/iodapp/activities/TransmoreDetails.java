package com.iodapp.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.iodapp.adapter.S_CustomListAdapter;
import com.iodapp.adapter.TransmoreAdapter;
import com.iodapp.model.Movie;
import com.iodapp.model.TransMoreData;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TransmoreDetails extends Activity {
	private static final String url = "http://jsoncdn.webcodeplus.com/OrderData.svc/GetTransarmorDetails/"+Sharedpref.getcustomerid();
	private ProgressDialog pDialog;
	private List<TransMoreData> movieList = new ArrayList<TransMoreData>();
	private ListView listView;
	private TransmoreAdapter adapter;

	private Button payment;
	protected String order_status_ID;
	protected String trnsMoreNo,cc_holdername,cc_expmonth,cc_expyear,cc_cmptype;
	LinearLayout transmore;
	SharedPreferences data;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask 
		setContentView(R.layout.activity_transmore_details);
	
		View titleView = getWindow().findViewById(android.R.id.title);
		    if (titleView != null) {
		      ViewParent parent = titleView.getParent();
		      if (parent != null && (parent instanceof View)) {
		        View parentView = (View)parent;
		        parentView.setBackgroundColor(Color.parseColor("#ffffff"));
		      }
		    }
		    
		    data =  PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		    ImageButton A_back = (ImageButton)this.findViewById(R.id.btn_back);
			A_back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				// TODO Auto-generated method stub
					onBackPressed();
				finish();
				}
				});
		

			
			transmore = (LinearLayout)this.findViewById(R.id.transmore_main);			
					
					
					
		listView = (ListView) findViewById(R.id.transList);
		adapter = new TransmoreAdapter(TransmoreDetails.this, movieList); 

		payment = (Button) findViewById(R.id.Pay);
		
		
		listView.setAdapter(adapter);
		
		pDialog = new ProgressDialog(TransmoreDetails.this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();
		getTransmoreList();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				TransMoreData tdm = (TransMoreData) movieList.get(position);
				
				order_status_ID = "19";
				trnsMoreNo = tdm.getTransArmorToken();
				
				cc_holdername=tdm.getCardHolderName();
				cc_expmonth=tdm.getCCExpiryMonth();	
				cc_expyear=tdm.getCCExpiryYear();
				cc_cmptype=tdm.getCCCompanyName();
				//Sharedpref.setcc_number(tdm.get)
				showDialog();
			}
		});
		
		

		
		payment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it_login = new Intent(TransmoreDetails.this,
						SamplePaymentgetway.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						TransmoreDetails.this, R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_login, bndlanimation);
				finish();
			}
		});
		
		
	}
	
	private void showDialog()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				TransmoreDetails.this);
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage("Confirm Payment ?").setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						
						
						
						
						 data.edit().putString("CardHolderName",cc_holdername ).commit();
					        data.edit().putString("CCExpiryMonth", cc_expmonth).commit();
					        data.edit().putString("CCExpiryYear", cc_expyear).commit();
					         data.edit().putString("CCCompanyName",cc_cmptype ).commit();
							
						
						
						
						
						Intent it_payment = new Intent(getApplicationContext(),
								OrderPlaceActivity.class);
								Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
								getApplicationContext(), R.anim.animation,
								R.anim.animation2).toBundle();

								it_payment.putExtra("Ord_st_id", order_status_ID);
								it_payment.putExtra("trns_no", trnsMoreNo);

								Log.d("order_id----transno",order_status_ID+"----"+transmore);

								
								startActivity(it_payment, bndlanimation);
								finish();

						dialog.cancel();
					
						
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
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.transmore_details, menu);
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
	
	public void getTransmoreList() {
		JsonArrayRequest movieReq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						
						hidePDialog();
						
						// Parsing json
						movieList.clear();
						
						if(response.length()<=0){
							
							Intent new_card = new Intent (TransmoreDetails.this,SamplePaymentgetway.class);
							startActivity(new_card);
							finish();
						}
						
						else {
							
						
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
								TransMoreData tmd = new TransMoreData();
								
								tmd.setCardHolderName(obj.getString("CardHolderName"));
								tmd.setCCCompanyName(obj.getString("CCCompanyName"));
								tmd.setCCExpiryMonth(obj.getString("CCExpiryMonth"));
								tmd.setCCExpiryYear(obj.getString("CCExpiryYear"));
								tmd.setTransArmerID(obj.getString("TransArmerID"));
								tmd.setTransArmorToken(obj.getString("TransArmorToken"));
								
								
//								
								// adding movie to movies array
								movieList.add(tmd);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
						}

						// notifying list adapter about data changes
						// so that it renders the list view with updated data
						adapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						
						hidePDialog();

					}
				});

		// Adding request to request queue
		Sharedpref.getInstance().addToRequestQueue(movieReq);

	}
	
	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}
}
