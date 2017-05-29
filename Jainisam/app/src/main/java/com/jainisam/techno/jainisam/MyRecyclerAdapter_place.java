package com.jainisam.techno.jainisam;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MyRecyclerAdapter_place extends RecyclerView
        .Adapter<MyRecyclerAdapter_place
        .DataObject_postHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObject_post> mDataset;
    private Context mContext;
    NotificationManager mNotifyManager;

    public static class DataObject_postHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView post_by;
        TextView post_details,post_time;
        ImageView post_img;

        public DataObject_postHolder(final View itemView) {
            super(itemView);



            post_by = (TextView) itemView.findViewById(R.id.tv_postby);
            post_details = (TextView) itemView.findViewById(R.id.tv_postdetails);
            post_time = (TextView) itemView.findViewById(R.id.tv_post_time);
            post_img = (ImageView) itemView.findViewById(R.id.img_post);





            // Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);





        }

        @Override
        public void onClick(View v) {
       //     myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public MyRecyclerAdapter_place(ArrayList<DataObject_post> myDataset, Context context) {
        mDataset = myDataset;
        mContext=context;


    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row_cardview_allinone, parent, false);
            DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);

             mNotifyManager = (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);


        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder,final int position) {
        holder.post_by.setText(mDataset.get(position).getmText1());
        holder.post_details.setText(mDataset.get(position).getmText2());
        holder.post_time.setText(mDataset.get(position).getmTime());

        if(mDataset.get(position).getmText2().isEmpty()
                || mDataset.get(position).getmText2().equalsIgnoreCase("null")/*
                || mDataset.get(position).getmText2().equalsIgnoreCase("NA")*/){
            holder.post_details.setVisibility(View.GONE);
        }else{
            holder.post_details.setVisibility(View.VISIBLE);
        }

    }

    public void addItem(DataObject_post dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }







}