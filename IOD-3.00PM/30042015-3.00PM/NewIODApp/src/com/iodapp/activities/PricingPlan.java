package com.iodapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PricingPlan extends Activity {

	private LinearLayout MonthlyPlan;
	private LinearLayout OneTimeLocal;
	private LinearLayout OnetimeInternation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
        
		setContentView(R.layout.activity_pricing_plan);
		
		MonthlyPlan = (LinearLayout) findViewById(R.id.MonthlyPlan);
		OneTimeLocal = (LinearLayout) findViewById(R.id.LocalPlan);
		OnetimeInternation = (LinearLayout) findViewById(R.id.InternationalPlan);
		
		  ImageButton A_back = (ImageButton)this.findViewById(R.id.btn_back);
			A_back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				// TODO Auto-generated method stub
					onBackPressed();
				finish();
				}
				});
		
		
		
		
		
		MonthlyPlan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(PricingPlan.this, "Monthly Plan", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(PricingPlan.this, MonthlyPlanActivity.class);
				startActivity(i);
			}
		});
		
		OneTimeLocal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(PricingPlan.this, "Local Plan", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(PricingPlan.this, OneTimeLocal.class);
				startActivity(i);
			}
		});
		OnetimeInternation.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(PricingPlan.this, "International Plan", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(PricingPlan.this, OneTimeInternationl.class);
				startActivity(i);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pricing_plan, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
