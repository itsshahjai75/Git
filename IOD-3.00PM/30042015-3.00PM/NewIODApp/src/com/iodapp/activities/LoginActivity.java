package com.iodapp.activities;




import com.iodapp.util.URLSuppoter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import android.view.View.OnClickListener;

public class LoginActivity extends Activity  {

	private ProgressDialog progress_dialog;
	RelativeLayout btnlogin;
	Button btnskip;
	TextView btncreateaccount;
	TextView btnforgetpass;
	Button btnreg;
	EditText inputEmail;
	EditText inputPassword;
	TextView loginErrorMsg;

	EditText emailEditText;
	EditText passEditText;
	
	TextView etResponse;
	CheckBox chebox1,chebox2;
	 // flag for Internet connection status
    Boolean isInternetPresent = false;
    final Context context = this;
    // Connection detector class
    ConnectionDetector cd;
    String email;
    String pass;
    String str = "";
	String str1 = "";
	String cid = "";
	String statecode = "";
	String mail = "";
	String fnm = "";
	String lnm = "";
//	String str1 = "";
//	String str1 = "";
	private Typeface sysFont;
	private TextView header_tv;
	private EditText AllredyReg;
	private ImageButton back_button;
	private TextView tour;
	private AlertDialog alert1;
	private AlertDialog alertDialog;
	private TextView textsigin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//int flag, int mask  
		setContentView(R.layout.new_ligin);
		
		progress_dialog = new ProgressDialog(this);
		progress_dialog.setMessage("Please Wait");
		
		
		if(Sharedpref.getloginStatus())
		{
		
			
			
		}
		else
		{
		
		
		
			if((Sharedpref.getmail()  != null && Sharedpref.getmail().equalsIgnoreCase(""))  && 
					(Sharedpref.getpass()  != null && Sharedpref.getpass().equalsIgnoreCase("")))
			{
				
			}else
			{
//				Intent it_skip = new Intent(getApplicationContext(),
//						DoctorAvailableActivity.class);
				Intent it_skip = new Intent(getApplicationContext(),
						DashboardActivity.class);
				
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_skip, bndlanimation);
				finish();
				
			}
		}
		
		 tour=(TextView)findViewById(R.id.btntour);
		 tour.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent i = new Intent(LoginActivity.this,FirstRun.class);
				startActivity(i);
				
				
			}
		});
		
			
		
		// creating connection detector class instance
        cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();
        etResponse=(TextView)findViewById(R.id.textView1);
        header_tv=(TextView)findViewById(R.id.textView2);
        //titel =(TextView)findViewById(R.id.textView3);
        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
//        	Toast.makeText(getApplicationContext(), "You have internet connection", Toast.LENGTH_LONG).show();
        } else {
            // Internet connection is not present
//        	Toast.makeText(getApplicationContext(), "You don't have internet connection", Toast.LENGTH_LONG).show();
        	
        	Intent it_skip = new Intent(getApplicationContext(),
					InternetConnectionActivity.class);
			Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
					getApplicationContext(), R.anim.animation,
					R.anim.animation2).toBundle();
			startActivity(it_skip, bndlanimation);
			finish();
        }
		btnlogin = (RelativeLayout) findViewById(R.id.ubtncreateacc);
		textsigin = (TextView) findViewById(R.id.Signintext);
		
		
		
		btnskip = (Button) findViewById(R.id.btnskip);
		btnforgetpass = (TextView) findViewById(R.id.btnforgetpass);
		btncreateaccount = (TextView) findViewById(R.id.btncreateaccount);

		emailEditText = (EditText) findViewById(R.id.editemail);
		passEditText = (EditText) findViewById(R.id.editpass);
		back_button = (ImageButton) findViewById(R.id.btn_back);
		
		
		 AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
	        builder1.setMessage("Please agree with our Terms & Conditions, Privacy Policy & Legal Adult Status to Continue.")
	               .setCancelable(false)
	               .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {

	                	   
	                	   LoginDialog();
	                	   dialog.cancel();
	                	   
	                	   
	                   }
	               })
	               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                        dialog.cancel();
	                      //  finish();
	                   }
	               });
	      alert1 = builder1.create();

		
		
		
		
		
		
		
		
		back_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			
				
			}
		});
		
		


//		chebox1=(CheckBox)findViewById(R.id.checkBox1);
//		chebox2=(CheckBox)findViewById(R.id.checkBox2);
		
		setSystemFont();
		
		btnlogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

