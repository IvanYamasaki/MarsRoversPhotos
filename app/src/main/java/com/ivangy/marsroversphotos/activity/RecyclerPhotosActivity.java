package com.ivangy.marsroversphotos.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ivangy.marsroversphotos.R;
import com.ivangy.marsroversphotos.adapter.PhotosAdapter;

import java.util.ArrayList;

import static java.lang.String.valueOf;

public class RecyclerPhotosActivity extends AppCompatActivity {

    private RecyclerView recyclerMarsPhotos;
    private TextView lblSun, lblRover;
    private ImageButton btnNextSun, btnPrevSun;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_photos);

        recyclerMarsPhotos = findViewById(R.id.recyclerMarsPhotos);
        recyclerMarsPhotos.setHasFixedSize(true);
        recyclerMarsPhotos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        lblSun = findViewById(R.id.lblCountSun);
        lblRover = findViewById(R.id.lblRover);
        lblSun.append(" " + MainActivity.querySun);
        lblRover.setText(" " + MainActivity.queryRover);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                RecyclerPhotosActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void callBack(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        RecyclerPhotosActivity.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerMarsPhotos.setAdapter(new PhotosAdapter());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        RecyclerPhotosActivity.this.finish();
    }
}
