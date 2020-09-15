package com.ivangy.marsroversphotos.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.ivangy.marsroversphotos.Helper;
import com.ivangy.marsroversphotos.JSonParser;
import com.ivangy.marsroversphotos.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GeolocationActivity extends AppCompatActivity {

    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;
    private double currentLat = 0, currentLong = 0;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocation);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        client = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

        SearchView searchPlaces = findViewById(R.id.searchPlaces);
        TextInputEditText txtRadius = findViewById(R.id.txtRadius);

        searchPlaces.setQuery(getApplicationContext().getString(R.string.default_place_search), true);

        searchPlaces.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.replace(" ", "%");
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                        "?location=" + currentLat + "," + currentLong +
                        "&radius=" + (txtRadius.getText().toString().isEmpty() ? getApplicationContext().getString(R.string.default_place_radius) : txtRadius.getText().toString()) +
                        "&keyword=" + query +
                        "&sensor=true" +
                        "&key=" + getResources().getString(R.string.google_map_key);

                Helper.hideKeyboard(GeolocationActivity.this);
                new PlaceTask().execute(url);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(GeolocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location != null) {

                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();

                    supportMapFragment.getMapAsync(googleMap -> {
                        map = googleMap;
                        LatLng latLng = new LatLng(currentLat, currentLong);

                        // Marker Option
                        currentPositionMarker(map);

                        // Zoom map
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    });
                }
            });
        } else {
            ActivityCompat.requestPermissions(GeolocationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        String data = builder.toString();
        reader.close();
        return data;
    }

    private void currentPositionMarker(GoogleMap map) {
        MarkerOptions marker = new MarkerOptions().position(new LatLng(currentLat, currentLong)).title(getApplicationContext().getString(R.string.map_marker_you_are_here));
        map.addMarker(marker);
    }

    private class PlaceTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {

            JSonParser jSonParser = new JSonParser();
            List<HashMap<String, String>> mapList = null;

            JSONObject object;
            try {
                object = new JSONObject(strings[0]);
                mapList = jSonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            map.clear();

            for (int i = 0; i < hashMaps.size(); i++) {
                HashMap<String, String> hashMapsList = hashMaps.get(i);
                double lat = Double.parseDouble(Objects.requireNonNull(hashMapsList.get("lat"))),
                        lng = Double.parseDouble(Objects.requireNonNull(hashMapsList.get("lng")));
                String name = hashMapsList.get("name");

                LatLng latLng = new LatLng(lat, lng);

                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(name);

                map.addMarker(options);
            }
            currentPositionMarker(map);
        }
    }

}