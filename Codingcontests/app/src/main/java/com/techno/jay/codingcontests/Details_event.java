package com.techno.jay.codingcontests;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DB.DBManager;
import DB.DatabaseHelper;

public class Details_event extends AppCompatActivity {

    String str_url;
    String str_eventid,END,START;
    private DBManager dbManager;
    Intent intentAlarm;

    SharedPreferences sharepref;

    AlarmManager alarmManager;
    DatabaseHelper dbh;
    SQLiteDatabase db;

    Date date_Now,date_Start,date_End;


    Cursor mCursor;
    TextView tv_title_companyname, tv_contest_titlte,tv_status,tv_title_companyname_logo,tv_remaintime,tv_addreminder,tv_share,tv_status_countdown
              ,tv_event_discription;
    TextView tv_startday,tv_endday,tv_link_url, tv_startdate_detailevent, tv_starttime_detailevent,tv_endtime_detailevent, tv_enddate_detailevent,tv_duration;
    DateFormat df_db_dateformate;


    //private ArcProgress arcProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_event);



        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "fonts/ProductSans-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==this = inflater.inflate(R.layout.listitem, null);
        //==fontChanger.replaceFonts((ViewGroup)this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        str_eventid=getIntent().getStringExtra("eventid");


        //arcProgress = (ArcProgress) findViewById(R.id.arc_progress);

        tv_title_companyname = (TextView) this.findViewById(R.id.tv_title_companyname);
        tv_contest_titlte = (TextView) this.findViewById(R.id.tv_contest_titlte);
        tv_title_companyname_logo = (TextView) this.findViewById(R.id.tv_title_companyname_logo);


        tv_status = (TextView) this.findViewById(R.id.tv_status);

        tv_link_url = (TextView) this.findViewById(R.id.tv_link_url);

        tv_startdate_detailevent = (TextView) this.findViewById(R.id.tv_startdate_detailevent);
        tv_starttime_detailevent = (TextView) this.findViewById(R.id.tv_starttime_detailevent);
        tv_startday = (TextView) this.findViewById(R.id.tv_startday_detailevent);

        tv_enddate_detailevent = (TextView) this.findViewById(R.id.tv_enddate_detailevent);
        tv_endtime_detailevent = (TextView) this.findViewById(R.id.tv_endtime_detailevent);
        tv_endday = (TextView) this.findViewById(R.id.tv_endday_detailevent);

        tv_duration = (TextView) this.findViewById(R.id.tv_duration);

        tv_remaintime = (TextView) this.findViewById(R.id.tv_remaintime);
        tv_status_countdown =(TextView) this.findViewById(R.id.tv_status_countdown);
        tv_addreminder = (TextView) this.findViewById(R.id.tv_addreminder);




        tv_addreminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PendingIntent operation=null;
                ////Log.d("clicked","1");

