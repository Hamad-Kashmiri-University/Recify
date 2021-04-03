package com.example.recify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {

    //vars
    public FirebaseAuth userAuth;
    MaterialCardView uploadCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        userAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser loggedInUser = userAuth.getCurrentUser();
        if(loggedInUser!=null){

        }else{
           // startActivity(new Intent(getApplicationContext(), Login.class));
           // finish();
        }
    }

    public void logout(View view) {
        final Loader loader = new Loader(Dashboard.this);
        loader.loadStart();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);
        //userAuth.signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();

    }
}