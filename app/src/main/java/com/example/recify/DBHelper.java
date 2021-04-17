package com.example.recify;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//helper class which defined create read update and delte methods
public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(@Nullable Context context) {
        super(context, DatabaseConstants.DB_NAME, null, DatabaseConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //create table
        db.execSQL(DatabaseConstants.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //upgrade db
        //drop old table if it already is there
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_NAME);
        onCreate(db);
    }

    public long addRecord(String name, String image, String time, String instructions, String ingredients, String added, String updated) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //add data
        values.put(DatabaseConstants.C_NAME, name);
        values.put(DatabaseConstants.C_IMAGE, image);
        values.put(DatabaseConstants.C_TIME, time);
        values.put(DatabaseConstants.C_INSTRUCTIONS, instructions);
        values.put(DatabaseConstants.C_INGREDIENTS, ingredients);
        values.put(DatabaseConstants.C_ADDED, added);
        values.put(DatabaseConstants.C_UPDATED, updated);

        // add row (returns id of saved)
        long id = db.insert(DatabaseConstants.TABLE_NAME, null, values);

        //connection end
        db.close();

        //id returned
        return id;

    }


}
