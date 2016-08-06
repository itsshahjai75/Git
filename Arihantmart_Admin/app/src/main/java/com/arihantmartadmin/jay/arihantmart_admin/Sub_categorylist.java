package com.arihantmartadmin.jay.arihantmart_admin;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Sub_categorylist extends AppCompatActivity {

    ProgressDialog progressDialog;


    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    int load_count=1;

    //ui control
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView img_subcatagory;
    CustomRecyclerView mRecyclerView;

    private CustomRecyclerView.Adapter adapter;

    ArrayList results;

    String pemail,res2,res,str_catgry_name,str_catgry_id,str_imageresourse,str_keyword;

    Boolean isInternetPresent = false;

    MaterialSearchView searchView;
    SharedPreferences sharepref;
    View footerLayout;
    private boolean loading = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categorylist);
        final FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);


        collapsingToolbarLayout = (CollapsingToolbarLayout)this.findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);




        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        pemail = sharepref.getString("key_useremail", "null");



        str_catgry_name=getIntent().getStringExtra("category_name");
        str_catgry_id=getIntent().getStringExtra("category_id");
        str_imageresourse=getIntent().getStringExtra("category_image");
        str_keyword=getIntent().getStringExtra("search_keyword");
        Log.d("strs",str_imageresourse);








        img_subcatagory=(ImageView)this.findViewById(R.id.image_subcatagory);
        int imageResourceId = this.getBaseContext().getResources().getIdentifier(str_imageresourse, "drawable", this.getPackageName());
        img_subcatagory.setImageResource(imageResourceId);



        collapsingToolbarLayout.setTitle(str_catgry_name);









        // Initialize recycler view
        mRecyclerView = (CustomRecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        results = new ArrayList<DataObject_Itemlist>();
        adapter = new MyRecyclerAdapter_Itemlist(results, this);

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests



            if(str_keyword.equalsIgnoreCase("null")){

                new Getcatagoryitem().execute();

            }else {
                new GetAllitems().execute();
            }


        } else {
            // Internet connection is not present
            // Ask user to connect to Internet

            mRecyclerView.setVisibility(View.GONE);
            // footerLayout.setVisibility(View.GONE);

        }



        // Fetching footer layout
        footerLayout = findViewById(R.id.footerView);

