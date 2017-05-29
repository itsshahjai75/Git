package com.techno.jay.codingcontests;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techno.jay.codingcontests.Firebase.DataObject_postFirebase;
import com.techno.jay.codingcontests.UtilitiClasses.Const;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import DB.DatabaseHelper;

import static com.techno.jay.codingcontests.Home.tv_total_contests;

public class Show_Set_Reminders extends AppCompatActivity {




    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;


    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private boolean loading = true;
    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;


    ArrayList results;

    //TextView textView_loadmore;
    Boolean isInternetPresent = false;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__set__reminders);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==this = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        results = new ArrayList<DataObject_post>();
        mAdapter = new MyRecyclerAdapter_contestview(results);


        // Initialize recycler view
        mRecyclerView = (RecyclerView)this.findViewById(R.id.post_recycler_view);



        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        SharedPreferences sharepref;
        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(sharepref.getString(Const.IMEI_DEVICE,"111"))
                .child("Reminders");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mRecyclerView.setAdapter(mAdapter);

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                    //Log.e("objects =====" ,""+dataSnapshot.toString());
                    tv_total_contests.setText("Total Reminders : "+dataSnapshot.getChildrenCount());

                    String DURATION = singleSnapshot.child("DURATION").getValue().toString();
                    String END = singleSnapshot.child("END").getValue().toString();
                    String START = singleSnapshot.child("START").getValue().toString();
                    String EVENT = singleSnapshot.child("EVENT").getValue().toString();
                    String HREF = singleSnapshot.child("HREF").getValue().toString();
                    long CONTEST_ID = Long.parseLong(singleSnapshot.child("CONTEST_ID").getValue().toString());
                    long RESOURCE_ID =Long.parseLong(singleSnapshot.child("RESOURCE_ID").getValue().toString());
                    String RESOURCE_NAME =singleSnapshot.child("RESOURCE_NAME").getValue().toString();
                    String REMINDER_STATUS = singleSnapshot.child("REMINDER_STATUS").getValue().toString();

                    DataObject_postFirebase obj12 = new DataObject_postFirebase(DURATION, END, START, EVENT, HREF, CONTEST_ID, RESOURCE_ID, RESOURCE_NAME,REMINDER_STATUS);
                    //Log.i("DURATION",DURATION);
                    results.add(obj12);

                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







        final CoordinatorLayout parent = (CoordinatorLayout)this.findViewById(R.id.cordinatelayout);








        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();

//=========================================================================================================
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    //onHide();


                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    //onShow();
                    controlsVisible = true;
                    scrolledDistance = 0;

                   /* if(textView_loadmore.getVisibility()==View.VISIBLE){
                        textView_loadmore.setVisibility(View.GONE);
                    }*/

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

                          /*  textView_loadmore.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                            textView_loadmore.setVisibility(View.VISIBLE);*/

                        }
                    }


                    if (!loading && (totalItemCount - visibleItemCount) <= (pastVisiblesItems + visibleThreshold)) {
                        // End has been reached

                       /* textView_loadmore.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                        textView_loadmore.setVisibility(View.VISIBLE);*/

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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //startActivity(new Intent(Show_Set_Reminders.this,Home.class));

        finish();
    }
}
