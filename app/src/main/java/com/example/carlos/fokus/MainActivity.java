package com.example.carlos.fokus;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import constants.ApiUrls;
import com.example.carlos.fokus.helpers.MapFunctions;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener  {
    private GoogleMap mMap;
    private RequestQueue requestQueue;
    private String apiUrl = ApiUrls.apiUrlTest;
    private static final String TAG = "MainActivity";

    // my api fields returned
    //private String description;
    private int device_id;
    private String name;
    private String description;

    // marker to setup click listener
    private Marker mMarker;

    private static final CharSequence[] MAP_TYPE_ITEMS =
            {"Mapa de rua", "Hibrido", "Satellite", "Terreno"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
    }

    public void initializeComponents () {

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

        requestQueue = Volley.newRequestQueue(this);

        callApi();
    }

    private void callApi() {

        JsonArrayRequest arrReq = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // verify if there a response back do something with it
                        if (response.length() > 0) {

                            // loop through them all
                            for (int i = 0; i <response.length(); i++) {

                                try {

                                    JSONObject jsonObj = response.getJSONObject(i);

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
                                    showLog("Volley, json object invalid:  " + e.getMessage());
                                }
                            }

                        } else {

                            // if there is no response, print something
                            showToast("No response from api");
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // if there is a connection problem, so log this error
                        showToast("Volley error:  " + error.toString());
                    }
                }

        );

        requestQueue.add(arrReq);
    }

    private void showLog(String s) {
        Log.d(TAG, s);
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_api_test) {
            callApi();
        }

        if (id == R.id.action_my_points) {
            callApi();
        }

        if (id == R.id.action_about) {
            callApi();
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

        mMarker = mMap.addMarker(new MarkerOptions().
                position(currentLocation).
                title("Marker in Sydney"));

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
