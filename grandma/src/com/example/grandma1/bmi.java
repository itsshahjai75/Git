package com.example.grandma1;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class bmi extends Fragment{
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState); 

	}


	
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static bmi newInstance(int sectionNumber) {
		bmi fragment = new bmi();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	public bmi() {
	}

	NumberPicker n1,n2,n3;
	TextView t1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View rootView = inflater.inflate(R.layout.bmi, container,false);
		Button b1 = (Button)rootView.findViewById(R.id.button1);
		t1 = (TextView)rootView.findViewById(R.id.textView8);
		n1=(NumberPicker)rootView.findViewById(R.id.numberPicker1);
		n2=(NumberPicker)rootView.findViewById(R.id.numberPicker2);
		n3=(NumberPicker)rootView.findViewById(R.id.numberPicker3);
		String[] nums = new String[100];
		int i;
		for( i=0; i<nums.length; i++)
		{nums[i]=Integer.toString(i);}
		n1.setMinValue(10);
		n1.setMaxValue(100);
		n1.setWrapSelectorWheel(false);
		n1.setDisplayedValues(nums);
		n1.setValue(10);
		
		n3.setMinValue(1);
		n3.setMaxValue(100);
		n3.setWrapSelectorWheel(false);
		n3.setDisplayedValues(nums);
		n3.setValue(1);
		
		nums = new String[20];
		for( i=0;i<nums.length;i++)
		{nums[i]=Integer.toString(i);}
		n2.setMinValue(1);
		n2.setMaxValue(12);
		n2.setWrapSelectorWheel(false);
		n2.setDisplayedValues(nums);
		n2.setValue(1);
		
		
		b1.setOnClickListener(new View.OnClickListener() {
				
					@Override
					public void onClick(View v) {
					int wt=n1.getValue();
					int ft=n2.getValue();
					int in=n3.getValue();
					float ht= (float) ((ft*0.3)+(in*0.025));
					float bmi=wt/(ht*ht);
					t1.setVisibility(View.VISIBLE);
					if(bmi<18.5)
					{
						t1.setText("BMI index:"+bmi+"\n\nA BMI of less than 18.5 indicates that you are undernourished, so you may need to put on some weight. You are recommended to ask your doctor or a dietician for advice.");
						//t1.setBackground(new ColorDrawable(Color.parseColor("#ffff00")));
					}
					else if(bmi<25)
					{
						t1.setText("BMI index:"+bmi+"\n\nA BMI ranging in 18.5-25 indicates that you are at healthy weight for your hieght.By maintaining a healthy weight, you lower your risk of developing serious health problems.");
						//t1.setBackground(new ColorDrawable(Color.parseColor("#00ff00")));
					}
					else if(bmi<30)
					{
						t1.setText("BMI index:"+bmi+"\n\nA BMI ranging in 25-30 indicates that you are slightly overweight. You may be advised to lose some weight for health reasons.You are recommended to ask your doctor or a dietician for advice.");
						//t1.setBackground(new ColorDrawable(Color.parseColor("#ff9900")));
					}
					else 
					{
						t1.setText("BMI index:"+bmi+"\n\nA BMI ranging above 30 indicates that you are heavily overweight. Your health may be at risk if you dont lose some weight.You are recommended to ask your doctor or a dietician for advice.");
						//t1.setBackground(new ColorDrawable(Color.parseColor("#ff0000")));
					}

					
					
						
						
						
						
						
					}
		});
	     	      	
		return rootView;
	
	      }

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}



}
