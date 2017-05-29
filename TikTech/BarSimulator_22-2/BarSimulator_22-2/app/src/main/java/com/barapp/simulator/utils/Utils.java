package com.barapp.simulator.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;


/**
 * Created by Shanni on 1/13/2017.
 */

public class Utils {



    public static Typeface getFont(Context context, int tag) {
        if (tag == 100) {
            return Typeface.createFromAsset(context.getAssets(),
                    "Blockletter.otf");
        }else if (tag == 200) {
            return Typeface.createFromAsset(context.getAssets(),
                    "Questrial-Regular.ttf");
        }
        return Typeface.DEFAULT;
    }


    public static void setPrefs(Context context , String prefname ,String prefvalue)
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

        sharepref.edit().putString(Const.ISLOGIN,"no").commit();
        sharepref.edit().putString(Const.TYPE,"3").commit();

    }





}
