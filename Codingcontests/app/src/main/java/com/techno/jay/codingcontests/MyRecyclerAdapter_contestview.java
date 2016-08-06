package com.techno.jay.codingcontests;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.techno.jay.codingcontests.DataObject_post;
import com.techno.jay.codingcontests.FontChangeCrawler;
import com.techno.jay.codingcontests.R;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import me.himanshusoni.quantityview.QuantityView;

public class MyRecyclerAdapter_contestview extends RecyclerView
        .Adapter<MyRecyclerAdapter_contestview
        .DataObject_postHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private static ArrayList<DataObject_post> mDataset;
    private static Context mContext;
    Date date_Start,date_End,date_Now;


    SharedPreferences sharepref;




    public static class DataObject_postHolder extends RecyclerView.ViewHolder
           {
        TextView tv_title_companyname, tv_contest_titlte, tv_end_onlytime, tv_status, tv_day,tv_title_companyname_logo;
        TextView tv_link_url, tv_onlydate, tv_onlymonth, tv_onlytime, tv_end_onlydate, tv_end_onlymonth,tv_duration;
        CardView cardview;
        ImageView img_share,img_reminderest;

        public DataObject_postHolder(final View itemView) {
            super(itemView);


            tv_title_companyname = (TextView) itemView.findViewById(R.id.tv_title_companyname);
            tv_contest_titlte = (TextView) itemView.findViewById(R.id.tv_contest_titlte);
            tv_end_onlytime = (TextView) itemView.findViewById(R.id.tv_end_onlytime);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
            tv_link_url = (TextView) itemView.findViewById(R.id.tv_link_url);
            tv_onlydate = (TextView) itemView.findViewById(R.id.tv_onlydate);
            tv_onlymonth = (TextView) itemView.findViewById(R.id.tv_onlymonth);
            tv_onlytime = (TextView) itemView.findViewById(R.id.tv_onlytime);
            tv_end_onlydate = (TextView) itemView.findViewById(R.id.tv_end_onlydate);
            tv_end_onlymonth = (TextView) itemView.findViewById(R.id.tv_end_onlymonth);

            tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
            tv_title_companyname_logo = (TextView) itemView.findViewById(R.id.tv_title_companyname_logo);
            img_reminderest=(ImageView) itemView.findViewById(R.id.img_reminder_set);
            img_share=(ImageView) itemView.findViewById(R.id.img_share);
            cardview=(CardView) itemView.findViewById(R.id.card_view);










        }


    }

   /* public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }*/

    public MyRecyclerAdapter_contestview(ArrayList<DataObject_post> myDataset) {
        mDataset = myDataset;



    }

    @Override
    public DataObject_postHolder onCreateViewHolder(ViewGroup parent,
                                                    int viewType) {



        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_cardview_home_withoutimage, parent, false);

         mContext = parent.getContext();

        FontChangeCrawler fontChanger = new FontChangeCrawler(mContext.getAssets(), "fonts/ProductSans-Regular.ttf");
        //fontChanger.replaceFonts((ViewGroup) this.findViewById(android.R.id.content));
        //==2) for fragment hoy to====
        //== fontChanger.replaceFonts((ViewGroup) this.getView());
        //===3) for adepterview and handlerview na use mate====
        //==convertView = inflater.inflate(R.layout.listitem, null);
        fontChanger.replaceFonts((ViewGroup)view);

        DataObject_postHolder dataObjectHolder = new DataObject_postHolder(view);



        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(final DataObject_postHolder holder,final int position) {

        sharepref = mContext.getApplicationContext().getSharedPreferences("MyPref",mContext.MODE_PRIVATE);

        if(mDataset.get(position).getReminderstatus().equalsIgnoreCase("0")){
            holder.img_reminderest.setVisibility(View.GONE);
        }else if(mDataset.get(position).getReminderstatus().equalsIgnoreCase("1")){
            holder.img_reminderest.setVisibility(View.VISIBLE);
        }

        // Log.i(LOG_TAG, "Adding Listener");
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Toast.makeText(v.getContext(),"itemcode="+mDataset.get(position).getContestid(),Toast.LENGTH_LONG).show();

                Intent edit = new Intent(v.getContext(),Details_event.class);
                edit.putExtra("eventid",mDataset.get(position).getContestid());
                mContext.startActivity(edit);
            }
        });



        holder.img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentshare = new Intent(Intent.ACTION_SEND);
                intentshare.setType("text/plain");
                intentshare.putExtra(Intent.EXTRA_TEXT, "Event Details "
                        +"\nTitle : "+holder.tv_contest_titlte.getText().toString()
                        +"\nWebHost : "+holder.tv_title_companyname.getText().toString()
                        +"\nURL : "+holder.tv_link_url.getText().toString()
                        +"\nStarts : "+mDataset.get(position).getStart()
                        +"\nEnd : "+mDataset.get(position).getEnd()
                        +"\nDuration : "+holder.tv_duration.getText().toString()
                        +"\nfrom Coding Contests.\n\n"
                        + "https://play.google.com/store/apps/details?id=com.techno.jay.codingcontests&hl=en");
                mContext.startActivity(Intent.createChooser(intentshare, "Share"));

            }
        });



        try {


             holder.tv_contest_titlte.setText(mDataset.get(position).getEvent());
            String companyname=mDataset.get(position).getResource_name();

            companyname=companyname.substring(0,companyname.indexOf("."));

            String uper_companyname=companyname.substring(0,1).toUpperCase();
             holder.tv_title_companyname_logo.setText(uper_companyname);

            companyname=uper_companyname+companyname.substring(1);
             holder.tv_title_companyname.setText(companyname);


            //==duration
            String duration_str = mDataset.get(position).getDuration();
            long seconds = Long.parseLong(duration_str);
            int day_duration = (int) TimeUnit.SECONDS.toDays(seconds);
            long hours = TimeUnit.SECONDS.toHours(seconds) - (day_duration *24);
            long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
            long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);




             holder.tv_duration.setText(day_duration+" day "+hours+" Hr : "+minute+" min : "+second+" sec");

            //===== status of compition

            String dtStart = mDataset.get(position).getStart();
            String dtEnd = mDataset.get(position).getEnd();
            SimpleDateFormat format_diffrent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {

                Date now = new Date();
                String strDate = format_diffrent.format(now);

                date_Start = format_diffrent.parse(dtStart);
                date_End = format_diffrent.parse(dtEnd);
                date_Now= format_diffrent.parse(strDate);

                // System.out.println(date_Start);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            if(between(date_Now,date_Start,date_End)==true){
                 holder.tv_status.setText(" Running ");
                 holder.tv_status.setBackgroundColor(mContext.getResources().getColor(R.color.md_green_A700));

            }else if(date_End.before(date_Now)){
                 holder.tv_status.setText(" Completed ");

                 holder.tv_status.setBackgroundColor(mContext.getResources().getColor(R.color.md_red_A700));

            }else{
                 holder.tv_status.setText(" Yet not started ");

                 holder.tv_status.setBackgroundColor(mContext.getResources().getColor(R.color.md_yellow_A700));

            }


             holder.tv_link_url.setText( mDataset.get(position).getHref());

            //start date and end date details set text code====
            String full_start =  mDataset.get(position).getStart();
            DateFormat df_post = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            //df_post.setTimeZone(TimeZone.getTimeZone("MST"));

            Date date_Start = df_post.parse(full_start);
            full_start = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.US).format(date_Start.getTime());

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.US);
            Calendar end_date, cal = null;

            cal = Calendar.getInstance();
            cal.setTime(format.parse(full_start));
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int date = cal.get(Calendar.DATE);
            int day = cal.get(Calendar.DAY_OF_WEEK);
            String dayOfWeek = getDayOfWeek(day);
             holder.tv_day.setText(dayOfWeek);

            if(date<=9){
                 holder.tv_onlydate.setText("0"+Integer.toString(date));
            }else{

                 holder.tv_onlydate.setText(Integer.toString(date));
            }

             holder.tv_onlymonth.setText(Integer.toString(month) +" / "+ Integer.toString(year));

            String onlytime = new SimpleDateFormat("hh:mm aa", Locale.US).format(date_Start.getTime());
             holder.tv_onlytime.setText(onlytime);


            String full_end =  mDataset.get(position).getEnd();


            Date date_end = df_post.parse(full_end);
            full_end = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.US).format(date_end.getTime());


            end_date = Calendar.getInstance();
            end_date.setTime(format.parse(full_end));
            int enddate_year = end_date.get(Calendar.YEAR);
            int enddate_month = end_date.get(Calendar.MONTH) + 1;
            int enddate_date = end_date.get(Calendar.DATE);
            int enddate_day = end_date.get(Calendar.DAY_OF_MONTH);
            String enddate_dayOfWeek = getDayOfWeek(Calendar.DAY_OF_WEEK);




            if(enddate_date<=9){
                 holder.tv_end_onlydate.setText("0"+Integer.toString(enddate_date));
            }else{

                 holder.tv_end_onlydate.setText(Integer.toString(enddate_date));
            }

             holder.tv_end_onlymonth.setText(Integer.toString(enddate_month) +" / "+ Integer.toString(enddate_year));

            String onlyendtime = new SimpleDateFormat("hh:mm aa", Locale.US).format(date_end.getTime());
             holder.tv_end_onlytime.setText(onlyendtime);


        } catch (Exception dateexceptio) {
            dateexceptio.printStackTrace();
        }



    }

    public void addItem(DataObject_post dataObj, int index) {
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






    private String getDayOfWeek(int value) {
        String day = "";
        switch (value) {
            case 1:
                day = "SUN";
                break;
            case 2:
                day = "MON";
                break;
            case 3:
                day = "TUE";
                break;
            case 4:
                day = "WED";
                break;
            case 5:
                day = "THU";
                break;
            case 6:
                day = "FRI";
                break;
            case 7:
                day = "SAT";
                break;
        }
        return day;
    }


    public static boolean between(Date date, Date dateStart, Date dateEnd) {
        if (date != null && dateStart != null && dateEnd != null) {
            if (date.after(dateStart) && date.before(dateEnd)) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }



}