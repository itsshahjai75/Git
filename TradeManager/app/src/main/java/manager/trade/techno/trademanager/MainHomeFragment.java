package manager.trade.techno.trademanager;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.Parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainHomeFragment extends Fragment {


    TextView tv_sensex,tv_sensex_diff_per,tv_sensex_time;
    TextView tv_nifty,tv_nifty_diff_per,tv_nifty_time;
    TextView tv_nasdaq,tv_nasdaq_diff_per,tv_nasdaq_time;
    TextView tv_nikkei,tv_nikkei_diff_per,tv_nikkei_time;
    TextView tv_mcx,tv_mcx_diff_per,tv_mcx_time;

    ImageView img_sensex,img_nifty,img_nasdaq,img_nikkei,img_mcx;
    SwipeRefreshLayout mSwipeRefreshLayout;


    String res1;
    Boolean isInternetPresent = false;


    public MainHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View convertView = inflater.inflate(R.layout.fragment_main_home, container, false);


        tv_sensex =(TextView)convertView.findViewById(R.id.tv_sensext_count);
        tv_sensex_diff_per=(TextView)convertView.findViewById(R.id.tv_sensext_diff_per);
        tv_sensex_time=(TextView)convertView.findViewById(R.id.tv_sensext_time);

        tv_nifty =(TextView)convertView.findViewById(R.id.tv_nifty_count);
        tv_nifty_diff_per=(TextView)convertView.findViewById(R.id.tv_nifty_diff_per);
        tv_nifty_time=(TextView)convertView.findViewById(R.id.tv_nifty_time);

        tv_nasdaq =(TextView)convertView.findViewById(R.id.tv_nasdaq_count);
        tv_nasdaq_diff_per=(TextView)convertView.findViewById(R.id.tv_nasdaq_diff_per);
        tv_nasdaq_time=(TextView)convertView.findViewById(R.id.tv_nasdaq_time);


        tv_nikkei =(TextView)convertView.findViewById(R.id.tv_nikkei_count);
        tv_nikkei_diff_per=(TextView)convertView.findViewById(R.id.tv_nikkei_diff_per);
        tv_nikkei_time=(TextView)convertView.findViewById(R.id.tv_nikkei_time);

        tv_mcx =(TextView)convertView.findViewById(R.id.tv_mcx_count);
        tv_mcx_diff_per=(TextView)convertView.findViewById(R.id.tv_mcx_diff_per);
        tv_mcx_time=(TextView)convertView.findViewById(R.id.tv_mcx_time);

        img_sensex=(ImageView)convertView.findViewById(R.id.img_sensex);
        img_nifty=(ImageView)convertView.findViewById(R.id.img_nifty);
        img_nasdaq=(ImageView)convertView.findViewById(R.id.img_nasdaq);
        img_nikkei=(ImageView)convertView.findViewById(R.id.img_nikkei);
        img_mcx=(ImageView)convertView.findViewById(R.id.img_mcx);

        mSwipeRefreshLayout = (SwipeRefreshLayout)convertView.findViewById(R.id.swipe_refrash_home);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Refreshing data on server
                startActivity(new Intent(getActivity(),Home.class));
            }
        });

        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new GetSensex().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new GetSensex().execute();
            }








        } else {
            // Internet connection is not present
            // Ask user to connect to Internet



        }






        return  convertView;

    }

    class GetSensex extends AsyncTask<Object, Void, String> {



        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");




        }

        @Override
        protected String doInBackground(Object... parametros) {

            // System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");

// should be a singleton
            OkHttpClient client = new OkHttpClient();
            /*HttpUrl.Builder urlBuilder = HttpUrl.parse("https://ajax.googleapis.com/ajax/services/search/images").newBuilder();
            urlBuilder.addQueryParameter("v", "1.0");
            urlBuilder.addQueryParameter("q", "android");
            urlBuilder.addQueryParameter("rsz", "8");
            String url = urlBuilder.build().toString();*/



            Request request = new Request.Builder()
                    .url("http://finance.google.com/finance/info?client=ig&q=INDEXBOM:SENSEX,nse:nifty,INDEXNIKKEI:NI225,INDEXNASDAQ%3A.IXIC,NSE%3AMCX")
                    .build();



            try{
                //request mate nicheno code
                Response response = client.newCall(request).execute();

                res1=response.body().string();
               // Log.d("okhtp==",res1);

            }catch(Exception e){
                e.printStackTrace();

            }



//            progressDialog.dismiss();
            return res1;

        }



        @Override
        protected void onPostExecute(String result)
        {

            String response_string="";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res1 == null || res1.equals("")) {



                Toast.makeText(getContext(), "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                //

                return;
            }

            try {



                res1=res1.substring(3);
                JSONArray array_res = new JSONArray(res1);

                for(int a =0;a<=array_res.length();a++){


                    //  Log.i("RESPONSE", res1);
                    JSONObject obj = array_res.getJSONObject(0);

                    String sensex = obj.getString("l_cur");
                    sensex= Html.fromHtml((String) sensex).toString();
                    String sensex_diff = obj.getString("c");
                    String sensex_diff_per = obj.getString("cp");
                    String sensex_time = obj.getString("lt");


                    tv_sensex.setText(sensex+" ( "+sensex_diff+")");
                    tv_sensex_diff_per.setText(sensex_diff_per+" % ");
                    tv_sensex_time.setText(sensex_time);

                    if(sensex_diff.contains("+")){
                        img_sensex.setImageResource(R.drawable.upmarket);
                    }else{
                        img_sensex.setImageResource(R.drawable.downmarket);
                    }


                    // ==------nifty---------------------

                    JSONObject obj2 = array_res.getJSONObject(1);

                    String nifty = obj2.getString("l_cur");
                    nifty= Html.fromHtml((String) nifty).toString();
                    String nifty_diff = obj2.getString("c");
                    String nifty_diff_per = obj2.getString("cp");
                    String nifty_time = obj2.getString("lt");


                    tv_nifty.setText(nifty+" ( "+nifty_diff+")");
                    tv_nifty_diff_per.setText(nifty_diff_per +" % ");
                    tv_nifty_time.setText(nifty_time);

                    if(nifty_diff.contains("+")){
                        img_nifty.setImageResource(R.drawable.upmarket);
                    }else{
                        img_nifty.setImageResource(R.drawable.downmarket);
                    }


                    //===========nasdaq============================

                    JSONObject obj3 = array_res.getJSONObject(3);

                    String nasdaq = obj3.getString("l_cur");
                    String nasdaq_diff = obj3.getString("c");
                    String nasdaq_diff_per = obj3.getString("cp");
                    String nasdaq_time = obj3.getString("lt");


                    tv_nasdaq.setText(nasdaq+" ( "+nasdaq_diff+")");
                    tv_nasdaq_diff_per.setText(nasdaq_diff_per+" % ");
                    tv_nasdaq_time.setText(nasdaq_time);

                    if(nasdaq_diff.contains("+")){
                        img_nasdaq.setImageResource(R.drawable.upmarket);
                    }else{
                        img_nasdaq.setImageResource(R.drawable.downmarket);
                    }


                    //========nikkei-----------------

                    JSONObject obj4 = array_res.getJSONObject(2);

                    String nikkei = obj4.getString("l_cur");
                    String nikkei_diff = obj4.getString("c");
                    String nikkei_diff_per = obj4.getString("cp");
                    String nikkei_time = obj4.getString("lt");


                    tv_nikkei.setText(nikkei+" ( "+nikkei_diff+")");
                    tv_nikkei_diff_per.setText(nikkei_diff_per+" % ");
                    tv_nikkei_time.setText(nikkei_time);

                    if(nikkei_diff.contains("+")){
                        img_nikkei.setImageResource(R.drawable.upmarket);
                    }else{
                        img_nikkei.setImageResource(R.drawable.downmarket);
                    }

                    ///===mcx=====

                    JSONObject obj5 = array_res.getJSONObject(4);
                    String mcx = obj5.getString("l_cur");
                    mcx= Html.fromHtml((String) mcx).toString();
                    String mcx_diff = obj5.getString("c");
                    String mcx_diff_per = obj5.getString("cp");
                    String mcx_time = obj5.getString("lt");


                    tv_mcx.setText(mcx+" ( "+mcx_diff+")");
                    tv_mcx_diff_per.setText(mcx_diff_per+" % ");
                    tv_mcx_time.setText(mcx_time);

                    if(mcx_diff.contains("+")){
                        img_mcx.setImageResource(R.drawable.upmarket);
                    }else{
                        img_mcx.setImageResource(R.drawable.downmarket);
                    }

                }

            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            //pg_bar.setVisibility(View.GONE);





        }
    }



}
