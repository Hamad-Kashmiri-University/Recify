package com.example.recify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {

    //vars
    MaterialCardView uploadCard, myRecipesCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //hooks
        uploadCard = findViewById(R.id.cardOne);
        myRecipesCard = findViewById(R.id.cardTwo);

        //navigation
        uploadCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadRecipe.class);
                startActivity(intent);
            }
        });

        myRecipesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyRecipes.class);
                startActivity(intent);
            }
        });

    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        final Loader loader = new Loader(Dashboard.this);
        loader.loadStart();
        //Handler handler = new Handler();
       // handler.postDelayed(new Runnable() {
        //    @Override
        //    public void run() {

        //    }
       // }, 3000);
        //userAuth.signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();

    }
}