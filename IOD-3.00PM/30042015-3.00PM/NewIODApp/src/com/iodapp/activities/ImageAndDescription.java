package com.iodapp.activities;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.iodapp.util.SetDoctoreAvailable;
import com.iodapp.util.URLSuppoter;
import com.opentok.android.demo.opentoksamples.MultipartyActivity;




import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageAndDescription extends Activity {

	 private Uri mImageCaptureUri;
	  
		private Bitmap bitmap;
		private PersonDetail person;
		private String buttonTag = null;
		private ImageView mImageView2;
		private ImageView mImageView1;
		private ImageView mImageView3;
		private ImageView mImageView4;
		public static long timeElapsed;
		
		public  final long startTime = 258000;
		public  final long interval = 1000;

		private TextView timer_view;
		private int remain_second;
		
		private Button btnCancel;

		private Button btnUpload;
		private static boolean timerHasStarted=false;
		public static MYTimer timer;
		private String dr_name=null;

		public String token_id_oncreat="";

		private ImageButton btnback;

		private TextView titeletext;

		private TextView descripText;

		private Typeface sysFont;
		private String img_name,img_type,img_data;
		private Bitmap imageBitmap;

		private String dr_id;
		private final String setTokenURL ="http://jsoncdn.webcodeplus.com/TokenData.svc/SetTokenDetails";
		private final String DescURl = "http://jsoncdn.webcodeplus.com/CallerImageData.svc/AddCallerDescription";
		private final String imgURl =	"http://jsoncdn.webcodeplus.com/CallerImageData.svc/AddCallerImage";
		public String tokenid="";

		protected byte[] imgByte;

		private ProgressDialog progress_dialog;
		
	    private static final int PICK_FROM_CAMERA = 1;
	    private static final int PICK_FROM_FILE = 2;
	 
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	 
	        requestWindowFeature(Window.FEATURE_NO_TITLE);  
			this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        setContentView(R.layout.activity_image_and_description);
	        
	        Bundle img_bandle = getIntent().getExtras();
	        
	        dr_name = img_bandle.getString("Dr.Name");
	        token_id_oncreat =img_bandle.getString("TokenId");
	        Log.d("oncreat-tokenGEt",token_id_oncreat);
	        dr_id = img_bandle.getString("drID");
	        
	       
	        
	        
	        timer = new MYTimer(startTime, interval);
	        
	        timer_view = (TextView) findViewById(R.id.time);
	        titeletext = (TextView) findViewById(R.id.textView1);
	        descripText = (TextView) findViewById(R.id.Descrip_Text);
	        
	        btnCancel = (Button) findViewById(R.id.btn_cancel);
	        btnUpload = (Button) findViewById(R.id.btn_Upload);
	        btnback = (ImageButton) findViewById(R.id.img_Des_back);
	        
	        progress_dialog = new ProgressDialog(this);
			progress_dialog.setMessage("Please Wait, Loading...");
			
	       
	        timer_view.setVisibility(View.GONE);
	        
	        btnback.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					time_Start_Stop();
					
//					
					String set_str = "http://jsoncdn.webcodeplus.com/TokenData.svc/ExpireTokenDetails/"+token_id_oncreat+"/"+Sharedpref.getcustomerid();
					SetDoctoreAvailable SetDA = new SetDoctoreAvailable();
					SetDA.execute(set_str);
					
					
					Intent it_DocAvl = new Intent(ImageAndDescription.this,
							DoctorAvailableActivity.class);
					Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
							getApplicationContext(), R.anim.animation,
							R.anim.animation2).toBundle();
					it_DocAvl.putExtra("Select", 0);
					startActivity(it_DocAvl, bndlanimation);
					
					finish();
				}
			});
	        
	        
	        
	        time_Start_Stop();
	 
	        final String [] items           = new String [] {"From Camera", "From SD Card"};
	        ArrayAdapter<String> adapter  = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
	        AlertDialog.Builder builder     = new AlertDialog.Builder(this);
	 
	        builder.setTitle("Select Image");
	        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
	            public void onClick( DialogInterface dialog, int item ) {
	                if (item == 0) {
	                    Intent intent    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                    File file        = new File(Environment.getExternalStorageDirectory(),
	                                        Sharedpref.getfirstname() + String.valueOf(System.currentTimeMillis()) + ".jpg");
	                    mImageCaptureUri = Uri.fromFile(file);
	 
	                    try {
	                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
	                        intent.putExtra("return-data", true);
	 
	                        startActivityForResult(intent, PICK_FROM_CAMERA);
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	 
	                    dialog.cancel();
	                } else {
	                    Intent intent = new Intent();
	 
	                    intent.setType("image/*");
	                    intent.setAction(Intent.ACTION_GET_CONTENT);
	 
	                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
	                }
	            }
	        } );
	 
	        final AlertDialog dialog = builder.create();
	 
	        mImageView1 = (ImageView) findViewById(R.id.img1);
	        mImageView2 = (ImageView) findViewById(R.id.img2);
	        mImageView3 = (ImageView) findViewById(R.id.img3);
	        mImageView4 = (ImageView) findViewById(R.id.img4);
	        
	        mImageView1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					buttonTag = "image1";
					dialog.show();
					
				}
			});
	        
