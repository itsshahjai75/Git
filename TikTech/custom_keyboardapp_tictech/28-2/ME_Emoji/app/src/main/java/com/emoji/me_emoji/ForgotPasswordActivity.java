package com.emoji.me_emoji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.emoji.me_emoji.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgotPasswordActivity extends AppCompatActivity {


    FrameLayout framelayout;
    LinearLayout linear_submit , linear_cancel;
    EditText et_email;

    String str_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();

        framelayout = (FrameLayout)findViewById(R.id.signup);
        linear_submit = (LinearLayout)findViewById(R.id.submit);
        linear_cancel = (LinearLayout)findViewById(R.id.cancel);

        et_email = (EditText)findViewById(R.id.email);

        framelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ForgotPasswordActivity.this , LoginActivity.class));
            }
        });

        linear_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_email = et_email.getText().toString();

                if(str_email.isEmpty()){
                   et_email.setError("Email Field is Empty");
                }else if(!emailValidator(str_email)){
                   et_email.setError("EmailID must be valid");
                }else{
                    new ForgotPassword().execute();
                }

            }
        });

        linear_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ForgotPasswordActivity.this , LoginActivity.class));
            }
        });

    }


       private class ForgotPassword extends AsyncTask<Void , Void , Void>{

           ProgressDialog pDialog;
           String res;
           @Override
           protected void onPreExecute() {
               super.onPreExecute();
               pDialog = ProgressDialog.show(ForgotPasswordActivity.this , "" ,"Please Wait...");
           }

           @Override
           protected Void doInBackground(Void... params) {

               OkHttpClient client = new OkHttpClient();
               RequestBody formBody = new FormBody.Builder()
                       .add("action" , "forgot_password")
                       .add("email" , str_email)
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
                   JSONObject jsonObj =new JSONObject(res);

                   String status_code = jsonObj.getString("status_code");
                   String msg = jsonObj.getString("message");

                   Toast.makeText(getApplicationContext() , msg , Toast.LENGTH_SHORT).show();

               } catch (JSONException e) {
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
