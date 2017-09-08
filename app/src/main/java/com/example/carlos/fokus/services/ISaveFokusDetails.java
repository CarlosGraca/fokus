package com.example.carlos.fokus.services;

import com.androidnetworking.interfaces.StringRequestListener;

/**
 * Created by Carlos on 08/09/2017.
 */

public interface ISaveFokusDetails {
    void call(String url, String mLong, String mLat, String  deviceID, String mDescription, StringRequestListener listener);
}
