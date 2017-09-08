package com.example.carlos.fokus.helpers;

/**
 * Created by Carlos on 08/09/2017.
 */
import android.annotation.TargetApi;
import android.os.Build;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Okhttp3 {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String bowlingJson(String name, String player2) {
        return "{'name':'HIGH_SCORE',"
                + "'lat':'Bowling',"
                + "'long':4,"
                + "'description':1367702411696,"
                + "'image':1367702378785,"
                + "'device_id':device_id"
                + "}";
    }
}
