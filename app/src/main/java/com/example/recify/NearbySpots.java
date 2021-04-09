package com.example.recify;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class NearbySpots extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    boolean permissions;
    GoogleMap myMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private int GPS_REQUEST_CODE = 9001;
    private Location prevLoc;
    private Marker userMarker;
    private static final int REQUEST_USER_LOCATION_CODE = 99;
    private double lat, lng;
    private int radius = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_spots);
        permissionValidate();

        if (Build.VERSION.SDK_INT >- Build.VERSION_CODES.M){
            checkUserLoc();
        }

        if (permissions) {
            if(isGPS()) {
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                supportMapFragment.getMapAsync(this);
            }
        }
    }
// below 2 funcs for peremission checking
    private boolean isGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(enabled) {
            return true;
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("GPS permissions").setMessage("GPS is required").setPositiveButton("yes", ((dialogInterface, i) -> {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, GPS_REQUEST_CODE);
            })).setCancelable(false).show();
        }
        return false;
    }

    private void permissionValidate() {
        //dexter helps to shorten code
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(NearbySpots.this, "Permission granted", Toast.LENGTH_SHORT).show();
                permissions = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(NearbySpots.this, "You require permissions to access", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    // swtich clause for map buttons
    public void onClick(View v){

        String restaurant = "restaurant", grocery = "supermarket";
        Object dataT[] = new Object[2];
        NearbyLocations nearbyLocations = new NearbyLocations();

        switch(v.getId())
        {
            case R.id.restarauntsIcon:
                myMap.clear();
                String url = getUrl(lat,lng, restaurant);
                dataT[0] = myMap;
                dataT[1] = url;
                //nearbyLocations.execute(dataT);
                Toast.makeText(this, "Searching for restaurants", Toast.LENGTH_LONG);
                Toast.makeText(this, "Displaying restaurants", Toast.LENGTH_LONG);
                displayNearby();
                break;

            case R.id.groceryIcon:
                myMap.clear();
                String groceryurl = getUrl(lat,lng, grocery);
                dataT[0] = myMap;
                dataT[1] = groceryurl;
                //nearbyLocations.execute(dataT);
                displayGroceryNearby();
                Toast.makeText(this, "Searching for Grocery Stores", Toast.LENGTH_LONG);
                Toast.makeText(this, "Displaying Grocery Stores", Toast.LENGTH_LONG);
                break;
        }
    }

    private void displayGroceryNearby() {
        MarkerOptions markerOptions = new MarkerOptions();
        MarkerOptions markerOptions2 = new MarkerOptions();
        MarkerOptions markerOptions3 = new MarkerOptions();
        MarkerOptions markerOptions4 = new MarkerOptions();
        //get data from list and marker it

        LatLng latLng = new LatLng(51.52029742227154, -0.5996742334139364);
        LatLng latLng2 = new LatLng(51.518905020976106, -0.5955814430144937);
        LatLng latLng3 = new LatLng(51.52281155688339, -0.6097257435193473);
        LatLng latLng4 = new LatLng(51.52413431087809, -0.6159071242457069);


        markerOptions.position(latLng);
        markerOptions.title("Polski Sklep");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

        markerOptions2.position(latLng2);
        markerOptions2.title("S and S Supermarket");
        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

        markerOptions3.position(latLng3);
        markerOptions3.title("Kays super market");
        markerOptions3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

        markerOptions4.position(latLng4);
        markerOptions4.title("Sainsbury's");
        markerOptions4.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));


        myMap.addMarker(markerOptions);
        myMap.addMarker(markerOptions2);
        myMap.addMarker(markerOptions3);
        myMap.addMarker(markerOptions4);

        //myMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // amount zoomed by
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.5231433,-0.5975657), 14.0f));
    }

    private void displayNearby() {
        MarkerOptions markerOptions = new MarkerOptions();
        MarkerOptions markerOptions2 = new MarkerOptions();
        MarkerOptions markerOptions3 = new MarkerOptions();
        MarkerOptions markerOptions4 = new MarkerOptions();
        MarkerOptions markerOptions5 = new MarkerOptions();
        MarkerOptions markerOptions6 = new MarkerOptions();
        //get data from list and marker it

        LatLng latLng = new LatLng(51.5132619667169, -0.6056468404658227);
        LatLng latLng2 = new LatLng(51.5200196229926, -0.6003182871636479);
        LatLng latLng3 = new LatLng(51.523600282149076, -0.6149197327456638);
        LatLng latLng4 = new LatLng(51.51294147373268, -0.6132369406953729);
        LatLng latLng5 = new LatLng(51.50997449264537, -0.59650746210162023);
        LatLng latLng6 = new LatLng(51.51445917228655, -0.5930742347226688);

        markerOptions.position(latLng);
        markerOptions.title("Kashmiri Karahi");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        markerOptions2.position(latLng2);
        markerOptions2.title("K2 kebab and Grill");
        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        markerOptions3.position(latLng3);
        markerOptions3.title("Grill Street");
        markerOptions3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        markerOptions4.position(latLng4);
        markerOptions4.title("Paradise Grill Slough");
        markerOptions4.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        markerOptions5.position(latLng5);
        markerOptions5.title("Pizza Express");
        markerOptions5.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        markerOptions6.position(latLng6);
        markerOptions6.title("Farmers and Fishermen");
        markerOptions6.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        myMap.addMarker(markerOptions);
        myMap.addMarker(markerOptions2);
        myMap.addMarker(markerOptions3);
        myMap.addMarker(markerOptions4);
        myMap.addMarker(markerOptions5);
        myMap.addMarker(markerOptions6);

        //myMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // amount zoomed by
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.5231433,-0.5975657), 14.0f));
    }

    // gets strings for urls for buttons on map
    private String getUrl(double lat, double lng, String placesNearby) {
        // urls from google places api web serivcew
        StringBuilder googleURL =  new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + lat + "," + lng);
        googleURL.append("&radius=" + radius);
        googleURL.append("&type=" + placesNearby);
        googleURL.append("&key=" + "AIzaSyDXsphCVG6GbkzbhBO0y7bWzhGbJi0xwHU");
        Log.d("url for searches", "getUrl: " + googleURL.toString());
        return googleURL.toString();
    }

// initialiser method
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleClient();
            myMap.setMyLocationEnabled(true);
        }
        }

        public boolean checkUserLoc() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_USER_LOCATION_CODE);
                }
                else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_USER_LOCATION_CODE);
                }
                return false;
            }
        else {
            return true;
        }
        }
// builds apiclient if permission is granted
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_USER_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if (googleApiClient == null){
                            buildGoogleClient();
                        }
                        myMap.setMyLocationEnabled(true);
                    }
                }
                else {
                    Toast.makeText(this, "Permission was not granted", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    protected synchronized void buildGoogleClient() {

        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

        googleApiClient.connect();

        }

//google client methods
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,  this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GPS_REQUEST_CODE){
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(enabled){
               Toast.makeText(this, "gps enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "gps NOT enabled", Toast.LENGTH_SHORT).show();
            }

    }
}

// gets marker for current user
    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat = location.getLatitude();
        lng =location.getLongitude();
        prevLoc = location;

        if (userMarker != null)
        {
            userMarker.remove();
        }
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("positions", "onLocationChanged: test" + latLng);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Your Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        userMarker = myMap.addMarker(markerOptions);

        myMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // amount zoomed by
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13.0f));

        if (googleApiClient!= null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }
}