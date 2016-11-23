package com.iodapp.adapter;

import java.util.ArrayList;
import java.util.List;

import com.iodapp.activities.R;
import com.iodapp.model.HistoryBean;
import com.iodapp.model.OrderHistoryBean;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class OrderAdatper extends BaseAdapter{

	private Activity activity;
	private List<OrderHistoryBean> historyItems = new ArrayList<OrderHistoryBean>();
	private LayoutInflater inflater;
	
	private OrderHistoryBean ohb;
	private TextView orderDate;
	private TextView orderId;
	private TextView orderPlan;
	private TextView youPaid;
	private Typeface sysFont;
	
	
	public OrderAdatper(Activity activity,List<OrderHistoryBean> historyItems) {
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
			convertView = inflater.inflate(R.layout.order_list_row, null);
		
		
		
		ohb = historyItems.get(position);
		
		
		orderDate = (TextView) convertView.findViewById(R.id.date_order);
		orderId = (TextView) convertView.findViewById(R.id.order_no);
		orderPlan = (TextView) convertView.findViewById(R.id.od_plan);
		youPaid = (TextView) convertView.findViewById(R.id.paid);
		
		
		orderDate.setText(ohb.getDay()+"\n"+ohb.getMonth());
		orderId.setText("Order# "+ohb.getOrd_id());
		orderPlan.setText(ohb.getPlan());
		youPaid.setText("$"+ohb.getPaid());
		
		
		
		return convertView;
	}
	
	private void setSystemFont()
	{
	sysFont = Typeface.createFromAsset(activity.getAssets(),"font/Leelawadee.ttf");

	orderDate.setTypeface(sysFont);
	orderId.setTypeface(sysFont);
	orderPlan.setTypeface(sysFont);
	youPaid.setTypeface(sysFont);


	}

}
