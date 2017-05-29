package com.barapp.simulator;

import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.barapp.simulator.utils.Const;
import com.barapp.simulator.utils.Utils;

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

public class SignUPActivity extends AppCompatActivity {

    String[] states = new String[]{"Select State","American Samoa","Arizona","Alaska","Alabama","Arkansas","California","Colorado","Connecticut","Delaware","District of Columbia", "Florida",
            "Georgia","Guam","Hawaii","Idaho","Illinois","Indiana","Iow","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts",
            "Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico",
            "New York","North Carolina","North Dakota","Northern Mariana Islands","Ohio","Oklahoma","Oregon","Puerto Rico" ,"Pennsylvania","Rhode Island",
            "South CarolinaSouth Dakota" , "Tennessee" , "Texas" , "U.S." ,"Utah","Vermont","Virginia","Virgin Islands","Washington","West Virginia","Wisconsin","Wyoming"};


    EditText et_username ,et_email ,et_password ,et_confirmPass ;
    Spinner  spinner_state;
    TextView txt_signup;
    RadioButton rb_owner ,rb_user;
    RadioGroup rg;
    CircleImageView profile_img;

    String username ,email ,password ,confirm_pass ,state ="" ,logintype = "0" , encodedImage ,barname;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public final int MY_REQUEST_CODE=500;
    public final int MY_REQUEST_CODE_STORAGE=600;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final String IMAGE_DIRECTORY_NAME = "Camera";

    Uri editedImageUri;
    String emailPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        et_username = (EditText)findViewById(R.id.username);
        et_email    = (EditText)findViewById(R.id.email);
        et_password = (EditText)findViewById(R.id.password);
        et_confirmPass = (EditText)findViewById(R.id.confirmPassword);


        spinner_state = (Spinner)findViewById(R.id.state);
        profile_img = (CircleImageView)findViewById(R.id.profile_image);
        txt_signup = (TextView)findViewById(R.id.signup);

