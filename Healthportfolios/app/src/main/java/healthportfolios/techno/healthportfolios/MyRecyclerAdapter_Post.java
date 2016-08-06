package healthportfolios.techno.healthportfolios;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRecyclerAdapter_Post  extends RecyclerView
        .Adapter<MyRecyclerAdapter_Post
        .DataObject_postHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter1";
    private ArrayList<ReportsListviewObject> mDataset;
    private Context mContext;
    NotificationManager mNotifyManager;

    public static class DataObject_postHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView tv_eventdate,tv_eventdatemonth,tv_title,tv_team,tv_fees,tv_contact;
        Button btn_view,btn_view_rules;


        public DataObject_postHolder(final View itemView) {
            super(itemView);



            tv_eventdate = (TextView) itemView.findViewById(R.id.tv_eventetdate);
            tv_eventdatemonth = (TextView) itemView.findViewById(R.id.tv_eventetdate_month);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_fees= (TextView) itemView.findViewById(R.id.tv_fess);
            tv_team= (TextView) itemView.findViewById(R.id.tv_teamsize);
            tv_contact= (TextView) itemView.findViewById(R.id.tv_contact);

            btn_view=(Button) itemView.findViewById(R.id.btn_view);
            btn_view_rules=(Button) itemView.findViewById(R.id.btn_view_rules);





            // Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);





        }

        @Override
        public void onClick(View v) {
       //     myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public MyRecyclerAdapter_Post(ArrayList<ReportsListviewObject> myDataset,Context context) {
        mDataset = myDataset;
        mContext=context;


    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listview_reports, parent, false);
            DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);

             mNotifyManager =
                (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);


        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder,final int position) {

        String date= mDataset.get(position).getmDate();
        String sub_day=date.substring(0,2);
        String sub_month=date.substring(3,5);
        String sub_year=date.substring(6);

        if(sub_month.equalsIgnoreCase("01")){

            sub_month="Jan";
        }else if(sub_month.equalsIgnoreCase("02")){
            sub_month="Fab";
        }else if(sub_month.equalsIgnoreCase("03")){
            sub_month="Mar";
        }else if(sub_month.equalsIgnoreCase("04")){
            sub_month="Apr";
        }else if(sub_month.equalsIgnoreCase("05")){
            sub_month="May";
        }else if(sub_month.equalsIgnoreCase("06")){
            sub_month="June";
        }else if(sub_month.equalsIgnoreCase("07")){
            sub_month="July";
        }else if(sub_month.equalsIgnoreCase("08")){
            sub_month="Aug";
        }else if(sub_month.equalsIgnoreCase("09")){
            sub_month="Sept";
        }else if(sub_month.equalsIgnoreCase("10")){
            sub_month="Oct";
        }else if(sub_month.equalsIgnoreCase("11")){
            sub_month="Nov";
        }else if(sub_month.equalsIgnoreCase("12")){
            sub_month="Dec";
        }

        holder.tv_eventdate.setText(sub_day);
        holder.tv_eventdatemonth.setText(sub_month+"-"+sub_year);

        holder.tv_title.setText(mDataset.get(position).getmTitle());
        holder.tv_team.setText(mDataset.get(position).getmteam());

        holder.tv_fees.setText(mDataset.get(position).getmFees());
        holder.tv_contact.setText(mDataset.get(position).getmContact());
       // holder.tv_team.setText(mDataset.get(position).getm);







            holder.btn_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url = "http://www.myhealthportfolios.com/wp-content/uploads/hospital_assets/"+mDataset.get(position).getmContact();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setData(Uri.parse(url));
                    mContext.startActivity(i);

                }
            });




        holder.btn_view_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });





    }

    public void addItem(ReportsListviewObject dataObj, int index) {
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