package com.iodapp.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iodapp.model.BookAppointmentBean;
import com.iodapp.util.URLSuppoter;


import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.text.format.Time;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdditionalDetails extends FragmentActivity implements OnItemSelectedListener{
	
	EditText mEdit;
	
	private String res_get_detail;
	
	 ArrayAdapter<String> adpter_phonetype,adpter_gender;
	 private String[] gen = { "Select Gender","Male","Female"};
	 
	 private String[] p_type = { "Select PhoneType","Home","Work","Cell","Other"};
	 
	 private String url = "http://jsoncdn.webcodeplus.com/CustomerData.svc/UpdateCustomerWithExtraDetails";
	 					   


	private Typeface sysFont;


	private EditText address;


	private EditText city;


	private EditText zipcode;


	private EditText phoneno;


	private TextView text;


	private RelativeLayout submit;


	private TextView birthday;


	private Spinner phonetype;


	private ImageButton back_additional;


	private Spinner gender;


	private ProgressDialog pDialog;


	private TextView textsubmit;
	TextView btn_skip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    	requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
        
        
        setContentView(R.layout.activity_additional_details);
        
        
        if (android.os.Build.VERSION.SDK_INT > 14) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			} 
        
        
        System.out.println(gen.length);
        sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
		
         address = (EditText)findViewById(R.id.editaddress);
         city = (EditText) findViewById(R.id.editcity);
         zipcode = (EditText)findViewById(R.id.editZipcode);
         phoneno = (EditText) findViewById(R.id.editphoneno);
         text = (TextView) findViewById(R.id.text);
         birthday = (TextView)findViewById(R.id.editbirthdate);
         
         submit = (RelativeLayout)findViewById(R.id.asubmit);
         textsubmit = (TextView) findViewById(R.id.textsubmit);
      btn_skip= (TextView)this.findViewById(R.id.btnSkip);
    
         
       
         btn_skip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
		Intent a = new Intent(AdditionalDetails.this,TransmoreDetails.class);
		startActivity(a);
			
				
				
				
			}
		});
         
         
         
         
   
         pDialog = new ProgressDialog(this);
 		// Showing progress dialog before making http request
 		pDialog.setMessage("Loading...");
 		
        
         
         phonetype = (Spinner) findViewById(R.id.photype);
         back_additional  = (ImageButton) findViewById(R.id.btnback_Additional);
       
          adpter_phonetype = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, p_type);
         
        phonetype.setAdapter(adpter_phonetype);
        phonetype.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String item = parent.getItemAtPosition(position).toString();
				
				((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
		        ((TextView) parent.getChildAt(0)).setTextSize(18);
		        ((TextView) parent.getChildAt(0)).setTypeface(sysFont);


				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		
        
        });
        
        
        setsystemfont();
  gender = (Spinner) findViewById(R.id.gender);
       
 adpter_gender = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gen);
       
       
        gender.setAdapter(adpter_gender);
        
        gender.setOnItemSelectedListener(this);
        
        
        
        new get_details().execute("http://jsoncdn.webcodeplus.com/CustomerData.svc/LoadCustomerDetailsWithextraDetails/30/"+Sharedpref.getcustomerid());
        
        

        
		
        
        
        
        
        submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int errorCounter = 0;
				String e_address = address.getText().toString();
				String e_city = city.getText().toString();
				String e_zipcode = zipcode.getText().toString();
				String e_gender = gender.getSelectedItem().toString();
				String e_birthday = birthday.getText().toString();
				
				
				String e_phoneno = phoneno.getText().toString();
				String e_phonetype = phonetype.getSelectedItem().toString();
				
			
				 
				 if(phonetype.getSelectedItem().equals("Select PhoneType"))
					{
						((TextView)phonetype.getChildAt(0)).setError("Select PhoneType");
						
						gender.requestFocus();
						errorCounter++;
						
					} 	
				
				
				

					if (!isValidPhoneno(e_phoneno)) {
						phoneno.setError("Invalid Phone no");
						phoneno.requestFocus();
						errorCounter++;
					}
					else
					{
						phoneno.setError(null);
					}	
					

					if (!isValidBirthday(e_birthday)) {
						birthday.setError("Invalid Birthday");
						birthday.requestFocus();
						errorCounter++;
					}
					else
					{
						birthday.setError(null);
					}	
					
					 
					 if(gender.getSelectedItem().equals("Select Gender"))
						{
							((TextView)gender.getChildAt(0)).setError("Select Gender");
							
							gender.requestFocus();
							errorCounter++;
							
						} 	
					 
					 
						if(!isValidZipcode(e_zipcode)){
							zipcode.setError("Invalid Zipcode");
							zipcode.requestFocus();
							errorCounter++;
						} else {
							zipcode.setError(null);
							
						}
				
				if(!isValidCity(e_city))
				{
					city.setError("Invalid City");
					city.requestFocus();
					errorCounter++;
					
				}
				else {
					
					city.setError(null);
					
				}

				if (!isValidName(e_address)) {
					address.setError("Invalid Address");
					address.requestFocus();
					errorCounter++;
				}
				else
				{
					address.setError(null);
				}	
				
				
				if(errorCounter == 0)
				{
					
					new AdditionalAsyncTaskpost().execute(url);
				}
				
				

				
			
			}


			private boolean isValidPhoneno(String e_phoneno) {
				// TODO Auto-generated method stub
				
				if(e_phoneno != null && e_phoneno.length() == 10)
				{
					return true;
				}
				
				return false;
			}


			private boolean isValidZipcode(String e_zipcode) {
				// TODO Auto-generated method stub
				if(e_zipcode != null && e_zipcode.length() == 5)
				{
					
					
					
					
					return true;
				}
				
				return false;
			}


			private boolean isValidCity(String e_city) {
				// TODO Auto-generated method stub
				
				if(e_city != null && e_city.length() > 0)
				{
					return true;
				}
				
				return false;
			}


			private boolean isValidName(String e_address) {
				// TODO Auto-generated method stub
				
				if (e_address != null && e_address.length() > 0) {
					return true;
				}
				return false;
				


		
			}
		});
        
        
        
        back_additional.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
        
    }
    
   
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    }



	protected boolean isValidBirthday(String e_birthday) {
		// TODO Auto-generated method stub
	
		if (e_birthday != null && e_birthday.length() > 0) {
			
			
			
			
			return true;
		}
		return false;
		

	
	}





	private void setsystemfont() {
		// TODO Auto-generated method stub
		
        address.setTypeface(sysFont);
        city.setTypeface(sysFont);
        zipcode.setTypeface(sysFont);
        phoneno.setTypeface(sysFont);
        text.setTypeface(sysFont);
        textsubmit.setTypeface(sysFont);
        birthday.setTypeface(sysFont);
        
	}

	public void selectDate(View view) {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }
    public void populateSetDate(int year, int month, int day) {
   
    	
    		
    	
    	mEdit = (EditText)findViewById(R.id.editbirthdate);
    	mEdit.setText(month+"/"+day+"/"+year);

    	
    	
//    	youEditText.setInputType(InputType.TYPE_NULL);
//    	youEditText.requestFocus();
//    	
    	
    }
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    	
    	@Override
    	public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar calendar = Calendar.getInstance();
			int yy = calendar.get(Calendar.YEAR);
			
			int mm = calendar.get(Calendar.MONTH);
			int dd = calendar.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog datePK = new DatePickerDialog(getActivity(), this, yy, mm, dd);
			
			
			
			int year = yy - 18;
			calendar.set(Calendar.YEAR, yy-18);
