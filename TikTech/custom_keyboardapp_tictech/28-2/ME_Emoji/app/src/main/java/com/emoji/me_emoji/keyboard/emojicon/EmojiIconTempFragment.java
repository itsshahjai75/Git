package com.emoji.me_emoji.keyboard.emojicon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emoji.me_emoji.R;


/**
 * Created by Android Developer on 2/21/2017.
 */

public class EmojiIconTempFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.emoji_temp_fragment, container, false);

        return view;
    }
}
