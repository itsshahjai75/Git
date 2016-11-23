package com.example.admin.eventsapp;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.eventsapp.models.Object;

import java.util.List;

/**
 * Created by Admin on 10/15/2016.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ItemHolder> {
    private Activity activity;
    private List<Object> eventsList;

    public EventAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_event_layout, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Object mEventlist = eventsList.get(position);
        holder.txtEventletter.setText(mEventlist.getEvent().substring(0, 1).toUpperCase());
        holder.txtEventresources.setText(mEventlist.getEvent());
        holder.txtEventName.setText(mEventlist.getResource().getName());
        holder.txtEventLink.setText(Html.fromHtml("<a href=" + mEventlist.getHref() + ">" + mEventlist.getHref() + "</a> "));


        long millisecondsStart = TimeUtils.getMilliseconds(mEventlist.getStart());
        String dateStart = TimeUtils.getDate(millisecondsStart);
        String[] splitStart = dateStart.split("/");
        holder.txtStartDate.setText(splitStart[0]);
        holder.txtStartRemainig.setText(splitStart[1] + "/" + splitStart[2] + "\n" + TimeUtils.getTime(millisecondsStart));
        holder.txtStartDay.setText(TimeUtils.getDayOftheWeek(millisecondsStart));

        long millisecondsEnd = TimeUtils.getMilliseconds(mEventlist.getEnd());
        String dateEnd = TimeUtils.getDate(millisecondsEnd);
        String[] splitEnd = dateEnd.split("/");
        holder.txtEndDate.setText(splitEnd[0]);
        holder.txtEndRemaining.setText(splitEnd[1] + "/" + splitEnd[2] + "\n" + TimeUtils.getTime(millisecondsEnd));


        holder.txtStatus.setText("Loading.....");
        holder.txtDuration.setText("Duration :    " + TimeUtils.getTime(mEventlist.getDuration()*60));
    }

    @Override
    public int getItemCount() {
        if (eventsList != null) {
            return eventsList.size();
        }
        return 0;
    }

    public void setEventsList(List<Object> eventsList) {
        this.eventsList = eventsList;
        notifyDataSetChanged();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView txtEventletter, txtEventName, txtEventresources, txtEventLink;
        TextView txtStartDay, txtStartDate, txtEndDate;
        TextView txtStatus, txtDuration;
        TextView txtStartRemainig, txtEndRemaining;

        public ItemHolder(View itemView) {
            super(itemView);
            txtEventletter = (TextView) itemView.findViewById(R.id.txtEventletter);
            txtEventName = (TextView) itemView.findViewById(R.id.txtEventName);
            txtEventresources = (TextView) itemView.findViewById(R.id.txtEventResources);
            txtEventLink = (TextView) itemView.findViewById(R.id.txtEventLink);
            txtEventLink.setMovementMethod(LinkMovementMethod.getInstance());
            txtStartDay = (TextView) itemView.findViewById(R.id.txtStartDay);
            txtStartDate = (TextView) itemView.findViewById(R.id.txtStartDate);
            txtEndDate = (TextView) itemView.findViewById(R.id.txtEndDate);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            txtDuration = (TextView) itemView.findViewById(R.id.txtDuration);
            txtStartRemainig = (TextView) itemView.findViewById(R.id.txtStartDateRemainng);
            txtEndRemaining = (TextView) itemView.findViewById(R.id.txtEndDateRemaing);

        }
    }
}
