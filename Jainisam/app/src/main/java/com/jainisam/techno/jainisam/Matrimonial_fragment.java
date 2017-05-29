package com.jainisam.techno.jainisam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

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


public class Matrimonial_fragment extends Fragment {


    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    SharedPreferences sharepref;
    Toolbar toolbar;
    String pemail,res,res2,selected_gender;
    View footerLayout;

    CustomRecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    Boolean isInternetPresent = false;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean loading = true;
    private int visibleThreshold = 5;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    int load_count=1;
    ArrayList results;


    TextView textView_loadmore,tv_male,tv_female;



    public Matrimonial_fragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.matrimonial_fragment, container, false);

        tv_male=(TextView)view.findViewById(R.id.tv_man_matrimonial);
        tv_female=(TextView)view.findViewById(R.id.tv_femalematrimonial);


        tv_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_female.setBackgroundResource(R.drawable.rounded_corner_green);
                tv_male.setBackgroundResource(R.drawable.rounded_corner_oval_white);

                selected_gender = "female";
                new  GetAll2().execute();
            }
        });

        tv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_male.setBackgroundResource(R.drawable.rounded_corner_green);
                tv_female.setBackgroundResource(R.drawable.rounded_corner_oval_white);

                selected_gender = "male";
                new GetAll2().execute();

            }
        });



        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setEnabled(false);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                results.clear();
                mAdapter.notifyDataSetChanged();
                new GetAll().execute();
                load_count = 1;
                mRecyclerView.setEnabled(false);
                mRecyclerView.setNestedScrollingEnabled(false);
                Toast.makeText(getContext(), "Loading...", Toast.LENGTH_LONG).show();


            }
        });

        // Fetching footer layout
        footerLayout = view.findViewById(R.id.footerView);
        // Fetching footer layout
        footerLayout = view.findViewById(R.id.footerView);

