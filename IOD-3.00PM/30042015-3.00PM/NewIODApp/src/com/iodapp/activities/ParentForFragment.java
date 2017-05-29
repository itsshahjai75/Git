package com.iodapp.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class ParentForFragment extends Activity implements OnClickListener {

	
	
	
	private Fragment fragment;
	private Bundle extras;
	private int fragment_id;
	private CharSequence activityTitle;
	private ImageButton back;
	private TextView title;
	private Typeface sysFont;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.parent_fragment);
		
		
		extras = getIntent().getExtras();
		 fragment_id = extras.getInt("Fragment_Code");
		 back = (ImageButton) findViewById(R.id.parent_back_btn);
		 title = (TextView) findViewById(R.id.Fragment_title);
		 setSystemFont();
		
		 back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		call_Fragment();
		
	}

	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
		title.setTypeface(sysFont);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parent_for, menu);
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
	
	private void call_Fragment()
	{
		
		switch (fragment_id) {
		
		case R.id.view_plan:
			Intent i= new Intent(getApplication(),PricingPlan.class);
			startActivity(i);
			activityTitle = "Pricing";
		break;
		

		case R.id.Schedual_app:
			fragment = new HowtoSchedulAppoimtment();
			activityTitle = "Schedule Appointment";
		break;
		
		case R.id.About_Us:
			fragment = new AboutUsFragment();
			activityTitle = "About Us";
			break;
		case R.id.faq:
			fragment = new FAQFragment();
			activityTitle = "F.A.Q";
			break;
		case R.id.term_condition:
			fragment = new TermsConditionFragment();
			activityTitle = "Terms & Conditions";
			break;
		case R.id.contect_us:
			fragment = new ContactUsFragment();
			activityTitle = "Contact Us";
			break;	
		case R.id.hw_work:
			fragment = new HowItWorksFragment();
			activityTitle = "How it works?";
			break;	
		case R.id.privacy_police:
			fragment = new PrivacyPolicyFragment();
			activityTitle = "Privacy Policy";
			break;	
			
		case R.id.Pricing:
			fragment = new ViewOurPlansFragment();
			activityTitle = "Pricing";
		break;
		
		case R.id.Local_Credit_txt:
			fragment = new ViewOurPlansFragment();
			activityTitle = "Pricing";
		break;

		case R.id.Int_Credit_txt:
			fragment = new ViewOurPlansFragment();
			activityTitle = "Pricing";
		break;
		
		case R.id.link1:
			fragment = new TermsConditionFragment();
			activityTitle = "Terms & Conditions";
			break;
			
		case R.id.link2:
			fragment = new PrivacyPolicyFragment();
			activityTitle = "Privacy Policy";
			break;
			
		case R.id.list:
			fragment = new ViewOurPlansFragment();
			activityTitle = "Pricing";
		break;
		
		default:
			break;
		}
		
		
		if(fragment != null)
		{
			FragmentManager Frag_manager = getFragmentManager();
			Frag_manager.beginTransaction().replace(R.id.Paren_layout, fragment).commit();
			setTitle(activityTitle);
			title.setText(activityTitle);
		}
		else
		{
			Log.e("ParentForFragmentActivity", "Error in creating fragment");
		}
		
		
	}
	
	
	
	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.finish();
		super.onBackPressed();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
	}
}
