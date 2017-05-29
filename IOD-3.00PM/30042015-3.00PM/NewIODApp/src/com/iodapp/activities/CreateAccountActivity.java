package com.iodapp.activities;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.iodapp.util.URLSuppoter;

import android.R.integer;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.LauncherActivity.ListItem;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccountActivity extends Activity implements
		OnItemSelectedListener {

	EditText etfname;
	EditText etlname;
	EditText etEmail;
	EditText etPass;
	EditText etRepass;
	EditText etMobileno;

	RelativeLayout btncreateaccount;
	Spinner spinner;

	TextView tvIsConnected;
	TextView etResponse;
	PersonDetail person;
	InputStream is = null;

	String email; 
	 public static String mobileno;
	 String pass;
	 String fname;
	 String lname;
	
	 String status;
	 
	LinearLayout reg_liner;
	LinearLayout forget_liner;
	private ImageButton btn_back;
	private TextView registered_user;
	private Button btn_skip;
	private Typeface sysFont;
	private TextView singup_text;
	private TextView description_text;
	private ArrayAdapter<String> dataAdapter;
	private HashMap<String, Integer> stateMap = new HashMap<String, Integer>();
	private TextView btncreatetext;
	
	static List<String> listItems = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
		setContentView(R.layout.create_account_activity);

		reg_liner=(LinearLayout)findViewById(R.id.linearLayout1);
		forget_liner=(LinearLayout)findViewById(R.id.linerforgetpass);
		
		btncreateaccount = (RelativeLayout) findViewById(R.id.btncreateacc);
		btncreatetext = (TextView) findViewById(R.id.btncreatetext);
		
		
		
		
		tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
//		etResponse = (TextView) findViewById(R.id.textView1);
		etfname = (EditText) findViewById(R.id.editfname);
		etlname = (EditText) findViewById(R.id.editlname);

		etEmail = (EditText) findViewById(R.id.editemail);
		etPass = (EditText) findViewById(R.id.editpass);
		etRepass = (EditText) findViewById(R.id.editrepass);
		etMobileno = (EditText) findViewById(R.id.editphone);
		btn_back = (ImageButton) findViewById(R.id.btnback);
		registered_user = (TextView) findViewById(R.id.Allready_reg);
		btn_skip = (Button) findViewById(R.id.btnSkip);
		singup_text = (TextView) findViewById(R.id.textView1);
		description_text = (TextView) findViewById(R.id.textview21);
		
		
		setSystemFont();
		
		

		spinner = (Spinner) findViewById(R.id.spinnerstate);

		// check if you are connected or not
		if (isConnected()) {
			tvIsConnected.setBackgroundColor(0xFF00CC00);
			tvIsConnected.setText("You are conncted");
		} else {
			tvIsConnected.setText("You are NOT conncted");
		}
		
		registered_user.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it_LogIn = new Intent(getApplicationContext(),
						LoginActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_LogIn, bndlanimation);
				finish();
			}
		});
		
		btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it_LogIn = new Intent(getApplicationContext(),
						LoginActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_LogIn, bndlanimation);
				finish();
				
			}
		});
		
		btn_skip.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Sharedpref.setlogoutStatus(false);
				Intent it_skip = new Intent(getApplicationContext(),
						ContentActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_skip, bndlanimation);
			}
		});
		

		btncreateaccount.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				
				 String email = etEmail.getText().toString();
				 //String mobileno = etMobileno.getText().toString();
				 String pass = etPass.getText().toString();
				 String fname = etfname.getText().toString();
				 String lname = etlname.getText().toString();
				 //String repass = etRepass.getText().toString();
				 String status = spinner.getSelectedItem().toString();
				 
				 
				 if (!isValidPassword(pass)) {
						etPass.setError("Invalid Password");
						etPass.requestFocus();
					}
					else
					{
						etPass.setError(null);
					}
				 
				 if(spinner.getSelectedItem().equals("Select State"))
					{
						Toast.makeText(CreateAccountActivity.this,"Select Your State",Toast.LENGTH_SHORT).show();
						((TextView)spinner.getChildAt(0)).setError("Select yourState");
						spinner.requestFocus();
						
					}

				 
				if (!isValidEmail(email)) {
					etEmail.setError("Invalid Email");
					etEmail.requestFocus();
				}
				else
				{
					etEmail.setError(null);
				}

				if (!isValidName(lname)) {
					etlname.setError("Invalid Last Name");
					etlname.requestFocus();
				}
				else
				{
					etlname.setError(null);
				}
				
				if (!isValidName(fname)) {
					etfname.setError("Invalid First Name");
					etfname.requestFocus();
				}
				else
				{
					etfname.setError(null);
				}

				
			

