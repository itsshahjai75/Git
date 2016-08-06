package com.jainisam.techno.jainisam;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

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

import java.util.ArrayList;
import java.util.List;

public class Home_screen_navigation extends AppCompatActivity {


    Fragment fragment = null;

    private MenuItem activeMenuItem;

    SharedPreferences sharepref;
    Toolbar toolbar;
    String pemail,res;

    NavigationView navigationView;



    Boolean isInternetPresent = false;



    MyTextView tv_profile_header;

    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen_navigation);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        pemail=sharepref.getString("key_useremail", null).toString();

        FragmentTransaction tx;
        tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frame_container, new Timelinefrgmnt());
        tx.commit();

        getSupportActionBar().setTitle("Jainism");



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle Toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){

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


        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        tv_profile_header = (MyTextView) header.findViewById(R.id.tv_header_title);
        tv_profile_header.setText(pemail);



        tv_profile_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    new GetUserData().execute();



            }
        });



        // navigationView.setNavigationItemSelectedListener(this);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        assert navigationView != null;
        navigationView.setCheckedItem(R.id.nav_timeline);
        navigationView.getMenu().getItem(0).setChecked(true);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                navigationView.getMenu().getItem(0).setChecked(false);





                //Checking if the item is in checked state or not, if not make it in checked state
                if (activeMenuItem != null)activeMenuItem.setChecked(false);
                activeMenuItem = menuItem;
                menuItem.setChecked(true);
                //else menuItem.setChecked(true);

                //Closing drawer on item click
                drawer.closeDrawers();
                Fragment fragment=null;
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_timeline:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment = new Timelinefrgmnt();

                        toolbar.setTitle("Jainism");
                        break;
                    // For rest of the options we just show a toast on click

                    case R.id.nav_dailynotes:

                        fragment = new Dailynotes();

                        toolbar.setTitle("Today's Pachkhaan Times");


                        break;

                    case R.id.nav_aboutus:
                        fragment = new AboutUs();
                        toolbar.setTitle("About Us");
                        break;
                    case R.id.nav_choghadiya:
                        Intent choghadiya = new Intent(Home_screen_navigation.this,Choghadiya.class);
                        startActivity(choghadiya);

                        break;

                    case R.id.allinone:

                        Intent allinone = new Intent(Home_screen_navigation.this,Allinone.class);
                        startActivity(allinone);

                        break;

                    case R.id.nav_share:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, "Now you never miss any tithi,also location base pachhkhan times and many more just look and share.\n" + "https://play.google.com/store/apps/details?id=com.jainisam.techno.jainisam&hl=en");
                        startActivity(Intent.createChooser(intent, "Share"));


                        break;
                    case R.id.nav_pachkhan:
                        fragment = new Pachkhaan();
                        toolbar.setTitle("Pachkhaan");

                        break;

                    case R.id.nav_logout:
                        //=================================================================
                        FacebookSdk.sdkInitialize(getApplicationContext());
                        LoginManager.getInstance().logOut();
                        //======================================================================

                        Intent intenta = new Intent(getApplicationContext(), LoginActivity.class);
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

                        Toast.makeText(Home_screen_navigation.this, "Logout Done ! \nMiss you, Comeback Soon.  ", Toast.LENGTH_LONG).show();

                        sharepref.edit().putString("key_login","no").commit();
                        sharepref.edit().putString("key_useremail","no user").commit();


                        break;

                    case R.id.nav_punchang_item:
                        Intent punchang = new Intent(Home_screen_navigation.this,Punchang.class);
                        startActivity(punchang);

                        break;

                    case R.id.nav_deravasi_punchang_item:
                        Intent Dpunchang = new Intent(Home_screen_navigation.this,Deravasi_panchang.class);
                        startActivity(Dpunchang);

                        break;

                    case R.id.nav_donateus:
                        fragment = new Donateus();
                        toolbar.setTitle("Donate Us");
                        break;

                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();

                        break;

                }

                if(fragment!=null){

                    fragmentTransaction.replace(R.id.frame_container,fragment).commit();

                    if(menuItem.getItemId()==R.id.nav_timeline){
                        getSupportActionBar().setTitle("Jainism");

                    }else  if(menuItem.getItemId()==R.id.nav_choghadiya){
                        menuItem.setChecked(false);
                    }else  if(menuItem.getItemId()==R.id.allinone){
                        menuItem.setChecked(false);
                    }else  if(menuItem.getItemId()==R.id.nav_punchang_item){
                        menuItem.setChecked(false);
                    }else  if(menuItem.getItemId()==R.id.nav_deravasi_punchang_item){
                        menuItem.setChecked(false);
                    } else {
                        getSupportActionBar().setTitle(menuItem.getTitle().toString());
                    }

                }else{
                    menuItem.setChecked(false);
                }



                return true;
            }
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //  int id = item.getItemId();

        /*//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }




    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }



    class GetUserData extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Home_screen_navigation.this, "Loading", "Please Wait--------.", true, false);
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

                HttpPost post = new HttpPost("http://technocratsappware.com/androidapps/info.php?email_address="+pemail);//2015-5-15
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                //params.add(new BasicNameValuePair("key_item",item_ingredients));


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    // System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
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



                Toast.makeText(Home_screen_navigation.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                // Log.i("RESPONSE", res);

                response_string=obj.getString("info");

                JSONArray array_res=new JSONArray(response_string);

                String Ruser_email = array_res.getJSONObject(0).getString("email_address");
                String Ruser_name = array_res.getJSONObject(0).getString("user_name");
                String Rmobile_no = array_res.getJSONObject(0).getString("mobile_no");
                String Rgender = array_res.getJSONObject(0).getString("gender");
                String Raddress = array_res.getJSONObject(0).getString("address");
                String Rcity = array_res.getJSONObject(0).getString("city");
                String Rstate = array_res.getJSONObject(0).getString("state");
                String Ruser_pro_pic = array_res.getJSONObject(0).getString("user_pro_pic");





                byte[] imageAsBytes = Base64.decode(Ruser_pro_pic, Base64.DEFAULT);
                Bitmap btmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);




                Intent profile = new Intent(Home_screen_navigation.this,Myprofile.class);

                Bundle bndl= new Bundle();

                bndl.putString("email", Ruser_email);
                bndl.putString("name", Ruser_name);
                bndl.putString("mobile", Rmobile_no);
                bndl.putString("gender", Rgender);
                bndl.putString("address", Raddress);
                bndl.putString("city", Rcity);
                bndl.putString("state", Rstate);
                profile.putExtras(bndl);// ek akhu bundla
                profile.putExtra("bitmap",btmp);// bija ma only bitmap string----

                startActivity(profile);





                // Log.d("usr email", Ruser_email);





            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();




        }
    }



}
