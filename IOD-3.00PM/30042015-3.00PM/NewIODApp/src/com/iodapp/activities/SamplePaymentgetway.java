package com.iodapp.activities;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.math.IEEE754rUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.firstdata.globalgatewaye4.util.Hmac;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SamplePaymentgetway extends Activity implements
		OnItemSelectedListener {

	EditText editcc_number;
	EditText editcardholder_name;
	// EditText editcc_expiry;
	EditText editcvd_code;
	EditText editamount;

	String GATEWAY_ID = "AE1534-05";
	String PASSWORD = "0mkw6u5h";
	String KEY_ID = "110312";
	String HMAC_KEY;
	TextView msg, msg1, msg2, msg3;

	//String data;
	public static String month_cc_expiry;
	public static String year_cc_expiry;
	public static String cc_expiry;
	String cc_number;
	public static String cardholder_name;
	String cvvn;
	String amount;
	String cvd_code;
	RelativeLayout btncheck;
	String exp_date;
	static String cctype_name;
	Spinner spimonth, spiyear,sp_cctype;
	PaymentDetailName person;
	PersonDetail cardedetail;
	private String order_status_ID="";
	private Typeface sysFont;
	private ProgressBar progress_bar;
	private ImageButton pay_back;
	private TextView msg4;
	private ProgressDialog progress_dialog;
	private CheckBox save_details;
	SharedPreferences data;
	
	private boolean save=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
		setContentView(R.layout.payment_getway_activity);
		data =  PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		

		msg = (TextView) findViewById(R.id.textView1);
		msg1 = (TextView) findViewById(R.id.textView2);
		msg2 = (TextView) findViewById(R.id.textView3);
		msg3 = (TextView) findViewById(R.id.textView4);
		msg4 = (TextView) findViewById(R.id.textpay);

		editcc_number = (EditText) findViewById(R.id.edit_cc_number);
		editcardholder_name = (EditText) findViewById(R.id.editholdernm);
		editcvd_code = (EditText) findViewById(R.id.edit_cvd_code);
		editamount = (EditText) findViewById(R.id.editamount);

		btncheck = (RelativeLayout) findViewById(R.id.btncheck);
		spimonth = (Spinner) findViewById(R.id.spimonth);
		spiyear = (Spinner) findViewById(R.id.spiyear);
		sp_cctype = (Spinner) findViewById(R.id.sp_cardtype);
		progress_bar = (ProgressBar) findViewById(R.id.progressBar1);
		pay_back = (ImageButton)	findViewById(R.id.p_back);
		save_details = (CheckBox) findViewById(R.id.Save_dtails);
		
		String checkbox_check = data.getString("checkboxcheck","no");
		if(checkbox_check.equalsIgnoreCase("yes")){
			save_details.setChecked(true);
			save_details.setEnabled(false);
		}
		
		progress_dialog = new ProgressDialog(this);
		progress_dialog.setMessage("Please Wait, Loading...");

		setSystemFont();
		// Month Spinner click listener
		spimonth.setOnItemSelectedListener(this);
		sp_cctype.setOnItemSelectedListener(this);
		
		List<String> cclist = new ArrayList<String>();
		cclist.add("Card Type");
		cclist.add("VISA");
		cclist.add("MASTER");
		cclist.add("AMERICAN EXPRESS");
		
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter_cctype = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, cclist);

		// Drop down layout style - list view with radio button
		dataAdapter_cctype
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		sp_cctype.setAdapter(dataAdapter_cctype);
		
		
		

		// Spinner Drop down elements
		List<String> categories = new ArrayList<String>();
		categories.add("Month");
		categories.add("01");
		categories.add("02");
		categories.add("03");
		categories.add("04");
		categories.add("05");
		categories.add("06");
		categories.add("07");
		categories.add("08");
		categories.add("09");
		categories.add("10");
		categories.add("11");
		categories.add("12");
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, categories);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spimonth.setAdapter(dataAdapter);

		// Year Spinner click listener
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);

		spiyear.setOnItemSelectedListener(this);

		// Spinner Drop down elements
		List<String> categories1 = new ArrayList<String>();
		categories1.add("Year");
		categories1.add(String.valueOf(year));
		categories1.add(String.valueOf(year + 1));
		categories1.add(String.valueOf(year + 2));
		categories1.add(String.valueOf(year + 3));
		categories1.add(String.valueOf(year + 4));
		categories1.add(String.valueOf(year + 5));
		categories1.add(String.valueOf(year + 6));
		categories1.add(String.valueOf(year + 7));
		categories1.add(String.valueOf(year + 8));
		categories1.add(String.valueOf(year + 9));
		categories1.add(String.valueOf(year + 10));
		categories1.add(String.valueOf(year + 11));
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, categories1);
		// Drop down layout style - list view with radio button
		dataAdapter1
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// attaching data adapter to spinner
		spiyear.setAdapter(dataAdapter1);
		
		pay_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		
		btncheck.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				btncheck.setEnabled(false);
				cc_number = editcc_number.getText().toString();
				cardholder_name = editcardholder_name.getText().toString();
				amount = Sharedpref.getamount();
				
				
