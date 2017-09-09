package fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carlos.fokus.R;

/**
 * Created by eceybrito on 07/09/2017.
 */

public class FokusDescription extends DialogFragment {

    private String mDesc;

    public RatingBar mRatingBar;

    private ImageView imageFoku;

    private Button btEnviar;

    private String imageFokuUrl;

    // create a dialog instance
    public static FokusDescription newInstance (String name, String description, int device_id) {

        FokusDescription fokusDescription = new FokusDescription();

        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("description", description);
        args.putInt("description", device_id);
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

        imageFoku = (ImageView) v.findViewById(R.id.imgFoku);

        // using glide to set image to image view
        /*Glide.with(this)
            .load(imageFokuUrl)
            .into(imageFoku); */


        mRatingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        //mRatingBar.setClickable(false);
        mRatingBar.setEnabled(false);

        return v;
    }
}
