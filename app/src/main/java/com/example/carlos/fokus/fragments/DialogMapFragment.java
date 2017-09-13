package com.example.carlos.fokus.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.carlos.fokus.R;
import com.example.carlos.fokus.constants.Constants;
import com.example.carlos.fokus.services.FokusServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by eceybrito on 12/09/2017.
 */

public class DialogMapFragment extends DialogFragment {

    GoogleMap mMap;

    public DialogMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.map_dialog, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map_dialog_fragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                LatLng latLng = new LatLng(37.7688472,-122.4130859);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mMap.addMarker(markerOptions);

                new FokusServices().getFoku(Constants.serverUrl + "/spots/43", new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            LatLng currentLocation = new LatLng(response.getDouble("lat"), response.getDouble("long"));
                            float bitDesc = 0;
                            Log.d("bitDesc 11", response.getString("status"));
                            if (response.getString("status").equals(Constants.marked)) {
                                bitDesc = BitmapDescriptorFactory.HUE_ORANGE;
                                Log.d("bitDesc 1", String.valueOf(bitDesc));
                            } else if (response.getString("status").equals(Constants.confirmed)) {
                                bitDesc = BitmapDescriptorFactory.HUE_RED;
                                Log.d("bitDesc 2", String.valueOf(bitDesc));
                            } else if (response.getString("status").equals(Constants.sorted_out)) {
                                bitDesc = BitmapDescriptorFactory.HUE_GREEN;
                                Log.d("bitDesc 3", String.valueOf(bitDesc));
                            }
                            Log.d("bitDesc ", String.valueOf(bitDesc));

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,11));

                            Marker mMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.defaultMarker(bitDesc)));
                            // set object as tag
                            mMarker.setTag(response);

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                    }
                });
            }
        });

        return rootView;
    }
}