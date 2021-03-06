package com.example.carlos.fokus;

import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.carlos.fokus.adapter.DefaultSpotAdapter;
import com.example.carlos.fokus.constants.Constants;
import com.example.carlos.fokus.fragments.DialogMapFragment;
import com.example.carlos.fokus.model.Spot;
import com.example.carlos.fokus.services.FokusServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListFokusActivity extends AppCompatActivity
    implements CompoundButton.OnCheckedChangeListener {

    private RecyclerView fokusRecycler;
    private DefaultSpotAdapter fokusAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Spot> listFok = new ArrayList<>();

    private Switch switchMyFokusOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fokus);

        initializeComponents();

        getFokusList(Constants.serverUrl + "/spots");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_fokus, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home ) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeComponents() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarListFokus);
        setSupportActionBar(toolbar);

        switchMyFokusOnly = (Switch) findViewById(R.id.switch_top);
        switchMyFokusOnly.setOnCheckedChangeListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getFokusList(String url) {

        new FokusServices().getArrayFokus(url, new JSONArrayRequestListener() {
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
                            spot.setId(jsonObj.getInt("id"));
                            //spot.setUserId(jsonObj.getInt("user_id"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        listFok.add(spot);

                    }
                    // specify an adapter (see also next example)
                    fokusRecycler = (RecyclerView) findViewById(R.id.fokus_recycler);
                    fokusRecycler.setHasFixedSize(true);
                    fokusAdapter = new DefaultSpotAdapter(getApplicationContext(),listFok, ListFokusActivity.this);
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
    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query == null || query.length() < 0) {

                    Toast.makeText(ListFokusActivity.this, "Nenhum foku", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                fokusAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {

            String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

            listFok.clear();

            getFokusList(Constants.serverUrl + "/spots?device_id=" + deviceID);

            fokusAdapter.notifyDataSetChanged();

        } else {

            listFok.clear();

            getFokusList(Constants.serverUrl + "/spots");

            fokusAdapter.notifyDataSetChanged();
        }
    }
}
