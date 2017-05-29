package com.barapp.simulator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.barapp.simulator.Adapter.UserShopList_Adapter;
import com.barapp.simulator.Object_Getter_Setter.UserShopList_Object;
import com.barapp.simulator.uc.HorizontalListView;
import com.barapp.simulator.utils.Const;
import com.barapp.simulator.utils.ExpandCollapseAnimation;
import com.barapp.simulator.utils.Utils;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserMenuActivity extends AppCompatActivity implements View.OnClickListener ,BillingProcessor.IBillingHandler {


    LinearLayout linear_fab , linear_shop ,linear_mytest , linear_purchase ,linear_dropdown ,linear_category ,
            category1 ,category2 ,category3 ,category4 ,category5 ,linear_logout;
    private boolean mActive = false , mActive1 = false;

    EditText et_search;
    HorizontalListView hlistview;
    UserShopList_Adapter adapter;
    ImageView img_profile,img_logout;
    TextView  txt_user ,txt_coins , txt_purchasenow;

    ArrayList<UserShopList_Object> list_shopObj =new ArrayList<>();

    String filter = "" , keyword = "" , fullname , totalcoin , str_quantity ;
    BillingProcessor bp;

    int planindex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_new);
        getSupportActionBar().hide();

        bp = new BillingProcessor(this , Const.LICENSE_KEY , this);
        bp.consumePurchase(Const.Product_Plan10);

        new UserInfo().execute();

        linear_fab = (LinearLayout)findViewById(R.id.fab);
        linear_dropdown = (LinearLayout)findViewById(R.id.dropdown);
        linear_category = (LinearLayout)findViewById(R.id.category);

        img_logout= (ImageView)findViewById(R.id.img_logout);
        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Utils.ClearAllPrefs(UserMenuActivity.this);
                startActivity(new Intent(UserMenuActivity.this,LoginActivity.class));
                finish();*/
                new Logout().execute();

            }
        });

        linear_shop  = (LinearLayout)findViewById(R.id.shop);
        linear_mytest = (LinearLayout)findViewById(R.id.mytest);
        linear_purchase = (LinearLayout)findViewById(R.id.purchase);
        category1 = (LinearLayout)findViewById(R.id.category1);
        category2 = (LinearLayout)findViewById(R.id.category2);
        category3 = (LinearLayout)findViewById(R.id.category3);
        category4 = (LinearLayout)findViewById(R.id.category4);
        category5 = (LinearLayout)findViewById(R.id.category5);
        linear_logout = (LinearLayout)findViewById(R.id.logout);


        et_search = (EditText)findViewById(R.id.et_search);
        hlistview = (HorizontalListView)findViewById(R.id.hlistview);
        img_profile = (ImageView)findViewById(R.id.profile_image);
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMenuActivity.this,Profile_Get_Update.class));
                //finish();
            }
        });

        txt_user  = (TextView)findViewById(R.id.username);
        txt_coins = (TextView)findViewById(R.id.price);
        txt_purchasenow = (TextView)findViewById(R.id.txt_purchasenow);

        adapter = new UserShopList_Adapter(UserMenuActivity.this , list_shopObj);
        hlistview.setAdapter(adapter);


        linear_fab.setOnClickListener(this);
        linear_shop.setOnClickListener(this);
        linear_mytest.setOnClickListener(this);
        linear_purchase.setOnClickListener(this);
        linear_logout.setOnClickListener(this);
        category1.setOnClickListener(this);
        category2.setOnClickListener(this);
        category3.setOnClickListener(this);
        category4.setOnClickListener(this);
        category5.setOnClickListener(this);

        fullname = getIntent().getStringExtra("fullname");
        totalcoin = getIntent().getStringExtra("");

        et_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v  , MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (et_search.getRight() - et_search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        ExpandCollapseAnimation animation = null;
                        if (mActive1) {
                            animation = new ExpandCollapseAnimation(linear_category , 300 , 1);
                            mActive1 = false;
                        } else {
                            animation = new ExpandCollapseAnimation(linear_category , 300 , 0);
                            mActive1 = true;
                        }
                        linear_category.startAnimation(animation);
                        return true;
                    }
                }

                return false;
            }
        });


        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v , int actionId , KeyEvent event) {

                    keyword = v.getText().toString();
                    new WineList_Task().execute("");

                return false;
            }
        });


    }




    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.fab:

                ExpandCollapseAnimation animation = null;
                if (mActive) {
                    animation = new ExpandCollapseAnimation(linear_dropdown , 300 , 1);
                    mActive = false;
                } else {
                    animation = new ExpandCollapseAnimation(linear_dropdown , 300 , 0);
                    mActive = true;
                }
                linear_dropdown.startAnimation(animation);

                break;
            case  R.id.shop:

                ExpandCollapseAnimation  animation1 = new ExpandCollapseAnimation(linear_dropdown , 300 , 1);
                mActive = false;
                linear_dropdown.startAnimation(animation1);

                keyword = et_search.getText().toString();
                new WineList_Task().execute("");

                break;
            case R.id.mytest:

                ExpandCollapseAnimation  animation2 = new ExpandCollapseAnimation(linear_dropdown , 300 , 1);
                mActive = false;
                linear_dropdown.startAnimation(animation2);

                keyword = et_search.getText().toString();
                new WineList_Task_Mytest().execute();

                break;
            case R.id.purchase:

                hlistview.setVisibility(View.GONE);
                ExpandCollapseAnimation  animation11 = new ExpandCollapseAnimation(linear_dropdown , 300 , 1);
                mActive = false;
                linear_dropdown.startAnimation(animation11);
                planindex = 0;

                UserMenuDialog();
                break;
            case R.id.category1:

                ExpandCollapseAnimation animation12 = new ExpandCollapseAnimation(linear_category , 300 , 1);
                mActive1 = false;
                linear_category.startAnimation(animation12);

                keyword = et_search.getText().toString();
                filter = "1";

                new WineList_Task().execute("");
                break;
            case R.id.category2:
                ExpandCollapseAnimation animation13 = new ExpandCollapseAnimation(linear_category , 300 , 1);
                mActive1 = false;
                linear_category.startAnimation(animation13);

                keyword = et_search.getText().toString();
                filter = "2";

                new WineList_Task().execute("");
                break;
            case R.id.category3:
                ExpandCollapseAnimation animation14 = new ExpandCollapseAnimation(linear_category , 300 , 1);
                mActive1 = false;
                linear_category.startAnimation(animation14);

                keyword = et_search.getText().toString();
                filter = "3";

                new WineList_Task().execute("");
                break;
            case R.id.category4:
                ExpandCollapseAnimation animation15 = new ExpandCollapseAnimation(linear_category , 300 , 1);
                mActive1 = false;
                linear_category.startAnimation(animation15);

                keyword = et_search.getText().toString();
                filter = "4";

                new WineList_Task().execute("");
                break;


            case R.id.category5:
                ExpandCollapseAnimation animation16 = new ExpandCollapseAnimation(linear_category , 300 , 1);
                mActive1 = false;
                linear_category.startAnimation(animation16);

                keyword = et_search.getText().toString();
                filter = "5";

                new WineList_Task().execute("");
                break;
            case R.id.logout:


                /*Utils.ClearAllPrefs(UserMenuActivity.this);
                startActivity(new Intent(UserMenuActivity.this , LoginActivity.class));
                finish();*/

                new Logout().execute();

                break;
            }
    }

    String str_coins ="";
    private  void UserMenuDialog() {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        final Dialog dialog = new Dialog(UserMenuActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.buy_coins_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = width;
        lp.height = 600;
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0;
        dialog.getWindow().setAttributes(lp);

        ImageView img_close = (ImageView)dialog.findViewById(R.id.close);
        TextView  txt_buycoin = (TextView)dialog.findViewById(R.id.buycoins);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final LinearLayout product_plan1 = (LinearLayout)dialog.findViewById(R.id.product1);
        final LinearLayout product_plan2 = (LinearLayout)dialog.findViewById(R.id.product2);
        final LinearLayout product_plan3 = (LinearLayout)dialog.findViewById(R.id.product3);
        final LinearLayout product_plan4 = (LinearLayout)dialog.findViewById(R.id.product4);
        final LinearLayout product_plan5 = (LinearLayout)dialog.findViewById(R.id.product5);
        final LinearLayout product_plan6 = (LinearLayout)dialog.findViewById(R.id.product6);

        txt_buycoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(planindex > 0){

                    if(planindex == 1){

                        bp.purchase(UserMenuActivity.this , Const.Product_Plan10);
                        dialog.dismiss();
                    }else if(planindex == 2){

                        bp.purchase(UserMenuActivity.this , Const.Product_Plan20);
                        dialog.dismiss();
                    }else if(planindex == 3){

                        bp.purchase(UserMenuActivity.this , Const.Product_Plan30);
                        dialog.dismiss();
                    }else if(planindex == 4){

                        bp.purchase(UserMenuActivity.this , Const.Product_Plan40);
                        dialog.dismiss();
                    }else if(planindex == 5){

                        bp.purchase(UserMenuActivity.this , Const.Product_Plan50);
                        dialog.dismiss();
                    }else{
                        bp.purchase(UserMenuActivity.this , Const.Product_Plan60);
                        dialog.dismiss();
                    }

                }else{
                    Toast.makeText(getApplicationContext() , "Please select atleast one plan" , Toast.LENGTH_SHORT).show();
                }
            }
        });


        product_plan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planindex = 1;

                product_plan1.setBackgroundResource(R.drawable.wooden_active);
                product_plan2.setBackgroundResource(R.drawable.wooden);
                product_plan3.setBackgroundResource(R.drawable.wooden);
                product_plan4.setBackgroundResource(R.drawable.wooden);
                product_plan5.setBackgroundResource(R.drawable.wooden);
                product_plan6.setBackgroundResource(R.drawable.wooden);

            }
        });

        product_plan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planindex = 2;

                product_plan1.setBackgroundResource(R.drawable.wooden);
                product_plan2.setBackgroundResource(R.drawable.wooden_active);
                product_plan3.setBackgroundResource(R.drawable.wooden);
                product_plan4.setBackgroundResource(R.drawable.wooden);
                product_plan5.setBackgroundResource(R.drawable.wooden);
                product_plan6.setBackgroundResource(R.drawable.wooden);
            }
        });

        product_plan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planindex = 3;

                product_plan1.setBackgroundResource(R.drawable.wooden);
                product_plan2.setBackgroundResource(R.drawable.wooden);
                product_plan3.setBackgroundResource(R.drawable.wooden_active);
                product_plan4.setBackgroundResource(R.drawable.wooden);
                product_plan5.setBackgroundResource(R.drawable.wooden);
                product_plan6.setBackgroundResource(R.drawable.wooden);
            }
        });

        product_plan4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planindex = 4;

                product_plan1.setBackgroundResource(R.drawable.wooden);
                product_plan2.setBackgroundResource(R.drawable.wooden);
                product_plan3.setBackgroundResource(R.drawable.wooden);
                product_plan4.setBackgroundResource(R.drawable.wooden_active);
                product_plan5.setBackgroundResource(R.drawable.wooden);
                product_plan6.setBackgroundResource(R.drawable.wooden);
            }
        });

        product_plan5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planindex = 5;

                product_plan1.setBackgroundResource(R.drawable.wooden);
                product_plan2.setBackgroundResource(R.drawable.wooden);
                product_plan3.setBackgroundResource(R.drawable.wooden);
                product_plan4.setBackgroundResource(R.drawable.wooden);
                product_plan5.setBackgroundResource(R.drawable.wooden_active);
                product_plan6.setBackgroundResource(R.drawable.wooden);
            }
        });

        product_plan6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planindex = 6;

                product_plan1.setBackgroundResource(R.drawable.wooden);
                product_plan2.setBackgroundResource(R.drawable.wooden);
                product_plan3.setBackgroundResource(R.drawable.wooden);
                product_plan4.setBackgroundResource(R.drawable.wooden);
                product_plan5.setBackgroundResource(R.drawable.wooden);
                product_plan6.setBackgroundResource(R.drawable.wooden_active);
            }
        });

        dialog.show();
    }


    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {

        if(productId.equalsIgnoreCase(Const.Product_Plan10)){
            str_coins="1000";
            new UserMenuActivity.Update_coins().execute();
            bp.consumePurchase(Const.Product_Plan10);
        }else if(productId.equalsIgnoreCase(Const.Product_Plan20)){
            str_coins="3150";
            new UserMenuActivity.Update_coins().execute();
            bp.consumePurchase(Const.Product_Plan20);
        }else if(productId.equalsIgnoreCase(Const.Product_Plan30)){
            str_coins="5500";
            new UserMenuActivity.Update_coins().execute();
            bp.consumePurchase(Const.Product_Plan30);
        }else if(productId.equalsIgnoreCase(Const.Product_Plan40)){
            str_coins="11500";
            new UserMenuActivity.Update_coins().execute();
            bp.consumePurchase(Const.Product_Plan40);
        }else if(productId.equalsIgnoreCase(Const.Product_Plan50)){
            str_coins="24000";
            new UserMenuActivity.Update_coins().execute();
            bp.consumePurchase(Const.Product_Plan50);
        }else if(productId.equalsIgnoreCase(Const.Product_Plan60)){
            str_coins="57500";
            new UserMenuActivity.Update_coins().execute();
            bp.consumePurchase(Const.Product_Plan60);
        }


    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

        if(errorCode==7){
            Toast.makeText(UserMenuActivity.this,"already purchased.",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(UserMenuActivity.this,"Purchase Error.",Toast.LENGTH_LONG).show();
            //str_coins="100";
            ///new Update_coins().execute();
        }

    }

    @Override
    public void onBillingInitialized() {
        Log.d("Initialized" , "Initialized");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            new UserMenuActivity.Update_coins().execute();
        }


    }



    ProgressDialog upgradeDialog;
    private void promptForUpgrade() {
        AlertDialog.Builder upgradeAlert = new AlertDialog.Builder(UserMenuActivity.this);
        upgradeAlert.setTitle("Upgrade?");
        upgradeAlert.setCancelable(false);
        upgradeAlert.setMessage("Your 30 days trial version was expired, Please buy a full version.");
        upgradeAlert.setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //set progress dialog and start the in app purchase process
                // upgradeDialog = ProgressDialog.show(OwnerMenuActivity.this, "Please wait", "Upgrade transaction in process", true);
                bp.purchase(UserMenuActivity.this , Const.Product_Plan_Unlimitedversion);
                dialog.dismiss();
            }}).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                onBackPressed();
            }
        });
        upgradeAlert.show();
    }

    private class Update_coins extends AsyncTask<Void ,Void ,Void>{

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(UserMenuActivity.this , "" ,"Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {



            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "user")
                    .add("passing_value" , "buy_coins")
                    .add("user_id" , Utils.getPrefs(UserMenuActivity.this , Const.USER_ID))
                    .add("coins" ,str_coins)

                    .build();

            Request request = new Request.Builder()
                    .url(Const.API_URL)
                    .post(formBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();

                Log.e("response" , res);
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
                JSONObject jobj = new JSONObject(res);
                JSONObject jsonData = jobj.getJSONObject("data");
                String status_code = jsonData.getString("status_code");

                if(status_code.equalsIgnoreCase("200")){
                    startActivity(new Intent(UserMenuActivity.this,UserMenuActivity.class));
                    finish();
                }else if(status_code.equalsIgnoreCase("400")){
                    Toast.makeText(UserMenuActivity.this,jsonData.getString("message"),Toast.LENGTH_LONG).show();

                }




            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class WineList_Task extends AsyncTask<String ,Void ,Void> {

        ProgressDialog pDialog;
        String res , param;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(UserMenuActivity.this , "" , "Please Wait...");
        }

        @Override
        protected Void doInBackground(String... params) {


            Log.d("filter", filter);

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "owner")
                    .add("passing_value" , "get_product_list")
                    .add("category_type" , filter)
                    .add("keyword" , keyword)
                    .add("user_id" , Utils.getPrefs(UserMenuActivity.this , Const.USER_ID))
                    .build();


            Request request = new Request.Builder()
                    .url(Const.API_URL)
                    .post(formBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();

                Log.e("response" , res);
            } catch (IOException e) {
                e.printStackTrace();
            }

            param = params[0];
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
            list_shopObj.clear();
            try {
                JSONObject jobjMain = new JSONObject(res);
                String code = jobjMain.getString("status_code");

                if(code.equals("200")){
                    JSONArray jarray = jobjMain.getJSONArray("data");
                    for(int i=0; i<jarray.length(); i++){

                        JSONObject jobjinner= jarray.getJSONObject(i);

                        String id = jobjinner.getString("id");
                        String owner_id = jobjinner.getString("owner_id");
                        String product_img = jobjinner.getString("product_image");
                        String product_name = jobjinner.getString("title");
                        String barname     = jobjinner.getString("barname");
                        String selling_price = jobjinner.getString("selling_price");
                        String products  = jobjinner.getString("products");
                        String product_prices = jobjinner.getString("products_prices");
                        String purchase  = jobjinner.getString("purchase");
                        str_quantity = jobjinner.getString("quantity");

                        /*if(param.equals("mytest")){
                            if(purchase.equalsIgnoreCase("yes")){
                                UserShopList_Object obj =new UserShopList_Object(id , owner_id ,  product_img , product_name , barname , selling_price , products , product_prices , purchase ,str_quantity);
                                list_shopObj.add(obj);
                            }
                        }else{ }*/

                        UserShopList_Object obj =new UserShopList_Object(id , owner_id ,  product_img , product_name , barname , selling_price , products , product_prices , purchase ,str_quantity);
                        list_shopObj.add(obj);

                    }
                    adapter.notifyDataSetChanged();
                    hlistview.setVisibility(View.VISIBLE);
                }else{
                    adapter.notifyDataSetChanged();
                    hlistview.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext() , "No Data Available" ,Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class WineList_Task_Mytest extends AsyncTask<String ,Void ,Void> {

        ProgressDialog pDialog;
        String res , param;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(UserMenuActivity.this , "" , "Please Wait...");
        }

        @Override
        protected Void doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "owner")
                    .add("passing_value" , "get_product_list")
                    .add("category_type" , filter)
                    .add("keyword" , keyword)
                    .add("user_id" , Utils.getPrefs(UserMenuActivity.this , Const.USER_ID))
                    .add("filter_type","1")
                    .build();


            Request request = new Request.Builder()
                    .url(Const.API_URL)
                    .post(formBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();

                Log.e("response" , res);
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
            list_shopObj.clear();
            try {
                JSONObject jobjMain = new JSONObject(res);
                String code = jobjMain.getString("status_code");

                if(code.equals("200")){
                    JSONArray jarray = jobjMain.getJSONArray("data");
                    for(int i=0; i<jarray.length(); i++){

                        JSONObject jobjinner= jarray.getJSONObject(i);

                        String id = jobjinner.getString("id");
                        String owner_id = jobjinner.getString("owner_id");
                        String product_img = jobjinner.getString("product_image");
                        String product_name = jobjinner.getString("title");
                        String barname     = jobjinner.getString("barname");
                        String selling_price = jobjinner.getString("selling_price");
                        String products  = jobjinner.getString("products");
                        String product_prices = jobjinner.getString("products_prices");
                        String purchase  = jobjinner.getString("purchase");
                        str_quantity = jobjinner.getString("quantity");

                        /*if(param.equals("mytest")){
                            if(purchase.equalsIgnoreCase("yes")){
                                UserShopList_Object obj =new UserShopList_Object(id , owner_id ,  product_img , product_name , barname , selling_price , products , product_prices , purchase ,str_quantity);
                                list_shopObj.add(obj);
                            }
                        }else{ }*/

                        UserShopList_Object obj =new UserShopList_Object(id , owner_id ,  product_img , product_name , barname , selling_price , products , product_prices , purchase ,str_quantity);
                        list_shopObj.add(obj);

                    }
                    adapter.notifyDataSetChanged();
                    hlistview.setVisibility(View.VISIBLE);
                }else{
                    adapter.notifyDataSetChanged();
                    hlistview.setVisibility(View.VISIBLE);

                    Toast.makeText(getApplicationContext() , "No Data Available" ,Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class UserInfo extends AsyncTask<Void ,Void ,Void> {

        ProgressDialog pDialog;
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(UserMenuActivity.this , "" , "Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "owner")
                    .add("passing_value" , "get_user")
                    .add("userid" , Utils.getPrefs(UserMenuActivity.this , Const.USER_ID)).build();

            Request request = new Request.Builder()
                    .url(Const.API_URL).post(formBody).build();


            try {
                Response response = client.newCall(request).execute();
                res = response.body().string();

                Log.e("response" , res);
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

            list_shopObj.clear();
            try {
                JSONObject jobjMain = new JSONObject(res);
                String code = jobjMain.getString("status_code");

                if(code.equals("200")){

                    JSONObject jobj = jobjMain.getJSONObject("data");

                    String get_daycount = jobj.getString("count_days");
                    String status = jobj.getString("user_status");

                    int coutn_days = Integer.parseInt(get_daycount);

                    if(coutn_days>30 && status.equalsIgnoreCase("inactive")) {
                        pDialog.dismiss();
                        promptForUpgrade();
                    }


                    String  profile_pic = jobj.getString("profile_pic");
                    String  username    = jobj.getString("username");
                    String price    = jobj.getString("total_coin");
                    String count    = jobj.getString("total_coin");


                    if(username.length()>15){
                        txt_user.setText((username.substring(0,1).toUpperCase()+
                                username.substring(1,15)+"..."));
                    }else{
                        txt_user.setText((username.substring(0,1).toUpperCase()+
                                username.substring(1,username.length())));
                    }
                    txt_coins.setText(price);

                    Picasso.with(UserMenuActivity.this).load(Const.IMAGE_URL+profile_pic).placeholder(R.drawable.noimage)
                            .error(R.drawable.noimage).memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE).into(img_profile);


                }else{
                    adapter.notifyDataSetChanged();
                    hlistview.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext() , "No Data Available" ,Toast.LENGTH_SHORT).show();
                }

                pDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    String res;
    class Logout extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String item_ingre;

        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(UserMenuActivity.this, "Loading", "Please Wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");


          /*  try {
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                //String postURL = "http://169.254.76.188:8084/Sunil/order_entery";
                //HttpPost post = new HttpPost(postURL);

                HttpPost post = new HttpPost(Const.SERVER_URL);//2015-5-15
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("action","logout"));
                params.add(new BasicNameValuePair("user_id",  sharepref.getString(Const.PREF_USER_ID,"")));
                // params.add(new BasicNameValuePair("passing_value","get"));


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    // System.out.println("response got from server----- "+resp);


                }
            } catch (Exception e) {
                e.printStackTrace();

            }*/

            return res;

        }


        @Override
        protected void onPostExecute(String result) {

            int Total_cart_amount = 0;
            int Total_cart_saving_amount = 0;

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            /*if (res == null || res.equals("")) {

                progressDialog.dismiss();
                //footerLayout.setVisibility(View.GONE);
                Toast.makeText(HomeScreen.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }*/

            try {
                /*JSONObject obj = new JSONObject(res);


                // Log.i("RESPONSE", res);

                String status_code_string = obj.getString("status_code");*/


                //if(status_code_string.equalsIgnoreCase(Const.API_RESULT_SUCCESS)) {


                    //Check if user is currently logged in
                    if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null){
                        //Logged in so show the login button


                        LoginManager.getInstance().logOut();

                        Utils.ClearAllPrefs(UserMenuActivity.this);
                        //sharepref.edit().clear().commit();
                        //sharepref.edit().putString("key_login","no").commit();

                        Intent intenta = new Intent(UserMenuActivity.this, LoginActivity.class);
                        intenta.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intenta);
                        finish();

                        Snackbar snackbar = Snackbar
                                .make(findViewById(android.R.id.content), "  Thank You.!!!!", Snackbar.LENGTH_LONG);

                        // Changing message text color
                        snackbar.setActionTextColor(Color.BLUE);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();

                        Toast.makeText(UserMenuActivity.this, "Sign out Done !", Toast.LENGTH_LONG).show();



                    }else{

                        //sharepref.edit().clear().commit();
                        //sharepref.edit().putString("key_login","no").commit();

                        Utils.ClearAllPrefs(UserMenuActivity.this);



                        Intent intenta = new Intent(UserMenuActivity.this,LoginActivity.class);
                        intenta.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intenta);
                        finish();

                        Snackbar snackbar = Snackbar
                                .make(findViewById(android.R.id.content), "  Thank You.!!!!", Snackbar.LENGTH_LONG);

                        // Changing message text color
                        snackbar.setActionTextColor(Color.BLUE);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();

                        Toast.makeText(UserMenuActivity.this, "Sign out Done !", Toast.LENGTH_LONG).show();




                    }

                //}



                // Log.d("usr email", Ruser_email);


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }




            progressDialog.dismiss();


        }
    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();

        super.onDestroy();
    }


}
