package com.iodapp.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InternetConnectionActivity extends Activity {

	Button btngetstart, btntryagin;
	 // flag for Internet connection status
    Boolean isInternetPresent = false;
     
    // Connection detector class
    ConnectionDetector cd;
	private Typeface sysFont;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_internet_connection_activity);

		btngetstart = (Button) findViewById(R.id.btngetstart);
		btntryagin = (Button) findViewById(R.id.btntryagin);
		setSystemFont();
		
		btngetstart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it_getstart = new Intent(
						android.provider.Settings.ACTION_SETTINGS);
				// intent.setClassName("com.android.phone",
				// "com.android.phone.NetworkSetting");
				startActivity(it_getstart);
//				Intent intent = new Intent(Intent.ACTION_MAIN);
//				intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
//				startActivity(intent);
			}
		});

		btntryagin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// creating connection detector class instance
		        cd = new ConnectionDetector(getApplicationContext());
		        // get Internet status
		        isInternetPresent = cd.isConnectingToInternet();

		        // check for Internet status
		        if (isInternetPresent) {
		        	Intent it_tryagain = new Intent(getApplicationContext(),
							LoginActivity.class);
					Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
							getApplicationContext(), R.anim.animation,
							R.anim.animation2).toBundle();
					startActivity(it_tryagain, bndlanimation);
					finish();
		        	
		        } else {
		        }
		        
				
			}
		});
	}
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
		btngetstart.setTypeface(sysFont);
		btntryagin.setTypeface(sysFont);
		
		
		
	}
}
