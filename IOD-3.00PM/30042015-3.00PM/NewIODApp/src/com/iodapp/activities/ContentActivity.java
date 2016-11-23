package com.iodapp.activities;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class ContentActivity extends Activity implements OnClickListener {

	private Typeface sysFont;
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private TextView tv5;
	private TextView tv6;
	private TextView tv7;
	private RelativeLayout aboutus;
	private RelativeLayout how_work;
	private RelativeLayout FAQ;
	private RelativeLayout Terms_Condotion;
	private RelativeLayout Contect_Us;
	private RelativeLayout privacy_police;
	private RelativeLayout Schedule_App;
	
	private MediaController mController;
	
	private Uri uriYouTube;
	private TextView t_pricing;
	private RelativeLayout pricing;
	private TextView title;
	private ImageButton i_back;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_content);
		
		String vedio_url = "https://www.youtube.com/watch?v=SIBMqfxa1FU";
		tv1 = (TextView) findViewById(R.id.t1_1);
		tv2 = (TextView) findViewById(R.id.t1_2);
		tv3 = (TextView) findViewById(R.id.t1_3);
		tv4 = (TextView) findViewById(R.id.t1_4);
		tv5 = (TextView) findViewById(R.id.t1_5);
		tv6 = (TextView) findViewById(R.id.t1_6);
		tv7 = (TextView) findViewById(R.id.t1_7);
		
		aboutus = (RelativeLayout) findViewById(R.id.About_Us);
		how_work = (RelativeLayout) findViewById(R.id.hw_work);
		FAQ = (RelativeLayout) findViewById(R.id.faq);
		Terms_Condotion = (RelativeLayout) findViewById(R.id.term_condition);
		Contect_Us = (RelativeLayout) findViewById(R.id.contect_us);
		privacy_police = (RelativeLayout) findViewById(R.id.privacy_police);
		Schedule_App = (RelativeLayout) findViewById(R.id.Schedual_app);
		
			t_pricing= (TextView) findViewById(R.id.t_pricing);
		
			pricing = (RelativeLayout) findViewById(R.id.Pricing);
			
			 title = (TextView) findViewById(R.id.skipbtn);
			
			mController = new MediaController(this);
		
		 i_back = (ImageButton) findViewById(R.id.i_back);
		 i_back.setOnClickListener(new OnClickListener() {

		 @Override
		 public void onClick(View v) {
		 // TODO Auto-generated method stub

		 finish();
		 }
		 });
		
		setSystemFont();
		
		aboutus.setOnClickListener(this);
		how_work.setOnClickListener(this);
		FAQ.setOnClickListener(this);
		Terms_Condotion.setOnClickListener(this);
		Contect_Us.setOnClickListener(this);
		privacy_police.setOnClickListener(this);
		Schedule_App.setOnClickListener(this);
		pricing.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent j = new Intent(ContentActivity.this,PricingPlan.class);
				startActivity(j);
			}
		});
		
//		pricing.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//				Intent i = new Intent(ContentActivity.this, PricingPlan.class);
//				startActivity(i);
//			}
//		});
		
		
		 
		
	}

	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
		
		tv1.setTypeface(sysFont);
		tv2.setTypeface(sysFont);
		tv3.setTypeface(sysFont);
		tv4.setTypeface(sysFont);
		tv5.setTypeface(sysFont);
		tv6.setTypeface(sysFont);
		tv7.setTypeface(sysFont);
		t_pricing.setTypeface(sysFont);
		title.setTypeface(sysFont);
		
	}
	 
	 @Override
	 protected void onSaveInstanceState(Bundle outState) {
	 super.onSaveInstanceState(outState);
	
	 }
	 
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.content, menu);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
//		if(v == aboutus)
//		{
//			Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
//			
//		}
//		else if (v == pricing) {
//			
//			Toast.makeText(this, "pricing........", Toast.LENGTH_SHORT);
//			
//		}
//		else if (v == how_work) {
//			Toast.makeText(this, "How it works?", Toast.LENGTH_SHORT).show();
//		}
//		else if( v == FAQ)
//		{
//			Toast.makeText(this, "F.A.Q", Toast.LENGTH_SHORT).show();
//		}
//		else if (v == Terms_Condotion) {
//			Toast.makeText(this, "Terms & Condition", Toast.LENGTH_SHORT).show();
//		}
//		else if (v == Contect_Us )
//		{
//			Toast.makeText(this, "Contect Us", Toast.LENGTH_SHORT).show();
//		}
//		else if (v == privacy_police) {
//			Toast.makeText(this, "What is Call Waiting", Toast.LENGTH_SHORT).show();
//		}
//		else if (v == Schedule_App) {
//			Toast.makeText(this, "Schedule Appointment", Toast.LENGTH_SHORT).show();
//		}
//		else if(v == videoView)
//		{
//			Toast.makeText(this, "Start Video", Toast.LENGTH_SHORT).show();
//			
//			
//		}
		
		displayActivity(v.getId());
	}
	
	private void displayActivity(int position)
	{
		  Intent i = 	i = new Intent(getApplicationContext(), ParentForFragment.class);
		 
		switch (position) {
		case R.id.About_Us:
			i.putExtra("Fragment_Code", R.id.About_Us);
			break;
			
		case R.id.hw_work:
			i.putExtra("Fragment_Code", R.id.hw_work);
			break;
			
		case R.id.faq:
			i.putExtra("Fragment_Code", R.id.faq);
			break;
			
		case R.id.term_condition:
			i.putExtra("Fragment_Code", R.id.term_condition);
			break;
		
		case R.id.contect_us:
			i.putExtra("Fragment_Code", R.id.contect_us);
			break;
			
		case R.id.privacy_police:
			i.putExtra("Fragment_Code", R.id.privacy_police);
			break;
		
		case R.id.Schedual_app:
			i.putExtra("Fragment_Code", R.id.Schedual_app);
			break;
		
//		case R.id.Pricing:
//		//	i.putExtra("Fragment_Code", R.id.Pricing);
//			Intent j = new Intent(ContentActivity.this,PricingPlan.class);
//			startActivity(j);
//			break;
		


		default:
			break;
		}
		
		if( i != null && !i.getExtras().isEmpty())
		{
			startActivity(i);
		}
		
			
			
		
	}

	 
}
