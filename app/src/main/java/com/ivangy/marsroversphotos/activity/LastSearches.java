package com.ivangy.marsroversphotos.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ivangy.marsroversphotos.DatabaseHelper;
import com.ivangy.marsroversphotos.Helper;
import com.ivangy.marsroversphotos.R;
import com.ivangy.marsroversphotos.adapter.PhotosAdapter;

public class LastSearches extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_searches);
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        RecyclerView recyclerLastSearches = findViewById(R.id.recyclerLastSearches);
        Helper.setRecyclerConfig(recyclerLastSearches,
                new GridLayoutManager(getApplicationContext(), 4),
                new PhotosAdapter(databaseHelper.getPhotoList()));

        Button btnDelete = findViewById(R.id.btnDeleteAll);
        btnDelete.setOnClickListener(v -> {
            databaseHelper.deleteAll();
            recyclerLastSearches.getAdapter().notifyDataSetChanged();
        });
    }
}