package com.emoji.me_emoji.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Shanni on 2/15/2017.
 */

public class Utils {




    public static Typeface getFont(Context context, int tag) {
        if (tag == 100) {
            return Typeface.createFromAsset(context.getAssets() , "OpenSans-Bold.ttf");
        }else if (tag == 200) {
            return Typeface.createFromAsset(context.getAssets() , "OpenSans-Regular.ttf");
        }else if(tag == 300){
            return Typeface.createFromAsset(context.getAssets() , "OpenSans-Light.ttf");
        }
        return Typeface.DEFAULT;
    }

    public static void setPrefs(Context context , String prefname , String prefvalue)
    {
        SharedPreferences sharepref = context.getSharedPreferences("pref" , Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharepref.edit();

        edit.putString(prefname ,prefvalue);
        edit.commit();
    }


    public static String getPrefs(Context context , String prefname){

        SharedPreferences sharepref = context.getSharedPreferences("pref" ,Context.MODE_PRIVATE);
        String value = sharepref.getString(prefname ,"");

        return  value;
    }

    public static void clearPrefs(Context context , String prefname){

        SharedPreferences sharepref = context.getSharedPreferences("pref" , Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharepref.edit();
        edit.remove(prefname);
        edit.commit();
    }


    public static void ClearAllPrefs(Context context){

        SharedPreferences sharepref = context.getSharedPreferences("pref" , Context.MODE_PRIVATE);
        sharepref.edit().clear().commit();

    }


    public static  boolean isNetworkAvailable(Context context) {
        if(context == null) { return false; }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // if no network is available networkInfo will be null, otherwise check if we are connected
        try {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }


}
