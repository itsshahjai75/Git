package com.emoji.me_emoji;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emoji.me_emoji.dataset.EmojiDataset;
import com.emoji.me_emoji.utils.Constants;
import com.emoji.me_emoji.utils.Utils;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    TextView txt_signup, txt_forgotpass;
    LinearLayout linear_loginwith_fb, linear_login;
    EditText et_username, et_password;

    String str_email, str_password;
    Dialog dialog_fb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        txt_signup = (TextView) findViewById(R.id.txt_signup);
        txt_forgotpass = (TextView) findViewById(R.id.txt_forgotpass);
        linear_loginwith_fb = (LinearLayout) findViewById(R.id.linear_loginwith_fb);
        linear_login = (LinearLayout) findViewById(R.id.login);

        et_username = (EditText) findViewById(R.id.username);
        et_password = (EditText) findViewById(R.id.password);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, SignUPActivity.class));
            }
        });

        txt_forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        linear_loginwith_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        linear_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_email = et_username.getText().toString();
                str_password = et_password.getText().toString();

                if (str_email.isEmpty() || str_password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All Fields are Mandatory", Toast.LENGTH_SHORT).show();
                } else if (!emailValidator(str_email)) {
                    et_username.setError("Invalid EmailID");
                } else {
                    if (Utils.isNetworkAvailable(LoginActivity.this)) {

                        new LoginTask().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Network not Available", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }



    public File createDir(String DIRNAME) {
        File DIR=null;


        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            DIR=new File(android.os.Environment.getExternalStorageDirectory()+DIRNAME);
        else
            DIR = new File("/data/" + getApplicationContext().getPackageName() + "/" +DIRNAME + "/" );
//            DIR=this.getCacheDir();
        if(!DIR.exists())
            DIR.mkdirs();
        return DIR;
    }

    public File DownloadFromUrl(String imageURL, String fileName) {  //this is the downloader method
        File file=new File("");
        try {
            URL url = new URL(imageURL); //you can write here any link

            long startTime = System.currentTimeMillis();
            Log.d("ImageManager", "download begining");
            Log.d("ImageManager", "download url:" + url);
            Log.d("ImageManager", "downloaded file name:" + fileName);
                        /* Open a connection to that URL. */
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            File SDCardRoot = createDir("MeEmoji");
            Log.i("Local filename:", "" + fileName);
            file = new File(SDCardRoot, fileName);
            if (file.createNewFile()) {
                file.createNewFile();
            }

                        /*
                         * Define InputStreams to read from the URLConnection.
                         */
            InputStream is = urlConnection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            //We create an array of bytes
            byte[] data = new byte[50];
            int current = 0;

            while((current = bis.read(data,0,data.length)) != -1){
                buffer.write(data,0,current);
            }
                        /* Convert the Bytes read to a String. */
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer.toByteArray());
            fos.close();
            Log.d("ImageManager", "download ready in"
                    + ((System.currentTimeMillis() - startTime) / 1000)
                    + " sec");

        } catch (IOException e) {
            Log.d("ImageManager", "Error: " + e);
        }
        return file;
    }


    public String getfilepath(@NonNull String urlstring) {
        String filepath = "";

        try {
            URL url = new URL(urlstring);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
            String filename = "image.gif";
            Log.i("Local filename:", "" + filename);
            File file = new File(SDCardRoot, filename);
            if (file.createNewFile()) {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
            }
            fileOutput.close();
            if (downloadedSize == totalSize) filepath = file.getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            filepath = null;
            e.printStackTrace();
        }
        Log.i("filepath:", " " + filepath);
        return filepath;
    }

    private class LoginTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(LoginActivity.this, "Loading", "Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action", "login")
                    .add("email", str_email)
                    .add("password", str_password)
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

            if (res == null || res.equals("")) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Network Error Or Error", Toast.LENGTH_SHORT).show();
                return;
            }

            pDialog.dismiss();
            try {
                JSONObject jobjres = new JSONObject(res);
                String status_code = jobjres.getString("status_code");
                if (status_code.equals("200")) {

                    JSONObject jobj = jobjres.getJSONObject("user");
                    String userid = jobj.getString("id");
                    String emailid = jobj.getString("email");
                    String phoneno = jobj.getString("phone_no");
                    String username = jobj.getString("username");

                    Utils.setPrefs(LoginActivity.this, Constants.PREF_USERID, userid);
                    Utils.setPrefs(LoginActivity.this, Constants.PREF_EMAIL, emailid);
                    Utils.setPrefs(LoginActivity.this, Constants.PREF_PHONENO, phoneno);
                    Utils.setPrefs(LoginActivity.this, Constants.PREF_USERNAME, username);

                    startActivity(new Intent(LoginActivity.this, MainTabActivity.class));
                    finish();
                    Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, jobjres.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public boolean emailValidator(final String mailAddress) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);

        return matcher.matches();
    }

    private void LoginWithFB_Dailog() {
        Dialog dialog_fb = new Dialog(LoginActivity.this);
        dialog_fb.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_fb.setContentView(R.layout.dialog_login_withfb);

        dialog_fb.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = dialog_fb.getWindow().getAttributes();
        lp.width = 800;
        lp.height = 650;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0;
        dialog_fb.getWindow().setAttributes(lp);

        dialog_fb.show();
    }

}
