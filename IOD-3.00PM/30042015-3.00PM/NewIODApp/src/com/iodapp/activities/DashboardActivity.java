package com.iodapp.activities;




import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.iodapp.util.URLSuppoter;




import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.app.ActivityOptions;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DashboardActivity extends Activity implements OnClickListener {

	boolean dashval=false;
//	private DrawerLayout mDrawerLayout;
//	private ListView mDrawerList;
//	private ActionBarDrawerToggle mDrawerToggle;
	private ImageView UserImage;
	private TextView UserName;
	private TextView Local_Avl_Doctore;
	private TextView Int_Avl_doctore;
	private TextView Local_Credit;
	private TextView Int_Credit;
	private TextView Local_Doc_Text;
	private TextView Int_doc_text;
	private Timer timer = new Timer();
	private String State_Id;
	private String User_id;
	private String Credit_local,Credit_Int;
	private int i;
	private Bundle bndlanimation;
	private ProgressDialog progress_dialog;
	private View Local_doctore;
	private View Int_doctore;
	private View Schedule_appointment;
	private View Call_history;
	private View local_doc_layout;
	private View int_doc_layout;
	private Typeface sysFont;
	private TextView textView1;
	private TextView textView2;
	private TextView sched;
	private TextView callHis;
	private ImageButton userUpdate;
	private ImageLoader imageLoader = Sharedpref.getInstance().getImageLoader();
	private ImageView user_img;
	public static String img_path_user="";
	
	private String user_info = "http://jsoncdn.webcodeplus.com//CustomerData.svc/LoadCustomerDetails/30/"+Sharedpref.getcustomerid();
	
	private NetworkImageView thumbNail;
	
	
	

	// nav drawer title
//	private CharSequence mDrawerTitle;

	// used to store app title
//	private CharSequence mTitle;

	// slide menu items
//	private String[] navMenuTitles;
//	private TypedArray navMenuIcons;

//	private ArrayList<NavDrawerItem> navDrawerItems;
//	private NavDrawerListAdapter adapter;

//	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.new_deshboard);
		
	
	getActionBar().setSplitBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black_grey)));
	getActionBar().setTitle(Html.fromHtml("<font color='#00BFFF'>ActionBartitle</font>"));	
		
	getActionBar().setIcon(R.drawable.appicon);
		
		progress_dialog = new ProgressDialog(this);
		progress_dialog.setMessage("Please Wait, Loading...");
		progress_dialog.show();
		
		UserImage = (ImageView) findViewById(R.id.User_image);
		UserName = (TextView) findViewById(R.id.User_name);
		Local_Avl_Doctore = (TextView) findViewById(R.id.Local_doc_avl_text);
		Int_Avl_doctore = (TextView) findViewById(R.id.Int_doc_avl_text);
		Local_Credit = (TextView) findViewById(R.id.Local_Credit_txt);
		Int_Credit = (TextView) findViewById(R.id.Int_Credit_txt);
		Local_Doc_Text = (TextView) findViewById(R.id.l_doc_txt);
		Int_doc_text = (TextView) findViewById(R.id.Int_doc_txt);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		sched = (TextView) findViewById(R.id.sched);
		callHis = (TextView) findViewById(R.id.callHis);
		
		
		Local_Avl_Doctore.setOnClickListener(this);
		Local_Doc_Text.setOnClickListener(this);
		Int_Avl_doctore.setOnClickListener(this);
		Int_doc_text.setOnClickListener(this);
		
		UserName.setText(Sharedpref.getfirstname().toString());
		State_Id = Sharedpref.getstatecode().toString();
		User_id = Sharedpref.getcustomerid().toString();
		
		
		// find layout for click event
		Local_doctore = findViewById(R.id.Local_layout);
		Int_doctore = findViewById(R.id.Int_layout);
		Schedule_appointment = findViewById(R.id.Shed_layout);
		Call_history = findViewById(R.id.CallHis_layout);
		
		local_doc_layout = findViewById(R.id.Local_doc_layout);
		int_doc_layout = findViewById(R.id.Int_doc_layout);
		userUpdate = (ImageButton) findViewById(R.id.EditProfileImg_Btn);
		user_img = (ImageView) findViewById(R.id.User_image);
		
		Local_doctore.setOnClickListener(this);
		Int_doctore.setOnClickListener(this);
		Schedule_appointment.setOnClickListener(this);
		Call_history.setOnClickListener(this);
		local_doc_layout.setOnClickListener(this);
		int_doc_layout.setOnClickListener(this);
		
		//For User Image get in ImageView Through NetworkImageView
		
		
		
		userUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent updProf=new Intent(DashboardActivity.this,UpdateProfile.class);
				 bndlanimation = 
						ActivityOptions.makeCustomAnimation(DashboardActivity.this, R.anim.animation,R.anim.animation2).toBundle();
				
				
				startActivity(updProf,bndlanimation);
				
			}
		});
		
		
