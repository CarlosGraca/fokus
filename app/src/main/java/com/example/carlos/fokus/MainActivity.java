package com.example.carlos.fokus;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.location.LocationListener;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    private RequestQueue requestQueue;
    private String apiUrl = "http://app.yournit.com/spots";
    private static final String TAG = "MainActivity";

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
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                //Click Button
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
                                    String name = jsonObj.getString("name");
                                    double lat = jsonObj.getDouble("lat");
                                    double longit = jsonObj.getDouble("long");
                                    int user_id = jsonObj.getInt("user_id");
                                    String deleted_at = jsonObj.getString("deleted_at");
                                    String created_at = jsonObj.getString("created_at");
                                    String updated_at = jsonObj.getString("updated_at");
                                    //String description = jsonObj.getString("description");

                                    showLog("Response fom api:  " + "LATITUDE: " + lat + " LONGITUDE: " +longit);

                                    showToast("Response fom api:  " + "LATITUDE: " + lat + " LONGITUDE: " +longit);

                                    updateMapMarkers(mMap, lat, longit, name);

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng currentLocation = new LatLng(14.9364475, -23.5067295);

        //mMap.addMarker(new MarkerOptions().position(currentLocation).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
    }

    public void updateMapMarkers (GoogleMap map, double lat, double longt, String title) {

        map.addMarker(new MarkerOptions()
                .position(new LatLng(lat, longt))
                .title(title)
        );
    }
}