        rb_owner = (RadioButton)findViewById(R.id.owner);
        rb_user  = (RadioButton)findViewById(R.id.user);
        rg  = (RadioGroup)findViewById(R.id.rg);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        spinner_state.setAdapter(new CustomSpinner_Adapter(states));
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group  , int checkedId) {
                if(checkedId == R.id.owner) {
                    logintype = "0";
                }else if(checkedId == R.id.user){
                    logintype = "1";
                }
            }
        });

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = et_username.getText().toString();
                username = username.replaceFirst("^ *", "");


                email  = et_email.getText().toString();
                password = et_password.getText().toString();
                confirm_pass = et_confirmPass.getText().toString();
                state  = spinner_state.getSelectedItem().toString();


                Bitmap bitmap = ((BitmapDrawable)profile_img.getDrawable()).getBitmap();
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG , 90 , stream);
                byte[] image=stream.toByteArray();
                encodedImage = Base64.encodeToString(image , 0);


                 if (et_username.getText().toString().length()==0 ||
                        et_username.getText().toString().equalsIgnoreCase("")) {
                    et_username.setError("Enter Name.");
                }else if (et_email.getText().toString().length()==0 ||
                        !et_email.getText().toString().matches(emailPattern)
                        || et_email.getText().toString().matches("[0-9]+@[0-9]+@[0-9]") ||
                        et_email.getText().toString().equalsIgnoreCase("abc@abc.com")) {
                    et_email.setError("Enter e-mail address.");
                } else if (et_password.getText().toString().length()==0 ||
                        et_password.getText().toString().equalsIgnoreCase("")) {
                    et_password.setError("Enter password.");
                }else if (et_confirmPass.getText().toString().length()==0 ||
                         et_confirmPass.getText().toString().equalsIgnoreCase("")) {
                     et_confirmPass.setError("Enter Confirm password.");
                 } else if ((et_confirmPass.getText().toString().length()>0 &&
                         et_password.getText().toString().length()>0) &&
                         !et_password.getText().toString().equals(et_confirmPass.getText().toString())) {
                     et_confirmPass.setError("Password Not Matched.");
                 } else if (spinner_state.getSelectedItem().toString().equalsIgnoreCase("Select State")) {
                    TextView errorText = (TextView)spinner_state.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Select State. !");//changes the selected item text to this
                }else {
                    // state  =  state_spin.getSelectedItem().toString();
                    new SignupTask().execute();
                }


            }
        });


        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        checksCameraPermission();
        checksStoragePermission();

    }





    private class SignupTask extends AsyncTask<Void ,Void ,Void> {

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(SignUPActivity.this , "Signing Up" ,"Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                        .add("action" , "sign_up")
                        .add("username" , username)
                        .add("email" , email)
                        .add("password" , password)
                        .add("state" , state)
                        .add("type" , logintype)
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

            try {
                JSONObject jobjres =new JSONObject(res);
                String status_code = jobjres.getString("status_code");
                if(status_code.equals("200")){

                    String userid = jobjres.getJSONObject("data").getString("id");

                    Utils.setPrefs(SignUPActivity.this , Const.USER_ID , userid);
                    Utils.setPrefs(SignUPActivity.this , Const.EMAIL , email);
                    Utils.setPrefs(SignUPActivity.this , Const.STATE , state);
                    Utils.setPrefs(SignUPActivity.this , Const.ISLOGIN , "yes");
                    Utils.setPrefs(SignUPActivity.this , Const.TYPE , logintype);
                    Utils.setPrefs(SignUPActivity.this , Const.USERNAME , username);

                    if(logintype.equals("0")){
                        startActivity(new Intent(SignUPActivity.this , BarNameActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SignUPActivity.this , UserMenuActivity.class));
                        finish();
                    }
                    Toast.makeText(SignUPActivity.this , "Signed Up Successfully" ,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SignUPActivity.this , jobjres.getString("message") ,Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            pDialog.dismiss();
        }
    }



    public class CustomSpinner_Adapter extends BaseAdapter implements SpinnerAdapter {

        private String[] asr;
        private int hairIcon;

        public CustomSpinner_Adapter( String[] asr ) {
            this.asr=asr;
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
            TextView txt = new TextView(SignUPActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr[position]);
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(SignUPActivity.this);
            txt.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            txt.setPadding(10 , 10 , 10 , 10);
            txt.setTextSize(20);
            txt.setCompoundDrawablePadding(10);
            txt.setText(asr[i]);
            txt.setCompoundDrawablesWithIntrinsicBounds(0 , 0 , android.R.drawable.arrow_down_float , 0);
            txt.setTextColor(Color.parseColor("#ffffff"));
            return  txt;
        }

    }



    private void selectImage() {

        //
        final CharSequence[] options = { "Take Photo" , "Choose from Gallery" , "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUPActivity.this);
        builder.setTitle("Add Photo!");
        builder.setCancelable(false);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    captureImage();
                }
                else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new   Intent(Intent.ACTION_PICK , android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                ActivityCompat.requestPermissions(SignUPActivity.this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_REQUEST_CODE);

                if (! shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    showMessageOKCancel("You need to allow camera usage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(SignUPActivity.this , new String[] {android.Manifest.permission.CAMERA},
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
                ActivityCompat.requestPermissions(SignUPActivity.this ,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_STORAGE);

                if (! shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showMessageOKCancel("You need to allow storage usage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(SignUPActivity.this, new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
        new AlertDialog.Builder(SignUPActivity.this,R.style.MyAlertDialogMaterialStyle)
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
                profile_img.setImageBitmap(thumbnail);

                Log.w("image from gallery.", editedImageUri.toString() + "thubnail==");
            }else{

            }
        }
    }





    private void previewCapturedImage() {
        try {
            profile_img.setVisibility(View.VISIBLE);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            Log.e("picture path" , editedImageUri.getPath());
            final Bitmap bitmap = BitmapFactory.decodeFile(editedImageUri.getPath() , options);
            profile_img.setImageBitmap(bitmap);
            // tv_clickhere.setText("");
        } catch (NullPointerException e) {
            //onBackPressed();
            e.printStackTrace();
        }
    }





}
