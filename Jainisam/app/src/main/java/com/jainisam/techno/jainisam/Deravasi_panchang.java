package com.jainisam.techno.jainisam;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Deravasi_panchang extends AppCompatActivity {
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    MaterialCalendarView calendarView;

    DatabaseHelper_panchang_deravasi myDbHelper;
    Cursor c;

    ArrayList<CalendarDay> Ddates_sudpakhi ;
    ArrayList<CalendarDay> Ddates_aatham;
    ArrayList<CalendarDay> Ddates_pacham;
    ArrayList<CalendarDay> Ddates_bij;
    ArrayList<CalendarDay> Ddates_paryushan;
    ArrayList<CalendarDay> Ddates_aayumbilodi;
    ArrayList<CalendarDay> Ddates_shubhdivas;
    ArrayList<CalendarDay> Ddates_extra_days;


    SharedPreferences sharepref;

    Long time,time2;
    AlarmManager alarmManager;

    Date today_date;


    private FloatingActionButton fab_paakhi,fab_aatham;
    private FloatingActionButton fab_pacham,fab_bij;
    SimpleDateFormat sdftithi;
    Calendar cal_today_sudpakhi,cal_today_vadPaakhi,cal_today_aatham,cal_today_pacham,cal_today_bij;
    Calendar cal_datday;
    MyTextView tv_date_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deravasi_panchang);

        sharepref =getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myDbHelper = new DatabaseHelper_panchang_deravasi(Deravasi_panchang.this);

        sdftithi = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());

        tv_date_day =(MyTextView)this.findViewById(R.id.tv_today_date_deravasi);
        final SimpleDateFormat sdfCal_datday = new SimpleDateFormat("dd/MMM/yyyy EEEE",Locale.getDefault());
        cal_datday= Calendar.getInstance();
        tv_date_day.setText(sdfCal_datday.format(cal_datday.getTime()));

        today_date=cal_datday.getTime();

/*

Geting tday date

        SimpleDateFormat sdfCal_today = new SimpleDateFormat("dd/MM/yyyy");
        cal_today= Calendar.getInstance();
       String today= sdfCal_today.format(cal_today.getTime());
*/





        //Initialize CustomCalendarView from layout
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView_deravasi);

//Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());
        calendarView.setCurrentDate(currentCalendar);
        calendarView.setSelectedDate(currentCalendar);




//Show Monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.SUNDAY);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {

                //Toast.makeText(Deravasi_panchang.this, FORMATTER.format(date.getDate()), Toast.LENGTH_LONG).show();

                try{

                    myDbHelper.openDataBase();
                    String click_date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date.getDate());

                    c=myDbHelper.query("punchang_tithi_Db_deravasi",new String[]{"punchang_id",
                                    "date",
                                    "day",
                                    "tithi",
                                    "paksh",
                                    "month",
                                    "note"} ,
                            "date=?",new String[]{click_date}, null,null, null);

                    if(c.moveToFirst()) {
                        do {

                            String v_tithi="";
                            if(c.getString(3).equalsIgnoreCase("30")){
                                v_tithi="15";

                            }else{v_tithi=c.getString(3);}
                            showAlertDialog(Deravasi_panchang.this,
                                    c.getString(1)+"\t - "+c.getString(2),
                                    c.getString(5)+"\t - \t"+c.getString(4)+"\t - \t"+v_tithi+"\n\n"+c.getString(6),false);




                        } while (c.moveToNext());
                    }

                    myDbHelper.close();


                }catch(SQLException sqle){

                    throw sqle;

                }catch(Exception excmain){

                    excmain.printStackTrace();
                }




            }
        });


