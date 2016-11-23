package com.opentok.android.demo.opentoksamples;

import com.iodapp.activities.ConnectionDetector;
import com.iodapp.activities.DashboardActivity;
import com.iodapp.activities.DoctorAvailableActivity;
import com.iodapp.activities.InternetConnectionActivity;
import com.iodapp.activities.LoginActivity;
import com.iodapp.activities.R;
import com.iodapp.activities.RatingDoctor;
import com.iodapp.activities.Sharedpref;
import com.iodapp.activities.Test;
import com.iodapp.util.SetDoctoreAvailable;
import com.iodapp.util.URLSuppoter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.app.KeyguardManager.KeyguardLock;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.OnFinished;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CalendarContract.Reminders;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.opentok.android.AudioDeviceManager;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.BaseAudioDevice.OutputMode;
import com.opentok.android.Publisher.CameraListener;
import com.opentok.android.Session;
import com.opentok.android.demo.multiparty.MySession;
import com.opentok.android.demo.services.ClearNotificationService;
import com.opentok.android.demo.services.ClearNotificationService.ClearBinder;

public class MultipartyActivity extends Activity {

	private static final String LOGTAG = "demo-subclassing";
	public static MySession mSession;
	EditText mMessageEditText;
	public static String timervalue;
	private boolean resumeHasRun = false;

	private boolean mIsBound = false;
	private NotificationCompat.Builder mNotifyBuilder;
	NotificationManager mNotificationManager;
	ServiceConnection mConnection;
	public static Button btncall, btnendcall,btncanclecall;
	public static TextView timeElapsedView;
	public static  MalibuCountDownTimer countDownTimer;
	public static long timeElapsed;
	private static boolean timerHasStarted = false;
	private Button startB;
	
	private boolean remain_timerHasStarted = false;

	public  final long startTime = 960000;
	public  final long interval = 1000;
	
	private   long remain_startTime = 240000;
	private   long remain_interval = 1000;
	
	private boolean endclick = false;
	private int rimain_second;
	private TextView remain_time;
	private MyTimer remain_timer;
	public static TextView drName;
	private String Cheak_remain_time;
	public static String token_id_oncreat_multi;
	private Timer timer;
	private Boolean isInternetPresent = false;
	private ConnectionDetector cd;
	private TimerTask timertask;
	public static String userEnd="";
	
	private boolean is_int_check = false;
	public static String str_drName;
	private Typeface sysFont;
	public ImageButton camera_option;
	public static int cameraCounter = 1;
	
	public static ProgressBar progress_circle;
	public static Boolean isEnd = false;
	public static String DialogMsg = "";

	private ProgressDialog progress_dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Activity.KEYGUARD_SERVICE);
		KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
		lock.disableKeyguard();
		setContentView(R.layout.video_call);
		
		
		progress_dialog = new ProgressDialog(this);
		progress_dialog.setMessage("Please Wait, Loading...");
		
		

