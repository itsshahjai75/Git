package com.barapp.simulator.Adapter;

/**
 * Created by Shanni on 1/23/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.barapp.simulator.Object_Getter_Setter.UserShopList_Object;
import com.barapp.simulator.PurchaseDetail_User;
import com.barapp.simulator.R;
import com.barapp.simulator.utils.Const;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class UserShopList_Adapter extends BaseAdapter {

    ArrayList<UserShopList_Object> list_wine = new ArrayList();
    LayoutInflater inflater;
    Context context;
    int count =0;

    public UserShopList_Adapter(Context context, ArrayList<UserShopList_Object> myList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_usermenu_items, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        UserShopList_Object currentListData = list_wine.get(position);
        if(currentListData.getProduct_name().length()>12){
            mViewHolder.tv_productname.setText(currentListData.getProduct_name().substring(0,12)+"...");
        }else{

            mViewHolder.tv_productname.setText(currentListData.getProduct_name());
        }

        if(currentListData.getBarname().length()>10){
            mViewHolder.tv_barname.setText(currentListData.getBarname().substring(0,10)+"...");
        }else{

            mViewHolder.tv_barname.setText(currentListData.getBarname());
        }

        mViewHolder.tv_productprice.setText(currentListData.getProduct_price());

        if(!list_wine.get(position).getImg_url().equals("")) {
            Picasso.with(context)
                    .load(Const.IMAGE_URL+list_wine.get(position).getImg_url())
                    .placeholder(R.drawable.wine_bottle)
                    .error(R.drawable.wine_bottle)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(mViewHolder.img_wine);
        }

         //mViewHolder.linear_main.setId(position);
         mViewHolder.linear_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //int pos = v.getId();

                Intent intent =new Intent(context , PurchaseDetail_User.class);
                intent.putExtra("product_id" , list_wine.get(position).getId());
                intent.putExtra("products" , list_wine.get(position).getProducts());
                intent.putExtra("products_prices" , list_wine.get(position).getProducts_prices());
                intent.putExtra("purchase" , list_wine.get(position).getIsPurchase());
                intent.putExtra("quantity" , list_wine.get(position).getQuantity());
                context.startActivity(intent);
            }
        });


       /* mViewHolder.linear_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });*/
        YoYo.with(Techniques.ZoomIn).duration(1000).playOn(convertView);

        return convertView;
    }

    private class MyViewHolder {
        TextView tv_productname ,tv_barname ,tv_productprice;
        ImageView img_wine;
        LinearLayout linear_main;

        public MyViewHolder(View item) {
            tv_productname = (TextView) item.findViewById(R.id.product_name);
            tv_barname = (TextView) item.findViewById(R.id.barname);
            tv_productprice  = (TextView)item.findViewById(R.id.product_price);
            img_wine = (ImageView) item.findViewById(R.id.img_icon);
            linear_main = (LinearLayout)item.findViewById(R.id.linearmain);
        }
    }


}
