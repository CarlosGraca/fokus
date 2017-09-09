package com.example.carlos.fokus.helpers;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by eceybrito on 07/09/2017.
 */

public class MapFunctions {

    private static final String TAG = "MapFunctions";

    public MapFunctions() {
    }

    public static void updateMarkers(GoogleMap map,
                                     String title,
                                     int id,
                                     LatLng latLng,
                                     BitmapDescriptor bitmapDescriptor) {

        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .zIndex(id)
                .icon(bitmapDescriptor));
    }



}
