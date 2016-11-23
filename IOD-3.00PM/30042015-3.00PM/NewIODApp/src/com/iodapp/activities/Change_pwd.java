package com.iodapp.activities;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.iodapp.util.URLSuppoter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.Toast;

public class Change_pwd extends Activity {
	
	
	
	RelativeLayout btn_submit;
	EditText new_pwd,cnfm_pwd;
	String new_pwd_str,customer_id,systemuser_id,emailid,Ipaddress;
	//Button btn_submit_pwd;
	PersonDetail person;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask 
		setContentView(R.layout.change_pwd);
		
		
		new_pwd=(EditText)this.findViewById(R.id.edit_newpwd_one);

		cnfm_pwd=(EditText)this.findViewById(R.id.edit_newpwd_two);
		
		
		
		
		btn_submit=(RelativeLayout)this.findViewById(R.id.btn_submit_ok);
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				if(new_pwd.getText().length()==0 || !new_pwd.getText().toString().equals(cnfm_pwd.getText().toString())){
					
					new_pwd.setError("Password Doesn't Match!!!");
					return;
				}
				else{
					
					
					new HttpAsyncTask()
					.execute("http://jsoncdn.webcodeplus.com/CustomerData.svc/PasswordPUT");
		
					
				}
				
			}
		});
		
	}

	
private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		
		URLSuppoter suppoter = new URLSuppoter();
		@Override
		protected String doInBackground(String... urls) {

//			person = new PersonDetail();
//			person.setPassword(new_pwd.getText().toString());


			// person.setCountry(etEmail.getText().toString());
			// person.setTwitter(etPass.getText().toString());

			return POST(urls[0], new_pwd.getText().toString());
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
			Log.d("geting_pwd_response===",result);
			
			if (result.equalsIgnoreCase("0")) 
			{
				Toast.makeText(getApplicationContext(), "Sorry!!!Password did not changed.", Toast.LENGTH_LONG).show();
				
			} else {
				
				
				
				Toast.makeText(getApplicationContext(), "Succesfully Changed.", Toast.LENGTH_LONG).show();
				//Sharedpref.setregcheck(true);
				Intent it_createacc = new Intent(getApplicationContext(),
						MyAccount.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_createacc, bndlanimation);
				finish();
				
			
			}
		}
	}
	
	
	
public  String POST(String url, String person) {
	InputStream inputStream = null;
	String result = "";
	try {

		// 1. create HttpClient
		HttpClient httpclient = new DefaultHttpClient();
		// 2. make POST request to the given URL
		HttpPost httpPost = new HttpPost(url);
		// String json = "";
		
		customer_id=Sharedpref.getcustomerid();
		systemuser_id="30";
		emailid=Sharedpref.getmail();
		Ipaddress="123";
		// 3. build jsonObject
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("SystemUserID", systemuser_id);
		jsonObject.accumulate("CustomerID", customer_id);
		jsonObject.accumulate("EmailID", emailid);
		jsonObject.accumulate("Password", new_pwd.getText().toString());
		jsonObject.accumulate("IPAddress", "123");
		
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
		Log.d("JSON_pwd_change",se.toString()+"======"+jsonObject.toString());

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
		
	} catch (Exception e) {
		Log.d("InputStream=====", e.getLocalizedMessage());
	}

	// 11. return result
	Log.d("result",""+result);
	return result;
}

	
public   String convertInputStreamToString(InputStream inputStream)
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
