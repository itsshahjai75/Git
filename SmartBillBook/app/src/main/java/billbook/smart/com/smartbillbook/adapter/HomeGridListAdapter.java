package billbook.smart.com.smartbillbook.adapter;


import android.app.Activity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import billbook.smart.com.smartbillbook.R;
import billbook.smart.com.smartbillbook.modelpojo.IdNameUrlGrideRaw;

public class HomeGridListAdapter /*extends BaseAdapter*/ {
/*

    private ArrayList<IdNameUrlGrideRaw> vault_items;
    private Activity context;
    private LayoutInflater inflater;

    public HomeGridListAdapter(Activity context, ArrayList<IdNameUrlGrideRaw> vault_items) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.vendor_grid_raw, null, false);
            holder = new ViewHolder();

            holder.linear = (LinearLayout) convertView.findViewById(R.id.layout);
            holder.icon = (ImageView) convertView.findViewById(R.id.mainicon);
            holder.title = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int width =  (getScreenWidth() - 21)/3;

       // holder.linear.setLayoutParams(new LinearLayout.LayoutParams(width , width-15));
       */
/* holder.linear.setBackgroundColor(Color.parseColor(vault_items.get(position).getColors()));*//*

        Glide.with(context).load(vault_items.get(position).getUrl())
                .placeholder(R.drawable.loading_image_pic)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( holder.icon);
        holder.title.setText(vault_items.get(position).getTitle());

        return convertView;
    }

    static class ViewHolder {
        LinearLayout linear;
        ImageView  icon;
        TextView  title;
    }

       private  int getScreenWidth()
       {
           Display display = context.getWindowManager().getDefaultDisplay();
           int width = display.getWidth();
           return  width;
       }
*/

}
