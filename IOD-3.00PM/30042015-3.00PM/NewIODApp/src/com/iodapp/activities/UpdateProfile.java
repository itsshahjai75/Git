package com.iodapp.activities;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.iodapp.util.URLSuppoter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateProfile extends Activity implements OnItemSelectedListener {

	
	private ImageView userImage;
	private EditText fname;
	private EditText lName;
	private EditText userEmail;
	private Spinner userState;
	 private Uri mImageCaptureUri;
	private Bitmap bitmap;
	
	 private static final int PICK_FROM_CAMERA = 1;
	    private static final int PICK_FROM_FILE = 2;
	    private String userStateID;
	    private String users;
	    
	    static List<String> listItems = new ArrayList<String>();
	    private ArrayAdapter<String> dataAdapter;
		private HashMap<String, String> stateMap = new HashMap<String, String>();
		private HashMap<String, Integer> stateMapID = new HashMap<String, Integer>();
		private RelativeLayout submit;
		private Typeface sysFont;
		private ImageButton back;
		private ImageLoader imageLoader = Sharedpref.getInstance().getImageLoader();
		private String img_path = "http://cms.ionlinedoctor.com/CustomerData/";
		private final String custemrUpdateURL = "http://jsoncdn.webcodeplus.com/CustomerData.svc/UpdateCustomer";
		private final String uploadPic = "http://jsoncdn.webcodeplus.com/CallerImageData.svc/UpdateProfilePic";
		private ProgressDialog progress_dialog;
		public Integer state_id;
		private TextView txtsubmit;
		protected String img_name;
		protected Bitmap imageBitmap;
		protected String img_data;
		private NetworkImageView thumbNail;
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 requestWindowFeature(Window.FEATURE_NO_TITLE);  
			this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_update_profile);
		
		userImage = (ImageView) findViewById(R.id.uimageView2);
		fname = (EditText) findViewById(R.id.ueditfname);
		lName =(EditText) findViewById(R.id.ueditlname);
		userEmail = (EditText) findViewById(R.id.ueditemail);
		userState = (Spinner) findViewById(R.id.uspinnerstate);
	
		
		
		submit = (RelativeLayout) findViewById(R.id.ubtncreateacc);
		txtsubmit = (TextView) findViewById(R.id.textsubmit);
		
		
		back = (ImageButton) findViewById(R.id.uback);
		
		
		
		
		fname.setText(Sharedpref.getfirstname());
		lName.setText(Sharedpref.getlastname());
		userEmail.setText(Sharedpref.getmail());
		userStateID = Sharedpref.getstatecode();
		
		
		progress_dialog = new ProgressDialog(this);
		progress_dialog.setMessage("Please Wait, Loading...");
		
		if (imageLoader == null)
			imageLoader = Sharedpref.getInstance().getImageLoader();
		
		 thumbNail = (NetworkImageView) findViewById(R.id.uimageView2);
		
		
		
		thumbNail.setImageUrl(Sharedpref.getImagePath(), imageLoader);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int errorCounter=0;
				String email = userEmail.getText().toString();
				 //String mobileno = etMobileno.getText().toString();
				
				 String fnamestr = fname.getText().toString();
				 String lname = lName.getText().toString();
				 //String repass = etRepass.getText().toString();
				 String status = userState.getSelectedItem().toString();
				 
				 
				 
				 
				 if(userState.getSelectedItem().equals("Select State"))
					{
						Toast.makeText(UpdateProfile.this,"Select Your State",Toast.LENGTH_SHORT).show();
						((TextView)userState.getChildAt(0)).setError("Select yourState");
						userState.requestFocus();
						errorCounter++;
						
					}

				 
				if (!isValidEmail(email)) {
					userEmail.setError("Invalid Email");
					userEmail.requestFocus();
					errorCounter++;
				}
				else
				{
					userEmail.setError(null);
				}

				if (!isValidName(lname)) {
					lName.setError("Invalid Last Name");
					lName.requestFocus();
					errorCounter++;
				}
				else
				{
					lName.setError(null);
				}
				
				if (!isValidName(fnamestr)) {
					fname.setError("Invalid First Name");
					fname.requestFocus();
					errorCounter++;
				}
				else
				{
					fname.setError(null);
				}
				
				
				
				
				if(errorCounter == 0)
				{
					
					showDialog();
					
				}

			}
		});
		  
	  
	 
	 
	  
	   
	   
	   
		
		final String [] items           = new String [] {"From Camera", "From SD Card"};
        ArrayAdapter<String> adapter  = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder     = new AlertDialog.Builder(this);
 
        builder.setTitle("Select Image");
        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int item ) {
                if (item == 0) {
                    Intent intent    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file        = new File(Environment.getExternalStorageDirectory(),
                                        "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
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
        
        userImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.show();
			}
		});
		
        
        
        
     // Spinner click listener
     		userState.setOnItemSelectedListener(this);
     		
     		
     	
     		listItems.add("Select State");
     		
     		new StateHttpAsyncTask()
     		.execute("http://jsoncdn.webcodeplus.com/StateData.svc/StateList/223");
     		
     		
     		// categories=listItems;
     		// Creating adapter for spinner
