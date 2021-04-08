package com.example.recify;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class NearbySpots extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_spots);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        int errorCode = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(this);
        if (errorCode!= ConnectionResult.SUCCESS) {
            Dialog error = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, errorCode, errorCode, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            Toast.makeText(NearbySpots.this, "no services", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                error.show();
        }else {
            Toast.makeText(NearbySpots.this, "Google Services Connected", Toast.LENGTH_LONG).show();
        }
    }
}