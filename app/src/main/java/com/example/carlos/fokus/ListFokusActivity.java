package com.example.carlos.fokus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.carlos.fokus.adapter.DefaultSpotAdapter;
import com.example.carlos.fokus.constants.Constants;
import com.example.carlos.fokus.model.Spot;
import com.example.carlos.fokus.services.GetListFokusService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class ListFokusActivity extends AppCompatActivity {

    private RecyclerView fokusRecycler;
    private DefaultSpotAdapter fokusAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Spot> listFoku = new ArrayList<>();

    //DisplayUI ui = new DisplayUI(this.getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fokus);

        initializeComponents();
    }

    private void initializeComponents() {

        Toast.makeText(this, "List of fokus", Toast.LENGTH_SHORT).show();

        fokusRecycler = (RecyclerView) findViewById(R.id.fokus_recycler);

        fokusRecycler.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        fokusRecycler.setLayoutManager(mLayoutManager);
        //fokusRecycler.setVisibility(View.VISIBLE);

        listFoku = getFokusList();

        // specify an adapter (see also next example)
        fokusAdapter = new DefaultSpotAdapter(listFoku);
        fokusRecycler.setAdapter(fokusAdapter);

        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private List<Spot> getFokusList() {

        final List<Spot> listFok = new ArrayList<>();

        new GetListFokusService().call(Constants.serverUrl + "/spots", new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jsonObj = null;
                        try {
                            jsonObj = response.getJSONObject(i);

                            int id = jsonObj.getInt("id");
                            //int userId = jsonObj.getInt("user_id");
                            String name = jsonObj.getString("name");              // to be in
                            String created_at = jsonObj.getString("created_at");  // to be in
                            String deleted_at = jsonObj.getString("created_at");
                            String updated_at = jsonObj.getString("updated_at");
                            String description = jsonObj.getString("description"); // to be in
                            String image = jsonObj.getString("image");             // to be in
                            double lat = jsonObj.getDouble("lat");
                            double longit = jsonObj.getDouble("long");
                            String device_id = jsonObj.getString("device_id");

                            // add fields to list
                            /*listFok.add(new Spot(id, name, lat, longit, userId, deleted_at,
                                    created_at, updated_at, description, image, device_id)); */

                            listFok.add(new Spot(id, name, created_at, description, image));

                            Toast.makeText(ListFokusActivity.this, "" + name, Toast.LENGTH_SHORT).show();

                            Log.d("name", "name");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onError(ANError anError) {
                //ui.showToast("" + anError.getErrorDetail());
                Toast.makeText(ListFokusActivity.this, "" + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
            }
        });

        return listFok;
    }
}