//     		 dataAdapter = new ArrayAdapter<String>(this,
//     				android.R.layout.simple_spinner_dropdown_item, listItems);
     		 
     		 dataAdapter = new ArrayAdapter<String>(this,
     					R.layout.spinner_item, listItems);
     		 
     		// dataAdapter.addAll(listItems);
     		dataAdapter.notifyDataSetChanged();
     		// Drop down layout style - list view with radio button
//     		dataAdapter
//     				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     		// dataAdapter.addAll(listItems);
     		// dataAdapter.notifyDataSetChanged();
     		// attaching data adapter to spinner

     		userState.setAdapter(dataAdapter);
     		
//     		spinner.setSelection(1);
     		
     		
     		
		
	}
	private void setSystemFont()
	{
	sysFont = Typeface.createFromAsset(getAssets(),"font/Leelawadee.ttf");

	fname.setTypeface(sysFont);
	lName.setTypeface(sysFont);

	userEmail.setTypeface(sysFont);
	

	txtsubmit.setTypeface(sysFont);
	

	}
	
	
	private void hidePDialog() {
		if (progress_dialog != null) {
			progress_dialog.dismiss();
			progress_dialog = null;
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// On selecting a spinner item
		String item = parent.getItemAtPosition(position).toString();
		// TextView tv = (TextView)view;
		// String selection = tv.getText().toString();

		// etResponse.setText(selection);
		// Showing selected spinner item
		((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        ((TextView) parent.getChildAt(0)).setTextSize(20);
        
//        ((TextView) parent.getChildAt(0)).setTypeface(sysFont);
//		Toast.makeText(parent.getContext(), "Selected: " + item,
//				Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
//		 try
//		  {
//			  users = stateMap.get(userStateID);
//		  }catch(NullPointerException e){
//			  e.printStackTrace();
//		  }
		super.onStart();
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
        
        		userImage.setImageBitmap(getScaledBitmap(path, userImage.getWidth(), userImage.getHeight()));
        	
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
		getMenuInflater().inflate(R.menu.update_profile, menu);
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
	
private class StateHttpAsyncTask extends AsyncTask<String, Void, String> {
		
		URLSuppoter suppoter = new URLSuppoter();
		@Override
		protected String doInBackground(String... urls) {

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
				// JSONObject json = new JSONObject(result);
				
				dataAdapter.notifyDataSetChanged();
				JSONArray ja = new JSONArray(result);

				
				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					listItems.add(jo.getString("StateName"));
//					stateMap.put(jo.getString("StateID"),jo.getString("StateName").toString());
					stateMap.put(jo.getString("StateID").toString(), jo.getString("StateName").toString());
					stateMapID.put(jo.getString("StateName").toString(), Integer.parseInt(jo.getString("StateID")));
					
				}
				//
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try
			  {
				  String users1 = stateMap.get(userStateID);
				  if (!users1.equals(null)) {
				        int spinnerPostion = dataAdapter.getPosition(users1);
				        userState.setSelection(spinnerPostion);
				        
				        spinnerPostion = 0;
				    }
			  }catch(NullPointerException e){
				  e.printStackTrace();
			  }
			dataAdapter.notifyDataSetChanged();
			
		}

		@Override
		protected void onCancelled() {

			super.onCancelled();
//			Toast toast = Toast.makeText(getApplicationContext(),
//					"Error is occured due to some probelm", Toast.LENGTH_LONG);
//			toast.show();

		}
	}

private boolean isValidPassword(String pass) {
	if (pass != null && pass.length() > 0) {
		return true;
	}
	return false;
}

// validating password with retype password
private boolean isValidName(String name) {
	if (name != null && name.length() > 0) {
		return true;
	}
	return false;
}
private boolean isValidEmail(String email) {
	String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	Matcher matcher = pattern.matcher(email);
	return matcher.matches();
}

@Override
public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub
	
}



private class UpadateCustomerAsyncTaskpost extends AsyncTask<String, Void, String> {
	
	

	

	@Override
	protected String doInBackground(String... urls) {

		PersonDetail person = new PersonDetail(); 
		
		person.setCustomerID(Sharedpref.getcustomerid());
		person.setSystemUserID("30");
		person.setFirstName(fname.getText().toString());
		person.setLastName(lName.getText().toString());
		person.setMiddleInitial("");
		person.setPhoneNo("");
		person.setMobileNo("");
		person.setFaxNo("");
		person.setEmailID(userEmail.getText().toString());
		person.setAddress1("");
		person.setAddress2("");
		person.setStreetName("");
		person.setCity("");
		
		String State_Name = (String) userState.getItemAtPosition(userState.getSelectedItemPosition());
		
		 state_id = stateMapID.get(State_Name);
		 Sharedpref.setstatecode(String.valueOf(state_id));
		 
		person.setState(String.valueOf(state_id));
		
		person.setCountry("");
		person.setZipcode("");
		person.setBirthDate("");
		person.setGender("");
		person.setCompanyName("");
		person.setDesignation("");
		person.setCustomerType("");
		person.setPrefferedTimeToContact("");
		person.setHeardAboutUs("");
		person.setDailyAlert("");
		person.setNewsLetter("");
		person.setUsername(userEmail.getText().toString());
		person.setPassword(Sharedpref.getpass());
		person.setExtraInfo("");
		person.setComments("");
		person.setIPAddress("");
	

		return POST(urls[0], person);
	}
	@Override
	protected void onPreExecute() {
	// TODO Auto-generated method stub
		
	super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(String result) {
//		
		String responceResul = result;
		
		if(Integer.parseInt(responceResul) == 1)
		{
			Sharedpref.setfirstname(fname.getText().toString());
			Sharedpref.setlastname(lName.getText().toString());
			Sharedpref.setstatecode(String.valueOf(state_id));
			
			
		}
		else if (Integer.parseInt(responceResul) == 0) {
			Toast.makeText(UpdateProfile.this, "Your Data Not Update.", Toast.LENGTH_SHORT);
		}
		
		
			
			
		
	
		// etResponse.setText(json.toString());
//		hidePDialog();
	}
	
	@Override
	protected void onCancelled() {
	// TODO Auto-generated method stub
		hidePDialog();
	super.onCancelled();
	}
	
	}

public static String POST(String url, PersonDetail pd) {
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
		
		jsonObject.accumulate("CustomerID",pd.getCustomerID());
		jsonObject.accumulate("SystemUserID",pd.getSystemUserID());
		jsonObject.accumulate("FirstName",pd.getFirstName());
		jsonObject.accumulate("LastName",pd.getLastName());
		jsonObject.accumulate("MiddleInitial",pd.getMiddleInitial());
		
		jsonObject.accumulate("PhoneNo",pd.getPhoneNo());
		jsonObject.accumulate("MobileNo",pd.getMobileNo());
		jsonObject.accumulate("FaxNo",pd.getFaxNo());
		jsonObject.accumulate("EmailID",pd.getEmailID());
		jsonObject.accumulate("Address1",pd.getAddress1());
		
		jsonObject.accumulate("Address2",pd.getAddress2());
		jsonObject.accumulate("StreetName",pd.getStreetName());
		jsonObject.accumulate("City",pd.getCity());
		jsonObject.accumulate("State",pd.getState());
		jsonObject.accumulate("Country",pd.getCountry());
		
		jsonObject.accumulate("Zipcode",pd.getZipcode());
		jsonObject.accumulate("BirthDate",pd.getBirthDate());
		jsonObject.accumulate("Gender",pd.getGender());
		jsonObject.accumulate("CompanyName",pd.getCompanyName());
		jsonObject.accumulate("Designation",pd.getDesignation());
		
		jsonObject.accumulate("CustomerType",pd.getCustomerType());
		jsonObject.accumulate("PrefferedTimeToContact",pd.getPrefferedTimeToContact());
		jsonObject.accumulate("HeardAboutUs",pd.getHeardAboutUs());
		jsonObject.accumulate("DailyAlert",pd.getDailyAlert());
		jsonObject.accumulate("NewsLetter",pd.getNewsLetter());
		
		jsonObject.accumulate("Username",pd.getUsername());
		jsonObject.accumulate("Password",pd.getPassword());
		jsonObject.accumulate("ExtraInfo",pd.getExtraInfo());
		jsonObject.accumulate("Comments",pd.getComments());
		jsonObject.accumulate("IPAddress",pd.getIPAddress());
		
		
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
		{
			result = "Did not work!";
		}
		
		
	} catch (Exception e) {
		Log.d("InputStream", e.getLocalizedMessage());
	}

	// 11. return result
//	Log.d("result",""+result);
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
			UpdateProfile.this);

	
		// set title
//		alertDialogBuilder.setTitle("1. By Logging into and/or Registering New Account with I Online Doctor, I accept all I Online Terms and Conditions.");
		
		// set dialog message
		alertDialogBuilder
			.setMessage("Do you wish to Update your Profile").setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			

				public void onClick(DialogInterface dialog,int id) {
					
					
					
					new UpadateCustomerAsyncTaskpost().execute(custemrUpdateURL);
					progress_dialog.show();
					
					
					
					
					if(userImage.getDrawable() != null)
					{
					
						String name = Sharedpref.getfirstname();
						img_name = name+"_"+System.currentTimeMillis()+".jpeg";
			 
			 try {
					
					imageBitmap = ((BitmapDrawable)userImage.getDrawable()).getBitmap();
//					ByteArrayOutputStream stream = new ByteArrayOutputStream();
//					imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//					imgByte = stream.toByteArray();
					img_data = Base64.encodeToString(getBytesFromBitmap(imageBitmap), Base64.NO_WRAP);
					
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			
				 
						
				new AddImageAsyntask(img_data).execute(uploadPic);
					
					}		
					
					dialog.cancel();
					
				
					
				}
			  }).setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						
						
						dialog.cancel();
						
					}});
		

		
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			
//			alertDialog.getButton(0).setBackgroundColor(Color.BLACK);
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

		PersonDetail pdetails = new PersonDetail();
		pdetails.setCustomerID(Sharedpref.getcustomerid());
		pdetails.setImage_data(this.img_str);
		pdetails.setImage_name(img_name);
		
		return POSTimage(urls[0],pdetails);
	}
	
	@Override
	protected void onPostExecute(String result) {
//		
		Log.d("Result data", result.toString());
		
		hidePDialog();
		if(Integer.parseInt(result) > 0)
		{
			Toast.makeText(UpdateProfile.this, "Your Information Update Successfully", Toast.LENGTH_SHORT).show();
			
			Intent it_login = new Intent(UpdateProfile.this,
					MyAccount.class);
			Bundle bndlanimation = ActivityOptions.makeCustomAnimation(
					UpdateProfile.this, R.anim.animation,
					R.anim.animation2).toBundle();
			startActivity(it_login, bndlanimation);
			finish();
		}
		
	
		}
	
	
	}

public  String POSTimage(String url,PersonDetail pDetails) {
	InputStream inputStream = null;
	String result = "";
	try {

		HttpClient httpclient = new DefaultHttpClient();
		
		HttpPost httpPost = new HttpPost(url);
		
		JSONObject jsonObject = new JSONObject();
		
		
		jsonObject.accumulate("ImageName",pDetails.getImage_name().toString());
		
		jsonObject.accumulate("ImageData",pDetails.getImage_data().toString());
		jsonObject.accumulate("CustomerID",pDetails.getCustomerID().toString());
		
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
    bitmap.compress(CompressFormat.JPEG, 100, stream);
    return stream.toByteArray();
}

}



