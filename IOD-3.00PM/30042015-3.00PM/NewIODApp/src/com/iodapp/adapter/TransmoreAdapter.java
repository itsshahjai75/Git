package com.iodapp.adapter;



import com.iodapp.activities.R;

import com.iodapp.model.TransMoreData;



import java.util.List;



import android.app.Activity;

import android.content.Context;

import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;

import android.widget.BaseAdapter;

import android.widget.TextView;



public class TransmoreAdapter extends BaseAdapter  {
	private Activity activity;
	private LayoutInflater inflater;
	private List<TransMoreData> movieItems;
	
	
	
	private TransMoreData tmd;
	
	private Typeface sysFont;
	private TextView name;
	private TextView valid;
	private TextView ccCompany;

	public TransmoreAdapter(Activity activity, List<TransMoreData> movieItems) {
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
			convertView = inflater.inflate(R.layout.trans_row, null);

		
		
		
		 valid = (TextView) convertView.findViewById(R.id.valid);
		 name = (TextView) convertView.findViewById(R.id.card_name);
		 ccCompany =  (TextView) convertView.findViewById(R.id.ccCompany);
		
		 tmd = movieItems.get(position);
		
		 valid.setText("VALID THRU  "+tmd.getCCExpiryMonth()+" / "+tmd.getCCExpiryYear());
		 name.setText(tmd.getCardHolderName());
		 ccCompany.setText(tmd.getCCCompanyName());
		 
		return convertView;
	}
	
	
	
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(activity.getAssets(),"font/Leelawadee.ttf");
		
		valid.setTypeface(sysFont);
		name.setTypeface(sysFont);
		ccCompany.setTypeface(sysFont);
		
		
		
	}
	

	
}