package com.example.jay.codingcontests;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.SynchronousQueue;

import DB.DBManager;
import DB.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainHomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MainHomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;


    CollapsingToolbarLayout collapsingToolbarLayout;
    //Toolbar toolbar;

    // private ImageButton btnNext, btnFinish;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapterpager;

    private int[] mImageResources = {
            R.drawable.front_image1,
            R.drawable.front_image2,
            R.drawable.front_image3,
            //   R.drawable.front_image4,
    };



    String res;
    View footerLayout;
    ProgressBar pg_bar;
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


    DatabaseHelper dbh;
    SQLiteDatabase db;

    Cursor mCursor;
    String lastdate="";



    public MainHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_main_home, container, false);


        pager_indicator = (LinearLayout) convertView.findViewById(R.id.viewPagerCountDots);


        intro_images = (ViewPager) convertView.findViewById(R.id.pager_introduction);




        // Initialize recycler view
        mRecyclerView = (RecyclerView)convertView.findViewById(R.id.post_recycler_view);

        results = new ArrayList<DataObject_post>();
        mAdapter = new MyRecyclerAdapter_Post(results);

        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);





        collapsingToolbarLayout = (CollapsingToolbarLayout)convertView.findViewById(R.id.collapsing_toolbar);
        //toolbar = (Toolbar)convertView.findViewById(R.id.toolbar_cart);

        collapsingToolbarLayout.setTitle("");

        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mAdapterpager = new ViewPagerAdapter(getContext(), mImageResources);
        intro_images.setAdapter(mAdapterpager);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                }

                dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

                if (position + 1 == dotsCount) {
                   /* btnNext.setVisibility(View.GONE);
                    btnFinish.setVisibility(View.VISIBLE);*/
                } else {
                   /* btnNext.setVisibility(View.VISIBLE);
                    btnFinish.setVisibility(View.GONE);*/
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setUiPageViewController();




        Timer timer = new Timer();
        timer.schedule(new UpdateTimeTask(), 2000, 6000);







        pg_bar=(ProgressBar)convertView.findViewById(R.id.a_progressbar);





        // Fetching footer layout
        footerLayout = convertView.findViewById(R.id.footerView);

// Fetching the textview declared in footer.xml
        final TextView btn_pastweek = (TextView) footerLayout.findViewById(R.id.btn_pastweek);
        TextView btn_currentweek = (TextView) footerLayout.findViewById(R.id.btn_currentweek);
        TextView btn_nextweek = (TextView) footerLayout.findViewById(R.id.btn_nextweek);

        btn_pastweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try {


                   /* SimpleDateFormat sdflastdat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    String dateInString = lastdate+" 00:00:01";
                    Date date = sdflastdat.parse(dateInString);*/

                    String year_lastedat=lastdate.substring(0,lastdate.indexOf("-"));
                    String month_lastdate=lastdate.substring(lastdate.indexOf("-")+1,lastdate.indexOf("-")+3);
                    String date_lastdate=lastdate.substring(lastdate.length()-2);

                    int yearoflastedate = Integer.parseInt(year_lastedat);
                    int monthoflastedate = Integer.parseInt(month_lastdate)-1;
                    int dateoflastedate = Integer.parseInt(date_lastdate);
                    Log.d("date",year_lastedat+month_lastdate+date_lastdate);


                    results.clear();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    for (int i = 8; i > 0; i--) {
                        Calendar calendar = new GregorianCalendar();

                        calendar.set(yearoflastedate, monthoflastedate, dateoflastedate-1);

                        calendar.add(Calendar.DATE, -i);
                        String day = sdf.format(calendar.getTime());



                        dbh = new DatabaseHelper(getContext());
                        db = dbh.getWritableDatabase();
                        // Select All Query

                        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.START + " LIKE '%" + day + "%' ORDER BY cast(" + DatabaseHelper.START + " as REAL) DESC;";
                        //Log.i("TAG day", selectQuery);
                         mCursor = db.rawQuery(selectQuery, null);

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
                              /*  Log.i("START day", DURATION+
                                        END+
                                        START+
                                        EVENT+
                                        HREF+
                                        CONTEST_ID+
                                        RESOURCE_ID+
                                        RESOURCE_NAME);*/

                                DataObject_post obj12 = new DataObject_post(DURATION, END, START, EVENT, HREF, CONTEST_ID, RESOURCE_ID, RESOURCE_NAME);
                                 Log.d("object",START);
                                results.add(obj12);

                            } while (mCursor.moveToNext());
                        }

                        if(i==1){
                            lastdate=day;
                            Log.i("date",lastdate);
                        }
                    }
                }catch (Exception ecxe){
                    ecxe.printStackTrace();
                }finally {
                    if (mCursor != null) {
                        mCursor.close();
                        db.close();
                    }
                }



                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(0);

            }
        });


        btn_currentweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    results.clear();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    for (int i = 0; i < 8; i++) {
                        Calendar calendar = new GregorianCalendar();
                        calendar.add(Calendar.DATE, i);
                        String day = sdf.format(calendar.getTime());

                        dbh = new DatabaseHelper(getContext());
                        db = dbh.getWritableDatabase();
                        // Select All Query

                        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.START + " LIKE '%" + day + "%' ORDER BY cast(" + DatabaseHelper.START + " as REAL) DESC;";
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
                              /*  Log.i("START day", DURATION+
                                        END+
                                        START+
                                        EVENT+
                                        HREF+
                                        CONTEST_ID+
                                        RESOURCE_ID+
                                        RESOURCE_NAME);*/

                                DataObject_post obj12 = new DataObject_post(DURATION, END, START, EVENT, HREF, CONTEST_ID, RESOURCE_ID, RESOURCE_NAME);
                                // Log.d("object",EVENT);
                                results.add(obj12);

                            } while (mCursor.moveToNext());
                        }

                        if(i==7){
                            lastdate=day;
                            Log.i("date",lastdate);
                        }
                    }
                }catch (Exception ecxe){
                    ecxe.printStackTrace();
                }finally {
                    if (mCursor != null) {
                        mCursor.close();
                        db.close();
                    }
                }



                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(0);
            }
        });


        btn_nextweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {



                    /*SimpleDateFormat sdflastdat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    String dateInString = lastdate+" 00:00:01";;
                    Date date = sdflastdat.parse(dateInString);*/


                    String year_lastedat=lastdate.substring(0,lastdate.indexOf("-"));
                    String month_lastdate=lastdate.substring(lastdate.indexOf("-")+1,lastdate.indexOf("-")+3);
                    String date_lastdate=lastdate.substring(lastdate.length()-2);

                    int yearoflastedate = Integer.parseInt(year_lastedat);
                    int monthoflastedate = Integer.parseInt(month_lastdate)-1;
                    int dateoflastedate = Integer.parseInt(date_lastdate);
                    Log.d("date",year_lastedat+"/"+month_lastdate+"/"+date_lastdate);


                    results.clear();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    for (int i = 0; i < 8; i++) {
                        Calendar calendar = new GregorianCalendar();
                        calendar.set(yearoflastedate, monthoflastedate, dateoflastedate+1);

                        calendar.add(Calendar.DATE, i);
                        String day = sdf.format(calendar.getTime());

                        dbh = new DatabaseHelper(getContext());
                        db = dbh.getWritableDatabase();
                        // Select All Query

                        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.START + " LIKE '%" + day + "%' ORDER BY cast(" + DatabaseHelper.START + " as REAL) DESC;";
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
                              /*  Log.i("START day", DURATION+
                                        END+
                                        START+
                                        EVENT+
                                        HREF+
                                        CONTEST_ID+
                                        RESOURCE_ID+
                                        RESOURCE_NAME);*/

                                DataObject_post obj12 = new DataObject_post(DURATION, END, START, EVENT, HREF, CONTEST_ID, RESOURCE_ID, RESOURCE_NAME);
                                // Log.d("object",EVENT);
                                results.add(obj12);

                            } while (mCursor.moveToNext());
                        }

                        if(i==7){
                            lastdate=day;
                            Log.i("day",lastdate);
                        }
                    }
                }catch (Exception ecxe){
                    ecxe.printStackTrace();
                }finally {
                    if (mCursor != null) {
                        mCursor.close();
                        db.close();
                    }
                }



                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(0);
            }
        });







        final CoordinatorLayout parent = (CoordinatorLayout)convertView.findViewById(R.id.cordinatelayout);








        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();

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





        mRecyclerView.setAdapter(mAdapter);

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < 8; i++) {
                Calendar calendar = new GregorianCalendar();
                calendar.add(Calendar.DATE, i);
                String day = sdf.format(calendar.getTime());

                dbh = new DatabaseHelper(getContext());
                db = dbh.getWritableDatabase();
                // Select All Query

                String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.START + " LIKE '%" + day + "%' ORDER BY cast(" + DatabaseHelper.START + " as REAL) DESC;";
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
                                    /*Log.i("START day", DURATION+
                                            END+
                                            START+
                                            EVENT+
                                            HREF+
                                            CONTEST_ID+
                                            RESOURCE_ID+
                                            RESOURCE_NAME);*/

                        DataObject_post obj12 = new DataObject_post(DURATION, END, START, EVENT, HREF, CONTEST_ID, RESOURCE_ID, RESOURCE_NAME);
                        // Log.d("object",EVENT);
                        results.add(obj12);

                    } while (mCursor.moveToNext());
                }

                if(i==7){
                    lastdate=day;
                    Log.i("day",lastdate);
                }
            }
        }catch (Exception ecxe){
            ecxe.printStackTrace();
        }finally {
            if (mCursor != null) {
                mCursor.close();
                db.close();
            }
        }



        mAdapter.notifyDataSetChanged();







        return convertView;
    }









    private void setUiPageViewController() {

        dotsCount = mAdapterpager.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }


        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }



    class UpdateTimeTask extends TimerTask {
        public void run() {
            intro_images.post(new Runnable() {
                public void run() {

                    if (intro_images.getCurrentItem() < 2) {
                        intro_images.setCurrentItem(intro_images.getCurrentItem() + 1, true);
                        String abc = String.valueOf(intro_images.getCurrentItem());
                        //Log.i("timer_+", abc);
                    } else {
                        intro_images.setCurrentItem(0, true);
                    }
                }
            });
        }
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        final View view = getView();
        if (view != null) {
            initView(getActivity().getLayoutInflater(), (ViewGroup) view.findViewById(R.id.cordinatelayout));
        }
    }

    private void initView(final LayoutInflater inflater, final ViewGroup parent) {
        parent.removeAllViews();
        final View subRoot = inflater.inflate(R.layout.fragment_main_home, null);
        parent.addView(subRoot);
        //do all the stuff
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
