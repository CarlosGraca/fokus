package com.example.carlos.fokus.services;

import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import java.io.File;

/**
 * Created by Carlos on 08/09/2017.
 */

public interface ISaveFokusDetails {
    void call(String url, String mLong, String mLat, String  deviceID, String mDescription, File image, JSONObjectRequestListener listener);
}
