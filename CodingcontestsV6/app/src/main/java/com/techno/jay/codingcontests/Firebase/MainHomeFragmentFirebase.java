package com.techno.jay.codingcontests.Firebase;


import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techno.jay.codingcontests.FontChangeCrawler;
import com.techno.jay.codingcontests.MyRecyclerAdapter_contestview;
import com.techno.jay.codingcontests.R;
import com.techno.jay.codingcontests.ViewPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimerTask;

import static com.techno.jay.codingcontests.Home.tv_total_contests;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainHomeFragmentFirebase.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MainHomeFragmentFirebase extends Fragment {

    private OnFragmentInteractionListener mListener;
    //ProgressBar p_bar;

    TextView btn_pastweek,btn_currentweek,btn_nextweek;


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



    View footerLayout;
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;


    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private boolean loading = true;
    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;


    ArrayList results;

    String lastdate="";

    private DatabaseReference databaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "fonts/ProductSans-Regular.ttf");
        // fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //==fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_main_home, container, false);
        //p_bar=(ProgressBar)convertView.findViewById(R.id.p_bar);


        pager_indicator = (LinearLayout) convertView.findViewById(R.id.viewPagerCountDots);

        intro_images = (ViewPager) convertView.findViewById(R.id.pager_introduction);

     // Initialize recycler view
        mRecyclerView = (RecyclerView)convertView.findViewById(R.id.post_recycler_view);
        results = new ArrayList<DataObject_postFirebase>();
        mAdapter = new MyRecyclerAdapter_contestview(results);
        mRecyclerView.setAdapter(mAdapter);


        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        collapsingToolbarLayout = (CollapsingToolbarLayout)convertView.findViewById(R.id.collapsing_toolbar);

        collapsingToolbarLayout.setTitle("");

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

        footerLayout = convertView.findViewById(R.id.footerView);

        // Fetching the textview declared in footer.xml
        btn_pastweek = (TextView) footerLayout.findViewById(R.id.btn_pastweek);
        btn_currentweek = (TextView) footerLayout.findViewById(R.id.btn_currentweek);
        btn_nextweek = (TextView) footerLayout.findViewById(R.id.btn_nextweek);

        new FirebaseDataLoadCurrentWeek().execute();

        btn_pastweek.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new FirebaseDataLoadPreviousWeek().execute();
            }});

        btn_currentweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseDataLoadCurrentWeek().execute();
            }
        });

        btn_nextweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseDataLoadNextWeek().execute();
            }
        });

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

    ProgressDialog prog_dialog;

    class FirebaseDataLoadCurrentWeek extends AsyncTask<Object, Void, String> {
        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            prog_dialog = new ProgressDialog(getActivity());
            Log.d("AsyncTask!", "Showing dialog now!"); //shown in logcat
            prog_dialog.setMessage("Retreiving Data. Please wait.");
            prog_dialog.setCancelable(false);
            prog_dialog.show();

            btn_pastweek.setEnabled(false);
            btn_currentweek.setEnabled(false);
            btn_nextweek.setEnabled(false);


            results.clear();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(Object... parametros) {
             System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");

            try {
                databaseReference = FirebaseDatabase.getInstance().getReference("Contest").child("objects");
                databaseReference.keepSynced(true);
                databaseReference.orderByChild("objects").limitToFirst(1000).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mRecyclerView.setAdapter(mAdapter);

                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                            //Log.e("objects =====" ,""+dataSnapshot.toString());
                            tv_total_contests.setText("Total Contests : "+dataSnapshot.getChildrenCount());

                            String DURATION = singleSnapshot.child("duration").getValue().toString();
                            String END = singleSnapshot.child("end").getValue().toString();
                            String START = singleSnapshot.child("start").getValue().toString();
                            String EVENT = singleSnapshot.child("event").getValue().toString();
                            String HREF = singleSnapshot.child("href").getValue().toString();
                            long CONTEST_ID = Long.parseLong(singleSnapshot.child("id").getValue().toString());
                            long RESOURCE_ID = Long.parseLong(singleSnapshot.child("resource").child("id").getValue().toString());
                            String RESOURCE_NAME = singleSnapshot.child("resource").child("name").getValue().toString();
                            // String REMINDER_STATUS = singleSnapshot.child(Integer.toString(i)).child("").getValue().toString();


                            try {

                                for (int i = 0; i < 7; i++) {
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    //Log.i("dateTag", sdf.format(cal.getTime()));
                                    cal.add(Calendar.DAY_OF_WEEK, i);
                                    String day = sdf.format(cal.getTime());

                                    if(START.contains(day)){

                                        DataObject_postFirebase obj12 = new DataObject_postFirebase(DURATION, END, START, EVENT, HREF, CONTEST_ID, RESOURCE_ID, RESOURCE_NAME,"0");
                                        //Log.i("DURATION",DURATION);
                                        results.add(obj12);

                                    }
                                    if(i==6){
                                        lastdate=day;
                                        //Log.i("day",lastdate);
                                    }
                                }
                            }catch (Exception ecxe){
                                ecxe.printStackTrace();
                            }

                        }
                        mAdapter.notifyDataSetChanged();
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


            btn_pastweek.setEnabled(true);
            btn_currentweek.setEnabled(true);
            btn_nextweek.setEnabled(true);


        }
    }

    class FirebaseDataLoadPreviousWeek extends AsyncTask<Object, Void, String> {
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            prog_dialog = new ProgressDialog(getActivity());
            //Log.d("AsyncTask!", "Showing dialog now!"); //shown in logcat
            prog_dialog.setMessage("Retreiving Data. Please wait.");
            prog_dialog.setCancelable(false);
            prog_dialog.show();

            btn_pastweek.setEnabled(false);
            btn_currentweek.setEnabled(false);
            btn_nextweek.setEnabled(false);

            results.clear();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(Object... parametros) {
             System.out.println("On do in back ground----previousweek-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                //Log.e("date",lastdate.toString());
                String year_lastedat=lastdate.substring(0,lastdate.indexOf("-"));
                String month_lastdate=lastdate.substring(lastdate.indexOf("-")+1,lastdate.indexOf("-")+3);
                final String date_lastdate=lastdate.substring(lastdate.length()-2);

                final int yearoflastedate = Integer.parseInt(year_lastedat);
                final int monthoflastedate = Integer.parseInt(month_lastdate)-1;
                final int dateoflastedate = Integer.parseInt(date_lastdate);


                databaseReference = FirebaseDatabase.getInstance().getReference("Contest").child("objects");
                databaseReference.keepSynced(true);
                databaseReference.orderByChild("objects").limitToFirst(1000).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                            //Log.e("objects =====" ,""+dataSnapshot.toString());

                            String DURATION = singleSnapshot.child("duration").getValue().toString();
                            String END = singleSnapshot.child("end").getValue().toString();
                            String START = singleSnapshot.child("start").getValue().toString();
                            String EVENT = singleSnapshot.child("event").getValue().toString();
                            String HREF = singleSnapshot.child("href").getValue().toString();
                            long CONTEST_ID = Long.parseLong(singleSnapshot.child("id").getValue().toString());
                            long RESOURCE_ID = Long.parseLong(singleSnapshot.child("resource").child("id").getValue().toString());
                            String RESOURCE_NAME = singleSnapshot.child("resource").child("name").getValue().toString();
                            // String REMINDER_STATUS = singleSnapshot.child(Integer.toString(i)).child("").getValue().toString();


                            try {



                                for (int i = 7; i > 0; i--) {
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(yearoflastedate, monthoflastedate, dateoflastedate);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String day = sdf.format(cal.getTime());

                                    if(sdf.format(cal.getTime()).equalsIgnoreCase(sdf.format(Calendar.getInstance().getTime()))
                                            || cal.getTime().after(Calendar.getInstance().getTime())){

                                        cal.set(yearoflastedate, monthoflastedate, dateoflastedate+1);
                                        cal.add(Calendar.DAY_OF_YEAR, -i);
                                        cal.add(Calendar.DAY_OF_YEAR, -7);
                                        // Log.i("same week so new date-"+i, sdf.format(cal.getTime()));
                                        day = sdf.format(cal.getTime());

                                        if(START.contains(day)){

                                            DataObject_postFirebase obj12 = new DataObject_postFirebase(DURATION, END, START, EVENT, HREF, CONTEST_ID, RESOURCE_ID, RESOURCE_NAME,"0");
                                            // Log.i("DURATION",DURATION);
                                            results.add(obj12);

                                        }
                                    }else if(!cal.getTime().after(Calendar.getInstance().getTime())){

                                        cal.set(yearoflastedate, monthoflastedate, dateoflastedate);
                                        cal.add(Calendar.DAY_OF_YEAR, -i);
                                        //Log.i("other week -"+i, sdf.format(cal.getTime()));
                                        day = sdf.format(cal.getTime());
                                        if(START.contains(day)){

                                            DataObject_postFirebase obj12 = new DataObject_postFirebase(DURATION, END, START, EVENT, HREF, CONTEST_ID, RESOURCE_ID, RESOURCE_NAME,"0");
                                            // Log.i("DURATION",DURATION);
                                            results.add(obj12);

                                        }

                                    }

                                    if(i==7){
                                        lastdate=day;
                                        //Log.i("first"+i,lastdate);
                                    }

                                }
                            }catch (Exception ecxe){
                                ecxe.printStackTrace();
                            }

                        }

                        mAdapter.notifyDataSetChanged();
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

            btn_pastweek.setEnabled(true);
            btn_currentweek.setEnabled(true);
            btn_nextweek.setEnabled(true);
        }
    }

    class FirebaseDataLoadNextWeek extends AsyncTask<Object, Void, String> {
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            prog_dialog = new ProgressDialog(getActivity());
            //Log.d("AsyncTask!", "Showing dialog now!"); //shown in logcat
            prog_dialog.setMessage("Retreiving Data. Please wait.");
            prog_dialog.setCancelable(false);
            prog_dialog.show();

            btn_pastweek.setEnabled(false);
            btn_currentweek.setEnabled(false);
            btn_nextweek.setEnabled(false);

            results.clear();
            mAdapter.notifyDataSetChanged();

        }

        @Override
        protected String doInBackground(Object... parametros) {
            // System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");


            try {
                String year_lastedat=lastdate.substring(0,lastdate.indexOf("-"));
                String month_lastdate=lastdate.substring(lastdate.indexOf("-")+1,lastdate.indexOf("-")+3);
                String date_lastdate=lastdate.substring(lastdate.length()-2);

                final int yearoflastedate = Integer.parseInt(year_lastedat);
                final int monthoflastedate = Integer.parseInt(month_lastdate)-1;
                final int dateoflastedate = Integer.parseInt(date_lastdate);


                databaseReference = FirebaseDatabase.getInstance().getReference("Contest").child("objects");
                databaseReference.keepSynced(true);
                databaseReference.orderByChild("objects").limitToFirst(1000).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mAdapter.notifyDataSetChanged();
                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            //Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                            //Log.e("objects =====" ,""+dataSnapshot.toString());

                            String DURATION = singleSnapshot.child("duration").getValue().toString();
                            String END = singleSnapshot.child("end").getValue().toString();
                            String START = singleSnapshot.child("start").getValue().toString();
                            String EVENT = singleSnapshot.child("event").getValue().toString();
                            String HREF = singleSnapshot.child("href").getValue().toString();
                            long CONTEST_ID = Long.parseLong(singleSnapshot.child("id").getValue().toString());
                            long RESOURCE_ID = Long.parseLong(singleSnapshot.child("resource").child("id").getValue().toString());
                            String RESOURCE_NAME = singleSnapshot.child("resource").child("name").getValue().toString();
                            // String REMINDER_STATUS = singleSnapshot.child(Integer.toString(i)).child("").getValue().toString();


                            try {
                                for (int i = 0; i < 7; i++) {
                                    Calendar cal = Calendar.getInstance();
                                    cal.set(yearoflastedate, monthoflastedate, dateoflastedate+1);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    //Log.i("dateTag", sdf.format(cal.getTime()));
                                    cal.add(Calendar.DAY_OF_WEEK, i);
                                    String day = sdf.format(cal.getTime());

                                    if(START.contains(day)){

                                        DataObject_postFirebase obj12 = new DataObject_postFirebase(DURATION, END, START, EVENT, HREF, CONTEST_ID, RESOURCE_ID, RESOURCE_NAME,"0");
                                        //Log.i("DURATION",DURATION);
                                        results.add(obj12);

                                    }
                                    if(i==6){
                                        lastdate=day;
                                        //Log.i("day",lastdate);
                                    }
                                }
                            }catch (Exception ecxe){
                                ecxe.printStackTrace();
                            }

                        }
                        mAdapter.notifyDataSetChanged();
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

            btn_pastweek.setEnabled(true);
            btn_currentweek.setEnabled(true);
            btn_nextweek.setEnabled(true);


        }
    }

}
