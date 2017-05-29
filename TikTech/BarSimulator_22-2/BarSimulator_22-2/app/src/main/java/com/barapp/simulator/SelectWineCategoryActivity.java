package com.barapp.simulator;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.barapp.simulator.Adapter.WineCategory_Adapter;
import com.barapp.simulator.Object_Getter_Setter.WineCategory_Object;
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

public class SelectWineCategoryActivity extends AppCompatActivity {


    GridView grid_wines;
    ImageView img_back;
    LinearLayout linear_next;
    ArrayList<WineCategory_Object> list_Wine = new ArrayList<>();
   public static ArrayList<WineCategory_Object> list_SelectedWine = new ArrayList<>();

    WineCategory_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_winescategory);

        getSupportActionBar().hide();

        list_SelectedWine.clear();

        grid_wines = (GridView)findViewById(R.id.grid_wines);
        img_back = (ImageView)findViewById(R.id.backbtn);
        linear_next = (LinearLayout)findViewById(R.id.linear_next);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        adapter = new WineCategory_Adapter(SelectWineCategoryActivity.this , list_Wine);
        grid_wines.setAdapter(adapter);

        grid_wines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        linear_next.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                for(int i=0; i<list_Wine.size(); i++){

                    if(list_Wine.get(i).is_selected()){
                        list_SelectedWine.add(new WineCategory_Object("id" , list_Wine.get(i).getImage_url() , list_Wine.get(i).getWine_name() , list_Wine.get(i).getPrice() , true));
                    }
                }

                if(list_SelectedWine.size() > 0){
                    Intent intent = new Intent(SelectWineCategoryActivity.this , SelectWineSubCategoryActivity.class);
                    intent.putExtra("list" , list_SelectedWine);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(SelectWineCategoryActivity.this , "Please Select atleast one item" , Toast.LENGTH_SHORT).show();
                }

            }
        });

        new CategoryList_Task().execute();
    }

     //



    public static ProgressDialog pDialog;
    private class CategoryList_Task extends AsyncTask<Void ,Void ,Void> {

        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(SelectWineCategoryActivity.this , "" ,"Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "owner")
                    .add("passing_value" , "get_category")
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

                    String id =jobj.getString("id");
                    String title =jobj.getString("title");
                    String img_wine =jobj.getString("product_image");
                    String price =jobj.getString("price");

                    list_Wine.add(new WineCategory_Object(id , img_wine , title , price ,false));
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(SelectWineCategoryActivity.this,OwnerMenuActivity.class));
        finish();
    }
}
