package billbook.smart.com.smartbillbook.adapter;


import android.app.Activity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import billbook.smart.com.smartbillbook.R;
import billbook.smart.com.smartbillbook.modelpojo.IdNameUrlGrideRaw;
import billbook.smart.com.smartbillbook.utils.Const;


public class AmenitiesGridListAdapter/* extends BaseAdapter*/ {

   /* private ArrayList<IdNameUrlGrideRaw> vault_items;
    private Activity context;
    private LayoutInflater inflater;

    public AmenitiesGridListAdapter(Activity context, ArrayList<IdNameUrlGrideRaw> vault_items) {
        this.context = context;
        this.vault_items = vault_items;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return vault_items.size();
    }

    @Override
    public Object getItem(int position) {
        return vault_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.amenities_grid_raw, null, false);
            holder = new ViewHolder();

            holder.linear = (LinearLayout) convertView.findViewById(R.id.layout);
            holder.icon = (ImageView) convertView.findViewById(R.id.mainicon);
            holder.title = (CheckBox) convertView.findViewById(R.id.cb_title);
            holder.title.setClickable(false);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int width =  (getScreenWidth() - 21)/3;

       // holder.linear.setLayoutParams(new LinearLayout.LayoutParams(width , width-15));

        //holder.linear.setBackgroundColor(Color.parseColor(vault_items.get(position).getColors()));

        Glide.with(context).load(Const.WEBSITE_PIC_URL+"/img/amenity_icons/"+vault_items.get(position).getUrl())
                .thumbnail(0.1f)
                .placeholder(R.drawable.loading_image_pic)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( holder.icon);

        holder.title.setText(vault_items.get(position).getTitle());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!holder.title.isChecked()){
                    holder.title.setChecked(true);
                    str_pref_amenities_id = str_pref_amenities_id+","+vault_items.get(position).getId();
                }else{
                    holder.title.setChecked(false);
                    str_pref_amenities_id=str_pref_amenities_id.replace(vault_items.get(position).getId().toString(),"");

                    if(str_pref_amenities_id.contains(vault_items.get(position).getId())){

                    }
                }
                Log.e("Amenitites==",str_pref_amenities_id);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        LinearLayout linear;
        ImageView  icon;
        CheckBox  title;
    }

       private  int getScreenWidth()
       {
           Display display = context.getWindowManager().getDefaultDisplay();
           int width = display.getWidth();
           return  width;
       }
*/
}
