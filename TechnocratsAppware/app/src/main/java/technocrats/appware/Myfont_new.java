package technocrats.appware;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class Myfont_new extends TextView {

    public Myfont_new(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        isInEditMode();
    }

    public Myfont_new(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        isInEditMode();
    }
    public Myfont_new(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        isInEditMode();
    }

    public void setTypeface(Typeface tf, int style) {
        if(!this.isInEditMode()){
        Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Light.ttf");
        Typeface boldTypeface = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Light.ttf");

        if (style == Typeface.BOLD) {
            super.setTypeface(boldTypeface/*, -1*/);
        } else {
            super.setTypeface(normalTypeface/*, -1*/);
        }
        }

    }   

}