//==========================================================================================================================================


        try {

            myDbHelper.openDataBase();


            c=myDbHelper.query("punchang_tithi_Db_deravasi",new String[]{"punchang_id",
                            "date",
                            "day",
                            "tithi",
                            "paksh",
                            "month",
                            "note"} ,
                    null ,null, null,null, null);
            // Cursor c=myDbHelper.query("choghadia_name", null, null, null, null,null, null);
            Ddates_sudpakhi = new ArrayList<>();
            Ddates_aatham = new ArrayList<>();
            Ddates_pacham = new ArrayList<>();
            Ddates_bij = new ArrayList<>();
            Ddates_paryushan=new ArrayList<>();
            Ddates_aayumbilodi=new ArrayList<>();
            Ddates_shubhdivas=new ArrayList<>();
            Ddates_extra_days=new ArrayList<>();
            if(c.moveToFirst()) {
                do {

                    String str_date = c.getString(1);
                    Date db_date=  new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault()).parse(str_date);
                    str_date=new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault()).format(db_date);
                     /* Log.d("tithi",
                              "_id: " + c.getString(0) + "\n" +
                                      "E_NAME: " + c.getString(1) + "\n" +
                                      "E_AGE: " + c.getString(2) + "\n" +
                                      "E_DEPT:  " + c.getString(3) + "\n" +
                                      "E_DEPT4:  " + c.getString(4)+"\n" +
                                      "E_DEPT4:  " + c.getString(5)+"\n" +
                                      "E_DEPT4:  " + c.getString(6));*/



                    Calendar calendar_db = Calendar.getInstance();
                    // CalendarDay day_db = CalendarDay.from(calendar_db);
                    // Log.d("dates==tithi", c.getString(3));
                    //  Toast.makeText(Deravasi_panchang.this,"Thiti are==="+c.getString(3),Toast.LENGTH_LONG).show();

                    String today_dateDay= tv_date_day.getText().toString();
                    String tithi=c.getString(3).toString();

                    if(tithi.equalsIgnoreCase("30")){
                        tithi="15";

                    }


                    String only_date=new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault()).format(today_date);
                    if(str_date.equalsIgnoreCase(only_date)) {
                        tv_date_day.setText(today_dateDay + "( " + c.getString(5) + " - " + c.getString(4) + " - " + tithi+")");
                    }

                    //==for sud paakhi===
                    if(c.getString(3).equalsIgnoreCase("14")) {
                        //Log.d("punam tithi", str_date);

                        String date1=str_date.substring(0, 2);
                        String month=str_date.substring(3, 5);
                        String year=str_date.substring(6);
                        //Log.d("daye ",date1+"/"+month+"/"+year);


                        calendar_db.set(Calendar.DATE, Integer.parseInt(date1));
                        calendar_db.set(Calendar.MONTH,Integer.parseInt(month)-1);
                        calendar_db.set(Calendar.YEAR,Integer.parseInt(year));


                        CalendarDay day = CalendarDay.from(calendar_db);
                        Ddates_sudpakhi.add(day);
                    }
                   /* //======vad pakhi find
                    if(c.getString(3).equalsIgnoreCase("30")) {
                        //Log.d("vad pakhi", str_date);

                        String date1=str_date.substring(0, 2);
                        String month=str_date.substring(3, 5);
                        String year=str_date.substring(6);
                        //Log.d("daye ",date1+"/"+month+"/"+year);


                        calendar_db.set(Calendar.DATE, Integer.parseInt(date1));
                        calendar_db.set(Calendar.MONTH,Integer.parseInt(month)-1);
                        calendar_db.set(Calendar.YEAR,Integer.parseInt(year));


                        CalendarDay day = CalendarDay.from(calendar_db);
                        Ddates_vadpakhi.add(day);*//*
                    }*/
                    //======aatham find
                    if(c.getString(3).equalsIgnoreCase("8")) {
                        //Log.d("aatham", str_date);

                        String date1=str_date.substring(0, 2);
                        String month=str_date.substring(3, 5);
                        String year=str_date.substring(6);
                        //Log.d("daye ",date1+"/"+month+"/"+year);


                        calendar_db.set(Calendar.DATE, Integer.parseInt(date1));
                        calendar_db.set(Calendar.MONTH,Integer.parseInt(month)-1);
                        calendar_db.set(Calendar.YEAR,Integer.parseInt(year));


                        CalendarDay day = CalendarDay.from(calendar_db);
                        Ddates_aatham.add(day);
                    }


                    //======pacham find
                    if(c.getString(3).equalsIgnoreCase("5")) {
                        //Log.d("pacham", str_date);

                        String date1=str_date.substring(0, 2);
                        String month=str_date.substring(3, 5);
                        String year=str_date.substring(6);
                        // Log.d("daye ",date1+"/"+month+"/"+year);


                        calendar_db.set(Calendar.DATE, Integer.parseInt(date1));
                        calendar_db.set(Calendar.MONTH,Integer.parseInt(month)-1);
                        calendar_db.set(Calendar.YEAR,Integer.parseInt(year));


                        CalendarDay day = CalendarDay.from(calendar_db);
                        Ddates_pacham.add(day);
                    }


                    //======bij find
                    if(c.getString(3).equalsIgnoreCase("2")) {
                        //Log.d("bij", str_date);

                        String date1=str_date.substring(0, 2);
                        String month=str_date.substring(3, 5);
                        String year=str_date.substring(6);
                        //Log.d("daye ",date1+"/"+month+"/"+year);


                        calendar_db.set(Calendar.DATE, Integer.parseInt(date1));
                        calendar_db.set(Calendar.MONTH,Integer.parseInt(month)-1);
                        calendar_db.set(Calendar.YEAR,Integer.parseInt(year));


                        CalendarDay day = CalendarDay.from(calendar_db);
                        Ddates_bij.add(day);
                    }



                    //======paryushan===
                    if(c.getString(6)!=null&&c.getString(6).equalsIgnoreCase("parushan")) {
                        //Log.d("paryushan", str_date);

                        String date1=str_date.substring(0, 2);
                        String month=str_date.substring(3, 5);
                        String year=str_date.substring(6);
                        //Log.d("daye ",date1+"/"+month+"/"+year);


                        calendar_db.set(Calendar.DATE, Integer.parseInt(date1));
                        calendar_db.set(Calendar.MONTH,Integer.parseInt(month)-1);
                        calendar_db.set(Calendar.YEAR,Integer.parseInt(year));


                        CalendarDay day = CalendarDay.from(calendar_db);
                        Ddates_paryushan.add(day);
                    }


                    //======aayubil===
                    if(c.getString(6)!=null&&c.getString(6).equalsIgnoreCase("aayumbilodi")) {
                        //Log.d("aayumbil", str_date);

                        String date1=str_date.substring(0, 2);
                        String month=str_date.substring(3, 5);
                        String year=str_date.substring(6);
                        //Log.d("daye ",date1+"/"+month+"/"+year);


                        calendar_db.set(Calendar.DATE, Integer.parseInt(date1));
                        calendar_db.set(Calendar.MONTH,Integer.parseInt(month)-1);
                        calendar_db.set(Calendar.YEAR,Integer.parseInt(year));


                        CalendarDay day = CalendarDay.from(calendar_db);
                        Ddates_aayumbilodi.add(day);
                    }

                    //======shubh divas===
                    if(c.getString(6)!=null&&c.getString(6).equalsIgnoreCase("shubh divas")) {
                        //Log.d("aayumbil", str_date);

                        String date1=str_date.substring(0, 2);
                        String month=str_date.substring(3, 5);
                        String year=str_date.substring(6);
                        //Log.d("daye ",date1+"/"+month+"/"+year);


                        calendar_db.set(Calendar.DATE, Integer.parseInt(date1));
                        calendar_db.set(Calendar.MONTH,Integer.parseInt(month)-1);
                        calendar_db.set(Calendar.YEAR,Integer.parseInt(year));


                        CalendarDay day = CalendarDay.from(calendar_db);
                        Ddates_shubhdivas.add(day);
                    }

                    //======extra days===

                    Calendar calendar_dbD = Calendar.getInstance();
                    if(c.getString(6)!=null
                            && !c.getString(6).isEmpty()
                            && !c.getString(6).equalsIgnoreCase("shubh divas")
                            &&!c.getString(6).equalsIgnoreCase("aayumbilodi")
                            &&!c.getString(6).equalsIgnoreCase("null")
                            &&!c.getString(6).equalsIgnoreCase("parushan")) {
                        //Log.d("extra dysa", c.getString(6));

                        String date1d=str_date.substring(0, 2);
                        String monthd=str_date.substring(3, 5);
                        String yeard=str_date.substring(6);
                        //Log.d("daye ",date1d+"/"+monthd+"/"+yeard);


                        calendar_dbD.set(Calendar.DATE, Integer.parseInt(date1d));
                        calendar_dbD.set(Calendar.MONTH,Integer.parseInt(monthd)-1);
                        calendar_dbD.set(Calendar.YEAR,Integer.parseInt(yeard));


                        CalendarDay dayd = CalendarDay.from(calendar_dbD);
                        Ddates_extra_days.add(dayd);
                    }




                } while (c.moveToNext());


                //calendarView.addDecorator(new EventDecorator(Color.GREEN, dates_sudpakhi));
                calendarView.addDecorators(
                        new EventDecorator_Dot(Color.parseColor("#388E3C"), Ddates_sudpakhi),/*,
                        new EventDecorator_Dot(Color.parseColor("#388E3C"),Ddates_vadpakhi),*/
                        new EventDecorator_Dot(Color.parseColor("#FFA000"), Ddates_aatham),
                        new EventDecorator_Dot(Color.parseColor("#5D4037"), Ddates_pacham),
                        new EventDecorator_Dot(Color.parseColor("#C2185B"), Ddates_bij),
                        new EventDecorator(Color.parseColor("#FFEBAB"), Ddates_paryushan),
                        new EventDecorator(Color.parseColor("#FFEBAB"), Ddates_aayumbilodi),
                        new EventDecorator(Color.parseColor("#99FF0000"),Ddates_shubhdivas),
                        new EventDecorator(Color.parseColor("#99cccccc"), Ddates_extra_days));
                //new EventDecorator_SpecialDay(Color.parseColor("#CDDC39"), dates_aayumbilodi,Deravasi_panchang.this));
            }

            myDbHelper.close();


        }catch(SQLException sqle){

            throw sqle;

        }catch(Exception excmain){

            excmain.printStackTrace();
        }

