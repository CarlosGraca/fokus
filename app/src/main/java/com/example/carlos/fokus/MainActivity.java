package com.example.carlos.fokus;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.example.carlos.fokus.helpers.ApiImage;
import com.example.carlos.fokus.services.FokusServices;

import com.example.carlos.fokus.ui.DisplayUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.example.carlos.fokus.constants.Constants;
import com.example.carlos.fokus.helpers.MapFunctions;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.carlos.fokus.helpers.MapFunctions.DEFAULT_ZOOM;
import static com.example.carlos.fokus.helpers.MapFunctions.mDefaultLocation;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String apiUrl = Constants.serverUrl+"/posts";

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final CharSequence[] MAP_TYPE_ITEMS =
            {"Mapa de rua", "Hibrido", "Satellite", "Terreno"};

    private DisplayUI ui ;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private CameraPosition mCameraPosition;

    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    public void initializeComponents () {

        ui = new DisplayUI(this.getApplicationContext());

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NewFoku.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_my_points) {
            Intent intent = new Intent(MainActivity.this, ListFokusActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_about) {
            return  true;
        }
        if (id == R.id.action_my_map_type) {
            showMapTypeSelectorDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.getResult();

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(),
                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }

        setFokusMap();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showDialogFokus(marker);
                return true;
            }
        });
    }


    public void showDialogFokus(Marker marker){
        final Dialog dialog = new Dialog(this);
        final String serverUrl = Constants.serverUrl;

        dialog.setContentView(R.layout.custom_info_contents);

        final ImageView imageView = (ImageView) dialog.findViewById(R.id.imgFoku);

        // set the custom dialog components - text, image and button
        final TextView description = (TextView) dialog.findViewById(R.id.description);

        Button dialogButton = (Button) dialog.findViewById(R.id.btSend);
        RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);

        dialogButton.setVisibility(View.GONE);
        ratingBar.setEnabled(false);
        description.setEnabled(false);

        // to retrieve the marker
        JSONObject obj = (JSONObject) marker.getTag();// Type cast to your object type;
        try {
            description.setText(obj.getString("description"));
            Glide.with(this).load(serverUrl+"/"+obj.getString("image")).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG,e.getMessage());
        }

        dialog.show();
    }

    private void showMapTypeSelectorDialog() {
        // Prepare the dialog by setting up a Builder.
        final String fDialogTitle = "Selecione o tipo de mapa";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(fDialogTitle);

        // Find the current map type to pre-check the item representing the current state.
        int checkItem = mMap.getMapType() - 1;

        // Add an OnClickListener to the dialog, so that the selection will be handled.
        builder.setSingleChoiceItems(
                MAP_TYPE_ITEMS,
                checkItem,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int item) {
                        // Locally create a finalised object.

                        // Perform an action depending on which item was selected.
                        switch (item) {
                            case 1:
                                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;
                            case 2:
                                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;
                            case 3:
                                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                break;
                            default:
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        }
                        dialog.dismiss();
                    }
                }
        );

        // Build the dialog and show it.
        AlertDialog fMapTypeDialog = builder.create();
        fMapTypeDialog.setCanceledOnTouchOutside(true);
        fMapTypeDialog.show();
    }



    private void setFokusMap() {
        new FokusServices().getArrayFokus(Constants.serverUrl+"/spots", new JSONArrayRequestListener() {

            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jsonObj;
                        try {
                            jsonObj = response.getJSONObject(i);

                            LatLng currentLocation = new LatLng(jsonObj.getDouble("lat"), jsonObj.getDouble("long"));

                            Marker mMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                            // set object as tag
                            mMarker.setTag(jsonObj);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onError(ANError anError) {
                 Log.d(TAG,anError.getErrorDetail());
            }
        });

    }

}
