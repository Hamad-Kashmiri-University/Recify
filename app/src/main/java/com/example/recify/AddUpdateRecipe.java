package com.example.recify;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AddUpdateRecipe extends AppCompatActivity {

    //vars
    ImageView foodImage;
    EditText recipeName, recipeTime, recipeInstructions, recipeIngredients;
    FloatingActionButton doneButton;

    //perms
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    //image picker perms
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;
    //permission list
    private String[] camPerms; //both
    private String[] storagePerms; //exclusively storage

    Uri imageUri;
    private String id, name, time, ingredients, instructions, addedTime, updatedTime;
    private boolean EditMode = false;

    //get database helper class
    private DBHelper dbHelper;

    //ab
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_recipe);

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

        //retrieve data from intent for editing
        Intent intent = getIntent();
        EditMode = intent.getBooleanExtra("EditMode", false);


        //setting data
        if (EditMode){
            actionBar.setTitle("Update Recipe");
            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            imageUri = Uri.parse(intent.getStringExtra("IMAGE"));
            time = intent.getStringExtra("TIME");
            instructions = intent.getStringExtra("INSTRUCTIONS");
            ingredients = intent.getStringExtra("INGREDIENTS");
            addedTime = intent.getStringExtra("TIME_ADDED");
            updatedTime = intent.getStringExtra("TIME_UPDATED");

            recipeName.setText(name);
            recipeTime.setText(time);
            recipeInstructions.setText(instructions);
            recipeIngredients.setText(ingredients);

            //check null image
            if (imageUri.toString().equals("null")){
                foodImage.setImageResource(R.drawable.person);
            }
            else {
                foodImage.setImageURI(imageUri);

            }

        }
        else {
            actionBar.setTitle("Add Recipe");

        }

        //initialisations
        camPerms = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePerms = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        dbHelper = new DBHelper(this);


        //onclick
        foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //image picking 
                imagePick();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input();
            }
        });
    }

    private void input() {
        //getter func
        name = ""+recipeName.getText().toString().trim();
        time = ""+recipeTime.getText().toString().trim();
        ingredients = ""+recipeIngredients.getText().toString().trim();
        instructions = ""+recipeInstructions.getText().toString().trim();

        //check mode edit or not
        if (EditMode){

            //save to sqlite
            String stamp = ""+System.currentTimeMillis();
            dbHelper.updateRecord(
                    ""+id,
                    ""+name,
                    ""+imageUri,
                    ""+time,
                    ""+instructions,
                    ""+ingredients,
                    ""+addedTime,
                    ""+stamp
            );
            Toast.makeText(this, "Recipe has been Updated: "+id, Toast.LENGTH_LONG).show();

        }
        else {

            //save to sqlite
            String stamp = ""+System.currentTimeMillis();
            long id = dbHelper.addRecord(
                    ""+name,
                    ""+imageUri,
                    ""+time,
                    ""+instructions,
                    ""+ingredients,
                    ""+stamp,
                    ""+stamp
            );
            Toast.makeText(this, "Recipe has been added with ID: "+id, Toast.LENGTH_LONG).show();
        }


    }

    private void imagePick() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i==0){
                    if(!validateCameraPermissions()) {
                        requestCameraPermission();
                    }   
                    else {
                        selectFromCamera();
                    }

                }
                else  if (i==1){
                    if (!validatePermissions()){
                        requestPermission();
                    }
                    else {
                        selectFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void selectFromCamera() {
        //intent for camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");
        //get uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(camera, IMAGE_PICK_CAMERA_CODE);
    }

    private void selectFromGallery() {
        //intent for gallery select
        Intent gallery = new Intent(Intent.ACTION_PICK);
        //for images only
        gallery.setType("image/*");
        startActivityForResult(gallery, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean validatePermissions() {
        boolean res = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return res;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, storagePerms, STORAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //procured image
        if(resultCode == RESULT_OK) {
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //crop settings
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(this);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(this);
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                // cropped image ok
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    foodImage.setImageURI(resultUri);
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception error = result.getError();
                    Toast.makeText(this, "Error with getting image"+ error, Toast.LENGTH_SHORT).show();

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean validateCameraPermissions() {
        boolean res = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean res1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return res && res1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, camPerms, CAMERA_REQUEST_CODE);
    }

    //action bar navigation commands
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();    //return by clicking this instead of just back button
        return super.onSupportNavigateUp();
    }

    //permissions result execution
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0) {
                    //true if accepted permissions otherwise not true
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        selectFromCamera();
                    }
                    else {
                        Toast.makeText(this, "permissions required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length>0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        selectFromGallery();
                    }
                    else {
                        Toast.makeText(this, "storage permission required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }
}