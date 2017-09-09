package com.example.carlos.fokus.services;

/**
 * Created by eceybrito on 08/09/2017.
 */

public class GetFokuDetail implements IGetFokusDetailService{
    @Override
    public void call(int id, String url) {

        /*AndroidNetworking.get(url)
                .addPathParameter("id", String.valueOf(id))
                .setTag(this)
                .setPriority(Priority.LOW)
                .build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

                // update dialog, but first serialize thi JsonObject

            }

            @Override
            public void onError(ANError anError) {

                // log errors
            }
        });*/
    }
}
