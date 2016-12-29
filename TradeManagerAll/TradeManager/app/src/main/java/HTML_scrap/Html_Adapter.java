package HTML_scrap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import manager.trade.techno.trademanager.FontChangeCrawler;
import manager.trade.techno.trademanager.R;

public class Html_Adapter extends RecyclerView.Adapter<Html_Adapter.MyViewHolder> {
    ArrayList<Html_FeedItem> htmlFeedItems;
    Context context;
    public Html_Adapter(Context context, ArrayList<Html_FeedItem> htmlFeedItems){
        this.htmlFeedItems = htmlFeedItems;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rss_custom_row_view,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        FontChangeCrawler fontChanger = new FontChangeCrawler(context.getAssets(), "fonts/ProductSans-Regular.ttf");
        //fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        fontChanger.replaceFonts((ViewGroup)view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //YoYo.with(Techniques.FadeIn).playOn(holder.cardView);
        YoYo.with(Techniques.ZoomIn)
                .duration(700)
                .playOn(holder.cardView);
        final Html_FeedItem current= htmlFeedItems.get(position);
        holder.Title.setText(current.getTitle());
        holder.Description.setText(current.getDescription());
        holder.Date.setText(current.getPubDate());
        Picasso.with(context).load(current.getThumbnailUrl()).into(holder.Thumbnail);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(current.getLink()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return htmlFeedItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Title,Description,Date;
        ImageView Thumbnail;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            Title= (TextView) itemView.findViewById(R.id.title_text);
            Description= (TextView) itemView.findViewById(R.id.description_text);
            Date= (TextView) itemView.findViewById(R.id.date_text);
            Thumbnail= (ImageView) itemView.findViewById(R.id.thumb_img);
            cardView= (CardView) itemView.findViewById(R.id.cardview);

        }
    }
}
