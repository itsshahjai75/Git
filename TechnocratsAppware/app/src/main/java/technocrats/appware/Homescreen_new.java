package technocrats.appware;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;

import info.jay.slidingmenu.adapter.NavDrawerListAdapter;
import info.jay.slidingmenu.model.NavDrawerItem;

public class Homescreen_new extends AppCompatActivity {

	private Toolbar mToolbar;
	private DrawerLayout mDrawerLayout;
	NavigationView mNavigationView;
	FrameLayout mContentFrame;

	private static final String PREFERENCES_FILE = "mymaterialapp_settings";
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	private boolean mUserLearnedDrawer;
	private boolean mFromSavedInstanceState;
	private int mCurrentSelectedPosition;


	// used to store app title
	private CharSequence mTitle;
	// nav drawer title
	private CharSequence mDrawerTitle;

	private Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.homescreen_new);




		setUpToolbar();
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		mTitle = mDrawerTitle = getTitle();
		//((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");



		mDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
		FragmentManager fragmentManager1 = getFragmentManager();
		Fragment fragment1 = new HomeFragment();
		fragmentManager1.beginTransaction().replace(R.id.nav_contentframe, fragment1).commit();

		mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(this, PREF_USER_LEARNED_DRAWER, "false"));

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
			mFromSavedInstanceState = true;

		}


		setUpNavDrawer();

		mNavigationView = (NavigationView) findViewById(R.id.nav_view);
		mContentFrame = (FrameLayout) findViewById(R.id.nav_contentframe);

		mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				Fragment fragment = null;
				menuItem.setChecked(true);
				switch (menuItem.getItemId()) {
					case R.id.navigation_item_1:
						setTitle("Technocats Appware");
						fragment = new HomeFragment();
						Snackbar.make(mContentFrame, "Home", Snackbar.LENGTH_SHORT).show();
						mCurrentSelectedPosition = 0;

					break;
					case R.id.navigation_item_2:

						setTitle("Our Services");
						fragment = new PagesFragment();
						Snackbar.make(mContentFrame, "Service", Snackbar.LENGTH_SHORT).show();
						mCurrentSelectedPosition = 1;
						break;
					case R.id.navigation_item_3:

						setTitle("Live Projects");
						fragment = new WhatsHotFragment();
						Snackbar.make(mContentFrame, "Live Projects", Snackbar.LENGTH_SHORT).show();
						mCurrentSelectedPosition = 2;
						break;
					case R.id.navigation_item_4:

						setTitle("Online Registrationform");
						Intent reg= new Intent(Homescreen_new.this,Reg_form.class);
						startActivity(reg);

						Snackbar.make(mContentFrame, "Online Registration", Snackbar.LENGTH_SHORT).show();
						mCurrentSelectedPosition = 3;
						break;
					case R.id.navigation_item_5:

						setTitle("Offers");
						Intent offer= new Intent(Homescreen_new.this,Offers.class);
						startActivity(offer);

						Snackbar.make(mContentFrame, "Offers", Snackbar.LENGTH_SHORT).show();
						mCurrentSelectedPosition = 4;
						break;
					case R.id.navigation_item_6:

						setTitle("Communities");
						fragment = new CommunityFragment();
						Snackbar.make(mContentFrame, "Communities", Snackbar.LENGTH_SHORT).show();
						mCurrentSelectedPosition = 5;
						break;
					case R.id.navigation_item_7:

						setTitle("About Us");
						fragment = new  FindPeopleFragment();
						Snackbar.make(mContentFrame, "About Us", Snackbar.LENGTH_SHORT).show();
						mCurrentSelectedPosition = 6;
						break;
					case R.id.navigation_item_8:

						setTitle("Snapshot of Projects");
						fragment = new PhotosFragment();

						Snackbar.make(mContentFrame, "Live App's Snap Shots", Snackbar.LENGTH_SHORT).show();
						mCurrentSelectedPosition = 7;
						break;
					case R.id.navigation_item_9:

						setTitle("Scan QR Code");
						Intent scan= new Intent(Homescreen_new.this,Scan_QR.class);
						startActivity(scan);

						Snackbar.make(mContentFrame, "Scan Your Certificates", Snackbar.LENGTH_SHORT).show();
						mCurrentSelectedPosition = 8;
						break;

					default:
						return true;
				}


				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.nav_contentframe, fragment).commit();



					mDrawerLayout.closeDrawers();

				} else {
					// error in creating fragment
					Log.e("MainActivity", "Error in creating fragment");
				}
				return true;



			}
		});

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION, 0);
		Menu menu = mNavigationView.getMenu();
		menu.getItem(mCurrentSelectedPosition).setChecked(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.action_settings:
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setUpToolbar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
		}
	}

	private void setUpNavDrawer() {
		if (mToolbar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			mToolbar.setNavigationIcon(R.drawable.ic_drawer);
			mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mDrawerLayout.openDrawer(GravityCompat.START);
				}
			});
		}

		if (!mUserLearnedDrawer) {
			mDrawerLayout.openDrawer(GravityCompat.START);
			mUserLearnedDrawer = true;
			saveSharedSetting(this, PREF_USER_LEARNED_DRAWER, "true");
		}

	}

	public static void saveSharedSetting(Homescreen_new ctx, String settingName, String settingValue) {
		SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(settingName, settingValue);
		editor.apply();
	}

	public static String readSharedSetting(Homescreen_new ctx, String settingName, String defaultValue) {
		SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
		return sharedPref.getString(settingName, defaultValue);
	}
}