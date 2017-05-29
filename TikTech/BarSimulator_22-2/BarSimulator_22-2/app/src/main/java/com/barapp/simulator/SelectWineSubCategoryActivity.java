package com.barapp.simulator;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.barapp.simulator.Adapter.WineSubCategory_Adapter;
import com.barapp.simulator.Object_Getter_Setter.Category_SubCategory_Object;
import com.barapp.simulator.Object_Getter_Setter.WineCategory_Object;
import com.barapp.simulator.Object_Getter_Setter.WineSubCategory_Object;
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

public class SelectWineSubCategoryActivity extends AppCompatActivity {


    GridView grid_wines;
    ImageView img_back;
    LinearLayout linear_next;

    ArrayList<WineSubCategory_Object> list_Wine = new ArrayList<>();
    ArrayList<Category_SubCategory_Object> list_cat_subcat_obj = new ArrayList<>();

    WineSubCategory_Adapter adapter;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wine_sub_category);

        getSupportActionBar().hide();

        grid_wines = (GridView)findViewById(R.id.grid_mixtures);
        img_back = (ImageView)findViewById(R.id.backbtn);
        linear_next = (LinearLayout)findViewById(R.id.linear_next);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        adapter = new WineSubCategory_Adapter(SelectWineSubCategoryActivity.this , list_Wine);
        grid_wines.setAdapter(adapter);


        ArrayList<WineCategory_Object> list_wine = (ArrayList<WineCategory_Object>) getIntent().getSerializableExtra("list");
        for(WineCategory_Object wineobj : list_wine){
            list_cat_subcat_obj.add(new Category_SubCategory_Object(wineobj.getId() ,wineobj.getImage_url() ,wineobj.getWine_name() ,wineobj.getPrice() ,true));
        }

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
                        list_cat_subcat_obj.add(new Category_SubCategory_Object(list_Wine.get(i).getId() , list_Wine.get(i).getImage_url() , list_Wine.get(i).getWine_subcategory_name() , list_Wine.get(i).getWine_subcategory_price() , true));
                    }
                }

                    Intent intent = new Intent(SelectWineSubCategoryActivity.this , Purchase_Detail_Owner.class);
                    intent.putExtra("list" , list_cat_subcat_obj);
                    startActivity(intent);
                    finish();
            }
        });

        new SubCategoryList_Task().execute();
    }




    private class SubCategoryList_Task extends AsyncTask<Void ,Void ,Void> {

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(SelectWineSubCategoryActivity.this , "" ,"Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "owner")
                    .add("passing_value" , "get_subcategory")
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

                    Log.e("img wine" , jobj.getString("product_image"));
                    list_Wine.add(new WineSubCategory_Object(id , img_wine , title , price ,false));
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

        Intent intent = new Intent(SelectWineSubCategoryActivity.this , SelectWineCategoryActivity.class);
        startActivity(intent);
        finish();

    }
}
