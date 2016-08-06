package com.jainisam.techno.jainisam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by Jai on 08-Nov-15.
 */

public class DataObject_post {

    public static final int NO_IMAGE = 0;
    public static final int WITH_IMAGE = 1;

    private String mText1;
    private String mText2;
    private String mBitmap;
    private String mTime;
    private int mType;

    DataObject_post(String text1, String text2, String bitmap,String Time){
        mText1 = text1;
        mText2 = text2;
        mBitmap = bitmap;
        mTime=Time;

    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }

    public String getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(String mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public int getType() {
        if(mBitmap.equalsIgnoreCase("null")){
            mType=NO_IMAGE;
        }else{
            mType=WITH_IMAGE;
        }
        return mType;
    }
    public void setType(int type) {
        this.mType = type;
    }



}