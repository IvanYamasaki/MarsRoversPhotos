package com.ivangy.marsroversphotos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ivangy.marsroversphotos.Helper;
import com.ivangy.marsroversphotos.R;
import com.ivangy.marsroversphotos.adapter.PhotosAdapter;

import java.util.Objects;

public class RecyclerPhotosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_photos);

        Helper.setRecyclerConfig(findViewById(R.id.recyclerMarsPhotos), new GridLayoutManager(getApplicationContext(), 4), new PhotosAdapter(MainActivity.listPhotos));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView lblSun = findViewById(R.id.lblCountSun);
        TextView lblRover = findViewById(R.id.lblRover);

        lblSun.append(String.valueOf(MainActivity.sun));
        lblRover.setText(MainActivity.rover);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
