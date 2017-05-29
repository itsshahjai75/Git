package com.emoji.me_emoji;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emoji.me_emoji.dataset.EmojiDataset;
import com.emoji.me_emoji.keyboard.SimpleIME;
import com.emoji.me_emoji.utils.ConnectionDetector;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Android Developer on 2/24/2017.
 */

public class EmojiGridActivity extends AppCompatActivity {

    GridView gallary_gridview;

    public ArrayList<EmojiDataset> emojiDatasets = new ArrayList<>();

    FloatingActionButton fab_add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emojilist);
        getSupportActionBar().hide();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        gallary_gridview=(GridView) findViewById(R.id.emojilist_grid);
        fab_add=(FloatingActionButton) findViewById(R.id.fab_add);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addEmojiIntent=new Intent(EmojiGridActivity.this,AddEmojiActivity.class);
                startActivity(addEmojiIntent);
            }
        });


        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
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

            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), " Sorry! No Internet!!!", Snackbar.LENGTH_LONG);

            // Changing message text color
            snackbar.setActionTextColor(Color.BLUE);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

            Toast.makeText(getApplicationContext(), "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();

        }
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = this.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("LoginActivity", "Error writing bitmap", e);
        }
        return imageFile;
    }

    public class GallaryAdapter extends BaseAdapter {


        private Context context;
        private ArrayList<EmojiDataset> emojiDatasets;

        public GallaryAdapter(Context context, ArrayList<EmojiDataset> emojiDatasets) {
            this.context = context;
            this.emojiDatasets = emojiDatasets;
        }

        public int getCount() {
            return this.emojiDatasets.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(this.context);
                imageView.setLayoutParams(new GridView.LayoutParams(115, 115));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                imageView = (ImageView) convertView;
            }

//        imageView.setImageBitmap(this.bitmapList.get(position));

//        Picasso.with(context) //Context
//                .load(this.bitmapList.get(position)) //URL/FILE
//                .resize(115, 115)
//                .centerCrop()
//                .into(imageView);//an ImageView Object to show the loaded image

//            imageView.setImageBitmap(getBitmapFromURL(this.emojiDatasets.get(position).getUrl()));

            Picasso.with(context).load(this.emojiDatasets.get(position).getUrl()).resize(115,115).error(R.drawable.ic_vp_smileys).placeholder(R.drawable.ic_vp_smileys).into(imageView);

            return imageView;
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
            ViewHolder view;
            final LayoutInflater layoutInflater = LayoutInflater.from(EmojiGridActivity.this);

            if(convertView==null)
            {
                view = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.emojilist_row, null);

                view.img_emojilist = (ImageView) convertView.findViewById(R.id.img_emojilist);

                convertView.setTag(view);
            }
            else
            {
                view = (ViewHolder) convertView.getTag();
            }

            Picasso.with(EmojiGridActivity.this).load(emojiDatasets.get(position).getUrl()).resize(115,115).error(R.drawable.ic_vp_smileys).placeholder(R.drawable.ic_vp_smileys).into(view.img_emojilist);

            return convertView;
        }
    }
}
