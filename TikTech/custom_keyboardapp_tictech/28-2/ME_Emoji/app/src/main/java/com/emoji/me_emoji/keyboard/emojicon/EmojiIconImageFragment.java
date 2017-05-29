package com.emoji.me_emoji.keyboard.emojicon;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.emoji.me_emoji.R;


/**
 * Created by Android Developer on 2/20/2017.
 */

public class EmojiIconImageFragment extends Fragment implements AdapterView.OnItemClickListener {

    Bitmap bitmap;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.emoji_image_preview, container, false);
        ImageView imageView=(ImageView) view.findViewById(R.id.image_preview);

        if (getArguments()!=null && getArguments().containsKey("bitmap") && getArguments().getParcelable("bitmap")!=null){
            bitmap = getArguments().getParcelable("bitmap");

            imageView.setImageBitmap(bitmap);
        }else {

        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
