package com.example.carlos.fokus.adapter;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.carlos.fokus.R;
import com.example.carlos.fokus.constants.Constants;
import com.example.carlos.fokus.fragments.DialogMapFragment;
import com.example.carlos.fokus.model.Spot;
import com.example.carlos.fokus.services.FokusServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eceybrito on 10/09/2017.
 */

public class DefaultSpotAdapter extends RecyclerView.Adapter<DefaultSpotAdapter.ViewHolder>
    implements Filterable {

    private List<Spot> spots;
    private List<Spot> mArrayList;
    private Context ctx;
    private AppCompatActivity activity;

    public DefaultSpotAdapter(Context context, List<Spot> arrayList, AppCompatActivity activity) {
        this.mArrayList = arrayList;
        this.spots = arrayList;
        this.ctx = context;
        this.activity = activity;
    }

    @Override
    public DefaultSpotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.foku_row, parent, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Spot spot = spots.get(position);

        // setting values to view
        holder.nameFoku.setText(spot.getName());
        holder.createdAtFoku.setText(spot.getCreatedAt());
        holder.descriptionFoku.setText(spot.getDescription());

        // use glide library to load image for foku
        ImageView imageFoku = holder.imageFoku;
        Glide.with(ctx)
                .load(Constants.serverUrl + "/" + spot.getImage())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .override(600, 200)
                .centerCrop()
                .into(imageFoku);

        // setup popup listener
        holder.ibMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //PopupMenu popup = new PopupMenu(ctx, view);
                Context wrapper = new ContextThemeWrapper(ctx, R.style.MyPopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, view);
                MenuInflater inflate = popup.getMenuInflater();
                inflate.inflate(R.menu.popup_menu_card, popup.getMenu());
                popup.setOnMenuItemClickListener(new MyPopupMenu(activity));
                popup.show();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return spots.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    spots = mArrayList;
                } else {

                    List<Spot> filteredList = new ArrayList<>();

                    for (Spot spot : mArrayList) {

                        if (spot.getName().toLowerCase().contains(charSequence)) {

                            filteredList.add(spot);
                        }
                    }

                    spots = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = spots;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                spots = (List<Spot>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameFoku;
        public TextView createdAtFoku;
        public TextView descriptionFoku;
        public ImageView imageFoku;
        public ImageButton ibMore;

        public ViewHolder(View v) {
            super(v);
            nameFoku = (TextView) v.findViewById(R.id.nameFoku);
            createdAtFoku = (TextView) v.findViewById(R.id.createdAtFoku);
            descriptionFoku = (TextView) v.findViewById(R.id.descriptionFoku);
            imageFoku = (ImageView) v.findViewById(R.id.imageFoku);
            ibMore = (ImageButton) v.findViewById(R.id.ibFokuDetails);
        }
    }
}

class MyPopupMenu implements android.support.v7.widget.PopupMenu.OnMenuItemClickListener {

    private AppCompatActivity activity;

    public MyPopupMenu(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.map_look:

                /*FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                DialogMapFragment df = new DialogMapFragment();
                ft.addToBackStack(null);
                df.show(ft, "Tag");*/

                break;

            default:
                break;
        }

        return false;
    }
}