//		ActionBar actionBar = getActionBar();
//		actionBar.setHomeButtonEnabled(true);
//		actionBar.setDisplayHomeAsUpEnabled(true);
		Bundle b = getIntent().getExtras();
		
		rimain_second = Integer.parseInt( String.valueOf(b.get("Remain_Second")));
		token_id_oncreat_multi = b.getString("TokenId");
		Log.d("multipat_tokenOncreat",token_id_oncreat_multi);
		
		
		int mi = rimain_second / 60;
		int se = rimain_second - (mi*60);
		
		if(String.valueOf(se).length() == 2)
		{
			
			Cheak_remain_time = "0"+String.valueOf(mi)+":"+String.valueOf(se);
			
		}
		else
		{
			Cheak_remain_time = "0"+String.valueOf(mi)+":0"+String.valueOf(se);
		}

		mSession = new MySession(this);
		cd= new ConnectionDetector(this);
		

		mMessageEditText = (EditText) findViewById(R.id.message);
		Sharedpref.settimerval("0");
		btncall = (Button) findViewById(R.id.sendMessageButton);
		btnendcall = (Button) findViewById(R.id.endcallbutton);
		btncanclecall = (Button) findViewById(R.id.canclecall);
		btncall.setVisibility(View.GONE);
		btncall.setEnabled(false);
		timeElapsedView = (TextView) this.findViewById(R.id.timer);
		camera_option = (ImageButton) findViewById(R.id.btn_Camera);
	
		countDownTimer = new MalibuCountDownTimer(startTime, interval);
		remain_timer = new MyTimer(remain_startTime, remain_interval);
	
		progress_circle = (ProgressBar) findViewById(R.id.progress_circle);
		ViewGroup preview = (ViewGroup) findViewById(R.id.preview);
		mSession.setPreviewView(preview);
		drName = (TextView) this.findViewById(R.id.timertitle);
		remain_time = (TextView) this.findViewById(R.id.timertitle1);
		 str_drName = b.getString("Dr.Name").toString();
		drName.setText(str_drName);
		
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		ViewPager playersView = (ViewPager) findViewById(R.id.pager);
		mSession.setPlayersViewContainer(playersView);
		mSession.setMessageView((TextView) findViewById(R.id.messageView),
				(ScrollView) findViewById(R.id.scroller));
		setSystemFont();
		
		mSession.connect();
		// Toast.makeText(this, Sharedpref.gettimerval() + "...",
		// Toast.LENGTH_LONG).show();
		
		Remain_time_stop_start();
		
		camera_option.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cameraCounter++;
			
//				Toast.makeText(MultipartyActivity.this, "Camera Mode", Toast.LENGTH_SHORT).show();
				mSession.mPublisher.swapCamera();
//			CameraMode();
//		mSession.publish(mSession.mPublisher);
					
					
			}		
				
			
		});
		
	}
	
	public static void BackCamera()
	{
		mSession.mPublisher.setCameraId(CameraInfo.CAMERA_FACING_BACK);
	}
	
	
	public static void FrontCamera()
	{
		mSession.mPublisher.setCameraId(CameraInfo.CAMERA_FACING_FRONT);
	}
	
	public static void CameraMode()
	{
		if((cameraCounter%2)==0)
		{
			BackCamera();
		}
		else
		{
			FrontCamera();
		}
	}
	
	
	
	
	
	private void setSystemFont()
	{
		sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
		
		drName.setTypeface(sysFont);
		timeElapsedView.setTypeface(sysFont);
		btncall.setTypeface(sysFont);
		btnendcall.setTypeface(sysFont);
		
		
		
	}
	
	
	private void RatingDialog()
	{
		
		final Dialog rankDialog = new Dialog(MultipartyActivity.this, R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.customer_dailog);
        rankDialog.setCancelable(false);
       RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
        ratingBar.setRating(2.0f);

        TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
        text.setText(str_drName);

        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
        updateButton.setTextColor(Color.parseColor("#00bfff"));
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            
            	new HttpAsyncTask_cancl_call()
				.execute("http://jsoncdn.webcodeplus.com/TokenData.svc/ExpireTokenDetails/"+token_id_oncreat_multi+"/"+Sharedpref.getcustomerid());
            
            	
            	Sharedpref.setTokenId("");
            	Sharedpref.setTokenkey("");
            	Sharedpref.setsessionkey("");
            	
            	
            	Intent it_login = new Intent(getApplicationContext(),
						DashboardActivity.class);
				Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
						getApplicationContext(), R.anim.animation,
						R.anim.animation2).toBundle();
				startActivity(it_login, bndlanimation);
				finish();  
            	
            	
            	rankDialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it    
        rankDialog.show();    
	}
	    
	
	
	
	   
	
	private void showDialog()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage(DialogMsg).setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
