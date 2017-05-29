package com.emoji.me_emoji.keyboard.emojicon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.emoji.me_emoji.R;
import com.emoji.me_emoji.utils.ConnectionDetector;

import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Android Developer on 2/21/2017.
 */

public class EmojiIconGallaryFragment extends Fragment {

    private GridView imageGrid;
    private ArrayList<String> bitmapList;

    View view;
    boolean isInternetPresent=false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.emoji_gallary_grid, container, false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        this.imageGrid = (GridView) view.findViewById(R.id.gallary_gridview);
        this.bitmapList = new ArrayList<String>();


        ConnectionDetector cd = new ConnectionDetector(getActivity());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent){
            try {
                for(int i = 0; i < 10; i++) {
                    this.bitmapList.add(("http://placehold.it/150x150"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.imageGrid.setAdapter(new GallaryAdapter(getActivity(), this.bitmapList));
            Log.i("gallaryfragment", "this.bitmapList"+this.bitmapList);

            Log.i("gallaryfragment", "this.bitmapList.size()"+this.bitmapList.size());
        }else {

            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), " Sorry! No Internet!!!", Snackbar.LENGTH_LONG);

            // Changing message text color
            snackbar.setActionTextColor(Color.BLUE);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

            Toast.makeText(getActivity(), "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();

        }

        return view;
    }


    private Bitmap urlImageToBitmap(String imageUrl) throws Exception {
        Bitmap result = null;
        URL url = new URL(imageUrl);
        if(url != null) {
            result = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }
        return result;
    }
}
