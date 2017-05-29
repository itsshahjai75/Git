package com.iodapp.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;







import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.iodapp.activities.CircularProgressBar.ProgressAnimationListener;
import com.iodapp.util.URLSuppoter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class CallWaiting extends Activity {

	private String Doctor_Name,Doc_Id;
	private TextView name_titel;
	private ImageButton back_button;
	private String url_waiting;
	private TextView queuetext;
	private Timer timer;
	private TextView clock_view;
	private Button Waiting_Cancel;
	
	public String tokenid;
	 final Handler handler = new Handler();
	private TimerTask timertask;
	private String img_path;
	
	
	private ImageLoader imageLoader = Sharedpref.getInstance().getImageLoader();
	private String dr_rate="0";
	private RatingBar dr_rating;
	private ImageView Img_Dr;
	private int Frg_Code;
	private MediaPlayer mp;
	private final String ganrateTokenUrl = "http://jsoncdn.webcodeplus.com/TokenData.svc/AddTokenForMedia";
	int i=-1;
	int oldQueno;
	CircularProgressBar mProgressBar;
	private TextView textViewShowTime; // will show the time
	private CountDownTimer countDownTimer; // built in android class
	                                        // CountDownTimer
	private long totalTimeCountInMilliseconds,seconds; // total count down time in
	                                            // milliseconds
	private long timeBlinkInMilliseconds,timeCheckAgainQueNo; // start time of start blinking
	private boolean blink; // controls the blinking .. on and off

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
		setContentView(R.layout.activity_call_waiting);
		
			// Get Waiting list data url
		//http://jsoncdn.webcodeplus.com/CallWatingData.svc/CallWaitingRequest/DoctorID/CustomerID
		
			//Cancel callwaiting url
		//http://jsoncdn.webcodeplus.com/CallWatingData.svc/CancelCallRequest/CustomerId
	
		
		Doctor_Name = getIntent().getExtras().getString("Dr.Name");
		Doc_Id = getIntent().getExtras().getString("drID");
		img_path = getIntent().getExtras().getString("imagePath");
		dr_rate = getIntent().getExtras().getString("Dr_rating");
		Frg_Code = getIntent().getExtras().getInt("frag_code");
		
		
		
		Img_Dr = (ImageView) findViewById(R.id.thumbnailcallWait);
		name_titel = (TextView) findViewById(R.id.title);
//		back_button = (ImageButton) findViewById(R.id.btn_back_callWaiting);
		queuetext = (TextView)	findViewById(R.id.Queue_tv);
		clock_view = (TextView) findViewById(R.id.textclock);
		Waiting_Cancel = (Button) findViewById(R.id.cancel_waiting);
		
		dr_rating = (RatingBar) findViewById(R.id.Dr_rating123);
	
		name_titel.setText(Doctor_Name);
		dr_rating.setRating(Float.parseFloat(dr_rate));
		
		if (imageLoader == null)
			imageLoader = Sharedpref.getInstance().getImageLoader();
		
		NetworkImageView thumbNail = (NetworkImageView) findViewById(R.id.thumbnailcallWait);
		
		
		
		thumbNail.setImageUrl(img_path, imageLoader);
			
		
		textViewShowTime = (TextView) findViewById(R.id.tvTimeCount);

	    mProgressBar = (CircularProgressBar) findViewById(R.id.progressbar);
	    
	    
		
		
			
//		
//		back_button.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				finish();
//				onBackPressed();
//				
//			}
//		});
//		
		Waiting_Cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
				
			}
		});
		
		get_Waiting_List_data_firstcall();
		
		   mProgressBar.animateProgressTo(0,(int) (totalTimeCountInMilliseconds / 1000)-(int)seconds , new ProgressAnimationListener(){
   			
   			@Override
   			public void onAnimationStart() {				
   			}
   			
   			@Override
   			public void onAnimationProgress(int progress) {
   				//mProgressBar.setTitle(progress + "%");
   			}
   			
   			@Override
   			public void onAnimationFinish() {
   				//mProgressBar.setSubTitle("done");
   			}
   		});
		
		
	}
	////////////////////////////////////////////////////////////////////////////////////
	
	
	