//			calendar.set(y, Calendar.MONTH, Calendar.DAY_OF_MONTH);
			Date d = calendar.getTime();
			
			
			
		
			 
			
			 
			datePK.getDatePicker().setMaxDate(d.getTime());
//			datePK.getDatePicker().setMinDate(19970420);
			
			return datePK;
    	}
    	
    	
    	
    	public void onDateSet(DatePicker view, int yy, int mm, int dd) {
    		
    	
    		Time t1 = new Time(Time.getCurrentTimezone());
            t1.setToNow();
            t1.set(t1.monthDay, t1.month, t1.year-18);
            
            String date = t1.format("%Y/%m/%d");
            
            
    	
    		
//    		Calendar userAge = new GregorianCalendar(yy,mm,dd);
//            
//    		
//    		Calendar minAdultAge = new GregorianCalendar();
//          
//            minAdultAge.add(Calendar.YEAR, -18);
//            
//            if (minAdultAge.before(userAge.YEAR))
//            {
//
//        		populateSetDate(yy, mm+1, dd);
//
//            
//            } 
//            else{
//            	
//            	Toast.makeText(getApplicationContext(), "maximum 18 year", Toast.LENGTH_SHORT);
//            	
//            	
//            }
    		populateSetDate(yy, mm+1, dd);
    	}
    }
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String item = parent.getItemAtPosition(position).toString();
		
		((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView) parent.getChildAt(0)).setTextSize(20);
        
        ((TextView) parent.getChildAt(0)).setTypeface(sysFont);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
		
		
