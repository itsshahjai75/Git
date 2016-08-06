package com.example.jay.codingcontests;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.ColorInt;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import DB.DBManager;
import DB.DatabaseHelper;


public class Home extends AppCompatActivity {


    private DBManager dbManager;

    DrawerLayout drawer;
    private MenuItem activeMenuItem;
    NavigationView navigationView;
    MaterialSearchView searchView;
    Boolean isInternetPresent = false;

    TextView tv_total_sponsers, tv_total_contests;

    DatabaseHelper dbh;
    SQLiteDatabase db;

    String res,res2;
    String[] sponsers;
    ArrayList<String> stringArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);


        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.appicon);

   /*     if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#1976D2"));
            window.setNavigationBarColor(Color.parseColor("#1976D2"));
        }*/



        dbManager = new DBManager(this);
        dbManager.open();




        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(this);
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new Getallcontests().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new Getallcontests().execute();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new GetallSponsers().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new GetallSponsers().execute();
            }






        } else {
            // Internet connection is not present
            // Ask user to connect to Internet



        }






        FragmentTransaction tx;
        tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frame, new MainHomeFragment());
        tx.commit();



        stringArrayList = new ArrayList<String>();
        sponsers= getResources().getStringArray(R.array.query_suggestions);


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setHint("Search Link");
        searchView.setCursorDrawable(R.drawable.custome_cursor);
        searchView.setEllipsize(true);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(findViewById(android.R.id.content), "Query: " + query, Snackbar.LENGTH_LONG).show();
                Intent result = new Intent(Home.this, ResultContestLink.class);
                result.putExtra("query", query);

                startActivity(result);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle Toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(Toggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        Toggle.syncState();


        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        tv_total_sponsers = (TextView) header.findViewById(R.id.tv_total_sponsers);
        tv_total_contests = (TextView) header.findViewById(R.id.tv_total_contests);

        tv_total_contests.setText("Total Contests : "+getProfilesCount());




        // navigationView.setNavigationItemSelectedListener(this);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        assert navigationView != null;
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setItemIconTintList(null);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                navigationView.getMenu().getItem(0).setChecked(false);


                //Checking if the item is in checked state or not, if not make it in checked state
                if (activeMenuItem != null) activeMenuItem.setChecked(false);
                activeMenuItem = menuItem;
                menuItem.setChecked(true);
                //else menuItem.setChecked(true);

                //Closing drawer on item click
                drawer.closeDrawers();
                Fragment fragment = null;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment = new MainHomeFragment();
                        break;

                    case R.id.nav_longcompetition:

                        fragment= new LongtermCompetition();
                        break;
                    case R.id.nav_exit:
                        System.exit(0);
                        getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        break;
                    case R.id.nav_running:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment= new LiveContests();
                        break;

                    /*case R.id.nav_aboutus:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment= new Aboutus_fragment();
                        break;
                    case R.id.nav_contactus:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment= new Contactus();
                        break;

                    case R.id.nav_howtobuy:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment= new HowtouseFragment();
                        break;

                    case R.id.nav_share:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();

                        Intent intentshare = new Intent(Intent.ACTION_SEND);
                        intentshare.setType("text/plain");
                        intentshare.putExtra(Intent.EXTRA_TEXT, "Botad's General Store Online. \"Arihant Mart\" all kind of products available just download and Enjoy.\n\n" + "https://play.google.com/store/apps/details?id=arihantmart.techno.arihantmart&hl=en");
                        startActivity(Intent.createChooser(intentshare, "Share"));


                        break;
                    case R.id.nav_feedback:
                        fragment= new Feedback_fragment();
                        //Toast.makeText(getApplicationContext(),"Feedback",Toast.LENGTH_SHORT).show();
                        break;



                    case R.id.nav_termscondition:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment= new TermsFragment();
                        break;

                    case R.id.nav_privacypolicy:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment= new PrivacyPolicy();
                        break;
*/
                   /* case R.id.spam:
                        Toast.makeText(getApplicationContext(),"Spam Selected",Toast.LENGTH_SHORT).show();
                        return true;*/
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();

                        break;

                }

                if (fragment != null) {

                    fragmentTransaction.replace(R.id.frame, fragment).commit();

                    if (menuItem.getItemId() == R.id.nav_home) {
                        getSupportActionBar().setTitle("Coding Contests");

                    } else if (menuItem.getItemId() == R.id.nav_running) {
                        menuItem.setChecked(false);
                    } else {
                        getSupportActionBar().setTitle(menuItem.getTitle().toString());
                    }

                } else {
                    menuItem.setChecked(false);
                }


                return true;
            }
        });


    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (searchView.isSearchOpen()) {
                searchView.closeSearch();
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    System.exit(0);
                    getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.home, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*//noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            // Toast.makeText(getApplicationContext(),"Cart",Toast.LENGTH_SHORT).show();

            if(pemail.equalsIgnoreCase("demo@demo.com")){
                Toast.makeText(getApplicationContext(),"Register First !",Toast.LENGTH_SHORT).show();
            }else{
                startActivity(new Intent(Home.this,CartProducts.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
            return true;
        }


        if (id == R.id.action_dailyoffers) {
            // Toast.makeText(getApplicationContext(),"Cart",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Home.this,DailyOffers.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    class Getallcontests extends AsyncTask<Object, Void, String> {



        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //pg_bar.setVisibility(View.VISIBLE);



        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code


                URL obj = new URL("http://clist.by/api/v1/json/contest/?limit=2000&start__gt=2016-01-01&order_by=-start&username=shah.jai75&api_key=434a1084d357751c87dbd1dc9a8e0885accdd30b");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                //System.out.println("GET Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    //System.out.println(response.toString());
                    res=response.toString();
                }

                // print result





            }catch(Exception e){
                e.printStackTrace();

            }



//            progressDialog.dismiss();
            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {



                Toast.makeText(Home.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                //

                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                //  Log.i("RESPONSE", res);



                String response_string_meta = obj.getString("meta");
                JSONObject obj_meta = new JSONObject(response_string_meta);
                String total_count = obj_meta.getString("total_count");




                response_string = obj.getString("objects");


                JSONArray array_res = new JSONArray(response_string);


                if (array_res.length() == 0) {

                    //                  Toast.makeText(getContext(),"No data found!",Toast.LENGTH_LONG).show();

                } else {


                    for (int j = 0; j < array_res.length(); j++) {

                        String duration = array_res.getJSONObject(j).getString("duration");
                        String end = array_res.getJSONObject(j).getString("end");
                        end=end.replace("T"," ");
                        //Log.d("startDate",end);
                        String start = array_res.getJSONObject(j).getString("start");
                        start=start.replace("T"," ");
                        // Log.d("end date",start);
                        String event = array_res.getJSONObject(j).getString("event");
                        String href = array_res.getJSONObject(j).getString("href");
                        String contestid = array_res.getJSONObject(j).getString("id");

                        String resource = array_res.getJSONObject(j).getString("resource");
                        JSONObject obj_resourse = new JSONObject(resource);
                        String resource_id = obj_resourse.getString("id");
                        String resource_name = obj_resourse.getString("name");



                        try {
                            dbManager.insert(duration, end, start, event, href, contestid, resource_id, resource_name);
                        }catch (SQLException sql){
                            //sql.printStackTrace();
                        }
                        // Log.d("usr Img",j+"=="+ Rpost_image);


                    }
                    dbManager.close();




















                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            //pg_bar.setVisibility(View.GONE);





        }
    }


    class GetallSponsers extends AsyncTask<Object, Void, String> {



        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //pg_bar.setVisibility(View.VISIBLE);



        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code



                URL obj = new URL("http://clist.by:80/api/v1/json/resource/?limit=500&username=shah.jai75&api_key=434a1084d357751c87dbd1dc9a8e0885accdd30b");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                //System.out.println("GET Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    //System.out.println(response.toString());
                    res2=response.toString();
                }

                // print result





            }catch(Exception e){
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



                Toast.makeText(Home.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                //

                return;
            }

            try {
                JSONObject obj = new JSONObject(res2);






                response_string = obj.getString("objects");


                JSONArray array_res = new JSONArray(response_string);
                tv_total_sponsers.setText("Total Sponsers : "+array_res.length());


                if (array_res.length() == 0) {

                    //                  Toast.makeText(getContext(),"No data found!",Toast.LENGTH_LONG).show();

                } else {


                    for (int j = 0; j < array_res.length(); j++) {


                        String contestid = array_res.getJSONObject(j).getString("id");

                        String resource_name = array_res.getJSONObject(j).getString("name");


                      stringArrayList.add(resource_name);
                        Log.i("RESPONSE", stringArrayList.toString());




                    }

                    sponsers=stringArrayList.toArray(sponsers);
                    searchView.setSuggestions(sponsers);



















                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            //pg_bar.setVisibility(View.GONE);





        }
    }

    public long getProfilesCount() {

        dbh = new DatabaseHelper(this);
        db = dbh.getWritableDatabase();

        long cnt  = DatabaseUtils.queryNumEntries(db, DatabaseHelper.TABLE_NAME);
        db.close();
        return cnt;
    }



}

