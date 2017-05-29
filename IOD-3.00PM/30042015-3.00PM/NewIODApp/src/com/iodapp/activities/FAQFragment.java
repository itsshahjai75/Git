package com.iodapp.activities;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iodapp.util.URLSuppoter;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FAQFragment extends Fragment {
	
	TextView etResponse;
	private ProgressBar progressBar;
	private Typeface sysFont;
	
	public FAQFragment(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_faq, container, false);
        etResponse = (TextView) rootView.findViewById(R.id.etResponse);
        
        
        setSystemFont();
        
		progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		new HttpAsyncTask()
				.execute("http://jsoncdn.webcodeplus.com/ContentData.svc/ChildContentListFromContentID/30/823");
        return rootView;
    }
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getActivity().getAssets(),"font/Leelawadee.ttf");
		
		etResponse.setTypeface(sysFont);
		
		
	}
	
//	public static String GET(String url) {
//		InputStream inputStream = null;
//		String result = "";
//		try {
//
//			// create HttpClient
//			HttpClient httpclient = new DefaultHttpClient();
//
//			// make GET request to the given URL
//			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
//
//			// receive response as inputStream
//			inputStream = httpResponse.getEntity().getContent();
//
//			// convert inputstream to string
//			if (inputStream != null)
//				result = convertInputStreamToString(inputStream);
//			else
//				result = "Did not work!";
//
//		} catch (Exception e) {
//			Log.d("InputStream", e.getLocalizedMessage());
//		}
//
//		return result;
//	}

//	private static String convertInputStreamToString(InputStream inputStream)
//			throws IOException {
//		BufferedReader bufferedReader = new BufferedReader(
//				new InputStreamReader(inputStream));
//		String line = "";
//		String result = "";
//		while ((line = bufferedReader.readLine()) != null)
//			result += line;
//
//		inputStream.close();
//		return result;
//
//	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		private URLSuppoter supporter;

		@Override
		protected String doInBackground(String... urls) {

			supporter = new URLSuppoter();
			return supporter.GET(urls[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			progressBar.setVisibility(View.INVISIBLE);
			try {
				
					JSONArray jarray = new JSONArray(result);
					String str = "";
					Spanned sp = null;
				
					for(int i=0;i<jarray.length();i++)
					{
				
						JSONObject json = jarray.getJSONObject(i);
						// etResponse.setText(json.toString(1));
						// JSONObject json = new JSONObject(result);
						

						str += json.getString("ContentTitle");
						str += "<br><br>";
						str += json.getString("FullDescription");
						
						// str = json.getString("FullDescription");
						// str = json.getString("FullDescription");
						//
						 sp = Html.fromHtml(str);
						
				
				// etResponse.setText(json.toString());
				}
					
					etResponse.setText(sp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onCancelled() {

			super.onCancelled();
//			Toast toast = Toast.makeText(getActivity(),
//					"Error is occured due to some probelm", Toast.LENGTH_LONG);
//			toast.show();

		}
	}
}
