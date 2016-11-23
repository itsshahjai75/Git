package com.example.admin.eventsapp.networks;

import com.example.admin.eventsapp.AppConstants;
import com.example.admin.eventsapp.models.EventModel;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Admin on 10/15/2016.
 */

public interface ApiServices {

    @GET(AppConstants.EVENTS_URL)
    Call<EventModel> getEvents();
}
