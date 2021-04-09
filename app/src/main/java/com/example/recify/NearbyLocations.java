package com.example.recify;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

// gets the place data
public class NearbyLocations extends AsyncTask<Object, String, String>
{

    //vars
    private String placesData, url;
    private GoogleMap myMap;


    @Override
    protected String doInBackground(Object... objects) {

        myMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        // init new Url obj and read passed url data using method from Url
        Url load = new Url();
        try {
            placesData =  load.urlReader(url);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("fail", "failed ");
        }
        return placesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> placesList = null;
        ParseData parseData = new ParseData();
        placesList = parseData.parse(s);
        DisplayPlaces(placesList);
    }

    // add markers to map

    private void DisplayPlaces(List<HashMap<String, String>> placesList){
        for (int x = 0; x < placesList.size(); x++) {
            MarkerOptions markerOptions = new MarkerOptions();
            //get data from list and marker it
            HashMap<String, String> googlePlaces = placesList.get(x);
            String placeID = googlePlaces.get("name");
            String localArea = googlePlaces.get("vicinity");
            double lat = Double.parseDouble(googlePlaces.get("latitude"));
            double lng = Double.parseDouble(googlePlaces.get("longitude"));
            Log.d("display", "DisplayPlaces: " + lat + lng);
            LatLng latLng = new LatLng(lat, lng);
            Log.d("positions", "onLocationChanged: test" + latLng);
            markerOptions.position(latLng);
            markerOptions.title(placeID + " : " + localArea);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            myMap.addMarker(markerOptions);

            myMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            // amount zoomed by
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 13.0f));

            {
                Log.d("display", "DisplayPlaces: failed" );
            }


        }
    }
}
