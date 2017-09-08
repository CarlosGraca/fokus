package com.example.carlos.fokus.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.carlos.fokus.R;

/**
 * Created by eceybrito on 07/09/2017.
 */

public class FokusDescription extends DialogFragment {

    private String mDesc;

    public RatingBar mRatingBar;

    // create a dialog instance
    public static FokusDescription newInstance (String description) {

        FokusDescription fokusDescription = new FokusDescription();

        Bundle args = new Bundle();
        args.putString("description", description);
        fokusDescription.setArguments(args);

        return fokusDescription;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDesc = getArguments().getString("description");
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup conatiner,
                              Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.custom_info_contents, conatiner, false);

        // pickup a view from layout resource
        View description = v.findViewById(R.id.description);

        ((TextView)description).setText("" + mDesc);
        description.setEnabled(false);

        mRatingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        //mRatingBar.setClickable(false);
        mRatingBar.setEnabled(false);

        return v;
    }
}
