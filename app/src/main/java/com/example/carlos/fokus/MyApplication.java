package com.example.carlos.fokus;

import android.app.Application;

/**
 * Created by Carlos on 08/09/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Init Networking
        /*OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        AndroidNetworking.initialize(this,okHttpClient);*/
    }

}
