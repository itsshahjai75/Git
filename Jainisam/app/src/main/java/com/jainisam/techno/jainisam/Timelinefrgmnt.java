package com.jainisam.techno.jainisam;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class Timelinefrgmnt extends Fragment {


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



    String res,res2;
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

    int load_count=1;
    ArrayList results;

    TextView textView_loadmore;
    Boolean isInternetPresent = false;



    public Timelinefrgmnt() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //===3) for adepterview and handlerview na use mate====
        View convertView = inflater.inflate(R.layout.fragment_timeline, container, false);


        pager_indicator = (LinearLayout) convertView.findViewById(R.id.viewPagerCountDots);


        intro_images = (ViewPager) convertView.findViewById(R.id.pager_introduction);


       /* btnNext = (ImageButton) convertView.findViewById(R.id.btn_next);
        btnFinish = (ImageButton) convertView.findViewById(R.id.btn_finish);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intro_images.setCurrentItem((intro_images.getCurrentItem() < dotsCount)
                        ? intro_images.getCurrentItem() + 1 : 0);

            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish();
                intro_images.setCurrentItem((intro_images.getCurrentItem() < dotsCount)
                        ? 0:0);

            }
        });*/




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
        final MyTextView post = (MyTextView) footerLayout.findViewById(R.id.btn_post);
        MyTextView img_shre = (MyTextView) footerLayout.findViewById(R.id.btn_img);
        MyTextView checkin = (MyTextView) footerLayout.findViewById(R.id.btn_checkin);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postshare = new Intent(getContext(),Post_Share.class);
                postshare.putExtra("postshare","post");
                getContext().startActivity(postshare);

            }
        });


        img_shre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent postshare = new Intent(getContext(),Post_Share.class);
                postshare.putExtra("postshare","image");
                getContext().startActivity(postshare);

            }
        });


        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent postshare = new Intent(getContext(),Post_Share.class);
                postshare.putExtra("postshare","checkedin");
                getContext().startActivity(postshare);
            }
        });





        // Initialize recycler view
        mRecyclerView = (RecyclerView)convertView.findViewById(R.id.post_recycler_view);


        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests

            results = new ArrayList<DataObject_post>();
            mAdapter = new MyRecyclerAdapter_Post(results);

            new GetPostAll().execute();



        } else {
            // Internet connection is not present
            // Ask user to connect to Internet

            mRecyclerView.setVisibility(View.GONE);
            footerLayout.setVisibility(View.GONE);

        }




        final CoordinatorLayout parent = (CoordinatorLayout)convertView.findViewById(R.id.cordinatelayout);
        textView_loadmore = (TextView)convertView.findViewById(R.id.tv_loadmore);



        textView_loadmore.setVisibility(View.GONE);

        textView_loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Log.v("...", "Last Item Wow !");
                load_count = load_count + 1;

                textView_loadmore.setVisibility(View.GONE);

                new GetPost_increment().execute();


                loading = false;


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

                    if(textView_loadmore.getVisibility()==View.VISIBLE){
                        textView_loadmore.setVisibility(View.GONE);
                    }

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

                            textView_loadmore.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                            textView_loadmore.setVisibility(View.VISIBLE);



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




    class GetPostAll extends AsyncTask<Object, Void, String> {

        /*
               protected ProgressDialog progressDialog;*/
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            pg_bar.setVisibility(View.VISIBLE);

            //inicia diálogo de progress, mostranto processamento com servidor.
            // progressDialog = ProgressDialog.show(getContext(), "Loading", "Please Wait--------.", true, false);
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

                HttpPost post = new HttpPost("http://technocratsappware.com/androidapps/postall.php");
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    //  System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }

            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {



                Toast.makeText(getContext(), "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                pg_bar.setVisibility(View.GONE);
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                // Log.i("RESPONSE", res);

                response_string = obj.getString("info");


                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() == 0) {

                    Toast.makeText(getContext(),"No data found!",Toast.LENGTH_LONG).show();

                } else {



                    mRecyclerView.setAdapter(mAdapter);





                    for (int j = 0; j < array_res.length(); j++) {

                        String Rpost_id = array_res.getJSONObject(j).getString("post_id");
                        String Rposted_by_name = array_res.getJSONObject(j).getString("posted_by_name");
                        String Rpost_description = array_res.getJSONObject(j).getString("post_description");
                        String Rpost_image = array_res.getJSONObject(j).getString("post_img");
                        String Rpost_time = array_res.getJSONObject(j).getString("post_time");
                        String Rposted_by_email = array_res.getJSONObject(j).getString("posted_by_email");

                        //System.out.println("image name"+Rpost_image);

                        String xxEmail = Rposted_by_email.substring(0, Rposted_by_email.indexOf("\u0040"));
                        if (xxEmail.length() > 3) {

                            xxEmail = Rposted_by_email.substring(Rposted_by_email.indexOf("\u0040") - 3);
                        } else {
                            xxEmail = Rposted_by_email.substring(Rposted_by_email.indexOf("\u0040") - 1);
                        }


                        DateFormat df_post = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                        df_post.setTimeZone(TimeZone.getTimeZone("MST"));

                        Date date_post = df_post.parse(Rpost_time);
                        //String Rpost_time_new=df_post.format(date_post.getTime());
                        //Log.d("without adding zone",new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(date_post.getTime()));


                        // Rpost_time=new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(date_post.getTime() +TimeZone.getDefault().getOffset((date_post.getTime())));

                        Rpost_time = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault()).format(date_post.getTime());


                        DataObject_post obj1 = new DataObject_post("Member : " + Rposted_by_name + " (xxxxx" + xxEmail + ")", Rpost_description, Rpost_image, Rpost_time);
                        results.add(obj1);



                        // Log.d("usr Img",j+"=="+ Rpost_image);
                    }


                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // progressDialog.dismiss();
            pg_bar.setVisibility(View.GONE);

            mRecyclerView.setVisibility(View.VISIBLE);
            footerLayout.setVisibility(View.VISIBLE);





        }
    }


    class GetPost_increment extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";
        protected ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //pg_bar.setVisibility(View.VISIBLE);

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(getContext(), "Loading", "Please Wait--------", true, false);
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

                HttpPost post = new HttpPost("http://technocratsappware.com/androidapps/postall_increment.php?loadcount="+load_count);
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                //params.add(new BasicNameValuePair("loadcount",Integer.toString(load_count)));


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp2 = EntityUtils.toString(resEntity);
                    res2 = resp2;

                    //  System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }



