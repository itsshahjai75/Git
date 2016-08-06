package com.techno.jay.codingcontests;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
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

import DB.DBManager;
import DB.DatabaseHelper;

public class Splashscreen extends AppCompatActivity {

    final int SPLASH_TIME_OUT = 3500;
    SharedPreferences sharepref;
    Boolean isInternetPresent = false;
    String res,res2;

    DatabaseHelper dbh;
    SQLiteDatabase db;


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


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new Getallcontests().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new Getallcontests().execute();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new GetallSponsers().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                new GetallSponsers().execute();
            }






        } else {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {



				sharepref.edit().putString("totalsposor","62").commit();


                    startActivity(new Intent(Splashscreen.this,
                            Home.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();





                }
            }, SPLASH_TIME_OUT);


        }





    }

    class Getallcontests extends AsyncTask<Object, Void, String> {



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



            try{
                //request mate nicheno code


                URL obj = new URL("http://clist.by/api/v1/json/contest/?limit=2000&start__gt=2016-01-01&order_by=-start&username=shah.jai75&api_key=434a1084d357751c87dbd1dc9a8e0885accdd30b");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                //System.out.println("GET Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    //System.out.println(response.toString());
                    res=response.toString();
                }

                // print result





            }catch(Exception e){
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


                dbh = new DatabaseHelper(Splashscreen.this);
                db = dbh.getWritableDatabase();
                String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME + ";";
                ////Log.i("TAG day", selectQuery);
                mCursor = db.rawQuery(selectQuery, null);
                mCursor.moveToFirst();

                if(mCursor.getCount()!=0){
                    startActivity(new Intent(Splashscreen.this,
                            Home.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }

                mCursor.close();
                db.close();



                //

                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                //  Log.i("RESPONSE", res);



                String response_string_meta = obj.getString("meta");
                JSONObject obj_meta = new JSONObject(response_string_meta);
                String total_count = obj_meta.getString("total_count");




                response_string = obj.getString("objects");


                JSONArray array_res = new JSONArray(response_string);


                if (array_res.length() == 0) {

                    //                  Toast.makeText(getContext(),"No data found!",Toast.LENGTH_LONG).show();

                } else {

                    /*dbh = new DatabaseHelper(Splashscreen.this);
                    db = dbh.getWritableDatabase();
                    db.delete(DatabaseHelper.TABLE_NAME,null,null);
                    db.close();*/


                    for (int j = 0; j < array_res.length(); j++) {

                        String duration = array_res.getJSONObject(j).getString("duration");
                        String end = array_res.getJSONObject(j).getString("end");
                        end=end.replace("T"," ");
                        //Log.d("startDate",end);
                        String start = array_res.getJSONObject(j).getString("start");
                        start=start.replace("T"," ");
                        // Log.d("end date",start);
                        String event = array_res.getJSONObject(j).getString("event");
                        event=event.replace("'"," ");

                        String href = array_res.getJSONObject(j).getString("href");
                        href=href.replace("'"," ");

                        String contestid = array_res.getJSONObject(j).getString("id");

                        String resource = array_res.getJSONObject(j).getString("resource");
                        JSONObject obj_resourse = new JSONObject(resource);
                        String resource_id = obj_resourse.getString("id");
                        String resource_name = obj_resourse.getString("name");
                        resource_name=resource_name.replace("'"," ");
						
						//===convert UTC date time to local device date time==========
                       // Log.d("start_before",start);
                        SimpleDateFormat df_start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        df_start.setTimeZone(TimeZone.getTimeZone("UTC"));

                        Date date_start_UTC = df_start.parse(start);
                        df_start.setTimeZone(TimeZone.getDefault());
                        start = df_start.format(date_start_UTC);
                        //Log.d("start_after",start);

                        //===end time mate convert to local date formate
                        SimpleDateFormat df_end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        df_end.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date date_end_UTC = df_end.parse(end);
                        df_end.setTimeZone(TimeZone.getDefault());
                        end = df_end.format(date_end_UTC);


						//===========================================================================



                        try {

                            dbh = new DatabaseHelper(Splashscreen.this);
                            db = dbh.getWritableDatabase();

                            String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.CONTEST_ID + " = '" + contestid + "';";
                            //Log.i("TAG", selectQuery);
                             mCursor = db.rawQuery(selectQuery, null);
                            mCursor.moveToFirst();




                            if(mCursor==null || mCursor.getCount()<=0){
                               // Log.d("insert==","1");


                                String insertquery = "INSERT INTO " + DatabaseHelper.TABLE_NAME +" ( "+
                                DatabaseHelper.DURATION+","+
                                DatabaseHelper.END+","+
                                DatabaseHelper.START+","+
                                DatabaseHelper.EVENT+","+
                                DatabaseHelper.HREF+","+
                                DatabaseHelper.CONTEST_ID+","+
                                DatabaseHelper.RESOURCE_ID+","+
                                DatabaseHelper.RESOURCE_NAME+","+
                                DatabaseHelper.REMINDER + ") VALUES ( '"+
                                duration+"','"+
                                end+"','"+
                                start+"','"+
                                event+"','"+
                                href+"','"+
                                contestid+"','"+
                                resource_id+"','"+
                                resource_name+"',"+
                                "'0');";
                               // Log.i("TAG", insertquery);
                                Cursor insert_cursor = db.rawQuery(insertquery,null);
                                insert_cursor.moveToFirst();
                                insert_cursor.close();
                                mCursor.close();
                                db.close();

                            }else{
                               // Log.d("update==","1");
                                String updatequery = "UPDATE " + DatabaseHelper.TABLE_NAME +" SET "+
                                        DatabaseHelper.DURATION+"='"+duration+"',"+
                                        DatabaseHelper.END+"='"+ end+"',"+
                                        DatabaseHelper.START+"='"+start+"',"+
                                        DatabaseHelper.EVENT+"='"+event+"',"+
                                        DatabaseHelper.HREF+"='"+href+"',"+
                                        DatabaseHelper.CONTEST_ID+"='"+ contestid+"',"+
                                        DatabaseHelper.RESOURCE_ID+"='"+ resource_id+"',"+
                                        DatabaseHelper.RESOURCE_NAME+"='"+resource_name+"'"+
                                        " WHERE "+DatabaseHelper.CONTEST_ID+"='"+contestid+"';";
                               // Log.i("TAG", updatequery);
                                Cursor update_cursor = db.rawQuery(updatequery, null);
                                update_cursor.moveToFirst();
                                update_cursor.close();
                                mCursor.close();
                                db.close();

                            }






                        }catch (Exception Esql){
                            Esql.printStackTrace();
                        }finally {
                            if (mCursor != null && !mCursor.isClosed())
                                mCursor.close();
                                db.close();

                        }
                        // Log.d("usr Img",j+"=="+ Rpost_image);


                    }





                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            startActivity(new Intent(Splashscreen.this,
                    Home.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();





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



            try{
                //request mate nicheno code



                URL obj = new URL("http://clist.by:80/api/v1/json/resource/?limit=500&username=shah.jai75&api_key=434a1084d357751c87dbd1dc9a8e0885accdd30b");
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                //System.out.println("GET Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    //System.out.println(response.toString());
                    res2=response.toString();
                }

                // print result





            }catch(Exception e){
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



               // Toast.makeText(Splashscreen.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                //

                return;
            }

            try {
                JSONObject obj = new JSONObject(res2);






                response_string = obj.getString("objects");


                JSONArray array_res = new JSONArray(response_string);
                sharepref.edit().putString("totalsposor",Integer.toString(array_res.length())).commit();





                if (array_res.length() == 0) {

                    //                  Toast.makeText(getContext(),"No data found!",Toast.LENGTH_LONG).show();

                } else {


                    for (int j = 0; j < array_res.length(); j++) {


                        String contestid = array_res.getJSONObject(j).getString("id");

                        String resource_name = array_res.getJSONObject(j).getString("name");


                        stringArrayList.add(resource_name);
                        //Log.i("RESPONSE", stringArrayList.toString());




                    }


                    SharedPreferences.Editor edit=sharepref.edit();

                    Set<String> set = new HashSet<String>();
                    set.addAll(stringArrayList);
                    edit.putStringSet("arraylist", set);
                    edit.commit();




















                }
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            //pg_bar.setVisibility(View.GONE);





        }
    }
}

