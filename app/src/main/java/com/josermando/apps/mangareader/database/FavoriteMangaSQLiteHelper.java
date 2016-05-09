package com.josermando.apps.mangareader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Josermando Peralta on 4/28/2016.
 */
public class FavoriteMangaSQLiteHelper extends SQLiteOpenHelper {
    //SQL Sentence to create the FAVORITEMANGAS table
    final String TABLE_NAME = "FAVORITEMANGAS";
    String sqlCreate = "CREATE TABLE "+TABLE_NAME+" (_ID INTEGER PRIMARY KEY AUTOINCREMENT, MANGAID TEXT, NAME TEXT, IMAGE TEXT)";
    String sqlDelete = "DROP TABLE IF EXISTS "+TABLE_NAME;

    public FavoriteMangaSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlDelete);
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL(sqlCreate);
    }
}
