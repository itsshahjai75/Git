package com.arihantmartadmin.jay.arihantmart_admin;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import net.colindodd.toggleimagebutton.ToggleImageButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import me.himanshusoni.quantityview.QuantityView;

public class MyRecyclerAdapter_Itemlist extends RecyclerView
        .Adapter<MyRecyclerAdapter_Itemlist
        .DataObject_postHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private static ArrayList<DataObject_Itemlist> mDataset;
    private Context mContext;
    NotificationManager mNotifyManager;

    SharedPreferences sharepref;

    private int pricePerProduct = 180;
    String Price,res,str_itemid,str_itemname,str_useremail;
    String str_qnty="0";



    public static class DataObject_postHolder extends RecyclerView.ViewHolder
            {
        TextView itemname;
        TextView price,saving;
        ImageView item_img;

        TextView btn_addtocart,btn_share;
        ToggleImageButton tb_like;

        public DataObject_postHolder(final View itemView) {
            super(itemView);



            itemname= (TextView) itemView.findViewById(R.id.tv_itemname);
            price = (TextView) itemView.findViewById(R.id.tv_price);
            saving = (TextView) itemView.findViewById(R.id.tv_saving);
            item_img = (ImageView) itemView.findViewById(R.id.img_item);

            btn_addtocart = (TextView) itemView.findViewById(R.id.btn_addtocart);
            btn_share = (TextView) itemView.findViewById(R.id.btn_share);
            tb_like=(ToggleImageButton)itemView.findViewById(R.id.tb_like);


            // Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(itemView.getContext(),"itemcode="+mDataset.get(getAdapterPosition()).getmitemid().toString(),Toast.LENGTH_LONG).show();

                    Intent edit = new Intent(itemView.getContext(),Edit_item.class);
                    edit.putExtra("itemcode",mDataset.get(getAdapterPosition()).getmitemid().toString());
                   itemView.getContext().startActivity(edit);


                }
            });





        }



    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public MyRecyclerAdapter_Itemlist(ArrayList<DataObject_Itemlist> myDataset, Context context) {
        mDataset = myDataset;
        mContext=context;


    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {



            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.items_list_cardview, parent, false);

        FontChangeCrawler fontChanger = new FontChangeCrawler(mContext.getAssets(), "fonts/ProductSans-Regular.ttf");
        //fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        fontChanger.replaceFonts((ViewGroup)view);

        DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);

             mNotifyManager =
                (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);


        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder,final int position) {

        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref",mContext.MODE_PRIVATE);



        holder.itemname.setText(mDataset.get(position).getmitemname());
        holder.price.setText(mDataset.get(position).getmitemprice());
        holder.saving.setText(mDataset.get(position).getmSaving());


        String str_imageresourse = mDataset.get(position).getmBitmap();
        int imageResourceId = mContext.getResources().getIdentifier(str_imageresourse, "drawable", mContext.getPackageName());



        try {

            String itemname=mDataset.get(position).getmitemname();
            itemname=itemname.replace(" ","");
            itemname=itemname.substring(0,itemname.indexOf("("));

            Picasso.with(mContext)
                    .load("http://arihantmart.com/androidapp/images/"+itemname+".jpg")
                    .placeholder(imageResourceId) // optional
                    .error(imageResourceId)         // optional
                    .memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into( holder.item_img);


        }catch (Exception expbitmap){
            expbitmap.printStackTrace();

        }

        Price= mDataset.get(position).getmitemprice().toString();
        Price=Price.substring(Price.indexOf("Our Price: ₹ ")+13);


        pricePerProduct=Integer.parseInt(Price);


        holder.btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_itemid=mDataset.get(position).getmitemid();
                str_itemname=mDataset.get(position).getmitemname();
                str_useremail=sharepref.getString("key_useremail", "null");

                if(str_useremail.equalsIgnoreCase("demo@demo.com")){

                    Toast.makeText(mContext,"Please Register First, You are Demo user.",Toast.LENGTH_LONG).show();

                }else {
                  }

                //Toast.makeText(mContext,"quantity  is =="+str_qnty,Toast.LENGTH_LONG).show();
            }
        });

        holder.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intentshare = new Intent(Intent.ACTION_SEND);
                intentshare.setType("text/plain");
                intentshare.putExtra(Intent.EXTRA_TEXT, "Name : "+mDataset.get(position).getmitemname()
                +"\nPrice: "+mDataset.get(position).getmitemprice()
                        +"\n\nBotad's General Store Online. \"Arihant Mart\" all kind of products available just download and Enjoy.\n\n" + "https://play.google.com/store/apps/details?id=arihantmart.techno.arihantmart&hl=en");
                mContext.startActivity(Intent.createChooser(intentshare, "Share"));

                //Toast.makeText(mContext," Share Clicked ",Toast.LENGTH_LONG).show();
            }
        });






        holder.tb_like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Toast.makeText(mContext, "Liked: ", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(mContext, "Unliked: ", Toast.LENGTH_LONG).show();
                }


            }
        });


    }

    public void addItem(DataObject_Itemlist dataObj, int index) {
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