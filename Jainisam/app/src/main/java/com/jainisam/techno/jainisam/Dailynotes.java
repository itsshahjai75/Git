package com.jainisam.techno.jainisam;

import android.app.AlarmManager;
import android.support.v4.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Dailynotes extends Fragment {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";


    SharedPreferences sharepref;

    int n=1,c=1,p=1,dp=1;
    Long time;
    AlarmManager alarmManager;




    private FloatingActionButton fab_navkarshi,fab_chovihar;
    private FloatingActionButton fab_porsi,fab_dodhporsi;

    public Dailynotes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dailynotes, container, false);


        sharepref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        Typeface cFont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Thin.ttf");
        Typeface cFont2 = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        mAdapter = new MyRecyclerViewAdapter(getDataSet(),cFont,cFont2);
        mRecyclerView.setAdapter(mAdapter);
        final FloatingActionMenu menu1 = (FloatingActionMenu)view.findViewById(R.id.menu1);

         alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);


        fab_navkarshi = (FloatingActionButton)view.findViewById(R.id.fab_navkarshi);
        fab_chovihar = (FloatingActionButton)view.findViewById(R.id.fab_chovihar);
        fab_porsi = (FloatingActionButton)view.findViewById(R.id.fab_porsi);
        fab_dodhporsi = (FloatingActionButton)view.findViewById(R.id.fab_dodhporsi);


        MyTextView tv_date_day =(MyTextView)view.findViewById(R.id.tv_dat_day_dailynots);
        final SimpleDateFormat sdfCal_datday = new SimpleDateFormat("dd/MMM/yyyy EEEE",Locale.getDefault());
        final Calendar cal_datday= Calendar.getInstance();
        tv_date_day.setText(sdfCal_datday.format(cal_datday.getTime()));


        menu1.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menu1.isOpened()) {
                    //Toast.makeText(getActivity(), menu1.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                    if(sharepref.getString("navkarsi",null).equalsIgnoreCase("yes")){
                        fab_navkarshi.setImageResource(R.drawable.checked);
                    }else {
                        fab_navkarshi.setImageResource(R.drawable.uncheceked);
                    }
                    if(sharepref.getString("chovihar",null).equalsIgnoreCase("yes")){
                        fab_chovihar.setImageResource(R.drawable.checked);
                    }else {
                        fab_chovihar.setImageResource(R.drawable.uncheceked);
                    }
                    if(sharepref.getString("porsi",null).equalsIgnoreCase("yes")){
                        fab_porsi.setImageResource(R.drawable.checked);
                    }else {
                        fab_porsi.setImageResource(R.drawable.uncheceked);
                    }
                    if(sharepref.getString("dodhporsi",null).equalsIgnoreCase("yes")){
                        fab_dodhporsi.setImageResource(R.drawable.checked);
                    }else {
                        fab_dodhporsi.setImageResource(R.drawable.uncheceked);
                    }
                }

                menu1.toggle(true);
            }
        });
        menu1.setClosedOnTouchOutside(true);


        //fab1.setEnabled(false);



        fab_navkarshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                n=n+1;
                PendingIntent operation1=null;
                if(n%2==0){
                    fab_navkarshi.setImageResource(R.drawable.checked);
                    sharepref.edit().putString("navkarsi","yes").commit();

                    try{
                    final SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());
                    Calendar calendar_day3 = Calendar.getInstance();
                    calendar_day3.setTime(sdf3.parse(sharepref.getString("sunriseTime", null)));
                    //calendar_day.add(Calendar.HOUR, 48);
                    calendar_day3.add(Calendar.MINUTE, 48);

                    time = calendar_day3.getTimeInMillis();
                    }catch (Exception ed){ed.printStackTrace();}

                    Intent intentAlarm = new Intent(getActivity(), AlarmManagerBroadcastReceiver.class);

                    intentAlarm.putExtra("title","Navkarshi Time");
                    intentAlarm.putExtra("msg1","Navkarshi Time Done over the pachhkhaan and take breakfast.");
                    intentAlarm.putExtra("msg2","");

                    /** Creating a Pending Intent */
                    operation1 = PendingIntent.getBroadcast(getActivity(), 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);


                    //set the alarm for particular time
                    alarmManager.set(AlarmManager.RTC_WAKEUP,time,operation1);
                    Toast.makeText(getActivity(), "Alarm Scheduled for Navkarshi", Toast.LENGTH_LONG).show();

                }else{
                    fab_navkarshi.setImageResource(R.drawable.uncheceked);
                    sharepref.edit().putString("navkarsi","no").commit();
                    alarmManager.cancel(operation1);
                }


            }
        });
        fab_chovihar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c=c+1;
                PendingIntent operation2=null;
                if(c%2==0){
                    fab_chovihar.setImageResource(R.drawable.checked);
                    sharepref.edit().putString("chovihar","yes").commit();

                    try{
                        final SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm:ss aa",Locale.getDefault());
                        Calendar calendar_day3 = Calendar.getInstance();
                        calendar_day3.setTime(sdf3.parse(sharepref.getString("sunsetTime", null)));
                        //calendar_day.add(Calendar.HOUR, 48);
                        calendar_day3.add(Calendar.MINUTE, -50);

                        time = calendar_day3.getTimeInMillis();
                    }catch (Exception ed){ed.printStackTrace();}

                    Intent intentAlarm = new Intent(getActivity(), AlarmManagerBroadcastReceiver.class);

                    intentAlarm.putExtra("title","Chovihar Time");
                    intentAlarm.putExtra("msg1","50 min Remain for Today's Chovihar Time .");
                    intentAlarm.putExtra("msg2","");

                    /** Creating a Pending Intent */
                    operation2 = PendingIntent.getBroadcast(getActivity(), 2, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);


                    //set the alarm for particular time
                    alarmManager.set(AlarmManager.RTC_WAKEUP,time,operation2);
                    Toast.makeText(getActivity(), "Alarm Scheduled for Navkarshi", Toast.LENGTH_LONG).show();

                }else{
                    fab_chovihar.setImageResource(R.drawable.uncheceked);
                    sharepref.edit().putString("chovihar","no").commit();
                    alarmManager.cancel(operation2);
                }

            }
        });
        fab_porsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                p=p+1;
                PendingIntent operation3=null;
                if(p%2==0){
                    fab_porsi.setImageResource(R.drawable.checked);
                    sharepref.edit().putString("porsi","yes").commit();

                    try{

                        //This is code for day choghdiyas duration time by time
                        String day_one_choghadiyatime=sharepref.getString("daychoghdiyaDuration", null);
                        //String timestampStr = "14:35:06";
                        String[] partation = day_one_choghadiyatime.split(":");
                        int hours_Dpartion = Integer.parseInt(partation[0]);
                        int minutes_Dpartion = Integer.parseInt(partation[1]);
                        final SimpleDateFormat sdf4 = new SimpleDateFormat("hh:mm:ss aa",Locale.getDefault());
                        Calendar calendar_day4 = Calendar.getInstance();
                        calendar_day4.setTime(sdf4.parse(sharepref.getString("sunriseTime", null)));
                        calendar_day4.add(Calendar.HOUR, hours_Dpartion * 2);
                        calendar_day4.add(Calendar.MINUTE, minutes_Dpartion*2);
                        String new_time4 = sdf4.format(calendar_day4.getTime());

                        time =calendar_day4.getTimeInMillis();
                    }catch (Exception ed){ed.printStackTrace();}

                    Intent intentAlarm = new Intent(getActivity(), AlarmManagerBroadcastReceiver.class);

                    intentAlarm.putExtra("title","Porsi Time");
                    intentAlarm.putExtra("msg1","Porsi Time Done.Complete the pachhkhaan and take breakfast.");
                    intentAlarm.putExtra("msg2","");

                    /** Creating a Pending Intent */
                    operation3 = PendingIntent.getBroadcast(getActivity(), 3, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);


                    //set the alarm for particular time
                    alarmManager.set(AlarmManager.RTC_WAKEUP,time,operation3);
                    Toast.makeText(getActivity(), "Alarm Scheduled for Navkarshi", Toast.LENGTH_LONG).show();

                }else{
                    fab_porsi.setImageResource(R.drawable.uncheceked);
                    sharepref.edit().putString("porsi","no").commit();
                    alarmManager.cancel(operation3);
                }

            }
        });
        fab_dodhporsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dp=dp+1;
                PendingIntent operation4=null;
                if(dp%2==0){
                    fab_dodhporsi.setImageResource(R.drawable.checked);
                    sharepref.edit().putString("dodhporsi","yes").commit();

                    try{

                        //This is code for day choghdiyas duration time by time
                        String day_one_choghadiyatime=sharepref.getString("daychoghdiyaDuration", null);
                        //String timestampStr = "14:35:06";
                        String[] partation = day_one_choghadiyatime.split(":");
                        int hours_Dpartion = Integer.parseInt(partation[0]);
                        int minutes_Dpartion = Integer.parseInt(partation[1]);
                        final SimpleDateFormat sdf4 = new SimpleDateFormat("hh:mm:ss aa",Locale.getDefault());
                        Calendar calendar_day4 = Calendar.getInstance();
                        calendar_day4.setTime(sdf4.parse(sharepref.getString("sunriseTime", null)));
                        calendar_day4.add(Calendar.HOUR, hours_Dpartion * 3);
                        calendar_day4.add(Calendar.MINUTE, minutes_Dpartion*3);
                        String new_time4 = sdf4.format(calendar_day4.getTime());

                        time =calendar_day4.getTimeInMillis();
                    }catch (Exception ed){ed.printStackTrace();}

                    Intent intentAlarm = new Intent(getActivity(), AlarmManagerBroadcastReceiver.class);

                    intentAlarm.putExtra("title","DodhPorsi Time");
                    intentAlarm.putExtra("msg1","DodhPorsi Time Done.Complete the pachhkhaan and take breakfast.");
                    intentAlarm.putExtra("msg2","");

                    /** Creating a Pending Intent */
                    operation4 = PendingIntent.getBroadcast(getActivity(), 4, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);


                    //set the alarm for particular time
                    alarmManager.set(AlarmManager.RTC_WAKEUP,time,operation4);
                    Toast.makeText(getActivity(), "Alarm Scheduled for DodhPorsi", Toast.LENGTH_LONG).show();

                }else{
                    fab_dodhporsi.setImageResource(R.drawable.uncheceked);
                    sharepref.edit().putString("dodhporsi","no").commit();
                    alarmManager.cancel(operation4);
                }

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
               // Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();

        System.out.println("share pref are" + sharepref.getString("sunriseTime", null) + ",\n" +
                sharepref.getString("sunsetTime", null) + ",\n" +
                sharepref.getString("dayLength", null) + ",\n" +
                sharepref.getString("nightLength", null) + ",\n" +
                sharepref.getString("daychoghdiyaDuration", null) + ",\n" +
                sharepref.getString("nightchoghdiyaDuration", null));

        DateFormat df_dailynote = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());
        DateFormat df_dailynote_duration = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());


        try {


            String SRtime=sharepref.getString("sunriseTime", null).toString();
            String SStime=sharepref.getString("sunsetTime", null).toString();
            String DLtime=sharepref.getString("dayLength", null).toString();
            String NLtime=sharepref.getString("nightLength", null).toString();
            String Dchoghdiya=sharepref.getString("daychoghdiyaDuration", null).toString();
            String Nchoghdiya=sharepref.getString("nightchoghdiyaDuration", null).toString();




            Date sunriseTime = df_dailynote.parse(SRtime);
            DataObject obj1 = new DataObject("S.R", "Sun Rise Time( સૂર્યોદય)", new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault()).format(sunriseTime));
            results.add(obj1);


            Date sunsetTime = df_dailynote.parse(SStime);
            DataObject obj2 = new DataObject("S.S", "Sun Set Time( સુર્યાસ્ત)", new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault()).format(sunsetTime));
            results.add(obj2);



            String day_one_choghadiyatime = Dchoghdiya;//sharepref.getString("daychoghdiyaDuration", null);

            //String timestampStr = "14:35:06";
            String[] partation = day_one_choghadiyatime.split(":");
            int hours_Dpartion = Integer.parseInt(partation[0]);
            int minutes_Dpartion = Integer.parseInt(partation[1]);
            int seconds_DpartionH = Integer.parseInt(partation[2]);

            try {


                final SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());
                Calendar calendar_day3 = Calendar.getInstance();
                calendar_day3.setTime(sdf3.parse(SRtime));
                //calendar_day.add(Calendar.HOUR, 48);
                calendar_day3.add(Calendar.MINUTE, 48);
                String new_time3 = sdf3.format(calendar_day3.getTime());
                //new_time3=sdf3.format(new_time3);

                DataObject obj3 = new DataObject("N", "Navkarshi( નવકારશી )", new_time3);
                results.add(obj3);
            } catch (Exception ep) {

                ep.printStackTrace();
            }


            try {
                final SimpleDateFormat sdf4 = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());
                Calendar calendar_day4 = Calendar.getInstance();
                calendar_day4.setTime(sdf4.parse(SRtime));
                calendar_day4.add(Calendar.HOUR, hours_Dpartion * 2);
                calendar_day4.add(Calendar.MINUTE, minutes_Dpartion * 2);
                String new_time4 = sdf4.format(calendar_day4.getTime());

                DataObject obj4 = new DataObject("P", "Porshi( પોરસી )", new_time4);
                results.add(obj4);
            } catch (Exception ep) {

                ep.printStackTrace();
            }


            try {
                final SimpleDateFormat sdf5 = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());
                Calendar calendar_day5 = Calendar.getInstance();
                calendar_day5.setTime(sdf5.parse(SRtime));
                calendar_day5.add(Calendar.HOUR, hours_Dpartion * 3);
                calendar_day5.add(Calendar.MINUTE, minutes_Dpartion * 3);
                String new_time5 = sdf5.format(calendar_day5.getTime());


                DataObject obj5 = new DataObject("D.P", "Dodh Porshi( દોઢ પોરસી )", new_time5);
                results.add(obj5);
            } catch (Exception ep) {

                ep.printStackTrace();
            }

            //Date sunsetTime = df_post.parse(sharepref.getString("sunsetTime", null));
            DataObject obj6 = new DataObject("C", "Chovihar Time( ચોવિહાર )", new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault()).format(sunsetTime));
            results.add(obj6);

            Date dayLength = df_dailynote_duration.parse(DLtime);
            DataObject obj7 = new DataObject("D.L", "Day Length( દિવસ ના કલાક )", new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(dayLength));
            results.add(obj7);

            Date nightLength = df_dailynote_duration.parse(NLtime);
            DataObject obj8 = new DataObject("N.L", "Night Length( રાત્રી ના કલાક )", new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(nightLength));
            results.add(obj8);

        }catch(Exception date_exp){
                date_exp.printStackTrace();
            }

        return results;
    }


}
