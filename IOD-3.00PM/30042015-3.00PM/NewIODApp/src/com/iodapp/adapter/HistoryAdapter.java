package com.iodapp.adapter;

import java.util.ArrayList;
import java.util.List;

import com.iodapp.activities.R;
import com.iodapp.model.HistoryBean;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryAdapter extends BaseAdapter{

	private Activity activity;
	private List<HistoryBean> historyItems = new ArrayList<HistoryBean>();
	private LayoutInflater inflater;
	private TextView drname;
	private TextView callDate;
	private TextView callDuration;
	private TextView callCredit;
	private HistoryBean hb;
	private Typeface sysFont;
	
	
	public HistoryAdapter(Activity activity,List<HistoryBean> historyItems) {
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.historyItems = historyItems;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.historyItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.historyItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
		if (convertView == null)
			convertView = inflater.inflate(R.layout.call_list_row, null);
		
		
		
		
		drname = (TextView) convertView.findViewById(R.id.title);
		callDate = (TextView) convertView.findViewById(R.id.date_call);
		callDuration = (TextView) convertView.findViewById(R.id.timeDuration);
		callCredit = (TextView) convertView.findViewById(R.id.local);
		
		hb = historyItems.get(position);
		
		drname.setText("Dr. "+hb.getDrName());
		callDate.setText(hb.getDay()+"\n"+hb.getMonth());
		callDuration.setText(hb.getTime() +" ("+hb.getDuration()+" )");
		
		if(Integer.parseInt(hb.getCredit().toString()) == 0)
		{
			callCredit.setText("with International Doctor");
		}
		else
		{
			callCredit.setText("with U.S Certified Doctor");
		}
		
		
		
		
		
		return convertView;
	}
	
	private void setSystemFont()
	{
	sysFont = Typeface.createFromAsset(activity.getAssets(),"font/Leelawadee.ttf");

	drname.setTypeface(sysFont);
	callDate.setTypeface(sysFont);
	callDuration.setTypeface(sysFont);
	callCredit.setTypeface(sysFont);


	}

}
