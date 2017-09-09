package com.example.carlos.fokus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListFokus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fokus);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
