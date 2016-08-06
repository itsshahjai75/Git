package com.jainisam.techno.jainisam;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Splashscreeen extends AppCompatActivity implements Animation.AnimationListener {



    private final static String TAG = "MainActivity";
    private String res;
    double latitude,longitude,altitude;


    Boolean isInternetPresent = false;

    String sunrise,sunset,daylength,nightlength,day_choghadia_duration,night_choghadiya_duration;

    SharedPreferences sharepref;
    ImageView imgLogo1,imgLogo2;

    // Animation
    Animation animTogether,animRotate;

    // GPSTracker class
    GPSTracker gps;


    DatabaseHelper myDbHelper;
    DatabaseHelper_panchang myDbHelper_panchang;
    DatabaseHelper_panchang_deravasi myDbHelper_panchang_deravasi;


    private static final int REQUEST_LOCATION_SERVICE = 0;


    private void populateLocation() {
        if (!mayRequestLocation()) {
            return;
        }

    }


    private boolean mayRequestLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                || shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Snackbar.make(imgLogo1, "Location permissions is required for Geo-Timings.", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_LOCATION_SERVICE);
                        }
                    });
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_SERVICE);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_SERVICE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateLocation();

            }else{

                Toast.makeText(Splashscreeen.this,"Timing will show wrong because Location Access Denied.",Toast.LENGTH_LONG).show();



                Snackbar.make(imgLogo1, "Location Permission Denied.", Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            @TargetApi(Build.VERSION_CODES.M)
                            public void onClick(View v) {
                               //requestPermissions(new String[]{LOCATION_SERVICE}, REQUEST_LOCATION_SERVICE);
                            }
                        });

            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreeen);


        if (android.os.Build.VERSION.SDK_INT > 14) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        //getKeyHash();

         sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        animTogether = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.together);

        // set animation listener
        animTogether.setAnimationListener(Splashscreeen.this);
        imgLogo1 = (ImageView) findViewById(R.id.logo1);
        imgLogo2 = (ImageView) findViewById(R.id.logo2);

        populateLocation();


        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // create class object


            imgLogo1.startAnimation(animTogether);
            imgLogo2.setVisibility(View.INVISIBLE);
            // new GetSunTimes().execute();



            final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("To receive relevant location based time you have to allow us access to your location.");
                    builder.setTitle("Location Services");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Splashscreeen.this, permissions, 0);
                        }
                    });

                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this, permissions, 0);
                }
            }else {

                gps = new GPSTracker(Splashscreeen.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    // \n is for new line
                    Log.d("Your Location is", Double.toString(latitude) + Double.toString(longitude));
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            showAlertDialog(Splashscreeen.this, "No Internet Connection",
                    "You don't have internet connection.", false);
        }





        myDbHelper = new DatabaseHelper(Splashscreeen.this);
        try {

            myDbHelper.createDataBase();
            myDbHelper.close();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }catch (Exception exp) {

            exp.printStackTrace();

        }

        myDbHelper_panchang = new DatabaseHelper_panchang(Splashscreeen.this);
        try {

            myDbHelper_panchang.createDataBase();
            myDbHelper_panchang.close();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }catch (Exception exp) {

            exp.printStackTrace();

        }


        myDbHelper_panchang_deravasi = new DatabaseHelper_panchang_deravasi(Splashscreeen.this);
        try {

            myDbHelper_panchang_deravasi.createDataBase();
            myDbHelper_panchang_deravasi.close();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }catch (Exception exp) {

            exp.printStackTrace();

        }








    }



    @SuppressWarnings({ "deprecation", "deprecation" })
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);
        alertDialog.setCanceledOnTouchOutside(false);

        // Setting alert dialog icon
        alertDialog.setIcon(R.drawable.appicon);

        // Setting OK Button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);


                alertDialog.dismiss();
            }
        });

        if(sharepref.getString("sunriseTime", null) !=null &&
                sharepref.getString("sunsetTime", null)!=null &&
                sharepref.getString("dayLength", null)!=null &&
                sharepref.getString("nightLength", null)!=null &&
                sharepref.getString("daychoghdiyaDuration", null )!= null &&
                sharepref.getString("nightchoghdiyaDuration", null) != null) {


            // Setting OK Button
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Open Anyhow!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    if (sharepref.getString("key_login", "no").equals("yes")) {

                        Log.d("user found = ",sharepref.getString("key_login", "no").toString());
                        startActivity(new Intent(Splashscreeen.this,
                                Home_screen_navigation.class));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
                        finish();
                    } else if(!sharepref.getString("key_login", "no").equals("yes")) {
                        Log.d("user not found = ",sharepref.getString("key_login", "no").toString());
                        startActivity(new Intent(Splashscreeen.this,
                                LoginActivity.class));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
                        finish();
                    }
                    alertDialog.dismiss();
                }
            });
        }



        // Showing Alert Message
        alertDialog.show();
    }



    class GetSunTimes extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
           // Log.d("pre execute", "Executando onPreExecute ingredients");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Splashscreeen.this, "Loading", "Please Wait.....", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            //System.out.println("On do in back ground----done-------");


            //Log.d("post execute", "Executando doInBackground   ingredients");



            try{
                //request mate nicheno code

                HttpClient client = new DefaultHttpClient();
                //String postURL = "http://169.254.76.188:8084/Sunil/order_entery";
                //HttpPost post = new HttpPost(postURL);

                HttpPost post = new HttpPost("http://api.sunrise-sunset.org/json?lat="+latitude+"&lng="+longitude+"&date=today");//2015-5-15
                List<NameValuePair> params = new ArrayList<NameValuePair>();

                //params.add(new BasicNameValuePair("key_item",item_ingredients));


                //response mate niche no code

                UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                post.setEntity(ent);

                HttpResponse responsePOST = client.execute(post);
                HttpEntity resEntity = responsePOST.getEntity();
                if (resEntity != null) {
                    String resp = EntityUtils.toString(resEntity);
                    res = resp;

                    System.out.println("response got from server----- "+resp);


                }}catch(Exception e){
                e.printStackTrace();

            }



