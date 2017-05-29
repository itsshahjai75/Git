package com.barapp.simulator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.barapp.simulator.utils.Const;
import com.barapp.simulator.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Add_Rate_Comment extends AppCompatActivity {


    String str_rating ="0" , str_comment , product_id;
    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rate_comment);

        getSupportActionBar().hide();

        final RatingBar rate = (RatingBar)findViewById(R.id.ratingBar);
        final EditText et_comment = (EditText)findViewById(R.id.et_comments);
        LinearLayout submit = (LinearLayout) findViewById(R.id.submit);
        img_back = (ImageView)findViewById(R.id.backbtn);

        product_id = getIntent().getStringExtra("productid");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_rating = String.valueOf(rate.getRating());
                str_comment = et_comment.getText().toString();
               /* if(!str_rating.equalsIgnoreCase("") ||
                        str_rating.length()<=0){

                }*/
                if(Float.parseFloat(str_rating)<=0){
                    Toast.makeText(Add_Rate_Comment.this,"Please Add Rating!",Toast.LENGTH_LONG).show();
                }else if(str_comment.equalsIgnoreCase("")
                        || str_comment.length()==0){
                    et_comment.setError("Please Say Something!");
                }else {
                    new AddRateComment_Task().execute();
                }


            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

    }





    private class AddRateComment_Task extends AsyncTask<Void , Void , Void> {

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(Add_Rate_Comment.this , "" , "Please Wait...");
        }


        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "user")
                    .add("passing_value" , "add_rating")
                    .add("productid" , product_id)
                    .add("userid" , Utils.getPrefs(Add_Rate_Comment.this , Const.USER_ID))
                    .add("rating" , str_rating)
                    .add("comment" , str_comment)
                    .build();

            Request request = new Request.Builder()
                    .url(Const.API_URL)
                    .post(formBody)
                    .build();

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

                Toast.makeText(getApplicationContext() , "Network Error Or Error" , Toast.LENGTH_SHORT).show();
                return;
            }

            pDialog.dismiss();
            try {
                JSONObject jobjMain = new JSONObject(res);
                String code = jobjMain.getString("status_code");
                if(code.equals("200")){
                    Toast.makeText(getApplicationContext() , "Review Added Successfully" , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Add_Rate_Comment.this , PurchaseDetail_User.class)
                    .putExtra("product_id",product_id));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext() , jobjMain.getString("message") , Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
