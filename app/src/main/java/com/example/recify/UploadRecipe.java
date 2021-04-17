package com.example.recify;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UploadRecipe extends AppCompatActivity {

    //vars
    private FloatingActionButton addButton;
    private RecyclerView dataView;

    //Helper
    private DBHelper dbHelper;

    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_recipe);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Uploaded Recipes");

        //hooks
        addButton = findViewById(R.id.addButton);
        dataView = findViewById(R.id.dataView);

        dbHelper = new DBHelper(this);

        loadData();

        //onclick listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to activity for adding a record to sqlite
                Intent intent = new Intent(UploadRecipe.this, AddUpdateRecipe.class);
                intent.putExtra("EditMode", false);//false for new item true for edit
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        ModelAdapter modelAdapter = new ModelAdapter(UploadRecipe.this, dbHelper.getData(DatabaseConstants.C_ADDED + " DESC"));

        dataView.setAdapter(modelAdapter);

        actionBar.setSubtitle(dbHelper.getDataCount() + ": Recipes");
    }

    private void searchData(String query) {
        ModelAdapter modelAdapter = new ModelAdapter(UploadRecipe.this, dbHelper.searchData(query));

        dataView.setAdapter(modelAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        //searchview
        MenuItem menuItem = menu.findItem(R.id.searchBar);
        SearchView SV = (SearchView) menuItem.getActionView();
        SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchData(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchData(s);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}