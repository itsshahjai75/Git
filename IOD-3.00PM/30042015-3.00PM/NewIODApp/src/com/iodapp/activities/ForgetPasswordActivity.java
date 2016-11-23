package com.iodapp.activities;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iodapp.util.URLSuppoter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPasswordActivity extends Activity {

	TextView etResponse;
	TextView tvIsConnected;

	EditText etforgetmail;
	RelativeLayout btnsubmit;
	 String email;
	private TextView errorView;
	private ImageButton btn_back;
	private TextView singIn;
	private Button skip;
	private Typeface sysFont;
	private TextView title;
	private TextView btntext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
		setContentView(R.layout.forget_pass_activity);

		// get reference to the views
		 etResponse = (TextView) findViewById(R.id.etResponse);
		tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
		etforgetmail = (EditText) findViewById(R.id.etforgetmail);
		errorView = (TextView) findViewById(R.id.ErrorTextview);

		btnsubmit=(RelativeLayout)findViewById(R.id.btnsubmit);
		btntext = (TextView) findViewById(R.id.btntext);
		
		btn_back = (ImageButton) findViewById(R.id.btnbackF);
		
		singIn = (TextView) findViewById(R.id.Allready_regF);
		skip = (Button) findViewById(R.id.btnskipf);
		title = (TextView) findViewById(R.id.text1);
		setSystemFont();
		
		// check if you are connected or not
		if (isConnected()) {
			tvIsConnected.setBackgroundColor(0xFF00CC00);
			tvIsConnected.setText("You are conncted");
		} else {
			tvIsConnected.setText("You are NOT conncted");
		}
	singIn.setOnClickListener(new View.OnClickListener() {
			
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
		
		btnsubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 email = etforgetmail.getText().toString();
					if (!isValidEmail(email)) {
						etforgetmail.setError("Invalid Email");
					}
					if(isValidEmail(email))
					{
					 new HttpAsyncTask()
					 .execute("http://jsoncdn.webcodeplus.com/CustomerData.svc/CheckExist/30/"+email);
			
					}
			}
		});
		
		skip.setOnClickListener(new OnClickListener() {
			
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
		// call AsynTask to perform network operation on separate thread
		
	}
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
		
		etResponse.setTypeface(sysFont);
		tvIsConnected.setTypeface(sysFont);
		etforgetmail.setTypeface(sysFont);
		errorView.setTypeface(sysFont);
		btntext.setTypeface(sysFont);
		singIn.setTypeface(sysFont);
		skip.setTypeface(sysFont);
		title.setTypeface(sysFont);
		
		
	}
	
	// validating email id
		private boolean isValidEmail(String email) {
			String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(email);
			return matcher.matches();
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
	
	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		
		private URLSuppoter suppoter;
		
		@Override
		protected String doInBackground(String... urls) {

			suppoter = new URLSuppoter();
			
			return suppoter.GET(urls[0]);
//			return GET(urls[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			progressBar.setVisibility(View.VISIBLE);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			
//			progressBar.setVisibility(View.INVISIBLE);
//			try {
//				JSONObject json = new JSONObject(result);
//				// etResponse.setText(json.toString(1));
//				// JSONObject json = new JSONObject(result);
//				String cid="";	
//				cid = json.getString("CustomerID");
//				
//				Spanned sp = Html.fromHtml(cid);
			etResponse.setText(result);
//				Log.d("strum2", ""+unm);
				if(result.equalsIgnoreCase("0"))
				{
					
					errorView.setText("Sorry, Requested Email Does Not Exit...");
//					etResponse.setText("Sorry,Requested Email Does Not Exit");
					Toast.makeText(getApplicationContext(), "Sorry,Requested Email Does Not Exit", Toast.LENGTH_LONG).show();
//					Intent it_forgetpass = new Intent(getApplicationContext(),
//							LoginActivity.class);
//					Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
//							getApplicationContext(), R.anim.animation,
//							R.anim.animation2).toBundle();
//					startActivity(it_forgetpass, bndlanimation);
//					finish();
				}else
				{
//					etResponse.setText("Password Reset Mail Sent to your Registred Email");
					Toast.makeText(getApplicationContext(), "Password Reset Mail Sent to your Registred Email", Toast.LENGTH_LONG).show();
					Intent it_forgetpass = new Intent(getApplicationContext(),
							LoginActivity.class);
					Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
							getApplicationContext(), R.anim.animation,
							R.anim.animation2).toBundle();
					startActivity(it_forgetpass, bndlanimation);
					finish();
				}
				
//				
				
//				 etResponse.setText(json.toString());
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}

		@Override
		protected void onCancelled() {

			super.onCancelled();
			Toast toast = Toast.makeText(getApplicationContext(),
					"Error is occured due to some probelm", Toast.LENGTH_LONG);
			toast.show();

		}
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
}