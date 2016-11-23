package com.iodapp.activities;



import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.firstdata.globalgatewaye4.*;
import com.firstdata.globalgatewaye4.exceptions.GlobalGatewayException;
import com.firstdata.globalgatewaye4.exceptions.InvalidTransactionException;
import com.firstdata.globalgatewaye4.exceptions.UnexpectedException;
import com.firstdata.globalgatewaye4.util.Hmac;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Xml.Encoding;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.concurrent.FutureTask;
import java.lang.Boolean;

public class PaymentGatwayActivity extends Activity implements OnItemSelectedListener{

	RelativeLayout btncheck;
	EditText editcc_number;
	EditText editcardholder_name;
//	EditText editcc_expiry;
	EditText editcvvn;
	EditText editamount;

//	private WebView webView;
	GlobalGatewayE4 e4;
	
	String GATEWAY_ID = "AE1534-05";
	String PASSWORD = "0mkw6u5h";
	String KEY_ID = "110312";
	String HMAC_KEY;
	TextView msg, msg1, msg2, msg3;
	String data;
	
	Spinner spimonth,spiyear;
	TextView etResponse;
	private ProgressBar progressBar;
	
	 String month_cc_expiry;
	 String year_cc_expiry;
	 String cc_expiry;
	 String cc_number;
	 String cardholder_name;
	 String cvvn;
	 String amount1;
	PaymentDetailName person;
	String strReturn = "";
	 Response response;
	 Request request;
	private Typeface sysFont;
	private TextView textpay;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_getway_activity);
	
		spimonth = (Spinner) findViewById(R.id.spimonth);
		spiyear = (Spinner) findViewById(R.id.spiyear);
		
		editcc_number = (EditText) findViewById(R.id.edit_cc_number);
		editcardholder_name = (EditText) findViewById(R.id.editholdernm);
		editcvvn = (EditText) findViewById(R.id.edit_cvd_code);
		editamount = (EditText) findViewById(R.id.editamount);

		btncheck = (RelativeLayout) findViewById(R.id.btncheck);
		title = (TextView) findViewById(R.id.Pay_title);
		
		textpay = (TextView) findViewById(R.id.textpay);

		msg = (TextView) findViewById(R.id.textView1);
		msg1 = (TextView) findViewById(R.id.textView2);
		msg2 = (TextView) findViewById(R.id.textView3);
		// msg3 = (TextView) findViewById(R.id.textView4);
		etResponse = (TextView) findViewById(R.id.textView4);
		
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		
		setSystemFont();
// Month Spinner click listener 
        spimonth.setOnItemSelectedListener(this);
 
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
 
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        // attaching data adapter to spinner
        spimonth.setAdapter(dataAdapter);
 
// Year Spinner click listener
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
       
        spiyear.setOnItemSelectedListener(this);
 
        // Spinner Drop down elements
        List<String> categories1 = new ArrayList<String>();
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
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories1);
        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spiyear.setAdapter(dataAdapter1);
        
        
		btncheck.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 cc_number = editcc_number.getText().toString();
				 cardholder_name = editcardholder_name.getText().toString();
				 amount1 = editamount.getText().toString();
				 month_cc_expiry = spimonth.getSelectedItem().toString();
				 year_cc_expiry = spiyear.getSelectedItem().toString().substring(2, 4);
				 cc_expiry = month_cc_expiry+year_cc_expiry;
				 
				 String str = new String();

					String x_TransactionKey = "eTv~5z0qmugbCIaxAF_Q";
					String x_Login = "WSP-IOD-D-XoZPfgAzuQ";
					String x_randomno = GetRandomNo();
					String x_timestamp = getTimeStamp();
					String x_amount = amount1;
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
					
					HMAC_KEY = sStringToHMACMD5(x_TransactionKey, str);
					
					String uri ="/transaction/v12";
					String url="https://api.demo.globalgatewaye4.firstdata.com" + uri; //DEMO Endpoint
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("gateway_id", "AE1534-05"));
					params.add(new BasicNameValuePair("password", "0mkw6u5h"));
					params.add(new BasicNameValuePair("transaction_type", "00"));
					
					params.add(new BasicNameValuePair("credit_card_type", "Visa"));
					params.add(new BasicNameValuePair("cc_number", "4111111111111111"));
					params.add(new BasicNameValuePair("cardholder_name", "PG"));
					params.add(new BasicNameValuePair("cc_expiry", "0420"));
					params.add(new BasicNameValuePair("amount", "10"));
					params.add(new BasicNameValuePair("cvd_code", "1111"));
					// getting JSON Object
					
					SendInfo(getApplicationContext(), url, params);
			
			}
		});
		