// Fetching the textview declared in footer.xml

        TextView loadmore = (TextView) footerLayout.findViewById(R.id.btn_loadmore);
        footerLayout.setVisibility(View.GONE);

        loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Getcatagoryitem_increment().execute();
                footerLayout.setVisibility(View.GONE);

            }
        });






        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {


//=========================================================================================================
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    //onHide();


                    footerLayout.animate().translationY(footerLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    //onShow();
                    footerLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                    controlsVisible = true;
                    scrolledDistance = 0;

                    if(footerLayout.getVisibility()==View.VISIBLE){
                        footerLayout.setVisibility(View.GONE);
                    }

                }

                if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                    scrolledDistance += dy;
                }

                //======================================================================================================

                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                            footerLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                            footerLayout.setVisibility(View.VISIBLE);



                        }
                    }


                    if (!loading && (totalItemCount - visibleItemCount) <= (pastVisiblesItems + visibleThreshold)) {
                        // End has been reached

                       /* // Do something
                        current_page++;

                        onLoadMore(current_page);*/

                        loading = true;

                    }


                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView mRecyclerView, int newState) {
                super.onScrollStateChanged(mRecyclerView, newState);
            }


        });










    }

    class Getcatagoryitem extends AsyncTask<Object, Void, String> {

        /*
               protected ProgressDialog progressDialog;*/
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            progressDialog = ProgressDialog.show(Sub_categorylist.this, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                String query="http://arihantmart.com/androidapp/itembycatgory.php?catagoryname="+str_catgry_name;
                query=query.replaceAll(" ","%20");
                //String query = URLEncoder.encode("http://arihantmart.com/androidapp/itembycatgory.php?catagoryname="+str_catgry_name, "utf-8");
                HttpPost post = new HttpPost(query);
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    //  System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }

            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {



                Toast.makeText(getBaseContext(),"No data found!",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            try {

                Log.i("RESPONSE", res);
                JSONObject obj = new JSONObject(res);




                response_string = obj.getString("info");


                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() == 0) {

                    Toast.makeText(getBaseContext(),"No data found!",Toast.LENGTH_LONG).show();

                } else {



                    mRecyclerView.setAdapter(adapter);





                    for (int j = 0; j < array_res.length(); j++) {

                        String item_code = array_res.getJSONObject(j).getString("item_code");
                        String item_compnay_name = array_res.getJSONObject(j).getString("item_compnay_name");
                        String item_name = array_res.getJSONObject(j).getString("item_name");
                        String item_category = array_res.getJSONObject(j).getString("item_category");
                        String item_sub_categary = array_res.getJSONObject(j).getString("item_sub_categary");
                        String mrp = array_res.getJSONObject(j).getString("mrp");
                        String our_price = array_res.getJSONObject(j).getString("our_price");
                        String quantity = array_res.getJSONObject(j).getString("quantity");
                        String discount = array_res.getJSONObject(j).getString("discount");

                        double MRP = Double.parseDouble(mrp);
                        double OURPRICE = Double.parseDouble(our_price);



                        double PER_ITEM_SAVE = MRP - OURPRICE;

                        String saving_amount = Double.toString(PER_ITEM_SAVE);

                        /*Total_cart_amount = Total_cart_amount + PER_ITEM_TOTAL;
                        Total_cart_saving_amount=Total_cart_saving_amount + PER_ITEM_TOTATSAVE;*/




                        DataObject_Itemlist obj1 = new DataObject_Itemlist(item_name + " ("+item_compnay_name+")", "MRP: ₹ "+mrp+"\nOur Price: ₹ "+our_price, str_imageresourse, "Saving : ₹ "+saving_amount,item_code);
                        results.add(obj1);



                        // Log.d("usr Img",j+"=="+ Rpost_image);
                    }


                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();

            mRecyclerView.setVisibility(View.VISIBLE);
            //footerLayout.setVisibility(View.VISIBLE);

            /*if(mSwipeRefreshLayout.isRefreshing()==true){
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setEnabled(false);}else{
                mRecyclerView.setNestedScrollingEnabled(true);
                mRecyclerView.setEnabled(true);
            }*/



        }
    }


    class Getcatagoryitem_increment extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";
        protected ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //pg_bar.setVisibility(View.VISIBLE);

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Sub_categorylist.this, "Loading", "Please Wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.

        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code
                load_count++;
                HttpClient client = new DefaultHttpClient();
                String query2="http://arihantmart.com/androidapp/itembycatgory_increment.php?loadcount="+load_count+"&&catagoryname="+str_catgry_name;
                query2=query2.replaceAll(" ","%20");
                //Log.d("URL",query2);


                HttpPost post = new HttpPost(query2);
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                //params.add(new BasicNameValuePair("loadcount",Integer.toString(load_count)));


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp2 = EntityUtils.toString(resEntity);
                    res2 = resp2;

                    //  System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }



//            progressDialog.dismiss();
            return res2;

        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res2 == null || res2.equals("null")) {

                Toast.makeText(getBaseContext(),"Finish,That's it!",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res2);


                Log.i("RESPONSE", res2);

                response_string = obj.getString("info");


                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() == 0) {

                    //                  Toast.makeText(Home_screen_navigation.this,"No data found!",Toast.LENGTH_LONG).show();

                } else {


                    for (int j = 0; j < array_res.length(); j++) {
                        String item_code = array_res.getJSONObject(j).getString("item_code");
                        String item_compnay_name = array_res.getJSONObject(j).getString("item_compnay_name");
                        String item_name = array_res.getJSONObject(j).getString("item_name");
                        String item_category = array_res.getJSONObject(j).getString("item_category");
                        String item_sub_categary = array_res.getJSONObject(j).getString("item_sub_categary");
                        String mrp = array_res.getJSONObject(j).getString("mrp");
                        String our_price = array_res.getJSONObject(j).getString("our_price");
                        String quantity = array_res.getJSONObject(j).getString("quantity");
                        String discount = array_res.getJSONObject(j).getString("discount");


                        double MRP = Double.parseDouble(mrp);
                        double OURPRICE = Double.parseDouble(our_price);



                        double PER_ITEM_SAVE = MRP - OURPRICE;

                        String saving_amount = Double.toString(PER_ITEM_SAVE);

                        DataObject_Itemlist obj12 = new DataObject_Itemlist(item_name + " ("+item_compnay_name+")", "MRP: ₹ "+mrp+"\nOur Price: ₹ "+our_price, str_imageresourse, "Saving : ₹ "+saving_amount,item_code);
                        results.add(obj12);

                        adapter.notifyDataSetChanged();
                        // Log.d("usr Img",j+"=="+ Rpost_image);

                    }



                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            progressDialog.dismiss();





        }
    }

    class GetAllitems extends AsyncTask<Object, Void, String> {

        /*
               protected ProgressDialog progressDialog;*/
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            progressDialog = ProgressDialog.show(Sub_categorylist.this, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                String query="http://arihantmart.com/androidapp/searchall_keyword.php?name="+str_keyword;
                query=query.replaceAll(" ","%20");
                //String query = URLEncoder.encode("http://arihantmart.com/androidapp/itembycatgory.php?catagoryname="+str_catgry_name, "utf-8");
                HttpPost post = new HttpPost(query);
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    //  System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }

            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {



                Toast.makeText(getBaseContext(),"No data found!",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                Log.i("RESPONSE", res);

                response_string = obj.getString("info");


                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() == 0) {

                    Toast.makeText(getBaseContext(),"No data found!",Toast.LENGTH_LONG).show();

                } else {



                    mRecyclerView.setAdapter(adapter);
                    results.clear();





                    for (int j = 0; j < array_res.length(); j++) {

                        String item_code = array_res.getJSONObject(j).getString("item_code");
                        String item_compnay_name = array_res.getJSONObject(j).getString("item_compnay_name");
                        String item_name = array_res.getJSONObject(j).getString("item_name");
                        String item_category = array_res.getJSONObject(j).getString("item_category");
                        String item_sub_categary = array_res.getJSONObject(j).getString("item_sub_categary");
                        String mrp = array_res.getJSONObject(j).getString("mrp");
                        String our_price = array_res.getJSONObject(j).getString("our_price");
                        String quantity = array_res.getJSONObject(j).getString("quantity");
                        String discount = array_res.getJSONObject(j).getString("discount");

                        int MRP = Integer.parseInt(mrp);
                        int OURPRICE = Integer.parseInt(our_price);



                        int PER_ITEM_SAVE = MRP - OURPRICE;

                        String saving_amount = Integer.toString(PER_ITEM_SAVE);



                        DataObject_Itemlist obj1 = new DataObject_Itemlist(item_name + " ("+item_compnay_name+")", "MRP: ₹ "+mrp+"\nOur Price: ₹ "+our_price, str_imageresourse, "Saving : ₹ "+saving_amount,item_code);
                        results.add(obj1);



                        // Log.d("usr Img",j+"=="+ Rpost_image);
                    }


                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();

            mRecyclerView.setVisibility(View.VISIBLE);
            //footerLayout.setVisibility(View.VISIBLE);

            /*if(mSwipeRefreshLayout.isRefreshing()==true){
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setEnabled(false);}else{
                mRecyclerView.setNestedScrollingEnabled(true);
                mRecyclerView.setEnabled(true);
            }*/



        }
    }












    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        //MenuItem item = menu.findItem(R.id.action_search);
        //searchView.setMenuItem(item);





        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }



        //return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
            super.onBackPressed();
            return;


    }
}