//				if(chebox1.isChecked() && chebox2.isChecked())
//				{
//					Toast.makeText(getApplicationContext(), "Succress", Toast.LENGTH_LONG).show();
//				}else
//				{
////					Toast.makeText(getApplicationContext(), "Pls Check Box click", Toast.LENGTH_LONG).show();
//				}
				
				Sharedpref.setlogoutStatus(true);
				  email = emailEditText.getText().toString();
				  
				if (!isValidEmail(email)) {
					emailEditText.setError("Invalid Email");
					emailEditText.requestFocus();
				}

				  pass = passEditText.getText().toString();
				if (!isValidPassword(pass)) {
					passEditText.setError("Invalid Password");
					passEditText.requestFocus();
				}
				
				if(!isValidEmail(email) && !isValidPassword(pass))
				{
					emailEditText.requestFocus();
				}
				
				if(isValidEmail(email) && isValidPassword(pass))
				{
					
					Sharedpref.setloginStatus(false);
					 
//					showDialog();
					LoginDialog();
					
//							
					
				}
				
			}
		});

		btnskip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Sharedpref.setlogoutStatus(false);
				Intent it_skip = new Intent(getApplicationContext(),
						ContentActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_skip, bndlanimation);
			}
		});
		btnforgetpass.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it_forgetpass = new Intent(getApplicationContext(),
						ForgetPasswordActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_forgetpass, bndlanimation);
				finish();
			}
		});
		btncreateaccount.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Intent it_createacc = new Intent(getApplicationContext(),
						CreateAccountActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_createacc, bndlanimation);
				finish();
			}
		});
	}

	// validating email id
	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
		
		header_tv.setTypeface(sysFont);
		emailEditText.setTypeface(sysFont);
		passEditText.setTypeface(sysFont);
		btnforgetpass.setTypeface(sysFont);
		btncreateaccount.setTypeface(sysFont);
		btnskip.setTypeface(sysFont);
		textsigin.setTypeface(sysFont);
		//titel.setTypeface(sysFont);
		
		
	}

	// validating password with retype password
	private boolean isValidPassword(String pass) {
		if (pass != null && pass.length() > 0) {
			return true;
		}
		return false;
	}
	


	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		
		
	
		
		private URLSuppoter SuppURL;
		

		@Override
		protected String doInBackground(String... urls) {

			
				  SuppURL = new URLSuppoter();
				  return SuppURL.GET(urls[0]);
			//return GET(urls[0]);
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			progressBar.setVisibility(View.VISIBLE);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			
			progress_dialog.cancel();
//			progressBar.setVisibility(View.INVISIBLE);
			try {
				JSONObject json = new JSONObject(result);
				// etResponse.setText(json.toString(1));
				// JSONObject json = new JSONObject(result);
				
				
				str = json.getString("Username");
				str1 = json.getString("Password");
				Sharedpref.setpass(str1);
				
				cid = json.getString("CustomerID");
				Sharedpref.setcustomerid(cid);
				
				statecode = json.getString("State");
				Sharedpref.setstatecode(statecode);
				
				mail = json.getString("EmailID");
				Sharedpref.setmail(mail);
				
				fnm  = json.getString("FirstName");
				Sharedpref.setfirstname(fnm);
				
				lnm = json.getString("LastName");
				Sharedpref.setlastname(lnm);
				
				Spanned sp = Html.fromHtml(str);
				String unm=String.valueOf(sp);
//				Log.d("strum2", ""+unm);
				etResponse.setText(sp);
//				Log.d("strum1", ""+sp);
//				Log.d("strum3", ""+email);
				if(email.equalsIgnoreCase(unm))
				{
//					Intent it_login = new Intent(getApplicationContext(),
//							DoctorAvailableActivity.class);
					
					Intent it_login = new Intent(getApplicationContext(),
							DashboardActivity.class);
					Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
							getApplicationContext(), R.anim.animation,
							R.anim.animation2).toBundle();
					startActivity(it_login, bndlanimation);
//					Toast toast=Toast.makeText(getApplicationContext(), "Success - Redorect to Dashboared", Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
//					toast.show();
//					hidePDialog();
					finish();
					
				}else
				{
					
					Sharedpref.setmail("");
					Sharedpref.setpass("");
					emailEditText.setError("Email Does Not Exist.");
					emailEditText.requestFocus();
					passEditText.setError("Password wrong");
										
//					Toast toast=Toast.makeText(getApplicationContext(), "Error Sorry. invalid Username/Password ", Toast.LENGTH_LONG);
//					toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
//					toast.show();
//					hidePDialog();
				}
				
//				 etResponse.setText(json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
		}

		@Override
		protected void onCancelled() {

			progress_dialog.cancel();
			super.onCancelled();
			Toast toast = Toast.makeText(getApplicationContext(),
					"Error is occured due to some probelm", Toast.LENGTH_LONG);
			toast.show();

		}
	}
	
	
	private void LoginDialog()
	{
		final Dialog rankDialog = new Dialog(LoginActivity.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.dialog_login);
        rankDialog.setCancelable(false);
       

        TextView text = (TextView) rankDialog.findViewById(R.id.link1);
        text.setText(Html.fromHtml("<a href = \"  Terms &amp; Conditions.\"> Terms &amp; Conditions.</a> "+"AND"));
        TextView text2 = (TextView) rankDialog.findViewById(R.id.link2);
        text2.setText(Html.fromHtml("<a href = \"   Privacy Policy.\"> Privacy Policy.</a> "));
      //  text2.setTextColor(Color.BLACK);
        
        TextView text3 = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        TextView text4 = (TextView) rankDialog.findViewById(R.id.ret);
        
        text.setTypeface(sysFont);
        text2.setTypeface(sysFont);
        text3.setTypeface(sysFont);
        text4.setTypeface(sysFont);
        
        text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(Feedback.this, "You Click On HyperLInk", Toast.LENGTH_SHORT).show();
				
				
				Intent it_login = new Intent(getApplicationContext(),
						ParentForFragment.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				
				it_login.putExtra("Fragment_Code", R.id.link1);
				startActivity(it_login, bndlanimation);
				
//				rankDialog.dismiss();
				
				
			}
		});
        
        
   text2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(Feedback.this, "You Click On HyperLInk", Toast.LENGTH_SHORT).show();
				
				
				Intent it_login = new Intent(getApplicationContext(),
						ParentForFragment.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				
				it_login.putExtra("Fragment_Code", R.id.link2);
				startActivity(it_login, bndlanimation);
				
//				rankDialog.dismiss();
				
				
			}
		});

        Button updateButton = (Button) rankDialog.findViewById(R.id.Disagree);
        updateButton.setTextColor(Color.parseColor("#00bfff"));
        updateButton.setTypeface(sysFont);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	

				alert1.show();
				alert1.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#ffffff"));
				alert1.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(Color.parseColor("#ffffff"));
				
				alert1.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00bfff"));
				alert1.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00bfff"));
				alert1.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(19.0f);
				alert1.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(19.0f);
				alert1.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(sysFont);
				alert1.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(sysFont);
            	
            	
            	rankDialog.dismiss();
            }
            
           
        }
        
        
        		
        		);
        Button agree = (Button) rankDialog.findViewById(R.id.Agree);
        agree.setTextColor(Color.parseColor("#00bfff"));
        agree.setTypeface(sysFont);
        agree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progress_dialog.show();
				new HttpAsyncTask()
				.execute("http://jsoncdn.webcodeplus.com/CustomerData.svc/CustomerLogin/30/"+email+"/"+pass);
			
				rankDialog.cancel();
			}
		});
        
        //now that the dialog is set up, it's time to show it    
        rankDialog.show();    
	}
