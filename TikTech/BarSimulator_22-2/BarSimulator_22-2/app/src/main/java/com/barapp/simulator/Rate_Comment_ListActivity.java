package com.barapp.simulator;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.barapp.simulator.Adapter.RateCommentList_Adapter;
import com.barapp.simulator.Object_Getter_Setter.RateComment_Object;
import com.barapp.simulator.utils.Const;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Rate_Comment_ListActivity extends AppCompatActivity {

    RecyclerView  list_comments;
    ImageView img_back;
    String product_id;

    RateCommentList_Adapter adapter;
    ArrayList<RateComment_Object> listRateComment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_comment_list);
        getSupportActionBar().hide();

        list_comments = (RecyclerView)findViewById(R.id.list_comments);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        list_comments.setLayoutManager(mLayoutManager);
        list_comments.setItemAnimator(new DefaultItemAnimator());
        img_back = (ImageView)findViewById(R.id.backbtn);

        product_id = getIntent().getStringExtra("product_id");



        adapter = new RateCommentList_Adapter(listRateComment , Rate_Comment_ListActivity.this);
        list_comments.setAdapter(adapter);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        new RateComment_List_Task().execute();

    }





    private class RateComment_List_Task extends AsyncTask<Void , Void , Void>{

        String res;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(Rate_Comment_ListActivity.this , "" , "Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("action" , "user")
                        .add("passing_value" , "get_rating")
                        .add("productid" , product_id)
                        .build();
                Request request = new Request.Builder()
                        .url(Const.API_URL)
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();

                res = response.body().string();

                Log.e("response" , res);
            } catch (IOException e) {
                e.printStackTrace();
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
                JSONArray jarray = jsonObj.getJSONArray("data");
                for(int i=0; i<jarray.length(); i++){
                    JSONObject jobj = jarray.getJSONObject(i);

                    String id  = jobj.getString("id");
                    String profile = jobj.getString("profile");
                    String username = jobj.getString("username");
                    String rating = jobj.getString("rating");
                    Log.d("Rating Listing",rating);
                    String comment = jobj.getString("comment");

                    listRateComment.add(new RateComment_Object(id ,profile ,username ,rating ,comment));
                    //adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            adapter.notifyDataSetChanged();
        }
    }




}
