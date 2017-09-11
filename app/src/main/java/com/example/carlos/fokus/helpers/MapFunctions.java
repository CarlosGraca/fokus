package com.example.carlos.fokus.helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by eceybrito on 07/09/2017.
 */

public class MapFunctions {

    private static final String TAG = "MapFunctions";
    public static final int DEFAULT_ZOOM = 15;
    //Praia CV
    public static final LatLng mDefaultLocation = new LatLng(14.9364475, -23.5067295);

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