//				if (!isValidPassword(repass)) {
//					etRepass.setError("Invalid Re-Password");
//				}

				// if (!validate()) {
				// Toast.makeText(getBaseContext(), "Enter some data!",
				// Toast.LENGTH_LONG).show();
				// call AsynTask to perform network operation on separate
				// thread

				if (isValidEmail(email) && isValidPassword(pass)
						&& isValidName(fname) && !spinner.getSelectedItem().equals("Select State")) {
//					if (pass.compareTo(repass) == 0) {

						new HttpAsyncTask()
								.execute("http://jsoncdn.webcodeplus.com/CustomerData.svc/CreateCustomer");
						//

						// Toast.makeText(getApplicationContext(),
						// "Successful", Toast.LENGTH_LONG).show();
//
//					} else {
//						etPass.setText("");
////						etRepass.setText("");
//
//						Toast.makeText(getApplicationContext(),
//								"Re-password Wrong", Toast.LENGTH_LONG).show();
//
//					}

				}
			}
		});

		// Spinner click listener
		spinner.setOnItemSelectedListener(this);
		
		
	
		listItems.add("Select State");
		
		new StateHttpAsyncTask()
		.execute("http://jsoncdn.webcodeplus.com/StateData.svc/StateList/223");
		
		
		// categories=listItems;
		// Creating adapter for spinner
//		 dataAdapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_spinner_dropdown_item, listItems);
		 
		 dataAdapter = new ArrayAdapter<String>(this,
					R.layout.spinner_item, listItems);
		 
		// dataAdapter.addAll(listItems);
		dataAdapter.notifyDataSetChanged();
		// Drop down layout style - list view with radio button
//		dataAdapter
//				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// dataAdapter.addAll(listItems);
		// dataAdapter.notifyDataSetChanged();
		// attaching data adapter to spinner

		spinner.setAdapter(dataAdapter);
		
//		spinner.setSelection(1);
		
		
		
	

		// spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// public void onItemSelected(AdapterView<?> av, View v,
		// int position, long itemId) {
		// // TODO Auto-generated method stub
		// String item=av.getItemAtPosition(position).toString();
		// Toast.makeText(getApplicationContext(),"Selected Item is "+item,Toast.LENGTH_LONG).show();
		// }
		//
		// public void onNothingSelected(AdapterView<?> av) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		
	}

	// validating email id
	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
		
		btncreatetext.setTypeface(sysFont);
		
		
		etfname.setTypeface(sysFont);
		etlname.setTypeface(sysFont);

		etEmail.setTypeface(sysFont);
		etPass.setTypeface(sysFont);
		
		
		
		registered_user.setTypeface(sysFont);
		btn_skip.setTypeface(sysFont);
		singup_text.setTypeface(sysFont);
		description_text.setTypeface(sysFont);
		
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent it_LogIn = new Intent(getApplicationContext(),
				LoginActivity.class);
		Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
				getApplicationContext(), R.anim.animation,
				R.anim.animation2).toBundle();
		startActivity(it_LogIn, bndlanimation);
		finish();
		super.onBackPressed();
	}

	// validating password with retype password
	private boolean isValidPassword(String pass) {
		if (pass != null && pass.length() > 0) {
			return true;
		}
		return false;
	}

	// validating password with retype password
	private boolean isValidName(String name) {
		if (name != null && name.length() > 0) {
			return true;
		}
		return false;
	}
	
	

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// On selecting a spinner item
		String item = parent.getItemAtPosition(position).toString();
		// TextView tv = (TextView)view;
		// String selection = tv.getText().toString();

		// etResponse.setText(selection);
		// Showing selected spinner item
		((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView) parent.getChildAt(0)).setTextSize(20);
        
        ((TextView) parent.getChildAt(0)).setTypeface(sysFont);
