package com.jainisam.techno.jainisam;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class Place_results extends AppCompatActivity {




    ProgressDialog progressDialog,progressDialog2,progressDialog3,progressDialog4;

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    SharedPreferences sharepref;
    Toolbar toolbar;
    String pemail,res,res2,res3,get_ext_value;
    View footerLayout;

    CustomRecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    Boolean isInternetPresent = false;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean loading = true;
    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    int load_count=1;
    ArrayList results,spiner_array;

    TextView textView_loadmore,tv_totalresult;
    Spinner dynamicSpinner;
    ArrayAdapter<String> spineradapter;
    String spiner_city;


  int check=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_results);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        get_ext_value = getIntent().getStringExtra("place_type_intent");
        Log.d("PLACeType", get_ext_value.toString());


        spiner_array = new ArrayList<String>();

        dynamicSpinner = (Spinner) findViewById(R.id.dynamic_spinner);

        //String[] items = new String[] { "Chai Latte", "Green Tea", "Black Tea" };simple_list_item_checked

       spineradapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, spiner_array);



        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                check=check+1;

                if(check>1) {
                    Log.v("item", (String) parent.getItemAtPosition(position));
                    spiner_city = parent.getItemAtPosition(position).toString();
                    if (spiner_city.equalsIgnoreCase("All")) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new GetPlaceAll().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            new GetPlaceAll().execute();
                        }

                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new Getplacebycity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            new Getplacebycity().execute();
                        }

                    }
                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });



        tv_totalresult=(TextView)this.findViewById(R.id.tv_totalresult);



        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                results.clear();
                mAdapter.notifyDataSetChanged();
                new GetPlaceAll().execute();
                load_count = 1;
                mRecyclerView.setEnabled(false);
                mRecyclerView.setNestedScrollingEnabled(false);
                Toast.makeText(Place_results.this, "Loading...", Toast.LENGTH_LONG).show();


            }
        });

        // Fetching footer layout
        footerLayout = findViewById(R.id.footerView);
        // Fetching footer layout
        footerLayout = findViewById(R.id.footerView);

