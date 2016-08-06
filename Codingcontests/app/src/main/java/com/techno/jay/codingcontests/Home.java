package com.techno.jay.codingcontests;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import DB.DatabaseHelper;


public class Home extends AppCompatActivity {



    SharedPreferences sharepref;

    DrawerLayout drawer;
    private MenuItem activeMenuItem;
    NavigationView navigationView;
    MaterialSearchView searchView;


    TextView tv_total_sponsers, tv_total_contests;

    DatabaseHelper dbh;
    SQLiteDatabase db;





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


        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);



        Set<String> set = sharepref.getStringSet("arraylist", null);
        List<String> sposers=new ArrayList<String>(set);

        String[] apossor_arry = new String[sposers.size()];
        apossor_arry = sposers.toArray(apossor_arry);






        FragmentTransaction tx;
        tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frame, new MainHomeFragment());
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


        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        tv_total_sponsers = (TextView) header.findViewById(R.id.tv_total_sponsers);
        tv_total_contests = (TextView) header.findViewById(R.id.tv_total_contests);

        tv_total_contests.setText("Total Contests : "+getProfilesCount());

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
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MainHomeFragment()).commit();
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
        super.onActivityResult(requestCode, resultCode, data);
    }






    public long getProfilesCount() {

        dbh = new DatabaseHelper(this);
        db = dbh.getWritableDatabase();

        long cnt  = DatabaseUtils.queryNumEntries(db, DatabaseHelper.TABLE_NAME);
        db.close();
        return cnt;
    }



}

