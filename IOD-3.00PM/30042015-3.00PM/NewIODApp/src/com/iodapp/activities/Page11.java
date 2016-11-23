package com.iodapp.activities;

import android.graphics.Typeface;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


public class Page11 extends Fragment {

	// Log tag
	
		






	private Typeface sysFont;




	private TextView pt1;

	private TextView pt2;

	private TextView pt3;




	private TextView pt4;




	private TextView pt5;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.page01, container,
				false);
		
		
	
		
	
		pt1 = (TextView) rootView.findViewById(R.id.pt1);
		pt2 = (TextView) rootView.findViewById(R.id.pt2);
		pt3 = (TextView) rootView.findViewById(R.id.pt3);
		pt4 = (TextView) rootView.findViewById(R.id.pt4);
		pt5 = (TextView) rootView.findViewById(R.id.pt5);
		
	
		setSystemFont();
		return rootView;
	}

	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getActivity().getAssets(),"font/Leelawadee.ttf");
		pt1.setTypeface(sysFont);
		pt2.setTypeface(sysFont);
		pt3.setTypeface(sysFont);
		pt4.setTypeface(sysFont);
		pt5.setTypeface(sysFont);
		
		
		
	}
	

	
	
	



}

