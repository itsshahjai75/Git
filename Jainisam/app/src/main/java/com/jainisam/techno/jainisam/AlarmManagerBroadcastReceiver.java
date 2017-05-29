package com.jainisam.techno.jainisam;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.PowerManager;


public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {


    String title, msg1, msg2,msg3;
    Context mContext;


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

        title=intent.getStringExtra("title").toString();
        msg1=intent.getStringExtra("msg1").toString();
        msg2=intent.getStringExtra("msg2").toString();
        msg3=intent.getStringExtra("msg3").toString();


/*



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
*/

        final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification.Builder builder = new Notification.Builder(mContext);





        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            // define sound URI, the sound to be played when there's a notification
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);





            Bitmap bm = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_notification_icon);

            builder
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setContentTitle(title)
                    .setContentText(msg1)
                    .setTicker("JainS")
                    .setOngoing(true)
                    .setLargeIcon(bm)
                    .setSound(soundUri)
                    .setLights(0xFFFF0000, 500, 500) //setLights (int argb, int onMs, int offMs)
                    //.setContentIntent(pendingIntent)
                    .setStyle(new Notification.BigTextStyle()
                            .bigText("This will Automatic Dismiss after 24hr.\nPlease don't eat green vegetables and kandmuls or atlist do Chovihar and don't take dinner after sun set."))
                    //.addAction(android.R.drawable.ic_menu_close_clear_cancel, coutnt_down, pendingIntent)
                    .setAutoCancel(true);



            Notification notification = builder.getNotification();
            notification.flags |= Notification.FLAG_NO_CLEAR;

            notificationManager.notify(R.drawable.ic_notification_icon, notification);

            new CountDownTimer(24*60*60*1000, 1000) {

                public void onTick(long millisUntilFinished) {
//                    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
/*
                    coutnt_down=String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    //here you can have your logic to set text to edittext
                    Log.d("remain time", coutnt_down);*/

                }

                public void onFinish() {
                    //mTextField.setText("done!");
                    //coutnt_down="Dismiss";

                  notificationManager.cancel(R.drawable.ic_notification_icon);
                }

            }.start();






        } else {
            // Lollipop specific setColor method goes here.

            // define sound URI, the sound to be played when there's a notification
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



            Bitmap bm = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_notification_icon);

            builder
                    .setSmallIcon(R.drawable.ic_notification_icon)
                    .setContentTitle(title)
                    .setContentText(msg1)
                    .setTicker("JainS")
                    .setOngoing(true)
                    .setLargeIcon(bm)
                    .setColor(Color.parseColor("#3F51B5"))
                    .setSound(soundUri)
                    .setLights(0xFFFF0000, 500, 500) //setLights (int argb, int onMs, int offMs)*/
                    //.setContentIntent(pendingIntent)
                    .setStyle(new Notification.BigTextStyle()
                            .bigText("This will Automatic Dismiss after 24hr.\nPlease don't eat green vegetables and kandmuls or atlist do Chovihar and don't take dinner after sun set."))
                    //.addAction(android.R.drawable.ic_menu_close_clear_cancel, coutnt_down, pendingIntent)
                    /*.addAction (R.drawable.ic_stat_snooze,
                            getString(R.string.snooze), piSnooze);*/
                    .setAutoCancel(true);



            Notification notification = builder.getNotification();
            notification.flags |= Notification.FLAG_NO_CLEAR;

            notificationManager.notify(R.drawable.ic_notification_icon, notification);





            new CountDownTimer(24*60*60*1000, 1000) {

                public void onTick(long millisUntilFinished) {
//                    mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);

                    /*coutnt_down=String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    //here you can have your logic to set text to edittext
                    Log.d("remain time", coutnt_down);
*/
                }

                public void onFinish() {
                    //mTextField.setText("done!");
                    //coutnt_down="Dismiss";


                    notificationManager.cancel(R.drawable.ic_notification_icon);
                    }

            }.start();


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
