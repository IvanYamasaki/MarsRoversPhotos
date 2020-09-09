package com.ivangy.marsroversphotos;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    private static final String API_KEY = "Y2IXSPEHAv5Yfp4hCPPgXHvzLOBtkDwISOXFQ64R";
    private static final String URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/";
    private static final String PHOTO_PARAM = "/photos?";
    private static final String SUN = "sol";
    private static final String KEY = "api_key";

    static String SearchPhoto(String queryRover, String querySun) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JSONString = null;

        try {
            Uri builtURI = Uri.parse(URL + queryRover + PHOTO_PARAM).buildUpon()
                    .appendQueryParameter(SUN, querySun)
                    .appendQueryParameter(KEY, API_KEY)
                    .build();
            Log.d("URI", builtURI.toString());

            java.net.URL requestURL = new URL(builtURI.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            if (builder.length() == 0)
                return null;
            JSONString = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return JSONString;
    }
}
