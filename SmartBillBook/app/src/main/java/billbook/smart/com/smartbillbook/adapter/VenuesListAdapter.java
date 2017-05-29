package billbook.smart.com.smartbillbook.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import billbook.smart.com.smartbillbook.R;
import billbook.smart.com.smartbillbook.modelpojo.VenuesListModel;


/**
 * Created by Testing on 18-Oct-16.
 */




public class VenuesListAdapter extends RecyclerView
        .Adapter<VenuesListAdapter
        .DataObject_postHolder> {
    static private ArrayList<VenuesListModel> mDataset;
    static private Context mContext;

    SharedPreferences sharepref;



    public static class DataObject_postHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_title,tv_capacity,tv_addressline1,tv_addressline2 ,tv_price,tvdemo ,tv_viewmore;
        ImageView img_venue;


        public DataObject_postHolder(final View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_addressline1 = (TextView) itemView.findViewById(R.id.tv_addressline1);
            tv_addressline2 = (TextView) itemView.findViewById(R.id.tv_addressline2);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_capacity= (TextView) itemView.findViewById(R.id.tv_capacity);
            tvdemo = (TextView) itemView.findViewById(R.id.tvdemo);

            img_venue=(ImageView)itemView.findViewById(R.id.img_venue);
            img_venue.setVisibility(View.GONE);

            // Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* Intent edit = new Intent(itemView.getContext(),PoolDetail_screen.class);
                    edit.putExtra("pool_id",mDataset.get(getAdapterPosition()).getPool_id());
                    edit.putExtra("creator_id",mDataset.get(getAdapterPosition()).getUser_id());
                    mContext.startActivity(edit);*/
                }
            });



        }

        @Override
        public void onClick(View v) {
            //     myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public VenuesListAdapter(ArrayList<VenuesListModel> myDataset, Context context) {
        mDataset = myDataset;
        mContext=context;
    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_venue_list, parent, false);
        DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder,final int position) {

        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref",mContext.MODE_PRIVATE);

        holder.tv_title.setText("Party : "+mDataset.get(position).getPartyname());
        holder.tv_addressline1.setText("Bill No: "+mDataset.get(position).getBillnumber()+" , Paid : "+mDataset.get(position).getPaid());
        holder.tv_addressline2.setVisibility(View.GONE);
        holder.tv_price.setText("Rs : "+mDataset.get(position).getAmount());
        holder.tv_capacity.setText("Note :\n"+mDataset.get(position).getNote());

       /*
        try {

            if(mDataset.get(position).getPhoto_url().toString()!=null) {
            final String venue_pic= mDataset.get(position).getPhoto_url().toString()!=null ? mDataset.get(position).getPhoto_url().toString() : null;
            //Log.d("imageURL====",venue_pic);
                *//*Glide.with(mContext)
                        .load(venue_pic)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .crossFade()
                        .dontAnimate()
                        .skipMemoryCache(true)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.img_venue);*//*

                Glide.with(mContext)
                        .load(venue_pic)
                        .asBitmap()
                        .placeholder(R.drawable.loading_image_pic)
                        .error(R.drawable.no_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                // Do something with bitmap here.
                                holder.img_venue.setImageBitmap(bitmap);
                                //Log.e("GalleryAdapter","Glide getMedium ");

                                Glide.with(mContext)
                                        .load(venue_pic)
                                        .asBitmap()
                                        .error(R.drawable.no_image)
                                        .placeholder(R.drawable.loading_image_pic)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                                // Do something with bitmap here.
                                                holder.img_venue.setImageBitmap(bitmap);
                                                Log.e("GalleryAdapter","Glide getLarge ");
                                            }
                                        });
                            }
                        });
            }else{

            }
            }catch (Exception expbitmap){
            expbitmap.printStackTrace();
        }
*/

    }

    public void addItem(VenuesListModel dataObj, int index) {
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