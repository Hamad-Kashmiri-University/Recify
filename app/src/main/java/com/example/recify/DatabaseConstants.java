package com.example.recify;

public class DatabaseConstants {

    //database name
    public static final String DB_NAME = "RecifyDB";
    //version
    public static final int DB_VERSION = 1;
    //name of model
    public static final String TABLE_NAME = "recipes";
    //fields
    public static final String C_ID = "ID";
    public static final String C_NAME = "NAME";
    public static final String C_IMAGE = "IMAGE";
    public static final String C_TIME = "TIME";
    public static final String C_INSTRUCTIONS = "INSTRUCTIONS";
    public static final String C_INGREDIENTS = "INGREDIENTS";
    public static final String C_ADDED = "ADD_TIME";
    public static final String C_UPDATED = "UPDATED_TIME";

    //query to create
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_NAME + " TEXT, "
            + C_IMAGE + " TEXT, "
            + C_TIME + " TEXT, "
            + C_INSTRUCTIONS + " TEXT, "
            + C_INGREDIENTS + " TEXT, "
            + C_ADDED + " TEXT, "
            + C_UPDATED +  " TEXT);";
}


