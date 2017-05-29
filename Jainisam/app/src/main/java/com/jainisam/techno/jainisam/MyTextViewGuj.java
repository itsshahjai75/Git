package com.jainisam.techno.jainisam;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextViewGuj extends TextView {

    public MyTextViewGuj(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewGuj(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewGuj(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Shree.ttf");
        setTypeface(tf ,1);

    }

}