package com.example.admin.eventsapp.networks;

import com.example.admin.eventsapp.AppConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Admin on 10/15/2016.
 */

public class ApiServiceClient {

    public static ApiServices getApiServices() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices service = retrofit.create(ApiServices.class);

        return service;
    }
}