//				cc_expiry = month_cc_expiry + year_cc_expiry;
				cvd_code=editcvd_code.getText().toString();
//				Sharedpref.setexp_year(spiyear.getSelectedItem().toString());
//				Sharedpref.setexp_year(spimonth.getSelectedItem().toString());
				
//				if (!isValidamount(amount)) {
//					editamount.setError("Amount");
//				}
				
				if (!isValidcc_number(cc_number)) {
					editcc_number.setError("Invalid Credit Card Number");
				}
				if (!isValidcvd_code(cvd_code)) {
					editcvd_code.setError("Invalid CVD Code");
				}
				if(editcardholder_name.getText().toString().trim().equals(""))
				{
					editcardholder_name.setError("Enter Name");
				}
				if(spimonth.getSelectedItem().toString().equalsIgnoreCase("Month"))
				{
					((TextView)spimonth.getChildAt(0)).setError("Month");
					
				}
				else
				{
					month_cc_expiry = spimonth.getSelectedItem().toString();
					Sharedpref.setexp_month(spimonth.getSelectedItem().toString());
					
				}
				
				if(spiyear.getSelectedItem().toString().equalsIgnoreCase("Year"))
				{
					((TextView)spiyear.getChildAt(0)).setError("Year");
					
				}
				else
				{
					year_cc_expiry = spiyear.getSelectedItem().toString().substring(2, 4);
					System.out.println("year goes to server is"+year_cc_expiry);
					Sharedpref.setexp_year(spiyear.getSelectedItem().toString());
				}
				
				if(month_cc_expiry != null && year_cc_expiry != null)
				{
					cc_expiry = month_cc_expiry + year_cc_expiry;
					System.out.println("epry send to server is===="+cc_expiry);
					exp_date = spimonth.getSelectedItem().toString() +"-" +spiyear.getSelectedItem().toString();
				}
				
				if (isValidcc_number(cc_number) && isValidcardholder_name(cardholder_name) && isValidcvd_code(cvd_code) 
						
						&& !cc_expiry.equalsIgnoreCase("")
						)
			{
					
					if(sp_cctype.getSelectedItem().toString().equalsIgnoreCase("Card Type")){
						
			
						Toast.makeText(SamplePaymentgetway.this, "Please, Select Card Type First!", Toast.LENGTH_LONG).show();
						btncheck.setEnabled(true);
						//return;
					}
					else{
					
					PaymentDialog();
					}
				}
				else
				{
					btncheck.setEnabled(true);
				}
			}
		});
		
		
		
	
		save_details.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
				if(isChecked)
				{
					save = true;
				}
				else
				{
					save = false;
				}
				
			}
		});
		
	}

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		super.onBackPressed();
		finish();
		
	}
	

	
	private void PaymentDialog()

	{
		final Dialog rankDialog = new Dialog(SamplePaymentgetway.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.payment_dialog);
        rankDialog.setCancelable(false);
       

        TextView text = (TextView) rankDialog.findViewById(R.id.tv_CardNo);
        TextView text2 = (TextView) rankDialog.findViewById(R.id.tv_name);
        TextView text3 = (TextView) rankDialog.findViewById(R.id.tv_expdate);
        TextView text4 = (TextView) rankDialog.findViewById(R.id.tv_CCno);
         text.setText("Card No  = "+editcc_number.getText().toString());
        text2.setText("Name      = "+cardholder_name);
        text3.setText("Exp Date = "+exp_date);
        text4.setText("CVV No   = "+cvd_code);
        
        
       
        
       text.setTypeface(sysFont);
       text2.setTypeface(sysFont);
       text3.setTypeface(sysFont);
       text4.setTypeface(sysFont);
       
       
        Button agree = (Button) rankDialog.findViewById(R.id.pay_yes);
        agree.setTextColor(Color.parseColor("#00bfff"));
        agree.setTypeface(sysFont);
        agree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				progress_dialog.show();
//				
				
				//String year = year_cc_expiry;
				
				 data.edit().putString("CardHolderName", cardholder_name).commit();
			        data.edit().putString("CCExpiryMonth", Sharedpref.getexp_month()).commit();
			        data.edit().putString("CCExpiryYear", year_cc_expiry).commit();
			        cctype_name=sp_cctype.getSelectedItem().toString();
			        data.edit().putString("CCCompanyName",cctype_name ).commit();
			        
			   //   String  flag_ccexpyear=data.getString("CCExpiryYear", "21");
			      
			      
			    //  Log.d("DATE_EXP", flag_ccexpyear);
					
				if(save_details.isChecked()==true){
					save=true;
				}else{
					save=false;
				}
				
				
				
				String str = new String();

				String x_TransactionKey = "eTv~5z0qmugbCIaxAF_Q";
				String x_Login = "WSP-IOD-D-XoZPfgAzuQ";
				String x_randomno = GetRandomNo();
				String x_timestamp = getTimeStamp();
				String x_amount = amount;
				String x_currency = ""; // default empty

				// str += x_TransactionKey;
				str += x_Login;
				str += "^";
				str += x_randomno;
				str += "^";
				str += x_timestamp;
				str += "^";
				str += x_amount;
				str += "^";
				str += x_currency;

				msg.setText(getTimeStamp());
				HMAC_KEY = sStringToHMACMD5(x_TransactionKey, str);
				String uri = "/transaction/v11";
				new HttpAsyncTask()
						.execute("https://api.demo.globalgatewaye4.firstdata.com"
								+ uri);
				rankDialog.cancel();
            	
			}
		});
        
        Button disagree = (Button) rankDialog.findViewById(R.id.pay_no);
        disagree.setTextColor(Color.parseColor("#00bfff"));
        disagree.setTypeface(sysFont);
        disagree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent it_login = new Intent(getApplicationContext(),
//						DashboardActivity.class);
//				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
//						getApplicationContext(), R.anim.animation,
//						R.anim.animation2).toBundle();
//				startActivity(it_login, bndlanimation);
////				Toast toast=Toast.makeText(getApplicationContext(), "Success - Redorect to Dashboared", Toast.LENGTH_LONG);
////				toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
////				toast.show();
////				hidePDialog();
//				finish();  
				btncheck.setEnabled(true);
            	rankDialog.cancel();
			}
		});
        
        //now that the dialog is set up, it's time to show it    
        rankDialog.show();    
	}
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
		
		
		editcc_number.setTypeface(sysFont);
		editcardholder_name.setTypeface(sysFont);
		editcvd_code.setTypeface(sysFont);
		editamount.setTypeface(sysFont);
		msg4.setTypeface(sysFont);
		
		
		
	}
	public static String POST(String url, PaymentDetailName person) {
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
			jsonObject.accumulate("gateway_id", "AE1534-05");
			jsonObject.accumulate("password", "0mkw6u5h");
			// jsonObject.accumulate("hmac_key", person.getHMAC_KEY());
			jsonObject.accumulate("transaction_type", "00");

			jsonObject.accumulate("credit_card_type", cctype_name);
			jsonObject.accumulate("cc_number", person.getcc_number());
			jsonObject.accumulate("cardholder_name",
					person.getcardholder_name());

			jsonObject.accumulate("cc_expiry", person.getcc_expiry());
			System.out.println("expiry gone to server is====="+person.getcc_expiry().toString());
			jsonObject.accumulate("amount", person.getamount());
			jsonObject.accumulate("cvd_code", person.getcvd_code());

			

			// 5. set json to StringEntity
			StringEntity se = new StringEntity(jsonObject.toString());

			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// String currentDateTimeString =
			// DateFormat.getDateTimeInstance().format(new Date());
			// SimpleDateFormat sdf = new SimpleDateFormat("EEE");
			// Date d = new Date();
			// String dayOfTheWeek = sdf.format(d);

			// Calendar c = Calendar.getInstance();
			// SimpleDateFormat sdf = new SimpleDateFormat(
			// "EEE, dd MMM yyyy HH:mm:ss");
			// String strDate = sdf.format(c.getTime());
			// SimpleDateFormat sdf = new SimpleDateFormat(
			// "yyyy-mm-ddTHH:mm:ssZ");
			// String strDate = sdf.format(getTimeStamp());
			// // 7. Set some headers to inform server about the type of the
			// content
			// httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-Type",
					"application/json; charset=UTF-8");
			// httpPost.setHeader("X-GGe4-Date:", getTimeStamp());
			// Log.d("datetime",""+ "");
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
			Sharedpref.setcvv_code(person.getcvd_code());
			}else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		// 11. return result
		Log.d("res", "" + result);
		return result;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			person = new PaymentDetailName();

			person.setHMAC_KEY(HMAC_KEY);
			person.setcc_number(cc_number);
			person.setcardholder_name(cardholder_name);

			person.setcc_expiry(cc_expiry);
			Log.d("ExpirySEND",cc_expiry);
			person.setcvd_code(editcvd_code.getText().toString());
			person.setamount(Sharedpref.getamount());

			return POST(urls[0], person);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
		
