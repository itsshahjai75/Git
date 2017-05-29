package com.emoji.me_emoji.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emoji.me_emoji.AddEmojiActivity;
import com.emoji.me_emoji.EmojiGridActivity;
import com.emoji.me_emoji.R;
import com.emoji.me_emoji.dataset.EmojiDataset;
import com.emoji.me_emoji.utils.ConnectionDetector;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Testing on 28-Feb-17.
 */

public class EmojiGridFragment extends Fragment {

    View view;
    GridView gallary_gridview;

    public ArrayList<EmojiDataset> emojiDatasets = new ArrayList<>();

    FloatingActionButton fab_add;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.emojilist, container, false);
        init();
        return view;
    }

    public void init(){
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        gallary_gridview=(GridView) view.findViewById(R.id.emojilist_grid);
        fab_add=(FloatingActionButton) view.findViewById(R.id.fab_add);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addEmojiIntent=new Intent(getContext(),AddEmojiActivity.class);
                startActivity(addEmojiIntent);
            }
        });


        ConnectionDetector cd = new ConnectionDetector(getContext());
        // get Internet status
        boolean isInternetPresent=false;
        isInternetPresent = cd.isConnectingToInternet();

        Log.i("isInternetPresent:", " " + isInternetPresent);
        if (isInternetPresent){

            ArrayList<String> stringArrayList = new ArrayList<>();

            stringArrayList.add("http://www.personal.psu.edu/users/r/j/rjq5009/mushroom.png");
            stringArrayList.add("http://www.freeiconspng.com/uploads/green-humming-bird-png-4.png");
            stringArrayList.add("http://www.queness.com/resources/images/png/apple_ex.png");
            stringArrayList.add("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcT7hvjx9l0Oid9wZhcsKg2YeMCZ1FtzJlyDdvknn-SaJdrEu_rQSA");
            stringArrayList.add("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcR6LtDD_c6NiRPq5y8CYuN1N6n9HGMb1IBRXLd_-orSh6BhR822");
            stringArrayList.add("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTxZKXYIXjFzWdZQVmNqA0qfpnfVE5wO54Jfp0GikRzy3Eg_9FR");
            stringArrayList.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQAtUkMnzqKFui2w3V9zduOpjkfp_U6LltzZXVOaNrqGjurIQh");
            stringArrayList.add("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcSjKsxA6wgqYFJARuYpqrWrSRCeOW2oktwtaVVOFBSAK0wJOVBP-Q");
            stringArrayList.add("http://www.pngall.com/wp-content/uploads/2016/04/Jaguar-PNG-HD.png");
            stringArrayList.add("http://www.freeiconspng.com/uploads/bird-png-15.png");

            emojiDatasets.clear();
            for (int i = 0; i < stringArrayList.size(); i++) {
                EmojiDataset emojiDataset = new EmojiDataset();
                emojiDataset.setUrl(stringArrayList.get(i));
//                        Bitmap bitmap=getBitmapFromURL(stringArrayList.get(i));
//                        emojiDataset.setFile(persistImage(bitmap,"image"+i));
//                        emojiDataset.setFilepath(emojiDataset.getFile().getAbsolutePath());
//                        Log.i("filepath:", " " + emojiDataset.getFilepath());
                emojiDatasets.add(emojiDataset);
            }

            Log.i("emojiDatasets.size():", " " + emojiDatasets.size());
            Log.i("emojiDatasets:", " " + emojiDatasets.toString());

            gallary_gridview.setAdapter(new GridviewAdapter());
            Log.i("gallaryfragment", "this.bitmapList"+emojiDatasets);

            Log.i("gallaryfragment", "this.bitmapList.size()"+emojiDatasets.size());
        }else {

            Snackbar snackbar = Snackbar.make(view.findViewById(android.R.id.content), " Sorry! No Internet!!!", Snackbar.LENGTH_LONG);

            // Changing message text color
            snackbar.setActionTextColor(Color.BLUE);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

            Toast.makeText(getContext(), "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();

        }
    }

    public class GridviewAdapter extends BaseAdapter
    {
        public GridviewAdapter() {
            super();

        }

        @Override
        public int getCount() {
            return emojiDatasets.size();
        }

        @Override
        public Object getItem(int position) {
            return emojiDatasets.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public class ViewHolder
        {
            public ImageView img_emojilist;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridviewAdapter.ViewHolder view;
            final LayoutInflater layoutInflater = LayoutInflater.from(getContext());

            if(convertView==null)
            {
                view = new GridviewAdapter.ViewHolder();
                convertView = layoutInflater.inflate(R.layout.emojilist_row, null);

                view.img_emojilist = (ImageView) convertView.findViewById(R.id.img_emojilist);

                convertView.setTag(view);
            }
            else
            {
                view = (GridviewAdapter.ViewHolder) convertView.getTag();
            }

            Picasso.with(getContext()).load(emojiDatasets.get(position).getUrl()).resize(115,115).error(R.drawable.ic_vp_smileys).placeholder(R.drawable.ic_vp_smileys).into(view.img_emojilist);

            return convertView;
        }
    }
}
