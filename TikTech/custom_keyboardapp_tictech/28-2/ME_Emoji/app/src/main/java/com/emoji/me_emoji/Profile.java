package com.emoji.me_emoji;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emoji.me_emoji.utils.Constants;
import com.emoji.me_emoji.utils.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Profile extends AppCompatActivity {


    EditText et_firstname ,et_email ,et_mobileno , et_password ,et_confirmpass;
    LinearLayout linear_submit;
    CircleImageView img_profile;

    public final int MY_REQUEST_CODE = 500;
    public final int MY_REQUEST_CODE_STORAGE = 600;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    private static final String IMAGE_DIRECTORY_NAME = "Camera";
    Uri editedImageUri;

    String firstname , email , mobileno , password , confirmpass , encodedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        et_firstname = (EditText)findViewById(R.id.firstname);
        et_email = (EditText)findViewById(R.id.email);
        et_mobileno = (EditText)findViewById(R.id.mobileno);
        et_password  = (EditText)findViewById(R.id.password);
        et_confirmpass = (EditText)findViewById(R.id.confirm_pass);

        img_profile = (CircleImageView) findViewById(R.id.img_profile);

        linear_submit = (LinearLayout)findViewById(R.id.linear_submit);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });

        linear_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname = et_firstname.getText().toString();
                email = et_email.getText().toString();
                mobileno = et_mobileno.getText().toString();
                password = et_password.getText().toString();
                confirmpass = et_confirmpass.getText().toString();

                Bitmap bitmap = ((BitmapDrawable)img_profile.getDrawable()).getBitmap();
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG , 90 , stream);
                byte[] image=stream.toByteArray();
                encodedImage = Base64.encodeToString(image , 0);

                if(firstname.isEmpty()){
                  et_firstname.setError("Please Enter First Name");
                }else if(mobileno.isEmpty()){
                  et_mobileno.setError("Please Enter Mobile No.");
                }else if(!password.isEmpty() || !confirmpass.isEmpty()){
                    if(!password.equals(confirmpass)){
                        et_confirmpass.setError("Password Mismatch");
                    }else{
                        new UpdateProfile().execute();
                    }
                }else{
                       new UpdateProfile().execute();
                }
            }
        });

        new GetProfile().execute();
    }


    private class GetProfile extends AsyncTask<Void , Void ,Void>{

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           pDialog = ProgressDialog.show(Profile.this , "" , "Please wait.....");
        }

        @Override
        protected Void doInBackground(Void... params) {


            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "user")
                    .add("passing_value" , "get_profile")
                    .add("userid" , Utils.getPrefs(Profile.this , Constants.PREF_USERID))
                    .build();

            Request request = new Request.Builder()
                    .url(Constants.API_URL)
                    .post(formBody).build();

            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(res == null || res.equals("")){
                pDialog.dismiss();
                Toast.makeText(getApplicationContext() , "Network Error Or Error" , Toast.LENGTH_SHORT).show();
                return;
            }

            pDialog.dismiss();
            try {
                JSONObject jsonObj=new JSONObject(res);
                String status_code = jsonObj.getString("status_code");
                if(status_code.equals("200")){
                    JSONObject jsonData = jsonObj.getJSONObject("data");

                    String username = jsonData.getString("username");
                    String  email   = jsonData.getString("email");
                    String  profile_pic = jsonData.getString("profile_pic");
                    String  phone_no  = jsonData.getString("phone_no");

                    et_firstname.setText(username);
                    et_email.setText(email);
                    et_mobileno.setText(phone_no);

                    Log.e("profile" , profile_pic);

                    if(!profile_pic.equals("")){
                        Picasso.with(Profile.this)
                                .load(Constants.IMAGE_API_URL+profile_pic)
                                .placeholder(R.drawable.noimage) // optional
                                .error(R.drawable.noimage)         // optional
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(img_profile);
                    }
                }else{

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private class UpdateProfile extends AsyncTask<Void ,Void ,Void>{

        ProgressDialog pDialog;
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(Profile.this ,"" , "Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "user")
                    .add("passing_value" , "update_profile")
                    .add("userid" , Utils.getPrefs(Profile.this , Constants.PREF_USERID))
                    .add("username" , firstname)
                    .add("email", email)
                    .add("password" , password)
                    .add("phone_no" , mobileno)
                    .add("profile_pic" , encodedImage)
                    .build();

            Request request = new Request.Builder().url(Constants.API_URL).post(formBody).build();

            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(res == null || res.equals("")){
                pDialog.dismiss();
                Toast.makeText(getApplicationContext() , "Network Error Or Error" , Toast.LENGTH_SHORT).show();
                return;
            }

            pDialog.dismiss();
            try {
                JSONObject jobjres =new JSONObject(res);
                String status_code = jobjres.getString("status_code");
                if(status_code.equals("200")){

                    JSONObject jobj = jobjres.getJSONObject("data");
                    String userid = jobj.getString("id");
                    String emailid = jobj.getString("email");
                    String phoneno = jobj.getString("phone_no");
                    String username = jobj.getString("username");

                    Utils.setPrefs(Profile.this , Constants.PREF_USERID , userid);
                    Utils.setPrefs(Profile.this , Constants.PREF_EMAIL , emailid);
                    Utils.setPrefs(Profile.this , Constants.PREF_PHONENO , phoneno);
                    Utils.setPrefs(Profile.this , Constants.PREF_USERNAME , username);

                    Toast.makeText(Profile.this , "Profile Updated Successfully" ,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Profile.this , jobjres.getString("message") ,Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    private void selectImage() {

        final CharSequence[] options = { "Take Photo" , "Choose from Gallery" , "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle("Add Photo!");
        builder.setCancelable(false);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    captureImage();
                }
                else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new   Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }




    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        editedImageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT , editedImageUri);
        startActivityForResult(intent , CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    private static Uri getOutputMediaFileUri(int type){
        File fileToReturn =  getOutputMediaFile(type);
        return  fileToReturn!=null? Uri.fromFile(fileToReturn):
                null;
    }


    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss" , Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }




    public void checksCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("MyApp", "SDK >= 23");
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d("MyApp", "Request permission");
                ActivityCompat.requestPermissions(Profile.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_REQUEST_CODE);

                if (! shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    showMessageOKCancel("You need to allow camera usage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(Profile.this , new String[] {android.Manifest.permission.CAMERA},
                                            MY_REQUEST_CODE);
                                }
                            });
                }
            }
            else {
                Log.d("MyApp", "Permission granted: taking pic");
                //captureImage();
            }
        }
        else {
            Log.d("MyApp", "Android < 6.0");
        }
    }


    public void checksStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("MyApp", "SDK >= 23");
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d("MyApp", "Request permission");
                ActivityCompat.requestPermissions(Profile.this ,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_STORAGE);

                if (! shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showMessageOKCancel("You need to allow storage usage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(Profile.this, new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            MY_REQUEST_CODE_STORAGE);
                                }
                            });
                }
            }
            else {
                Log.d("MyApp", "Permission granted: store pic");
                //captureImage();
            }
        }
        else {
            Log.d("MyApp", "Android < 6.0");
        }
    }



    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Profile.this , R.style.AppTheme)
                .setMessage(message)
                .setPositiveButton("OK" , okListener)
                .setNegativeButton("Cancel" , null)
                .create()
                .show();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE){

            previewCapturedImage();

        } else if (requestCode == 2) {
            if(data != null) {
                editedImageUri = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(editedImageUri , filePath , null , null , null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();

                Log.e("picture path" , picturePath);
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                img_profile.setImageBitmap(thumbnail);

                Log.w("image from gallery.", editedImageUri.toString() + "thubnail==");
            }else{

            }
        }
    }





    private void previewCapturedImage() {
        try {
            img_profile.setVisibility(View.VISIBLE);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            Log.e("picture path" , editedImageUri.getPath());
            final Bitmap bitmap = BitmapFactory.decodeFile(editedImageUri.getPath() , options);
            img_profile.setImageBitmap(bitmap);
            // tv_clickhere.setText("");
        } catch (NullPointerException e) {
            //onBackPressed();
            e.printStackTrace();
        }
    }




}
