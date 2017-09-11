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

    private List<Spot> listFok = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fokus);

        //initializeComponents();
        getFokusList();
    }

    private void initializeComponents() {

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getFokusList() {

        new GetListFokusService().call(Constants.serverUrl + "/spots", new JSONArrayRequestListener() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {

                    for (int i = 0; i < response.length(); i++) {

                        Spot spot = new Spot();

                        JSONObject jsonObj = null;

                        try {

                            jsonObj = response.getJSONObject(i);

                            spot.setName(jsonObj.getString("name"));
                            spot.setCreatedAt(jsonObj.getString("created_at"));
                            spot.setDescription(jsonObj.getString("description"));
                            spot.setImage(jsonObj.getString("image"));

                            Toast.makeText(ListFokusActivity.this, listFok.get(0).getName() + listFok.get(1).getName(), Toast.LENGTH_SHORT).show();

                            Log.d("name", listFok.get(0).getName());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        listFok.add(spot);

                    }
                    // specify an adapter (see also next example)
                    fokusRecycler = (RecyclerView) findViewById(R.id.fokus_recycler);
                    fokusRecycler.setHasFixedSize(true);
                    fokusAdapter = new DefaultSpotAdapter(getApplicationContext(),listFok);
                    fokusRecycler.setAdapter(fokusAdapter);
                    mLayoutManager = new LinearLayoutManager(ListFokusActivity.this);
                    fokusRecycler.setLayoutManager(mLayoutManager);
                    fokusRecycler.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(ANError anError) {
                //ui.showToast("" + anError.getErrorDetail());
                Toast.makeText(ListFokusActivity.this, "" + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