//		Toast.makeText(arg0.getContext(), "Not Selected Item: ",
//				Toast.LENGTH_LONG).show();
	}

	
private class AdditionalAsyncTaskpost extends AsyncTask<String, Void, String> {
		
		

		

		@Override
		protected String doInBackground(String... urls) {

			 
			
		PersonDetail pd = new PersonDetail();
			pd.setCustomerID(Sharedpref.getcustomerid());
			pd.setSystemUserID("30");
			pd.setEmailID(Sharedpref.getmail());
			pd.setPhoneNo(phoneno.getText().toString());
			pd.setMobileNo(phoneno.getText().toString());
			pd.setAddress1(address.getText().toString());
			pd.setCity(city.getText().toString());
			pd.setZipcode(zipcode.getText().toString());
			pd.setBirthDate(birthday.getText().toString());
			pd.setGender(gender.getSelectedItem().toString());
		
			pd.setPhoneType(phonetype.getSelectedItem().toString());
		

			return POST(urls[0], pd);
		}
		@Override
		protected void onPreExecute() {
		// TODO Auto-generated method stub
			pDialog.show();
			
		super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(String result) {
//			
			
			if(Integer.parseInt(result) > 0)
			{
				
				
				Intent it_login = new Intent(AdditionalDetails.this,
						TransmoreDetails.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						AdditionalDetails.this, R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_login, bndlanimation);
				finish();
			}
			else
			{
				pDialog.cancel();
				Toast.makeText(AdditionalDetails.this, "Error Occure When Data Upadate", Toast.LENGTH_SHORT).show();
			}
		
			// etResponse.setText(json.toString());
		}
		
		@Override
		protected void onCancelled() {
		// TODO Auto-generated method stub
			pDialog.cancel();
		super.onCancelled();
		}
		
		}
	
