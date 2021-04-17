package com.example.recify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UploadRecipe extends AppCompatActivity {

    //vars
    private FloatingActionButton addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);

        //hooks
        addButton = findViewById(R.id.addButton);

        //onclicklisterner
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to activity for adding a record to swlite
                startActivity(new Intent(UploadRecipe.this, AddUpdateRecipe.class));

            }
        });
    }
}