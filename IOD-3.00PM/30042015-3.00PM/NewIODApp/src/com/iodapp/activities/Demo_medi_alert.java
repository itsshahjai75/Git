package com.iodapp.activities;

import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class Demo_medi_alert extends FragmentActivity {
	
	MediaPlayer mp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
	
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);	
	
		setContentView(R.layout.healthy_hamesha_popup);
		
		
		
		final String medi_list = getIntent().getStringExtra("medilist");
		final String ptname = getIntent().getStringExtra("ptname");
		final String note = getIntent().getStringExtra("note");
		final int pendingintent_id = getIntent().getIntExtra("pendindintent_id", 3);
		
		Log.d("int is===",Integer.toString(pendingintent_id));
		
		
	manageSound("medi");

	try {
	     mp.prepare();
	    } catch (IllegalStateException e) {
	     // TODO Auto-generated catch block
	     e.printStackTrace();
	    } catch (IOException e) {
	     // TODO Auto-generated catch block
	     e.printStackTrace();
	    }
	
					
	             mp.start();
	         	mp.setLooping(true);
	     		
	     	/*/* Turn Screen On and Unlock the keypad when this alert dialog is displayed */
	     		Demo_medi_alert.this.getWindow().addFlags(LayoutParams.FLAG_TURN_SCREEN_ON |LayoutParams.FLAG_SHOW_WHEN_LOCKED | LayoutParams.FLAG_DISMISS_KEYGUARD);
	     		
	     		
	     		/* Creating a alert dialog builder */
	     		AlertDialog.Builder builder = new AlertDialog.Builder(Demo_medi_alert.this);
	     		
	     		
	     		/** Setting title for the alert dialog */
	     		builder.setTitle("Medicine Reminder...");
	     		builder.setIcon(R.drawable.app_icon);
	     		
	     		/** Setting the content for the alert dialog */
	     		builder.setMessage("Dear\n"+ptname+"\nyou have to take Medicines "+medi_list+"\nNotes: "+note);
	     		
	     		/** Defining an OK button event listener */
	     		builder.setNegativeButton(" OK,Thank You ", new OnClickListener() {
	     			@Override
	     			public void onClick(DialogInterface dialog, int which) {
	     				/** Exit application on click OK */
	     				mp.stop();
	     				onDestroy();
	     				
	     				return ;
	     			}						
	     		});
	     		
	     		
	     		builder.setPositiveButton("Cancel Reminder.", new OnClickListener() {
	     			@Override
	     			public void onClick(DialogInterface dialog, int which) {
	     				/** Exit application on click OK */
	     			
								     				 
							Intent i = new Intent(Demo_medi_alert.this,Demo_medi_alert.class);
							
							/** Creating a Pending Intent */
							PendingIntent operation = PendingIntent.getActivity(getBaseContext(), pendingintent_id , i, Intent.FLAG_ACTIVITY_NEW_TASK);
							
							/** Getting a reference to the System Service ALARM_SERVICE */
							AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
							
							alarmManager.cancel(operation);
							/** Alert is set successfully */
							Toast.makeText(getBaseContext(), "Reminder is Canceled successfully not ringing next time",Toast.LENGTH_LONG).show();
									mp.stop();
									
									onDestroy();
	     				
	     				
	     				return ;
	     			}						
	     		});
	     		
	     		
	     		builder.setNeutralButton("Snooze for 10 Min.", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					
						

	    				Intent i = new Intent(Demo_medi_alert.this,Demo_medi_alert.class);
	    				i.putExtra("medilist",medi_list );
						i.putExtra("ptname",ptname );
						i.putExtra("note",note );
					
	    				/** Creating a Pending Intent */
	    				PendingIntent operation2 = PendingIntent.getActivity(getBaseContext(),pendingintent_id+2000 , i, Intent.FLAG_ACTIVITY_NEW_TASK);
	    				
	    				/** Getting a reference to the System Service ALARM_SERVICE */
	    				AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
	    				Log.d("1", "1");
	    				/** Setting an alarm, which invokes the operation at alart_time */
	    		       // alarmManager.set(AlarmManager.RTC_WAKEUP ,cal.getTimeInMillis(),operation2);
	    		        alarmManager.set(AlarmManager.RTC_WAKEUP,Calendar.getInstance().getTimeInMillis()+10*60*1000,operation2);
	    		       
	    		        /** Alert is set successfully *///snoozed for 10 min
	    		     Toast.makeText(getBaseContext(), "Alarm is SNOOZED at "+ Calendar.getInstance().getTime()+10*60*1000,Toast.LENGTH_LONG).show();
	    		        		
	     				
	     				
	     				
	     				
	     				
	     				mp.stop();
	     				onDestroy();
	     				return ;
						
								
						
						
						
					}
				});
	     		
//===creating snooz==================
	     		
	     		
	     			     		
	     		
	     		/** Creating the alert dialog window */
	     		AlertDialog alert = builder.create();
				alert.show();

				alert.setCanceledOnTouchOutside(false);
	    		
	
	
	
	
	
}
		
		
		
		
		
		
		
		
		
		

	public void onBackPressed() {
    
	if(mp != null){
		mp.stop();
		
		finish();
		java.lang.System.exit(0);
	}else{
		
	}
		
		
}	
		
		
	
	
	/** The application should be exit, if the user presses the back button */ 
	@Override
	public void onDestroy() {		
		super.onDestroy();
		if(mp != null){
			mp.stop();
			mp.release();
			
			finish();
			java.lang.System.exit(0);
		}else{
			
			mp.stop();
			mp.release();
			
			finish();
			java.lang.System.exit(0);
			
			
		}  
	}
	
	
	
	protected void manageSound(String theText) {
	    if (mp != null) {
	        mp.reset();
	        mp.release();
	    }
	    if (theText.equals("drink")){
	        mp = MediaPlayer.create(this, R.raw.beep);
	}else if (theText.equals("medi")){
	        mp = MediaPlayer.create(this, R.raw.clock1);
	    }else{
	        mp = MediaPlayer.create(this, R.raw.clock3);
	    }
	    mp.start();
	}
	
	
	
}
