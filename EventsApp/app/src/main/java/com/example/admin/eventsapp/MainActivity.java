package com.example.admin.eventsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.admin.eventsapp.models.EventModel;
import com.example.admin.eventsapp.networks.ApiServiceClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRvEvents;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRvEvents= (RecyclerView) findViewById(R.id.rvEvents);
        mRvEvents.setHasFixedSize(true);
        mRvEvents.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter=new EventAdapter(this);
        mRvEvents.setAdapter(eventAdapter);

        ApiServiceClient.getApiServices().getEvents().enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                Log.e("onResponse: ","");
                eventAdapter.setEventsList(response.body().getObjects());
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {

            }
        });
    }
}