//		Toast.makeText(DashboardActivity.this, "User_ID = "+User_id  ,Toast.LENGTH_SHORT).show();
		
//		startRefreshing();
		
	setSystemFont();	
		
		
	if (imageLoader == null)
		imageLoader = Sharedpref.getInstance().getImageLoader();
	
	 thumbNail = (NetworkImageView) findViewById(R.id.User_image) ;
	
	
	
	
	
//		
}
	
	
	private void showDialog()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage("Sign Out").setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
					
						singOut();
					
						
					}
				  }).setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							
							
							dialog.cancel();
							
						}});
			
 
			
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				
//				alertDialog.getButton(0).setBackgroundColor(Color.BLACK);
				//alertDialog.getButton(1).setBackgroundColor(Color.BLACK);
			
				
 
				// show it
				alertDialog.show();
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#ffffff"));
				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(Color.parseColor("#ffffff"));
				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00bfff"));
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00bfff"));
				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(19.0f);
				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(19.0f);
				alertDialog.getWindow().clearFlags(100);
				
	}
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
		
		UserName.setTypeface(sysFont);
		Local_Avl_Doctore.setTypeface(sysFont);
		Int_Avl_doctore.setTypeface(sysFont);
		Local_Credit.setTypeface(sysFont);
		Int_Credit.setTypeface(sysFont);
		Local_Doc_Text.setTypeface(sysFont);
		Int_doc_text.setTypeface(sysFont);
		textView1.setTypeface(sysFont);
		textView2.setTypeface(sysFont);
		
		sched.setTypeface(sysFont);
		callHis.setTypeface(sysFont);
		
		
	}
	
	private void hidePDialog() {
		if (progress_dialog != null) {
			progress_dialog.dismiss();
			progress_dialog = null;
		}
	}

	private void setUserInfo()
	{
		
		String str = "http://jsoncdn.webcodeplus.com/TokenData.svc/GetAvailableDoctorList/"+State_Id+"/"+User_id;
		new HttpAsyncTask(Integer.valueOf(State_Id)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, str);
		
		GetUserCredit();
		
		
		getInternation_Doc_avl();
		
		new HttpImgAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, user_info);
		
		 
	}
	/**
	 * Slide menu item click listener
	 * */
