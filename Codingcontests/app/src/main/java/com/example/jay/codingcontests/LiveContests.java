package com.example.jay.codingcontests;


import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import DB.DatabaseHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class LiveContests extends Fragment {


    private OnFragmentInteractionListener mListener;

    Cursor mCursor;

    Date date_Start,date_End,date_Now;
    DatabaseHelper dbh;
    SQLiteDatabase db;

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


    ProgressDialog progressDialog4;

    public LiveContests() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_live_contests, container, false);




        // Initialize recycler view
        mRecyclerView = (RecyclerView)convertView.findViewById(R.id.post_recycler_view);




        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);



        results = new ArrayList<DataObject_post>();
        mAdapter = new MyRecyclerAdapter_Post(results);




        final CoordinatorLayout parent = (CoordinatorLayout)convertView.findViewById(R.id.cordinatelayout);











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







        mRecyclerView.setAdapter(mAdapter);

        try {

           /* SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < 8; i++) {
                Calendar calendar = new GregorianCalendar();
                calendar.add(Calendar.DATE, i);
                String day = sdf.format(calendar.getTime());*/

                dbh = new DatabaseHelper(getContext());
                db = dbh.getWritableDatabase();
                // Select All Query

                String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME ;//+ " WHERE " + DatabaseHelper.START + " LIKE '%" + day + "%' ORDER BY cast(" + DatabaseHelper.START + " as REAL) DESC;";
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
                                    /*Log.i("START day", DURATION+
                                            END+
                                            START+
                                            EVENT+
                                            HREF+
                                            CONTEST_ID+
                                            RESOURCE_ID+
                                            RESOURCE_NAME);*/



                        String dtStart = START;
                        String dtEnd = END;
                        SimpleDateFormat format_diffrent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    try{
                        Date now = new Date();
                        String strDate = format_diffrent.format(now);

                        date_Start = format_diffrent.parse(dtStart);
                        date_End = format_diffrent.parse(dtEnd);
                        date_Now= format_diffrent.parse(strDate);

                        System.out.println(date_Start);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    if(between(date_Now,date_Start,date_End)==true){
                        DataObject_post obj12 = new DataObject_post(DURATION, END, START, EVENT, HREF, CONTEST_ID, RESOURCE_ID, RESOURCE_NAME);
                        // Log.d("object",EVENT);
                        results.add(obj12);

                    }else if(date_End.before(date_Now)){

                    }else{

                    }


                    } while (mCursor.moveToNext());
                }


            //}
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


    public static boolean between(Date date, Date dateStart, Date dateEnd) {
        if (date != null && dateStart != null && dateEnd != null) {
            if (date.after(dateStart) && date.before(dateEnd)) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }


}