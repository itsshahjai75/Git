package com.jainisam.techno.jainisam;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObject> mDataset;
    private static MyClickListener myClickListener;
    public Typeface tf,tf2;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView lable;
        TextView title;
        TextView discription;

        public DataObjectHolder(View itemView) {
            super(itemView);



            lable = (TextView) itemView.findViewById(R.id.tv_leftlable);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            discription = (TextView) itemView.findViewById(R.id.tv_description);


           // Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<DataObject> myDataset, Typeface tf, Typeface tf2) {
        mDataset = myDataset;
        this.tf = tf;
        this.tf2 = tf2;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.lable.setText(mDataset.get(position).getmText1());
        holder.lable.setTypeface(tf,Typeface.BOLD);
        holder.title.setText(mDataset.get(position).getmText2());
        holder.title.setTypeface(tf2,Typeface.BOLD);
        holder.discription.setText(mDataset.get(position).getmText3());
        holder.discription.setTypeface(tf,Typeface.BOLD);
    }

    public void addItem(DataObject dataObj, int index) {
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

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}