package com.iodapp.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyAccount extends Activity {

	private TextView myaccount;
	private TextView t1;
	private TextView t2;
	private TextView t3;
	private TextView t4;
	private RelativeLayout upload_profile;
	private RelativeLayout additional_detail;
	private RelativeLayout call_history;
	private RelativeLayout order_history;
	private ImageButton back;
	private RelativeLayout myAppointment;
	protected Bundle bndlanimation;
	private TextView t5;
	private Typeface sysFont;
	private RelativeLayout paymentMethod,change_pwd;
	private RelativeLayout my_plan;
	private TextView t6;
	private TextView t7,t8;
	SharedPreferences data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
		setContentView(R.layout.activity_my_account);
		
		myaccount = (TextView) findViewById(R.id.my_account);

		t1 = (TextView)findViewById(R.id.upload);
		t2 = (TextView) findViewById(R.id.addi);
		t3 = (TextView) findViewById(R.id.call_his);
		t4 = (TextView) findViewById(R.id.order_his);
		t5 = (TextView) findViewById(R.id.addi12);
		t6 = (TextView) findViewById(R.id.addi123);
		t7 = (TextView) findViewById(R.id.addi1234);
		t8= (TextView) findViewById(R.id.chng_pwd);
		
		upload_profile = (RelativeLayout) findViewById(R.id.Upload_profile);
		additional_detail = (RelativeLayout) findViewById(R.id.Additional);
		call_history = (RelativeLayout) findViewById(R.id.Call_history);
		order_history = (RelativeLayout) findViewById(R.id.order_history);
		myAppointment = (RelativeLayout) findViewById(R.id.my_appointmnet);
		paymentMethod = (RelativeLayout) findViewById(R.id.my_Payment_Method);
		my_plan = (RelativeLayout) findViewById(R.id.my_plan);
		change_pwd = (RelativeLayout) findViewById(R.id.change_pwd);
		
		
		change_pwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent j = new Intent(MyAccount.this,Change_pwd.class);
				startActivity(j);
				finish();
				
				
			}
		});
		
		
		
		back = (ImageButton) findViewById(R.id.btn_back_myaccount);
		
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
				finish();
			}
		});
		
		my_plan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent appoint  = new Intent(MyAccount.this, UnsuscribePlan.class);
				 bndlanimation = 
							ActivityOptions.makeCustomAnimation(MyAccount.this, R.anim.animation,R.anim.animation2).toBundle();
				
				startActivity(appoint, bndlanimation);
				//finish();
				
			}
		});
		
		data =  PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		additional_detail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent appoint  = new Intent(MyAccount.this, Helathprofile.class);
				 bndlanimation = 
							ActivityOptions.makeCustomAnimation(MyAccount.this, R.anim.animation,R.anim.animation2).toBundle();
				
				 data.edit().putString("skipshow", "yes").commit();
					
				startActivity(appoint, bndlanimation);
				finish();
				
			}
		});
		
 paymentMethod.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent appoint  = new Intent(MyAccount.this, TransMoreRemove.class);
				 bndlanimation = 
							ActivityOptions.makeCustomAnimation(MyAccount.this, R.anim.animation,R.anim.animation2).toBundle();
				
				startActivity(appoint, bndlanimation);
				finish();
				
			}
		});
		
upload_profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent appoint  = new Intent(MyAccount.this, UpdateProfile.class);
				 bndlanimation = 
							ActivityOptions.makeCustomAnimation(MyAccount.this, R.anim.animation,R.anim.animation2).toBundle();
				
				startActivity(appoint, bndlanimation);
				finish();
			
				
			}
		});
		
		myAppointment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent appoint  = new Intent(MyAccount.this, DeleteAppointment.class);
				 bndlanimation = 
							ActivityOptions.makeCustomAnimation(MyAccount.this, R.anim.animation,R.anim.animation2).toBundle();
				
				startActivity(appoint, bndlanimation);
				
			}
		});
		
		order_history.setOnClickListener(new OnClickListener() {
			
		

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent ordHis =new Intent(MyAccount.this,OrderHistory.class);
				 bndlanimation = 
						ActivityOptions.makeCustomAnimation(MyAccount.this, R.anim.animation,R.anim.animation2).toBundle();
			
				startActivity(ordHis,bndlanimation);
				
			}
		});
		
		call_history.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent ordHis =new Intent(MyAccount.this,CallHistory.class);
				 bndlanimation = 
						ActivityOptions.makeCustomAnimation(MyAccount.this, R.anim.animation,R.anim.animation2).toBundle();
			
				startActivity(ordHis,bndlanimation);
				
			}
		});
		
		setSystemFont();
		
	}
	
	 private void setSystemFont()
	 {
	 sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");

	 myaccount.setTypeface(sysFont);
	 t1.setTypeface(sysFont);
	 t2.setTypeface(sysFont);
	 t3.setTypeface(sysFont);
	 t4.setTypeface(sysFont);
	 t5.setTypeface(sysFont);
	 t6.setTypeface(sysFont);
	 t7.setTypeface(sysFont);



	 }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_account, menu);
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