                try {
                    dbh = new DatabaseHelper(Details_event.this);
                    db = dbh.getWritableDatabase();

                    String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.CONTEST_ID + " = '" + str_eventid + "';";
                    ////Log.i("TAG day", selectQuery);
                     mCursor = db.rawQuery(selectQuery, null);

                    if (mCursor.moveToFirst()) {
                        do {

                            String REMINDER_STATUS = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.REMINDER));
                            String Start_time = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.START));

                           // //Log.i("TAG day", REMINDER_STATUS+Start_time);






                            if(REMINDER_STATUS.equalsIgnoreCase("0")
                                   || REMINDER_STATUS.equalsIgnoreCase("null")
                                    || REMINDER_STATUS.isEmpty()){
                                tv_addreminder.setText("Reminder Added !");
                                tv_addreminder.setBackgroundResource(R.color.md_green_A700);

                                String update_query = "UPDATE  "+ DatabaseHelper.TABLE_NAME + " SET "+DatabaseHelper.REMINDER+" = '1'"
                                        +" WHERE " + DatabaseHelper.CONTEST_ID + " = '" + str_eventid + "';";

                                Cursor mCursor2 = db.rawQuery(update_query, null);
                                mCursor2.moveToFirst();
                                mCursor2.close();

                                String min_before=sharepref.getString("reminder_min", "null");

                                if(min_before.equalsIgnoreCase("null")||min_before.isEmpty()){
                                    min_before="60";
                                }

                                Log.d("min before",min_before);
                                int min_reminder = Integer.parseInt(min_before);
                                date_Start = df_db_dateformate.parse(Start_time);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date_Start);
                                cal.add(Calendar.MINUTE,-min_reminder);

                                long time=cal.getTimeInMillis();
                                //Log.i("alarm added", df_db_dateformate.format(cal.getTime()));
                                intentAlarm = new Intent(Details_event.this, AlarmManagerBroadcastReceiver.class);

                                intentAlarm.putExtra("contestid",str_eventid);

                                /** Creating a Pending Intent */
                                operation = PendingIntent.getBroadcast(Details_event.this, Integer.parseInt(str_eventid), intentAlarm, 0);



                                //set the alarm for particular time

                                try {
                                    alarmManager = (AlarmManager) Details_event.this.getSystemService(Context.ALARM_SERVICE);
                                    Toast.makeText(Details_event.this,
                                            "Reminder Set for " + df_db_dateformate.format(cal.getTime()), Toast.LENGTH_LONG).show();
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, time, operation);

                                   // Log.d("alarms times are====", df_db_dateformate.format(cal.getTime()));
                                }catch (Exception dateexp){dateexp.printStackTrace();}

                            }else{
                                tv_addreminder.setText("Add Reminder");
                                tv_addreminder.setBackgroundResource(R.color.colorAccent);
                                String update_query = "UPDATE  "+ DatabaseHelper.TABLE_NAME + " SET "+DatabaseHelper.REMINDER+" = '0'"
                                        +" WHERE " + DatabaseHelper.CONTEST_ID + " = '" + str_eventid + "';";
                               // //Log.i("TAG day", update_query);
                                Cursor mCursor2 = db.rawQuery(update_query, null);
                                mCursor2.moveToFirst();
                                mCursor2.close();


                                    intentAlarm = new Intent(Details_event.this, AlarmManagerBroadcastReceiver.class);
                                    intentAlarm.putExtra("contestid",str_eventid);
                                    operation = PendingIntent.getBroadcast(Details_event.this, Integer.parseInt(str_eventid), intentAlarm, 0);

                                    alarmManager = (AlarmManager)Details_event.this.getSystemService(Context.ALARM_SERVICE);
                                    alarmManager.cancel(operation);

                            }
                        } while (mCursor.moveToNext());
                    }



                    mCursor.close();
                    db.close();

                }catch (Exception dberroe){

                }finally {
                    if (mCursor != null) {
                        mCursor.close();
                        db.close();

                    }
                }

            }
        });


        tv_share = (TextView) this.findViewById(R.id.tv_share);
        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentshare = new Intent(Intent.ACTION_SEND);
                intentshare.setType("text/plain");
                intentshare.putExtra(Intent.EXTRA_TEXT, "Event Details "
                        +"\nTitle : "+tv_contest_titlte.getText().toString()
                        +"\nWebHost : "+tv_title_companyname.getText().toString()
                        +"\nURL : "+tv_link_url.getText().toString()
                        +"\nStarts : "+tv_startdate_detailevent.getText().toString()+" On : "+tv_starttime_detailevent.getText().toString()
                        +"\nEnd : "+tv_enddate_detailevent.getText().toString()+" On : "+tv_endtime_detailevent.getText().toString()
                        +"\nDuration : "+tv_duration.getText().toString()
                        +"\nfrom Coding Contests.\n\n"
                        + "https://play.google.com/store/apps/details?id=com.techno.jay.codingcontests&hl=en");
                startActivity(Intent.createChooser(intentshare, "Share"));

            }
        });





        tv_event_discription = (TextView) this.findViewById(R.id.tv_event_discription);






