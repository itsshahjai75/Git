package com.barapp.simulator.uc;

/**
 * Created by Shanni on 1/25/2017.
 */

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.barapp.simulator.R;

public class RippleTextView extends UserTextView {

    private float mRadius;

    private Paint mPaint;
    private Paint mRectPaint;

    private Coord mCoord;

    int color;
    private class Coord {
        public float x;
        public float y;

        public Coord(float xValue , float yValue){
            this.x = xValue;
            this.y = yValue;
        }

        private void setX(float value){
            this.x = value;
        }

        private void setY(float value){
            this.y = value;
        }
    }

    public RippleTextView(final Context context) {
        super(context);
        init(context);
    }

    public RippleTextView(final Context context , final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RippleTextView(final Context context , final AttributeSet attrs , final int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs , R.styleable.NewCircleView , 0 , 0);

        try {
            color = a.getColor(R.styleable.NewCircleView_circlecolor , 0xff000000);
        } finally {
            a.recycle();
        }

        init(context);
    }

    private void init(Context context) {

        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#763276"));

        mRectPaint = new Paint();
        mRectPaint.setColor(Color.argb(0 , 84 , 110 , 122));

        mCoord = new Coord(0 , 0);
    }

    @Override
    public boolean onTouchEvent(@NonNull final MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            float mCenterX = (getTranslationX() + getWidth())/2.0f;
            float mCenterY = (getTranslationY() + getHeight())/2.0f;

            // reliable? http://stackoverflow.com/q/1410885
            mCoord.setX(event.getX());
            mCoord.setY(event.getY());

            Interpolator interpolator = new LinearInterpolator();
            long duration = 700;

            ObjectAnimator animRadius = ObjectAnimator.ofFloat(this , "radius" , 10f , getWidth()/2f);
            animRadius.setInterpolator(interpolator);
            animRadius.setDuration(duration);

            ObjectAnimator animAlpha = ObjectAnimator.ofInt(mPaint , "alpha" , 200 , 0);
            animAlpha.setInterpolator(interpolator);
            animAlpha.setDuration(duration);

            ObjectAnimator animX = ObjectAnimator.ofFloat(mCoord , "x" , mCoord.x , mCenterX);
            animX.setInterpolator(interpolator);
            animX.setDuration(duration);

            ObjectAnimator animY = ObjectAnimator.ofFloat(mCoord , "y" , mCoord.y , mCenterY);
            animY.setInterpolator(interpolator);
            animY.setDuration(duration);

            ObjectAnimator animRectAlpha = ObjectAnimator.ofInt(mRectPaint , "alpha" , 0 , 100 , 0);
            animRectAlpha.setInterpolator(interpolator);
            animRectAlpha.setDuration(duration);

            AnimatorSet animSetAlphaRadius = new AnimatorSet();
            animSetAlphaRadius.playTogether(animRadius, animAlpha, animX, animY, animRectAlpha);
            animSetAlphaRadius.start();

        }
        invalidate();
        return super.onTouchEvent(event);
    }

    public void setRadius(final float radius) {
        mRadius = radius;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull final Canvas canvas) {
        // before super.onDraw so it goes under the text
        canvas.drawCircle(mCoord.x , mCoord.y , mRadius , mPaint);
        canvas.drawRect(0 , 0 , getWidth() , getHeight() , mRectPaint);
        super.onDraw(canvas);
    }

}
