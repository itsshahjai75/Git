package com.iodapp.activities;


import com.iodapp.adapter.PagerAdapter;

import android.app.ActionBar;
import android.app.ActivityOptions;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class FirstRun extends FragmentActivity implements
ActionBar.TabListener {

private ViewPager viewPager;
private android.support.v4.view.PagerAdapter mAdapter;
// Tab titles
private String[] tabs = { "Local", "International" ,"sagar"};
private int tebItem;

private int count=0;
private TextView next;
private ImageView img1;
private ImageView img2;
private ImageView img3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
		
	
		setContentView(R.layout.activity_first_run);
		
		next = (TextView) findViewById(R.id.btnnext);
		
		img1 = (ImageView) findViewById(R.id.i1);
		img2 = (ImageView) findViewById(R.id.i2);
		img3 = (ImageView) findViewById(R.id.i3);
	
//		 tebItem = getIntent().getExtras().getInt("Select");
			
	
		 
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(count == 0)
				{
					viewPager.setCurrentItem(1);
				}
				else if(count == 1)
				{
					viewPager.setCurrentItem(2);
				}
				else
				{
					onBackPressed();
					finish();
				}
				
				
				
				
			}
		});

			// Initilization
			viewPager = (ViewPager) findViewById(R.id.pager);
			//actionBar = getActionBar();
			mAdapter = new PagerAdapter(getSupportFragmentManager());

			viewPager.setAdapter(mAdapter);

			/**
			 * on swiping the viewpager make respective tab selected
			 * */
			viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

				@Override
				public void onPageSelected(int position) {
					// on changing the page
					if(position == 0)
					{
						img1.setBackgroundResource(R.drawable.bluecircle);
						img2.setBackgroundResource(R.drawable.circle);
						img3.setBackgroundResource(R.drawable.circle);
					}
					else if(position == 1)
					{
						img1.setBackgroundResource(R.drawable.circle);
						img2.setBackgroundResource(R.drawable.bluecircle);
						img3.setBackgroundResource(R.drawable.circle);
					}
					else
					{
						img1.setBackgroundResource(R.drawable.circle);
						img2.setBackgroundResource(R.drawable.circle);
						img3.setBackgroundResource(R.drawable.bluecircle);
					}
					
					count = position;
					
					
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
		}
		
		
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			finish();
			super.onBackPressed();
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// on tab selected
			// show respected fragment view
			viewPager.setCurrentItem(tab.getPosition());
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}

	
	
	
	
	}