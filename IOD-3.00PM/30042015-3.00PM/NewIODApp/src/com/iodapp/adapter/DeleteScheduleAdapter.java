package com.iodapp.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import com.iodapp.activities.PersonDetail;
import com.iodapp.activities.R;
import com.iodapp.activities.Sharedpref;

import com.iodapp.model.ScheduleData;

public class DeleteScheduleAdapter extends BaseAdapter  {
	private Activity activity;
	private LayoutInflater inflater;
	private List<ScheduleData> movieItems = new ArrayList<ScheduleData>();
	PersonDetail person;
	public static String doctorid;
	ImageLoader imageLoader = Sharedpref.getInstance().getImageLoader();
	private TextView dateorder;
	private TextView fromtime;
	private TextView totime;
	private ScheduleData sc;
	private Typeface sysFont;
	private TextView btncancel;

	public DeleteScheduleAdapter(Activity activity, List<ScheduleData> movieItems) {
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
			convertView = inflater.inflate(R.layout.booked_list_row, null);

		
		

		
		

		dateorder = (TextView) convertView.findViewById(R.id.date_order2);
		
		fromtime = (TextView) convertView.findViewById(R.id.From_order2);
		
		totime = (TextView) convertView.findViewById(R.id.To_order2);
	
		sc = movieItems.get(position);
	btncancel = (TextView) convertView.findViewById(R.id.book2);
		
		
		dateorder.setText(sc.getDay()+"\n"+sc.getMonth());
		
	fromtime.setText(sc.getDrName());
		
		totime.setText("At-"+sc.getToTime());

	

		
	

		return convertView;
	}
//
	private void setSystemFont()
	{
	sysFont = Typeface.createFromAsset(activity.getAssets(),"font/Leelawadee.ttf");

	dateorder.setTypeface(sysFont);
	fromtime.setTypeface(sysFont);
	totime.setTypeface(sysFont);

	btncancel.setTypeface(sysFont);

	}

	
}
