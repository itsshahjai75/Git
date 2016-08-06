package arihantmart.techno.arihantmart;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Home extends AppCompatActivity {


    DrawerLayout drawer;
    private MenuItem activeMenuItem;
    NavigationView navigationView;
    MaterialSearchView searchView;
    Boolean isInternetPresent = false;

    TextView tv_username,tv_useremail;

    SharedPreferences sharepref;
    String res,pemail;

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





        Toolbar toolbar = (Toolbar)this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.appicon);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#1976D2"));
            window.setNavigationBarColor(Color.parseColor("#1976D2"));
        }

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();


        FragmentTransaction tx;
        tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frame, new MainHomeFragment());
        tx.commit();


        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setHint("Search Product Name/Brand/Catagory");
        searchView.setCursorDrawable(R.drawable.custome_cursor);
        searchView.setEllipsize(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               Snackbar.make(findViewById(android.R.id.content), "Query: " + query, Snackbar.LENGTH_LONG).show();
                Intent result = new Intent(Home.this, Sub_categorylist.class);
                result.putExtra("category_name", query);
                result.putExtra("category_grid_id","null");
                result.putExtra("category_image","miscellaneous_icon");
                result.putExtra("search_keyword",query);
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




        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        tv_username=(TextView)header.findViewById(R.id.tv_username_nav);
        tv_useremail=(TextView)header.findViewById(R.id.tv_useremail_nav);
        LinearLayout ll_header=(LinearLayout)header.findViewById(R.id.ll_header_profile);
        ImageView img_user = (ImageView)header.findViewById(R.id.profile_image);

        try {

            String user_dp = sharepref.getString("key_usermobno", "null");




            Picasso.with(this)
                    .load("http://arihantmart.com/androidapp/images/"+user_dp+".jpg")
                    .placeholder(R.drawable.profile_icon) // optional
                    .error(R.drawable.profile_icon)         // optional
                    .noFade()
                    .memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(img_user);

        }catch (Exception expbitmap){
            expbitmap.printStackTrace();
        }



        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInternetPresent) {
                    pemail = tv_useremail.getText().toString();
                    if(pemail.equalsIgnoreCase("demo@demo.com")){
                        Toast.makeText(getApplicationContext(),"Register First !",Toast.LENGTH_SHORT).show();
                    }else {
                        new GetUserData().execute();
                    }
                } else {

                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), " Sorry! No Internet!!!", Snackbar.LENGTH_LONG);

                    // Changing message text color
                    snackbar.setActionTextColor(Color.BLUE);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();

                    Toast.makeText(Home.this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();


                }



            }
        });





        String useremail=sharepref.getString("key_useremail", "null");
        String username=sharepref.getString("key_username", "null");
        tv_username.setText(username);
        tv_useremail.setText(useremail);
        pemail = sharepref.getString("key_useremail", "null");

        ll_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
                    pemail = tv_useremail.getText().toString();
                    if(pemail.equalsIgnoreCase("demo@demo.com")){
                        Toast.makeText(getApplicationContext(),"Register First !",Toast.LENGTH_SHORT).show();
                    }else {
                        new GetUserData().execute();
                    }
                } else {

                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), " Sorry! No Internet!!!", Snackbar.LENGTH_LONG);

                    // Changing message text color
                    snackbar.setActionTextColor(Color.BLUE);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();

                    Toast.makeText(Home.this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();


                }


            }
        });













        // navigationView.setNavigationItemSelectedListener(this);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        assert navigationView != null;
        navigationView.setCheckedItem(R.id.nav_home);
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
                    case R.id.nav_home:
                       // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment= new MainHomeFragment();
                        break;
                    // For rest of the options we just show a toast on click

                    case R.id.nav_orders:


                        if(pemail.equalsIgnoreCase("demo@demo.com")){
                            Toast.makeText(getApplicationContext(),"Register First !",Toast.LENGTH_SHORT).show();
                        }else{
                            startActivity(new Intent(Home.this,Orderhistory.class));
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        }

                        break;

                    case R.id.nav_aboutus:
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

                    case R.id.nav_logout:
                        //Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
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

                        Toast.makeText(Home.this, "Logout Done !\nMiss you, Comeback Soon.  ", Toast.LENGTH_LONG).show();

                        sharepref.edit().putString("key_login","no").commit();
                        sharepref.edit().putString("key_useremail","no user").commit();


                        break;

                    case R.id.nav_termscondition:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment= new TermsFragment();
                        break;

                    case R.id.nav_privacypolicy:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment= new PrivacyPolicy();
                        break;

                   /* case R.id.spam:
                        Toast.makeText(getApplicationContext(),"Spam Selected",Toast.LENGTH_SHORT).show();
                        return true;*/
                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();

                    break;

                }

                if(fragment!=null){

                    fragmentTransaction.replace(R.id.frame,fragment).commit();

                    if(menuItem.getItemId()==R.id.nav_home){
                        getSupportActionBar().setTitle("Arihant Mart");

                    }else  if(menuItem.getItemId()==R.id.nav_orders){
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
                        doubleBackToExitPressedOnce=false;
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

        //noinspection SimplifiableIfStatement
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
        }

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
            progressDialog = ProgressDialog.show(Home.this, "Loading", "Please Wait...", true, false);
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

                HttpPost post = new HttpPost("http://arihantmart.com/androidapp/info.php?email_address="+pemail);//2015-5-15
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



                Toast.makeText(Home.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                // Log.i("RESPONSE", res);

                response_string=obj.getString("info");

                JSONArray array_res=new JSONArray(response_string);

                String Ruser_email = array_res.getJSONObject(0).getString("email");
                String Ruser_name = array_res.getJSONObject(0).getString("name");
                String Rmobile_no = array_res.getJSONObject(0).getString("mobileno");
                String Rgender = array_res.getJSONObject(0).getString("gender");
                String Raddress_line1 = array_res.getJSONObject(0).getString("address_line1");
                String Raddress_line2 = array_res.getJSONObject(0).getString("address_line2");
                String Raddress_line3 = array_res.getJSONObject(0).getString("address_line3");
                String Rlanmark = array_res.getJSONObject(0).getString("landmark");
                String Rpincode = array_res.getJSONObject(0).getString("pincode");
                String Rcity = array_res.getJSONObject(0).getString("city");
                String Rstate = array_res.getJSONObject(0).getString("state");
                String Ruser_pro_pic = array_res.getJSONObject(0).getString("profile_pic");





               /* byte[] imageAsBytes = Base64.decode(Ruser_pro_pic, Base64.DEFAULT);
                Bitmap btmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);*/




                Intent profile = new Intent(Home.this,User_profile.class);

                Bundle bndl= new Bundle();

                bndl.putString("email", Ruser_email);
                bndl.putString("name", Ruser_name);
                bndl.putString("mobile", Rmobile_no);
                bndl.putString("gender", Rgender);
                bndl.putString("address_line1", Raddress_line1);
                bndl.putString("address_line2", Raddress_line2);
                bndl.putString("address_line3", Raddress_line3);
                bndl.putString("landmark", Rlanmark);
                bndl.putString("city", Rcity);
                bndl.putString("pincode", Rpincode);
                bndl.putString("bitmap", Ruser_pro_pic);
                profile.putExtras(bndl);// ek akhu bundla
                //profile.putExtra("bitmap",Ruser_pro_pic);// bija ma only bitmap string----

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
