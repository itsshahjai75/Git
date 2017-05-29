package com.techno.jay.codingcontests;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import DB.DatabaseHelper;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Splashscreen extends AppCompatActivity {

    final int SPLASH_TIME_OUT = 3500;
    SharedPreferences sharepref;
    Boolean isInternetPresent = false;
    String res, res2;

    ArrayList<String> stringArrayList;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen); //==1) for activity====
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)convertView);


        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


        stringArrayList = new ArrayList<String>();


        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(this);
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests

            //new GetDemo().execute();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                //new Getallcontests().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                //new Getallcontests().execute();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new GetallSponsers().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new GetallSponsers().execute();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    sharepref.edit().putString("totalsposor", "62").commit();


                    startActivity(new Intent(Splashscreen.this,
                            Home.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();


                }
            }, SPLASH_TIME_OUT);


        } else {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    sharepref.edit().putString("totalsposor", "62").commit();


                    startActivity(new Intent(Splashscreen.this,
                            Home.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();


                }
            }, SPLASH_TIME_OUT);
        }
    }

    class GetallSponsers extends AsyncTask<Object, Void, String> {


        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");

            //pg_bar.setVisibility(View.VISIBLE);


        }

        @Override
        protected String doInBackground(Object... parametros) {
            // System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://clist.by:80/api/v1/json/resource/?limit=500&username=shah.jai75&api_key=434a1084d357751c87dbd1dc9a8e0885accdd30b")
                        //.post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                res2 = response.body().string();

                // Log.e("response" , res2);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return res2;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res2 == null || res2.equals("")) {


                // Toast.makeText(Splashscreen.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                //

                return;
            }

            try {
                JSONObject obj = new JSONObject(res2);

                response_string = obj.getString("objects");


                JSONArray array_res = new JSONArray(response_string);
                sharepref.edit().putString("totalsposor", Integer.toString(array_res.length())).commit();

                if (array_res.length() == 0) {

                    //                  Toast.makeText(getContext(),"No data found!",Toast.LENGTH_LONG).show();

                } else {

                    for (int j = 0; j < array_res.length(); j++) {
                        String contestid = array_res.getJSONObject(j).getString("id");
                        String resource_name = array_res.getJSONObject(j).getString("name");
                        stringArrayList.add(resource_name);
                        //Log.i("RESPONSE", stringArrayList.toString());
                    }


                    SharedPreferences.Editor edit = sharepref.edit();
                    Set<String> set = new HashSet<String>();
                    set.addAll(stringArrayList);
                    edit.putStringSet("arraylist", set);
                    edit.commit();

                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*class GetDemo extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //pg_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {

                JSONObject req = new JSONObject();
                req.put("email_id","vani@ovenues.net");
                req.put("password","test@123");

                Log.d("REq Json======", req.toString());

                String response = post("http://54.153.127.215/api/login", req.toString());
                Log.d("REsponce Json====",response);
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return res;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


        }
    }*/

    public static final MediaType JSON = MediaType.parse("text/plain");

    OkHttpClient client = new OkHttpClient();
    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

}

