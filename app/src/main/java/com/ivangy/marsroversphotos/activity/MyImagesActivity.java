package com.ivangy.marsroversphotos.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ivangy.marsroversphotos.Helper;
import com.ivangy.marsroversphotos.R;
import com.ivangy.marsroversphotos.adapter.PhotosAdapter;


public class MyImagesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_images);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.setRecyclerConfig(findViewById(R.id.recyclerFavoriteImages),
                new GridLayoutManager(getApplicationContext(), 4),
                new PhotosAdapter(MainActivity.favoritedPhotos.getImages(this)));
    }
}