
package com.jainisam.techno.jainisam;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Choghadiya extends AppCompatActivity {



    SharedPreferences sharepref;
    String dayOfTheWeek,yesterDay;
    DatabaseHelper myDbHelper;
    Cursor c;
    CardView cv1,cv2;


    private Date date_current_hour;
    private Date dateCompareOne;
    private Date dateCompareTwo;

    private String compareStringOne;
    private String compareStringTwo;

    private Date date_after_12_dateCompareOne;
    private Date date_before_sunrise_dateCompareTwo;

    private String after_12_compareStringOne ;
    private String before_sunrise_compareStringTwo;
    MyTextView tv_date_day;
    TableLayout ll_2,ll_1;

    String SRtime,SStime,DLtime,NLtime,Dchoghdiya,Nchoghdiya;

    SimpleDateFormat sdf_HMa = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
    SimpleDateFormat sdf_HMSa = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());

    String one_day_choghadiya_duration,one_night_choghadiya_duration,sun_riseTime,sun_setTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choghadiya);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);



        SRtime=sharepref.getString("sunriseTime", null).toString();
        SStime=sharepref.getString("sunsetTime", null).toString();
        DLtime=sharepref.getString("dayLength", null).toString();
        NLtime=sharepref.getString("nightLength", null).toString();
        Dchoghdiya=sharepref.getString("daychoghdiyaDuration", null).toString();
        Nchoghdiya=sharepref.getString("nightchoghdiyaDuration", null).toString();


        tv_date_day =(MyTextView)this.findViewById(R.id.tv_dat_day);












        final Typeface cFont = Typeface.createFromAsset(getBaseContext().getAssets(), "Roboto-Thin.ttf");
        final Typeface cFont2 = Typeface.createFromAsset(getBaseContext().getAssets(), "Roboto-Regular.ttf");

        myDbHelper = new DatabaseHelper(Choghadiya.this);

        cv1 =(CardView)this.findViewById(R.id.card_view2);
        cv2 =(CardView)this.findViewById(R.id.card_view21);


        /*System.out.println("share pref are" + sharepref.getString("sunriseTime", null) + ",\n" +
                sharepref.getString("sunsetTime", null) + ",\n" +
                sharepref.getString("dayLength", null) + ",\n" +
                sharepref.getString("nightLength", null) + ",\n" +
                sharepref.getString("daychoghdiyaDuration", null) + ",\n" +
                sharepref.getString("nightchoghdiyaDuration", null));*/



        SimpleDateFormat sdfCal = new SimpleDateFormat("EEEE");
        Calendar calmain= Calendar.getInstance();
        calmain.add(Calendar.DAY_OF_WEEK,-1);
        Date d_yessterday = calmain.getTime();
        yesterDay = sdfCal.format(d_yessterday);
        // Log.d("yester Day==", yesterDay);

        //=====Static table title for Day===
        ll_1 = (TableLayout) findViewById(R.id.tabl_1);

        TableRow row0= new TableRow(this);
        TableRow.LayoutParams lp0 = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT);
        row0.setLayoutParams(lp0);
        row0.setBackgroundResource(R.color.colorPrimaryDark);
        row0.setGravity(Gravity.CENTER);


        TextView tv0 = new TextView(this);
        tv0.setText("Day Choghadiya");
        tv0.setTypeface(cFont2);
        tv0.setTextSize(27);
        tv0.setTextColor(Color.WHITE);


        row0.addView(tv0);
        ll_1.addView(row0);

        //====static table title for Night=======================================
        ll_2 = (TableLayout) findViewById(R.id.tabl_2);

        TableRow row01= new TableRow(this);
        TableRow.LayoutParams lp01 = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT);
        row01.setLayoutParams(lp01);
        row01.setGravity(Gravity.CENTER);
        row01.setBackgroundResource(R.color.colorPrimaryDark);

        TextView tv01 = new TextView(this);
        tv01.setText("Night Choghadiya");
        tv01.setTypeface(cFont2);
        tv01.setTextSize(27);
        tv01.setTextColor(Color.WHITE);

        row01.addView(tv01);
        ll_2.addView(row01);

