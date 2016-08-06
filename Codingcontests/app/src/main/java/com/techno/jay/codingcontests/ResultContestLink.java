package com.techno.jay.codingcontests;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import DB.DBManager;
import DB.DatabaseHelper;

public class ResultContestLink extends AppCompatActivity {




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
    private DBManager dbManager;

    DatabaseHelper dbh;
    SQLiteDatabase db;

    Cursor mCursor;
    String lastdate="";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_contest_link);
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







        String queryget=getIntent().getStringExtra("query");




                try {

                    results.clear();

                        dbh = new DatabaseHelper(this);
                        db = dbh.getWritableDatabase();
                        // Select All Query

                        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.EVENT + " LIKE '%" + queryget + "%' OR "
                                + DatabaseHelper.HREF + " LIKE '%" + queryget + "%' OR "
                                + DatabaseHelper.RESOURCE_NAME + " LIKE '%" + queryget + "%' "
                                +"ORDER BY cast(" + DatabaseHelper.START + " as REAL) DESC;";
                        //Log.i("TAG day", selectQuery);
                        Cursor mCursor = db.rawQuery(selectQuery, null);

                        /*String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.DURATION, DatabaseHelper.END,DatabaseHelper.START, DatabaseHelper.EVENT
                                ,DatabaseHelper.HREF, DatabaseHelper.CONTEST_ID,DatabaseHelper.RESOURCE_ID, DatabaseHelper.RESOURCE_NAME};
                        //Cursor mCursor = db.query(DatabaseHelper.TABLE_NAME, columns,null,  null, null, null, null);*/


                        if (mCursor.moveToFirst()) {
                            do {
                                String DURATION = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DURATION));
                                String END = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.END));
                                String START = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.START));
                                String EVENT = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.EVENT));
                                String HREF = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.HREF));
                                String CONTEST_ID = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.CONTEST_ID));
                                String RESOURCE_ID = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.RESOURCE_ID));
                                String RESOURCE_NAME = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.RESOURCE_NAME));
                                String REMINDER_STATUS = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.REMINDER));
                              /*  Log.i("START day", DURATION+
                                        END+
                                        START+
                                        EVENT+
                                        HREF+
                                        CONTEST_ID+
                                        RESOURCE_ID+
                                        RESOURCE_NAME);*/

                                DataObject_post obj12 = new DataObject_post(DURATION, END, START, EVENT, HREF, CONTEST_ID, RESOURCE_ID, RESOURCE_NAME,REMINDER_STATUS);
                                // Log.d("object",EVENT);
                                results.add(obj12);

                            } while (mCursor.moveToNext());
                        }

                        if(mCursor==null || mCursor.getCount()<=0){

                            DataObject_post obj12 = new DataObject_post(null, null, null, "No Data Found Sorry!!!", null, null, null, "No Data Found Sorry.","0");
                            // Log.d("object",EVENT);
                            results.add(obj12);
                        }



                        mCursor.close();
                        db.close();

                }catch (Exception ecxe){
                    ecxe.printStackTrace();
                }finally {
                    if (mCursor != null) {
                        mCursor.close();
                        db.close();
                    }
                }








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

        startActivity(new Intent(ResultContestLink.this,Home.class));

        finish();
    }
}
