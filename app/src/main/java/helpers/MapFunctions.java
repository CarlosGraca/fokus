package helpers;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static java.security.AccessController.getContext;


/**
 * Created by eceybrito on 07/09/2017.
 */

public class MapFunctions {

    private static final String TAG = "MapFunctions";

    public static String apiUrl = "http://pocteam2.gov.cv/public";


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
