package com.ivangy.marsroversphotos.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ivangy.marsroversphotos.LoadPhotos;
import com.ivangy.marsroversphotos.R;
import com.ivangy.marsroversphotos.adapter.PhotosAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private Spinner spinRover;
    private SeekBar seekSun;
    private TextView lblProgressSun;
    private TextInputEditText txtSun;
    private ConstraintLayout MainLayout;
    public static ArrayList<String> ListCameraName, ListCameraFullName, ListImageURL;
    public static String queryRover, querySun, RoverStatus, RoverLandingDate, RoverLaunchDate;
    private ProgressBar pgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pgBar = findViewById((R.id.pgBar));
        MainLayout = findViewById(R.id.MainLayout);
        ListCameraName = new ArrayList<>();
        ListCameraFullName = new ArrayList<>();
        ListImageURL = new ArrayList<>();

        spinRover = findViewById(R.id.spinRover);
        seekSun = findViewById(R.id.seekSun);
        lblProgressSun = findViewById(R.id.lblProgressSun);
        txtSun = findViewById(R.id.txtSun);
        txtSun.setText(valueOf(seekSun.getProgress()));

        lblProgressSun.setText("Sun:" + seekSun.getProgress());

        ArrayAdapter adapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.rovers, R.layout.my_spinner);
        spinRover.setAdapter(adapter);

        queryRover = spinRover.getSelectedItem().toString().toLowerCase();
        querySun = valueOf(seekSun.getProgress());

        events();

        if (getSupportLoaderManager().getLoader(0) != null)
            getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        int random = new Random().nextInt(3);
        if (random == 0)
            spinRover.setSelection(0);
        else {
            if (random == 1)
                spinRover.setSelection(1);
            else {
                if (random == 2)
                    spinRover.setSelection(2);
            }
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryRover = "", querySun = "";
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

            for (int i = 0; i < 30; i++) {
                JSONObject items = photos.getJSONObject(i);
                JSONObject camera = items.getJSONObject("camera");
                JSONObject rover = items.getJSONObject("rover");
                ListImageURL.add(items.getString("img_src"));
                ListCameraName.add(camera.getString("name"));
                ListCameraFullName.add(camera.getString("full_name"));
            }
            JSONObject items = photos.getJSONObject(0);
            JSONObject camera = items.getJSONObject("camera");
            JSONObject rover = items.getJSONObject("rover");
            RoverStatus = (rover.getString("status"));
            RoverLandingDate = (rover.getString("landing_date"));
            RoverLaunchDate = (rover.getString("launch_date"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ListImageURL.size() != 0) {
            pgBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), RecyclerPhotosActivity.class));
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "No images available for this Sun...", Toast.LENGTH_SHORT).show();
            pgBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
    }

    public void events() {

        txtSun.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txtSun.setText("");
                return false;
            }
        });

        txtSun.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(txtSun.getText().toString()).isEmpty()) {
                    seekSun.setProgress(Integer.parseInt(txtSun.getText().toString()));
                    //lblProgressSun.setText("Sun: " + txtSun.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        seekSun.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lblProgressSun.setText("Progress: " + seekSun.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txtSun.setText(valueOf(seekSun.getProgress()));
            }
        });

        spinRover.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinRover.getSelectedItemPosition() == 0)
                    MainLayout.setBackgroundResource(R.drawable.curiosity_theme);
                else {
                    if (spinRover.getSelectedItemPosition() == 1)
                        MainLayout.setBackgroundResource(R.drawable.opportunity_theme);
                    else {
                        if (spinRover.getSelectedItemPosition() == 2)
                            MainLayout.setBackgroundResource(R.drawable.spirit_theme);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
            Toast.makeText(getApplicationContext(), "Connection Failure", Toast.LENGTH_LONG);
        }
    }

    public void btnSearch(View v) {
        pgBar.setVisibility(View.VISIBLE);
        querySun = valueOf(seekSun.getProgress());
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null)
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        restartLoader(spinRover.getSelectedItem().toString().toLowerCase(), valueOf(seekSun.getProgress()));
    }

    public void openNasaWeb(View view) {
        startActivity(new Intent(getApplicationContext(), NasaWebsiteActivity.class));
    }

}

