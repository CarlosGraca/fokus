package com.example.carlos.fokus.services;

import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

/**
 * Created by eceybrito on 09/09/2017.
 */

public interface IGetFokusService {
    void call(String url, JSONArrayRequestListener listener);
}
