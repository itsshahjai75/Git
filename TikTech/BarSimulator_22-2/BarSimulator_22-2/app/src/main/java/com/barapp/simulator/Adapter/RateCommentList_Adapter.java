package com.barapp.simulator.Adapter;

/**
 * Created by Shanni on 1/31/2017.
 */

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.barapp.simulator.Object_Getter_Setter.RateComment_Object;
import com.barapp.simulator.R;
import com.barapp.simulator.utils.Const;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;


public class RateCommentList_Adapter extends RecyclerView.Adapter<RateCommentList_Adapter.DataObject_postHolder> {

    private static String LOG_TAG = "Adapter_employeeList";
    static private ArrayList<RateComment_Object> mDataset;
    static private Context mContext;
    static String type ;
    NotificationManager mNotifyManager;
    SharedPreferences sharepref;


    public static class DataObject_postHolder extends RecyclerView.ViewHolder {

        TextView  txt_username , txt_description;
        CircleImageView img_icon;
        ImageView rate1  ,rate2 ,rate3 ,rate4 ,rate5;

        public DataObject_postHolder(final View itemView) {
            super(itemView);

            img_icon = (CircleImageView) itemView.findViewById(R.id.profile_image);

            txt_username = (TextView)itemView.findViewById(R.id.username);
            txt_description    = (TextView)itemView.findViewById(R.id.description);

            rate1 = (ImageView)itemView.findViewById(R.id.rate_img1);
            rate2 = (ImageView)itemView.findViewById(R.id.rate_img2);
            rate3 = (ImageView)itemView.findViewById(R.id.rate_img3);
            rate4 = (ImageView)itemView.findViewById(R.id.rate_img4);
            rate5 = (ImageView)itemView.findViewById(R.id.rate_img5);
        }

    }


    public RateCommentList_Adapter(ArrayList<RateComment_Object> myDataset , Context context) {
        mDataset = myDataset;
        mContext = context;
    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent , int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ratecomment_list , parent , false);
        DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);
        mNotifyManager = (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder , final int position) {

        if(!mDataset.get(position).getProfileimg_url().equals("")) {
            Picasso.with(mContext)
                    .load(Const.IMAGE_URL+mDataset.get(position).getProfileimg_url())
                    .placeholder(R.drawable.noimage) // optional
                    .error(R.drawable.noimage)         // optional
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.img_icon);
        }

        Log.e("image url" , mDataset.get(position).getProfileimg_url() );
        holder.txt_username.setText(mDataset.get(position).getUsername());
        holder.txt_description.setText(mDataset.get(position).getDescription());

        Log.d("Rate Listing Adpt",mDataset.get(position).getRating());
        setRatings(holder , Double.parseDouble(mDataset.get(position).getRating()));
    }


    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    private void setRatings(DataObject_postHolder dataObjectHolder , double rating) {

        if (rating == 0.5) {
            dataObjectHolder.rate1.setImageResource(R.drawable.star_enable_half);
            dataObjectHolder.rate2.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate3.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate4.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 0.5 && rating <= 1) {
            dataObjectHolder.rate1.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate2.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate3.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate4.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 1 && rating <= 1.5) {
            dataObjectHolder.rate1.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate2.setImageResource(R.drawable.star_enable_half);
            dataObjectHolder.rate3.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate4.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating > 1.5 && rating <= 2) {
            dataObjectHolder.rate1.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate2.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate3.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate4.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating >= 2 && rating <= 2.5) {
            dataObjectHolder.rate1.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate2.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate3.setImageResource(R.drawable.star_enable_half);
            dataObjectHolder.rate4.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating >= 2.5 && rating <= 3) {
            dataObjectHolder.rate1.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate2.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate3.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate4.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating >= 3 && rating <= 3.5) {
            dataObjectHolder.rate1.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate2.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate3.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate4.setImageResource(R.drawable.star_enable_half);
            dataObjectHolder.rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating >= 3.5 && rating <= 4) {
            dataObjectHolder.rate1.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate2.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate3.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate4.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate5.setImageResource(R.drawable.star_diseble);
        } else if (rating >= 4 && rating <= 4.5) {
            dataObjectHolder.rate1.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate2.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate3.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate4.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate5.setImageResource(R.drawable.star_enable_half);
        } else if (rating >= 4.5) {
            dataObjectHolder.rate1.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate2.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate3.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate4.setImageResource(R.drawable.star_enable);
            dataObjectHolder.rate5.setImageResource(R.drawable.star_enable);
        }else{
            dataObjectHolder.rate1.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate2.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate3.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate4.setImageResource(R.drawable.star_diseble);
            dataObjectHolder.rate5.setImageResource(R.drawable.star_diseble);
        }
    }






}
