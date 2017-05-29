package com.barapp.simulator.Adapter;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.barapp.simulator.Object_Getter_Setter.OwnerMenu_Object;
import com.barapp.simulator.PurchaseDetail_User;
import com.barapp.simulator.R;
import com.barapp.simulator.utils.Const;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class OwnerMenu_Adapter extends RecyclerView.Adapter<OwnerMenu_Adapter.DataObject_postHolder> {

    private static String LOG_TAG = "Adapter_employeeList";
    static private ArrayList<OwnerMenu_Object> mDataset;
    static private Context mContext;
    static String type ;
    NotificationManager mNotifyManager;


    public static class DataObject_postHolder extends RecyclerView.ViewHolder {

        TextView  txt_productname , txt_quantity , txt_unitprice ;
        ImageView  img_icon;
        LinearLayout linear;

        public DataObject_postHolder(final View itemView) {
            super(itemView);

            img_icon = (ImageView)itemView.findViewById(R.id.icon);

            txt_productname = (TextView)itemView.findViewById(R.id.product_name);
            txt_quantity    = (TextView)itemView.findViewById(R.id.quantity);
            txt_unitprice   = (TextView)itemView.findViewById(R.id.unit_price);

            linear = (LinearLayout)itemView.findViewById(R.id.linear);

        }


    }


    public OwnerMenu_Adapter(ArrayList<OwnerMenu_Object> myDataset , Context context) {

        mDataset = myDataset;
        mContext = context;
        this.type = type;
    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent , int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_ownermenu_list , parent , false);
        DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);
        mNotifyManager = (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder , final int position) {


        holder.txt_productname.setText(mDataset.get(position).getProduct_name());
        holder.txt_quantity.setText("Qty : "+mDataset.get(position).getQuantity());
        holder.txt_unitprice.setText(mDataset.get(position).getUnit_price());


        if(!mDataset.get(position).getProduct_img().equals("")) {
            Picasso.with(mContext)
                    .load(Const.IMAGE_URL+mDataset.get(position).getProduct_img())
                    .placeholder(R.drawable.animated_loading_icon) // optional
                    .error(R.drawable.no_image_available)         // optional
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.img_icon);
        }


        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = holder.getAdapterPosition();

                Intent intent =new Intent(mContext , PurchaseDetail_User.class);
                intent.putExtra("product_id" , mDataset.get(pos).getId());
                mContext.startActivity(intent);

            }
        });

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