//	private void showDialog()
//	{
//		
//		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//				context);
// 
//		
//			// set title
////			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
//			
//			// set dialog message
//			alertDialogBuilder
//			.setMessage(Html.fromHtml(" 1. By Logging into and/or Registering New Account with I  Online Doctor, I accept all I  Online <a href = \"  Terms and Conditions and Privacy Policy.\">  Terms and Conditions and Privacy Policy.</a>      " +
//					"\t" +
//					"\t" +
//					"\t" +
//					"\t" +
//					"\t" +
//					"\t" +
//					"\t" +
//					"\t" +
//					"\t" +
//					"\t" +
//					"\t" +
//					"\t" +
//					"     2. I am a legal Adult and I confirm that I am 18 years or over. "  )) 		
//					.setPositiveButton("Agree",new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,int id) {
//						// if this button is clicked, close
//						// current activity
////						progress_dialog.show();
//						
//							new HttpAsyncTask()
//							.execute("http://jsoncdn.webcodeplus.com/CustomerData.svc/CustomerLogin/30/"+email+"/"+pass);
//						
////							Toast.makeText(LoginActivity.this, "Check Your Internet..", Toast.LENGTH_SHORT).show();
//						
//						
//					}
//				  })
//				.setNegativeButton("Disagree",new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog,int id) {
//						// if this button is clicked, just close
//						// the dialog box and do nothing
//		
//						alert1.show();
//						alert1.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#ffffff"));
//						alert1.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(Color.parseColor("#ffffff"));
//						
//						alert1.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00bfff"));
//						alert1.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00bfff"));
//						alert1.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(19.0f);
//						alert1.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(19.0f);
//						alert1.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(sysFont);
//						alert1.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(sysFont);
//						
//						
//					
//					}
//				});
//			
//			
//			
//			
// 
//				// create alert dialog
//					alertDialog = alertDialogBuilder.create();
//					
//					// show it
//					alertDialog.show();
//					
//					((TextView)alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
////					((TextView)alertDialog.findViewById(android.R.id.message)).setOnClickListener(new OnClickListener() {
////						
////						@Override
////						public void onClick(View v) {
////							// TODO Auto-generated method stub
////						
////
////						}
////					});
//
//				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#ffffff"));
//				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(Color.parseColor("#ffffff"));
//				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00bfff"));
//				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00bfff"));
//				alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(19.0f);
//				alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(19.0f);
//				 alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(sysFont);
//
//				 alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(sysFont);
//
//	}
	
	private void hidePDialog() {
		if (progress_dialog != null) {
			progress_dialog.dismiss();
			progress_dialog = null;
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