//						progress_dialog.show();
						new HttpAsyncTask_cancl_call()
						.execute("http://jsoncdn.webcodeplus.com/TokenData.svc/ExpireTokenDetails/"+token_id_oncreat_multi+"/"+Sharedpref.getcustomerid());
					
						mSession.SendEndCallSignal("endcall");
						userEnd = "end";
						
						starttime();
						
					RatingDialog();
						
						
						
					}
				  }).setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							
							dialog.cancel();
						}
				  }
			);
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}

	
	
	
	
	private void showDialog_cancle()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage(DialogMsg).setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
//						progress_dialog.show();
						
						new HttpAsyncTask_cancl_call()
						.execute("http://jsoncdn.webcodeplus.com/TokenData.svc/ExpireTokenDetails/"+token_id_oncreat_multi+"/"+Sharedpref.getcustomerid());
						mSession.oncancelcall();
						Log.d("link_oncancle",token_id_oncreat_multi+Sharedpref.getcustomerid());
						
					}
				  }).setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							
							dialog.cancel();
						}
				  }
			);
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}

private class HttpAsyncTask_cancl_call extends AsyncTask<String, Void, String> {
		
		
	
		
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
			Log.d("call_cancledTime",result);
			Sharedpref.setTokenId("");
			Sharedpref.setTokenkey("");
			Sharedpref.setsessionkey("");
			Intent go_dashbord = new Intent (getApplicationContext(),
						DashboardActivity.class);
			startActivity(go_dashbord);
 			
			
//		
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

	
	
	
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if((event.getKeyCode() == KeyEvent.KEYCODE_POWER) || (event.getKeyCode() == KeyEvent.KEYCODE_HOME) )
		{
			return false;
			
		
		}
		
		
		return super.dispatchKeyEvent(event);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (mSession != null) {
			mSession.onPause();
		}

		mNotifyBuilder = new NotificationCompat.Builder(this)
				.setContentTitle(this.getTitle())
				.setContentText(getResources().getString(R.string.notification))
				.setSmallIcon(R.drawable.ic_launcher).setOngoing(true);

		Intent notificationIntent = new Intent(this, MultipartyActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		mNotifyBuilder.setContentIntent(intent);

		if (mConnection == null) {
			mConnection = new ServiceConnection() {
				@Override
				public void onServiceConnected(ComponentName className,
						IBinder binder) {
					((ClearBinder) binder).service.startService(new Intent(
							MultipartyActivity.this,
							ClearNotificationService.class));
					NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					mNotificationManager.notify(
							ClearNotificationService.NOTIFICATION_ID,
							mNotifyBuilder.build());
				}

				@Override
				public void onServiceDisconnected(ComponentName className) {
					mConnection = null;
				}

			};
		}

		if (!mIsBound) {
			bindService(new Intent(MultipartyActivity.this,
					ClearNotificationService.class), mConnection,
					Context.BIND_AUTO_CREATE);
			mIsBound = true;
		}

	}

	@Override
	public void onResume() {
		super.onResume();

		if (mIsBound) {
			unbindService(mConnection);
			mIsBound = false;
		}

		if (!resumeHasRun) {
			resumeHasRun = true;
			return;
		} else {
			if (mSession != null) {
				mSession.onResume();
			}
		}
		mNotificationManager.cancel(ClearNotificationService.NOTIFICATION_ID);
	}

	@Override
	public void onStop() {
		super.onStop();

		if (mIsBound) {
			unbindService(mConnection);
			mIsBound = false;
		}
		if (isFinishing()) {
			mNotificationManager
					.cancel(ClearNotificationService.NOTIFICATION_ID);
			if (mSession != null) {
				mSession.disconnect();
			}
		}
		this.finish();
	}

	@Override
	public void onDestroy() {
		mNotificationManager.cancel(ClearNotificationService.NOTIFICATION_ID);
		if (mIsBound) {
			unbindService(mConnection);
			mIsBound = false;
		}

		if (mSession != null) {
			mSession.disconnect();
		}
		restartAudioMode();

		Sharedpref.setTokenId("");
		Sharedpref.setTokenkey("");
		Sharedpref.setsessionkey("");
		super.onDestroy();
		this.finish();
	}

	
	
