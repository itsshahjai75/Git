package com.jainisam.techno.jainisam;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.RemoteViews;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyWidgetProvider extends AppWidgetProvider {
//==============================================================================================


    SharedPreferences sharepref;

    Date date_current_hour;
    Date dateCompareOne;
    Date dateCompareTwo;

    String compareStringOne;
    String compareStringTwo;

    Date date_after_12_dateCompareOne;
    Date date_before_sunrise_dateCompareTwo;

    String after_12_compareStringOne ;
    String before_sunrise_compareStringTwo;

    String dayOfTheWeek,yesterDay;

    String SRtime,SStime,DLtime,NLtime,Dchoghdiya,Nchoghdiya;

    SimpleDateFormat sdf_HMa = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
    SimpleDateFormat sdf_HMSa = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());

    String one_day_choghadiya_duration,one_night_choghadiya_duration,sun_riseTime,sun_setTime;

//==============================================================================================


    final static String WIDGET_UPDATE_ACTION ="com.jainisam.techno.intent.action.UPDATE_WIDGET";
    DatabaseHelper_panchang myDbHelper_S;
    DatabaseHelper_panchang_deravasi myDbHelper_D;
    DatabaseHelper myDbHelper_cho;
    Cursor c1,c2,c3;


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {



        // initializing widget layout
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);

        // register for button event
        remoteViews.setOnClickPendingIntent(R.id.btn_sync,
                buildButtonPendingIntent(context));
 //===============================================================

        Intent choghdiya_intent = new Intent(context, Choghadiya.class);

        PendingIntent choghdiya_PendingIntent = PendingIntent.getActivity(context, 5000, choghdiya_intent, 0);

        remoteViews.setOnClickPendingIntent(R.id.tv_current_choghdiya, choghdiya_PendingIntent);


       //===================================================================

        Intent deravasi_intent = new Intent(context, Deravasi_panchang.class);

        PendingIntent deravasi_PendingIntent = PendingIntent.getActivity(context, 5001, deravasi_intent, 0);

        remoteViews.setOnClickPendingIntent(R.id.ll_deravasi, deravasi_PendingIntent);



//==================================================================================
        Intent sthankvasi_intent = new Intent(context, Punchang.class);

        PendingIntent sthankvasi_PendingIntent = PendingIntent.getActivity(context, 5002, sthankvasi_intent, 0);

        remoteViews.setOnClickPendingIntent(R.id.ll_sthnkvasi, sthankvasi_PendingIntent);


//==================================================================================
        Intent app_intent = new Intent(context, Splashscreeen.class);

        PendingIntent app_PendingIntent = PendingIntent.getActivity(context, 5003, app_intent, 0);

        remoteViews.setOnClickPendingIntent(R.id.app_icon_img, app_PendingIntent);



