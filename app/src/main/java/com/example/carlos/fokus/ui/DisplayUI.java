package com.example.carlos.fokus.ui;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by eceybrito on 09/09/2017.
 */

public class DisplayUI {

    private Context context;

    private static final String TAG = DisplayUI.class.getSimpleName();

    public DisplayUI(Context context) {
        this.context = context;
    }

    public void showLog(String s) {
        Log.d(TAG, s);
    }

    public void showToast(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

}
