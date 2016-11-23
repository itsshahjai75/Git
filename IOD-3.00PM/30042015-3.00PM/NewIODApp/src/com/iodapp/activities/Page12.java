package com.iodapp.activities;



import android.graphics.Typeface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class Page12 extends Fragment {


    private Typeface sysFont;
	private TextView ptext1;

	private TextView ptext2;


	private TextView ptext;
	private TextView ptext3;
	private TextView ptext4;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.page02, container, false);
		ptext = (TextView) rootView.findViewById(R.id.ptext11);
		 ptext1 = (TextView) rootView.findViewById(R.id.ptext12);
		 ptext2 = (TextView) rootView.findViewById(R.id.ptext13);
		 ptext3 = (TextView) rootView.findViewById(R.id.ptx4);
		 ptext4 = (TextView) rootView.findViewById(R.id.ptx5);
		
		
		 setSystemFont();
		
		 return rootView;
	}
	
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getActivity().getAssets(),"font/Leelawadee.ttf");
		ptext.setTypeface(sysFont);
		ptext1.setTypeface(sysFont);
		ptext2.setTypeface(sysFont);
		ptext3.setTypeface(sysFont);
		ptext4.setTypeface(sysFont);
	
	}
	
}