//============on creat ma query call krai badha data laai set kre e code================================================

         df_db_dateformate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date a = null, b = null;


        try {


            dbh = new DatabaseHelper(Details_event.this);
            db = dbh.getWritableDatabase();




            String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.CONTEST_ID + " = '" + str_eventid + "';";
                ////Log.i("TAG day", selectQuery);
                Cursor mCursor = db.rawQuery(selectQuery, null);

                        /*String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.DURATION, DatabaseHelper.END,DatabaseHelper.START, DatabaseHelper.EVENT
                                ,DatabaseHelper.HREF, DatabaseHelper.CONTEST_ID,DatabaseHelper.RESOURCE_ID, DatabaseHelper.RESOURCE_NAME};
                        //Cursor mCursor = db.query(DatabaseHelper.TABLE_NAME, columns,null,  null, null, null, null);*/


                if (mCursor.moveToFirst()) {
                    do {
                        String DURATION = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DURATION));
                        END = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.END));
                        START = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.START));
                        String EVENT = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.EVENT));
                        String HREF = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.HREF));
                        String CONTEST_ID = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.CONTEST_ID));
                        String RESOURCE_ID = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.RESOURCE_ID));
                        String RESOURCE_NAME = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.RESOURCE_NAME));
                        String REMINDER_STATUS = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.REMINDER));

                              /*  //Log.i("START day", DURATION+
                                        END+
                                        START+
                                        EVENT+
                                        HREF+
                                        CONTEST_ID+
                                        RESOURCE_ID+
                                        RESOURCE_NAME);*/

                        //==REMINDER BUTTON===

                        if (REMINDER_STATUS.equalsIgnoreCase("1")) {
                            tv_addreminder.setText("Reminder Added !");
                            tv_addreminder.setBackgroundResource(R.color.md_green_A700);
                        } else {
                            tv_addreminder.setText("Add Reminder");
                            tv_addreminder.setBackgroundResource(R.color.colorAccent);
                        }

                        //================================

                        tv_contest_titlte.setText(EVENT);

                        String Uper_RESOURCE_NAME = RESOURCE_NAME.substring(0, 1).toUpperCase();
                        if (RESOURCE_NAME.contains(".")){
                            RESOURCE_NAME = RESOURCE_NAME.substring(1, RESOURCE_NAME.indexOf("."));
                        }else {
                            RESOURCE_NAME = RESOURCE_NAME.substring(1);
                        }

                        tv_title_companyname.setText(Uper_RESOURCE_NAME+RESOURCE_NAME);

                        tv_title_companyname_logo.setText(Uper_RESOURCE_NAME);


                        //==========start date and end date details set text code====
                        String full_start_date = START;

                        //df_post.setTimeZone(TimeZone.getTimeZone("MST"));

                        date_Start = df_db_dateformate.parse(START);
                        full_start_date = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(date_Start.getTime());
                        String full_start_time = new SimpleDateFormat("hh:mm:ss aa", Locale.US).format(date_Start.getTime());

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new SimpleDateFormat("dd-MMM-yyyy", Locale.US).parse(full_start_date));
                        int daystart = cal.get(Calendar.DAY_OF_WEEK);
                        String dayOfWeek = getDayOfWeek(daystart);
                        tv_startday.setText(dayOfWeek);

                        tv_startdate_detailevent.setText(full_start_date);
                        tv_starttime_detailevent.setText(full_start_time);


                        //== end date details==========================================================================

                        String full_end_date = END;

                         date_End = df_db_dateformate.parse(END);
                        full_end_date = new SimpleDateFormat("dd-MMM-yyyy", Locale.US).format(date_End.getTime());
                        String full_end_time = new SimpleDateFormat("hh:mm:ss aa", Locale.US).format(date_End.getTime());

                        cal.setTime(new SimpleDateFormat("dd-MMM-yyyy", Locale.US).parse(full_end_date));
                        int dayEnd = cal.get(Calendar.DAY_OF_WEEK);
                        String dayOfWeekEnd = getDayOfWeek(dayEnd);
                        tv_endday.setText(dayOfWeekEnd);

                        tv_enddate_detailevent.setText(full_end_date);
                        tv_endtime_detailevent.setText(full_end_time);

                        //url=====
                        tv_link_url.setText(HREF);


                        //===status=====================================

                            Date now = new Date();
                            String nowstr = df_db_dateformate.format(now);
                            date_Now= df_db_dateformate.parse(nowstr);



                        if(between(date_Now,date_Start,date_End)==true){
                            tv_status.setText(" Running ");
                            tv_status.setBackgroundColor(getResources().getColor(R.color.md_green_A700));
                            tv_addreminder.setEnabled(false);

                        }else if(date_End.before(date_Now)){
                            tv_status.setText(" Completed ");
                            tv_status.setBackgroundColor(getResources().getColor(R.color.md_red_A700));
                            tv_addreminder.setEnabled(false);
                        }else{
                            tv_status.setText(" Yet not started ");
                            tv_status.setBackgroundColor(getResources().getColor(R.color.md_yellow_A700));

                        }


                        //==duration
                        String duration_str = DURATION;
                        long seconds = Long.parseLong(duration_str);
                        int day_duration = (int) TimeUnit.SECONDS.toDays(seconds);
                        long hours = TimeUnit.SECONDS.toHours(seconds) - (day_duration *24);
                        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
                        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

                        tv_duration.setText(day_duration+" day "+hours+" Hr : "+minute+" min : "+second+" sec");






                    } while (mCursor.moveToNext());
                }








                if(mCursor==null || mCursor.getCount()<=0){

                    DataObject_post obj12 = new DataObject_post(null, null, null, "No Data Found Sorry!!!", null, null, null, null,"0");
                    // //Log.d("object",EVENT);

                }




