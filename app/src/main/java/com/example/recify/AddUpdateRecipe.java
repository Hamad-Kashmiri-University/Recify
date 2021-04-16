package com.example.recify;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddUpdateRecipe extends AppCompatActivity {

    //vars
    ImageView foodImage;
    EditText recipeName, recipeTime, recipeInstructions, recipeIngredients;
    FloatingActionButton doneButton;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_recipe);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        actionBar = getSupportActionBar();
        actionBar.setTitle("AddRecipe");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        //hooks
        foodImage = findViewById(R.id.foodImage);
        recipeName = findViewById(R.id.recipeName);
        recipeTime = findViewById(R.id.recipeTime);
        recipeInstructions = findViewById(R.id.recipeInstructions);
        recipeIngredients = findViewById(R.id.recipeIngredients);
        doneButton = findViewById(R.id.doneButton);

        //onclick
        foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    //action bar navigation commands
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();    //return by clicking this instead of just back button
        return super.onSupportNavigateUp();
    }
}