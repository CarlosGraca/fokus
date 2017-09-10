package com.example.carlos.fokus.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.carlos.fokus.R;
import com.example.carlos.fokus.model.Spot;
import com.example.carlos.fokus.model.Spots;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eceybrito on 10/09/2017.
 */

public class DefaultSpotAdapter extends RecyclerView.Adapter<DefaultSpotAdapter.ViewHolder> {

    private List<Spot> spots = new ArrayList<>();

    public DefaultSpotAdapter(List<Spot> mSpots) {
       spots = mSpots;
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

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return spots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameFoku;
        public TextView createdAtFoku;
        public TextView descriptionFoku;
        public ImageView imageFoku;

        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            nameFoku = (TextView) v.findViewById(R.id.nameFoku);
            createdAtFoku = (TextView) v.findViewById(R.id.createdAtFoku);
            descriptionFoku = (TextView) v.findViewById(R.id.descriptionFoku);
            imageFoku = (ImageView) v.findViewById(R.id.imageFoku);
        }
    }
}
