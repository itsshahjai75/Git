package arihantmart.techno.arihantmart;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;



public class CardAdapter_subcatagory extends CustomRecyclerView.Adapter<CardAdapter_subcatagory.ViewHolder> {
    List<Subcategory_objectclass> list = new ArrayList<>();
    public CardAdapter_subcatagory(List<Subcategory_objectclass> list) {
        this.list = list;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public Subcategory_objectclass getItem(int i) {
        return list.get(i);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_subcatagory, parent, false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.subcategory_objectclass=getItem(position);
        holder.cardtitle.setText(list.get(position).name);
        holder.cardimage.setImageResource(list.get(position).id);
    }
   /* @Override
    public void onAttachedToRecyclerView(CustomRecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }*/

    public class ViewHolder extends CustomRecyclerView.ViewHolder {
        ImageView cardimage;
        TextView cardtitle;
        Subcategory_objectclass subcategory_objectclass;
        public ViewHolder(View itemView) {
            super(itemView);
            cardimage = (ImageView) itemView.findViewById(R.id.cardimage);
            cardtitle = (TextView) itemView.findViewById(R.id.cardtitle);
        }
    }
}