//===================================================================================================================


        Date d_today = new Date();
        dayOfTheWeek = sdfCal.format(d_today);
        // Log.d("TOday Day==",dayOfTheWeek);

        try {

            DateFormat df_choghadiya = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());
            DateFormat df_choghadiya_duration = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());

            Date sunriseTime = df_choghadiya.parse(SRtime);
            Date sunsetTime = df_choghadiya.parse(SStime);

            Date daychoghdiyaDuration = df_choghadiya_duration.parse(Dchoghdiya);
            Date nightchoghdiyaDuration = df_choghadiya_duration.parse(Nchoghdiya);

            one_day_choghadiya_duration = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(daychoghdiyaDuration);
            one_night_choghadiya_duration = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(nightchoghdiyaDuration);

            sun_riseTime = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault()).format(sunriseTime);
            sun_setTime = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault()).format(sunsetTime);

        }catch (Exception date_choghadiya){
            date_choghadiya.printStackTrace();
        }

        after_12_compareStringOne="12:01 am";
        before_sunrise_compareStringTwo=sun_riseTime;

        //==geting current Hour===
        Calendar now = Calendar.getInstance();

        int hour = now.get(Calendar.HOUR);
        int minute = now.get(Calendar.MINUTE);
        int AM_PM =now.get(Calendar.AM_PM);
        String am_pm;
        if(AM_PM==0){
            am_pm="am";
        }else {
            am_pm="pm";
        }

        //Log.d("current Hr",am_pm);
        try{
            date_current_hour= sdf_HMa.parse(hour+":"+minute+" "+am_pm);
            date_after_12_dateCompareOne = sdf_HMa.parse(after_12_compareStringOne);
            date_before_sunrise_dateCompareTwo = sdf_HMSa.parse(before_sunrise_compareStringTwo);

//########################################################################################################################################
            if(date_after_12_dateCompareOne.before(date_current_hour) && date_before_sunrise_dateCompareTwo.after(date_current_hour)){


                SimpleDateFormat sdfCal_datday = new SimpleDateFormat("dd/MMM/yyyy EEEE",Locale.getDefault());
                Calendar cal_datday= Calendar.getInstance();
                cal_datday.add(Calendar.DATE,-1);
                tv_date_day.setText(sdfCal_datday.format(cal_datday.getTime()));

                //  Log.d("Time 12:00Am to SunRise",sdf_HMa.format(date_current_hour));
                //==ahiya old choghdiyaj batava na che sun rise time sudhi====

                dayOfTheWeek=yesterDay;

                String[] partation = one_day_choghadiya_duration.split(":");
                int hours_Dpartion = Integer.parseInt(partation[0]);
                int minutes_Dpartion = Integer.parseInt(partation[1]);
                int seconds_DpartionH = Integer.parseInt(partation[2]);
                //=======================

                String[] partation_night = one_night_choghadiya_duration.split(":");
                int hours_Npartion = Integer.parseInt(partation_night[0]);
                int minutes_Npartion = Integer.parseInt(partation_night[1]);
                int seconds_NpartionH = Integer.parseInt(partation_night[2]);


                //===============================

                String choghadiyo_day="";
                String choghadiyo_night="";

                String first_day_time=sun_riseTime;
                first_day_time=first_day_time.substring(0,5);
                first_day_time=first_day_time+" am";

                //===for night========
                String first_night_time=sun_setTime;
                first_night_time=first_night_time.substring(0,5);
                first_night_time=first_night_time+" pm";



                for(int i=1;i<=8;i++){

                    try {

                        myDbHelper.openDataBase();

                        c=myDbHelper.query("choghadia_name",new String[]{"choghadiya_Type",
                                        "choghadiya_Day",
                                        "choghaidya_Count",
                                        "choghadiya_Name",} ,
                                "choghadiya_Type=? and choghadiya_Day=? and choghaidya_Count=?" ,
                                new String []{"day",dayOfTheWeek,Integer.toString(i)}, null,null, null);
                        // Cursor c=myDbHelper.query("choghadia_name", null, null, null, null,null, null);

                        if(c.moveToFirst())
                        {
                            do {

                                choghadiyo_day = c.getString(3);
                     /* Log.d("Choghadiya",
                              "_id: " + c.getString(0) + "\n" +
                                      "E_NAME: " + c.getString(1) + "\n" +
                                      "E_AGE: " + c.getString(2) + "\n" +
                                      "E_DEPT:  " + c.getString(3) + "\n" +
                                      "E_DEPT4:  " + c.getString(4));
*/

                            } while (c.moveToNext());

                            myDbHelper.close();
                        }



                        Calendar calendar_day = Calendar.getInstance();
                        calendar_day.setTime(sdf_HMSa.parse(sun_riseTime));
                        calendar_day.add(Calendar.HOUR, hours_Dpartion * i);
                        calendar_day.add(Calendar.MINUTE, minutes_Dpartion * i);
                        String new_time = sdf_HMa.format(calendar_day.getTime());
                        //new_time = new_time.substring(0, 5);



                        TableRow row= new TableRow(this);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT);
                        // lp.setMargins(0,15,0,15);
                        row.setLayoutParams(lp);
                        row.setGravity(Gravity.CENTER);


                        TextView tv1 = new TextView(this);
                        tv1.setText(first_day_time + "  -  " + new_time);
                        tv1.setTypeface(cFont, Typeface.BOLD);
                        tv1.setGravity(Gravity.CENTER);
                        tv1.setTextSize(19);



                        TextView tv2 = new TextView(this);
                        tv2.setText(" "+choghadiyo_day);
                        tv2.setTypeface(cFont, Typeface.BOLD);
                        tv2.setGravity(Gravity.CENTER);
                        tv2.setTextSize(19);



                        row.addView(tv1);
                        row.addView(tv2);
                        ll_1.addView(row);


                        compareStringOne = first_day_time;
                        compareStringTwo = new_time;

                        dateCompareOne = sdf_HMa.parse(compareStringOne);
                        dateCompareTwo = sdf_HMa.parse(compareStringTwo);

                        if (dateCompareOne.before(date_current_hour) && dateCompareTwo.after(date_current_hour)) {
                            //yada yada
                            row.setBackgroundResource(R.color.colorhighlight);
                            tv1.setTextColor(Color.parseColor("#ffffff"));
                            tv2.setTextColor(Color.parseColor("#ffffff"));
                            //   Log.d("current choghadiya",choghadiyo_day);
                        } else {
                            if (i % 2 == 0) {
                                row.setBackgroundResource(R.color.colorAccent);
                            }
                        }


                        first_day_time=new_time;


                        //====day compplited========================================================================================


                        myDbHelper.openDataBase();


                        // Cursor c=myDbHelper.query("choghadia_name", null, null, null, null,null, null);
                        c=myDbHelper.query("choghadia_name",new String[]{"choghadiya_Type",
                                        "choghadiya_Day","choghaidya_Count","choghadiya_Name"} ,
                                "choghadiya_Type=? and choghadiya_Day=? and choghaidya_Count=?" ,
                                new String []{"night",dayOfTheWeek,Integer.toString(i)}, null,null, null);

                        if(c.moveToFirst())
                        {
                            do {

                                choghadiyo_night = c.getString(3);
/*
                        Toast.makeText(Choghadiya.this,
                                "_id: " + c.getString(0) + "\n" +
                                        "E_NAME: " + c.getString(1) + "\n" +
                                        "E_AGE: " + c.getString(2) + "\n" +
                                        "E_DEPT:  " + c.getString(3) + "\n" +
                                        "E_DEPT4:  " + c.getString(4),
                                Toast.LENGTH_LONG).show();*/

                            } while (c.moveToNext());

                            myDbHelper.close();
                        }

                        String new_time_night="";
                        Calendar calendar_night = Calendar.getInstance();
                        calendar_night.setTime(sdf_HMSa.parse(sun_setTime));
                        //calendar_night.setTime(new SimpleDateFormat("dd/mm/yyyy hh:mm:ss aa").parse("1/1/2015 " + sun_setTime));
                        calendar_night.add(Calendar.HOUR, hours_Npartion * i);
                        calendar_night.add(Calendar.MINUTE, minutes_Npartion * i);
                        new_time_night= sdf_HMa.format(calendar_night.getTime());
                        // Log.d("new time at ",Integer.toString(i)+"="+new_time_night);



                        //new_time = new_time.substring(0, 5);


                        TableRow rowN= new TableRow(this);
                        TableRow.LayoutParams lpN = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT);
                        //lpN.setMargins(0,15,0,15);
                        rowN.setLayoutParams(lpN);
                        rowN.setGravity(Gravity.CENTER);


                        TextView tv1N = new TextView(this);
                        tv1N.setText(first_night_time + "  -  " + new_time_night);
                        tv1N.setTypeface(cFont, Typeface.BOLD);
                        tv1N.setGravity(Gravity.CENTER);
                        tv1N.setTextSize(19);



                        TextView tv2N = new TextView(this);
                        tv2N.setText(" " + choghadiyo_night);
                        tv2N.setTypeface(cFont, Typeface.BOLD);
                        tv2N.setGravity(Gravity.CENTER);
                        tv2N.setTextSize(19);



                        rowN.addView(tv1N);
                        rowN.addView(tv2N);
                        ll_2.addView(rowN);


                        compareStringOne = first_night_time;
                        compareStringTwo = new_time_night;



                       /* dateCompareOne = sdf_HMa.parse(compareStringOne);
                        dateCompareTwo = sdf_HMa.parse(compareStringTwo);

                        if (dateCompareOne.before(date_current_hour) && dateCompareTwo.after(date_current_hour)) {
                            //yada yada
                            rowN.setBackgroundResource(R.color.colorhighlight);
                            tv1N.setTextColor(Color.parseColor("#ffffff"));
                            tv2N.setTextColor(Color.parseColor("#ffffff"));
                            Log.d("current choghadiya",choghadiyo_night);
                        } else {
                            if (i % 2 == 0) {
                                rowN.setBackgroundResource(R.color.colorAccent);
                            }
                        }*/




                        Date mToday = new Date();

                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa",Locale.getDefault());
                        String curTime = sdf.format(mToday);
                        Date start = sdf.parse(compareStringOne);
                        Date end = sdf.parse(compareStringTwo);
                        Date userDate = sdf.parse(curTime);

                        if(end.before(start))
                        {
                            Calendar mCal = Calendar.getInstance();
                            mCal.setTime(end);
                            mCal.add(Calendar.DAY_OF_YEAR, 1);
                            end.setTime(mCal.getTimeInMillis());
                        }

                        // Log.d("curTime", userDate.toString());
                        //Log.d("start", start.toString());
                        //Log.d("end", end.toString());


                        if (userDate.after(start) && userDate.before(end)) {
                            rowN.setBackgroundResource(R.color.colorhighlight);
                            tv1N.setTextColor(Color.parseColor("#ffffff"));
                            tv2N.setTextColor(Color.parseColor("#ffffff"));
                            //Log.d("current choghadiya",choghadiyo_night);
                            //Log.d("result", "falls between start and end , go to screen 1 ");
                        }
                        else{
                            if (i % 2 == 0) {
                                rowN.setBackgroundResource(R.color.colorAccent);
                            }
                            //Log.d("result", "does not fall between start and end , go to screen 2 ");
                        }






                        first_night_time=new_time_night;





                    }catch(SQLException sqle){

                        throw sqle;

                    }catch(Exception excmain){

                        throw excmain;

                    }

                }//====for loop ahi complite thaay che==

                //======================================================================
                //=========================================================================================================
                //====================================================================================================================
            }else{
                //===ahiya today vala choghdiya dekhadvana che.

                //Log.d("sunrise to 12:00 am",sdf_HMa.format(date_current_hour));


                SimpleDateFormat sdfCal_datday = new SimpleDateFormat("dd/MMM/yyyy EEEE",Locale.getDefault());
                Calendar cal_datday= Calendar.getInstance();
                tv_date_day.setText(sdfCal_datday.format(cal_datday.getTime()));


                String[] partation = one_day_choghadiya_duration.split(":");
                int hours_Dpartion = Integer.parseInt(partation[0]);
                int minutes_Dpartion = Integer.parseInt(partation[1]);
                int seconds_DpartionH = Integer.parseInt(partation[2]);
                //=======================

                String[] partation_night = one_night_choghadiya_duration.split(":");
                int hours_Npartion = Integer.parseInt(partation_night[0]);
                int minutes_Npartion = Integer.parseInt(partation_night[1]);
                int seconds_NpartionH = Integer.parseInt(partation_night[2]);


                //===============================

                String choghadiyo_day="";
                String choghadiyo_night="";

                String first_day_time=sun_riseTime;
                first_day_time=first_day_time.substring(0,5);
                first_day_time=first_day_time+" am";

                //===for night========
                String first_night_time=sun_setTime;
                first_night_time=first_night_time.substring(0,5);
                first_night_time=first_night_time+" pm";



                for(int i=1;i<=8;i++){

                    try {

                        myDbHelper.openDataBase();

                        c=myDbHelper.query("choghadia_name",new String[]{"choghadiya_Type",
                                        "choghadiya_Day",
                                        "choghaidya_Count",
                                        "choghadiya_Name",} ,
                                "choghadiya_Type=? and choghadiya_Day=? and choghaidya_Count=?" ,
                                new String []{"day",dayOfTheWeek,Integer.toString(i)}, null,null, null);
                        // Cursor c=myDbHelper.query("choghadia_name", null, null, null, null,null, null);

                        if(c.moveToFirst())
                        {
                            do {

                                choghadiyo_day = c.getString(3);
                     /* Log.d("Choghadiya",
                              "_id: " + c.getString(0) + "\n" +
                                      "E_NAME: " + c.getString(1) + "\n" +
                                      "E_AGE: " + c.getString(2) + "\n" +
                                      "E_DEPT:  " + c.getString(3) + "\n" +
                                      "E_DEPT4:  " + c.getString(4));
*/

                            } while (c.moveToNext());

                            myDbHelper.close();
                        }



                        Calendar calendar_day = Calendar.getInstance();
                        calendar_day.setTime(sdf_HMSa.parse(sun_riseTime));
                        calendar_day.add(Calendar.HOUR, hours_Dpartion * i);
                        calendar_day.add(Calendar.MINUTE, minutes_Dpartion * i);
                        String new_time = sdf_HMa.format(calendar_day.getTime());
                        //new_time = new_time.substring(0, 5);



                        TableRow row= new TableRow(this);
                        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT);
                        //lp.setMargins(0,900,0,900);
                        row.setLayoutParams(lp);
                        row.setGravity(Gravity.CENTER);


                        TextView tv1 = new TextView(this);
                        tv1.setText(first_day_time + "  -  " + new_time);
                        tv1.setTypeface(cFont, Typeface.BOLD);
                        tv1.setGravity(Gravity.CENTER);
                        tv1.setTextSize(19);



                        TextView tv2 = new TextView(this);
                        tv2.setText(" "+choghadiyo_day);
                        tv2.setTypeface(cFont, Typeface.BOLD);
                        tv2.setGravity(Gravity.CENTER);
                        tv2.setTextSize(19);



                        row.addView(tv1);
                        row.addView(tv2);
                        ll_1.addView(row);


                        compareStringOne = first_day_time;
                        compareStringTwo = new_time;

                        dateCompareOne = sdf_HMa.parse(compareStringOne);
                        dateCompareTwo = sdf_HMa.parse(compareStringTwo);

                        if (dateCompareOne.before(date_current_hour) && dateCompareTwo.after(date_current_hour)) {
                            //yada yada
                            row.setBackgroundResource(R.color.colorhighlight);
                            tv1.setTextColor(Color.parseColor("#ffffff"));
                            tv2.setTextColor(Color.parseColor("#ffffff"));
                            // Log.d("current choghadiya",choghadiyo_day);
                        } else {
                            if (i % 2 == 0) {
                                row.setBackgroundResource(R.color.colorAccent);
                            }
                        }


                        first_day_time=new_time;


                        //====day compplited========================================================================================


                        myDbHelper.openDataBase();


                        // Cursor c=myDbHelper.query("choghadia_name", null, null, null, null,null, null);
                        c=myDbHelper.query("choghadia_name",new String[]{"choghadiya_Type",
                                        "choghadiya_Day","choghaidya_Count","choghadiya_Name"} ,
                                "choghadiya_Type=? and choghadiya_Day=? and choghaidya_Count=?" ,
                                new String []{"night",dayOfTheWeek,Integer.toString(i)}, null,null, null);

                        if(c.moveToFirst())
                        {
                            do {

                                choghadiyo_night = c.getString(3);
/*
                        Toast.makeText(Choghadiya.this,
                                "_id: " + c.getString(0) + "\n" +
                                        "E_NAME: " + c.getString(1) + "\n" +
                                        "E_AGE: " + c.getString(2) + "\n" +
                                        "E_DEPT:  " + c.getString(3) + "\n" +
                                        "E_DEPT4:  " + c.getString(4),
                                Toast.LENGTH_LONG).show();*/

                            } while (c.moveToNext());

                            myDbHelper.close();
                        }

                        String new_time_night="";
                        Calendar calendar_night = Calendar.getInstance();
                        calendar_night.setTime(sdf_HMSa.parse(sun_setTime));
                        //calendar_night.setTime(new SimpleDateFormat("dd/mm/yyyy hh:mm:ss aa").parse("1/1/2015 " + sun_setTime));
                        calendar_night.add(Calendar.HOUR, hours_Npartion * i);
                        calendar_night.add(Calendar.MINUTE, minutes_Npartion * i);
                        new_time_night= sdf_HMa.format(calendar_night.getTime());
                        //  Log.d("new time at ",Integer.toString(i)+"="+new_time_night);



                        //new_time = new_time.substring(0, 5);


                        TableRow rowN= new TableRow(this);
                        TableRow.LayoutParams lpN = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT);
                        // lpN.setMargins(0,15,0,15);
                        rowN.setLayoutParams(lpN);
                        rowN.setGravity(Gravity.CENTER);


                        TextView tv1N = new TextView(this);
                        tv1N.setText(first_night_time + "  -  " + new_time_night);
                        tv1N.setTypeface(cFont, Typeface.BOLD);
                        tv1N.setGravity(Gravity.CENTER);
                        tv1N.setTextSize(19);



                        TextView tv2N = new TextView(this);
                        tv2N.setText(" " + choghadiyo_night);
                        tv2N.setTypeface(cFont, Typeface.BOLD);
                        tv2N.setGravity(Gravity.CENTER);
                        tv2N.setTextSize(19);



                        rowN.addView(tv1N);
                        rowN.addView(tv2N);
                        ll_2.addView(rowN);


                        compareStringOne = first_night_time;
                        compareStringTwo = new_time_night;



                       /* dateCompareOne = sdf_HMa.parse(compareStringOne);
                        dateCompareTwo = sdf_HMa.parse(compareStringTwo);

                        if (dateCompareOne.before(date_current_hour) && dateCompareTwo.after(date_current_hour)) {
                            //yada yada
                            rowN.setBackgroundResource(R.color.colorhighlight);
                            tv1N.setTextColor(Color.parseColor("#ffffff"));
                            tv2N.setTextColor(Color.parseColor("#ffffff"));
                            Log.d("current choghadiya",choghadiyo_night);
                        } else {
                            if (i % 2 == 0) {
                                rowN.setBackgroundResource(R.color.colorAccent);
                            }
                        }*/




                        Date mToday = new Date();

                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa",Locale.getDefault());
                        String curTime = sdf.format(mToday);
                        Date start = sdf.parse(compareStringOne);
                        Date end = sdf.parse(compareStringTwo);
                        Date userDate = sdf.parse(curTime);

                        if(end.before(start))
                        {
                            Calendar mCal = Calendar.getInstance();
                            mCal.setTime(end);
                            mCal.add(Calendar.DAY_OF_YEAR, 1);
                            end.setTime(mCal.getTimeInMillis());
                        }

                        // Log.d("curTime", userDate.toString());
                        //Log.d("start", start.toString());
                        //Log.d("end", end.toString());


                        if (userDate.after(start) && userDate.before(end)) {
                            rowN.setBackgroundResource(R.color.colorhighlight);
                            tv1N.setTextColor(Color.parseColor("#ffffff"));
                            tv2N.setTextColor(Color.parseColor("#ffffff"));
                            // Log.d("current choghadiya",choghadiyo_night);
                            //Log.d("result", "falls between start and end , go to screen 1 ");
                        }
                        else{
                            if (i % 2 == 0) {
                                rowN.setBackgroundResource(R.color.colorAccent);
                            }
                            // Log.d("result", "does not fall between start and end , go to screen 2 ");
                        }






                        first_night_time=new_time_night;





                    }catch(SQLException sqle){

                        throw sqle;

                    }catch(Exception excmain){

                        throw excmain;

                    }

                }//====for loop ahi complite thaay che==





            }



        }catch(Exception cur_time){cur_time.printStackTrace();
        }



