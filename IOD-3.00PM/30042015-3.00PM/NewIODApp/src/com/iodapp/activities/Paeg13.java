package com.iodapp.activities;

import com.iodapp.activities.R;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Paeg13 extends Fragment {

	private TextView ptxt;
	private TextView ptxt1;
	private TextView ptxt2;
	private TextView ptext3;
	private TextView ptext4;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.page03, container,
				false);
	
	       
		ptxt = (TextView) rootView.findViewById(R.id.ptx1);
		ptxt1 = (TextView) rootView.findViewById(R.id.ptx2);
		ptxt2 = (TextView) rootView.findViewById(R.id.ptx3);
		 ptext3 = (TextView) rootView.findViewById(R.id.ptx4);
		 ptext4 = (TextView) rootView.findViewById(R.id.ptx5);
		
		
		setsystemfont();
		
		return rootView;
}
	
	



	private void setsystemfont() {
		// TODO Auto-generated method stub
		
		Typeface sysFont = Typeface.createFromAsset(getActivity().getAssets(),"font/Leelawadee.ttf");
		
		ptxt.setTypeface(sysFont);
        ptxt1.setTypeface(sysFont);
        ptxt2.setTypeface(sysFont);
        ptext3.setTypeface(sysFont);
        
        ptext4.setTypeface(sysFont);
	}

}