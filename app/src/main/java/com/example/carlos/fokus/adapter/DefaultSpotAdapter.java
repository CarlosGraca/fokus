package com.example.carlos.fokus.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.carlos.fokus.R;
import com.example.carlos.fokus.constants.Constants;
import com.example.carlos.fokus.model.Spot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eceybrito on 10/09/2017.
 */

public class DefaultSpotAdapter extends RecyclerView.Adapter<DefaultSpotAdapter.ViewHolder>
    implements Filterable{

    private List<Spot> spots;
    private List<Spot> mArrayList;
    private Context ctx;

    public DefaultSpotAdapter(Context context, List<Spot> arrayList) {
        this.mArrayList = arrayList;
        this.spots = arrayList;
        this.ctx = context;
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
    public void onBindViewHolder(ViewHolder holder, int position) {

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

        public ViewHolder(View v) {
            super(v);
            nameFoku = (TextView) v.findViewById(R.id.nameFoku);
            createdAtFoku = (TextView) v.findViewById(R.id.createdAtFoku);
            descriptionFoku = (TextView) v.findViewById(R.id.descriptionFoku);
            imageFoku = (ImageView) v.findViewById(R.id.imageFoku);
        }
    }
}
