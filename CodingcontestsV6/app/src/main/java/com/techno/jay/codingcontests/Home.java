package com.techno.jay.codingcontests;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.techno.jay.codingcontests.CustomViews.CustomTypefaceSpan;
import com.techno.jay.codingcontests.Firebase.MainHomeFragmentFirebase;
import com.techno.jay.codingcontests.UtilitiClasses.Const;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class Home extends AppCompatActivity implements BillingProcessor.IBillingHandler{



    SharedPreferences sharepref;

    DrawerLayout drawer;
    private MenuItem activeMenuItem;
    NavigationView navigationView;
    MaterialSearchView searchView;


    public static TextView tv_total_sponsers, tv_total_contests;

    private DatabaseReference databaseReferenceUser,databaseReferenceEventData;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;

    private TelephonyManager mTelephonyManager;
    String simTwoNumber="", simOneNumber="";

    Boolean isInternetPresent = false;
    String str_IMEI="",str_cellNumber,token,date;

    BillingProcessor bp;


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "ProductSans-Regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseApp.initializeApp(this);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);

        bp = new BillingProcessor(Home.this , Const.LICENSE_KEY , Home.this);
        /*bp.consumeAsync(inventory.getPurchase(SKU_GAS),
                mConsumeFinishedListener);*/
        bp.consumePurchase(Const.Product_Plan_Unlimitedversion);

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

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= 23){
            if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        PERMISSIONS_REQUEST_READ_PHONE_STATE);
            } else {
                getDeviceImei();
            }
        }else{
            getDeviceImei();
        }


        databaseReferenceUser= FirebaseDatabase.getInstance().getReference("Users");
        //databaseReferenceUser.keepSynced(true);

        token = FirebaseInstanceId.getInstance().getToken();

        SimpleDateFormat format_diffrent = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date now = new Date();
        date = format_diffrent.format(now);

        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            //refreshedToken = FirebaseInstanceId.getInstance().getToken();
             databaseReferenceUser.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value

                            if(dataSnapshot.hasChild(str_IMEI)){
                                String IMEI_no = dataSnapshot.child("IMEI").getValue(String.class);
                                String date_got = dataSnapshot.child("date").getValue(String.class);
                                Log.d("date got",date_got);
                                String status_got = dataSnapshot.child("status").getValue(String.class);

                                Toast.makeText(Home.this, "  Already registered. !   ", Toast.LENGTH_LONG).show();
                                SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                                try {
                                    Date date1 = myFormat.parse(date_got);
                                    Date date2 = myFormat.parse(date);
                                    long diff = date2.getTime() - date1.getTime();
                                     System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                                    long days_count = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                                    if (days_count > 10 && status_got.equalsIgnoreCase("inactive") ) {
                                        promptForUpgrade();
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                      //  Log.w("TAG", "getUser:onCancelled", databaseError.toException());
                       // new Signup_async().execute();
                    }
                });


        }else{

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






    Set<String> set = sharepref.getStringSet("arraylist", null);
        List<String> sposers=new ArrayList<String>(set);

        String[] apossor_arry = new String[sposers.size()];
        apossor_arry = sposers.toArray(apossor_arry);


        FragmentTransaction tx;
        tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frame, new MainHomeFragmentFirebase());
        tx.commit();

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setHint("Enter Coding website/technology");
        searchView.setCursorDrawable(R.drawable.custome_cursor);
        searchView.setSuggestions(apossor_arry);
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

        databaseReferenceEventData = FirebaseDatabase.getInstance().getReference("Contest").child("objects");
        databaseReferenceEventData.keepSynced(true);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        View header = navigationView.getHeaderView(0);
        tv_total_sponsers = (TextView) header.findViewById(R.id.tv_total_sponsers);
        tv_total_contests = (TextView) header.findViewById(R.id.tv_total_contests);

        tv_total_sponsers.setText("Total Contests websites : "+sharepref.getString("totalsposor","NA"));




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
                        fragment = new MainHomeFragmentFirebase();
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

                    case R.id.nav_past:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment= new CompletedContest();
                        break;

                    case R.id.nav_share:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();

                        Intent intentshare = new Intent(Intent.ACTION_SEND);
                        intentshare.setType("text/plain");
                        intentshare.putExtra(Intent.EXTRA_TEXT, "Best Competitive Programming news/reminder app for Coders.World wide coding competitions and hiring challenges.Solve the challenges and get chance to win awesome prizes and also hired by world famous MNCs(i.e AMAZON , IBM , intel , google , SAP many more...).\n\n\n" + "https://play.google.com/store/apps/details?id=com.techno.jay.codingcontests&hl=en"+"\n\n-developed by Technocrats Appware");
                        startActivity(Intent.createChooser(intentshare, "Share"));


                        break;
                    case R.id.nav_feedback:
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (Exception e) {
                           // Log.d("TAG","Message ="+e);
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.techno.jay.codingcontests&hl=en")));
                        }
                        break;

                    case R.id.nav_hiring:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        Intent result = new Intent(Home.this, ResultContestLink.class);
                        result.putExtra("query", "hiring");

                        startActivity(result);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;

                    case R.id.nav_setting:
                        Intent seting = new Intent(Home.this, Setting_app.class);
                        startActivity(seting);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;

                    case R.id.nav_aboutus:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment= new Aboutus();
                        break;

                    /*case R.id.nav_aboutus:
                        // Toast.makeText(getApplicationContext(),"Shop",Toast.LENGTH_SHORT).show();
                        fragment= new Aboutus_fragment();
                        break;

                    case R.id.spam:
                        Toast.makeText(getApplicationContext(),"Spam Selected",Toast.LENGTH_SHORT).show();
                        return true;*/
                    default:
                        Toast.makeText(getApplicationContext(), "Coming Soon...", Toast.LENGTH_SHORT).show();

                        break;

                }

                if (fragment != null) {

                    fragmentTransaction.replace(R.id.frame, fragment);
                    //getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                    fragmentTransaction.addToBackStack(null).commit();

                    getSupportActionBar().setTitle("Coding Contests");

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
                    finish();
                    return;
                } else if(getFragmentManager().getBackStackEntryCount() == 0) {
                    this.doubleBackToExitPressedOnce = true;
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MainHomeFragmentFirebase()).commit();
                    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
                }else {
                    // getFragmentManager().popBackStack();
                }


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
        if (!bp.handleActivityResult(requestCode, resultCode, data))
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getDeviceImei();
        }
    }

    private void getDeviceImei() {
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String deviceid = mTelephonyManager.getDeviceId();
        String mPhoneNumber = mTelephonyManager.getLine1Number();
//        String xxx = mTelephonyManager.getCellLocation().toString();
        //Log.d("msg/num/location", "DeviceImei " + deviceid+mPhoneNumber);

        str_cellNumber=mPhoneNumber;
        str_IMEI=deviceid;
        //str_IMEI="358223078419310";// this is for emulator code only so please comment for real time debug.
        sharepref.edit().putString(Const.IMEI_DEVICE,str_IMEI).commit();


        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                SubscriptionManager subManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                List<SubscriptionInfo> subInfoList = null;
                subInfoList = subManager.getActiveSubscriptionInfoList();
                if (subInfoList != null && subInfoList.size() > 0) {
                    switch (subInfoList.size()) {
                        case 2:
                            simTwoNumber = subInfoList.get(1).getNumber();
                        case 1:
                            simOneNumber = subInfoList.get(0).getNumber();
                            break;
                        default:
                            break;
                    }
                }
                //Log.d("SIm numbers",simOneNumber+simTwoNumber);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    String res;
    class Signup_async extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Home.this, "Loading", "Please Wait--------.", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

           // System.out.println("On do in back ground----done-------");

            String reqString = Build.MANUFACTURER
                    + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                    + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();

            HashMap<String, Object> user_details = new HashMap<>();
            user_details.put("IMEI", str_IMEI);
            user_details.put("cell_number", simOneNumber+";"+simTwoNumber);
            user_details.put("token", token);
            user_details.put("date", date);
            user_details.put("status", "inactive");
            user_details.put("device", reqString);

            //Log.d("data",str_IMEI+str_cellNumber+token+date);

            //=================================================================


            databaseReferenceUser.child(str_IMEI).setValue(user_details);


            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

           // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);
            progressDialog.dismiss();
            Intent intent = new Intent(Home.this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // call this to finish the current activity

        }
    }


    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {

        if(productId.equalsIgnoreCase(Const.Product_Plan_Unlimitedversion)){

            new Update_Status().execute();
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        if(errorCode==7){
            Toast.makeText(Home.this,"already purchased.Contact Admin.",Toast.LENGTH_LONG).show();
        }else{
            promptForUpgrade();
            Toast.makeText(Home.this,"Purchase Error.",Toast.LENGTH_LONG).show();
            //new Update_Status().execute();
        }
    }

    @Override
    public void onBillingInitialized() {
        //Log.d("Initialized" , "Initialized");
    }


    ProgressDialog upgradeDialog;
    private void promptForUpgrade() {
        AlertDialog.Builder upgradeAlert = new AlertDialog.Builder(Home.this);
        upgradeAlert.setTitle("Please Purchase App plan.");
        upgradeAlert.setCancelable(false);
        upgradeAlert.setMessage("For unlimited use purchase our basic plan.");
        upgradeAlert.setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //set progress dialog and start the in app purchase process
                // upgradeDialog = ProgressDialog.show(OwnerMenuActivity.this, "Please wait", "Upgrade transaction in process", true);
                bp.purchase(Home.this , Const.Product_Plan_Unlimitedversion);
                dialog.dismiss();
            }}).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                startActivity(new Intent(Home.this,Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
        upgradeAlert.show();
    }



    private class Update_Status extends AsyncTask<Void ,Void ,Void>{

        ProgressDialog pDialog;
        String res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(Home.this , "" ,"Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {

            databaseReferenceUser.child(str_IMEI).child("status").setValue("active");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pDialog.dismiss();

        }
    }


}

