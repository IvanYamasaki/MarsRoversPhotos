package com.ivangy.marsroversphotos.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.ivangy.marsroversphotos.Helper;
import com.ivangy.marsroversphotos.LoadPhotos;
import com.ivangy.marsroversphotos.R;
import com.ivangy.marsroversphotos.model.Photo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import static com.ivangy.marsroversphotos.Helper.toast;
import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String> {

    private Spinner spinRover;
    public static ArrayList<Photo> listPhotos = new ArrayList<>();
    private ConstraintLayout MainLayout;
    private ProgressBar pgBar;
    public static String rover;
    public static int sun;
    private EditText txtSun;
    private int[] backImages = new int[]{R.drawable.curiosity_theme,
            R.drawable.opportunity_theme,
            R.drawable.spirit_theme};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pgBar = findViewById((R.id.pgBar));
        MainLayout = findViewById(R.id.MainLayout);
        spinRover = findViewById(R.id.spinRover);
        txtSun = findViewById(R.id.txtSun);

        spinRover.setAdapter(ArrayAdapter.createFromResource(MainActivity.this, R.array.rovers, R.layout.my_spinner));

        if (savedInstanceState != null)
            txtSun.setText(savedInstanceState.getString("querySun"));
        txtSun.setOnFocusChangeListener((v, hasFocus) -> txtSun.setText(""));

        txtSun.setOnEditorActionListener((v, actionId, event) -> {
            Helper.hideKeyboard(MainActivity.this);
            btnSearch(v);
            return true;
        });

        spinRover.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainLayout.setBackgroundResource(backImages[position]);
                rover = spinRover.getSelectedItem().toString().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (getSupportLoaderManager().getLoader(0) != null)
            getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        spinRover.setSelection(new Random().nextInt(3));
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryRover = null, querySun = null;
        if (args != null) {
            queryRover = args.getString("queryRover");
            querySun = args.getString("querySun");
        }
        return new LoadPhotos(this, queryRover, querySun);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        Log.d("Array:", data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray photos = jsonObject.getJSONArray("photos");

            for (int i = 0; i < photos.length(); i++) {
                JSONObject items = photos.getJSONObject(i);
                JSONObject camera = items.getJSONObject("camera");
                JSONObject rover = photos.getJSONObject(i).getJSONObject("rover");
                listPhotos.add(new Photo(items.getInt("id"),
                        camera.getString("name"),
                        camera.getString("full_name"),
                        items.getString("img_src"),
                        items.getString("earth_date"),
                        MainActivity.rover,
                        sun,
                        rover.getString("status"),
                        rover.getString("landing_date"),
                        rover.getString("launch_date")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (listPhotos.size() != 0) {
            toast(this, "Loading...");
            startActivity(new Intent(this, RecyclerPhotosActivity.class));

            finish();
        } else {
            toast(this, "No images available for this Sun...");
        }
        pgBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
    }

    public void restartLoader(String queryRover, String querySun) {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connManager != null)
            networkInfo = connManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Bundle bundle = new Bundle();
            bundle.putString("queryRover", queryRover);
            bundle.putString("querySun", querySun);
            getSupportLoaderManager().restartLoader(0, bundle, this);
        } else {
            toast(this, "Connection Failure");
        }
    }

    public void btnSearch(View v) {
        pgBar.setVisibility(View.VISIBLE);
        sun = Integer.parseInt(String.valueOf(txtSun.getText()));
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null)
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        restartLoader(spinRover.getSelectedItem().toString().toLowerCase(), valueOf(txtSun.getText()));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        toast(this, "sdf");
        outState.putString("querySun", txtSun.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        txtSun.setText(savedInstanceState.getString("querySun"));
    }

    public void openNasaWeb(View view) {
        startActivity(new Intent(getApplicationContext(), NasaWebsiteActivity.class));
    }

    public void startGeolocationActivity(View v) {
        startActivity(new Intent(this, GeolocationActivity.class));
    }

    public void startMyImages(View v) {
        startActivity(new Intent(this, MyImagesActivity.class));
    }
}

