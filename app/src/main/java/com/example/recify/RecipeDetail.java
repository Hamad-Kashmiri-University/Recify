package com.example.recify;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class RecipeDetail extends AppCompatActivity {

    //vars
    private ImageView foodImage;
    private TextView recipeTime, recipeInstructions, recipeIngredients, recipeName, dateAdded, dateUpdated;

    private ActionBar actionBar;

    private String recipeId;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);


        actionBar= getSupportActionBar();
        actionBar.setTitle("Recipe Details");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        recipeId = intent.getStringExtra("RECIPE_ID");

        dbHelper = new DBHelper(this);

        //hooks
        foodImage = findViewById(R.id.foodImage);
        recipeName = findViewById(R.id.recipeName);
        recipeTime = findViewById(R.id.recipeTime);
        recipeIngredients = findViewById(R.id.recipeIngredients);
        recipeInstructions = findViewById(R.id.recipeInstructions);
        dateAdded = findViewById(R.id.recipeDateAdded);
        dateUpdated = findViewById(R.id.recipeDateUpdated);
        
        showRecipeDetail();

    }

    private void showRecipeDetail() {
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME + " WHERE " + DatabaseConstants.C_ID + " =\"" + recipeId + "\"";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //check db for recipe
        if (cursor.moveToFirst()){
            do {
                String id = ""+cursor.getInt(cursor.getColumnIndex(DatabaseConstants.C_ID));
                String name = ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_NAME));
                String image = ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_IMAGE));
                String time = ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_TIME));
                String instructions = ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_INSTRUCTIONS));
                String ingredients = ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_INGREDIENTS));
                String added = ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_ADDED));
                String updated = ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_UPDATED));

                //timestamp beautification
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.setTimeInMillis(Long.parseLong(added));
                String timeAdded = ""+DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar);

                Calendar calendar1 = Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(updated));
                String timeUpdate = ""+DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar1);

                //set
                if (name!=null){
                    Log.d("namecheck", "showRecipeDetail: "+ name);
                } else {
                    Log.d("namecheck", "showRecipeDetail: ", null);
                }
                recipeName.setText(name);
                recipeTime.setText(time);
                recipeInstructions.setText(instructions);
                recipeIngredients.setText(ingredients);
                dateAdded.setText(timeAdded);
                dateUpdated.setText(timeUpdate);
                if(image.equals("null")){
                    //iuf no image
                    foodImage.setImageResource(R.drawable.person);

                }
                else{
                    foodImage.setImageURI(Uri.parse(image));
                }


            }while (cursor.moveToNext());
        }

        db.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}