	public static String POST(String url, PersonDetail pd) {
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
			
			jsonObject.accumulate("CustomerID",pd.getCustomerID());
			jsonObject.accumulate("SystemUserID",pd.getSystemUserID());
			jsonObject.accumulate("EmailID",pd.getEmailID());
			jsonObject.accumulate("PhoneNo",pd.getPhoneNo());
			jsonObject.accumulate("MobileNo",pd.getMobileNo());
			jsonObject.accumulate("Address1",pd.getAddress1());
			jsonObject.accumulate("City",pd.getCity());
			jsonObject.accumulate("Zipcode",pd.getZipcode());
			jsonObject.accumulate("BirthDate",pd.getBirthDate());
			jsonObject.accumulate("Gender",pd.getGender());
			jsonObject.accumulate("PrimaryPhoneType",pd.getPhoneType());
			
			
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

	
	
	 class get_details extends AsyncTask<String, Void, String> {

	        private final static String TAG = "LoginActivity.EfetuaLogin";

	        protected ProgressDialog progressDialog;
	        private URLSuppoter SuppURL;

	        @Override
	        protected void onPreExecute()
	        {
	        	
             
	        	System.out.println("On PRe Execute----done-------");
	            super.onPreExecute();
	            Log.d(TAG, "Executan do onPreExecute de EfetuaLogin");
	            
	            //inicia diálogo de progresso, mostranto processamento com servidor.
	            progressDialog = ProgressDialog.show(AdditionalDetails.this, "Loading", "Please Wait--------.", true, false);
	        }


	        @Override
	        protected String doInBackground(String... urls) {
	        	
	        	System.out.println("On do in back ground----done-------"); 	
	        	
	        	InputStream inputStream = null;
	            Log.d(TAG, "Executando doInBackground de EfetuaLogin");
	            
	            SuppURL = new URLSuppoter();
				  return SuppURL.GET(urls[0]);

	        }       	
	        	
	        	
	        @Override
	        protected void onPostExecute(String result)
	        {
	        	
	        	System.out.println("OnpostExecute----done-------");
	            super.onPostExecute(result);

	            
//	            
//	            if (res_get_detail == null || res_get_detail.equals("")) {
//	                progressDialog.dismiss();
//	                Toast.makeText(AdditionalDetails.this, "Erron in internet connection", Toast.LENGTH_LONG).show();
//	                return;
//	            }

	            Log.d(TAG, "Login passou persistindo info de login local no device");

     if(result.isEmpty() || result == null){
	            	
	            	address.setText("Enter Address");
	             	city.setText("Enter City");
	             	zipcode.setText("Enter Zip");
	             	gender.setSelection(adpter_gender.getPosition("MALE"));
	             	//rdob=rdob.replace("\'", " ");
	             	birthday.setText("D.O.B");
	             	phoneno.setText("Enter Phone No");
	             	phonetype.setSelection(adpter_phonetype.getPosition("WORK"));
	             	
	         				progressDialog.dismiss();
	         				
	         				btn_skip.setVisibility(View.GONE);
	            	
	            } 
     

	            else{
	        
	            
     
				try {
					JSONObject	obj = new JSONObject(result);
				
     		Log.i("RESPONSE",result);
     		
     		
     		
     	String add = obj.getString("Address1");
     	String rcity = obj.getString("City");
     	String rzip = obj.getString("Zipcode");
     	String rgen = obj.getString("Gender");
     	String rdob = obj.getString("BirthDate");
     	
     	if(add.isEmpty() || add.equalsIgnoreCase("null") && rcity.isEmpty() || rcity.equalsIgnoreCase("null") )
     	{
     		btn_skip.setVisibility(View.GONE);
     		
     	}else{
     		btn_skip.setVisibility(View.VISIBLE);
     	}
     	
     	if(rdob.contains("1/1/1900")){
			
			
     		rdob= "D.O.B";
		}
		
		
     	String rphono = obj.getString("MobileNo");
     	String rphtype = obj.getString("PrimaryPhoneType");
     		
     	
     		
     	//System.out.println("gteing string are====="+add+"\n"+rcity+"\n"+rzip+"\n"+rgen+"\n"+rdob+"\n"+rphono+"\n"+rphtype);
        
     	address.setText(add);
     	city.setText(rcity);
     	zipcode.setText(rzip);
     	gender.setSelection(adpter_gender.getPosition(rgen));
     	rdob=rdob.replace("\'", " ");
     	birthday.setText(rdob);
     	phoneno.setText(rphono);
     	phonetype.setSelection(adpter_phonetype.getPosition(rphtype));
     	
 				progressDialog.dismiss();
 			
     		
	           
				} 

     		catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
     		
	        }
	        }

	 }	
	
	
	
	
	
	
	
	
	
	
	
	
	
    
}