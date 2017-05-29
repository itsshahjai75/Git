package com.iodapp.activities;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.firstdata.globalgatewaye4.util.Hmac;
import com.iodapp.util.URLSuppoter;





import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import android.os.AsyncTask;
import android.os.Bundle;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class HowItWorksFragment extends Fragment{

	private TextView step1;
	private TextView step2;
	private TextView step3;
	private TextView image1_text;
	private TextView image2_text;
	private TextView image3_text;
	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	private ProgressBar progressBar;
	private  String content_Url;
	private HashMap<String, String> map = new HashMap<String, String>();
	private String img_path;
	private Typeface sysFont;
	
	private static final String TAG = HowItWorksFragment.class
			.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.how_it_work, container, false);
		
		 content_Url = "http://jsoncdn.webcodeplus.com/ContentData.svc/ChildContentListFromContentID/30/816";
		 img_path = "http://ecmspro.webcodeplus.com/ContentData/";
		
		
		step1 = (TextView) rootView.findViewById(R.id.step1_tv);
		step2 = (TextView) rootView.findViewById(R.id.step2_tv);
		step3 = (TextView) rootView.findViewById(R.id.step3_tv);
		
		image1_text = (TextView) rootView.findViewById(R.id.imageView_1_Text);
		image2_text = (TextView) rootView.findViewById(R.id.imageView_2_Text);
		image3_text = (TextView) rootView.findViewById(R.id.imageView_3_Text);
		
		image1 = (ImageView) rootView.findViewById(R.id.imageView1);
		image2 = (ImageView) rootView.findViewById(R.id.imageView2);
		image3 = (ImageView) rootView.findViewById(R.id.imageView3);
		setSystemFont();
		
		progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		
		
	
		new HowWorks_AsyncTask().execute(content_Url);
		
	
		
	
		
		
		
		
		return rootView;
		
		
		
		
		
	     
	}
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getActivity().getAssets(),"font/Leelawadee.ttf");
		
		step1.setTypeface(sysFont);
		step2.setTypeface(sysFont);
		step3.setTypeface(sysFont);
		
		image1_text.setTypeface(sysFont);
		image2_text.setTypeface(sysFont);
		image3_text.setTypeface(sysFont);
	}
	
	private class HowWorks_AsyncTask extends AsyncTask<String, Void, String> {
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
				//JSONObject json = new JSONObject(result);
				// etResponse.setText(json.toString(1));
				// JSONObject json = new JSONObject(result);
				JSONArray jason = new JSONArray(result);
				String str = "";
				Log.d("Content Data", jason.toString());
				
				int c=1;
				
				for(int i=0;i<jason.length();i++)
				{
					JSONObject obj = jason.getJSONObject(i);
					
					map.put("ContentTitle"+c, obj.getString("ContentTitle").toString());
					map.put("Tagline"+c, obj.getString("Tagline").toString());
					map.put("LargeImage"+c, obj.getString("LargeImage").toString());
					
					c++;
				}
				
				step1.setText(map.get("Tagline1").toString());
				step2.setText(map.get("Tagline2").toString());
				step3.setText(map.get("Tagline3").toString());
				
				image1_text.setText(map.get("ContentTitle1").toString());
				image2_text.setText(map.get("ContentTitle2").toString());
				image3_text.setText(map.get("ContentTitle3").toString());
				
//				image1.setImageBitmap(getImageBitmapFormURL(img_path+map.get("LargeImage1")));
				
//				String image_path1 = "http://ecmspro.webcodeplus.com/ContentData/"+map.get("LargeImage1").toString();
				new DownloadImageTask(image1).execute(img_path+map.get("LargeImage1").toString());
				
				new DownloadImageTask(image2).execute(img_path+map.get("LargeImage2").toString());
				
				new DownloadImageTask(image3).execute(img_path+map.get("LargeImage3").toString());
				
				
				
				Log.d("Map Data", map.get("ContentTitle1").toString());
				Log.d("Map Data", map.get("Tagline1").toString());
				Log.d("Map Data", map.get("LargeImage1").toString());
				
				
				
//				
//				str += jason.getString("ContentTitle");
//				str += "\n";
//    			str += jason.getString("FullDescription");
				
//				str +="<br><br>Comming Soon";
//				Spanned sp = Html.fromHtml(str);
//				etResponse.setText(sp);
				// etResponse.setText(json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		protected void onCancelled() {

		
			super.onCancelled();
			Toast toast = Toast.makeText(getActivity(),
					"Error is occured due to some probelm", Toast.LENGTH_LONG);
			toast.show();

		}
	}
	
	
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	   
	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    @Override
	    protected void onPreExecute() {
	    	// TODO Auto-generated method stub
	    	progressBar.setVisibility(View.VISIBLE);
	    	super.onPreExecute();
	    }
	    protected Bitmap doInBackground(String... urls) {
	    	
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	    	progressBar.setVisibility(View.INVISIBLE);
	        bmImage.setImageBitmap(result);
	        
	    }    
	}
	
	
	
	

	
	
}
