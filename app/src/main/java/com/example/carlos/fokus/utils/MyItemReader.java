package com.example.carlos.fokus.utils;

import android.util.Log;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;
import com.example.carlos.fokus.services.FokusServices;
import com.example.carlos.fokus.utils.model.MyItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 12/09/2017.
 */

public class MyItemReader {
    private static final String REGEX_INPUT_BOUNDARY_BEGINNING = "\\A";

    public List<MyItem> read(String url) throws JSONException {
        final List<MyItem> items = new ArrayList<MyItem>();



        Log.d("items 1","Start");
        ANRequest request = new FokusServices().getArrayFokus2(url);
        ANResponse<JSONArray> response = request.executeForJSONArray();

        if (response.isSuccess()) {
            JSONArray jsonArray = response.getResult();

            Log.d("Response", "response : " + jsonArray.toString());

        } else {
            ANError error = response.getError();
            // Handle Error
        }


        /*String json = new Scanner(inputStream).useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            String title = null;
            String snippet = null;
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            if (!object.isNull("title")) {
                title = object.getString("title");
            }
            if (!object.isNull("snippet")) {
                snippet = object.getString("snippet");
            }
            items.add(new MyItem(lat, lng, title, snippet));
        }*/
        return items;
    }
}
