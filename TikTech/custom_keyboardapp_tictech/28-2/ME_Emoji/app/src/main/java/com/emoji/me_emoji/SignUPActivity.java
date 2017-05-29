package com.emoji.me_emoji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emoji.me_emoji.utils.Constants;
import com.emoji.me_emoji.utils.Utils;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUPActivity extends AppCompatActivity {


    EditText et_username , et_email , et_mobile ,et_password ,et_confirmpass;
    LinearLayout linear_signup;
    FrameLayout signin_framelayout;

    String str_username ,str_email , str_phone ,str_password ,str_confirmpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        et_username = (EditText)findViewById(R.id.username);
        et_email = (EditText)findViewById(R.id.email);
        et_mobile = (EditText)findViewById(R.id.mobileno);
        et_password = (EditText)findViewById(R.id.password);
        et_confirmpass = (EditText)findViewById(R.id.confirm_pass);

        linear_signup = (LinearLayout)findViewById(R.id.linear_signup);
        signin_framelayout = (FrameLayout)findViewById(R.id.redirect_signin);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        linear_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_username = et_username.getText().toString();
                str_email = et_email.getText().toString();
                str_phone = et_mobile.getText().toString();
                str_password = et_password.getText().toString();
                str_confirmpass = et_confirmpass.getText().toString();


                if(str_username.isEmpty() || str_email.isEmpty() || str_phone.isEmpty() || str_password.isEmpty() || str_confirmpass.isEmpty()  )
                {
                   Toast.makeText(getApplicationContext() , "All Fields are Mandatory" , Toast.LENGTH_SHORT).show();
                }
                else if(!emailValidator(str_email))
                {
                    et_email.setError("Invalid EmailID");
                }
                else if(str_phone.length() < 10 || str_phone.length() > 12)
                {
                    et_mobile.setError("Invalid Mobile Number");
                }
                else if(!str_password.equals(str_confirmpass))
                {
                    et_confirmpass.setError("Password and confirm  password must match");
                }
                else{

                    if(Utils.isNetworkAvailable(SignUPActivity.this)){
                        new SignupTask().execute();
                    }else{
                        Toast.makeText(getApplicationContext() , "Network not Available" ,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        signin_framelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignUPActivity.this , LoginActivity.class));
            }
        });

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
                    .add("username" , str_username)
                    .add("email" , str_email)
                    .add("password" , str_password)
                    .add("phone_no" , str_phone)
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
                JSONObject jobjres =new JSONObject(res);
                String status_code = jobjres.getString("status_code");
                if(status_code.equals("200")){

                    JSONObject jobj = jobjres.getJSONObject("user");
                    String userid = jobj.getString("id");
                    String emailid = jobj.getString("email");
                    String phoneno = jobj.getString("phone_no");
                    String username = jobj.getString("username");

                    Utils.setPrefs(SignUPActivity.this , Constants.PREF_USERID , userid);
                    Utils.setPrefs(SignUPActivity.this , Constants.PREF_EMAIL , emailid);
                    Utils.setPrefs(SignUPActivity.this , Constants.PREF_PHONENO , phoneno);
                    Utils.setPrefs(SignUPActivity.this , Constants.PREF_USERNAME , username);

                    Toast.makeText(SignUPActivity.this , "Signed Up Successfully" ,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SignUPActivity.this , jobjres.getString("message") ,Toast.LENGTH_SHORT).show();
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

}
