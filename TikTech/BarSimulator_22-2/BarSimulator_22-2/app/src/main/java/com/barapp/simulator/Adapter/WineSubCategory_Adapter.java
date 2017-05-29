package com.barapp.simulator.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.barapp.simulator.Object_Getter_Setter.WineSubCategory_Object;
import com.barapp.simulator.R;
import com.barapp.simulator.utils.Const;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class WineSubCategory_Adapter extends BaseAdapter {

    ArrayList<WineSubCategory_Object> list_wine = new ArrayList<WineSubCategory_Object>();
    LayoutInflater inflater;
    Context context;
    int count =0;

    public WineSubCategory_Adapter(Context context, ArrayList<WineSubCategory_Object> myList) {
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
            convertView = inflater.inflate(R.layout.row_winesubcategory_list, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        WineSubCategory_Object currentListData = list_wine.get(position);
        mViewHolder.tv_name.setText(currentListData.getWine_subcategory_name());
        mViewHolder.tv_price.setText("$"+currentListData.getWine_subcategory_price());

        if(!list_wine.get(position).getImage_url().equals("")) {

            Picasso.with(context).load(Const.IMAGE_URL+list_wine.get(position).getImage_url()).placeholder(R.drawable.wine_bottle)
                    .error(R.drawable.noimage).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(mViewHolder.img_wine);
        }


        if(list_wine.get(position).is_selected() == true){
            mViewHolder.rel_selected.setVisibility(View.VISIBLE);
        }else{
            mViewHolder.rel_selected.setVisibility(View.GONE);
        }


        mViewHolder.rel_main.setId(position);
        mViewHolder.rel_selected.setId(position);
        mViewHolder.tv_name1.setId(position);

        mViewHolder.rel_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = v.getId();
                int selec_pos =   mViewHolder.rel_selected.getId();

                Log.e("pos " , pos+" "+selec_pos);
                if (pos == selec_pos) {
                    if (mViewHolder.rel_selected.getVisibility() == View.VISIBLE) {
                        mViewHolder.rel_selected.setVisibility(View.GONE);
                        list_wine.set(pos , new WineSubCategory_Object(list_wine.get(pos).getId() , list_wine.get(pos).getImage_url(), list_wine.get(pos).getWine_subcategory_name(), list_wine.get(pos).getWine_subcategory_price(), false));
                    } else {
                        mViewHolder.rel_selected.setVisibility(View.VISIBLE);
                        mViewHolder.tv_name1.setText(list_wine.get(pos).getWine_subcategory_name());
                        list_wine.set(pos , new WineSubCategory_Object(list_wine.get(pos).getId() , list_wine.get(pos).getImage_url(), list_wine.get(pos).getWine_subcategory_name(), list_wine.get(pos).getWine_subcategory_price(), true));
                    }
                }
            }
        });

        YoYo.with(Techniques.ZoomIn).duration(1000).playOn(convertView);

        return convertView;
    }

    private class MyViewHolder {
        TextView tv_name, tv_price ,tv_name1;
        ImageView img_wine;
        RelativeLayout rel_main ,rel_selected;

        public MyViewHolder(View item) {
            tv_name = (TextView) item.findViewById(R.id.subcategory_wine_name);
            tv_price = (TextView) item.findViewById(R.id.subcategory_wine_price);
            tv_name1  = (TextView)item.findViewById(R.id.subcategory_wine_name1);
            img_wine = (ImageView) item.findViewById(R.id.subcategory_wine_image);

            rel_main = (RelativeLayout)item.findViewById(R.id.rel_main);
            rel_selected = (RelativeLayout)item.findViewById(R.id.rel_selected);
        }
    }


}