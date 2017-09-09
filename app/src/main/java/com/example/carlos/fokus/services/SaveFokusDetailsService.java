package com.example.carlos.fokus.services;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.StringRequestListener;

/**
 * Created by Carlos on 08/09/2017.
 */

public class SaveFokusDetailsService implements ISaveFokusDetails {
    @Override
    public void call(String url, String mLong, String mLat, String  deviceID, String mDescription, StringRequestListener listener) {
        AndroidNetworking.post(url)
                .addBodyParameter("name", "Amit")
                .addBodyParameter("long", mLong)
                .addBodyParameter("lat", mLat)
                .addBodyParameter("description", mDescription)
                .addBodyParameter("image", "Shekhar")
                .addBodyParameter("device_id",deviceID )
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(listener);
    }
}