private void setTimer(int queno) {
    long time = 0;
    if (queno == 0) {
        time = (long)0.30;
     
        System.out.println("GOT QUE no========1");
        
    } else{
    	
    	time = queno*21;
        System.out.println("GOT QUE no----"+queno);
        Toast.makeText(CallWaiting.this, "No que no found...",
                Toast.LENGTH_LONG).show();

    }

    totalTimeCountInMilliseconds = 60 * time * 1000;

    timeBlinkInMilliseconds = 50 * 1000;
    
    
   
}

private void startTimer() {
    countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
        // 500 means, onTick function will be called at every 1 SEC
        // milliseconds

        @Override
        public void onTick(long leftTimeInMilliseconds) {
             seconds = leftTimeInMilliseconds / 1000;
            //i++;
            //Setting the Progress Bar to decrease wih the timer
            mProgressBar.setMax((int) totalTimeCountInMilliseconds/1000);
            mProgressBar.setProgress((int) (leftTimeInMilliseconds / 1000));
         
          
            textViewShowTime.setTextAppearance(getApplicationContext(),
                    R.style.AppTheme);
            
            get_Waiting_List_data();
            
            if (leftTimeInMilliseconds < timeBlinkInMilliseconds) {
                textViewShowTime.setTextAppearance(getApplicationContext(),
                        R.style.ActionBarTabStyle_Example);
                // change the style of the textview .. giving a red
                // alert style

                if (blink) {
                	
                	
                    textViewShowTime.setVisibility(View.VISIBLE);
                    playSound();
                    
                    // if blink is true, textview will be visible
                } else {
                    textViewShowTime.setVisibility(View.INVISIBLE);
                }

                blink = !blink; // toggle the value of blink
            }

            textViewShowTime.setText(String.format("%02d", seconds / 60)
                    + ":" + String.format("%02d", seconds % 60));
            // format the textview to show the easily readable format

        }

        @Override
        public void onFinish() {
            // this function will be called when the timecount is finished
            textViewShowTime.setText("Time up!");
            
            textViewShowTime.setVisibility(View.VISIBLE);
            mp.stop();
            mp.release();
           
            countDownTimer.cancel();
        
           create_call();
        }

    }.start();

}
	
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////////////////

	private void showDialog()
	{
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
 
		
			// set title
//			alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
			
			// set dialog message
			alertDialogBuilder
				.setMessage("Stop Waiting ? ").setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						//stopRefreshing();
						countDownTimer.cancel();
						cancelCallWaiting();
					
						
					}
				  }).setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							
							
							dialog.cancel();
							
						}});
			
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
	}
	
	
	private void get_Waiting_List_data()
	{
		url_waiting = "http://jsoncdn.webcodeplus.com/CallWatingData.svc/CallWaitingRequest/"+Doc_Id+"/"+Sharedpref.getcustomerid();
		new CallWaitAsynTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url_waiting);
	}
	
	
	private void get_Waiting_List_data_firstcall()
	{
		url_waiting = "http://jsoncdn.webcodeplus.com/CallWatingData.svc/CallWaitingRequest/"+Doc_Id+"/"+Sharedpref.getcustomerid();
		new CallWaitAsynTask_firstcall().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url_waiting);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		stopRefreshing();