//==================================================================================


        // updating view with initial data
       /*  remoteViews.setTextViewText(R.id.tv_sthakvasi_tithi, getTitle());
        remoteViews.setTextViewText(R.id.tv_deravasi_tithi, getTitle());*/


        myDbHelper_S = new DatabaseHelper_panchang(context.getApplicationContext());
        myDbHelper_D = new DatabaseHelper_panchang_deravasi(context.getApplicationContext());
        myDbHelper_cho= new DatabaseHelper(context.getApplicationContext());


        try{

            SimpleDateFormat sdfCal_today = new SimpleDateFormat("dd-MM-yyyy");
           Calendar cal_today= Calendar.getInstance();
            String today= sdfCal_today.format(cal_today.getTime());
            remoteViews.setTextViewText(R.id.tv_title_widget, today+ " 's Tithi");

            myDbHelper_S.openDataBase();
           // String click_date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date.getDate());

            c1=myDbHelper_S.query("punchang_tithi_Db",new String[]{"punchang_id",
                            "date",
                            "day",
                            "tithi",
                            "paksh",
                            "month",
                            "note"} ,
                    "date=?",new String[]{today}, null,null, null);

            if(c1.moveToFirst()) {
                do {

                    String v_tithi="";
                    if(c1.getString(3).equalsIgnoreCase("30")){
                        v_tithi="15";

                    }else{v_tithi=c1.getString(3);}
                   /* showAlertDialog(Punchang.this,
                            c.getString(1)+"\t - "+c.getString(2),
                            c.getString(5)+"\t - \t"+c.getString(4)+"\t - \t"+v_tithi+"\n\n"+c.getString(6),false);
*/

                    remoteViews.setTextViewText(R.id.tv_sthakvasi_tithi,
                            c1.getString(5)+"\t - \t"+c1.getString(4)+"\t - \t"+v_tithi);




                } while (c1.moveToNext());


            }


            myDbHelper_S.close();

//===========================================================================================================

            myDbHelper_D.openDataBase();

            c2=myDbHelper_D.query("punchang_tithi_Db_deravasi",new String[]{"punchang_id",
                            "date",
                            "day",
                            "tithi",
                            "paksh",
                            "month",
                            "note"} ,
                    "date=?",new String[]{today}, null,null, null);

            if(c2.moveToFirst()) {
                do {

                    String v_tithi="";
                    if(c2.getString(3).equalsIgnoreCase("30")){
                        v_tithi="15";

                    }else{v_tithi=c2.getString(3);}
                    /*showAlertDialog(Deravasi_panchang.this,
                            c.getString(1) + "\t - " + c.getString(2),
                            c.getString(5) + "\t - \t" + c.getString(4) + "\t - \t" + v_tithi + "\n\n" + c.getString(6), false);
*/

                    remoteViews.setTextViewText(R.id.tv_deravasi_tithi,c2.getString(5)+"\t - \t"+c2.getString(4)+"\t - \t"+v_tithi);



                } while (c2.moveToNext());
            }

            myDbHelper_D.close();




        }catch(SQLException sqle){

            throw sqle;

        }catch(Exception excmain){

            excmain.printStackTrace();
        }
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


        SimpleDateFormat sdfCal = new SimpleDateFormat("EEEE");
        Calendar calmain= Calendar.getInstance();
        calmain.add(Calendar.DAY_OF_WEEK,-1);
        Date d_yessterday = calmain.getTime();
        yesterDay = sdfCal.format(d_yessterday);

        Date d_today = new Date();
        dayOfTheWeek = sdfCal.format(d_today);
        // Log.d("TOday Day==",dayOfTheWeek);

        sharepref = context.getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);


        if(sharepref.getString("sunriseTime", null).toString().isEmpty()
                || sharepref.getString("sunriseTime", null).toString().equalsIgnoreCase("null")){

            Toast.makeText(context.getApplicationContext(),"Please open application First once !",Toast.LENGTH_LONG).show();
        }else {

            SRtime = sharepref.getString("sunriseTime", null).toString();
            SStime = sharepref.getString("sunsetTime", null).toString();
            DLtime = sharepref.getString("dayLength", null).toString();
            NLtime = sharepref.getString("nightLength", null).toString();
            Dchoghdiya = sharepref.getString("daychoghdiyaDuration", null).toString();
            Nchoghdiya = sharepref.getString("nightchoghdiyaDuration", null).toString();


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

            } catch (Exception date_choghadiya) {
                date_choghadiya.printStackTrace();
            }

            after_12_compareStringOne = "12:01 am";
            before_sunrise_compareStringTwo = sun_riseTime;

            //==geting current Hour===
            Calendar now = Calendar.getInstance();

            int hour = now.get(Calendar.HOUR);
            int minute = now.get(Calendar.MINUTE);
            int AM_PM = now.get(Calendar.AM_PM);
            String am_pm;
            if (AM_PM == 0) {
                am_pm = "am";
            } else {
                am_pm = "pm";
            }

            //Log.d("current Hr",am_pm);
            try {
                date_current_hour = sdf_HMa.parse(hour + ":" + minute + " " + am_pm);
                date_after_12_dateCompareOne = sdf_HMa.parse(after_12_compareStringOne);
                date_before_sunrise_dateCompareTwo = sdf_HMSa.parse(before_sunrise_compareStringTwo);

//########################################################################################################################################
                if (date_after_12_dateCompareOne.before(date_current_hour) && date_before_sunrise_dateCompareTwo.after(date_current_hour)) {


                    SimpleDateFormat sdfCal_datday = new SimpleDateFormat("dd/MMM/yyyy EEEE", Locale.getDefault());
                    Calendar cal_datday = Calendar.getInstance();
                    cal_datday.add(Calendar.DATE, -1);
                    // tv_date_day.setText(sdfCal_datday.format(cal_datday.getTime()));

                    //  Log.d("Time 12:00Am to SunRise",sdf_HMa.format(date_current_hour));
                    //==ahiya old choghdiyaj batava na che sun rise time sudhi====

                    dayOfTheWeek = yesterDay;

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

                    String choghadiyo_day = "";
                    String choghadiyo_night = "";

                    String first_day_time = sun_riseTime;
                    first_day_time = first_day_time.substring(0, 5);
                    first_day_time = first_day_time + " am";

                    //===for night========
                    String first_night_time = sun_setTime;
                    first_night_time = first_night_time.substring(0, 5);
                    first_night_time = first_night_time + " pm";


                    for (int i = 1; i <= 8; i++) {

                        try {

                            myDbHelper_cho.openDataBase();

                            c3 = myDbHelper_cho.query("choghadia_name", new String[]{"choghadiya_Type",
                                            "choghadiya_Day",
                                            "choghaidya_Count",
                                            "choghadiya_Name",},
                                    "choghadiya_Type=? and choghadiya_Day=? and choghaidya_Count=?",
                                    new String[]{"day", dayOfTheWeek, Integer.toString(i)}, null, null, null);
                            // Cursor c=myDbHelper.query("choghadia_name", null, null, null, null,null, null);

                            if (c3.moveToFirst()) {
                                do {

                                    choghadiyo_day = c3.getString(3);
                     /* Log.d("Choghadiya",
                              "_id: " + c.getString(0) + "\n" +
                                      "E_NAME: " + c.getString(1) + "\n" +
                                      "E_AGE: " + c.getString(2) + "\n" +
                                      "E_DEPT:  " + c.getString(3) + "\n" +
                                      "E_DEPT4:  " + c.getString(4));
*/

                                } while (c3.moveToNext());

                                myDbHelper_cho.close();
                            }


                            Calendar calendar_day = Calendar.getInstance();
                            calendar_day.setTime(sdf_HMSa.parse(sun_riseTime));
                            calendar_day.add(Calendar.HOUR, hours_Dpartion * i);
                            calendar_day.add(Calendar.MINUTE, minutes_Dpartion * i);
                            String new_time = sdf_HMa.format(calendar_day.getTime());
                            //new_time = new_time.substring(0, 5);


                            compareStringOne = first_day_time;
                            compareStringTwo = new_time;

                            dateCompareOne = sdf_HMa.parse(compareStringOne);
                            dateCompareTwo = sdf_HMa.parse(compareStringTwo);

                            if (dateCompareOne.before(date_current_hour) && dateCompareTwo.after(date_current_hour)) {
                                //yada yada
                            /*row.setBackgroundResource(R.color.colorhighlight);
                            tv1.setTextColor(Color.parseColor("#ffffff"));
                            tv2.setTextColor(Color.parseColor("#ffffff"));*/

                                //   Log.d("current choghadiya",choghadiyo_day);

                                remoteViews.setTextViewText(R.id.tv_current_choghdiya, " " + first_day_time + "  -  " + new_time + "  " + choghadiyo_day);


                            } else {
                            /*if (i % 2 == 0) {
                                row.setBackgroundResource(R.color.colorAccent);
                            }*/
                            }


                            first_day_time = new_time;


                            //====day compplited========================================================================================


                            myDbHelper_cho.openDataBase();


                            // Cursor c=myDbHelper.query("choghadia_name", null, null, null, null,null, null);
                            c3 = myDbHelper_cho.query("choghadia_name", new String[]{"choghadiya_Type",
                                            "choghadiya_Day", "choghaidya_Count", "choghadiya_Name"},
                                    "choghadiya_Type=? and choghadiya_Day=? and choghaidya_Count=?",
                                    new String[]{"night", dayOfTheWeek, Integer.toString(i)}, null, null, null);

                            if (c3.moveToFirst()) {
                                do {

                                    choghadiyo_night = c3.getString(3);
/*
                        Toast.makeText(Choghadiya.this,
                                "_id: " + c.getString(0) + "\n" +
                                        "E_NAME: " + c.getString(1) + "\n" +
                                        "E_AGE: " + c.getString(2) + "\n" +
                                        "E_DEPT:  " + c.getString(3) + "\n" +
                                        "E_DEPT4:  " + c.getString(4),
                                Toast.LENGTH_LONG).show();*/

                                } while (c3.moveToNext());

                                myDbHelper_cho.close();
                            }

                            String new_time_night = "";
                            Calendar calendar_night = Calendar.getInstance();
                            calendar_night.setTime(sdf_HMSa.parse(sun_setTime));
                            //calendar_night.setTime(new SimpleDateFormat("dd/mm/yyyy hh:mm:ss aa").parse("1/1/2015 " + sun_setTime));
                            calendar_night.add(Calendar.HOUR, hours_Npartion * i);
                            calendar_night.add(Calendar.MINUTE, minutes_Npartion * i);
                            new_time_night = sdf_HMa.format(calendar_night.getTime());
                            // Log.d("new time at ",Integer.toString(i)+"="+new_time_night);


                            //new_time = new_time.substring(0, 5);


                            compareStringOne = first_night_time;
                            compareStringTwo = new_time_night;


                            Date mToday = new Date();

                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                            String curTime = sdf.format(mToday);
                            Date start = sdf.parse(compareStringOne);
                            Date end = sdf.parse(compareStringTwo);
                            Date userDate = sdf.parse(curTime);

                            if (end.before(start)) {
                                Calendar mCal = Calendar.getInstance();
                                mCal.setTime(end);
                                mCal.add(Calendar.DAY_OF_YEAR, 1);
                                end.setTime(mCal.getTimeInMillis());
                            }

                            // Log.d("curTime", userDate.toString());
                            //Log.d("start", start.toString());
                            //Log.d("end", end.toString());


                            if (userDate.after(start) && userDate.before(end)) {
                            /*rowN.setBackgroundResource(R.color.colorhighlight);
                            tv1N.setTextColor(Color.parseColor("#ffffff"));
                            tv2N.setTextColor(Color.parseColor("#ffffff"));*/
                                //Log.d("current choghadiya",choghadiyo_night);
                                //Log.d("result", "falls between start and end , go to screen 1 ");

                                remoteViews.setTextViewText(R.id.tv_current_choghdiya, " " + first_night_time + "  -  " + new_time_night + "  " + choghadiyo_night);


                            } else {
                                //Log.d("result", "does not fall between start and end , go to screen 2 ");
                            }


                            first_night_time = new_time_night;


                        } catch (SQLException sqle) {

                            throw sqle;

                        } catch (Exception excmain) {

                            throw excmain;

                        }

                    }//====for loop ahi complite thaay che==

                    //======================================================================
                    //=========================================================================================================
                    //====================================================================================================================
                } else {
                    //===ahiya today vala choghdiya dekhadvana che.

                    //Log.d("sunrise to 12:00 am",sdf_HMa.format(date_current_hour));


                    SimpleDateFormat sdfCal_datday = new SimpleDateFormat("dd/MMM/yyyy EEEE", Locale.getDefault());
                    Calendar cal_datday = Calendar.getInstance();
                    //tv_date_day.setText(sdfCal_datday.format(cal_datday.getTime()));


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

                    String choghadiyo_day = "";
                    String choghadiyo_night = "";

                    String first_day_time = sun_riseTime;
                    first_day_time = first_day_time.substring(0, 5);
                    first_day_time = first_day_time + " am";

                    //===for night========
                    String first_night_time = sun_setTime;
                    first_night_time = first_night_time.substring(0, 5);
                    first_night_time = first_night_time + " pm";


                    for (int i = 1; i <= 8; i++) {

                        try {

                            myDbHelper_cho.openDataBase();

                            c3 = myDbHelper_cho.query("choghadia_name", new String[]{"choghadiya_Type",
                                            "choghadiya_Day",
                                            "choghaidya_Count",
                                            "choghadiya_Name",},
                                    "choghadiya_Type=? and choghadiya_Day=? and choghaidya_Count=?",
                                    new String[]{"day", dayOfTheWeek, Integer.toString(i)}, null, null, null);
                            // Cursor c=myDbHelper.query("choghadia_name", null, null, null, null,null, null);

                            if (c3.moveToFirst()) {
                                do {

                                    choghadiyo_day = c3.getString(3);
                     /* Log.d("Choghadiya",
                              "_id: " + c.getString(0) + "\n" +
                                      "E_NAME: " + c.getString(1) + "\n" +
                                      "E_AGE: " + c.getString(2) + "\n" +
                                      "E_DEPT:  " + c.getString(3) + "\n" +
                                      "E_DEPT4:  " + c.getString(4));
*/

                                } while (c3.moveToNext());

                                myDbHelper_cho.close();
                            }


                            Calendar calendar_day = Calendar.getInstance();
                            calendar_day.setTime(sdf_HMSa.parse(sun_riseTime));
                            calendar_day.add(Calendar.HOUR, hours_Dpartion * i);
                            calendar_day.add(Calendar.MINUTE, minutes_Dpartion * i);
                            String new_time = sdf_HMa.format(calendar_day.getTime());
                            //new_time = new_time.substring(0, 5);


                            compareStringOne = first_day_time;
                            compareStringTwo = new_time;

                            dateCompareOne = sdf_HMa.parse(compareStringOne);
                            dateCompareTwo = sdf_HMa.parse(compareStringTwo);

                            if (dateCompareOne.before(date_current_hour) && dateCompareTwo.after(date_current_hour)) {
                                //yada yada
                            /*row.setBackgroundResource(R.color.colorhighlight);
                            tv1.setTextColor(Color.parseColor("#ffffff"));
                            tv2.setTextColor(Color.parseColor("#ffffff"));*/

                                remoteViews.setTextViewText(R.id.tv_current_choghdiya, " " + first_day_time + "  -  " + new_time + "  " + choghadiyo_day);


                                // Log.d("current choghadiya",choghadiyo_day);
                            } else {
                            /*if (i % 2 == 0) {
                                row.setBackgroundResource(R.color.colorAccent);
                            }*/
                            }


                            first_day_time = new_time;


                            //====day compplited========================================================================================


                            myDbHelper_cho.openDataBase();


                            // Cursor c=myDbHelper.query("choghadia_name", null, null, null, null,null, null);
                            c3 = myDbHelper_cho.query("choghadia_name", new String[]{"choghadiya_Type",
                                            "choghadiya_Day", "choghaidya_Count", "choghadiya_Name"},
                                    "choghadiya_Type=? and choghadiya_Day=? and choghaidya_Count=?",
                                    new String[]{"night", dayOfTheWeek, Integer.toString(i)}, null, null, null);

                            if (c3.moveToFirst()) {
                                do {

                                    choghadiyo_night = c3.getString(3);
/*
                        Toast.makeText(Choghadiya.this,
                                "_id: " + c.getString(0) + "\n" +
                                        "E_NAME: " + c.getString(1) + "\n" +
                                        "E_AGE: " + c.getString(2) + "\n" +
                                        "E_DEPT:  " + c.getString(3) + "\n" +
                                        "E_DEPT4:  " + c.getString(4),
                                Toast.LENGTH_LONG).show();*/

                                } while (c3.moveToNext());

                                myDbHelper_cho.close();
                            }

                            String new_time_night = "";
                            Calendar calendar_night = Calendar.getInstance();
                            calendar_night.setTime(sdf_HMSa.parse(sun_setTime));
                            //calendar_night.setTime(new SimpleDateFormat("dd/mm/yyyy hh:mm:ss aa").parse("1/1/2015 " + sun_setTime));
                            calendar_night.add(Calendar.HOUR, hours_Npartion * i);
                            calendar_night.add(Calendar.MINUTE, minutes_Npartion * i);
                            new_time_night = sdf_HMa.format(calendar_night.getTime());
                            //  Log.d("new time at ",Integer.toString(i)+"="+new_time_night);


                            //new_time = new_time.substring(0, 5);


                            compareStringOne = first_night_time;
                            compareStringTwo = new_time_night;



                        /*dateCompareOne = sdf_HMa.parse(compareStringOne);
                        dateCompareTwo = sdf_HMa.parse(compareStringTwo);

                        if (dateCompareOne.before(date_current_hour) && dateCompareTwo.after(date_current_hour)) {
                            //yada yada
                         *//*   rowN.setBackgroundResource(R.color.colorhighlight);
                            tv1N.setTextColor(Color.parseColor("#ffffff"));
                            tv2N.setTextColor(Color.parseColor("#ffffff"));*//*


      remoteViews.setTextViewText(R.id.tv_current_choghdiya,"\t"+first_day_time + "  -  " + new_time+"\t"+choghadiyo_night);

                            //Log.d("current choghadiya",choghadiyo_night);
                        } else {
                            *//*if (i % 2 == 0) {
                                rowN.setBackgroundResource(R.color.colorAccent);
                            }*//*
                        }*/


                            Date mToday = new Date();

                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                            String curTime = sdf.format(mToday);
                            Date start = sdf.parse(compareStringOne);
                            Date end = sdf.parse(compareStringTwo);
                            Date userDate = sdf.parse(curTime);

                            if (end.before(start)) {
                                Calendar mCal = Calendar.getInstance();
                                mCal.setTime(end);
                                mCal.add(Calendar.DAY_OF_YEAR, 1);
                                end.setTime(mCal.getTimeInMillis());
                            }

                            // Log.d("curTime", userDate.toString());
                            //Log.d("start", start.toString());
                            //Log.d("end", end.toString());


                            if (userDate.after(start) && userDate.before(end)) {
                            /*rowN.setBackgroundResource(R.color.colorhighlight);
                            tv1N.setTextColor(Color.parseColor("#ffffff"));
                            tv2N.setTextColor(Color.parseColor("#ffffff"));*/

                                remoteViews.setTextViewText(R.id.tv_current_choghdiya, " "
                                        + first_night_time + "  -  " + new_time_night + "  " + choghadiyo_night);

                                // Log.d("current choghadiya",choghadiyo_night);
                                //Log.d("result", "falls between start and end , go to screen 1 ");
                            } else {
                            /*if (i % 2 == 0) {
                                rowN.setBackgroundResource(R.color.colorAccent);
                            }*/
                                // Log.d("result", "does not fall between start and end , go to screen 2 ");
                            }


                            first_night_time = new_time_night;


                        } catch (SQLException sqle) {

                            throw sqle;

                        } catch (Exception excmain) {

                            throw excmain;

                        }

                    }//====for loop ahi complite thaay che==


                }


            } catch (Exception cur_time) {
                cur_time.printStackTrace();
            }


        }


//****************************************************************************************************
//====================================================================================================
        // request for widget update
        pushWidgetUpdate(context, remoteViews);
//====================================================================================================
    }

    public static PendingIntent buildButtonPendingIntent(Context context) {
        ++MyWidgetIntentReceiver.clickCount;

        // initiate widget update request
        Intent intent = new Intent();
        intent.setAction(WIDGET_UPDATE_ACTION);
        return PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static CharSequence getDesc() {
        return "Sync to see some of our funniest joke collections";
    }

    private static CharSequence getTitle() {
        return "Funny Jokes";
    }

    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context,
                MyWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }
}
