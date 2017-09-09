package com.example.carlos.fokus.services;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

/**
 * Created by eceybrito on 08/09/2017.
 */

public class GetFokuDetail implements IGetFokusDetailService{
    @Override
    public void call(int id, String url) {

        AndroidNetworking.get(url)
                .addPathParameter("id", String.valueOf(id))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

            }

            @Override
            public void onError(ANError anError) {

            }
        });
    }
}
