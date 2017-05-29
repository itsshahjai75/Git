package com.barapp.simulator.Adapter;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.barapp.simulator.Object_Getter_Setter.PurchaseDetailList_Object;
import com.barapp.simulator.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PurchaseDetailList_Adapter extends RecyclerView.Adapter<PurchaseDetailList_Adapter.DataObject_postHolder> {

    private static String LOG_TAG = "Adapter_employeeList";
    static private ArrayList<PurchaseDetailList_Object> mDataset;
    static private Context mContext;
    static String type ;
    NotificationManager mNotifyManager;
    SharedPreferences sharepref;
    purchaseDetail  p;

    public static class DataObject_postHolder extends RecyclerView.ViewHolder {

        TextView  txt_productname , txt_price;
        ImageView  img_icon ,img_close ,img_coin;


        public DataObject_postHolder(final View itemView) {
            super(itemView);

            img_icon = (ImageView)itemView.findViewById(R.id.icon);

            txt_productname = (TextView)itemView.findViewById(R.id.wine_name);
            txt_price    = (TextView)itemView.findViewById(R.id.wine_price);
            img_icon   = (ImageView) itemView.findViewById(R.id.wine_icon);
            img_close  = (ImageView)itemView.findViewById(R.id.close);
            img_coin  = (ImageView)itemView.findViewById(R.id.coin);

        }


    }


    public PurchaseDetailList_Adapter(ArrayList<PurchaseDetailList_Object> myDataset , Context context , purchaseDetail  p) {

        mDataset = myDataset;
        mContext = context;
        this.type = type;
        this.p = p ;
    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent , int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_owner_purchaselist , parent , false);
        DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);
        mNotifyManager = (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder , final int position) {

        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref" , mContext.MODE_PRIVATE);

        holder.txt_productname.setText(mDataset.get(position).getWine_name());
        holder.txt_price.setText(mDataset.get(position).getWine_price());

        if(!mDataset.get(position).getWine_image().equals("")) {
            Picasso.with(mContext).load(mDataset.get(position).getWine_image()).placeholder(R.drawable.noimage)
                    .error(R.drawable.noimage).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).into(holder.img_icon);
        }

        holder.img_close.setId(position);
        holder.img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.img_close.getId();
                deleteItem(pos);
                p.onDelete();
            }
        });


        if( p == null){
            holder.img_close.setVisibility(View.INVISIBLE);
            holder.txt_price.setText("");
            holder.img_coin.setVisibility(View.GONE);
        }

    }


    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }




    public interface purchaseDetail{

        public void onDelete();
    }






}
