package com.ivangy.marsroversphotos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSonParser {

    private HashMap<String, String> parseJSonObject(JSONObject object) {
        HashMap<String, String> dataList = new HashMap<>();

        try {
            String name = object.getString("name");
            JSONObject objectLocation = object.getJSONObject("geometry")
                    .getJSONObject("location");
            String latitude = objectLocation.getString("lat"),
                    longitude = objectLocation.getString("lng");
            dataList.put("name", name);
            dataList.put("lat", latitude);
            dataList.put("lng", longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private List<HashMap<String, String>> parseJSonArray(JSONArray jsonArray) {
        List<HashMap<String, String>> dataList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                dataList.add(parseJSonObject((JSONObject) jsonArray.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }

    public List<HashMap<String, String>> parseResult(JSONObject object) {
        JSONArray jsonArray = null;
        try {
            jsonArray = object.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parseJSonArray(jsonArray);
    }

}