//====================================================================================================================================
        //=================================================================================================================================

        final FloatingActionMenu menu1 = (FloatingActionMenu)this.findViewById(R.id.menu_tithi_deravasi);

        alarmManager = (AlarmManager)Deravasi_panchang.this.getSystemService(Context.ALARM_SERVICE);


        fab_paakhi = (FloatingActionButton)this.findViewById(R.id.fab_paakhi_deravasi);
        fab_aatham = (FloatingActionButton)this.findViewById(R.id.fab_Aatham_deravasi);
        fab_pacham = (FloatingActionButton)this.findViewById(R.id.fab_pacham_deravasi);
        fab_bij = (FloatingActionButton)this.findViewById(R.id.fab_bij_deravasi);


        menu1.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(getActivity(), menu1.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                if((sharepref.getString("chaudas","no")).equalsIgnoreCase("yes")){
                    fab_paakhi.setImageResource(R.drawable.checked);
                }else {
                    fab_paakhi.setImageResource(R.drawable.uncheceked);
                }
                if(sharepref.getString("aatham","no").equalsIgnoreCase("yes")){
                    fab_aatham.setImageResource(R.drawable.checked);
                }else {
                    fab_aatham.setImageResource(R.drawable.uncheceked);
                }
                if(sharepref.getString("pacham","no").equalsIgnoreCase("yes")){
                    fab_pacham.setImageResource(R.drawable.checked);
                }else {
                    fab_pacham.setImageResource(R.drawable.uncheceked);
                }
                if(sharepref.getString("bij","no").equalsIgnoreCase("yes")){
                    fab_bij.setImageResource(R.drawable.checked);
                }else {
                    fab_bij.setImageResource(R.drawable.uncheceked);
                }


                if (menu1.isOpened()) {

                }

                menu1.toggle(true);
            }
        });
        menu1.setClosedOnTouchOutside(true);


        //fab1.setEnabled(false);



        fab_paakhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PendingIntent operation5=null;
                if(!(sharepref.getString("chaudas","no")).equalsIgnoreCase("yes")){
                    fab_paakhi.setImageResource(R.drawable.checked);
                    sharepref.edit().putString("chaudas", "yes").commit();

                    try{

                        for(int k=0;k<Ddates_sudpakhi.size();k++){

                            String iDate=Ddates_sudpakhi.get(k).toString();
                            iDate=iDate.replace("CalendarDay","");
                            iDate=iDate.replace("{","");
                            iDate=iDate.replace("}","");
                            iDate=iDate.replace("-","/");
                           // Log.d("Array strig",iDate);

                            int year=Integer.parseInt(iDate.substring(0, 4));

                            String str_month=iDate.substring(iDate.indexOf("/")+1);
                            str_month=str_month.substring(0, str_month.indexOf("/"));

                            int month=Integer.parseInt(str_month);
                            if(month==0){
                                month=1;
                            }else{
                                month=month+1;
                            }

                            String str_day=iDate.substring(iDate.indexOf("/")+1);
                            str_day=str_day.substring(str_day.indexOf("/")+1);
                            int dayofmonth=Integer.parseInt(str_day);




                            //Log.d("Array dat integer", Integer.toString(year) + Integer.toString(month) + Integer.toString(dayofmonth));


                            cal_today_sudpakhi= Calendar.getInstance();
                            cal_today_sudpakhi.set(year,month-1,dayofmonth,00,01,00);
                            time = cal_today_sudpakhi.getTimeInMillis();

                            if (cal_today_sudpakhi.getTime().after(today_date)){

                                Intent intentAlarm = new Intent(Deravasi_panchang.this, AlarmManagerBroadcastReceiver.class);

                                intentAlarm.putExtra("title","Today is Chaudas");
                                intentAlarm.putExtra("msg1","Hello,Today is Chaudas keep in mind please...");
                                intentAlarm.putExtra("msg2","");
                                intentAlarm.putExtra("msg3","deravasi");
                                /** Creating a Pending Intent */
                                operation5 = PendingIntent.getBroadcast(Deravasi_panchang.this, 12*10 + k, intentAlarm, 0);


                                //set the alarm for particular time
                                alarmManager.set(AlarmManager.RTC_WAKEUP, time, operation5);
                              //  System.out.println("pakki alarms times are====" + cal_today_sudpakhi.getTime());
                            } else {

                                //System.out.println("pakhi misses are=" + cal_today_sudpakhi.getTime());

                            }



                        }



                        /*for(int k2=0;k2<Ddates_vadpakhi.size();k2++){

                            String iDate2=Ddates_vadpakhi.get(k2).toString();
                            iDate2=iDate2.replace("CalendarDay","");
                            iDate2=iDate2.replace("{","");
                            iDate2=iDate2.replace("}","");
                            iDate2=iDate2.replace("-","/");
                            Log.d("Array strig222",iDate2);
                            int year=Integer.parseInt(iDate2.substring(0,4));

                            String str_month2=iDate2.substring(iDate2.indexOf("/")+1);
                            str_month2=str_month2.substring(0,str_month2.indexOf("/"));

                            int month2=Integer.parseInt(str_month2);
                            if(month2==0){
                                month2=1;
                            }else{
                                month2=month2+1;
                            }

                            String str_day2=iDate2.substring(iDate2.indexOf("/") + 1);
                            str_day2=str_day2.substring(str_day2.indexOf("/")+1);
                            int dayofmonth2=Integer.parseInt(str_day2);

                            Log.d("Array dat integer", Integer.toString(year)+ Integer.toString(month2)+Integer.toString(dayofmonth2));


                            cal_today_vadPaakhi= Calendar.getInstance();
                            cal_today_vadPaakhi.set(year, month2-1, dayofmonth2, 00, 01, 00);
                            time2 = cal_today_vadPaakhi.getTimeInMillis();


                            if (cal_today_vadPaakhi.getTime().after(today_date)) {

                                Intent intentAlarm = new Intent(Deravasi_panchang.this, AlarmManagerBroadcastReceiver.class);

                                intentAlarm.putExtra("title","Today is Chaudas");
                                intentAlarm.putExtra("msg1","Hello,Today is Chaudas keep in mind please...");
                                intentAlarm.putExtra("msg2", "");

                                *//** Creating a Pending Intent *//*
                                operation52 = PendingIntent.getBroadcast(Deravasi_panchang.this, 15*10 + k2, intentAlarm,0);


                                //set the alarm for particular time
                                alarmManager.set(AlarmManager.RTC_WAKEUP, time2, operation52);
                                // Toast.makeText(Deravasi_panchang.this, "Alarm Scheduled for pakkhi", Toast.LENGTH_LONG).show();
                                //Log.d("2nd pakhi times are=",sdftithi.format(cal_today.getTime()));
                                System.out.println("2ndpakki alarms times are====" + cal_today_vadPaakhi.getTime());

                            } else {
                                System.out.println("2ndpakki misees are====" + cal_today_vadPaakhi.getTime());

                            }



                        }*/

                        Toast.makeText(Deravasi_panchang.this, "Alarm Scheduled for Chaudas", Toast.LENGTH_LONG).show();

                    }catch (Exception ed){ed.printStackTrace();}


                }else {
                    fab_paakhi.setImageResource(R.drawable.uncheceked);
                    sharepref.edit().putString("chaudas","no").commit();
                    alarmManager.cancel(operation5);
                    Toast.makeText(Deravasi_panchang.this, "Alarm Removed for Chaudas", Toast.LENGTH_LONG).show();

                }


            }
        });
        fab_aatham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PendingIntent operation6=null;
                if(!sharepref.getString("aatham","no").equalsIgnoreCase("yes")){
                    fab_aatham.setImageResource(R.drawable.checked);
                    sharepref.edit().putString("aatham", "yes").commit();

                    try{
                        for(int k=0;k<Ddates_aatham.size();k++){

                            String iDate=Ddates_aatham.get(k).toString();
                            iDate=iDate.replace("CalendarDay","");
                            iDate=iDate.replace("{","");
                            iDate=iDate.replace("}","");
                            iDate=iDate.replace("-","/");
                            int year=Integer.parseInt(iDate.substring(0,4));

                            String str_month=iDate.substring(iDate.indexOf("/")+1);
                            str_month=str_month.substring(0, str_month.indexOf("/"));

                            int month=Integer.parseInt(str_month);
                            if(month==0){
                                month=1;
                            }else{
                                month=month+1;
                            }

                            String str_day=iDate.substring(iDate.indexOf("/") + 1);
                            str_day=str_day.substring(str_day.indexOf("/")+1);
                            int dayofmonth=Integer.parseInt(str_day);

                            //Log.d("Array dat integer", Integer.toString(year) + Integer.toString(month) + Integer.toString(dayofmonth));


                            cal_today_aatham= Calendar.getInstance();
                            cal_today_aatham.set(year, month-1, dayofmonth, 00, 01, 00);
                            time = cal_today_aatham.getTimeInMillis();


                            if (cal_today_aatham.getTime().after(today_date)) {
                                Intent intentAlarm = new Intent(Deravasi_panchang.this, AlarmManagerBroadcastReceiver.class);

                                intentAlarm.putExtra("title","Today is Aatham");
                                intentAlarm.putExtra("msg1","Hello,Today is Aatham keep in mind please...");
                                intentAlarm.putExtra("msg2","");
                                intentAlarm.putExtra("msg3","deravasi");

                                /** Creating a Pending Intent */
                                operation6 = PendingIntent.getBroadcast(Deravasi_panchang.this, 40*10 + k, intentAlarm, 0);


                                //set the alarm for particular time
                                alarmManager.set(AlarmManager.RTC_WAKEUP, time, operation6);
                               // System.out.println("aatham alarms times are====" + cal_today_aatham.getTime());


                            } else {
                               // System.out.println("aatham misses are====" + cal_today_aatham.getTime());

                            }

                        }
                        Toast.makeText(Deravasi_panchang.this, "Alarm Scheduled for aatham", Toast.LENGTH_LONG).show();

                    }catch (Exception ed){ed.printStackTrace();}


                }else{
                    fab_aatham.setImageResource(R.drawable.uncheceked);
                    sharepref.edit().putString("aatham","no").commit();
                    alarmManager.cancel(operation6);
                    Toast.makeText(Deravasi_panchang.this, "Alarm Removed for aatham", Toast.LENGTH_LONG).show();

                }

            }
        });
        fab_pacham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PendingIntent operation7=null;
                if(!sharepref.getString("pacham","no").equalsIgnoreCase("yes")){
                    fab_pacham.setImageResource(R.drawable.checked);
                    sharepref.edit().putString("pacham", "yes").commit();

                    try{


                        for(int k=0;k<Ddates_pacham.size();k++){

                            String iDate=Ddates_pacham.get(k).toString();
                            iDate=iDate.replace("CalendarDay","");
                            iDate=iDate.replace("{","");
                            iDate=iDate.replace("}","");
                            iDate=iDate.replace("-","/");
                            int year=Integer.parseInt(iDate.substring(0,4));

                            String str_month=iDate.substring(iDate.indexOf("/")+1);
                            str_month=str_month.substring(0, str_month.indexOf("/"));

                            int month=Integer.parseInt(str_month);
                            if(month==0){
                                month=1;
                            }else{
                                month=month+1;
                            }

                            String str_day=iDate.substring(iDate.indexOf("/") + 1);
                            str_day=str_day.substring(str_day.indexOf("/")+1);
                            int dayofmonth=Integer.parseInt(str_day);

                            //Log.d("Array dat integer", Integer.toString(year) + Integer.toString(month) + Integer.toString(dayofmonth));


                            cal_today_pacham= Calendar.getInstance();
                            cal_today_pacham.set(year, month-1, dayofmonth, 00, 01, 00);
                            time = cal_today_pacham.getTimeInMillis();


                            if (cal_today_pacham.getTime().after(today_date)) {
                                Intent intentAlarm = new Intent(Deravasi_panchang.this, AlarmManagerBroadcastReceiver.class);

                                intentAlarm.putExtra("title","Today is Pacham");
                                intentAlarm.putExtra("msg1","Hello,Today is Pacham keep in mind please...");
                                intentAlarm.putExtra("msg2","");
                                intentAlarm.putExtra("msg3","deravasi");

                                /** Creating a Pending Intent */
                                operation7 = PendingIntent.getBroadcast(Deravasi_panchang.this, 70*10 + k, intentAlarm, 0);


                                //set the alarm for particular time
                                alarmManager.set(AlarmManager.RTC_WAKEUP, time, operation7);
                                //System.out.println("pacham alarms times are====" + cal_today_pacham.getTime());
                            } else {
                                //System.out.println("pacham misess are====" + cal_today_pacham.getTime());

                            }




                        }

                        Toast.makeText(Deravasi_panchang.this, "Alarm Scheduled for pacham", Toast.LENGTH_LONG).show();

                    }catch (Exception ed){ed.printStackTrace();}


                }else{
                    fab_pacham.setImageResource(R.drawable.uncheceked);
                    sharepref.edit().putString("pacham","no").commit();
                    alarmManager.cancel(operation7);
                    Toast.makeText(Deravasi_panchang.this, "Alarm Removed for pacham", Toast.LENGTH_LONG).show();

                }

            }
        });
        fab_bij.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PendingIntent operation8=null;
                if(!sharepref.getString("bij","no").equalsIgnoreCase("yes")){
                    fab_bij.setImageResource(R.drawable.checked);
                    sharepref.edit().putString("bij", "yes").commit();

                    try{

                        for(int k=0;k<Ddates_bij.size();k++){

                            String iDate=Ddates_bij.get(k).toString();
                            iDate=iDate.replace("CalendarDay","");
                            iDate=iDate.replace("{","");
                            iDate=iDate.replace("}","");
                            iDate=iDate.replace("-","/");
                            int year=Integer.parseInt(iDate.substring(0,4));

                            String str_month=iDate.substring(iDate.indexOf("/")+1);
                            str_month=str_month.substring(0, str_month.indexOf("/"));

                            int month=Integer.parseInt(str_month);
                            if(month==0){
                                month=1;
                            }else{
                                month=month+1;
                            }

                            String str_day=iDate.substring(iDate.indexOf("/") + 1);
                            str_day=str_day.substring(str_day.indexOf("/")+1);
                            int dayofmonth=Integer.parseInt(str_day);

                            //Log.d("Array dat integer", Integer.toString(year) + Integer.toString(month) + Integer.toString(dayofmonth));


                            cal_today_bij= Calendar.getInstance();
                            cal_today_bij.set(year, month-1, dayofmonth, 00, 01, 00);
                            time = cal_today_bij.getTimeInMillis();

                            if (cal_today_bij.getTime().after(today_date)) {
                                Intent intentAlarm = new Intent(Deravasi_panchang.this, AlarmManagerBroadcastReceiver.class);

                                intentAlarm.putExtra("title","Today is Bij");
                                intentAlarm.putExtra("msg1","Hello,Today is Bij keep in mind please...");
                                intentAlarm.putExtra("msg2","");
                                intentAlarm.putExtra("msg3","deravasi");

                                /** Creating a Pending Intent */
                                operation8 = PendingIntent.getBroadcast(Deravasi_panchang.this, 101*10 + k, intentAlarm, 0);


                                //set the alarm for particular time
                                alarmManager.set(AlarmManager.RTC_WAKEUP, time, operation8);
                                //System.out.println("bij alarms times are====" + cal_today_bij.getTime());
                            } else {
                                //System.out.println("bij misses are====" + cal_today_bij.getTime());

                            }




                        }
                        Toast.makeText(Deravasi_panchang.this, "Alarm Scheduled for Bij", Toast.LENGTH_LONG).show();

                    }catch (Exception ed){ed.printStackTrace();}


                }else{
                    fab_bij.setImageResource(R.drawable.uncheceked);
                    sharepref.edit().putString("bij","no").commit();
                    alarmManager.cancel(operation8);
                    Toast.makeText(Deravasi_panchang.this, "Alarm Removed for Bij", Toast.LENGTH_LONG).show();

                }

            }
        });














//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//********************************************************************************************************************************************
    }



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
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                alertDialog.dismiss();
            }
        });
   // Showing Alert Message
        alertDialog.show();
    }



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
