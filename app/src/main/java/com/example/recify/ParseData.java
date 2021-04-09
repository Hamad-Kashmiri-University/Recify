package com.example.recify;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// get latlng data using hashmap and parse it so its usable and then return it
public class ParseData {
    private HashMap<String, String> placeGetterSingle(JSONObject placesJSON) {
        HashMap<String, String> mapForPlaces = new HashMap<>();
        String placeId = "-NA-";
        String localArea = "-NA-";
        String lat = "";
        String lng = "";
        String ref = "";

        try {
           if (!placesJSON.isNull("name")){
               placeId = placesJSON.getString("name");
           }
            if (!placesJSON.isNull("Local Area")){
                localArea = placesJSON.getString("vicinity");
            }
            lat = placesJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            lng = placesJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
            ref = placesJSON.getString("ref");

            mapForPlaces.put("name", placeId);
            mapForPlaces.put("vicinity", localArea);
            mapForPlaces.put("latitude", lat);
            mapForPlaces.put("longitude", lng);
            mapForPlaces.put("reference", ref);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mapForPlaces;
        // each hashmap stores loc for a single place, below method makes a list of hashmaps for nearby
    }

    private List<HashMap<String, String>> placeGetterList(JSONArray jsonArray) {
        int length = jsonArray.length();
        Log.d("length", "dsdf"  + length);
        List<HashMap<String, String>> placesList = new ArrayList<>();

        HashMap<String, String> placeMap =  null;
        //loop throug hashmap data
        for(int x = 0; x<length; x++){
            try {
                //call single place getter and add it to the lsit
                placeMap = placeGetterSingle( (JSONObject) jsonArray.get(x) );
                placesList.add(placeMap);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    // parse data and pass to placegetterlist as jsonarray
    public List<HashMap<String, String>> parse(String jsonData){
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return placeGetterList(jsonArray);

    }
}
