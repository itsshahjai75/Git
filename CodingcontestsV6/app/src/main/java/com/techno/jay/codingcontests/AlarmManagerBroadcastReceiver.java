package com.techno.jay.codingcontests;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import DB.DatabaseHelper;


public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {


    String contestid, msg1, msg2,msg3;
    Context mContext;

    DatabaseHelper dbh;
    SQLiteDatabase db;
    Cursor mCursor;
/*
    String coutnt_down="";
    private static final String FORMAT = "%02d:%02d:%02d";
*/




    @Override
    public void onReceive(final Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        //Acquire the lock
        wl.acquire();

        mContext=context;

        contestid=intent.getStringExtra("contestid").toString();

        //========================================================================================



        try {


                dbh = new DatabaseHelper(context);
                db = dbh.getWritableDatabase();
                // Select All Query

                String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.CONTEST_ID + " = '" + contestid + "';";
                ////Log.i("TAG day", selectQuery);
                Cursor mCursor = db.rawQuery(selectQuery, null);


                if (mCursor.moveToFirst()) {
                    do {
                        String DURATION = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.DURATION));
                        String END = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.END));
                        String START = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.START));
                        String EVENT = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.EVENT));
                        String HREF = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.HREF));
                        String CONTEST_ID = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.CONTEST_ID));
                        String RESOURCE_ID = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.RESOURCE_ID));
                        String RESOURCE_NAME = mCursor.getString(mCursor.getColumnIndex(DatabaseHelper.RESOURCE_NAME));
                              /*  Log.i("START day", DURATION+
                                        END+
                                        START+
                                        EVENT+
                                        HREF+
                                        CONTEST_ID+
                                        RESOURCE_ID+
                                        RESOURCE_NAME);*/
                        msg1="Reminder : \n"+RESOURCE_NAME;
                        msg2=EVENT;
                        msg3=EVENT+"\nStart : "+START+"\nEnd : "+END;



                    } while (mCursor.moveToNext());
                }

                if(mCursor==null || mCursor.getCount()<=0){

                    // Log.d("object",EVENT);

                }

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






        //============================================================================================





        //Log.v("contestid==",contestid);
        Intent intent2 = new Intent(context, Details_event.class);
        intent2.putExtra("eventid",contestid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent2,
                PendingIntent.FLAG_ONE_SHOT);

        final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification.Builder builder = new Notification.Builder(mContext);





        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            // define sound URI, the sound to be played when there's a notification
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);





            Bitmap bm = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_notofication_icon);

            builder
                    .setSmallIcon(R.drawable.ic_notofication_icon)
                    .setContentTitle(msg1)
                    .setContentText(msg2)
                    .setTicker("CodingContests")
                    .setOngoing(false)
                    .setLargeIcon(bm)
                    .setSound(soundUri)
                    .setLights(0xFFFF0000, 500, 500) //setLights (int argb, int onMs, int offMs)
                    //.setContentIntent(pendingIntent)
                    .setStyle(new Notification.BigTextStyle()
                            .bigText(msg3))
                    //.addAction(android.R.drawable.ic_menu_close_clear_cancel, coutnt_down, pendingIntent)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);



            Notification notification = builder.getNotification();

            notificationManager.notify(R.drawable.ic_notofication_icon, notification);






        } else {
            // Lollipop specific setColor method goes here.

            // define sound URI, the sound to be played when there's a notification
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



            Bitmap bm = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_notofication_icon);

            builder
                    .setSmallIcon(R.drawable.ic_notofication_icon)
                    .setContentTitle(msg1)
                    .setContentText(msg2)
                    .setTicker("CodingContests")
                    .setOngoing(false)
                    .setLargeIcon(bm)
                    .setColor(context.getResources().getColor(R.color.colorPrimary))
                    .setSound(soundUri)
                    .setLights(0xFFFF0000, 500, 500) //setLights (int argb, int onMs, int offMs)*/
                    //.setContentIntent(pendingIntent)
                    .setStyle(new Notification.BigTextStyle()
                            .bigText(msg3))
                    //.addAction(android.R.drawable.ic_menu_close_clear_cancel, coutnt_down, pendingIntent)
                    /*.addAction (R.drawable.ic_stat_snooze,
                            getString(R.string.snooze), piSnooze);*/
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);



            Notification notification = builder.getNotification();

            notificationManager.notify(R.drawable.ic_notofication_icon, notification);







        }


        boolean isScreenOn = pm.isScreenOn();

        //Log.e("screen on.......", "" + isScreenOn);

        if(isScreenOn==false)
        {

            wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");

            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");

            wl_cpu.acquire(10000);
        }




    }

}