	@Override
	public void onBackPressed() {
//		if (mSession != null) {
//			mSession.disconnect();
//
//		}
//		restartAudioMode();
//
//		super.onBackPressed();
	}
	
	

	
	public void onClickSend(View v) {
		
//		Remain_time_stop_start();
		remain_timer.cancel();
		remain_timer = null;
		
		btnendcall.setVisibility(View.GONE);
		btncanclecall.setVisibility(View.GONE);
	
		
		// if(mMessageEditText.getText().toString().compareTo("") == 0){
		// Log.i(LOGTAG, "Cannot Send - Empty String Message");
		// }
		// else{
		Log.i(LOGTAG, "Sending a chat message");
		// mSession.sendChatMessage(mMessageEditText.getText().toString());
		mSession.sendChatMessage("iod");
		// mMessageEditText.setText("");
		// btncall.setEnabled(false);
		// btncall.setVisibility(View.GONE);
		// btnendcall.setVisibility(View.VISIBLE);
		if (Sharedpref.gettimerval().equalsIgnoreCase("1")) {
//		starttime();
//		Toast.makeText(getApplicationContext(), "Click On Call",Toast.LENGTH_SHORT ).show();
		}
		// }
	}

	public  void onClickEnd(View v) {

		DialogMsg = "Do you Want to End Call ?";
		showDialog();
		//endclick = true;
		
//		Toast.makeText(getApplicationContext(), "Click On End Call",Toast.LENGTH_SHORT ).show();
		
//		mSession.SendEndCallSignal("iod");
//		
//		starttime();
		
		// if(mMessageEditText.getText().toString().compareTo("") == 0){
		// Log.i(LOGTAG, "Cannot Send - Empty String Message");
		// }
		// else{
		// Log.i(LOGTAG, "Sending a chat message");
		// // mSession.sendChatMessage(mMessageEditText.getText().toString());
		// mSession.sendChatMessage("iod");
		// // mMessageEditText.setText("");
		// }
		// countDownTimer.cancel();
		// timerHasStarted = false;
//		Intent it_LogIn = new Intent(getApplicationContext(),
//				DashboardActivity.class);
//		Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
//				getApplicationContext(), R.anim.animation,
//				R.anim.animation2).toBundle();
//		startActivity(it_LogIn, bndlanimation);
//		finish();
		

	}
	
	
	public  void onClickCancle(View v) {

		DialogMsg = "Do you Want to Cancle Call ?";
		//mSession.SendEndCallSignal("endcall");
		
		showDialog_cancle();
		
		
		}
	
	
	
	
	private void sendEndCallSignal()
	{
		
		
	
		
		mSession.SendEndCallSignal("endcall");
		starttime();
		
		RatingDialog();
//		Intent it_LogIn = new Intent(getApplicationContext(),
//				DashboardActivity.class);
//		Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
//				getApplicationContext(), R.anim.animation,
//				R.anim.animation2).toBundle();
//		startActivity(it_LogIn, bndlanimation);
//		finish();
		
	}
	
	
	
	
	
	private void finish_On_Remaintime()
	{
//		Remain_time_stop_start();
		remain_timer.cancel();
		remain_timer = null;
		String set_str = "http://jsoncdn.webcodeplus.com/TokenData.svc/ExpireTokenDetails/"+token_id_oncreat_multi+"/"+Sharedpref.getcustomerid();
		Sharedpref.setTokenId("");
		Sharedpref.setsessionkey("");
		Sharedpref.setTokenkey("");
		SetDoctoreAvailable SetDA = new SetDoctoreAvailable();
		SetDA.execute(set_str);
		Intent it_LogIn = new Intent(getApplicationContext(),
				DashboardActivity.class);
		Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
				getApplicationContext(), R.anim.animation,
				R.anim.animation2).toBundle();
		startActivity(it_LogIn, bndlanimation);
		finish();
	}

	
	public void restartAudioMode() {
		AudioManager Audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		Audio.setMode(AudioManager.MODE_RINGTONE);
		this.setVolumeControlStream(AudioManager.ADJUST_RAISE);
		
		
		
		
	}

	// CountDownTimer class
	public class MalibuCountDownTimer extends CountDownTimer {

		public MalibuCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		
		
		
		@Override
		public void onFinish() {
			
			
		}

		
		
		@TargetApi(Build.VERSION_CODES.GINGERBREAD)
		@SuppressLint("NewApi")
		@Override
		public void onTick(long millisUntilFinished) {
			long millis = millisUntilFinished;
			String hms = String.format(
					"%02d:%02d",
					TimeUnit.MILLISECONDS.toMinutes(millis)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
									.toHours(millis)),
					TimeUnit.MILLISECONDS.toSeconds(millis)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes(millis)));
			System.out.println(hms);
			// String settime="03:00";
			// if(settime == hms)
			// {
			// text.setVisibility(View.VISIBLE);
			// text.setText("Time Remain:" + hms);
			// }
