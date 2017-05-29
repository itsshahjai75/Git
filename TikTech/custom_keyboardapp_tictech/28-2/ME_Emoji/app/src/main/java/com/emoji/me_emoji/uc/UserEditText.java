package com.emoji.me_emoji.uc;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.emoji.me_emoji.utils.Utils;


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