//            progressDialog.dismiss();
            return res;

        }



        @Override
        protected void onPostExecute(String result)
        {

            //System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            if (res == null || res.equals("")) {


                progressDialog.dismiss();

                Toast.makeText(Splashscreeen.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject obj = new JSONObject(res);


                //Log.i("RESPONSE", res);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.



                item_ingre=obj.getString("results");//"" ma je key hoi tej key nb ma rakvi
                JSONObject obj_new = new JSONObject(item_ingre);
                String Sunrise =obj_new.getString("sunrise");
                Sunrise=Sunrise.replace("AM","am");

                String Sunset =obj_new.getString("sunset");
                Sunset=Sunset.replace("PM","pm");
                String day_length1 =obj_new.getString("day_length");

                //System.out.println(" sub json are====" + Sunrise);





                //===convert UTC in GMT time of device automaticaly----
                DateFormat dateformat = new SimpleDateFormat("hh:mm:ss aa",Locale.getDefault());
                /*----This is Main Part of UTC to GMT Convert Time Formula==
                java.util.Date date = dateformat.parse(Sunrise);
                Date local_date =   new Date(date.getTime()+ TimeZone.getDefault().getOffset(date.getTime()));

                 */

                String sub_hr=day_length1.substring(0,2);
                String sub_min=day_length1.substring(3,5);

                int hr=23-Integer.parseInt(sub_hr);
                int min=59-Integer.parseInt(sub_min);
                String night_length="";
                if(min<=9){
                     night_length=Integer.toString(hr)+":0"+Integer.toString(min)+":00";
                }else{
                    night_length=Integer.toString(hr)+":"+Integer.toString(min)+":00";
                }




                //String timestampStr = "14:35:06";
                String[] tokens = day_length1.split(":");
                int hours = Integer.parseInt(tokens[0]);
                int minutes = Integer.parseInt(tokens[1]);
                int seconds = Integer.parseInt(tokens[2]);
                int duration = 3600 * hours + 60 * minutes + seconds;


                //Log.d("toal sec of day", Integer.toString(duration));

                int one_daychoghdiya_duration = duration/8;

                int hr2=one_daychoghdiya_duration/3600;
                int newhr2=(one_daychoghdiya_duration%3600);

                int min2=(newhr2)/60;
                int newmin2=(newhr2)%60;

                int sec2 =newmin2;

              //  Log.d("onechoghdiyaTime", hr2 + ":" + min2 + ":" + sec2);

                String[] tokens2 = night_length.split(":");
                int hours2 = Integer.parseInt(tokens2[0]);
                int minutes2 = Integer.parseInt(tokens2[1]);
                int seconds2 = Integer.parseInt(tokens2[2]);
                int duration2 = 3600 * hours2 + 60 * minutes2 + seconds2;


               // Log.d("toal sec of night", Integer.toString(duration2));

                int one_nighthoghdiya_duration = duration2/8;

                int hr22=one_nighthoghdiya_duration/3600;
                int newhr22=(one_nighthoghdiya_duration%3600);

                int min22=(newhr22)/60;
                int newmin22=(newhr22)%60;

                int sec22 =newmin22;

               // Log.d("oneNightchoghdiyaTime", hr22 + ":" + min22 + ":" + sec22);


                dateformat.setTimeZone(TimeZone.getTimeZone("UTC"));
                //"SunRise =" +
                //tv_sunrisetime.setText(dateformat.format((dateformat.parse(Sunrise)).getTime() + TimeZone.getDefault().getOffset((dateformat.parse(Sunrise)).getTime())));


                sunrise =new SimpleDateFormat("hh:mm:ss aa",Locale.getDefault()).format((dateformat.parse(Sunrise)));

                //"Sunset ="
                //tv_sunsettime.setText(dateformat.format((dateformat.parse(Sunset)).getTime()+ TimeZone.getDefault().getOffset((dateformat.parse(Sunset)).getTime())));
                sunset=new SimpleDateFormat("hh:mm:ss aa",Locale.getDefault()).format((dateformat.parse(Sunset)));
                //"DayLength ="+
                //tv_daytime.setText(df.format((df.parse(day_length1))));
                daylength=day_length1;
                //"Night Lenght ="+
               // tv_nighttime.setText(df.format((df.parse(night_length))));
                nightlength=night_length;//df.parse(night_length).toString();
                //"OneDay Choghdiya Dureation ="
               // tv_daychoghdiyaduration.setText(df.format(df.parse(hr2+":"+min2+":"+sec2)));
                day_choghadia_duration=hr2+":"+min2+":"+sec2;//df.format(df.parse(hr2+":"+min2+":"+sec2)).toString();

                //"One_Night _Choghdiya_Dureation ="+
                //tv_nightchoghdiyaduration.setText(df.format(df.parse(hr22+":"+min22+":"+sec22)));
                night_choghadiya_duration=hr22+":"+min22+":"+sec22;//df.format(df.parse(hr22+":"+min22+":"+sec22)).toString();





                SharedPreferences.Editor editor = sharepref.edit();
                editor.putString("sunriseTime", sunrise);
                editor.putString("sunsetTime", sunset);
                editor.putString("dayLength", daylength);
                editor.putString("nightLength", nightlength);
                editor.putString("daychoghdiyaDuration", day_choghadia_duration);
                editor.putString("nightchoghdiyaDuration", night_choghadiya_duration);
                editor.commit();





                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (sharepref.getString("key_login", "no").equals("yes")) {

                            Log.d("user found = ", sharepref.getString("key_login", "no").toString());
                            Intent Home=new Intent(Splashscreeen.this,
                                    Home_screen_navigation.class);
                          /*  Home.putExtra("PsunriseTime", sunrise);
                            Home.putExtra("PsunsetTime", sunset);
                            Home.putExtra("PdayLength", daylength);
                            Home.putExtra("PnightLength", nightlength);
                            Home.putExtra("PdaychoghdiyaDuration", day_choghadia_duration);
                            Home.putExtra("PnightchoghdiyaDuration", night_choghadiya_duration);*/

                            startActivity(Home);

                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
                            finish();
                        } else if(!sharepref.getString("key_login", "no").equals("yes")) {
                            Log.d("user not found = ",sharepref.getString("key_login", "no").toString());

                            progressDialog.dismiss();

                            Intent ALogin=new Intent(Splashscreeen.this,
                                    LoginActivity.class);
                           /* ALogin.putExtra("PsunriseTime", sunrise);
                            ALogin.putExtra("PsunsetTime", sunset);
                            ALogin.putExtra("PdayLength", daylength);
                            ALogin.putExtra("PnightLength", nightlength);
                            ALogin.putExtra("PdaychoghdiyaDuration", day_choghadia_duration);
                            ALogin.putExtra("PnightchoghdiyaDuration", night_choghadiya_duration);*/

                            startActivity(ALogin);

                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
                            finish();
                        }


                    }
                }, 3500);







            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }





        }
    }



    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for zoom in animation
        if (animation == animTogether) {
            animRotate = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.rotate);



            animRotate.setAnimationListener(Splashscreeen.this);



            imgLogo2.setVisibility(View.VISIBLE);
            imgLogo2.startAnimation(animRotate);
        }
        if(animation == animRotate){
            new GetSunTimes().execute();


        }

    }



    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }


    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();

    }



    private void getKeyHash() {

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.jainisam.techno.jainisam", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
}

