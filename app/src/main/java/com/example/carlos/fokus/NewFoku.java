package com.example.carlos.fokus;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.carlos.fokus.constants.Constants;
import com.example.carlos.fokus.helpers.ApiImage;
import com.example.carlos.fokus.helpers.MapFunctions;
import com.example.carlos.fokus.services.FokusServices;
import com.example.carlos.fokus.utils.BaseMapActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vistrav.pop.Pop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;



public class NewFoku extends BaseMapActivity {
    private static final String TAG = NewFoku.class.getSimpleName();

    private Marker marker;

    private ImageView imageView;

    final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private Uri file;

    private File image_foku;

    String deviceID;
    String mDescription;
    String mRating;
    String mName;
    String mLat;
    String mLong;

    @Override
    protected void startMap() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        //fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewFoku.this, MainActivity.class);
                startActivity(intent);
            }
        });
        setEventsMap(getMap());
        //getting unique id for device
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    //EVENTS
    public void setEventsMap(GoogleMap map){

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null){
                    marker.remove();
                }
                costumerMarker(new LatLng(latLng.latitude,latLng.longitude));
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showDialogFokus(marker);
                return false;
            }
        });
    }

    public void costumerMarker(LatLng latLng){
        marker = getMap().addMarker(new MarkerOptions().position(latLng)
                .draggable(true));

        String adrress = MapFunctions.getAddress(this.getApplicationContext(), latLng.latitude, latLng.longitude);
        marker.setTag(adrress);
    }

    public void showDialogFokus(Marker mMarker){

        marker = mMarker;
        mName = (String) marker.getTag();// Type cast to your object type;

        final Pop pop = Pop.on(this)
                .with()
                .cancelable(true)
                .title(mName)
                .layout(R.layout.custom_info_contents);

        pop.show(new Pop.View() {
            @Override
            public void prepare(final View view) { // assign value to view element
                Button dialogButton = (Button) view.findViewById(R.id.btSend);
                imageView = (ImageView) view.findViewById(R.id.imgFoku);
                final EditText description = (EditText) view.findViewById(R.id.description);
                //final TextView location = (TextView) view.findViewById(R.id.location);

                //image_foku = null;

                final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

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
                                view.setVisibility(View.GONE);
                                pop.dismissDialog();
                            }
                        }
                    }
                });

            }
        });

        /*final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.custom_info_contents);

        imageView = (ImageView) dialog.findViewById(R.id.imgFoku);

        image_foku = null;

        // set the custom dialog components - text, image and button
        final EditText description = (EditText) dialog.findViewById(R.id.description);
        final TextView location = (TextView) dialog.findViewById(R.id.location);

        Button dialogButton = (Button) dialog.findViewById(R.id.btSend);

        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                mRating = String.valueOf(rating);
            }
        });

        // to retrieve the marker
        mName = (String) marker.getTag();// Type cast to your object type;

        //location.setText(mName);

        dialog.setTitle(mName);

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
        dialog.show();*/
    }

    public boolean checkFieldsRequired(TextView textView,RatingBar ratingBar){
        if (textView.getText().toString().trim().equals("")) {
            textView.setError("Required!");
            return false;
        }
        return true;
    }

    public boolean saveFokus(final Marker marker){
        final String url =  Constants.serverUrl + "/spots";

        mLong = String.valueOf(marker.getPosition().longitude);
        mLat = String.valueOf(marker.getPosition().latitude);

        new FokusServices().saveFoku(url,mName, mLong, mLat, deviceID, mDescription,image_foku, mRating, new JSONObjectRequestListener() {
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
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

}

