package com.example.admin.eventsapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 10/15/2016.
 */

public class TimeUtils {

    public static String getTime(int totalSecs) {

        int days = ((totalSecs/3600))/24;
        int hours = (int) (totalSecs / 3600)%24;
        int minutes = (int) ((totalSecs % 3600) / 60);
        int seconds = (int) (totalSecs % 60);

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return timeString;
    }

    public static long getMilliseconds(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date testDate = null;
        try {
            testDate = sdf.parse(dateFormat);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return testDate.getTime();
    }

    public static String getDate(long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(new Date(millis));
    }

    public static String getTime(long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a");
        return formatter.format(new Date(millis));
    }
    public static String getDayOftheWeek(long millies){
        String[] days=new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        Calendar c = Calendar.getInstance();
//Set time in milliseconds
        c.setTimeInMillis(millies);
        int mDay = c.get(Calendar.DAY_OF_WEEK);
        return days[mDay-1];
    }
}