// Fetching the textview declared in footer.xml

        FloatingActionButton addplace = (FloatingActionButton) footerLayout.findViewById(R.id.fab_addplace);

        addplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addplace = new Intent(Place_results.this,AddnewPlace.class);
                startActivity(addplace);

            }
        });

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        pemail=sharepref.getString("key_useremail", null).toString();

        // Initialize recycler view
        mRecyclerView = (CustomRecyclerView) findViewById(R.id.place_recycler_view);
        mRecyclerView.setHasFixedSize(true);


        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests

            results = new ArrayList<DataObject_post>();
            mAdapter = new MyRecyclerAdapter_place(results, getBaseContext());


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new GetPlaceAll().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new GetPlaceAll().execute();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new Getcity_bycatagory().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new Getcity_bycatagory().execute();
            }


        } else {
            // Internet connection is not present
            // Ask user to connect to Internet

            mRecyclerView.setVisibility(View.GONE);
            footerLayout.setVisibility(View.GONE);

        }




        final LinearLayout parent = (LinearLayout) findViewById(R.id.home_relative_main);
        textView_loadmore = new TextView(Place_results.this);



        textView_loadmore.setText("Load More      ");
        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, textView_loadmore.getId());
        textView_loadmore.setLayoutParams(params);
        textView_loadmore.setGravity(Gravity.CENTER);
        textView_loadmore.setBackgroundColor(Color.parseColor("#993F51B5"));
        textView_loadmore.setTextSize(35);
        //textView.setTypeface(cFont);
        textView_loadmore.setTextColor(Color.parseColor("#ffffff"));

        parent.addView(textView_loadmore);
        textView_loadmore.setVisibility(View.GONE);

        textView_loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Log.v("...", "Last Item Wow !");
                load_count = load_count + 1;
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                textView_loadmore.setVisibility(View.GONE);

                new GetPlace_increment().execute();


                loading = false;


            }
        });



        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
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

                    if(textView_loadmore.getVisibility()==View.VISIBLE){
                        textView_loadmore.setVisibility(View.GONE);
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

                            textView_loadmore.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                            textView_loadmore.setVisibility(View.VISIBLE);



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

                //=======================================================================================================
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }


        });







    }




    class GetPlaceAll extends AsyncTask<Object, Void, String> {

        /*
               protected ProgressDialog progressDialog;*/

        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");



            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Place_results.this, "Loading", "Please Wait....", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                //String postURL = "http://169.254.76.188:8084/Sunil/order_entery";
                //HttpPost post = new HttpPost(postURL);

                HttpPost post = new HttpPost("http://technocratsappware.com/androidapps/place_all_by_type.php?placetype="+get_ext_value);
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                      System.out.println("response got from server----- " + resp);


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



                Toast.makeText(Place_results.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                // Log.i("RESPONSE", res);

                response_string = obj.getString("info");


                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() == 0) {

                    Toast.makeText(Place_results.this,"Sorry!!!No data found!",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                } else {



                    mRecyclerView.setAdapter(mAdapter);
                    if(results.size()>1){
                        results.clear();
                        mAdapter.notifyDataSetChanged();
                    }







                    for (int j = 0; j < array_res.length(); j++) {

                        String place_id = array_res.getJSONObject(j).getString("place_id");
                        String place_type = array_res.getJSONObject(j).getString("place_type");
                        String city = array_res.getJSONObject(j).getString("city");
                        String name = array_res.getJSONObject(j).getString("name");
                        String address = array_res.getJSONObject(j).getString("address");
                        String tel_no = array_res.getJSONObject(j).getString("tel_no");
                        String mob_no = array_res.getJSONObject(j).getString("mob_no");
                        String email_address = array_res.getJSONObject(j).getString("email_address");
                        String contact_person = array_res.getJSONObject(j).getString("contact_person");
                        String bhagvan_name = array_res.getJSONObject(j).getString("bhagvan_name");


                        String city_output = city.substring(0, 1).toUpperCase() + city.substring(1);

                        tv_totalresult.setText(array_res.getJSONObject(0).getString("place_type").toUpperCase());


                        if(tel_no.equalsIgnoreCase("null")){
                            tel_no="NA";
                        }
                        if(mob_no.equalsIgnoreCase("null")){
                            mob_no="NA";
                        }
                        if(email_address.equalsIgnoreCase("null")){
                            email_address="NA";
                        }

                        DataObject_post obj1 = new DataObject_post("Name : " + name
                                , address+"\nCity : "+city_output,"null","Tel.No : "+tel_no+"\nMob.No : "+mob_no+"\ne-mail : "+email_address);
                        results.add(obj1);



                        // Log.d("usr Img",j+"=="+ Rpost_image);
                    }




                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // progressDialog.dismiss();
            progressDialog.dismiss();
            mRecyclerView.setVisibility(View.VISIBLE);
            footerLayout.setVisibility(View.VISIBLE);

            if(mSwipeRefreshLayout.isRefreshing()==true){
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setEnabled(false);}else{
                mRecyclerView.setNestedScrollingEnabled(true);
                mRecyclerView.setEnabled(true);
            }



        }
    }

    class Getcity_bycatagory extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //pg_bar.setVisibility(View.VISIBLE);

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog2 = ProgressDialog.show(Place_results.this, "Loading", "Please Wait----...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.

        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                //String postURL = "http://169.254.76.188:8084/Sunil/order_entery";
                //HttpPost post = new HttpPost(postURL);

                HttpPost post = new HttpPost("http://technocratsappware.com/androidapps/allcity_place.php?place_type="+get_ext_value);
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                //params.add(new BasicNameValuePair("loadcount",Integer.toString(load_count)));


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp3 = EntityUtils.toString(resEntity);
                    res3 = resp3;

                    //  System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }



//            progressDialog.dismiss();
            return res3;

        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res3 == null || res3.equals("")) {



                Toast.makeText(Place_results.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                //
                progressDialog2.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res3);


//                 Log.i("RESPONSE", res2);

                response_string = obj.getString("info");


                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() == 0) {

                    //                  Toast.makeText(Home_screen_navigation.this,"No data found!",Toast.LENGTH_LONG).show();

                } else {


                    dynamicSpinner.setAdapter(spineradapter);
                    spiner_array.clear();
                    spineradapter.notifyDataSetChanged();
                    spiner_array.add("All");

                    for (int j = 0; j < array_res.length(); j++) {

                        String city = array_res.getJSONObject(j).getString("city");

                        String city_output = city.substring(0, 1).toUpperCase() + city.substring(1);

                        spiner_array.add(city_output);



                    }
                    spineradapter.notifyDataSetChanged();




                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog2.dismiss();
            //pg_bar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            footerLayout.setVisibility(View.VISIBLE);


           /* if(mSwipeRefreshLayout.isRefreshing()==true){
                mSwipeRefreshLayout.setRefreshing(false);}*/



        }
    }



    class GetPlace_increment extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //pg_bar.setVisibility(View.VISIBLE);

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog3 = ProgressDialog.show(Place_results.this, "Loading", "Please Wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.

        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                //String postURL = "http://169.254.76.188:8084/Sunil/order_entery";
                //HttpPost post = new HttpPost(postURL);

                HttpPost post = new HttpPost("http://technocratsappware.com/androidapps/place_all_by_type_increment.php?loadcount="+load_count+"&&placetype="+get_ext_value);
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

            if (res2 == null || res2.equals("")) {



                Toast.makeText(Place_results.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                //
                progressDialog3.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res2);


//                 Log.i("RESPONSE", res2);

                response_string = obj.getString("info");


                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() == 0) {

                    //                  Toast.makeText(Home_screen_navigation.this,"No data found!",Toast.LENGTH_LONG).show();

                } else {


                    for (int j = 0; j < array_res.length(); j++) {

                        String place_id = array_res.getJSONObject(j).getString("place_id");
                        String place_type = array_res.getJSONObject(j).getString("place_type");
                        String city = array_res.getJSONObject(j).getString("city");
                        String name = array_res.getJSONObject(j).getString("name");
                        String address = array_res.getJSONObject(j).getString("address");
                        String tel_no = array_res.getJSONObject(j).getString("tel_no");
                        String mob_no = array_res.getJSONObject(j).getString("mob_no");
                        String email_address = array_res.getJSONObject(j).getString("email_address");
                        String contact_person = array_res.getJSONObject(j).getString("contact_person");
                        String bhagvan_name = array_res.getJSONObject(j).getString("bhagvan_name");

                        String city_output = city.substring(0, 1).toUpperCase() + city.substring(1);

                        if(tel_no.equalsIgnoreCase("null")){
                            tel_no="";
                        }
                        if(mob_no.equalsIgnoreCase("null")){
                            mob_no="";
                        }
                        if(email_address.equalsIgnoreCase("null")){
                            email_address="";
                        }


                        DataObject_post obj2 = new DataObject_post("Name : " + name
                                , address+"\nCity : "+city_output,"null","Tel.No : "+tel_no+"\nMob.No : "+mob_no+"\ne-mail : "+email_address);
                        results.add(obj2);






                        mAdapter.notifyDataSetChanged();
                        // Log.d("usr Img",j+"=="+ Rpost_image);

                    }





                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog3.dismiss();
            //pg_bar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            footerLayout.setVisibility(View.VISIBLE);


           /* if(mSwipeRefreshLayout.isRefreshing()==true){
                mSwipeRefreshLayout.setRefreshing(false);}*/



        }
    }


    class Getplacebycity extends AsyncTask<Object, Void, String> {

        /*
               protected ProgressDialog progressDialog;*/

        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");



            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog4 = ProgressDialog.show(Place_results.this, "Loading", "Please Wait....", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                //String postURL = "http://169.254.76.188:8084/Sunil/order_entery";
                //HttpPost post = new HttpPost(postURL);

                HttpPost post = new HttpPost("http://technocratsappware.com/androidapps/place_bycity.php?placetype="+get_ext_value+"&&city="+spiner_city);
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    System.out.println("response got from server----- " + resp);


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



                Toast.makeText(Place_results.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                progressDialog4.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                // Log.i("RESPONSE", res);

                response_string = obj.getString("info");


                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() == 0) {

                    Toast.makeText(Place_results.this,"Sorry!!!No data found!",Toast.LENGTH_LONG).show();
                    progressDialog4.dismiss();

                } else {



                    mRecyclerView.setAdapter(mAdapter);
                    results.clear();
                    mAdapter.notifyDataSetChanged();







                    for (int j = 0; j < array_res.length(); j++) {

                        String place_id = array_res.getJSONObject(j).getString("place_id");
                        String place_type = array_res.getJSONObject(j).getString("place_type");
                        String city = array_res.getJSONObject(j).getString("city");
                        String name = array_res.getJSONObject(j).getString("name");
                        String address = array_res.getJSONObject(j).getString("address");
                        String tel_no = array_res.getJSONObject(j).getString("tel_no");
                        String mob_no = array_res.getJSONObject(j).getString("mob_no");
                        String email_address = array_res.getJSONObject(j).getString("email_address");
                        String contact_person = array_res.getJSONObject(j).getString("contact_person");
                        String bhagvan_name = array_res.getJSONObject(j).getString("bhagvan_name");


                        String city_output = city.substring(0, 1).toUpperCase() + city.substring(1);

                        tv_totalresult.setText(array_res.getJSONObject(0).getString("place_type").toUpperCase());


                        if(tel_no.equalsIgnoreCase("null")){
                            tel_no="NA";
                        }
                        if(mob_no.equalsIgnoreCase("null")){
                            mob_no="NA";
                        }
                        if(email_address.equalsIgnoreCase("null")){
                            email_address="NA";
                        }

                        DataObject_post obj1 = new DataObject_post("Name : " + name
                                , address+"\nCity : "+city_output,"null","Tel.No : "+tel_no+"\nMob.No : "+mob_no+"\ne-mail : "+email_address);
                        results.add(obj1);



                        // Log.d("usr Img",j+"=="+ Rpost_image);
                    }

                    mAdapter.notifyDataSetChanged();





                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // progressDialog.dismiss();
            progressDialog4.dismiss();
            mRecyclerView.setVisibility(View.VISIBLE);
            footerLayout.setVisibility(View.VISIBLE);

            if(mSwipeRefreshLayout.isRefreshing()==true){
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setEnabled(false);}else{
                mRecyclerView.setNestedScrollingEnabled(true);
                mRecyclerView.setEnabled(true);
            }



        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public void onBackPressed() {


        finish();
    }


}
