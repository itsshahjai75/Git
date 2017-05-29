package com.barapp.simulator;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barapp.simulator.Adapter.PurchaseDetailList_Adapter;
import com.barapp.simulator.Object_Getter_Setter.Category_SubCategory_Object;
import com.barapp.simulator.Object_Getter_Setter.PurchaseDetailList_Object;
import com.barapp.simulator.utils.Const;
import com.barapp.simulator.utils.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Purchase_Detail_Owner extends AppCompatActivity {


    EditText et_title ,et_tasteDescribe ,et_sellingPrice;
    TextView  txt_total_price;
    RecyclerView listwine_recyclerview;
    LinearLayout linear_done;
    ImageView img_back ,product_pic;

    PurchaseDetailList_Adapter detail_adapter;
    ArrayList<PurchaseDetailList_Object> listWines =new ArrayList<>();

     String title ,taste_desc ,total_price ,selling_price ,products = "" ,product_prices = "" , img_id="" ,img_url;
    int total = 0;

    private static final int requestCode = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_detail_owner);
        getSupportActionBar().hide();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        et_title = (EditText)findViewById(R.id.title);
        et_tasteDescribe = (EditText)findViewById(R.id.taste_describe);
        et_sellingPrice = (EditText)findViewById(R.id.selling_price);

        txt_total_price = (TextView)findViewById(R.id.total_price);
        linear_done  = (LinearLayout)findViewById(R.id.done);

        img_back   = (ImageView)findViewById(R.id.backbtn);
        product_pic = (ImageView)findViewById(R.id.product_pic);

        listwine_recyclerview = (RecyclerView)findViewById(R.id.listwines);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listwine_recyclerview.setLayoutManager(mLayoutManager);
        listwine_recyclerview.setItemAnimator(new DefaultItemAnimator());

        ArrayList<Category_SubCategory_Object> list_wine = (ArrayList<Category_SubCategory_Object>) getIntent().getSerializableExtra("list");

        for(Category_SubCategory_Object wineobj : list_wine){
            listWines.add(new PurchaseDetailList_Object(wineobj.getId() , wineobj.getWine_name() , wineobj.getPrice() , wineobj.getImage_url()));

            products = products+wineobj.getWine_name()+",";
            product_prices = product_prices+wineobj.getPrice()+",";

            total = total + Integer.parseInt(wineobj.getPrice());
        }


        detail_adapter = new PurchaseDetailList_Adapter(listWines , Purchase_Detail_Owner.this , new PurchaseDetailList_Adapter.purchaseDetail() {
            @Override
            public void onDelete() {

                int total = 0;
                products = "";
                product_prices = "";
                for(int i=0; i<listWines.size(); i++){

                    products = products+listWines.get(i).getWine_name()+",";
                    product_prices = product_prices+listWines.get(i).getWine_price()+",";
                    total = total + Integer.parseInt(listWines.get(i).getWine_price());

                }
                txt_total_price.setText(total+"");

            }
        });

        listwine_recyclerview.setAdapter(detail_adapter);
        txt_total_price.setText(total+"");


       /* RippleView rippleView   = (RippleView)this.findViewById(R.id.more);
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                Log.d("Sample", "Ripple completed");


            }

        });*/


        linear_done.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                title = et_title.getText().toString();
                title = title.replaceFirst("^ *", "");

                taste_desc = et_tasteDescribe.getText().toString();
                taste_desc = taste_desc.replaceFirst("^ *", "");

                selling_price = et_sellingPrice.getText().toString();
                selling_price = selling_price.replaceFirst("^ *", "");

                if(products.equals("")){
                    Toast.makeText(getApplicationContext() , "Glass is empty,Add Brews!!!" ,Toast.LENGTH_SHORT).show();
                }else if(title.equals("") || title.length()==0){
                    et_title.setError("Enter your product name");

                }else if(taste_desc.equals("") || taste_desc.length()==0){
                    et_tasteDescribe.setError("Describe test overview.");

                }else if(selling_price.equals("") || selling_price.length()==0){
                    et_sellingPrice.setError("Enter selling price.");

                }else if(img_id.equals("") || img_id.length()==0){
                    Toast.makeText(getApplicationContext() , "Please Select Image" , Toast.LENGTH_SHORT).show();
                } else{
                    products = products.substring(0 , products.length()-1);
                    product_prices = product_prices.substring(0 , product_prices.length()-1);
                    total_price = txt_total_price.getText().toString();//total+"";

                    if(img_id != null){
                        new AddProduct_Task().execute();
                    }else{
                        Toast.makeText(getApplicationContext() , "Please Select Image" , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                finish();
            }
        });



        product_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(Purchase_Detail_Owner.this , GalleryActivity.class);
                startActivityForResult(intent , requestCode);
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100)
        {

                img_id = data.getStringExtra("image_id");
                img_url = data.getStringExtra("image_url");

                Picasso.with(Purchase_Detail_Owner.this)
                        .load(Const.IMAGE_URL + img_url)
                        .placeholder(R.drawable.wine_bottle) // optional
                        .error(R.drawable.noimage)         // optional
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(product_pic);

        }
    }


    private class AddProduct_Task extends AsyncTask<Void ,Void ,Void> {

        ProgressDialog pDialog;
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(Purchase_Detail_Owner.this , "" ,"Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "owner")
                    .add("passing_value" , "add_product")
                    .add("owner_id" , Utils.getPrefs(Purchase_Detail_Owner.this , Const.USER_ID))
                    .add("title" , title)
                    .add("taste_desc" , taste_desc)
                    .add("total_price" , total_price)
                    .add("selling_price" , selling_price)
                    .add("products" , products)
                    .add("products_prices" , product_prices)
                    .add("img_id" , img_id)
                    .build();


            Log.d("data",title+taste_desc);

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
                JSONObject jobj =new JSONObject(res);
                String code =jobj.getString("status_code");
                if(code.equals("200")){
                    Toast.makeText(getApplicationContext() , "Product Added Successfully" , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Purchase_Detail_Owner.this , OwnerMenuActivity.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext() , jobj.getString("message") , Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Purchase_Detail_Owner.this , SelectWineCategoryActivity.class));
        finish();
      }


}
