package technocrats.appware;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class Custom_font extends TextView {

    public Custom_font(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Custom_font(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Custom_font(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"Roboto-Light.ttf");
        setTypeface(tf);
    }

}
