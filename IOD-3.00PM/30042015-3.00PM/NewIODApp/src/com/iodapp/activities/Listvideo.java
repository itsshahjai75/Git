package com.iodapp.activities;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.opentok.android.demo.opentoksamples.UIActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.AdapterView.OnItemClickListener;

public class Listvideo extends Activity {
	 PersonDetail person;
	File file;
	ArrayList<String> lis;
	private VideoView videoPreview;
	Button btndel, btnmore, btnlink;
//	Bundle b;
	TextView etfullname;
	public static List<String> listDoctorname = new ArrayList<String>();
	public static List<String> listDoctorid = new ArrayList<String>();
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		b = savedInstanceState;
		
		setContentView(R.layout.videolist);

		 listView = (ListView) findViewById(R.id.PhoneVideoList);
		Button btndel = (Button) findViewById(R.id.btndel);
		etfullname = (TextView) findViewById(R.id.txtfullname);
		etfullname.setText(Sharedpref.getfirstname() + " "
				+ Sharedpref.getlastname());

		new HttpAsyncTask()
				.execute("http://jsoncdn.webcodeplus.com/TokenData.svc/GetAvailableDoctorList/5");

//		lis=(ArrayList<String>) listDoctorname;
//		lis = new ArrayList<String>();
		int delay = 60000;// in ms

		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			public void run() {
				// System.out.println("Wait, what..:");
			}
		}, delay);
		// file = new File("/sdcard/Secret Camera");
		// File list[] = file.listFiles();
		//
		// for( int i=0; i< list.length; i++)
		// {
		// lis.add(list[i].getName());
		// }
//		lis.add("Json Verismo");
		// lis.add("Anil Sharma");
		

	}

	private OnItemClickListener videogridlistener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {

			TextView textViewItem = (TextView) v;
			String filename = textViewItem.getText().toString();
//			Toast.makeText(getApplicationContext(), "" + filename,
//					Toast.LENGTH_LONG).show();

		}
	};

	public void mydelbtn(View v) {

		// before we set the clicked one..
//		lis.add("4");
//		lis.add("Json Verismo");
		// lis.add("Anil Sharma");

//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//				R.layout.list_item_doctor, R.id.appname, listDoctorname);
		// get the row the clicked button is in
//		LinearLayout vwParentRow = (LinearLayout) v.getParent();
//		TextView textViewItem = (TextView) vwParentRow
//				.findViewById(R.id.time);
//		String filename = textViewItem.getText().toString();

//		Toast.makeText(getApplicationContext(), "4",
//				Toast.LENGTH_LONG).show();
		new HttpAsyncTaskpost()
		.execute("http://jsoncdn.webcodeplus.com/TokenData.svc/GenerateToken");
//		adapter.notifyDataSetChanged();
		onCreate(null);
	}
	private class HttpAsyncTaskpost extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			person = new PersonDetail();

			person.setCustomerID(Sharedpref.getcustomerid());
			person.setState(Sharedpref.getstatecode());
			person.setdoctorid("4");

			// person.setCountry(etEmail.getText().toString());
			// person.setTwitter(etPass.getText().toString());

			return POST(urls[0], person);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
//			txtLabel.setText(result);
			// Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG)
			// .show();
//			Spanned sp = Html.fromHtml(result);
//			String unm=String.valueOf(sp);
			try
			{
				JSONObject json = new JSONObject(result);
			// etResponse.setText(json.toString(1));
			// JSONObject json = new JSONObject(result);
			String str = "";
			String str1 = "";
			String tokenid = "";
			
			str = json.getString("SessionKey");
			Sharedpref.setsessionkey(str);
			str1 = json.getString("TokenKey");
			Sharedpref.setTokenkey(str1);
			tokenid = json.getString("TokenID");
			if(!(tokenid==null))
			{
//				etdocid.setVisibility(View.GONE);
//				button.setVisibility(View.GONE);
//				txtLabel.setVisibility(View.GONE);
//				button1.setVisibility(View.VISIBLE);
				 Intent intent = new Intent(getApplicationContext().getApplicationContext(), UIActivity.class);
			        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			       
					Bundle bndlanimation = 
							ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
					startActivity(intent, bndlanimation);
			}
			//
//			Spanned sp = Html.fromHtml(str);
//			etResponse.setText(sp);
			// etResponse.setText(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			}
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
			jsonObject.accumulate("StateID", person.getState());
			jsonObject.accumulate("CustomerID", person.getCustomerID());
			jsonObject.accumulate("DoctorID", person.getdoctorid());

			

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
			// progressBar.setVisibility(View.VISIBLE);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			// progressBar.setVisibility(View.INVISIBLE);
			try {
				// result = jsonstr;
				listDoctorname = new ArrayList<String>();
				listDoctorid = new ArrayList<String>();
				JSONArray ja = new JSONArray(result);
				String str = "";
				String str1 = "";
				String str2 = "";
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					str = jo.getString("FirstName");
//					str += " ";
					str1 = jo.getString("LastName");
					str2 = jo.getString("DoctorID");
					listDoctorname.add(str + str1 + " "+str2);
					listDoctorid.add(jo.getString("DoctorID"));
					
					// jo.getString("ProductName"), Toast.LENGTH_LONG).show();
				}
				
//				lis = new ArrayList<String>();
//
//			
//				// }
////				lis.add("Json Verismo");
//				// lis.add("Anil Sharma");
//				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
//						R.layout.list_item_doctor, R.id.appname, listDoctorname);
//				listView.setAdapter(adapter);
//				listView.setOnItemClickListener(videogridlistener);
				
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(Listvideo.this,
							R.layout.list_item_doctor, R.id.appname, listDoctorname);
					
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(videogridlistener);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
//					R.layout.list_item_doctor, R.id.appname, listDoctorname);
//			listView.setAdapter(adapter);
		}

		@Override
		protected void onCancelled() {

			super.onCancelled();
			Toast toast = Toast.makeText(getApplicationContext(),
					"Error is occured due to some probelm", Toast.LENGTH_LONG);
			toast.show();

		}
	}

	// public void myplaybtn(View v)
	// {
	//
	// //before we set the clicked one..
	//
	// lis.add("hello");
	// lis.add("hello1");
	//
	// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	// R.layout.list_item_doctor, R.id.appname,lis);
	// //get the row the clicked button is in
	//
	// LinearLayout vwParentRow = (LinearLayout)v.getParent();
	// TextView textViewItem = (TextView)vwParentRow.findViewById(R.id.appname);
	// String filename = textViewItem.getText().toString();
	// // Toast.makeText(getApplicationContext(), ""+filename,
	// Toast.LENGTH_LONG).show();
	//
	//
	//
	// Toast.makeText(getApplicationContext(), "Play Video"+filename,
	// Toast.LENGTH_LONG).show();
	// }

	protected void onResume() {

		super.onResume();
		//
		onCreate(null);

	}
}