//		Toast.makeText(parent.getContext(), "Selected: " + item,
//				Toast.LENGTH_LONG).show();
	}

	//
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
//		Toast.makeText(arg0.getContext(), "Not Selected Item: ",
//				Toast.LENGTH_LONG).show();
	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

//	

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		
		URLSuppoter suppoter = new URLSuppoter();
		@Override
		protected String doInBackground(String... urls) {

			person = new PersonDetail();

			person.setSystemUserID("30");
			person.setCustomerID("0");
			person.setFirstName(etfname.getText().toString());

			person.setLastName(etlname.getText().toString());
			person.setMiddleInitial("");
			person.setPhoneNo("");

//			person.setMobileNo(etMobileno.getText().toString());
			person.setMobileNo("");
			person.setFaxNo("");
			person.setEmailID(etEmail.getText().toString());

			person.setAddress1("");
			person.setAddress2("");
			person.setStreetName("");

			String State_Name = (String) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
			
			int state_id = stateMap.get(State_Name);
			
			person.setCity("");
//			person.setState(String.valueOf(spinner.getSelectedItemId()));
			person.setState(String.valueOf(state_id));
			person.setCountry("");

			person.setZipcode("");
			person.setBirthDate("");
			person.setGender("");

			person.setCompanyName("");
			person.setDesignation("");
			person.setCustomerType("47");

			person.setPrefferedTimeToContact("");
			person.setHeardAboutUs("");
			person.setDailyAlert("");

			person.setNewsLetter("");
			person.setUsername(etEmail.getText().toString());
			person.setPassword(etPass.getText().toString());

			person.setExtraInfo("");
			person.setComments("");
			person.setIPAddress("");
			

			// person.setCountry(etEmail.getText().toString());
			// person.setTwitter(etPass.getText().toString());

			return suppoter.POST(urls[0], person);
//			return POST(urls[0], person);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
//			etResponse.setText(result);
			// Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG)
			// .show();
//			Spanned sp = Html.fromHtml(result);
//			String unm=String.valueOf(sp);
			
			if (result.equalsIgnoreCase("0")) 
			{
				Toast.makeText(getApplicationContext(), "Mail Already Exit Requested mail alredy exit", Toast.LENGTH_LONG).show();
				forget_liner.setVisibility(View.VISIBLE);
				reg_liner.setVisibility(View.GONE);
			} else {
				
				
				
				Toast.makeText(getApplicationContext(), "Redirect to Dashboard", Toast.LENGTH_LONG).show();
				Sharedpref.setregcheck(true);
				Intent it_createacc = new Intent(getApplicationContext(),
						DashboardActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_createacc, bndlanimation);
				finish();
				
			
			}
		}
	}

	private boolean validate() {
		if (etfname.getText().toString().trim().equals(""))
			return false;
		else if (etEmail.getText().toString().trim().equals(""))
			return false;
		else if (etPass.getText().toString().trim().equals(""))
			return false;
		else
			return true;
	}

//	

	private class StateHttpAsyncTask extends AsyncTask<String, Void, String> {
		
		URLSuppoter suppoter = new URLSuppoter();
		@Override
		protected String doInBackground(String... urls) {

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
				// JSONObject json = new JSONObject(result);
				dataAdapter.notifyDataSetChanged();
				JSONArray ja = new JSONArray(result);

				
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					listItems.add(jo.getString("StateName"));
					stateMap.put(jo.getString("StateName").toString(), Integer.parseInt(jo.getString("StateID")));
					
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
//			Toast toast = Toast.makeText(getApplicationContext(),
//					"Error is occured due to some probelm", Toast.LENGTH_LONG);
//			toast.show();

		}
	}

}
