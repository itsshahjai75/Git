package com.techno.jay.codingcontests;


import android.app.ProgressDialog;
import android.content.res.Configuration;
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

import com.techno.jay.codingcontests.Firebase.DataObject_postFirebase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class LongtermCompetition extends Fragment {

    Boolean isInternetPresent = false;

    private OnFragmentInteractionListener mListener;




    String res;
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


    ProgressDialog progressDialog4;

    public LongtermCompetition() {
        // Required empty public constructor
    }


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
        View convertView = inflater.inflate(R.layout.fragment_longterm_competition, container, false);




        // Initialize recycler view
        mRecyclerView = (RecyclerView)convertView.findViewById(R.id.post_recycler_view);




        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);



        results = new ArrayList<DataObject_post>();
        mAdapter = new MyRecyclerAdapter_contestview(results);




         final CoordinatorLayout parent = (CoordinatorLayout)convertView.findViewById(R.id.cordinatelayout);



        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new Getlongcontests().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new Getlongcontests().execute();
            }

        } else {
            // Internet connection is not present
            // Ask user to connect to Internet



        }








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


    class Getlongcontests extends AsyncTask<Object, Void, String> {



        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            progressDialog4 = ProgressDialog.show(getContext(), "Loading", "Please Wait....", true, false);



        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://clist.by:80/api/v1/contest/?limit=2000&start__gt=2016-01-01&duration__gt=30%20days&order_by=-end&username=shah.jai75&api_key=434a1084d357751c87dbd1dc9a8e0885accdd30b")
                        //.post(formBody)
                        .build();
                Response response = client.newCall(request).execute();
                res = response.body().string();
                //Log.e("response" , res);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

//            progressDialog.dismiss();
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
                //

                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                 //Log.i("RESPONSE", res);

                mRecyclerView.setAdapter(mAdapter);

                String response_string_meta = obj.getString("meta");
                JSONObject obj_meta = new JSONObject(response_string_meta);
                String total_count = obj_meta.getString("total_count");




                response_string = obj.getString("objects");


                JSONArray array_res = new JSONArray(response_string);


                if (array_res.length() == 0) {

                    //Toast.makeText(getContext(),"No data found!",Toast.LENGTH_LONG).show();

                } else {


                    for (int j = 0; j < array_res.length(); j++) {

                        String duration = array_res.getJSONObject(j).getString("duration");
                        String end = array_res.getJSONObject(j).getString("end");
                        //Log.d("startDate",end);
                        String start = array_res.getJSONObject(j).getString("start");
                        // Log.d("end date",start);
                        String event = array_res.getJSONObject(j).getString("event");
                        String href = array_res.getJSONObject(j).getString("href");

                        String resource = array_res.getJSONObject(j).getString("resource");
                        JSONObject obj_resourse = new JSONObject(resource);
                        String resource_name = obj_resourse.getString("name");

                        long CONTEST_ID = Long.parseLong(array_res.getJSONObject(j).getString("id").toString());
                        long RESOURCE_ID = Long.parseLong(obj_resourse.getString("id").toString());



                        DataObject_postFirebase obj12 = new DataObject_postFirebase(duration, end, start, event, href, CONTEST_ID, RESOURCE_ID, resource_name,"0");
                        // Log.d("object",EVENT);
                        results.add(obj12);


                    }

                    mAdapter.notifyDataSetChanged();
                    progressDialog4.dismiss();

                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}