//			if (millisUntilFinished == 180000) {
//				// text.setVisibility(View.VISIBLE);
//				// text.setText("Time Remain:" + hms);
//			}
			timeElapsed = startTime - millisUntilFinished;

			long millis1 = timeElapsed;
			String hms1 = String.format(
					"%02d:%02d",
					TimeUnit.MILLISECONDS.toMinutes(millis1)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
									.toHours(millis1)),
					TimeUnit.MILLISECONDS.toSeconds(millis1)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes(millis1)));
			System.out.println(hms1);
			if(hms1.equalsIgnoreCase("12:00"))
			{
				Toast.makeText(MultipartyActivity.this, "You have Remaining only 3 Minuts For Call", Toast.LENGTH_SHORT).show();	
				
//				timeElapsedView.setBackgroundResource(R.drawable.time_red);
				timeElapsedView.setBackgroundColor(Color.RED);
				
			}

			// timeElapsedView.setText("Time Start: " + String.valueOf(hms1));
			timeElapsedView.setText(String.valueOf(hms1));
			
			
			if(hms1.equalsIgnoreCase("15:00"))
			{
				sendEndCallSignal();
			
			}
			
			
			
			
			
			
		}
	}

	public static void starttime() {
		if (!timerHasStarted) {
			countDownTimer.start();
			timerHasStarted = true;
		} else {
			countDownTimer.cancel();
			timerHasStarted = false;
		}
	}
	
	
	
	
	
	
	private class MyTimer extends CountDownTimer
	{

		public MyTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			
			long millis = millisUntilFinished;
			String hms = String.format(
					"%02d:%02d",
					TimeUnit.MILLISECONDS.toMinutes(millis)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
									.toHours(millis)),
					TimeUnit.MILLISECONDS.toSeconds(millis)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes(millis)));
			System.out.println(hms);
			// String settime="03:00";
			// if(settime == hms)
			// {
			// text.setVisibility(View.VISIBLE);
			// text.setText("Time Remain:" + hms);
			// }
			if (millisUntilFinished == 180000) {
				// text.setVisibility(View.VISIBLE);
				// text.setText("Time Remain:" + hms);
			}
			timeElapsed = startTime - millisUntilFinished;

			long millis1 = timeElapsed;
			
			
			String hms1 = String.format(
					"%02d:%02d",
					TimeUnit.MILLISECONDS.toMinutes(millis1)
							- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
									.toHours(millis1)),
					TimeUnit.MILLISECONDS.toSeconds(millis1)
							- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
									.toMinutes(millis1)));
			System.out.println(hms1);
			
			remain_time.setText(hms1.toString());
			if(hms1.equalsIgnoreCase(Cheak_remain_time))
			{
				
				finish_On_Remaintime();
			}
			
		}
		
	}
	
	
	
	private void Remain_time_stop_start()
	{
		if (!remain_timerHasStarted) {
			remain_timer.start();
			remain_timerHasStarted = true;
		} else {
			remain_timer.cancel();
			remain_timerHasStarted = false;
		}
		
	}
	
	
	
	
	
	
}
