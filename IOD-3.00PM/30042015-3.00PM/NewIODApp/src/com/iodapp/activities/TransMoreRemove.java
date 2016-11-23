package com.iodapp.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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

import com.iodapp.adapter.S_CustomListAdapter;
import com.iodapp.adapter.TransmoreAdapter;
import com.iodapp.adapter.TransmoreAdapter2;
import com.iodapp.model.Movie;
import com.iodapp.model.TransMoreData;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;

public class TransMoreRemove extends Activity {
	private  final String geturl = "http://jsoncdn.webcodeplus.com/OrderData.svc/GetTransarmorDetails/"+Sharedpref.getcustomerid();
	private final String delURL = "http://jsoncdn.webcodeplus.com/OrderData.svc/RemoveCard";
	private ProgressDialog pDialog;
	private List<TransMoreData> movieList = new ArrayList<TransMoreData>();
	private ListView listView;
	
	private CheckBox check;
	private Button payment;
	protected String order_status_ID;
	protected String trnsMoreNo;
	private TransmoreAdapter2 adapter;
	private String msg;
	private ImageButton back_btn;
	private TextView title,btn_nocard,tv_nocardfound;
	private Typeface sysFont;
	protected static String transMoreID;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
		setContentView(R.layout.activity_trans_more_remove);
		
		title = (TextView) findViewById(R.id.Trans_title);
		
		back_btn = (ImageButton) findViewById(R.id.method_back_btn);
		listView = (ListView) findViewById(R.id.transListDelete);
		adapter = new TransmoreAdapter2(TransMoreRemove.this, movieList); 
		
		setSystemFont();
		listView.setAdapter(adapter);
		
		pDialog = new ProgressDialog(TransMoreRemove.this);
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
				transMoreID = tdm.getTransArmerID();
				showDialog();
			}
		});
		
back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
btn_nocard=(TextView)this.findViewById(R.id.btn_cardpage_ok);
tv_nocardfound=(TextView)this.findViewById(R.id.tv_nocardfound);

btn_nocard.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		Intent j = new Intent(TransMoreRemove.this,MyAccount.class);
		startActivity(j);
		finish();
		
		
	}
});
		
		
	}
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
		
		
		title.setTypeface(sysFont);

		
	}
	
	private void showDialog()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				TransMoreRemove.this);
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage("Remove This Card?").setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						
						new DeleteTransmoreAsyncTaskpost().execute(delURL);
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
		JsonArrayRequest movieReq = new JsonArrayRequest(geturl,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						
						hidePDialog();
						
						// Parsing json
						movieList.clear();
						
						if(response.length()==0){
							
							
							
							tv_nocardfound.setVisibility(View.VISIBLE);
							btn_nocard.setVisibility(View.VISIBLE);
							tv_nocardfound.setText("Sorry!\nNo Card Saved!!!.");
								
							btn_nocard.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									
									Intent nocardfound = new Intent(TransMoreRemove.this,MyAccount.class);
									startActivity(nocardfound);
									finish();
									
									
								}
							});
								
				
					}else{
						
						
						
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
	
	
	
	 private class DeleteTransmoreAsyncTaskpost extends AsyncTask<String, Void, String> {

		 @Override
		 protected String doInBackground(String... urls) {

			return POST(urls[0]);
		 }
		 @Override
		 protected void onPreExecute() {
		 // TODO Auto-generated method stub
//		 pDialog.show();

		 super.onPreExecute();
		 }

		 @Override
		 protected void onPostExecute(String result) {
		 //	

		 if(Integer.parseInt(result) > 0)
		 {

			 msg = "Card Remove successfully";
			 showDialogConfirm();
		//	 Toast.makeText(TransMoreRemove.this, "Your Transmore detalis Delete", Toast.LENGTH_SHORT).show();
		
		 }
		 else
		 {
		
			 msg = "Error occure when Your Payment Method Remove. Please try after some time";
			 showDialogConfirm();
		 Toast.makeText(TransMoreRemove.this, "Error Occure When Data Upadate", Toast.LENGTH_SHORT).show();
		 }

		 // etResponse.setText(json.toString());
		 }

		 @Override
		 protected void onCancelled() {
		 // TODO Auto-generated method stub
		
		 super.onCancelled();
		 }

		 }

		 public static String POST(String urls) {
		 InputStream inputStream = null;
		 String result = "";
		 try {

		 // 1. create HttpClient
		 HttpClient httpclient = new DefaultHttpClient();
		 // 2. make POST request to the given URL
		 HttpPost httpPost = new HttpPost(urls);
		 // String json = "";

		 // 3. build jsonObject
		 JSONObject jsonObject = new JSONObject();

		 jsonObject.accumulate("TransArmerID",transMoreID);
		 


		 Log.d("Post JsonObject", jsonObject.toString());

		
		 StringEntity se = new StringEntity(jsonObject.toString());

		
		 httpPost.setEntity(se);

		
		 httpPost.setHeader(HTTP.CONTENT_TYPE,"application/json; charset=utf-8");
		
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
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}
	
	private void showDialogConfirm()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				TransMoreRemove.this);
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage(msg).setCancelable(false)
				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						//onBackPressed();
						dialog.cancel();
				Intent this_page = new Intent(TransMoreRemove.this,TransMoreRemove.class);
				startActivity(this_page);
						
						
						finish();
						
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
				
	}
}