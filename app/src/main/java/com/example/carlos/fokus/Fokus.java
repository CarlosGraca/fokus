package com.example.carlos.fokus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Fokus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fokus);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
