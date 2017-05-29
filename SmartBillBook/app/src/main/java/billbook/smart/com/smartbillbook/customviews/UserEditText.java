package billbook.smart.com.smartbillbook.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;


public class UserEditText extends EditText{

	public UserEditText(Context context) {
		super(context);
		this.setTypeface(Utils.getFont(context, Integer.parseInt(this.getTag().toString())));
	}

	public UserEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setTypeface(Utils.getFont(context, Integer.parseInt(this.getTag().toString())));
	}

	public UserEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setTypeface(Utils.getFont(context, Integer.parseInt(this.getTag().toString())));
	}

}
