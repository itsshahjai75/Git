package com.barapp.simulator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.barapp.simulator.utils.Const;
import com.barapp.simulator.utils.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Profile_Get_Update extends AppCompatActivity implements View.OnClickListener {

    EditText et_fullname , et_newpass , et_confirmpass , et_email;
    Spinner spinner_state;
    CircleImageView img_profilepic;

    TextView  txt_submit , txt_username ,txt_coins;
    LinearLayout linear_ratings;
    ImageView img_rate1 ,img_rate2 ,img_rate3 ,img_rate4 ,img_rate5 ,img_logout ,img_back;


    String username , email , password , state , encodedImage ,userid;

    String[] states = new String[]{"Select State","American Samoa","Arizona","Alaska","Alabama","Arkansas","California","Colorado","Connecticut","Delaware","District of Columbia", "Florida",
            "Georgia","Guam","Hawaii","Idaho","Illinois","Indiana","Iow","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts",
            "Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico",
            "New York","North Carolina","North Dakota","Northern Mariana Islands","Ohio","Oklahoma","Oregon","Puerto Rico" , "Pennsylvania" , "Rhode Island" ,
            "South CarolinaSouth Dakota" , "Tennessee" , "Texas" , "U.S." , "Utah" , "Vermont","Virginia","Virgin Islands","Washington","West Virginia","Wisconsin","Wyoming"};


    public final int MY_REQUEST_CODE = 500;
    public final int MY_REQUEST_CODE_STORAGE = 600;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    private static final String IMAGE_DIRECTORY_NAME = "Camera";
    Uri editedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_get_update);

        et_fullname = (EditText)findViewById(R.id.fullname);
        et_newpass  = (EditText)findViewById(R.id.password);
        et_confirmpass  = (EditText)findViewById(R.id.confirm_pass);
        et_email  = (EditText)findViewById(R.id.email);

        spinner_state = (Spinner)findViewById(R.id.spinner_state);
        img_profilepic = (CircleImageView)findViewById(R.id.profile_image);
        img_logout   = (ImageView)findViewById(R.id.logout);

        txt_submit = (TextView)findViewById(R.id.submit);
        txt_username = (TextView)findViewById(R.id.username);
        txt_coins    = (TextView)findViewById(R.id.coins);

        linear_ratings = (LinearLayout)findViewById(R.id.linear_ratings);

        img_rate1 = (ImageView)findViewById(R.id.rate_img1);
        img_rate2 = (ImageView)findViewById(R.id.rate_img2);
        img_rate3 = (ImageView)findViewById(R.id.rate_img3);
        img_rate4 = (ImageView)findViewById(R.id.rate_img4);
        img_rate5 = (ImageView)findViewById(R.id.rate_img5);
        img_back  = (ImageView)findViewById(R.id.backbtn);

        txt_submit.setOnClickListener(this);
        img_logout.setOnClickListener(this);
        img_profilepic.setOnClickListener(this);
        img_back.setOnClickListener(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        userid = getIntent().getStringExtra("userid");
        spinner_state.setAdapter(new CustomSpinner_Adapter(Profile_Get_Update.this , states , 0));

        getSupportActionBar().hide();

        new GetProfileTask().execute();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.submit:

                username = et_fullname.getText().toString();
                username = username.replaceFirst("^ *", "");

                //Log.d("user name",username);

                email  = et_email.getText().toString();
                password  = et_newpass.getText().toString();
                state    = spinner_state.getSelectedItem().toString();

                Bitmap bitmap = ((BitmapDrawable)img_profilepic.getDrawable()).getBitmap();
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG , 90 , stream);
                byte[] image=stream.toByteArray();
                encodedImage = Base64.encodeToString(image , Base64.DEFAULT);

                if (username.length()==0 || username.equalsIgnoreCase("")) {
                    et_fullname.setError("Enter Name.");
                } else if (spinner_state.getSelectedItem().toString().equalsIgnoreCase("Select State")) {
                    TextView errorText = (TextView)spinner_state.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select State. !");//changes the selected item text to this
                }else if (et_newpass.getText().toString().length()>0 &&
                        et_confirmpass.getText().toString().length()==0) {
                    et_confirmpass.setError("Enter Confirm password.");
                }else if (et_confirmpass.getText().toString().length()>0 &&
                        et_newpass.getText().toString().length()==0) {
                    et_newpass.setError("Enter password.");
                }else if ((et_confirmpass.getText().toString().length()>0 &&
                        et_newpass.getText().toString().length()>0) &&
                        !et_newpass.getText().toString().equals(et_confirmpass.getText().toString())) {
                    et_confirmpass.setError("Password Not Matched.");
                }else{
                    new ProfileUpdateTask().execute();
                }

            break;

            case R.id.logout:

                Utils.ClearAllPrefs(Profile_Get_Update.this);
                startActivity(new Intent(Profile_Get_Update.this , LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();


                break;
            case R.id.profile_image:

              selectImage();
                break;
            case R.id.backbtn:

                onBackPressed();
                break;
        }
    }


    private void selectImage() {

        //
        final CharSequence[] options = { "Take Photo" , "Choose from Gallery" , "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Profile_Get_Update.this);
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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, editedImageUri);

        // start the image capture Intent
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
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
                ActivityCompat.requestPermissions(Profile_Get_Update.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_REQUEST_CODE);

                if (! shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    showMessageOKCancel("You need to allow camera usage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(Profile_Get_Update.this , new String[] {android.Manifest.permission.CAMERA},
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
                ActivityCompat.requestPermissions(Profile_Get_Update.this ,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_STORAGE);

                if (! shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showMessageOKCancel("You need to allow storage usage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(Profile_Get_Update.this, new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
        new AlertDialog.Builder(Profile_Get_Update.this,R.style.MyAlertDialogMaterialStyle)
                .setMessage(message)
                .setPositiveButton("OK" , okListener)
                .setNegativeButton("Cancel" , null)
                .create()
                .show();
    }


    private class ProfileUpdateTask extends AsyncTask<Void ,Void ,Void> {

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(Profile_Get_Update.this , "Updating Profile" , "Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "user")
                    .add("passing_value" , "update_profile")
                    .add("userid" , Utils.getPrefs(Profile_Get_Update.this , Const.USER_ID))
                    .add("username" , username)
                    .add("email" , email)
                    .add("password" , password)
                    .add("state" , state)
                    .add("profile_pic" , encodedImage)
                    .build();

            Request request = new Request.Builder()
                    .url(Const.API_URL)
                    .post(formBody).build();

            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
          if(res == null || res.equals("")){
              pDialog.dismiss();

              Toast.makeText(Profile_Get_Update.this , "Network Error Or Error" , Toast.LENGTH_SHORT).show();
              return;
          }

            pDialog.dismiss();
            try {
                JSONObject jobjres =new JSONObject(res);
                String status_code = jobjres.getString("status_code");
                if(status_code.equals("200")){
                  String type =   Utils.getPrefs(Profile_Get_Update.this , Const.TYPE);
                    if(type.equals("0")){
                        startActivity(new Intent(Profile_Get_Update.this , OwnerMenuActivity.class));
                    }else{
                        startActivity(new Intent(Profile_Get_Update.this , UserMenuActivity.class));
                    }
                }else {
                    Toast.makeText(Profile_Get_Update.this , jobjres.getString("message") , Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            pDialog.dismiss();
        }
    }


    private class GetProfileTask extends AsyncTask<Void , Void , Void> {

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(Profile_Get_Update.this , "Getting Profile" , "Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "user")
                    .add("passing_value" , "get_profile")
                    .add("userid" , Utils.getPrefs(Profile_Get_Update.this , Const.USER_ID))
                    .build();

            Request request = new Request.Builder()
                    .url(Const.API_URL)
                    .post(formBody).build();

            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(res == null || res.equals("")){
                pDialog.dismiss();

                Toast.makeText(Profile_Get_Update.this , "Network Error Or Error" , Toast.LENGTH_SHORT).show();
                return;
            }

            pDialog.dismiss();
            try {
                JSONObject jobjres =new JSONObject(res);
                String status_code = jobjres.getString("status_code");
                if(status_code.equals("200")){

                    JSONObject jsonObj = jobjres.getJSONObject("data");
                    String  id = jsonObj.getString("id");
                    String username = jsonObj.getString("username");
                    String email   = jsonObj.getString("email");
                    String password = jsonObj.getString("password");
                    String rating   = jsonObj.getString("rating");
                    Log.d("Rating===pro",rating);
                    String coins    = jsonObj.getString("total_coin");
                    String profile_pic = jsonObj.getString("profile_pic");
                    String type  = jsonObj.getString("type");
                    String state = jsonObj.getString("state");

                    et_fullname.setText(username);
                    et_email.setText(email);

                    if(!profile_pic.equals("")){
                        Picasso.with(Profile_Get_Update.this)
                                .load(Const.IMAGE_URL+profile_pic)
                                .placeholder(R.drawable.noimage) // optional
                                .error(R.drawable.noimage)         // optional
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(img_profilepic);
                    }

                    if(type.equals("0")){
                        setRatings(Double.parseDouble(rating));
                    }else{
                        linear_ratings.setVisibility(View.GONE);
                    }

                    txt_username.setText(username);
                    txt_coins.setText(coins);
                    for(int i=0; i<states.length; i++){
                        if(state.equals(states[i])){
                            spinner_state.setSelection(i);
                        }
                    }

                }else {
                    Toast.makeText(Profile_Get_Update.this , jobjres.getString("message") , Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            pDialog.dismiss();
        }
    }

    private void setRatings(double rating) {

        if (rating <= 0.5) {
            img_rate1.setImageResource(R.drawable.star_enable_half);
            img_rate2.setImageResource(R.drawable.star_diseble);
            img_rate3.setImageResource(R.drawable.star_diseble);
            img_rate4.setImageResource(R.drawable.star_diseble);
            img_rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 0.5 && rating <= 1) {
            img_rate1.setImageResource(R.drawable.star_enable);
            img_rate2.setImageResource(R.drawable.star_diseble);
            img_rate3.setImageResource(R.drawable.star_diseble);
            img_rate4.setImageResource(R.drawable.star_diseble);
            img_rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 1 && rating <= 1.5) {
            img_rate1.setImageResource(R.drawable.star_enable);
            img_rate2.setImageResource(R.drawable.star_enable_half);
            img_rate3.setImageResource(R.drawable.star_diseble);
            img_rate4.setImageResource(R.drawable.star_diseble);
            img_rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 1.5 && rating <= 2) {
            img_rate1.setImageResource(R.drawable.star_enable);
            img_rate2.setImageResource(R.drawable.star_enable);
            img_rate3.setImageResource(R.drawable.star_diseble);
            img_rate4.setImageResource(R.drawable.star_diseble);
            img_rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 2 && rating <= 2.5) {
            img_rate1.setImageResource(R.drawable.star_enable);
            img_rate2.setImageResource(R.drawable.star_enable);
            img_rate3.setImageResource(R.drawable.star_enable_half);
            img_rate4.setImageResource(R.drawable.star_diseble);
            img_rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 2.5 && rating <= 3) {
            img_rate1.setImageResource(R.drawable.star_enable);
            img_rate2.setImageResource(R.drawable.star_enable);
            img_rate3.setImageResource(R.drawable.star_enable);
            img_rate4.setImageResource(R.drawable.star_diseble);
            img_rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 3 && rating <= 3.5) {
            img_rate1.setImageResource(R.drawable.star_enable);
            img_rate2.setImageResource(R.drawable.star_enable);
            img_rate3.setImageResource(R.drawable.star_enable);
            img_rate4.setImageResource(R.drawable.star_enable_half);
            img_rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 3.5 && rating <= 4) {
            img_rate1.setImageResource(R.drawable.star_enable);
            img_rate2.setImageResource(R.drawable.star_enable);
            img_rate3.setImageResource(R.drawable.star_enable);
            img_rate4.setImageResource(R.drawable.star_enable);
            img_rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 4 && rating <= 4.5) {
            img_rate1.setImageResource(R.drawable.star_enable);
            img_rate2.setImageResource(R.drawable.star_enable);
            img_rate3.setImageResource(R.drawable.star_enable);
            img_rate4.setImageResource(R.drawable.star_enable);
            img_rate5.setImageResource(R.drawable.star_enable_half);
        } else if (rating > 4.5) {
            img_rate1.setImageResource(R.drawable.star_enable);
            img_rate2.setImageResource(R.drawable.star_enable);
            img_rate3.setImageResource(R.drawable.star_enable);
            img_rate4.setImageResource(R.drawable.star_enable);
            img_rate5.setImageResource(R.drawable.star_enable);
        }else{
            img_rate1.setImageResource(R.drawable.star_diseble);
            img_rate2.setImageResource(R.drawable.star_diseble);
            img_rate3.setImageResource(R.drawable.star_diseble);
            img_rate4.setImageResource(R.drawable.star_diseble);
            img_rate5.setImageResource(R.drawable.star_diseble);
        }
    }


    public class CustomSpinner_Adapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private String[] asr;
        private int statetype;

        public CustomSpinner_Adapter(Context context , String[] asr , int statetype) {
            this.asr=asr;
            activity = context;
            this.statetype = statetype;
        }



        public int getCount() {
            return asr.length;
        }

        public Object getItem(int i) {
            return asr[i];
        }

        public long getItemId(int i) {
            return (long)i;
        }



        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(activity);
            txt.setPadding(16 , 16 , 16 , 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr[position]);
            txt.setTextColor(Color.parseColor("#000000"));

            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(activity);
            txt.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            txt.setPadding(10 , 10 , 10 , 10);
            txt.setTextSize(20);
            txt.setCompoundDrawablePadding(10);
            txt.setText(asr[i]);
            txt.setTextColor(Color.parseColor("#ffffff"));

            return  txt;
        }

    }







    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode==RESULT_OK){

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
                img_profilepic.setImageBitmap(thumbnail);

                Log.w("image from gallery.", editedImageUri.toString() + "thubnail==");
            }else{

            }
        }
    }





    private void previewCapturedImage() {
        try {
            img_profilepic.setVisibility(View.VISIBLE);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            Log.e("picture path" , editedImageUri.getPath());
            final Bitmap bitmap = BitmapFactory.decodeFile(editedImageUri.getPath() , options);
            img_profilepic.setImageBitmap(bitmap);
            // tv_clickhere.setText("");
        } catch (NullPointerException e) {
            //onBackPressed();
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(Utils.getPrefs(Profile_Get_Update.this,Const.TYPE).equalsIgnoreCase("0")){
            startActivity(new Intent(Profile_Get_Update.this,OwnerMenuActivity.class));
            finish();
        }else{
            startActivity(new Intent(Profile_Get_Update.this,UserMenuActivity.class));
            finish();
        }
    }
}
