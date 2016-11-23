package com.iodapp.adapter;


import com.iodapp.activities.PersonDetail;

import com.iodapp.activities.CallWaiting;
import com.iodapp.activities.DoctorAvailableActivity;
import com.iodapp.activities.ImageAndDescription;
import com.iodapp.activities.R;
import com.iodapp.activities.Sharedpref;

import com.iodapp.model.Movie;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.opentok.android.demo.opentoksamples.MultipartyActivity;
import com.opentok.android.demo.opentoksamples.UIActivity;

public class CustomListAdapter extends BaseAdapter  {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Movie> movieItems;
	PersonDetail person;
	public static String doctorid;
	ImageLoader imageLoader = Sharedpref.getInstance().getImageLoader();
	private TextView title;
	private ImageView Dr_Image;
	private Button call;
	private TextView rating;
	private TextView year;
	private String imgUrl;
	private Movie m;
	private RatingBar Dr_rating;
	private Typeface sysFont;
	private TextView sp1;

	public CustomListAdapter(Activity activity, List<Movie> movieItems) {
		this.activity = activity;
		this.movieItems = movieItems;
	}

	@Override
	public int getCount() {
		return movieItems.size();
	}

	@Override
	public Object getItem(int location) {
		return movieItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		if (imageLoader == null)
			imageLoader = Sharedpref.getInstance().getImageLoader();
		
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		
		 title = (TextView) convertView.findViewById(R.id.title);
		 rating = (TextView) convertView.findViewById(R.id.rating);
		 Dr_rating = (RatingBar) convertView.findViewById(R.id.Dr_rating);
//		TextView genre = (TextView) convertView.findViewById(R.id.genre);
		year = (TextView) convertView.findViewById(R.id.releaseYear);
		 call = (Button) convertView.findViewById(R.id.btncall);
		 sp1 = (TextView) convertView.findViewById(R.id.sp1);
		Dr_Image = (ImageView) convertView.findViewById(R.id.thumbnail);
		
		// getting movie data for the row
		
		
//		int atualpos = position-1;
		 m = movieItems.get(position);
		
		//set Doctor Image
		
		
		

		// thumbnail image
		thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
//		Dr_Image.setImageBitmap(m.getImageBitmap());
		
		if(m.getAvailable().equalsIgnoreCase("Available"))
		{
//		Dr_Image.setBackgroundColor(Color.GREEN);
			Dr_Image.setBackgroundResource(R.drawable.fram_green);
		}
		else
		{
			Dr_Image.setBackgroundResource(R.drawable.fram_red);
		}
		
		// title
		title.setText(m.getTitle());
		
		// rating
		rating.setText(m.getAvailable());
		year.setText(m.getYear());
		call.setTag(m.getDoctore_id());
		Dr_rating.setRating(Float.parseFloat(m.getRating()));
		sp1.setText(m.getSpeciality());
		
//		
				
				
				
			
			
	
setSystemFont();
		
	

		return convertView;
	}
	
	private void setSystemFont()
	{
	sysFont = Typeface.createFromAsset(activity.getAssets(),"font/Leelawadee.ttf");

	title.setTypeface(sysFont);
	year.setTypeface(sysFont);
	sp1.setTypeface(sysFont);


	}

//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		if(v  == call || v== title || v == Dr_Image || v == rating)
//		{
//			String s;
//			s=(String) v.getTag();
//			
//	
////			Intent it_skip = new Intent(activity,
////					CallVideo.class);
////			
//			doctorid=s;
//			
////			activity.startActivity(it_skip);
//			new HttpAsyncTaskpost()
//				.execute("http://jsoncdn.webcodeplus.com/TokenData.svc/GenerateToken");
//		}
//		
//	}
/*
	private class HttpAsyncTaskpost extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			person = new PersonDetail();

			person.setCustomerID(Sharedpref.getcustomerid());
			person.setState(Sharedpref.getstatecode());
			person.setdoctorid(doctorid);

			return POST(urls[0], person);
		}
		
		@Override
		protected void onPostExecute(String result) {
//			
			Log.d("Result data", result.toString());
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
			
			Sharedpref.setTokenId(tokenid.toString());
			
			Log.d("DrImage Click", doctorid);
			Log.d("TokenId", tokenid);
			Log.d("ShatredTokenId", Sharedpref.getTokenId());
			
			
			
						
			if(!(tokenid==null))
			{
//				
				 Intent intent = new Intent(activity.getApplicationContext(), ImageAndDescription.class);
			        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        intent.putExtra("Dr.Name", title.getText().toString());
		        intent.putExtra("TokenId", tokenid);
//			       
					Bundle bndlanimation = 
							ActivityOptions.makeCustomAnimation(activity, R.anim.animation,R.anim.animation2).toBundle();
					activity.startActivity(intent, bndlanimation);
					
					
			}
		
			// etResponse.setText(json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			}
		}
	
*/
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
			jsonObject.accumulate("CustomerID", person.getCustomerID());
			jsonObject.accumulate("DoctorID", person.getdoctorid());
			jsonObject.accumulate("StateID", person.getState());
			
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

	
}