//			progress_dialog.show();
			progress_bar.setVisibility(View.VISIBLE);
			
			super.onPreExecute();
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
//			Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG)
//					.show();
//			msg1.setText(result);
			Log.d("REESPONSE==", result);
			

			if(result.contains("Bad Request")){
				
				result="Bed Request";
				
			}
			
			if(result.contains("Bad Request")){
					result ="Something Error!!!!!!!";
					
		AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(SamplePaymentgetway.this);
		
			// set title
//						alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
		
		// set dialog message
		alertDialogBuilder2
			.setMessage("Sorry!!!Wrong Data Pass!!!").setCancelable(false)
			.setPositiveButton("OK",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
		
				dialog.dismiss();
					
				}
			  });
 
			
				// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder2.create();
			
//							alertDialog.getButton(0).setBackgroundColor(Color.BLACK);
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

			 
			else{
			
			
			try {
				JSONObject json = new JSONObject(result);
				// etResponse.setText(json.toString(1));
				// JSONObject json = new JSONObject(result);
				String cc_no = "";
				String exact_code = "";
				String exact_msg = "";
				String auth_no = "";
				String bank_msg;
				
//				cc_no= json.getString("cc_number");
//				cardedetail.setcc_no(cc_no);
				exact_code= json.getString("exact_resp_code");
				Log.d("ext_resp_code=====",exact_code);
				exact_msg= json.getString("exact_message");
				
				auth_no= json.getString("authorization_num");
				String trnsMoreNo = "";
				if(save)
				{
					 trnsMoreNo = json.getString("transarmor_token");
				}
				else
				{
					trnsMoreNo = "";
				}
				
				bank_msg =json.getString("bank_message");
				//
				Spanned sp = Html.fromHtml(result);
				msg2.setText(sp);
				if(bank_msg.equalsIgnoreCase("Approved"))
				{
					Sharedpref.setcc_number(cc_number);
					Sharedpref.setautho_num(auth_no);
					Sharedpref.setexact_msg(exact_msg);
					order_status_ID = "19";
					Intent it_payment = new Intent(getApplicationContext(),
							OrderPlaceActivity.class);
					Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
							getApplicationContext(), R.anim.animation,
							R.anim.animation2).toBundle();
					
					it_payment.putExtra("Ord_st_id", order_status_ID);
					it_payment.putExtra("trns_no", trnsMoreNo);
					
					startActivity(it_payment, bndlanimation);
					finish();
					
				}else if(bank_msg.equalsIgnoreCase("Invalid CC Number") || bank_msg.equalsIgnoreCase("No Answer") || bank_msg.equalsIgnoreCase("Card is expired"))
				{
					order_status_ID = "20";
					msg3.setText(result);
					Intent it_payment = new Intent(getApplicationContext(),
							OrderPlaceActivity.class);
					Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
							getApplicationContext(), R.anim.animation,
							R.anim.animation2).toBundle();
					
					it_payment.putExtra("Ord_st_id", order_status_ID);
					it_payment.putExtra("trns_no", trnsMoreNo);
					startActivity(it_payment, bndlanimation);
					finish();
					
//					Toast.makeText(getApplicationContext(), "Not Approved", Toast.LENGTH_LONG).show();
				}
				else if (bank_msg.equalsIgnoreCase("Other Error"))
				{
					
					order_status_ID = "21";
					Intent it_payment = new Intent(getApplicationContext(),
							OrderPlaceActivity.class);
					Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
							getApplicationContext(), R.anim.animation,
							R.anim.animation2).toBundle();
					
					it_payment.putExtra("Ord_st_id", order_status_ID);
					it_payment.putExtra("trns_no", trnsMoreNo);
					startActivity(it_payment, bndlanimation);
					finish();
				}
				else
				{
//					hidePDialog();
					progress_bar.setVisibility(View.GONE);
					btncheck.setEnabled(true);					
				}
				// etResponse.setText(json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				
				//Toast.makeText(SamplePaymentgetway.this, "Please Check Your Card number", Toast.LENGTH_LONG).show();
				progress_bar.setVisibility(View.GONE);
				btncheck.setEnabled(true);
