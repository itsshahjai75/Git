package com.iodapp.activities;

import com.iodapp.adapter.TabsPagerAdapter;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;

public class DoctorAvailableActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Local", "International" };
	private int tebItem;

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);  
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.doctor_available_activity);

		 tebItem = getIntent().getExtras().getInt("Select");
		
		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		actionBar.setIcon(R.drawable.appicon);
			
		viewPager.setAdapter(mAdapter);
//		actionBar.setHomeButtonEnabled(true);
//		actionBar.setBackgroundDrawable(new ColorDrawable(R.color.main_background));
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);	
		getActionBar().setTitle(Html.fromHtml("<font color='#ffffff' text-align='center'> Video Call</font>"));
		
		
		
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#00BFFF")));
	
		actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#00BFFF")));
		
actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00BFFF")));
		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		
		actionBar.setSelectedNavigationItem(tebItem);
		

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
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