// Fetching the textview declared in footer.xml

        FloatingActionButton addmatrilmonial = (FloatingActionButton) footerLayout.findViewById(R.id.fab_addplace);

        addmatrilmonial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "technocratsappware@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Matrimonial details from Jainism Application.");
                intent.putExtra(Intent.EXTRA_TEXT, "Please Fill your details.\nName:\nContact.No:\nLooking for(Male/Female)?\n\n" +
                        "Your Bio data:" +
                        "\n\n" +
                        "required person Bio data: \n\n\nThank you");
                intent.putExtra(Intent.EXTRA_BCC, "shah.jai75@gmail.com");
                startActivity(intent);

            }
        });


        // Initialize recycler view
        mRecyclerView = (CustomRecyclerView)view.findViewById(R.id.place_recycler_view);
        mRecyclerView.setHasFixedSize(true);


        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);






        final LinearLayout parent = (LinearLayout)view.findViewById(R.id.home_relative_main);
        textView_loadmore = new TextView(getContext());



        textView_loadmore.setText("Load More      ");
        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, textView_loadmore.getId());
        textView_loadmore.setLayoutParams(params);
        textView_loadmore.setGravity(Gravity.CENTER);
        textView_loadmore.setBackgroundColor(Color.parseColor("#993F51B5"));
        textView_loadmore.setTextSize(35);
        //textView.setTypeface(cFont);
        textView_loadmore.setTextColor(Color.parseColor("#ffffff"));

        parent.addView(textView_loadmore);
        textView_loadmore.setVisibility(View.GONE);

        textView_loadmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Log.v("...", "Last Item Wow !");
                load_count = load_count + 1;
                if(mSwipeRefreshLayout.isRefreshing()){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                textView_loadmore.setVisibility(View.GONE);

                //new GetPlace_increment().execute();


                loading = false;


            }
        });



        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
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

                            //textView_loadmore.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                            //textView_loadmore.setVisibility(View.VISIBLE);



                        }
                    }


                    if (!loading && (totalItemCount - visibleItemCount) <= (pastVisiblesItems + visibleThreshold)) {
                        // End has been reached

                       /* // Do something
                        current_page++;

                        onLoadMore(current_page);*/

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








        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests

            results = new ArrayList<DataObject_post>();
            mAdapter = new MyRecyclerAdapter_place(results, getContext());

            tv_male.setBackgroundResource(R.drawable.rounded_corner_green);
            selected_gender="male";

            new GetAll().execute();



        } else {
            // Internet connection is not present
            // Ask user to connect to Internet

            mRecyclerView.setVisibility(View.GONE);
            footerLayout.setVisibility(View.GONE);

        }



    }




    class GetAll extends AsyncTask<Object, Void, String> {

               protected ProgressDialog progressDialog;


        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");



            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(getContext(), "Loading", "Please Wait....", true, false);
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

                HttpPost post = new HttpPost("http://technocratsappware.com/androidapps/matrimonial_def.php?lookingfor="+selected_gender);
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    System.out.println("response got from server----- " + resp);


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
                progressDialog.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                // Log.i("RESPONSE", res);

                response_string = obj.getString("info");


                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() == 0) {

                    Toast.makeText(getContext(),"Sorry!!!No data found!",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                } else {



                    mRecyclerView.setAdapter(mAdapter);







                    for (int j = 0; j < array_res.length(); j++) {

                        String reg_id = array_res.getJSONObject(j).getString("reg_id");
                        String own_gen = array_res.getJSONObject(j).getString("own_gender");
                        String looking_gen = array_res.getJSONObject(j).getString("looking_gender");
                        String details = array_res.getJSONObject(j).getString("details");
                        String posted_date = array_res.getJSONObject(j).getString("posted_date");
                        String email_id = array_res.getJSONObject(j).getString("email_id");
                        String contact_no = array_res.getJSONObject(j).getString("contact_no");



                        DataObject_post obj1 = new DataObject_post("Looking for : " + looking_gen
                                , details+"\ne-mail : "+email_id+"\nCont.No : "+contact_no,"null","\nposted on : "+posted_date);
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
            progressDialog.dismiss();
            mRecyclerView.setVisibility(View.VISIBLE);
            footerLayout.setVisibility(View.VISIBLE);

            if(mSwipeRefreshLayout.isRefreshing()==true){
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setEnabled(false);}else{
                mRecyclerView.setNestedScrollingEnabled(true);
                mRecyclerView.setEnabled(true);
            }



        }
    }

    class GetAll2 extends AsyncTask<Object, Void, String> {

        protected ProgressDialog progressDialog;


        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");



            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(getContext(), "Loading", "Please Wait....", true, false);
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

                HttpPost post = new HttpPost("http://technocratsappware.com/androidapps/matrimonial_def.php?lookingfor="+selected_gender);
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    System.out.println("response got from server----- " + resp);


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
                progressDialog.dismiss();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                // Log.i("RESPONSE", res);

                response_string = obj.getString("info");


                JSONArray array_res = new JSONArray(response_string);

                if (array_res.length() == 0) {

                    Toast.makeText(getContext(),"Sorry!!!No data found!",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                } else {



                    mRecyclerView.setAdapter(mAdapter);







                    results.clear();
                    mAdapter.notifyDataSetChanged();

                    for (int j = 0; j < array_res.length(); j++) {

                        String reg_id = array_res.getJSONObject(j).getString("reg_id");
                        String own_gen = array_res.getJSONObject(j).getString("own_gender");
                        String looking_gen = array_res.getJSONObject(j).getString("looking_gender");
                        String details = array_res.getJSONObject(j).getString("details");
                        String posted_date = array_res.getJSONObject(j).getString("posted_date");
                        String email_id = array_res.getJSONObject(j).getString("email_id");
                        String contact_no = array_res.getJSONObject(j).getString("contact_no");



                        DataObject_post obj1 = new DataObject_post("Looking for : " + looking_gen
                                , details+"\ne-mail : "+email_id+"\nCont.No : "+contact_no,"null","\nposted on : "+posted_date);
                        results.add(obj1);
                        mAdapter.notifyDataSetChanged();




                        // Log.d("usr Img",j+"=="+ Rpost_image);
                    }



                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // progressDialog.dismiss();
            progressDialog.dismiss();
            mRecyclerView.setVisibility(View.VISIBLE);
            footerLayout.setVisibility(View.VISIBLE);

            if(mSwipeRefreshLayout.isRefreshing()==true){
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setEnabled(false);}else{
                mRecyclerView.setNestedScrollingEnabled(true);
                mRecyclerView.setEnabled(true);
            }



        }
    }

}