//		btncheck.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				 cc_number = editcc_number.getText().toString();
//				 cardholder_name = editcardholder_name.getText()
//						.toString();
////				final String cc_expiry = editcc_expiry.getText().toString();
//				 cvvn = editcvvn.getText().toString();
//				 amount1 = editamount.getText().toString();
//				 month_cc_expiry = spimonth.getSelectedItem().toString();
//				 year_cc_expiry = spiyear.getSelectedItem().toString().substring(2, 4);
//				 cc_expiry = month_cc_expiry+year_cc_expiry;
////				msg1.setText(cc_expiry);
//				if (!isValidamount(amount1)) {
//					editamount.setError("Amount");
//				}
//				
//				String str = new String();
//
//				String x_TransactionKey = "eTv~5z0qmugbCIaxAF_Q";
//				String x_Login = "WSP-IOD-D-XoZPfgAzuQ";
//				String x_randomno = GetRandomNo();
//				String x_timestamp = getTimeStamp();
//				String x_amount = amount1;
//				String x_currency = ""; // default empty
//
//				// str += x_TransactionKey;
//				str += x_Login;
//				str += "^";
//				str += x_randomno;
//				str += "^";
//				str += x_timestamp;
//				str += "^";
//				str += x_amount;
//				str += "^";
//				str += x_currency;
//				// msg3.setText(str);
//				// msg1.setText("No"+x_randomno);
//				// msg2.setText("time"+x_timestamp);
//
//				// msg.setText(sStringToHMACMD5(x_TransactionKey,str));
//				HMAC_KEY = sStringToHMACMD5(x_TransactionKey, str);
//
//				if (!isValidcc_number(cc_number)) {
//					editcc_number.setError("Invalid Credit Card Number");
//				}
//				if (!isValidcvvn(cvvn)) {
//					editcvvn.setError("Invalid CVV Number");
//				}
////				if(editcardholder_name.getText().toString().trim().equals(""))
////				{
////					editcardholder_name.setError("Enter Name");
////				}
////				if (isValidcc_number(cc_number)
////					    && isValidcvvn(cvvn)
////						&& isValidamount(amount1)) {
//					 
////				
//					BigDecimal sB = new BigDecimal(amount1);
//					 e4 = new GlobalGatewayE4(Environment.DEMO, GATEWAY_ID,PASSWORD, KEY_ID, HMAC_KEY);
//					
//					 Request request = e4.getRequest();
//					 request.credit_card_type(CreditCardType.Visa);
//					 request.cardholder_name(cardholder_name);
//					 request.cc_number(cc_number);
//					 request.cc_expiry(cc_expiry);
////					 request.cavv(cvvn);
//					 request.transaction_type(TransactionType.Purchase);
//					 request.amount(sB);
//					 
//					
//					 try {
//
//					  response =request.submit();
//					 
//					 System.out.println("Transaction Approved: "
//					 + response.transaction_approved());
//					 msg.setText(String.valueOf(response.transaction_approved()));
//					 System.out.println("Bank Message:"
//					 + response.bank_message());
//					 msg.setText(String.valueOf(response.bank_message()));
//					 System.out.println(response.ctr());
//					 msg.setText(String.valueOf(response.ctr()));
//					
//					 } catch (Exception e) {
//					 // TODO Auto-generated catch block
//						 Log.d("mainmm", ""+e.getMessage());
//					 e.printStackTrace();
//					 }
////					 AsyncResponse res=new AsyncResponse();
////					 res.execute();
//				
////				}
//			}
//		});
	}

	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
		
		
		editcc_number.setTypeface(sysFont);
		editcardholder_name.setTypeface(sysFont);
		editcvvn.setTypeface(sysFont);
		editamount.setTypeface(sysFont);
		textpay.setTypeface(sysFont);
		title.setTypeface(sysFont);
		
		
		
	}
	

	 public static String SendInfo(Context context, String url, List<NameValuePair> infos) {

	        String serverResponse = null;

	        final HttpPost   httppost   = new HttpPost(url);  

	        final HttpClient httpclient = new DefaultHttpClient();
	        try {                    
	            httppost.setEntity(new UrlEncodedFormEntity(infos));  
	            final HttpResponse response = httpclient.execute(httppost);
	            final InputStream is = response.getEntity().getContent();
	            final BufferedInputStream bis = new BufferedInputStream(is);
	            final ByteArrayBuffer baf = new ByteArrayBuffer(20);
	            int current = 0;  
	            while((current = bis.read()) != -1) {  
	                baf.append((byte)current);  
	            }  
	            serverResponse = response.getStatusLine().getReasonPhrase();
	            bis.close();
	            is.close();
	            Log.d("infourl", "Info sent to url: " + url);
	        } catch (Exception e) {
	            Log.e("errordata", "Error while transfering data" + e);
	            Log.d("errordata", "Error while transfering data"+ e);
	        }

	        return serverResponse;

	    }

