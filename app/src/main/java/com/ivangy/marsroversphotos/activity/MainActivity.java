package com.ivangy.marsroversphotos.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.ivangy.marsroversphotos.Helper;
import com.ivangy.marsroversphotos.LoadPhotos;
import com.ivangy.marsroversphotos.R;
import com.ivangy.marsroversphotos.model.FavoritedPhotos;
import com.ivangy.marsroversphotos.model.Photo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
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
    public static FavoritedPhotos favoritedPhotos;
    public static String rover;
    public static int sun;
    private EditText txtSun;
    private int[] backImages = new int[]{R.drawable.curiosity_theme,
            R.drawable.opportunity_theme,
            R.drawable.spirit_theme};
    SharedPreferences sharedPreferences;
    private ImageButton btnViewMode;

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();

            assert children != null;
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile())
            return dir.delete();
        else
            return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnViewMode = findViewById(R.id.imgViewMode);
        sharedPreferences = getSharedPreferences("DarkMode", MODE_PRIVATE);
        setDrawableDark(isDarkEnable());

        pgBar = findViewById((R.id.pgBar));
        MainLayout = findViewById(R.id.MainLayout);
        spinRover = findViewById(R.id.spinRover);
        txtSun = findViewById(R.id.txtSun);

        spinRover.setAdapter(ArrayAdapter.createFromResource(MainActivity.this, R.array.rovers, R.layout.my_spinner));

        txtSun.setText(String.valueOf(sharedPreferences.getInt("querySun", 0)));
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

    @Override
    protected void onResume() {
        super.onResume();
        favoritedPhotos = new FavoritedPhotos(this);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void toggleViewMode(View v) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isDarkEnable", !isDarkEnable());
        editor.apply();
        setDrawableDark(isDarkEnable());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setDrawableDark(boolean darkEnable) {
        if (darkEnable) {
            btnViewMode.setImageDrawable(getDrawable(R.drawable.ic_baseline_brightness_5_24));
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            btnViewMode.setImageDrawable(getDrawable(R.drawable.ic_baseline_brightness_3_24));
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
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

    public boolean isDarkEnable() {
        return sharedPreferences.getBoolean("isDarkEnable", false);
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

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        Log.d("Array:", data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray photos = jsonObject.getJSONArray("photos");

            for (int i = 0; i < photos.length(); i++) {
                JSONObject items = photos.getJSONObject(i);
                JSONObject camera = items.getJSONObject("camera");
                JSONObject rover = items.getJSONObject("rover");
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
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("querySun", sun);
            editor.apply();
            toast(this, "Loading...");
            startActivity(new Intent(this, RecyclerPhotosActivity.class));
            finish();
        } else {
            toast(this, "No images available for this Sun...");
        }
        pgBar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            File dir = getCacheDir();
            deleteDir(dir);
        } catch (Exception ignored) {
        }
    }
}