//====count down show code==============================================================


            if(date_Start.before(date_Now) && date_End.after(date_Now)) {
                // System.out.println("dates=="+date_Now+date_End+date_Start);
                a = df_db_dateformate.parse(END);
                Date today = new Date();
                String s = df_db_dateformate.format(today);
                b = df_db_dateformate.parse(s);
                //      .getTime() does the conversion: Date --> long
               // arcProgress.setMax((int)(a.getTime()-b.getTime())/1000);

                CountDownTimer cdt = new CountDownTimer(a.getTime() - b.getTime(), 1000) {

                    public void onTick(long millisUntilFinished) {
                        // TODO Auto-generated method stub
                        long sec = millisUntilFinished/1000;

                        String duration_str = Long.toString(sec);
                        long seconds = Long.parseLong(duration_str);
                        int day = (int) TimeUnit.SECONDS.toDays(seconds);
                        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
                        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
                        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

                        /*int day_duration = (int) TimeUnit.MILLISECONDS.toDays(millis);
                        int hours = (int) TimeUnit.MILLISECONDS.toHours(millis)- 12;
                        long minute = TimeUnit.MILLISECONDS.toMinutes(millis) - (TimeUnit.MILLISECONDS.toHours(millis)* 60);
                        long second = TimeUnit.MILLISECONDS.toSeconds(millis) - (TimeUnit.MILLISECONDS.toMinutes(millis) *60);*/

                        tv_remaintime.setText(day+" day "+hours+" Hr : "+minute+" min : "+second+" sec");
                        tv_status_countdown.setText("Remaining Time");

                        int progress = (int) (millisUntilFinished/1000);
                        /*arcProgress.setProgress(arcProgress.getMax()-progress);
                        arcProgress.setSuffixText("");
                        arcProgress.setFinishedStrokeColor(R.color.colorPrimaryDark);
                        arcProgress.setUnfinishedStrokeColor(R.color.md_white_1000);*/


                    }

                    public void onFinish() {
                        // TODO Auto-generated method stub

                    }
                }.start();

            }else if(date_Start.after(date_Now) && date_End.after(date_Now)){
                //System.out.println("not started  =="+date_Now+date_End+date_Start);
                a = df_db_dateformate.parse(START);
                Date today = new Date();
                String s = df_db_dateformate.format(today);
                b = df_db_dateformate.parse(s);
              //  arcProgress.setMax((int)(a.getTime()-b.getTime())/1000);
                //      .getTime() does the conversion: Date --> long
                CountDownTimer cdt = new CountDownTimer(a.getTime() - b.getTime(), 1000) {

                    public void onTick(long millisUntilFinished) {
                        // TODO Auto-generated method stub
                        long sec = millisUntilFinished/1000;

                        String duration_str = Long.toString(sec);
                        long seconds = Long.parseLong(duration_str);
                        int day = (int) TimeUnit.SECONDS.toDays(seconds);
                        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
                        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
                        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);


                        tv_remaintime.setText(day+" day "+hours+" Hr : "+minute+" min : "+second+" sec");
                        tv_status_countdown.setText("Starts within");

                        int progress = (int) (millisUntilFinished/1000);
                       /* arcProgress.setProgress(arcProgress.getMax()-progress);
                        arcProgress.setSuffixText("");
                        arcProgress.setFinishedStrokeColor(R.color.colorPrimaryDark);
                        arcProgress.setUnfinishedStrokeColor(R.color.md_white_1000);*/


                    }

                    public void onFinish() {
                        // TODO Auto-generated method stub

                    }
                }.start();
            }
//=================================================================================================================

            mCursor.close();
            db.close();





        }catch (Exception ecxe){
            ecxe.printStackTrace();
        }finally {
            if (mCursor != null) {
                mCursor.close();
                db.close();

            }
        }


        //===========URL  par thi decription lae e code=====================

                try {
                    str_url = tv_link_url.getText().toString();

                    URL yahoo = new URL(str_url);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    yahoo.openStream()));

                    String inputLine;
                    String discription="";

                    while ((inputLine = in.readLine()) != null)
                        //System.out.println(inputLine);
                    if(inputLine.contains("meta name=\"description\" content=")){

                         discription = in.readLine();
                        tv_event_discription.setText("Description  :\n"+discription);
                       // System.out.println(discription);
                    }

                    if(discription.isEmpty() || discription.equalsIgnoreCase("")){
                        tv_event_discription.setText("Description :\n"+"Participate in "+tv_contest_titlte.getText().toString()+" and improve your programming,skills and win prizes and may get developer job. ");
                    }

                    in.close();

                }catch (Exception web){
                    web.printStackTrace();
                    tv_event_discription.setText("Description :\n"+"Participate in "+tv_contest_titlte.getText().toString()+" and improve your programming,skills and win prizes and may get developer job. ");
                }
//===============================================================================================



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


    private String getDayOfWeek(int value) {
        String day = "";
        switch (value) {
            case 1:
                day = "SUNDAY";
                break;
            case 2:
                day = "MONDAY";
                break;
            case 3:
                day = "TUESDAY";
                break;
            case 4:
                day = "WEDNESDAY";
                break;
            case 5:
                day = "THURSDAY";
                break;
            case 6:
                day = "FRIDAY";
                break;
            case 7:
                day = "SATURDAY";
                break;
        }
        return day;
    }







    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();


        finish();
    }
}
