package com.example.carlos.fokus.services;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.carlos.fokus.constants.Constants;

/**
 * Created by eceybrito on 09/09/2017.
 */

public class GetListFokusService implements IGetFokusService {
    @Override
    public void call(String url, JSONArrayRequestListener listener) {
        AndroidNetworking.get(url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(listener);
    }
}