//				hidePDialog();
				e.printStackTrace();
			}catch(Exception e)
			{
				progress_bar.setVisibility(View.GONE);
				//Toast.makeText(SamplePaymentgetway.this, "Please Check Your Card number", Toast.LENGTH_LONG).show();
//				hidePDialog();
				btncheck.setEnabled(true);
				e.printStackTrace();
			}
			
			}
		}
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
//			hidePDialog();
			progress_bar.setVisibility(View.GONE);
			super.onCancelled();
		}
		
		
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

	protected static String GetRandomNo() {
		Random random = new Random();
		return String.valueOf(random.nextInt(1000));
	}

	protected static String getTimeStamp() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		long mil = calendar.getTimeInMillis();
		
		return String.valueOf(mil);
	}

	public static String sStringToHMACMD5(String sKey, String sData) {
		SecretKeySpec key;
		byte[] bytes;
		String sEncodedString = null;
		try {
			key = new SecretKeySpec((sKey).getBytes(), "ASCII");
			Mac mac = Mac.getInstance("HMACMD5");
			mac.init(key);
			// mac.update(sKey.getBytes());
			bytes = mac.doFinal(sData.getBytes("ASCII"));

			StringBuffer hash = new StringBuffer();

			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					hash.append('0');
				}
				hash.append(hex);
			}
			sEncodedString = hash.toString();
		} catch (UnsupportedEncodingException e) {
		} catch (InvalidKeyException e) {
		} catch (NoSuchAlgorithmException e) {
		}
		return sEncodedString;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
		
		
		
		((TextView) parent.getChildAt(0)).setTypeface(sysFont);
		 ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
	        ((TextView) parent.getChildAt(0)).setTextSize(20);

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

//	
		private boolean isValidcc_number(String cc_number) {
			if (cc_number != null && cc_number.length() > 15) {
				return true;
			}
			return false;
		}


		// validating cvvn
		private boolean isValidcvd_code(String cvd_code) {
			if (cvd_code != null && cvd_code.length() > 2) {
				return true;
			}
			return false;
		}

		// validating cardholder_name
		private boolean isValidcardholder_name(String cardholder_name) {
			if (cardholder_name != null && cardholder_name.length() > 0) {
				return true;
			}
//			 if(editcardholder_name.getText().toString().trim().equals(""))
//			 {
//		            return false;
//			 }
		    return false;
		}

		// validating amount
		private boolean isValidamount(String amount1) {
			if (amount1 != null && amount1.length() > 1) {
				return true;
			}
			return false;
		}
		
		
		private void hidePDialog() {
			if (progress_dialog != null) {
				progress_dialog.dismiss();
				progress_dialog = null;
			}
		}
}