//            progressDialog.dismiss();
            return res2;

        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res2 == null || res2.equals("")) {



                Toast.makeText(getContext(), "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                //
                progressDialog.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res2);


//                 Log.i("RESPONSE", res2);

                response_string = obj.getString("info");


                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() == 0) {

                    //                  Toast.makeText(getContext(),"No data found!",Toast.LENGTH_LONG).show();

                } else {


                    for (int j = 0; j < array_res.length(); j++) {

                        String Rpost_id = array_res.getJSONObject(j).getString("post_id");
                        String Rposted_by_name = array_res.getJSONObject(j).getString("posted_by_name");
                        String Rpost_description = array_res.getJSONObject(j).getString("post_description");
                        String Rpost_image = array_res.getJSONObject(j).getString("post_img");
                        String Rpost_time = array_res.getJSONObject(j).getString("post_time");
                        String Rposted_by_email = array_res.getJSONObject(j).getString("posted_by_email");

                        String xxEmail = Rposted_by_email.substring(0, Rposted_by_email.indexOf("\u0040"));
                        if (xxEmail.length() > 3) {

                            xxEmail = Rposted_by_email.substring(Rposted_by_email.indexOf("\u0040") - 3);
                        } else {
                            xxEmail = Rposted_by_email.substring(Rposted_by_email.indexOf("\u0040") - 1);
                        }


                        DateFormat df_post = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                        df_post.setTimeZone(TimeZone.getTimeZone("MST"));

                        Date date_post = df_post.parse(Rpost_time);
                        //String Rpost_time_new=df_post.format(date_post.getTime());
                        //Log.d("without adding zone",new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(date_post.getTime()));


                        // Rpost_time=new SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(date_post.getTime() +TimeZone.getDefault().getOffset((date_post.getTime())));

                        Rpost_time = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault()).format(date_post.getTime());


                        DataObject_post obj12 = new DataObject_post("Member : " + Rposted_by_name + " (xxxxx" + xxEmail + ")", Rpost_description, Rpost_image, Rpost_time);

                        results.add(obj12);

                        mAdapter.notifyDataSetChanged();
                        // Log.d("usr Img",j+"=="+ Rpost_image);

                    }



                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            progressDialog.dismiss();
            //pg_bar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            footerLayout.setVisibility(View.VISIBLE);


           /* if(mSwipeRefreshLayout.isRefreshing()==true){
                mSwipeRefreshLayout.setRefreshing(false);}*/



        }
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
        final View subRoot = inflater.inflate(R.layout.fragment_timeline, null);
        parent.addView(subRoot);
        //do all the stuff
    }

}
