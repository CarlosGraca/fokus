package com.example.carlos.fokus.services;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import java.io.File;

/**
 * Created by Carlos on 10/09/2017.
 */

public class FokusServices implements IFokusServices {
    @Override
    public void getFoku(String url, JSONObjectRequestListener listener) {
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(listener);
    }

    @Override
    public void saveFoku(String url,String nName, String mLong, String mLat, String  deviceID, String mDescription , File image_foku, String rating, JSONObjectRequestListener listener) {
        AndroidNetworking.upload(url)
                .addMultipartParameter("name", nName)
                .addMultipartParameter("long", mLong)
                .addMultipartParameter("lat", mLat)
                .addMultipartParameter("description", mDescription)
                .addMultipartParameter("rating", rating)
                .addMultipartParameter("device_id",deviceID )
                .addMultipartFile("image",image_foku)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsJSONObject(listener);
        //.getAsString(listener);
    }

    @Override
    public void getArrayFokus(String url, JSONArrayRequestListener listener) {
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(listener);
    }

    @Override
    public ANRequest getArrayFokus2(String url) {
        ANRequest request = AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build();
        return request;

    }
}