//	private class AsyncResponse extends AsyncTask<String, Void, Void> {
//        @Override
//        protected Void doInBackground(String... params) {
////            Log.i(TAG, "doInBackground");
////            getFahrenheit(celcius);
//        	
//        	
//        	BigDecimal sB = new BigDecimal(amount1);
//        	
//			 e4 = new GlobalGatewayE4(Environment.DEMO, GATEWAY_ID,PASSWORD, KEY_ID, HMAC_KEY);
//			 
////			 msg2.setText(e4.getUrl());
//			  request = e4.getRequest();
//			 request.amount(sB);
//			 request.cardholder_name("PG");
////			 request.cavv("1111");
//			 request.cc_expiry("0620");
//			 request.cc_number("4111111111111111");
//			 
//			 request.credit_card_type(CreditCardType.Visa);
//			
//			 request.transaction_type(TransactionType.Purchase);
//			
////			 request.authorization_num("1222");
////			 request.card_cost(sB);
////			 request.cavv("111");
////			 request.credit_card_type(CreditCardType.Visa);
////			 request.currency_code("USD");
////			 
//////			 request.credit_card_type(CreditCardType.Visa);
////			 request.transaction_type(TransactionType.Purchase);
////			 request.cc_number(cc_number);
////			 request.cardholder_name(cardholder_name);
////			 request.cc_expiry(cc_expiry);
////			 request.amount(sB);
//			 
////			 try {
////				 processCommand();
////				 response = request.submit();
////				 processCommand();
////			 strReturn += "Transaction Record: " + response.ctr();
////             strReturn += "|" + response.exact_resp_code();
////             strReturn += "|" + response.exact_message();
////             strReturn += "|" + response.cc_number();
////             strReturn += "|" + response.authorization_num();
////             
////			 
////			 System.out.println("Transaction Approved: "
////			 + response.transaction_approved());
////			 msg.setText(String.valueOf(response.transaction_approved()));
////			 System.out.println("Bank Message:"
////			 + response.bank_message());
////			 msg.setText(String.valueOf(response.bank_message()));
////			 System.out.println(response.ctr());
//////			 
//////			 msg.setText(String.valueOf(response));
//////			 msg1.setText(strReturn);
////			 } catch (Exception e) {
////			 // TODO Auto-generated catch block
////			 e.printStackTrace();
////			 }
//			 run();
//            return null;
//        }
// 
//        @Override
//        protected void onPostExecute(Void result) {
////            Log.i(TAG, "onPostExecute");
////            tv.setText(fahren + "° F");
//        	progressBar.setVisibility(View.INVISIBLE);
//        }
// 
//        @Override
//        protected void onPreExecute() {
////            Log.i(TAG, "onPreExecute");
////            tv.setText("Calculating...");
//        	progressBar.setVisibility(View.VISIBLE);
//        }
// 
//        @Override
//        protected void onProgressUpdate(Void... values) {
////            Log.i(TAG, "onProgressUpdate");
//        }
// 
//    }
//	
//	private void processCommand() {
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//	 private boolean validate(){
//	        if(editcc_number.getText().toString().trim().equals(""))
//	            return false;
//	        else if(editcardholder_name.getText().toString().trim().equals(""))
//	            return false;
//	       
//	        else if(editcvvn.getText().toString().trim().equals(""))
//	            return false;
//	        else if(editamount.getText().toString().trim().equals(""))
//	            return false;
//	        else
//	            return true;    
//	    }
//	    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
//	        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
//	        String line = "";
//	        String result = "";
//	        while((line = bufferedReader.readLine()) != null)
//	            result += line;
//	 
//	        inputStream.close();
//	        return result;
//	 
//	    }   
	    
	protected static String GetRandomNo() {
		Random random = new Random();
		return String.valueOf(random.nextInt(1000));
	}

	protected static String getTimeStamp() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		long mil = calendar.getTimeInMillis();
		// msg.setText(String.valueOf(mil));
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

	// validating cc_number
	private boolean isValidcc_number(String cc_number) {
		if (cc_number != null && cc_number.length() > 15) {
			return true;
		}
		return false;
	}


	// validating cvvn
	private boolean isValidcvvn(String cvvn) {
		if (cvvn != null && cvvn.length() > 2) {
			return true;
		}
		return false;
	}

	// validating cardholder_name
	private boolean isValidcardholder_name(String cardholder_name) {
//		if (cardholder_name != null && cardholder_name.length() > 0) {
//			return true;
//		}
		 if(editcardholder_name.getText().toString().trim().equals(""))
		 {
	            return false;
		 }
	    return true;
	}

	// validating amount
	private boolean isValidamount(String amount1) {
		if (amount1 != null && amount1.length() > 1) {
			return true;
		}
		return false;
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

		

//		@Override
//		public void run() throws UnexpectedException, InvalidTransactionException {
//			// TODO Auto-generated method stub
//			 try {
////				 GsonBuilder builder = new GsonBuilder();
////			        builder.registerTypeAdapter(GlobalGatewayE4.class, new Gson());
////			        builder.create(); 
//				 processCommand();
//				
//					response = request.submit();
//				
//				 processCommand();
//			 strReturn += "Transaction Record: " + response.ctr();
//             strReturn += "|" + response.exact_resp_code();
//             strReturn += "|" + response.exact_message();
//             strReturn += "|" + response.cc_number();
//             strReturn += "|" + response.authorization_num();
//             
//			 
//			 System.out.println("Transaction Approved: "
//			 + response.transaction_approved());
//			 msg.setText(String.valueOf(response.transaction_approved()));
//			 System.out.println("Bank Message:"
//			 + response.bank_message());
//			 msg.setText(String.valueOf(response.bank_message()));
//			 System.out.println(response.ctr());
////			
//			 }
//			 catch (GlobalGatewayException e) {
//			
//				 Log.d("main", ""+e.getMessage());
//			 e.printStackTrace();
//			
//			 }catch (Exception e1) {
//				// TODO: handle exception
//				 Log.d("main", ""+e1.getMessage());
//				 e1.printStackTrace();
//			}
//		}
}