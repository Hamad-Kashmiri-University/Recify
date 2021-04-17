package com.example.recify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

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
    //add row
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

    //update row
    public void updateRecord(String id, String name, String image, String time, String instructions, String ingredients, String added, String updated) {
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
        db.update(DatabaseConstants.TABLE_NAME, values, DatabaseConstants.C_ID + " = ?", new String[]{id});

        //connection end
        db.close();

    }


    //data list
    public ArrayList<ModelData> getData(String orderBy){
        ArrayList<ModelData> dataList = new ArrayList<>();

        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME;
       //String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME + " ORDER_BY " + orderBy;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //loop to append data returned to list
        if (cursor.moveToFirst()) {
            do {
                ModelData modelData = new ModelData(
                        ""+cursor.getInt(cursor.getColumnIndex(DatabaseConstants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_TIME)),
                        ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_INSTRUCTIONS)),
                        ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_INGREDIENTS)),
                        ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_ADDED)),
                        ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_UPDATED)));

                dataList.add(modelData);
            }while (cursor.moveToNext());
        }
        db.close();
        return dataList;
    }

    //data search
    public ArrayList<ModelData> searchData(String query){
        ArrayList<ModelData> dataList = new ArrayList<>();

        String searchQuery = "SELECT * FROM " + DatabaseConstants.TABLE_NAME + " WHERE " + DatabaseConstants.C_NAME + " LIKE '%" + query + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(searchQuery, null);

        //loop to append data returned to list
        if (cursor.moveToFirst()) {
            do {
                ModelData modelData = new ModelData(
                        ""+cursor.getInt(cursor.getColumnIndex(DatabaseConstants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_TIME)),
                        ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_INSTRUCTIONS)),
                        ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_INGREDIENTS)),
                        ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_ADDED)),
                        ""+cursor.getString(cursor.getColumnIndex(DatabaseConstants.C_UPDATED)));

                dataList.add(modelData);
            }while (cursor.moveToNext());
        }
        db.close();
        return dataList;
    }

    public int getDataCount(){
        String query = "SELECT * FROM " + DatabaseConstants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //delete by id
    public void deleteByID(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DatabaseConstants.TABLE_NAME, DatabaseConstants.C_ID + " = ?", new String[]{id});
        db.close();
    }

    //delete all
    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ DatabaseConstants.TABLE_NAME);
        db.close();
    }
}
