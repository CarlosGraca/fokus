package com.example.carlos.fokus.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


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

    public static String getAddress(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(lat, lng, 2);

                android.location.Address obj = addresses.get(1);
                Log.d("MAD "+1, String.valueOf(obj));
                String  add = obj.getAddressLine(0);
                Log.d("MAD ", String.valueOf(add));

            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    public static boolean getLocationPermission(Context context) {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
           return true;
        } else {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        return false;
    }

}
