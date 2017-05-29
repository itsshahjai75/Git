package com.barapp.simulator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.barapp.simulator.Adapter.ProductMixture_Adapter;
import com.barapp.simulator.Object_Getter_Setter.PurchaseDetailList_Object;
import com.barapp.simulator.uc.UserTextView;
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

public class PurchaseDetail_User extends AppCompatActivity {

    TextView txt_price ,txt_product_name ,txt_barname ,txt_quantity ,txt_description ,txt_reviewcount ,txt_purchasenow,tv_review;
    LinearLayout linear_reviews,linear_ispurchased;
    MaterialRippleLayout linear_purchase,linear_mixture;
    RelativeLayout linear_decrement ,linear_increment;
    ImageView img_wine ,img_back ,rate1 ,rate2 ,rate3 ,rate4,rate5;



    String id,product_id,owner_id,products,products_prices;/*  ,  ,  , purchase*/;
    String  str_rating , str_comment ,price , str_quantity , isFromOwner;
    int quantity = 1;

    ArrayList<PurchaseDetailList_Object> listWines =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_detail_user);

        getSupportActionBar().hide();


        txt_price = (TextView)this.findViewById(R.id.price);
        txt_product_name = (TextView)this.findViewById(R.id.name);
        txt_barname = (TextView)this.findViewById(R.id.bar_name);
        txt_quantity = (TextView)this.findViewById(R.id.wine_quantity);
        txt_description = (TextView)this.findViewById(R.id.wine_description);
        txt_reviewcount = (TextView)this.findViewById(R.id.review_count);
        txt_purchasenow = (TextView)findViewById(R.id.txt_purchasenow);
        tv_review = (TextView)findViewById(R.id.tv_review);

        linear_purchase = (MaterialRippleLayout)this.findViewById(R.id.purchase_now);
        linear_reviews  = (LinearLayout)this.findViewById(R.id.reviews);
        linear_mixture  = (MaterialRippleLayout)this.findViewById(R.id.product_mixture);
        linear_ispurchased = (LinearLayout)this.findViewById(R.id.linear_isPurchased);

        img_wine     = (ImageView)this.findViewById(R.id.img_wine);
        img_back     = (ImageView)this.findViewById(R.id.backbtn);

        rate1 = (ImageView)findViewById(R.id.rate_img1);
        rate2 = (ImageView)findViewById(R.id.rate_img2);
        rate3 = (ImageView)findViewById(R.id.rate_img3);
        rate4 = (ImageView)findViewById(R.id.rate_img4);
        rate5 = (ImageView)findViewById(R.id.rate_img5);

        linear_decrement = (RelativeLayout)findViewById(R.id.decrement);
        linear_increment = (RelativeLayout)findViewById(R.id.increment);

        product_id = getIntent().getStringExtra("product_id");


        if(getIntent().hasExtra("owner")){
            linear_purchase.setVisibility(View.GONE);
            linear_reviews.setVisibility(View.GONE);
            linear_decrement.setVisibility(View.GONE);
            linear_increment.setVisibility(View.GONE);
            linear_decrement.setEnabled(false);
            linear_increment.setEnabled(false);
        }

        new PurchaseDetailUser_Task().execute();


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        linear_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Purchase_Product().execute();
            }
        });

        linear_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity > 1){
                    quantity--;
                    txt_quantity.setText(quantity+"");
                    linear_increment.setEnabled(true);

                    if(Float.parseFloat(price)<10){
                        txt_price.setText((Float.parseFloat(price)*quantity)+"");
                    }else{
                        txt_price.setText((Integer.parseInt(price)*quantity)+"");
                    }
                    //txt_price.setText((Float.parseFloat(price)*quantity)+"");

                    if(quantity < Integer.parseInt(str_quantity)){
                        linear_increment.setEnabled(true);
                    }
                }else{
                    linear_decrement.setEnabled(false);
                }
            }
        });

        linear_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity < 10){
                    quantity++;
                    txt_quantity.setText(quantity+"");
                    linear_decrement.setEnabled(true);

                    if(Float.parseFloat(price)<10){
                        txt_price.setText((Float.parseFloat(price)*quantity)+"");
                    }else{
                        txt_price.setText((Integer.parseInt(price)*quantity)+"");
                    }

                    if(quantity == Integer.parseInt(str_quantity)){
                        linear_increment.setEnabled(false);
                    }
                }else{
                    linear_increment.setEnabled(false);
                }
            }
        });

        linear_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Intent intent =new Intent(PurchaseDetail_User.this , Add_Rate_Comment.class);
            intent.putExtra("productid" , id);
            startActivity(intent);

            }
        });

        tv_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(PurchaseDetail_User.this , Add_Rate_Comment.class);
                intent.putExtra("productid" , id);
                startActivity(intent);
            }
        });

        linear_mixture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(PurchaseDetail_User.this , Dialog_Product_Mixtures.class);
                intent.putExtra("products" , products);
                intent.putExtra("products_prices" , products_prices);
                startActivity(intent);
            }
        });

        txt_reviewcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(PurchaseDetail_User.this , Rate_Comment_ListActivity.class);
                intent.putExtra("product_id" , id);
                startActivity(intent);
            }
        });

    }






    private  void ProductMixtureDialog() {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        final Dialog dialog =new Dialog(PurchaseDetail_User.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_product_mixtures);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = width;
        lp.height = 600;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0;
        dialog.getWindow().setAttributes(lp);

        RecyclerView  listRecycler = (RecyclerView)dialog.findViewById(R.id.listwines);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listRecycler.setLayoutManager(mLayoutManager);
        listRecycler.setItemAnimator(new DefaultItemAnimator());

        UserTextView btn_submit = (UserTextView) dialog.findViewById(R.id.submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        //Log.e("products"  ,  products+" "+products_prices);

        String[] productarray = products.split(",");
        String[] productprices = products_prices.split(",");

        Log.e("products" , products+" "+products_prices);

        listWines.clear();
        for(int i=0; i<productarray.length; i++){
            listWines.add(new PurchaseDetailList_Object("" , productarray[i] , productprices[i] , ""));
        }
        listRecycler.setAdapter(new ProductMixture_Adapter(listWines , PurchaseDetail_User.this));

        dialog.show();
    }




    private class PurchaseDetailUser_Task extends AsyncTask<Void , Void , Void> {

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(PurchaseDetail_User.this , "" , "Please Wait...");
        }


        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "owner")
                    .add("passing_value" , "get_product_details")
                    .add("product_id" , product_id)
                    .add("user_id",Utils.getPrefs(PurchaseDetail_User.this,Const.USER_ID))
                    .build();

            Request request = new Request.Builder()
                    .url(Const.API_URL)
                    .post(formBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();

                Log.e("response=====" , res);
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
                    JSONObject jobjinner = jobjMain.getJSONObject("data");
                    id = jobjinner.getString("id");
                    owner_id = jobjinner.getString("owner_id");
                    String title  = jobjinner.getString("title");
                    String product_image = jobjinner.getString("product_image");
                    String desc  = jobjinner.getString("taste_des");
                    price = jobjinner.getString("selling_price");
                    String barname = jobjinner.getString("barname");


                    String rating_count = jobjinner.getString("rating");
                    Log.d("Rating user purchaseD",rating_count);
                    String review_count = jobjinner.getString("total_review");
                    String str_purchase = jobjinner.getString("purchase");
                    str_quantity = jobjinner.getString("quantity");
                    if(str_quantity.equals("0")){
                        txt_purchasenow.setText("Product Out Of Stock");
                        txt_purchasenow.setEnabled(false);
                    }

                    products = jobjinner.getString("products");
                    products_prices = jobjinner.getString("products_prices");

                    if(str_purchase.equalsIgnoreCase("yes")){
                        linear_ispurchased.setVisibility(View.VISIBLE);
                    }else{
                        linear_ispurchased.setVisibility(View.GONE);
                    }

                    if(owner_id.equalsIgnoreCase(Utils.getPrefs(PurchaseDetail_User.this,Const.USER_ID))){
                        linear_ispurchased.setVisibility(View.GONE);
                        linear_decrement.setVisibility(View.GONE);
                        linear_increment.setVisibility(View.GONE);
                        linear_purchase.setVisibility(View.GONE);
                    }else{

                    }


                    if(title.length()>16){
                        txt_product_name.setText(title.substring(0,16)+"...");
                    }else{
                        txt_product_name.setText(title);
                    }
                    //txt_product_name.setText(title);
                    /*if(Integer.parseInt(price)<50){
                        price=price+".00";
                    }*/
                    txt_price.setText(price);
                    txt_description.setText(desc);


                    if(owner_id.equalsIgnoreCase(Utils.getPrefs(PurchaseDetail_User.this,Const.USER_ID))){
                        txt_quantity.setText(str_quantity);
                    }else {
                        txt_quantity.setText("1");
                    }


                    if(barname.length()>20){
                        txt_barname.setText("BarName : "+barname.substring(0,20)+"...");;
                    }else{
                        txt_barname.setText("BarName : "+barname);
                    }

                    //txt_barname.setText("BarName : "+barname);
                    if(review_count.equalsIgnoreCase("0")){
                        txt_reviewcount.setEnabled(false);
                    }
                    txt_reviewcount.setText("Reviews : "+review_count);

                    setRatings(Double.parseDouble(rating_count));

                    Picasso.with(PurchaseDetail_User.this).load(Const.IMAGE_URL+product_image).placeholder(R.drawable.wine_bottle)
                            .error(R.drawable.wine_bottle).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(img_wine);

                }else{

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class Purchase_Product extends AsyncTask<Void , Void , Void> {

        ProgressDialog pDialog;
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(PurchaseDetail_User.this , "" , "Purchasing Product...");
        }


        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "user")
                    .add("passing_value" , "purchase")
                    .add("userid" , Utils.getPrefs(PurchaseDetail_User.this , Const.USER_ID))
                    .add("productid" , id)
                    .add("quantity" , quantity+"")
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
                    Toast.makeText(getApplicationContext() , "Product Purchased" , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PurchaseDetail_User.this , UserMenuActivity.class));
                    //onBackPressed();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext() , jobjMain.getString("message") , Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    private void setRatings(double rating) {

        if (rating == 0.5) {
            rate1.setImageResource(R.drawable.star_enable_half);
            rate2.setImageResource(R.drawable.star_diseble);
            rate3.setImageResource(R.drawable.star_diseble);
            rate4.setImageResource(R.drawable.star_diseble);
            rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 0.5 && rating <= 1) {
           rate1.setImageResource(R.drawable.star_enable);
           rate2.setImageResource(R.drawable.star_diseble);
           rate3.setImageResource(R.drawable.star_diseble);
           rate4.setImageResource(R.drawable.star_diseble);
           rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 1 && rating <= 1.5) {
            rate1.setImageResource(R.drawable.star_enable);
            rate2.setImageResource(R.drawable.star_enable_half);
            rate3.setImageResource(R.drawable.star_diseble);
            rate4.setImageResource(R.drawable.star_diseble);
            rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 1.5 && rating <= 2) {
            rate1.setImageResource(R.drawable.star_enable);
            rate2.setImageResource(R.drawable.star_enable);
            rate3.setImageResource(R.drawable.star_diseble);
            rate4.setImageResource(R.drawable.star_diseble);
            rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating >= 2 && rating <= 2.5) {
            rate1.setImageResource(R.drawable.star_enable);
            rate2.setImageResource(R.drawable.star_enable);
            rate3.setImageResource(R.drawable.star_enable_half);
            rate4.setImageResource(R.drawable.star_diseble);
            rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating >= 2.5 && rating <= 3) {
           rate1.setImageResource(R.drawable.star_enable);
           rate2.setImageResource(R.drawable.star_enable);
           rate3.setImageResource(R.drawable.star_enable);
           rate4.setImageResource(R.drawable.star_diseble);
           rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating >= 3 && rating <= 3.5) {
           rate1.setImageResource(R.drawable.star_enable);
           rate2.setImageResource(R.drawable.star_enable);
           rate3.setImageResource(R.drawable.star_enable);
           rate4.setImageResource(R.drawable.star_enable_half);
           rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating >= 3.5 && rating <= 4) {
            rate1.setImageResource(R.drawable.star_enable);
            rate2.setImageResource(R.drawable.star_enable);
            rate3.setImageResource(R.drawable.star_enable);
            rate4.setImageResource(R.drawable.star_enable);
            rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating >= 4 && rating <= 4.5) {
            rate1.setImageResource(R.drawable.star_enable);
            rate2.setImageResource(R.drawable.star_enable);
            rate3.setImageResource(R.drawable.star_enable);
            rate4.setImageResource(R.drawable.star_enable);
            rate5.setImageResource(R.drawable.star_enable_half);
        } else if (rating >= 4.5) {
           rate1.setImageResource(R.drawable.star_enable);
           rate2.setImageResource(R.drawable.star_enable);
           rate3.setImageResource(R.drawable.star_enable);
           rate4.setImageResource(R.drawable.star_enable);
           rate5.setImageResource(R.drawable.star_enable);
        } else {
            rate1.setImageResource(R.drawable.star_diseble);
            rate2.setImageResource(R.drawable.star_diseble);
            rate3.setImageResource(R.drawable.star_diseble);
            rate4.setImageResource(R.drawable.star_diseble);
            rate5.setImageResource(R.drawable.star_diseble);
        }
    }



}
