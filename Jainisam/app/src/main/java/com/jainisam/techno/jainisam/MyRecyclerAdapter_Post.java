package com.jainisam.techno.jainisam;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter_Post extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DataObject_post> mList;
    public static final int NO_IMAGE = 0;
    public static final int WITH_IMAGE = 1;
    NotificationManager mNotifyManager;
    Context context;


    public MyRecyclerAdapter_Post(List<DataObject_post> list) {
        this.mList = list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        mNotifyManager =
                (NotificationManager) parent.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        switch (viewType) {
            case NO_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_cardview_home_withoutimage, parent, false);
                context = parent.getContext();
                return new NoimageViewHolder(view);
            case WITH_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_cardview_home, parent, false);
                context = parent.getContext();
                return new WithimageViewHolder(view);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DataObject_post object = mList.get(position);

        if (object != null) {
            switch (object.getType()) {
                case NO_IMAGE:
                    ((NoimageViewHolder) holder).post_by.setText(object.getmText1());
                    ((NoimageViewHolder) holder).post_details.setText(object.getmText2());
                    ((NoimageViewHolder) holder).post_time.setText(object.getmTime());


                    break;
                case WITH_IMAGE:
                    ((WithimageViewHolder) holder).post_by.setText(object.getmText1());
                    ((WithimageViewHolder) holder).post_details.setText(object.getmText2());
                    ((WithimageViewHolder) holder).post_time.setText(object.getmTime());




                    try {

                        String postimage=object.getmBitmap();


                        Picasso.with(context)
                                .load("http://www.technocratsappware.com/androidapps/images/"+postimage)
                                .placeholder(R.drawable.loading) // optional
                                .error(R.drawable.loading)         // optional
                                .memoryPolicy(MemoryPolicy.NO_CACHE )
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(((WithimageViewHolder)holder).post_img);


                    }catch (Exception expbitmap){
                        expbitmap.printStackTrace();
                    }

                    ((WithimageViewHolder) holder).post_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final Dialog dialog = new Dialog(v.getContext());

                            dialog.setContentView(R.layout.image_post_lyout);

                            dialog.setCanceledOnTouchOutside(false);

                            final ImageView img_post_view = (ImageView) dialog.findViewById(R.id.img_post_view);
                            Button btn_download = (Button) dialog.findViewById(R.id.btn_download_img);

                            String postimage=object.getmBitmap();


                            Picasso.with(context)
                                    .load("http://www.technocratsappware.com/androidapps/images/"+postimage)
                                    .placeholder(R.drawable.loading) // optional
                                    .error(R.drawable.loading)         // optional
                                    .memoryPolicy(MemoryPolicy.NO_CACHE )
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .into(img_post_view);

                   /* img_post_view.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {

                        @Override
                        public void onMove() {
                            PointF point = img_post_view.getScrollPosition();
                            RectF rect = img_post_view.getZoomedRect();
                            float currentZoom = img_post_view.getCurrentZoom();
                            boolean isZoomed = img_post_view.isZoomed();
                        }
                    });*/

                            btn_download.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(final View v) {


                                    final int id = 1;

                                    final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                                    mBuilder.setContentTitle("Picture Download")
                                            .setContentText("Download in progress")
                                            .setSmallIcon(R.drawable.ic_notification_icon);
// Start a lengthy operation in a background thread
                                    new Thread(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    int incr = 100;
                                                    // Do the "lengthy" operation 20 times
                                                    mBuilder.setProgress(100, incr, false);
                                                    // Displays the progress bar for the first time.
                                                    mNotifyManager.notify(id, mBuilder.build());
                                                    // Sleeps the thread, simulating an operation
                                                    // that takes time
                                                    try {

                                                        try {
                                                            URL url = new URL("http://www.technocratsappware.com/androidapps/images/"+object.getmBitmap());
                                                            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                                                            File direct = new File(Environment.getExternalStorageDirectory() + "/JainS");

                                                            if (!direct.exists()) {
                                                                File wallpaperDirectory = new File("/sdcard/JainS/");
                                                                wallpaperDirectory.mkdirs();
                                                            }

                                                            File file = new File(new File("/sdcard/JainS/"), "JainS" + String.valueOf(System.currentTimeMillis()) + ".jpeg");
                                                            if (file.exists()) {
                                                                // file.delete();
                                                            }
                                                            try {
                                                                FileOutputStream out = new FileOutputStream(file);
                                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                                                out.flush();
                                                                out.close();

                                                                MediaStore.Images.Media.insertImage(v.getContext().getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());


                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        } catch (Exception e11) {
                                                            e11.printStackTrace();

                                                        } finally {

                                                            //post_img.setDrawingCacheEnabled(false);
                                                        }


                                                        // Sleep for 5 seconds
                                                        //Thread.sleep(5*1000);

                                                    } catch (Exception e) {
                                                        //Log.d("donloading", "sleep failure");
                                                    }

                                                    // When the loop is finished, updates the notification
                                                    mBuilder.setContentText("Download complete")
                                                            // Removes the progress bar
                                                            .setProgress(0, 0, false);

                                                    mNotifyManager.notify(id, mBuilder.build());

                                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                                    intent.setType("image/*");
                                                    // This pending intent will open after notification click
                                                    PendingIntent pi= PendingIntent.getService(context, 0, intent, 0);
                                                    mBuilder.setContentIntent(pi);

                                                }
                                            }
// Starts the thread by calling the run() method in its Runnable
                                    ).start();
                                    Toast.makeText(v.getContext(), "Image Saved.\nFind in Files/Gallery.", Toast.LENGTH_LONG).show();

                                    dialog.dismiss();


//====================================================================================================
                                    //========================================================================================================

                                }
                            });

                            dialog.show();
                        }
                    });







                    break;
            }
        }
    }
    @Override
    public int getItemCount() {
        if (mList == null)
            return 0;
        return mList.size();
    }
    @Override
    public int getItemViewType(int position) {
        if (mList != null) {
            DataObject_post object = mList.get(position);
            if (object != null) {
                return object.getType();
            }
        }
        return 0;
    }
    public static class NoimageViewHolder extends RecyclerView.ViewHolder {
        TextView post_by;
        TextView post_details,post_time;
        public NoimageViewHolder(View itemView) {
            super(itemView);
            post_by = (TextView) itemView.findViewById(R.id.tv_postby);
            post_details = (TextView) itemView.findViewById(R.id.tv_postdetails);
            post_time = (TextView) itemView.findViewById(R.id.tv_post_time);
        }
    }
    public static class WithimageViewHolder extends RecyclerView.ViewHolder {
        TextView post_by;
        TextView post_details,post_time;
        ImageView post_img;
        public WithimageViewHolder(View itemView) {
            super(itemView);
            post_by = (TextView) itemView.findViewById(R.id.tv_postby);
            post_details = (TextView) itemView.findViewById(R.id.tv_postdetails);
            post_time = (TextView) itemView.findViewById(R.id.tv_post_time);
            post_img = (ImageView) itemView.findViewById(R.id.img_post);



        }
    }
}