package com.example.carlos.fokus;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

/**
 * Created by Carlos on 08/09/2017.
 */

public class MyApplication extends Application {

    private static MyApplication appInstance = null;

    public static MyApplication getInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        //Init Networking
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        AndroidNetworking.initialize(this,okHttpClient);

    }

}
