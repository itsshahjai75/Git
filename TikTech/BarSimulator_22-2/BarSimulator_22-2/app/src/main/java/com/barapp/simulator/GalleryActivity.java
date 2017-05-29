package com.barapp.simulator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.barapp.simulator.Adapter.PurchaseGallery_Adapter;
import com.barapp.simulator.Object_Getter_Setter.Gallery_Object;
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

public class GalleryActivity extends AppCompatActivity {



    ImageView img_back;



    GridView grid_pics;
    PurchaseGallery_Adapter adapter;
    ArrayList<Gallery_Object> listGallery =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        getSupportActionBar().hide();

       grid_pics = (GridView)findViewById(R.id.grid_pics);

       adapter =new PurchaseGallery_Adapter(GalleryActivity.this , listGallery);
       grid_pics.setAdapter(adapter);


        grid_pics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent=new Intent();
                intent.putExtra("image_id", listGallery.get(position).getId());
                intent.putExtra("image_url", listGallery.get(position).getFile());
                setResult(100 , intent);
                finish();
            }
        });


        img_back   = (ImageView)findViewById(R.id.backbtn);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("image_id", "");
                intent.putExtra("image_url", "");
                setResult(100 , intent);
                finish();
            }
        });

        new GalleryPic().execute();
    }



    private class GalleryPic extends AsyncTask<Void ,Void ,Void> {

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(GalleryActivity.this , "" ,"Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "owner")
                    .add("passing_value" , "image_gallery")
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
                JSONObject jsonObj =new JSONObject(res);
                JSONArray jarray =jsonObj.getJSONArray("data");
                for(int i=0; i<jarray.length(); i++){
                    JSONObject jobj =jarray.getJSONObject(i);

                    String id = jobj.getString("id");
                    String file_name = jobj.getString("file_name");
                    String file = jobj.getString("file");


                    listGallery.add(new Gallery_Object(id , file_name , file));
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
