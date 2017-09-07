package fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.carlos.fokus.R;

/**
 * Created by eceybrito on 07/09/2017.
 */

public class FokusDescription extends DialogFragment {

    private int mNum;

    // create a dialog instance
    public static FokusDescription newInstance (int num) {

        FokusDescription fokusDescription = new FokusDescription();

        Bundle args = new Bundle();
        args.putInt("num", num);
        fokusDescription.setArguments(args);

        return fokusDescription;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNum = getArguments().getInt("num");
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup conatiner,
                              Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.custom_info_contents, conatiner, false);

        // pickup a view from layout resource
        View title = v.findViewById(R.id.title);

        ((TextView)title).setText("Dialog" + mNum);

        return v;
    }
}
