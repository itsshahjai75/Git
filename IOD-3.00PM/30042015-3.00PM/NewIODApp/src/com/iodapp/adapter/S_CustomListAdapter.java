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

public class S_CustomListAdapter extends BaseAdapter  {
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

	public S_CustomListAdapter(Activity activity, List<Movie> movieItems) {
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
			convertView = inflater.inflate(R.layout.sc_list_row, null);

		if (imageLoader == null)
			imageLoader = Sharedpref.getInstance().getImageLoader();
		
		
		NetworkImageView thumbNail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail123);
		
		 title = (TextView) convertView.findViewById(R.id.title);
		 rating = (TextView) convertView.findViewById(R.id.rating);
		 Dr_rating = (RatingBar) convertView.findViewById(R.id.Dr_rating);
//		TextView genre = (TextView) convertView.findViewById(R.id.genre);
		year = (TextView) convertView.findViewById(R.id.releaseYear);
		 call = (Button) convertView.findViewById(R.id.btncall);
		Dr_Image = (ImageView) convertView.findViewById(R.id.thumbnail123);
		
		// getting movie data for the row
		
	sp1 = (TextView) convertView.findViewById(R.id.sp1);
	
		
//		int atualpos = position-1;
		 m = movieItems.get(position);
		
		//set Doctor Image
		
		 setSystemFont();
		

		// thumbnail image
//		Dr_Image.setImageBitmap(m.getImageBitmap());
		
		 thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
		// title
		title.setText(m.getTitle());
		sp1.setText(m.getSpeciality());
		// rating
		rating.setText(m.getAvailable());
		year.setText(m.getYear());
		call.setTag(m.getDoctore_id());
		Dr_rating.setRating(Float.parseFloat(m.getRating()));
		
//		
		setSystemFont();		
			
		Dr_Image.setBackgroundResource(R.drawable.fram_white);
			
	

		
	

		return convertView;
	}
	
	 private void setSystemFont()
	 {
	 sysFont = Typeface.createFromAsset(activity.getAssets(),"font/Leelawadee.ttf");

	 title.setTypeface(sysFont);
	 year.setTypeface(sysFont);
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

	

	
}