package com.example.carlos.fokus.services;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import java.io.File;

/**
 * Created by Carlos on 08/09/2017.
 */

public class SaveFokusDetailsService implements ISaveFokusDetails {
    @Override
    public void call(String url, String mLong,String mLat, String  deviceID, String mDescription , File image,  JSONObjectRequestListener listener) {
        AndroidNetworking.post(url)
                .addBodyParameter("name", "Amit")
                .addBodyParameter("long", mLong)
                .addBodyParameter("lat", mLat)
                .addBodyParameter("description", mDescription)
                .addFileBody(image)
                .addBodyParameter("device_id",deviceID )
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(listener);
                //.getAsString(listener);
    }
}
