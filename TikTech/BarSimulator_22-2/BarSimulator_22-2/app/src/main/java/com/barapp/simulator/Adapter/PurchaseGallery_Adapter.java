package com.barapp.simulator.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.barapp.simulator.Object_Getter_Setter.Gallery_Object;
import com.barapp.simulator.R;
import com.barapp.simulator.utils.Const;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PurchaseGallery_Adapter extends BaseAdapter {

    ArrayList<Gallery_Object> list_wine = new ArrayList();
    LayoutInflater inflater;
    Context context;
    int count =0;

    public PurchaseGallery_Adapter(Context context, ArrayList<Gallery_Object> myList) {
        this.list_wine = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return list_wine.size();
    }

    @Override
    public Object getItem(int position) {
        return list_wine.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_gallery_list, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }


        if(!list_wine.get(position).getFile().equals("")) {
            Picasso.with(context)
                    .load(Const.IMAGE_URL+list_wine.get(position).getFile())
                    .placeholder(R.drawable.wine_bottle) // optional
                    .error(R.drawable.noimage)         // optional
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(mViewHolder.img_wine);
        }


        YoYo.with(Techniques.ZoomIn).duration(1000).playOn(convertView);

        return convertView;
    }

    private class MyViewHolder {
        ImageView img_wine;

        public MyViewHolder(View item) {
            img_wine = (ImageView) item.findViewById(R.id.product_image);
        }
    }

}