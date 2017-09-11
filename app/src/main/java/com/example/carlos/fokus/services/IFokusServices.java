package com.example.carlos.fokus.services;

import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import java.io.File;

/**
 * Created by Carlos on 10/09/2017.
 */

public interface IFokusServices {
    void getFoku (String url, JSONObjectRequestListener listener);
    void saveFoku(String url, String nName,String mLong, String mLat, String  deviceID, String mDescription, File image_foku,String rating, JSONObjectRequestListener listener);
    void getArrayFokus(String url, JSONArrayRequestListener listener);
}