//[============================================================================================================================================
//-----------------------------------------------        ------------------------------------------------------------------------

        tv_date_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar newCalendar = Calendar.getInstance();
                final DatePickerDialog selectdate = new DatePickerDialog(Choghadiya.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                        Toast.makeText(Choghadiya.this,"If selected date difference 2 Month from NOW, so Choghdiya Time should be (+ or -) 5 minutes each choghdiya.",
                                Toast.LENGTH_LONG).show();

                        ll_1.removeAllViews();
                        ll_2.removeAllViews();
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        String new_selecteddate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newDate.getTime());

                        SimpleDateFormat simpledateformat_dateselect = new SimpleDateFormat("EEEE",Locale.getDefault());
                        Date date = new Date(year, monthOfYear, dayOfMonth-1);
                        String new_selectedday = simpledateformat_dateselect.format(date);



                        tv_date_day.setText(new_selecteddate+" "+new_selectedday);



                        String[] partation = one_day_choghadiya_duration.split(":");
                        int hours_Dpartion = Integer.parseInt(partation[0]);
                        int minutes_Dpartion = Integer.parseInt(partation[1]);
                        int seconds_DpartionH = Integer.parseInt(partation[2]);
                        //=======================

                        String[] partation_night = one_night_choghadiya_duration.split(":");
                        int hours_Npartion = Integer.parseInt(partation_night[0]);
                        int minutes_Npartion = Integer.parseInt(partation_night[1]);
                        int seconds_NpartionH = Integer.parseInt(partation_night[2]);


                        //===============================

                        String choghadiyo_day="";
                        String choghadiyo_night="";

                        String first_day_time=sun_riseTime;
                        first_day_time=first_day_time.substring(0,5);
                        first_day_time=first_day_time+" am";

                        //===for night========
                        String first_night_time=sun_setTime;
                        first_night_time=first_night_time.substring(0,5);
                        first_night_time=first_night_time+" pm";



                        for(int i=1;i<=8;i++){

                            try {

                                myDbHelper.openDataBase();

                                c=myDbHelper.query("choghadia_name",new String[]{"choghadiya_Type",
                                                "choghadiya_Day",
                                                "choghaidya_Count",
                                                "choghadiya_Name",} ,
                                        "choghadiya_Type=? and choghadiya_Day=? and choghaidya_Count=?" ,
                                        new String []{"day",new_selectedday,Integer.toString(i)}, null,null, null);
                                // Cursor c=myDbHelper.query("choghadia_name", null, null, null, null,null, null);

                                if(c.moveToFirst())
                                {
                                    do {

                                        choghadiyo_day = c.getString(3);
                     /* Log.d("Choghadiya",
                              "_id: " + c.getString(0) + "\n" +
                                      "E_NAME: " + c.getString(1) + "\n" +
                                      "E_AGE: " + c.getString(2) + "\n" +
                                      "E_DEPT:  " + c.getString(3) + "\n" +
                                      "E_DEPT4:  " + c.getString(4));
*/

                                    } while (c.moveToNext());

                                    myDbHelper.close();
                                }



                                Calendar calendar_day = Calendar.getInstance();
                                calendar_day.setTime(sdf_HMSa.parse(sun_riseTime));
                                calendar_day.add(Calendar.HOUR, hours_Dpartion * i);
                                calendar_day.add(Calendar.MINUTE, minutes_Dpartion * i);
                                String new_time = sdf_HMa.format(calendar_day.getTime());
                                //new_time = new_time.substring(0, 5);



                                TableRow row= new TableRow(Choghadiya.this);
                                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT);
                                //lp.setMargins(0,900,0,900);
                                row.setLayoutParams(lp);
                                row.setGravity(Gravity.CENTER);


                                TextView tv1 = new TextView(Choghadiya.this);
                                tv1.setText(first_day_time + "  -  " + new_time);
                                tv1.setTypeface(cFont, Typeface.BOLD);
                                tv1.setGravity(Gravity.CENTER);
                                tv1.setTextSize(19);



                                TextView tv2 = new TextView(Choghadiya.this);
                                tv2.setText(" " + choghadiyo_day);
                                tv2.setTypeface(cFont, Typeface.BOLD);
                                tv2.setGravity(Gravity.CENTER);
                                tv2.setTextSize(19);



                                row.addView(tv1);
                                row.addView(tv2);
                                ll_1.addView(row);


                                compareStringOne = first_day_time;
                                compareStringTwo = new_time;

                                dateCompareOne = sdf_HMa.parse(compareStringOne);
                                dateCompareTwo = sdf_HMa.parse(compareStringTwo);

                                    if (i % 2 == 0) {
                                        row.setBackgroundResource(R.color.colorAccent);
                                    }else{
                                        row.setBackgroundResource(R.color.cardview_light_background);
                                    }


                                first_day_time=new_time;


                                //====day compplited========================================================================================


                                myDbHelper.openDataBase();


                                // Cursor c=myDbHelper.query("choghadia_name", null, null, null, null,null, null);
                                c=myDbHelper.query("choghadia_name",new String[]{"choghadiya_Type",
                                                "choghadiya_Day","choghaidya_Count","choghadiya_Name"} ,
                                        "choghadiya_Type=? and choghadiya_Day=? and choghaidya_Count=?" ,
                                        new String []{"night",new_selectedday
                                                ,Integer.toString(i)}, null,null, null);

                                if(c.moveToFirst())
                                {
                                    do {

                                        choghadiyo_night = c.getString(3);
/*
                        Toast.makeText(Choghadiya.this,
                                "_id: " + c.getString(0) + "\n" +
                                        "E_NAME: " + c.getString(1) + "\n" +
                                        "E_AGE: " + c.getString(2) + "\n" +
                                        "E_DEPT:  " + c.getString(3) + "\n" +
                                        "E_DEPT4:  " + c.getString(4),
                                Toast.LENGTH_LONG).show();*/

                                    } while (c.moveToNext());

                                    myDbHelper.close();
                                }

                                String new_time_night="";
                                Calendar calendar_night = Calendar.getInstance();
                                calendar_night.setTime(sdf_HMSa.parse(sun_setTime));
                                //calendar_night.setTime(new SimpleDateFormat("dd/mm/yyyy hh:mm:ss aa").parse("1/1/2015 " + sun_setTime));
                                calendar_night.add(Calendar.HOUR, hours_Npartion * i);
                                calendar_night.add(Calendar.MINUTE, minutes_Npartion * i);
                                new_time_night= sdf_HMa.format(calendar_night.getTime());
                                //  Log.d("new time at ",Integer.toString(i)+"="+new_time_night);



                                //new_time = new_time.substring(0, 5);


                                TableRow rowN= new TableRow(Choghadiya.this);
                                TableRow.LayoutParams lpN = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT);
                                // lpN.setMargins(0,15,0,15);
                                rowN.setLayoutParams(lpN);
                                rowN.setGravity(Gravity.CENTER);


                                TextView tv1N = new TextView(Choghadiya.this);
                                tv1N.setText(first_night_time + "  -  " + new_time_night);
                                tv1N.setTypeface(cFont, Typeface.BOLD);
                                tv1N.setGravity(Gravity.CENTER);
                                tv1N.setTextSize(19);



                                TextView tv2N = new TextView(Choghadiya.this);
                                tv2N.setText(" " + choghadiyo_night);
                                tv2N.setTypeface(cFont, Typeface.BOLD);
                                tv2N.setGravity(Gravity.CENTER);
                                tv2N.setTextSize(19);



                                rowN.addView(tv1N);
                                rowN.addView(tv2N);
                                ll_2.addView(rowN);


                                compareStringOne = first_night_time;
                                compareStringTwo = new_time_night;





                                Date mToday = new Date();

                                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa",Locale.getDefault());
                                String curTime = sdf.format(mToday);
                                Date start = sdf.parse(compareStringOne);
                                Date end = sdf.parse(compareStringTwo);
                                Date userDate = sdf.parse(curTime);

                                    if (i % 2 == 0) {
                                        rowN.setBackgroundResource(R.color.colorAccent);
                                    }else{

                                        row.setBackgroundResource(R.color.cardview_light_background);
                                    }
                                    // Log.d("result", "does not fall between start and end , go to screen 2 ");







                                first_night_time=new_time_night;





                            }catch(SQLException sqle){

                                throw sqle;

                            }catch(Exception epx){

                                epx.printStackTrace();

                            }

                        }//====for loop ahi complite thaay che==








                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));



                selectdate.show();
            }
        });





//8888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888

    }//===========oncreat complete breaket




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(sharepref.getString("key_login","yes").equals("yes")){

            finish();
        }else{
            System.exit(0);
            finish();
        }
    }
}