//		this.finish();
//		super.onBackPressed();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.call_waiting, menu);
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
	private void playSound()
	{
		
				mp = MediaPlayer.create(CallWaiting.this, R.raw.beep);
			
				  mp.start();
				 
			
	}
	
	private class CallWaitAsynTask extends AsyncTask<String, Void, String>
	{
		private URLSuppoter Supporter = new URLSuppoter();
		private String queue,waiting_time;
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			return Supporter.GET(params[0]);
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			try {
				
				JSONObject waitingJson = new JSONObject(result);
				queue = waitingJson.getString("QueNumber");
				waiting_time = waitingJson.getString("WaitingTime");
				
		
		if(Integer.parseInt(queue)<oldQueno){
					
			countDownTimer.cancel();
			System.out.println("quechanged===="+oldQueno+"==to=="+Integer.parseInt(queue));	
			
				oldQueno=Integer.parseInt(queue);
				Intent repeat = new Intent(CallWaiting.this,CallWaiting.class);
				startActivity(repeat);
				finish();
				}else{
				
				}
					
				
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}catch (Exception exp) {
				// TODO: handle exception
				exp.printStackTrace();
			}
		}
		
		
	}
	
	
	private class CallWaitAsynTask_firstcall extends AsyncTask<String, Void, String>
	{
		private URLSuppoter Supporter = new URLSuppoter();
		private String queue,waiting_time;
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			return Supporter.GET(params[0]);
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			try {
				
				JSONObject waitingJson = new JSONObject(result);
				queue = waitingJson.getString("QueNumber");
				waiting_time = waitingJson.getString("WaitingTime");
				
				oldQueno=Integer.parseInt(queue);
				
				queuetext.setText("You are number "+queue+" in queue.");
				clock_view.setText(waiting_time);
				
				System.out.println("Que no in Service-----"+queue);
				
				
				
				
				setTimer(Integer.parseInt(queue));
				startTimer();
				
				
				
				/*if(waiting_time.equalsIgnoreCase("00:00:00"))
				{
					
					handler.removeCallbacks(timertask);
			
					create_call();
					
				}
				
				if(waiting_time.equalsIgnoreCase("00:02:00") || waiting_time.equalsIgnoreCase("00:01:00") ||
						waiting_time.equalsIgnoreCase("00:00:59") || waiting_time.equalsIgnoreCase("00:00:55") ||
						waiting_time.equalsIgnoreCase("00:00:50")||waiting_time.equalsIgnoreCase("00:00:45")||
						waiting_time.equalsIgnoreCase("00:00:40")||waiting_time.equalsIgnoreCase("00:00:35")||
						waiting_time.equalsIgnoreCase("00:00:30")||waiting_time.equalsIgnoreCase("00:00:25")||
						waiting_time.equalsIgnoreCase("00:00:20")||waiting_time.equalsIgnoreCase("00:00:15"))
				{
					clock_view.setBackgroundResource(R.drawable.clock_red);
					playSound();
				}*/
				
				
				
				
				
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}catch (Exception exp) {
				// TODO: handle exception
				exp.printStackTrace();
			}
		}
		
		
	}
	
	private class CancelCallWaitAsynTask extends AsyncTask<String, Void, String>
	{
		private URLSuppoter Supporter = new URLSuppoter();
		private String queue,waiting_time;
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			return Supporter.GET(params[0]);
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			if(result.equalsIgnoreCase("1"))
			{
				
				
			}
			else
			{
//				Toast.makeText(CallWaiting.this, "Your Request has not canceled", Toast.LENGTH_SHORT).show();
			}
		}
		
		
	}
	
	
	
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	   
	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    @Override
	    protected void onPreExecute() {
	    	// TODO Auto-generated method stub
	    	
	    	super.onPreExecute();
	    }
	    protected Bitmap doInBackground(String... urls) {
	    	
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	    
	        bmImage.setImageBitmap(result);
	        
	    }    
	}
	
	
	private void create_call()
	{
		//timer.cancel();
		
//		String Calcel_url = "http://jsoncdn.webcodeplus.com/CallWatingData.svc/CancelCallRequest/"+Sharedpref.getcustomerid();
//		new CancelCallWaitAsynTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Calcel_url);
		try
		{
		new GanreteTokenClass().execute(ganrateTokenUrl);
//		Toast.makeText(CallWaiting.this, "Your Request has canceled", Toast.LENGTH_SHORT).show();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	private void cancelCallWaiting()
	{
		
		
		String Calcel_url = "http://jsoncdn.webcodeplus.com/CallWatingData.svc/CancelCallRequest/"+Sharedpref.getcustomerid();
		Log.d("cancel+acalllllllllllll",Calcel_url);
		
		new CancelCallWaitAsynTask().execute(Calcel_url);
		
		Intent intent = new Intent(CallWaiting.this, DoctorAvailableActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    
//       
		Bundle bndlanimation = 
				ActivityOptions.makeCustomAnimation(CallWaiting.this, R.anim.animation,R.anim.animation2).toBundle();
		intent.putExtra("Select", Frg_Code);
		CallWaiting.this.startActivity(intent, bndlanimation);
		finish();
	
		
	}
	
	private class GanreteTokenClass extends AsyncTask<String, Void, String> {
		
		@Override
		protected String doInBackground(String... urls) {

			PersonDetail person = new PersonDetail();
			
			person.setCustomerID(Sharedpref.getcustomerid());
			person.setState(Sharedpref.getstatecode());
			person.setdoctorid(Doc_Id);

			return POST(urls[0], person);
		}
		
		@SuppressWarnings("unused")
		@Override
		protected void onPostExecute(String result) {
			
			Log.d("Result data", result.toString());
		
			String tokenid = "";
			tokenid = result;
			//Sharedpref.setTokenId(result);
			
						
			if(!(tokenid==null))
			{
				
				 Intent iMAGEintent = new Intent(CallWaiting.this, ImageAndDescription.class);
			        
				 iMAGEintent.putExtra("Dr.Name", Doctor_Name);
				 iMAGEintent.putExtra("drID", Doc_Id);
				 iMAGEintent.putExtra("TokenId", tokenid);
				 
				 startActivity(iMAGEintent);
				 finish();
		       
					
					
					
			}
			else
			{
				Toast.makeText(CallWaiting.this,"Your Token Not ganarete." ,Toast.LENGTH_LONG).show();
			}
		
			}
		}
	
	public static String POST(String url, PersonDetail person) {
		InputStream inputStream = null;
		String result = "";
		try {

			// 1. create HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// 2. make POST request to the given URL
			HttpPost httpPost = new HttpPost(url);
			// String json = "";

			// 3. build jsonObject
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("TokenID", "0");
			jsonObject.accumulate("CustomerID", person.getCustomerID());
			jsonObject.accumulate("DoctorID", person.getdoctorid());
			jsonObject.accumulate("StateID", person.getState());
			
			Log.d("Post JsonObject", jsonObject.toString());
			
			// 4. convert JSONObject to JSON to String
			// json = jsonObject.toString();

			// ** Alternative way to convert Person object to JSON string usin
			// Jackson Lib
			// ObjectMapper mapper = new ObjectMapper();
			// json = mapper.writeValueAsString(person);

			// 5. set json to StringEntity
			StringEntity se = new StringEntity(jsonObject.toString());

			// 6. set httpPost Entity
			httpPost.setEntity(se);

			// // 7. Set some headers to inform server about the type of the
			// content
			// httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader(HTTP.CONTENT_TYPE,
					"application/json; charset=utf-8");
			// se.setContentType("application/json;charset=UTF-8");
			// se.setContentEncoding(new
			// BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
			// 8. Execute POST request to the given URL
			HttpResponse httpResponse = httpclient.execute(httpPost);

			// 9. receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// 10. convert inputstream to string
			if (inputStream != null)
			{
				result = convertInputStreamToString(inputStream);
			
			}
			else
				result = "Did not work!";
			
			
			
		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		// 11. return result
		Log.d("result",""+result);
		return result;
	}
	
	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

}
