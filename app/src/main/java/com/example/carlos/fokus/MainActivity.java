package com.example.carlos.fokus;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.carlos.fokus.services.GetListFokusService;
import com.example.carlos.fokus.ui.DisplayUI;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.example.carlos.fokus.constants.Constants;
import com.example.carlos.fokus.helpers.MapFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {
    private GoogleMap mMap;
    private String apiUrl = Constants.serverUrl+"/posts";

    private static final String TAG = MainActivity.class.getSimpleName();
    private String name;
    private String description;

    // marker to setup click listener
    private Marker mMarker;

    private static final CharSequence[] MAP_TYPE_ITEMS =
            {"Mapa de rua", "Hibrido", "Satellite", "Terreno"};

    private DisplayUI ui ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
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

        callApi();
    }

    private void callApi() {
        new GetListFokusService().call(Constants.serverUrl+"/spots", new JSONArrayRequestListener() {

            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jsonObj = null;
                        try {
                            jsonObj = response.getJSONObject(i);

                            int id = jsonObj.getInt("id");
                            name = jsonObj.getString("name");
                            double lat = jsonObj.getDouble("lat");
                            double longit = jsonObj.getDouble("long");

                            LatLng currentLocation = new LatLng(lat, longit);

                            MapFunctions.updateMarkers(
                                    mMap,
                                    name,
                                    id,
                                    currentLocation,
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
            @Override
            public void onError(ANError anError) {

            }
        });

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

        LatLng currentLocation = new LatLng(14.9364475, -23.5067295);

        /*mMarker = mMap.addMarker(new MarkerOptions().
                position(currentLocation).
                title("Marker in Sydney")); */

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));

        mMap.setOnMarkerClickListener(this);
    }

    public void showDialogFokus(Marker marker){
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Detalhes do foku");

        dialog.setContentView(R.layout.custom_info_contents);

        ImageView imageView = (ImageView) dialog.findViewById(R.id.imageCamera);

        // set the custom dialog components - text, image and button
        final TextView description = (TextView) dialog.findViewById(R.id.description);

        Button dialogButton = (Button) dialog.findViewById(R.id.btSend);
        RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);

        dialogButton.setVisibility(View.GONE);
        ratingBar.setEnabled(false);

        String url = Constants.serverUrl+"/spots/"+ Math.round(marker.getZIndex());

        new GetListFokusService().call(url, new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                ui.showToast("" + response.length());
                /*JSONObject jsonObj = null;
                try {
                    jsonObj = response.getJSONObject(0);

                    int id = jsonObj.getInt("id");
                    name = jsonObj.getString("name");

                    ui.showToast("" + name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/


            }

            @Override
            public void onError(ANError anError) {
                ui.showLog("" + anError.getErrorDetail());
            }
        });

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

    public boolean onMarkerClick(Marker marker) {

        showDialogFokus(marker);

        return true;
    }

}
