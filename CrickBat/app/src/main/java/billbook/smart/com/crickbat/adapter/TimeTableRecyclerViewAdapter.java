package billbook.smart.com.crickbat.adapter;

/**
 * Created by Jay on 27-Aug-16.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import billbook.smart.com.crickbat.R;
import billbook.smart.com.crickbat.ScoreTable;
import billbook.smart.com.crickbat.UserScoreTable;
import billbook.smart.com.crickbat.model.TimeTableModel;

public class TimeTableRecyclerViewAdapter extends RecyclerView.Adapter<TimeTableRecyclerViewAdapter.Tips_RecyclerViewHolders> {

    private List<TimeTableModel> timeTableModel;
    protected Context context;
    public TimeTableRecyclerViewAdapter(Context context, List<TimeTableModel> timeTableModel) {
        this.timeTableModel = timeTableModel;
        this.context = context;
    }
    @Override
    public Tips_RecyclerViewHolders onCreateViewHolder(ViewGroup parent,final int viewType) {
        Tips_RecyclerViewHolders viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tips_raw_layout, parent, false);
        viewHolder = new Tips_RecyclerViewHolders(layoutView, timeTableModel);



        return viewHolder;
    }
    @Override
    public void onBindViewHolder(Tips_RecyclerViewHolders holder, final int position) {
        holder.title.setText(timeTableModel.get(position).getDate());
        holder.details.setText(timeTableModel.get(position).getDetails()+"\n"+ timeTableModel.get(position).getVenue());
        holder.time.setText(timeTableModel.get(position).getTime());
        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ScoreTable.class)
                .putExtra("match_name", timeTableModel.get(position).getDetails())
                        .putExtra("venue", timeTableModel.get(position).getVenue())
                .putExtra("date_time", timeTableModel.get(position).getDate()+","+ timeTableModel.get(position).getTime()));
            }
        });


        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserScoreTable.class)
                        .putExtra("match_name", timeTableModel.get(position).getDetails())
                        .putExtra("venue", timeTableModel.get(position).getVenue())
                        .putExtra("date_time", timeTableModel.get(position).getDate()+","+ timeTableModel.get(position).getTime()));
            }
        });


    }
    @Override
    public int getItemCount() {
        return this.timeTableModel.size();
    }



    public class Tips_RecyclerViewHolders extends RecyclerView.ViewHolder{
        private final String TAG = Tips_RecyclerViewHolders.class.getSimpleName();
        public TextView title;
        public TextView details;
        public TextView time;
        public ImageView img_edit;
        public List<TimeTableModel> timeTableModel;
        public CardView card_view;

        public Tips_RecyclerViewHolders(final View itemView, final List<TimeTableModel> timeTableModel) {
            super(itemView);
            this.timeTableModel = timeTableModel;
            title = (TextView)itemView.findViewById(R.id.tv_title_tips);
            details = (TextView)itemView.findViewById(R.id.tv_details_tips);
            time = (TextView)itemView.findViewById(R.id.tv_time_tips);
            img_edit=(ImageView)itemView.findViewById(R.id.img_edit);
            card_view=(CardView)itemView.findViewById(R.id.card_view);

        }
    }
}