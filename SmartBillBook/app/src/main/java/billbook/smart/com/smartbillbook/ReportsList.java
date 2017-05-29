package billbook.smart.com.smartbillbook;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import billbook.smart.com.smartbillbook.adapter.VenuesListAdapter;
import billbook.smart.com.smartbillbook.modelpojo.VenuesListModel;
import billbook.smart.com.smartbillbook.utils.ConnectionDetector;
import billbook.smart.com.smartbillbook.utils.Const;

public class ReportsList extends AppCompatActivity {

    boolean loading_data=false;
    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    int load_count = 1;


    ArrayList results;
    private RecyclerView.Adapter adapter;


    Boolean isInternetPresent = false;
    SwipeRefreshLayout mSwipeRefreshLayout;
    View footerLayout;
    String str_from="0",str_to="10";
    int int_from=0,int_to=10;

    RecyclerView mRecyclerView;
    SharedPreferences sharepref;
    String pref_str_billdate,pref_str_billnumber,pref_str_partyname ,pref_str_mobileno,pref_str_amount ,pref_str_note;
    private DatabaseReference databaseReference;
    String token,date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_list);


        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reports");

        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        databaseReference.keepSynced(true);

        token = FirebaseInstanceId.getInstance().getToken();

        SimpleDateFormat format_diffrent = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date now = new Date();
        date = format_diffrent.format(now);



        pref_str_billdate=sharepref.getString(Const.PREF_STR_BILLDATE,"");
        pref_str_billnumber=sharepref.getString(Const.PREF_STR_BILLNUMBER,"");
        pref_str_partyname=sharepref.getString(Const.PREF_STR_PARTYNAME,"");
        pref_str_mobileno=sharepref.getString(Const.PREF_STR_MOBILENO,"");
        pref_str_amount=sharepref.getString(Const.PREF_STR_AMOUNT,"");
        pref_str_note=sharepref.getString(Const.PREF_STR_NOTE,"");




        // Fetching footer layout
        footerLayout = this.findViewById(R.id.footerView);
        footerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReportsList.this,SearchFilterReports.class));
                finish();
            }
        });

        // Initialize recycler view
        mRecyclerView = (RecyclerView)this.findViewById(R.id.rv_venuelist);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(ReportsList.this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        results = new ArrayList<ReportsList>();
        adapter = new VenuesListAdapter(results, ReportsList.this);

        mRecyclerView.setAdapter(adapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                results.clear();
                adapter.notifyDataSetChanged();

                new FirebaseDataLoadCurrentWeek().execute();

                load_count=1;
                mRecyclerView.setEnabled(false);
                mRecyclerView.setNestedScrollingEnabled(false);
                Toast.makeText(ReportsList.this,"Loading...",Toast.LENGTH_LONG).show();


            }
        });


        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            new FirebaseDataLoadCurrentWeek().execute();

        } else {
            Toast.makeText(ReportsList.this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();
        }


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (mRecyclerView == null || mRecyclerView.getChildCount() == 0) ? 0 : mRecyclerView.getChildAt(0).getTop();
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

                   /* if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            textView_loadmore.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                            textView_loadmore.setVisibility(View.VISIBLE);
                        }
                    }*/

                    if ((totalItemCount - visibleItemCount) <= (pastVisiblesItems + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        int_from=int_from+10;
                        int_to=int_to+10;

                        str_from=Integer.toString(int_from);
                        str_to=Integer.toString(int_to);

                        if(loading_data!=true) {
                            new FirebaseDataLoadCurrentWeek().execute();
                        }
                    }
                }

                //=======================================================================================================
            }

            @Override
            public void onScrollStateChanged(RecyclerView mRecyclerView, int newState) {
                super.onScrollStateChanged(mRecyclerView, newState);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;

        /*    case R.id.action_custom_indicator:
                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                break;
            case R.id.action_custom_child_animation:
                mDemoSlider.setCustomAnimation(new ChildAnimationExample());
                break;
            case R.id.action_restore_default:
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                break;
            case R.id.action_github:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/daimajia/AndroidImageSlider"));
                startActivity(browserIntent);
                break;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        sharepref.edit().putString(Const.PREF_STR_BILLDATE,"").apply();
        sharepref.edit().putString(Const.PREF_STR_BILLNUMBER,"").apply();
        sharepref.edit().putString(Const.PREF_STR_PARTYNAME,"").apply();
        sharepref.edit().putString(Const.PREF_STR_MOBILENO,"").apply();
        sharepref.edit().putString(Const.PREF_STR_AMOUNT,"").apply();
        sharepref.edit().putString(Const.PREF_STR_NOTE,"").apply();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sharepref.edit().putString(Const.PREF_STR_BILLDATE,"").apply();
        sharepref.edit().putString(Const.PREF_STR_BILLNUMBER,"").apply();
        sharepref.edit().putString(Const.PREF_STR_PARTYNAME,"").apply();
        sharepref.edit().putString(Const.PREF_STR_MOBILENO,"").apply();
        sharepref.edit().putString(Const.PREF_STR_AMOUNT,"").apply();
        sharepref.edit().putString(Const.PREF_STR_NOTE,"").apply();

    }

    ProgressDialog prog_dialog;

    class FirebaseDataLoadCurrentWeek extends AsyncTask<Object, Void, String> {
        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            prog_dialog = new ProgressDialog(ReportsList.this);
            // Log.d("AsyncTask!", "Showing dialog now!"); //shown in logcat
            prog_dialog.setMessage("Retreiving Data. Please wait.");
            prog_dialog.setCancelable(false);
            prog_dialog.show();

        }

        @Override
        protected String doInBackground(Object... parametros) {
            System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");

            try {
                databaseReference = FirebaseDatabase.getInstance().getReference("users").child(sharepref.getString(Const.PREF_MOBILE_NO,"")).child("Bills");
                databaseReference.keepSynced(true);
                databaseReference.orderByChild("objects").limitToFirst(1000).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                            //Log.e("objects =====" ,""+dataSnapshot.toString());

                            String totalbills = Long.toString(dataSnapshot.getChildrenCount());

                            String billnumber = singleSnapshot.child("billnumber").getValue().toString();
                            String partyname = singleSnapshot.child("partyname").getValue().toString();
                            String email = singleSnapshot.child("email").getValue().toString();
                            String mobileno = singleSnapshot.child("mobileno").getValue().toString();
                            String amount = singleSnapshot.child("amount").getValue().toString();
                            String note = singleSnapshot.child("note").getValue().toString();
                            String paid = singleSnapshot.child("paid").getValue().toString();
                            String timestamp = singleSnapshot.child("timestamp").getValue().toString();
                            // String REMINDER_STATUS = singleSnapshot.child(Integer.toString(i)).child("").getValue().toString();


                            VenuesListModel obj12 = new VenuesListModel(billnumber,partyname,email ,mobileno,amount ,note,paid ,timestamp);
                            //Log.i("DURATION",DURATION);
                            results.add(obj12);

                        }
                        adapter.notifyDataSetChanged();
                        prog_dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        prog_dialog.dismiss();
                    }
                });
            }catch (Exception e) {
                e.printStackTrace();
                prog_dialog.dismiss();
            }
            return null;
        }



        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            //p_bar.setVisibility(View.GONE);
            //prog_dialog.dismiss();
        }
    }

}
