package com.FoF.foodordering.Retrofit;

import retrofit.RestAdapter;

/**
 * Created by AbhiAndroid
 */
public class Api {

    public static ApiInterface getClient() {

        // change your base URL
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("https://spanprogram.com") //Set the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        ApiInterface api = adapter.create(ApiInterface.class);
        return api;
    }
}
