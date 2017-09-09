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

    public static String apiUrl = "http://app.yournit.com";


    public MapFunctions() {
    }

    public static void updateMarkers(GoogleMap map,
                                     String title,
                                     LatLng latLng,
                                     BitmapDescriptor bitmapDescriptor) {

        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(bitmapDescriptor));
    }

    public void changeMarkerColor(GoogleMap map,
                                  String type) {


    }

}
