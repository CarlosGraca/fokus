package com.example.carlos.fokus;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.bumptech.glide.Glide;
import com.example.carlos.fokus.constants.Constants;
import com.example.carlos.fokus.services.FokusServices;
import com.example.carlos.fokus.utils.BaseMapActivity;
import com.example.carlos.fokus.utils.model.MyItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.vistrav.pop.Pop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseMapActivity implements ClusterManager.OnClusterClickListener<MyItem>, ClusterManager.OnClusterInfoWindowClickListener<MyItem>, ClusterManager.OnClusterItemClickListener<MyItem>, ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>  {
    private ClusterManager<MyItem> mClusterManager;
    private List<MyItem> items;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void startMap() {
        Log.d(TAG, String.valueOf(getMarker()));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewFoku.class);

                /*Bundle bundle = new Bundle();

                bundle.putString("lat", String.valueOf(getMap().getMyLocation().getLatitude()));
                bundle.putString("long", String.valueOf(getMap().getMyLocation().getLongitude()));
                intent.putExtras(bundle);*/


                startActivity(intent);
            }
        });

        mClusterManager = new ClusterManager<MyItem>(this, getMap());
        items = new ArrayList<MyItem>();
        getMap().setOnCameraIdleListener(mClusterManager);

        setFokusMap();

        setEventsMap(getMap());

        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener( this);

    }

    class OwnIconRendered extends DefaultClusterRenderer<MyItem> {

        public OwnIconRendered(Context context, GoogleMap map,
                               ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(item.getIdIcon()));
            super.onBeforeClusterItemRendered(item, markerOptions);
        }

        @Override
        protected void onClusterItemRendered(MyItem clusterItem, Marker marker) {
            marker.setTag(clusterItem.getOBject());
            super.onClusterItemRendered(clusterItem, marker);
        }
    }

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    @Override
    public void onClusterInfoWindowClick(Cluster<MyItem> cluster) {
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(MyItem item) {
        showDialogFokus(item);
        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem item) {
        // Does nothing, but you could go into the user's profile page, for example.

    }


    public void setEventsMap(GoogleMap map){
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
               // showDialogFokus(marker);
                return false;
            }
        });
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
                            float bitDesc = 0;
                            if (jsonObj.getString("status").equals(Constants.marked)) {
                                bitDesc = BitmapDescriptorFactory.HUE_ORANGE;
                            }else if (jsonObj.getString("status").equals(Constants.confirmed)) {
                                bitDesc = BitmapDescriptorFactory.HUE_RED;
                            }else if (jsonObj.getString("status").equals(Constants.sorted_out)) {
                                bitDesc = BitmapDescriptorFactory.HUE_GREEN;
                            }
                            Log.d("Response iCon 1", String.valueOf(bitDesc));
                            items.add(new MyItem(jsonObj.getDouble("lat"), jsonObj.getDouble("long"), jsonObj.getString("name"), jsonObj.getString("description"),bitDesc,jsonObj));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mClusterManager.addItems(items);
                    mClusterManager.setRenderer(new OwnIconRendered(MainActivity.this, getMap(), mClusterManager));
                    Log.d("Response ", String.valueOf(items));
                }
            }
            @Override
            public void onError(ANError anError) {
                Log.d("Response Error ",anError.getErrorDetail());
            }
        });

    }

    public void showDialogFokus(MyItem item){
        final JSONObject obj = (JSONObject) item.getOBject();
        final String serverUrl = Constants.serverUrl;
        try {
        Pop.on(this)
                .with()
                .cancelable(true)
                .title(obj.getString("name"))
                .layout(R.layout.custom_info_contents)
                .show(new Pop.View() {
                    @Override
                    public void prepare(View view) { // assign value to view element
                        Button dialogButton = (Button) view.findViewById(R.id.btSend);
                        final ImageView imageView = (ImageView) view.findViewById(R.id.imgFoku);
                        final EditText description = (EditText) view.findViewById(R.id.description);
                        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
                        dialogButton.setVisibility(View.GONE);
                        description.setCursorVisible(false);
                        try {
                            ratingBar.setRating(obj.getInt("rating"));
                            Glide.with(MainActivity.this).load(serverUrl+"/"+obj.getString("image")).into(imageView);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            description.setText(obj.getString("description"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*final Dialog dialog = new Dialog(this);
        final String serverUrl = Constants.serverUrl;

        dialog.setContentView(R.layout.custom_info_contents);

        final ImageView imageView = (ImageView) dialog.findViewById(R.id.imgFoku);

        // set the custom dialog components - text, image and button
        final EditText description = (EditText) dialog.findViewById(R.id.description);
        final TextView location = (TextView) dialog.findViewById(R.id.location);
        location.setVisibility(View.INVISIBLE);
        description.setCursorVisible(false);

        Button dialogButton = (Button) dialog.findViewById(R.id.btSend);
        RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);

        dialogButton.setVisibility(View.GONE);
       /* ratingBar.setEnabled(false);
        description.setEnabled(false);*/

        // to retrieve the marker
        /*JSONObject obj = (JSONObject) item.getOBject();// Type cast to your object type;
        try {
            dialog.setTitle(obj.getString("name"));
            description.setText(obj.getString("description"));
           // location.setText(obj.getString("name"));
            ratingBar.setRating(obj.getInt("rating"));
            Glide.with(this).load(serverUrl+"/"+obj.getString("image")).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();*/
    }
}
