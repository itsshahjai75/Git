package com.iodapp.activities;


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class OrderPlaceActivity extends Activity {
	private ProgressBar progressBar;
	Button sendxml;
	TextView eResonse;
	PersonDetail person;
	static String sOutBuffer;
	PaymentDetailName person1;
	private String ord_status_id;
	private Button btnDesh;
	private Typeface sysFont;
	private String trns_no; 

	SharedPreferences data;
	String flag_proId,flag_local,flag_inter,flag_validity,flag_ccholdrename,flg_ccexpmonth,flag_ccexpyear,flag_cccomapnyname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
        
		setContentView(R.layout.order_place_activity);
		
		ord_status_id = getIntent().getExtras().getString("Ord_st_id");
		trns_no = getIntent().getExtras().getString("trns_no");

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		sendxml = (Button) findViewById(R.id.btnsendxml);
		eResonse = (TextView) findViewById(R.id.textView1);
		btnDesh = (Button) findViewById(R.id.btnDesh);
		btnDesh.setVisibility(View.GONE);
		// sendxml.setOnClickListener(new View.OnClickListener() {

		setSystemFont();
		data =  PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			
		flag_proId=data.getString("ProductID", "510");
		flag_local=data.getString("LocalCredit", "1");
		flag_inter=data.getString("InternationalCredit", "1");
		flag_validity=data.getString("Validity", "1"); 
		flag_ccholdrename=data.getString("CardHolderName", "DEMO");
				flg_ccexpmonth=data.getString("CCExpiryMonth", "13");
				flag_ccexpyear=data.getString("CCExpiryYear", "21");
				flag_cccomapnyname=data.getString("CCCompanyName", "DEMO");
		
		
		Log.d("TAG","data are==="+flag_proId+flag_local+flag_inter+flag_validity);
		
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// // XmlTools();
		Xmlfile();
		new HttpAsyncTask()
				.execute("http://jsoncdn.webcodeplus.com/OrderData.svc/PlaceOrder");
		
		
		btnDesh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				data.edit().clear().commit();
				
				Intent it_payment = new Intent(getApplicationContext(),
						DashboardActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_payment, bndlanimation);
				finish();
			}
		});
		
		
		
		
		ImageButton A_back = (ImageButton)this.findViewById(R.id.btn_back_orderplace);
		A_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
				data.edit().clear().commit();
				Intent it_payment = new Intent(getApplicationContext(),
						DashboardActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_payment, bndlanimation);
				finish();
			}
			});

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		data.edit().clear().commit();
		Intent it_payment = new Intent(getApplicationContext(),
				DashboardActivity.class);
		Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
				getApplicationContext(), R.anim.animation,
				R.anim.animation2).toBundle();
		startActivity(it_payment, bndlanimation);
		finish();
		
		
		
	}
	
	
	
	private void setSystemFont()
	{
	sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");

	eResonse.setTypeface(sysFont);
	btnDesh.setTypeface(sysFont);


	}
	public void Xmlfile() {
		String SystemUserID = "30";
		String CustomerID = Sharedpref.getcustomerid();
		String TaxCategoryID = "";
		String Discount = "";
		String OrderStatusID = ord_status_id;
		//String OrderStatusID = "7";
		String CCNumber = Sharedpref.getcc_number();
		String SubTotal = Sharedpref.getamount();
		String OrderTotal = Sharedpref.getamount();
		String PaymentMethod = Sharedpref.getautho_num();
		String PaymentStatus = Sharedpref.getexact_msg();
		String ShippingCharge = "";
		String Tax = "";
		String OrderID = "0";
		String ProductID =flag_proId;// ViewOurPlansFragment.productid;
		String Quantity = "1";
		String Price = Sharedpref.getamount();
		String ShippingMethod = "";
		String CCCompanyName = flag_cccomapnyname;
		String CardHolderName = flag_ccholdrename;
		String CVVNumber = "";
		String CCExpiryMonth = flg_ccexpmonth;
		String CCExpiryYear = flag_ccexpyear;
		String trnasarmore_no = trns_no;
		String BillingFirstName = Sharedpref.getfirstname();
		String BillingLastName = Sharedpref.getlastname();
		String BillingAddress1 = "street2";
		String BillingAddress2 = "";
		String BillingCity = "valley";
		String BillingStateID = "5";
		String BillingCountryID = "0";
		String BillingZipcode = "12345";
		String BillingEmailId = Sharedpref.getmail();
		String BillingPhoneNo = "1234567891";
		String ShippingFirstName = Sharedpref.getfirstname();
		String ShippingLastName = Sharedpref.getlastname();
		String ShippingAddress1 = "street2";
		String ShippingAddress2 = "";
		String ShippingCity = "valley";
		String ShippingStateID = "5";
		String ShippingCountryID = "0";
		String ShippingZipcode = "12345";
		String ShippingEmailID = Sharedpref.getmail();
		String ShippingPhoneNo = "1234567891";

		String LocalCredit =flag_local;// ViewOurPlansFragment.local;
		String InternationalCredit =flag_inter;// ViewOurPlansFragment.international;
		String Validity =flag_validity;//ViewOurPlansFragment.validite;
		StringBuffer outBuffer = new StringBuffer();
		// XML packet to process transactions.
		// outBuffer.append("{");
		// outBuffer.append("XML:");
		outBuffer.append("<Orders>");
		outBuffer.append("<OrderDetails ");
		outBuffer.append("SystemUserID='" + SystemUserID + "' ");
		outBuffer.append("CustomerID='" + CustomerID + "' ");
		outBuffer.append("TaxCategoryID='" + TaxCategoryID + "' ");
		outBuffer.append("OrderStatusID='" + OrderStatusID + "' ");
		outBuffer.append("SubTotal='" + SubTotal + "' ");
		outBuffer.append("Discount='" + Discount + "' ");

		outBuffer.append("ShippingCharge='" + ShippingCharge + "' ");
		outBuffer.append("Tax='" + Tax + "' ");
		outBuffer.append("OrderTotal='" + OrderTotal + "' ");
		outBuffer.append("PaymentMethod='" + PaymentMethod + "' ");
		outBuffer.append("PaymentStatus='" + PaymentStatus + "' ");
		outBuffer.append("ShippingMethod='" + ShippingMethod + "' " + ">");
		outBuffer.append("</OrderDetails>");

		outBuffer.append("<OrderProducts ");
		outBuffer.append("SystemUserID='" + SystemUserID + "' ");
		outBuffer.append("OrderID='" + OrderID + "' ");
		outBuffer.append("ProductID='" + ProductID + "' ");
		outBuffer.append("Quantity='" + Quantity + "' ");
		outBuffer.append("Price='" + Price + "' " + ">");
		outBuffer.append("</OrderProducts>");

		outBuffer.append("<CCDetails ");
		outBuffer.append("SystemUserID='" + SystemUserID + "' ");
		outBuffer.append("OrderID='" + OrderID + "' ");
		outBuffer.append("CCCompanyName='" + CCCompanyName + "' ");
		outBuffer.append("CardHolderName='" + CardHolderName + "' ");
		outBuffer.append("CCNumber='" + CCNumber + "' ");
		outBuffer.append("CVVNumber='" + CVVNumber + "' ");
		outBuffer.append("CCExpiryMonth='" + CCExpiryMonth + "' ");
		outBuffer.append("CCExpiryYear='" + CCExpiryYear + "' " + " ");
		outBuffer.append("TransArmorToken='" + trnasarmore_no + "' " + ">");
		outBuffer.append("</CCDetails>");

		outBuffer.append("<CustomerDetails ");
		outBuffer.append("CustomerID='" + CustomerID + "' ");
		outBuffer.append("BillingFirstName='" + BillingFirstName + "' ");
		outBuffer.append("BillingLastName='" + BillingLastName + "' ");
		outBuffer.append("BillingAddress1='" + BillingAddress1 + "' ");
		outBuffer.append("BillingAddress2='" + BillingAddress2 + "' ");
		outBuffer.append("BillingCity='" + BillingCity + "' ");
		outBuffer.append("BillingStateID='" + BillingStateID + "' ");
		outBuffer.append("BillingCountryID='" + BillingCountryID + "' ");
		outBuffer.append("BillingZipcode='" + BillingZipcode + "' ");
		outBuffer.append("BillingEmailId='" + BillingEmailId + "' ");
		outBuffer.append("BillingPhoneNo='" + BillingPhoneNo + "' ");
		outBuffer.append("ShippingFirstName='" + ShippingFirstName + "' ");
		outBuffer.append("ShippingLastName='" + ShippingLastName + "' ");
		outBuffer.append("ShippingAddress1='" + ShippingAddress1 + "' ");
		outBuffer.append("ShippingAddress2='" + ShippingAddress2 + "' ");
		outBuffer.append("ShippingCity='" + ShippingCity + "' ");
		outBuffer.append("ShippingStateID='" + ShippingStateID + "' ");
		outBuffer.append("ShippingCountryID='" + ShippingCountryID + "' ");
		outBuffer.append("ShippingZipcode='" + ShippingZipcode + "' ");
		outBuffer.append("ShippingEmailID='" + ShippingEmailID + "' ");
		outBuffer.append("ShippingPhoneNo='" + ShippingPhoneNo + "' " + ">");
		outBuffer.append("</CustomerDetails>");

		outBuffer.append("<CreditDetails ");
		outBuffer.append("LocalCredit='" + LocalCredit + "' ");
		outBuffer.append("InternationalCredit='" + InternationalCredit + "' ");
		outBuffer.append("Validity='" + Validity + "' " + ">");
		outBuffer.append("</CreditDetails>");

		outBuffer.append("</Orders>");
		// outBuffer.append("}");

		// Print out the packet to stdout for your convenience.
		sOutBuffer = outBuffer.toString();
		// person.setxmldata(sOutBuffer);
		// eResonse.setText(sOutBuffer);
		Log.d("result1", "" + sOutBuffer);
		System.out.println(sOutBuffer);

	}

	public static String POST(String url, PersonDetail person) {
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
			jsonObject.accumulate("XML", person.getxmldata());

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
			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);

			} else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		// 11. return result
		Log.d("result", "" + result);
		return result;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			person = new PersonDetail();
			// //
			// // person.setSystemUserID("30");
			person.setxmldata(sOutBuffer);
			// person.setFirstName(etfname.getText().toString());

			return POST(urls[0], person);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
//			eResonse.setText("You have succefully paid for our service. Go to dashboard to start our amaizing service");
			int delay = 10000;// in ms

			Timer timer = new Timer();

			timer.schedule(new TimerTask() {
				public void run() {
					// System.out.println("Wait, what..:");
				}
			}, delay);
			// String unm=String.valueOf(sp);
//			Intent it_payment = new Intent(getApplicationContext(),
//					DashboardActivity.class);
//			Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
//					getApplicationContext(), R.anim.animation,
//					R.anim.animation2).toBundle();
//			startActivity(it_payment, bndlanimation);
//			Toast.makeText(
//					getApplicationContext(),
//					"You have succefully paid for our service. Go to dashboard to start our amaizing service",
//		
//			Toast.LENGTH_LONG).show();
			
			progressBar.setVisibility(View.GONE);
			btnDesh.setVisibility(View.VISIBLE);
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
}
