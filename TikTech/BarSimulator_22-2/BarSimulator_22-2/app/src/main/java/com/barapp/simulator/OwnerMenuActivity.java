package com.barapp.simulator;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.barapp.simulator.Adapter.OwnerMenu_Adapter;
import com.barapp.simulator.Object_Getter_Setter.OwnerMenu_Object;
import com.barapp.simulator.utils.Const;
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

public class OwnerMenuActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler{

    RecyclerView owner_recyclerview;
    LinearLayout linear_add_wine , linear_left ,linear_addwines,ll_owner_wallet;
    ImageView rate1 ,rate2 ,rate3 ,rate4 ,rate5 , profile_img,img_logout;
    TextView txt_owner_name ,txt_bar_name, txt_totalcoins;

    ArrayList<OwnerMenu_Object> results = new ArrayList<>();
    OwnerMenu_Adapter adapter;
    BillingProcessor bp;

    int planindex = 0;


    String user_id,str_coins="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ownerpage);

        owner_recyclerview = (RecyclerView)findViewById(R.id.owner_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        owner_recyclerview.setLayoutManager(mLayoutManager);
        owner_recyclerview.setItemAnimator(new DefaultItemAnimator());


        if(Utils.getPrefs(OwnerMenuActivity.this,Const.USER_ID).equalsIgnoreCase("")
                ||Utils.getPrefs(OwnerMenuActivity.this,Const.USER_ID)==null){
            startActivity(new Intent(OwnerMenuActivity.this , BarNameActivity.class));
            finish();
        }else {
            new Owner_info().execute();
        }

        bp = new BillingProcessor(this , Const.LICENSE_KEY , this);
        /*bp.consumeAsync(inventory.getPurchase(SKU_GAS),
                mConsumeFinishedListener);*/
        bp.consumePurchase(Const.Product_Plan10);

        linear_add_wine = (LinearLayout)findViewById(R.id.linear_addwine);
        linear_left     = (LinearLayout)findViewById(R.id.linear_left);
        linear_addwines = (LinearLayout)findViewById(R.id.linear_addwines);
        ll_owner_wallet = (LinearLayout)findViewById(R.id.ll_owner_wallet);
        ll_owner_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserMenuDialog();
            }
        });

        txt_owner_name   = (TextView)findViewById(R.id.owner_name);
        txt_bar_name   = (TextView)this.findViewById(R.id.bar_name);
        txt_totalcoins   = (TextView)findViewById(R.id.total_coins);

        rate1 = (ImageView)findViewById(R.id.rate1);
        rate2 = (ImageView)findViewById(R.id.rate2);
        rate3 = (ImageView)findViewById(R.id.rate3);
        rate4 = (ImageView)findViewById(R.id.rate4);
        rate5 = (ImageView)findViewById(R.id.rate5);

        profile_img = (ImageView)findViewById(R.id.profile_image);
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerMenuActivity.this,Profile_Get_Update.class));
                finish();
            }
        });
        img_logout= (ImageView)findViewById(R.id.img_logout);
        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Utils.ClearAllPrefs(OwnerMenuActivity.this);
                startActivity(new Intent(OwnerMenuActivity.this,LoginActivity.class));
                finish();*/

                new Logout().execute();

            }
        });

        adapter =new OwnerMenu_Adapter(results , OwnerMenuActivity.this);
        owner_recyclerview.setAdapter(adapter);

        getSupportActionBar().hide();

        linear_add_wine.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                ViewAnimationUtils.createCircularReveal(linear_add_wine , 0 , 0 , 0 , linear_add_wine.getHeight()).start();

                if(txt_totalcoins.getText().equals("0")){
                    Toast.makeText(OwnerMenuActivity.this,"You don't have balance, purchase coin.",Toast.LENGTH_LONG).show();
                }else{
                    startActivity(new Intent(OwnerMenuActivity.this , SelectWineCategoryActivity.class));
                }
            }
        });

        linear_addwines.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                ViewAnimationUtils.createCircularReveal(linear_add_wine , 0 , 0 , 0 , linear_add_wine.getHeight()).start();

                if(txt_totalcoins.getText().equals("0")){
                    Toast.makeText(OwnerMenuActivity.this,"You don't have balance, purchase coin.",Toast.LENGTH_LONG).show();
                }else{
                    startActivity(new Intent(OwnerMenuActivity.this , SelectWineCategoryActivity.class));
                }
                //startActivity(new Intent(OwnerMenuActivity.this , SelectWineCategoryActivity.class));
            }
        });

     }



    ProgressDialog upgradeDialog;
    private void promptForUpgrade() {
        AlertDialog.Builder upgradeAlert = new AlertDialog.Builder(OwnerMenuActivity.this);
        upgradeAlert.setTitle("Upgrade?");
        upgradeAlert.setCancelable(false);
        upgradeAlert.setMessage("Your 30 days trial version was expired, Please buy a full version.");
        upgradeAlert.setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //set progress dialog and start the in app purchase process
               // upgradeDialog = ProgressDialog.show(OwnerMenuActivity.this, "Please wait", "Upgrade transaction in process", true);
                bp.purchase(OwnerMenuActivity.this , Const.Product_Plan_Unlimitedversion);
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









    private  void UserMenuDialog() {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        final Dialog dialog = new Dialog(OwnerMenuActivity.this);
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

                        bp.purchase(OwnerMenuActivity.this , Const.Product_Plan10);
                        dialog.dismiss();
                    }else if(planindex == 2){

                        bp.purchase(OwnerMenuActivity.this , Const.Product_Plan20);
                        dialog.dismiss();
                    }else if(planindex == 3){

                        bp.purchase(OwnerMenuActivity.this , Const.Product_Plan30);
                        dialog.dismiss();
                    }else if(planindex == 4){

                        bp.purchase(OwnerMenuActivity.this , Const.Product_Plan40);
                        dialog.dismiss();
                    }else if(planindex == 5){

                        bp.purchase(OwnerMenuActivity.this , Const.Product_Plan50);
                        dialog.dismiss();
                    }else{
                        bp.purchase(OwnerMenuActivity.this , Const.Product_Plan60);
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
            new Update_coins().execute();
        }else if(productId.equalsIgnoreCase(Const.Product_Plan20)){
            str_coins="3150";
            new Update_coins().execute();
        }else if(productId.equalsIgnoreCase(Const.Product_Plan30)){
            str_coins="5500";
            new Update_coins().execute();
        }else if(productId.equalsIgnoreCase(Const.Product_Plan40)){
            str_coins="11500";
            new Update_coins().execute();
        }else if(productId.equalsIgnoreCase(Const.Product_Plan50)){
            str_coins="24000";
            new Update_coins().execute();
        }else if(productId.equalsIgnoreCase(Const.Product_Plan60)){
            str_coins="57500";
            new Update_coins().execute();
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

        if(errorCode==7){
            Toast.makeText(OwnerMenuActivity.this,"already purchased.",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(OwnerMenuActivity.this,"Purchase Error.",Toast.LENGTH_LONG).show();
            str_coins="100";
            new Update_coins().execute();
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
           // new Update_coins().execute();
        }


    }



    private void setRatings(double rating) {

        if (rating <= 0.5) {
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
        } else if (rating > 2 && rating <= 2.5) {
            rate1.setImageResource(R.drawable.star_enable);
            rate2.setImageResource(R.drawable.star_enable);
            rate3.setImageResource(R.drawable.star_enable_half);
            rate4.setImageResource(R.drawable.star_diseble);
            rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 2.5 && rating <= 3) {
            rate1.setImageResource(R.drawable.star_enable);
            rate2.setImageResource(R.drawable.star_enable);
            rate3.setImageResource(R.drawable.star_enable);
            rate4.setImageResource(R.drawable.star_diseble);
            rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 3 && rating <= 3.5) {
            rate1.setImageResource(R.drawable.star_enable);
            rate2.setImageResource(R.drawable.star_enable);
            rate3.setImageResource(R.drawable.star_enable);
            rate4.setImageResource(R.drawable.star_enable_half);
            rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 3.5 && rating <= 4) {
            rate1.setImageResource(R.drawable.star_enable);
            rate2.setImageResource(R.drawable.star_enable);
            rate3.setImageResource(R.drawable.star_enable);
            rate4.setImageResource(R.drawable.star_enable);
            rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 4 && rating <= 4.5) {
            rate1.setImageResource(R.drawable.star_enable);
            rate2.setImageResource(R.drawable.star_enable);
            rate3.setImageResource(R.drawable.star_enable);
            rate4.setImageResource(R.drawable.star_enable);
            rate5.setImageResource(R.drawable.star_enable_half);
        } else if (rating > 4.5) {
            rate1.setImageResource(R.drawable.star_enable);
            rate2.setImageResource(R.drawable.star_enable);
            rate3.setImageResource(R.drawable.star_enable);
            rate4.setImageResource(R.drawable.star_enable);
            rate5.setImageResource(R.drawable.star_enable);
        }else{
            rate1.setImageResource(R.drawable.star_diseble);
            rate2.setImageResource(R.drawable.star_diseble);
            rate3.setImageResource(R.drawable.star_diseble);
            rate4.setImageResource(R.drawable.star_diseble);
            rate5.setImageResource(R.drawable.star_diseble);
        }
    }

    private class Update_coins extends AsyncTask<Void ,Void ,Void>{

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(OwnerMenuActivity.this , "" ,"Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {



            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "user")
                    .add("passing_value" , "buy_coins")
                    .add("userid" , Utils.getPrefs(OwnerMenuActivity.this , Const.USER_ID))
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
                //JSONObject jsonData = jobj.getJSONObject("data");
                String status_code = jobj.getString("status_code");

                if(status_code.equalsIgnoreCase("200")){
                    startActivity(new Intent(OwnerMenuActivity.this,OwnerMenuActivity.class));
                    finish();
                }else if(status_code.equalsIgnoreCase("400")){
                    Toast.makeText(OwnerMenuActivity.this,jobj.getString("message"),Toast.LENGTH_LONG).show();

                }




            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class Owner_info extends AsyncTask<Void ,Void ,Void>{

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(OwnerMenuActivity.this , "" ,"Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {



            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("action" , "owner")
                    .add("passing_value" , "get")
                    .add("user_id" , Utils.getPrefs(OwnerMenuActivity.this , Const.USER_ID))
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

                String get_daycount = jsonData.getString("count_days");
                String status = jsonData.getString("user_status");

                int coutn_days = Integer.parseInt(get_daycount);

                if(coutn_days>30 && status.equalsIgnoreCase("inactive")) {
                    pDialog.dismiss();
                    promptForUpgrade();
                }

                String username = jsonData.getString("username");
                String barname = jsonData.getString("barname");
                String email = jsonData.getString("email");
                String  state = jsonData.getString("state");
                String coins  = jsonData.getString("total_coin");
                String ratings = jsonData.getString("rating");
                Log.d("Rating owner home",ratings);
                String img_url = jsonData.getString("profile_pic");




                if(username.length()>12){
                    txt_owner_name.setText(username.substring(0,1).toUpperCase()+
                            username.substring(1,12)+"...");
                }else{
                    txt_owner_name.setText(username.substring(0,1).toUpperCase()+
                            username.substring(1,username.length()));
                }

                if(barname.length()>10){
                    txt_bar_name.setText("Bar Name : "+barname.substring(0,10)+"...");
                }else{
                    txt_bar_name.setText("Bar Name : "+barname);
                }



                txt_totalcoins.setText(coins);
                setRatings(Double.parseDouble(ratings));

                if(!img_url.equals("")){
                    Picasso.with(OwnerMenuActivity.this)
                            .load(Const.IMAGE_URL+img_url)
                            .placeholder(R.drawable.noimage) // optional
                            .error(R.drawable.noimage)         // optional
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .into(profile_img);
                }

                JSONArray jarray = jsonData.getJSONArray("product_info");
                for(int i=0; i<jarray.length(); i++){

                    JSONObject jobjstr = jarray.getJSONObject(i);
                    String id =  jobjstr.getString("id");
                    String product_name = jobjstr.getString("title");
                    String unitprice = jobjstr.getString("selling_price");
                    String quantity  = jobjstr.getString("quantity");
                    String products  = jobjstr.getString("products");
                    String product_image = jobjstr.getString("product_image");
                    String product_prices = jobjstr.getString("products_prices");

                    results.add(new OwnerMenu_Object(id , product_image, unitprice , product_name , quantity ,products ,product_prices));
                }

                if(results.size() != 0){
                    linear_left.setVisibility(View.VISIBLE);
                    linear_addwines.setVisibility(View.GONE);
                }else{
                    linear_left.setVisibility(View.GONE);
                    linear_addwines.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();

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
            progressDialog = ProgressDialog.show(OwnerMenuActivity.this, "Loading", "Please Wait...", true, false);
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



                    Utils.ClearAllPrefs(OwnerMenuActivity.this);
                    //sharepref.edit().clear().commit();
                    //sharepref.edit().putString("key_login","no").commit();

                    Intent intenta = new Intent(OwnerMenuActivity.this, LoginActivity.class);
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

                    Toast.makeText(OwnerMenuActivity.this, "Sign out Done !", Toast.LENGTH_LONG).show();

                    LoginManager.getInstance().logOut();

                }else{

                    //sharepref.edit().clear().commit();
                    //sharepref.edit().putString("key_login","no").commit();

                    Utils.ClearAllPrefs(OwnerMenuActivity.this);



                    Intent intenta = new Intent(OwnerMenuActivity.this, LoginActivity.class);
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

                    Toast.makeText(OwnerMenuActivity.this, "Sign out Done !", Toast.LENGTH_LONG).show();

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



}