//	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
		startRefreshing();
		super.onStart();
	}
	
	private void getInternation_Doc_avl()
	{
		
		String str = "http://jsoncdn.webcodeplus.com/TokenData.svc/GetAvailableDoctorList/0/"+User_id;
		new HttpAsyncTask(0).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, str);
		 
	}
	
	private void GetUserCredit()
	{
		String Credit_url  = "http://jsoncdn.webcodeplus.com/CkeckCreditData.svc/GetCreditAvailable/"+User_id+"/0";
		new getCredit().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Credit_url);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.dash_menu, menu);
		
		return true;
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stopRefreshing();
		super.onDestroy();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		stopRefreshing();
		super.onStop();
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
//		if(item.getItemId() == R.id.my_account)
//		{
//			Intent Doc_avl=new Intent(getApplicationContext(),MyAccount.class);
//			 bndlanimation = 
//					ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
//			Doc_avl.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			
//			startActivity(Doc_avl,bndlanimation);
//			
//		}
		
		displayActivity(item.getItemId());
		
		return true;
		
		
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		private URLSuppoter suppoter;
		private int P_State_Id;
		
		public HttpAsyncTask(int P_State_Id) {
			// TODO Auto-generated constructor stub
			this.P_State_Id = P_State_Id;
			
		}
		
		@Override
		protected String doInBackground(String... urls) {

			suppoter = new URLSuppoter();
			return suppoter.GET(urls[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// progressBar.setVisibility(View.VISIBLE);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			// progressBar.setVisibility(View.INVISIBLE);
			try {
				// result = jsonstr;
				
				JSONArray ja = new JSONArray(result);
				int count = ja.length();
				
					if(P_State_Id > 0)
					{
						Local_Avl_Doctore.setText(String.valueOf(count));
						if(count > 0)
						{
							local_doc_layout.setBackgroundColor(getResources().getColor(R.color.green));
//							Local_Avl_Doctore.setBackgroundResource(R.drawable.availdocgreenbg);
//							Local_Doc_Text.setTextColor(getResources().getColor(R.color.green));
						}
						else
						{
							local_doc_layout.setBackgroundColor(getResources().getColor(R.color.red));
//							Local_Avl_Doctore.setBackgroundResource(R.drawable.availdocredbg);
//							Local_Doc_Text.setTextColor(getResources().getColor(R.color.red));
						}
//						Toast.makeText(DashboardActivity.this,"For St_id="+P_State_Id+"Avl Doctore = "+count, Toast.LENGTH_SHORT).show();
					}
					
					if(P_State_Id == 0)
					{
						Int_Avl_doctore.setText(String.valueOf(count));
						if(count > 0)
						{
							int_doc_layout.setBackgroundColor(getResources().getColor(R.color.green));
//							Int_Avl_doctore.setBackgroundResource(R.drawable.availdocgreenbg);
//							Int_doc_text.setTextColor(getResources().getColor(R.color.green));
						}
						else
						{
							
							int_doc_layout.setBackgroundColor(getResources().getColor(R.color.red));
//							Int_Avl_doctore.setBackgroundResource(R.drawable.availdocredbg);
//							Int_doc_text.setTextColor(getResources().getColor(R.color.red));
						}
						
//						Toast.makeText(DashboardActivity.this,"For St_id="+P_State_Id+"Avl Doctore = "+count, Toast.LENGTH_SHORT).show();
					}
					
			
				
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			       
		}

		@Override
		protected void onCancelled() {

			super.onCancelled();
//			Toast toast = Toast.makeText(DashboardActivity.this,
//					"Error is occured due to some probelm", Toast.LENGTH_LONG);
//			toast.show();

		}
	}
	
	
	private class getCredit extends AsyncTask<String, Void, String> {
		private URLSuppoter suppoter;
		
		
		
		
		@Override
		protected String doInBackground(String... urls) {

			suppoter = new URLSuppoter();
			return suppoter.GET(urls[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// progressBar.setVisibility(View.VISIBLE);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			// progressBar.setVisibility(View.INVISIBLE);
			try {
				// result = jsonstr;
				
//				JSONArray ja = new JSONArray(result);
				JSONObject ja = new JSONObject(result);
				
				Credit_local = ja.getString("RemianLoacalCredit");
				Credit_Int = ja.getString("RemainInternationalCredit");
				
				Sharedpref.setLocalCredit(Credit_local);
				
				
				Local_Credit.setText(Credit_local);
				Int_Credit.setText(Credit_Int);
				hidePDialog();
				
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			       
		}

		@Override
		protected void onCancelled() {

			hidePDialog();
			super.onCancelled();
//			Toast toast = Toast.makeText(DashboardActivity.this,
//					"Error is occured due to some probelm", Toast.LENGTH_LONG);
//			toast.show();

		}
	}

	private void singOut()
	{
		Sharedpref.setmail("");
		Sharedpref.setpass("");
		Sharedpref.setfirstname("");
		Sharedpref.setlastname("");
		Sharedpref.setcustomerid("");
		Sharedpref.setstatecode("");
		Intent it_logout=new Intent(getApplicationContext(),LoginActivity.class);
		 bndlanimation = 
				ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
		it_logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		it_logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(it_logout,bndlanimation);
		finish();
	}
	
	private void displayActivity(int position)
	{
		
		switch (position) {
		case R.id.sing_out:
			showDialog();
			
			break;
			
		case R.id.my_account:
			
			Intent myAccount = new Intent(getApplicationContext(), MyAccount.class);
				
			startActivity(myAccount);
			break;

		case R.id.view_plan:
//			ViewOurPlansFragment vpf = new ViewOurPlansFragment();
			
			Intent i = new Intent(getApplicationContext(), PricingPlan.class);
	//		i.putExtra("Fragment_Code", R.id.view_plan);
		
			startActivity(i);
			
			break;

		case R.id.more_option:
			
			Intent it_skip = new Intent(getApplicationContext(),
					ContentActivity.class);
			Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
					getApplicationContext(), R.anim.animation,
					R.anim.animation2).toBundle();
			startActivity(it_skip, bndlanimation);
			
//			Intent updProf=new Intent(DashboardActivity.this,UpdateProfile.class);
//			 bndlanimation = 
//					ActivityOptions.makeCustomAnimation(DashboardActivity.this, R.anim.animation,R.anim.animation2).toBundle();
//			
//			
//			startActivity(updProf,bndlanimation);
			break;

		default:
			break;
		}
		
		
	}
	public void startRefreshing() {
	    final Handler handler = new Handler();

	    TimerTask timertask = new TimerTask() {
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                public void run() {
	                    try {
	                       
	                    	setUserInfo();
	                    } catch (Exception e) {
	                        // TODO Auto-generated catch block
	                    }
	                }
	            });
	        }
	    };
	    timer = new Timer(); 
	    timer.schedule(timertask, 0, 10000); // execute in every 10sec
//	    Toast.makeText(DashboardActivity.this, "Data Start Refresing", Toast.LENGTH_SHORT).show();
	    }

	private void stopRefreshing()
	{
		
		timer.cancel();
//		Toast.makeText(DashboardActivity.this, "Data Stop Refresing", Toast.LENGTH_SHORT).show();
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		if(v == Local_doctore || v == local_doc_layout || v == Local_Avl_Doctore || v == Local_Doc_Text)
		{
			
			if(Integer.valueOf(Local_Credit.getText().toString())>0)
			{
			if(Integer.valueOf(Local_Avl_Doctore.getText().toString()) > 0)
			{
				
				Intent Doc_avl=new Intent(getApplicationContext(),DoctorAvailableActivity.class);
				 bndlanimation = 
						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
				Doc_avl.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Doc_avl.putExtra("Select", 0);
				startActivity(Doc_avl,bndlanimation);
				
//				Toast.makeText(DashboardActivity.this, "Local Available Doctor", Toast.LENGTH_SHORT).show();
			}
			}
			else
			{
				Intent Doc_avl=new Intent(getApplicationContext(),PricingPlan.class);
				 bndlanimation = 
						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
				Doc_avl.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Doc_avl.putExtra("Fragment_Code", R.id.Local_Credit_txt);
				startActivity(Doc_avl,bndlanimation);
			}
		}
		else if(v == Int_doctore || v == int_doc_layout || v == Int_Avl_doctore || v == Int_doc_text)
		{
			if(Integer.valueOf(Int_Credit.getText().toString())>0)
			{
			if(Integer.valueOf(Int_Avl_doctore.getText().toString()) > 0)
			{
				Intent Doc_avl=new Intent(getApplicationContext(),DoctorAvailableActivity.class);
				 bndlanimation = 
						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
				Doc_avl.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Doc_avl.putExtra("Select", 1);
				startActivity(Doc_avl,bndlanimation);
				
//				Toast.makeText(DashboardActivity.this, "International Available Doctor", Toast.LENGTH_SHORT).show();
			}
			}
			else
			{
				
				Intent Doc_avl=new Intent(getApplicationContext(),PricingPlan.class);
				 bndlanimation = 
						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
				Doc_avl.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Doc_avl.putExtra("Fragment_Code", R.id.Int_Credit_txt);
				startActivity(Doc_avl,bndlanimation);
			}
		}
		else if(v == Schedule_appointment)
		{
			
			
			
			
			Intent sch =new Intent(getApplicationContext(),ScheduleAppintment.class);
			 bndlanimation = 
					ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
		
			startActivity(sch,bndlanimation);
			
			
							
			
			
				
		}
		else if (v == Call_history) {
//			Toast.makeText(DashboardActivity.this, "Call History", Toast.LENGTH_SHORT).show();
			
			Intent callHis =new Intent(getApplicationContext(),CallHistory.class);
			 bndlanimation = 
					ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
		
			startActivity(callHis,bndlanimation);
		}
		
		
		
		
		
		
		
		
		
		
//		
//		if(v == Local_Avl_Doctore || v == Local_Doc_Text)
//		{
//			
//			if(Integer.valueOf(Local_Avl_Doctore.getText().toString()) > 0)
//			{
//				
//				Intent Doc_avl=new Intent(getApplicationContext(),DoctorAvailableActivity.class);
//				 bndlanimation = 
//						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
//				Doc_avl.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(Doc_avl,bndlanimation);
//				finish();
//				Toast.makeText(DashboardActivity.this, "Local Available Doctor", Toast.LENGTH_SHORT).show();
//			}
//			
//		}
//		else if (v == Int_Avl_doctore || v == Int_doc_text) 
//		{
//			if(Integer.valueOf(Int_Avl_doctore.getText().toString()) > 0)
//			{
//				
//				Intent Doc_avl=new Intent(getApplicationContext(),DoctorAvailableActivity.class);
//				 bndlanimation = 
//						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation,R.anim.animation2).toBundle();
//				Doc_avl.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(Doc_avl,bndlanimation);
//				finish();
//				Toast.makeText(DashboardActivity.this, "International Available Doctor", Toast.LENGTH_SHORT).show();
//			}
//		}
	}
	
	
	
	private class HttpImgAsyncTask extends AsyncTask<String, Void, String> {
		private URLSuppoter suppoter;
		private int P_State_Id;
		
		
		
		@Override
		protected String doInBackground(String... urls) {

			suppoter = new URLSuppoter();
			return suppoter.GET(urls[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// progressBar.setVisibility(View.VISIBLE);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			String userImageName = "";
			// progressBar.setVisibility(View.INVISIBLE);
			try {
				// result = jsonstr;
				
				JSONObject obj = new JSONObject(result);
				
				userImageName = obj.getString("ExtraInfo").toString();
				
				String img_path = "http://cms.ionlinedoctor.com/CustomerData/"+userImageName;
				thumbNail.setImageUrl(img_path, imageLoader);
				
				img_path_user += img_path;
				
				Sharedpref.setImagePath(img_path);
					
			
				
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			       
		}

		@Override
		protected void onCancelled() {

			super.onCancelled();
//			Toast toast = Toast.makeText(DashboardActivity.this,
//					"Error is occured due to some probelm", Toast.LENGTH_LONG);
//			toast.show();

		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("EXIT", true);
		startActivity(intent);
		System.exit(0);
		finish();
		
	}
	
}
