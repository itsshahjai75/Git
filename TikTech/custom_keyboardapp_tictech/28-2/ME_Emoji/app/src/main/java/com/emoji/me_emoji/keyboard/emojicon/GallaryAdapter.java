package com.emoji.me_emoji.keyboard.emojicon;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.emoji.me_emoji.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Android Developer on 2/21/2017.
 */

public class GallaryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> bitmapList;

    public GallaryAdapter(Context context, ArrayList<String> bitmapList) {
        this.context = context;
        this.bitmapList = bitmapList;
    }

    public int getCount() {
        return this.bitmapList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(this.context);
            imageView.setLayoutParams(new GridView.LayoutParams(115, 115));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

//        imageView.setImageBitmap(this.bitmapList.get(position));

//        Picasso.with(context) //Context
//                .load(this.bitmapList.get(position)) //URL/FILE
//                .resize(115, 115)
//                .centerCrop()
//                .into(imageView);//an ImageView Object to show the loaded image

        imageView.setImageResource(R.drawable.ic_launcher);

//        Picasso.with(context).load(R.drawable.ic_launcher).into(imageView);


        return imageView;
    }

}