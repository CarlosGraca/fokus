package com.example.carlos.fokus;


import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.carlos.fokus.constants.Constants;
import com.example.carlos.fokus.helpers.ApiImage;
import com.example.carlos.fokus.services.FokusServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.carlos.fokus.helpers.MapFunctions.DEFAULT_ZOOM;
import static com.example.carlos.fokus.helpers.MapFunctions.mDefaultLocation;


public class NewFoku extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = NewFoku.class.getSimpleName();

    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private Marker marker;

    private ImageView imageView;

    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private Uri file;

    private File image_foku;

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "Fokus";

    private Uri fileUri;
    static File mediaFile;


    String deviceID;
    String mDescription;
    String mRating;
    String mTitle;
    String mLat;
    String mLong;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_new_foku);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNewFokus);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapLocation);
        mapFragment.getMapAsync(this);

        //queue = Volley.newRequestQueue(this);  // this = context

        //getting unique id for device
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.d("deviceID",deviceID);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

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

    //EVENTS
    public void setEventsMap(){

        // mMap.setOnCameraMoveListener(new GoogleMap.OnCameraChangeListener());
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d("setOnMapClickListener","latLng");
                if (marker != null){
                    marker.remove();
                }
                costumerMarker(new LatLng(latLng.latitude,latLng.longitude),"New Fokus","New Fokus");
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showDialogFokus(marker);
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.d("String","Marker"+marker.getTitle());
            }
        });
    }



    public void showDialogFokus(Marker mMarker){

        marker = mMarker;

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.custom_info_contents);

        imageView = (ImageView) dialog.findViewById(R.id.imgFoku);

        image_foku = null;

        // set the custom dialog components - text, image and button
        final TextView description = (TextView) dialog.findViewById(R.id.description);

        Button dialogButton = (Button) dialog.findViewById(R.id.btSend);

        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                mRating = String.valueOf(rating);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });


        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDescription = String.valueOf(description.getText());
                if (checkFieldsRequired(description, ratingBar)){
                    if (saveFokus(marker)){
                        Toast.makeText(NewFoku.this, "Sua Denuncia foi enviado!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }
        });

        dialog.show();
    }

    public boolean checkFieldsRequired(TextView textView,RatingBar ratingBar){
        if (textView.getText().toString().trim().equals("")) {
            textView.setError("Required!");
            return false;
        }
        return true;
    }

    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if  (task.isSuccessful() && task.getResult() != null) {
                                // Set the map's camera position to the current location of the device.
                                mLastKnownLocation = task.getResult();

                                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude()))
                                                               .draggable(true));

                                 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            marker = mMap.addMarker(new MarkerOptions().position(mDefaultLocation));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        setEventsMap();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void costumerMarker(LatLng latLng, String title, String snippet){
        marker = mMap.addMarker(new MarkerOptions().position(latLng)
                .draggable(true));
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        image_foku = getOutputMediaFile();
        file = Uri.fromFile(image_foku);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            image_foku = ApiImage.compressImage(image_foku);

            imageView.setImageURI(file);

        }

    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Fokus");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    public boolean saveFokus(final Marker marker){
        final boolean mSuccess;
        final String url =  Constants.serverUrl + "/spots";

        mLong = String.valueOf(marker.getPosition().longitude);
        mLat = String.valueOf(marker.getPosition().latitude);
        Log.d("url",Constants.serverUrl + "/spots");

        new FokusServices().saveFoku(url, mLong, mLat, deviceID, mDescription,image_foku, mRating, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, response.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.d("URl",url);
                if (anError.getErrorCode() != 0) {
                    Log.d(TAG, "onError errorCode : " + anError.getErrorCode());
                    Log.d(TAG, "onError errorBody : " + anError.getErrorBody());
                    Log.d(TAG, "onError errorDetail : " + anError.getErrorDetail());
                } else {
                    Log.d(TAG, "onError errorDetail : " + anError.getErrorDetail());
                }
            }
        });
        return true;
    }
}

