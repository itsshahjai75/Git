package com.barapp.simulator.Adapter;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.barapp.simulator.Object_Getter_Setter.PurchaseDetailList_Object;
import com.barapp.simulator.R;

import java.util.ArrayList;


public class ProductMixture_Adapter extends RecyclerView.Adapter<ProductMixture_Adapter.DataObject_postHolder> {

    private static String LOG_TAG = "Adapter_employeeList";
    static private ArrayList<PurchaseDetailList_Object> mDataset;
    static private Context mContext;
    static String type ;
    NotificationManager mNotifyManager;
    SharedPreferences sharepref;


    public static class DataObject_postHolder extends RecyclerView.ViewHolder {

        TextView  txt_productname , txt_price;



        public DataObject_postHolder(final View itemView) {
            super(itemView);

            txt_productname = (TextView)itemView.findViewById(R.id.wine_name);

        }


    }


    public ProductMixture_Adapter(ArrayList<PurchaseDetailList_Object> myDataset , Context context) {

        mDataset = myDataset;
        mContext = context;
    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent , int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_productmixture_user , parent , false);
        DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);
        mNotifyManager = (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder , final int position) {

        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref" , mContext.MODE_PRIVATE);

        holder.txt_productname.setText(mDataset.get(position).getWine_name());

    }


    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }









}
