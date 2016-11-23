package com.iodapp.activities;


import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	private List<String> _listDataHeader1; // header titles
	private List<String> _listDataHeader2; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<String>> _listDataChild;
	private Typeface sysFont;
	private TextView lblListHeader;
	private TextView lblListHeader1;
	private TextView lblListHeader2;

	public ExpandableListAdapter(Context context, List<String> listDataHeader,List<String> listDataHeader1,
			List<String> listDataHeader2,HashMap<String, List<String>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataHeader1 = listDataHeader1;
		this._listDataHeader2 = listDataHeader2;
		this._listDataChild = listChildData;
	}

	public ExpandableListAdapter(Activity activity,
			List<String> listDataHeader, List<String> listDataHeader1,
			List<String> listDataHeader2) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader1.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = (String) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_item, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.lblListItem);
		
		txtListChild.setText(childText);
		txtListChild.setTypeface(sysFont);
		
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader1.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	public Object getGroup1(int groupPosition) {
		return this._listDataHeader1.get(groupPosition);
	}
	
	public Object getGroup2(int groupPosition) {
		return this._listDataHeader2.get(groupPosition);
	}
	
	@Override
	public int getGroupCount() {
		return this._listDataHeader1.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		String headerTitle1 = (String) getGroup1(groupPosition);
		String headerTitle2 = (String) getGroup2(groupPosition);
		
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		
		 lblListHeader1 = (TextView) convertView
				.findViewById(R.id.lbllistcontine);
		
		 lblListHeader2 = (TextView) convertView
				.findViewById(R.id.lblprice);
		
		
//		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);
		
		
//		lblListHeader1.setTypeface(null, Typeface.NORMAL);
		lblListHeader1.setText(headerTitle1);
		
//		lblListHeader2.setTypeface(null, Typeface.NORMAL);
		lblListHeader2.setText(headerTitle2);
		
		setSystemFont();
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(_context.getAssets(),"font/Leelawadee.ttf");
		
		
		lblListHeader.setTypeface(sysFont);
		lblListHeader1.setTypeface(sysFont);
		lblListHeader2.setTypeface(sysFont);
		
		
		
		
	}

}