mImageView2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					buttonTag = "image2";
					dialog.show();
				}
			});

mImageView3.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		buttonTag = "image3";
		dialog.show();
		
	}
});

mImageView4.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		buttonTag = "image4";
		dialog.show();
		
	}
});

btnCancel.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		time_Start_Stop();
		
		String set_str = "http://jsoncdn.webcodeplus.com/TokenData.svc/ExpireTokenDetails/"+token_id_oncreat+"/"+Sharedpref.getcustomerid();
		SetDoctoreAvailable SetDA = new SetDoctoreAvailable();
		SetDA.execute(set_str);
		
		
		Intent it_DocAvl = new Intent(ImageAndDescription.this,
				DoctorAvailableActivity.class);
		Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
				getApplicationContext(), R.anim.animation,
				R.anim.animation2).toBundle();
		it_DocAvl.putExtra("Select", 0);
		startActivity(it_DocAvl, bndlanimation);
		
		finish();
		
	}
});

btnUpload.setOnClickListener(new OnClickListener() {
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		
		
		
		if(descripText.getText() == null || descripText.getText().toString().equalsIgnoreCase(""))
		{
			descripText.setError("Video call cannot be placed without description");
		}
		else
		{
			progress_dialog.show();
			descripText.setError(null);
			img_type = "jpeg";
			
			if(mImageView1.getDrawable() != null)
			{
				img_name = mImageView1.getTag().toString();
				
//				bitmap = mImageView1.getDrawingCache(true);
				try {
					
					imageBitmap = ((BitmapDrawable)mImageView1.getDrawable()).getBitmap();
//					ByteArrayOutputStream stream = new ByteArrayOutputStream();
//					imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//					imgByte = stream.toByteArray();
					img_data = Base64.encodeToString(getBytesFromBitmap(imageBitmap), Base64.NO_WRAP);
					
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				 
						
				new AddImageAsyntask(img_data).execute(imgURl);
				
				
			}
			if(mImageView2.getDrawable() != null)
			{
				descripText.setError(null);
				img_type = "jpeg";
				
				if(mImageView1.getDrawable() != null)
				{
					img_name = mImageView2.getTag().toString();
					
//					bitmap = mImageView1.getDrawingCache(true);
					try {
						
						imageBitmap = ((BitmapDrawable)mImageView2.getDrawable()).getBitmap();
//						ByteArrayOutputStream stream = new ByteArrayOutputStream();
//						imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//						imgByte = stream.toByteArray();
						img_data = Base64.encodeToString(getBytesFromBitmap(imageBitmap), Base64.NO_WRAP);
						
						
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
					 
							
					new AddImageAsyntask(img_data).execute(imgURl);
				
			}
			if(mImageView3.getDrawable() != null)
			{
				descripText.setError(null);
				img_type = "jpeg";
				
				if(mImageView1.getDrawable() != null)
				{
					img_name = mImageView3.getTag().toString();
					
//					bitmap = mImageView1.getDrawingCache(true);
					try {
						
						imageBitmap = ((BitmapDrawable)mImageView3.getDrawable()).getBitmap();
//						ByteArrayOutputStream stream = new ByteArrayOutputStream();
//						imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//						imgByte = stream.toByteArray();
						img_data = Base64.encodeToString(getBytesFromBitmap(imageBitmap), Base64.NO_WRAP);
						
						
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
					 
							
					new AddImageAsyntask(img_data).execute(imgURl);
			}
			if(mImageView4.getDrawable() != null)
			{
				descripText.setError(null);
				img_type = "jpeg";
				
				if(mImageView1.getDrawable() != null)
				{
					img_name = mImageView4.getTag().toString();
					
//					bitmap = mImageView1.getDrawingCache(true);
					try {
						
						imageBitmap = ((BitmapDrawable)mImageView4.getDrawable()).getBitmap();
//						ByteArrayOutputStream stream = new ByteArrayOutputStream();
//						imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//						imgByte = stream.toByteArray();
						img_data = Base64.encodeToString(getBytesFromBitmap(imageBitmap), Base64.NO_WRAP);
						
						
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
					 
							
					new AddImageAsyntask(img_data).execute(imgURl);
			}
			
			
		time_Start_Stop();
		
		
		StringTokenizer formate = new StringTokenizer(timer_view.getText().toString(),	":");
		String minuts = formate.nextToken();
		String second = formate.nextToken();
		
		remain_second = 240 - ((Integer.parseInt(minuts)*60)+Integer.parseInt(second));
		
		new AddDescripAsyntask().execute(DescURl);
		
		
		}
		
	
	}
});
		setSystemFont();
//	        
	    }
	    
	    private void setSystemFont()
		{
			sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");
			
			titeletext.setTypeface(sysFont);
			descripText.setTypeface(sysFont);
			btnCancel.setTypeface(sysFont);
			btnUpload.setTypeface(sysFont);
			
			
			
		}
	 
	    @Override
	    public void onBackPressed() {
	    	// TODO Auto-generated method stub
	    	
	    	
	    }
	   
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (resultCode != RESULT_OK) return;
	 
	          bitmap = null;
	        String path     = "";
	 
	        if (requestCode == PICK_FROM_FILE) {
	            mImageCaptureUri = data.getData();
	            path = getRealPathFromURI(mImageCaptureUri); //from Gallery
	 
	            if (path == null)
	                path = mImageCaptureUri.getPath(); //from File Manager
	 
	            if (path != null)
	                bitmap  = BitmapFactory.decodeFile(path);
	        } else {
	            path    = mImageCaptureUri.getPath();
	            bitmap  = BitmapFactory.decodeFile(path);
	        }
	        
	        if(buttonTag != null)
	        {
	        	if(buttonTag.equalsIgnoreCase("image1"))
	        	{
	        		mImageView1.setImageBitmap(getScaledBitmap(path, mImageView1.getWidth(), mImageView1.getHeight()));
	        		mImageView1.setTag(Sharedpref.getfirstname()+String.valueOf(System.currentTimeMillis())+".jpg");
	        		mImageView1.setDrawingCacheEnabled(true);
//	        		Toast.makeText(getApplicationContext(), mImageView1.getTag().toString(), Toast.LENGTH_LONG).show();
	        	}
	        	else if(buttonTag.equalsIgnoreCase("image2"))
	        	{
	        		mImageView2.setImageBitmap(getScaledBitmap(path, mImageView2.getWidth(), mImageView2.getHeight()));
	        		mImageView2.setTag(Sharedpref.getfirstname()+String.valueOf(System.currentTimeMillis())+".jpg");
//	        		Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
	        	}
	        	else if(buttonTag.equalsIgnoreCase("image3"))
	        	{
	        		mImageView3.setImageBitmap(getScaledBitmap(path, mImageView3.getWidth(), mImageView3.getHeight()));
	        		mImageView3.setTag(Sharedpref.getfirstname()+String.valueOf(System.currentTimeMillis())+".jpg");
//	        		Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
	        	}
	        	else if(buttonTag.equalsIgnoreCase("image4"))
	        	{
	        		mImageView4.setImageBitmap(getScaledBitmap(path, mImageView4.getWidth(), mImageView4.getHeight()));
	        		mImageView4.setTag(Sharedpref.getfirstname()+String.valueOf(System.currentTimeMillis())+".jpg");
//	        		Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
	        	}
	        }
	 
	        
	    }
	 
	    public String getRealPathFromURI(Uri contentUri) {
	        String [] proj      = {MediaStore.Images.Media.DATA};
	        Cursor cursor       = managedQuery( contentUri, proj, null, null,null);
	 
	        if (cursor == null) return null;
	 
	        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	 
	        cursor.moveToFirst();
	 
	        return cursor.getString(column_index);
	    }
	    
	    
	    private Bitmap getScaledBitmap(String picturePath, int width, int height) {
	        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
	        sizeOptions.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(picturePath, sizeOptions);

	        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

	        sizeOptions.inJustDecodeBounds = false;
	        sizeOptions.inSampleSize = inSampleSize;

	        return BitmapFactory.decodeFile(picturePath, sizeOptions);
	    }

	    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	        // Raw height and width of image
	        final int height = options.outHeight;
	        final int width = options.outWidth;
	        int inSampleSize = 1;

	        if (height > reqHeight || width > reqWidth) {

	            // Calculate ratios of height and width to requested height and
	            // width
	            final int heightRatio = Math.round((float) height / (float) reqHeight);
	            final int widthRatio = Math.round((float) width / (float) reqWidth);

	            // Choose the smallest ratio as inSampleSize value, this will
	            // guarantee
	            // a final image with both dimensions larger than or equal to the
	            // requested height and width.
	            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	        }

	        return inSampleSize;
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_and_description, menu);
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
	
	
	public class MYTimer extends CountDownTimer{

		

		

		public MYTimer(long millisInFuture, long countDownInterval) {
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
			
			timer_view.setText(hms1.toString());
			
			if(hms1.equalsIgnoreCase("03:30"))
			{
				timer_view.setVisibility(View.VISIBLE);
				Toast tmsg = Toast.makeText(ImageAndDescription.this, "Your Sessoin will expire in 30 second", Toast.LENGTH_LONG);
				
				
				tmsg.setGravity(Gravity.TOP, 0, 0);
				tmsg.show();
			}
			
			if(hms1.equalsIgnoreCase("04:00"))
			{
				sendEndCallSignal();
			}

			
			
		}
		
	}
		
		private void sendEndCallSignal()
		{
			

			time_Start_Stop();
			String set_str = "http://jsoncdn.webcodeplus.com/TokenData.svc/ExpireTokenDetails/"+token_id_oncreat+"/"+Sharedpref.getcustomerid();
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
		
		public static void time_Start_Stop() {
			if (!timerHasStarted) {
				timer.start();
				timerHasStarted = true;
			} else {
				timer.cancel();
				timerHasStarted = false;
			}


	}
		
		
		
		private class HttpAsync_videocallTaskpost extends AsyncTask<String, Void, String> {
			
			
			
			

			@Override
			protected String doInBackground(String... urls) {

				person = new PersonDetail();

				
//				Log.d("tokenId==Gone",Sharedpref.getTokenId());
				person.setTokenId(token_id_oncreat);
				person.setSessionKey("0");
				person.setTokenKey("0");
				return POST(urls[0], person);
			}
			
			@Override
			protected void onPostExecute(String result) {
//				
				Log.d("Result data", result.toString());
				try
				{
					JSONObject json = new JSONObject(result);
				// etResponse.setText(json.toString(1));
				// JSONObject json = new JSONObject(result);
				String str = "";
				String str1 = "";
				 tokenid = "";
				
				str = json.getString("SessionKey");
				Sharedpref.setsessionkey(str);
				str1 = json.getString("TokenKey");
				Sharedpref.setTokenkey(str1);
				tokenid = json.getString("TokenID");
				//dfsdfds
				
				//Sharedpref.setTokenId(tokenid);
				
				Log.d("token_new", tokenid);
//				Log.d("TokenId", tokenid)=============================;
//				Log.d("ShatredTokenId", Sharedpref.getTokenId());
				
				
				
							
				if(!(tokenid==null))
				{
//					
					Intent intent = new Intent(ImageAndDescription.this, MultipartyActivity.class);
			        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		       
			    intent.putExtra("Remain_Second", String.valueOf(remain_second));
		        intent.putExtra("drID",dr_id);
		        intent.putExtra("Dr.Name", dr_name);
		        intent.putExtra("TokenId", token_id_oncreat);
		       // Sharedpref.setTokenId(tokenid);
//			       
		       
		        
//			       
					Bundle bndlanimation = 
							ActivityOptions.makeCustomAnimation(ImageAndDescription.this, R.anim.animation,R.anim.animation2).toBundle();
					ImageAndDescription.this.startActivity(intent, bndlanimation);
					finish();
			       
						
						
						
				}
			
				// etResponse.setText(json.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
				}
			}
		
		public static String POST(String url, PersonDetail person) {
			InputStream inputStream = null;
			String result = "";
			try {

				HttpClient httpclient = new DefaultHttpClient();
				
				HttpPost httpPost = new HttpPost(url);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("TokenID", person.getTokenId());
				jsonObject.accumulate("SessionKey",person.getSessionKey());
				jsonObject.accumulate("TokenKey", person.getTokenKey());
				
				Log.d("Json---2222", jsonObject.toString());
				
				StringEntity se = new StringEntity(jsonObject.toString());

				httpPost.setEntity(se);

				httpPost.setHeader(HTTP.CONTENT_TYPE,
						"application/json; charset=utf-8");
				
				HttpResponse httpResponse = httpclient.execute(httpPost);

				inputStream = httpResponse.getEntity().getContent();

				if (inputStream != null)
				{
					result = convertInputStreamToString(inputStream);
				}
				else
				{
					result = "Did not work!";
				}
				
				
			} catch (Exception e) {
				Log.d("InputStream", e.getLocalizedMessage());
			}

			// 11. return result
			Log.d("result",""+result);
			return result;
		}
		
		
		
		
private class AddDescripAsyntask extends AsyncTask<String, Void, String> {
			
	
			@Override
			protected String doInBackground(String... urls) {

				
				return POSTDescription(urls[0]);
			}
			@Override
			protected void onPreExecute() {
			// TODO Auto-generated method stub
				progress_dialog.show();
			super.onPreExecute();
			}
			
			@Override
			protected void onPostExecute(String result) {
//				
				progress_dialog.cancel();
				Log.d("Adddiscri_postexcu", result.toString());
				
				if(Integer.parseInt(result) > 0)
				{
					Toast.makeText(ImageAndDescription.this, "Descrioption Send.", Toast.LENGTH_SHORT).show();
					showDialog();
				}
				
				
			}
			
			}
		
		public  String POSTDescription(String url) {
			InputStream inputStream = null;
			String result = "";
			try {

				HttpClient httpclient = new DefaultHttpClient();
				
				HttpPost httpPost = new HttpPost(url);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("Description",descripText.getText().toString() );
				
				jsonObject.accumulate("TokenID",token_id_oncreat);
				
				//Sharedpref.setTokenId(token_id);
				
				Log.d("DiscriptinCall JsonObject", jsonObject.toString());
				
				StringEntity se = new StringEntity(jsonObject.toString());

				httpPost.setEntity(se);

				httpPost.setHeader(HTTP.CONTENT_TYPE,
						"application/json; charset=utf-8");
				
				HttpResponse httpResponse = httpclient.execute(httpPost);

				inputStream = httpResponse.getEntity().getContent();

				if (inputStream != null)
				{
					result = convertInputStreamToString(inputStream);
				}
				else
				{
					result = "Did not work!";
				}
				
				
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
		
		private void showDialog()
		{
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					ImageAndDescription.this);
	 
			
				// set title
//				alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
				
				// set dialog message
				alertDialogBuilder
					.setMessage("Ready to video call doctor?").setCancelable(false)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							
							new HttpAsync_videocallTaskpost()
							.execute(setTokenURL);
							
						}
					  }).setNegativeButton("No", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								
								
								dialog.cancel();
								
							}});
				
	 
				
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					
//					alertDialog.getButton(0).setBackgroundColor(Color.BLACK);
					//alertDialog.getButton(1).setBackgroundColor(Color.BLACK);
				
					
	 
					// show it
					alertDialog.show();
					alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#ffffff"));
					alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(Color.parseColor("#ffffff"));
					alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00bfff"));
					alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00bfff"));
					alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(19.0f);
					alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(19.0f);
					
		}
		
		

		
private class AddImageAsyntask extends AsyncTask<String, Void, String> {
			
			String img_str = "";
	public AddImageAsyntask(String img_str) {
		// TODO Auto-generated constructor stub
		this.img_str = img_str;
		
	}

			@Override
			protected String doInBackground(String... urls) {

				
				return POSTimage(urls[0],this.img_str);
			}
			
			@Override
			protected void onPostExecute(String result) {
//				
				Log.d("Result data", result.toString());
				
				if(Integer.parseInt(result) > 0)
				{
					Toast.makeText(ImageAndDescription.this, "Your Image sended.", Toast.LENGTH_SHORT).show();
				}
				
			
				}
			
			
			}
		
		public  String POSTimage(String url,String img_datap) {
			InputStream inputStream = null;
			String result = "";
			try {

				HttpClient httpclient = new DefaultHttpClient();
				
				HttpPost httpPost = new HttpPost(url);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("Description",descripText.getText().toString() );
				
				jsonObject.accumulate("ImageName",img_name.toString());
				jsonObject.accumulate("ImageType",img_type.toString());
				jsonObject.accumulate("ImageData",img_datap);
				jsonObject.accumulate("TokenID",token_id_oncreat);
				
				Log.d("Post JsonObject", jsonObject.toString());
				
				StringEntity se = new StringEntity(jsonObject.toString());

				httpPost.setEntity(se);

				httpPost.setHeader(HTTP.CONTENT_TYPE,
						"application/json; charset=utf-8");
				
				HttpResponse httpResponse = httpclient.execute(httpPost);

				inputStream = httpResponse.getEntity().getContent();

				if (inputStream != null)
				{
					result = convertInputStreamToString(inputStream);
				}
				else
				{
					result = "Did not work!";
				}
				
				
			} catch (Exception e) {
				Log.d("InputStream", e.getLocalizedMessage());
			}

			// 11. return result
			Log.d("result",""+result);
			return result;
		}
		

		public byte[] getBytesFromBitmap(Bitmap bitmap) {
		    ByteArrayOutputStream stream = new ByteArrayOutputStream();
		    bitmap.compress(CompressFormat.JPEG, 70, stream);
		    return stream.toByteArray();
		}
	
	
}
