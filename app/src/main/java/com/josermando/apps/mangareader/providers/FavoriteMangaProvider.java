package com.josermando.apps.mangareader.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.josermando.apps.mangareader.database.FavoriteMangaSQLiteHelper;

/**
 * Created by Josermando Peralta on 4/28/2016.
 */
public class FavoriteMangaProvider extends ContentProvider {

    //CONTENT_URI Definition
    private static final String URI = "content://com.josermando.apps.mangareader.contentproviders/favoritemangas";
    public static final Uri CONTENT_URI = Uri.parse(URI);

    private static final UriMatcher uriMatcher;
    private static final int MANGAS = 1;
    private static final int MANGA_ID = 2;

    //Inner class to define the column constants
    public static final class FavoriteMangas implements BaseColumns{
        private FavoriteMangas(){}

        //Columns Names
        public static final String ID_COL = "MANGAID";
        public static final String NAME_COL = "NAME";
        public static final String IMG_COL = "IMAGE";

    }

    //Database
    private FavoriteMangaSQLiteHelper favoriteMangaSQLiteHelper;
    private static final String DB_NAME = "DBFavoriteMangas";
    private static final int DB_VERSION = 2;
    private static final String FAVORITEMANGAS_TABLE = "FAVORITEMANGAS";

    //UriMatcher Initialization
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.josermando.apps.mangareader","favoritemangas",MANGAS);
        uriMatcher.addURI("com.josermando.apps.mangareader","favoritemangas/#",MANGA_ID);
    }
    @Override
    public boolean onCreate() {
        favoriteMangaSQLiteHelper = new FavoriteMangaSQLiteHelper(getContext(),DB_NAME,null, DB_VERSION);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String where = selection;
        if(uriMatcher.match(uri) == MANGA_ID){
            where = "_ID="+ uri.getLastPathSegment();
        }

        SQLiteDatabase db = favoriteMangaSQLiteHelper.getWritableDatabase();

        Cursor cursor = db.query(FAVORITEMANGAS_TABLE, projection, where, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match){
            case MANGAS:
                return "vnd.android.cursor.dir/vnd.peralta.favoritemanga";
            case MANGA_ID:
                return "vnd.android.cursor.item/vnd.peralta.favoritemanga";
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long regId = 1;
        SQLiteDatabase db = favoriteMangaSQLiteHelper.getWritableDatabase();
        regId = db.insert(FAVORITEMANGAS_TABLE, null, values);

        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, regId);

        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int counter;

        String where = selection;
        if(uriMatcher.match(uri) == MANGA_ID){
            where = "_ID="+ uri.getLastPathSegment();
        }
        SQLiteDatabase db = favoriteMangaSQLiteHelper.getWritableDatabase();
        counter = db.delete(FAVORITEMANGAS_TABLE, where, selectionArgs);

        return counter;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int counter;

        String where = selection;
        if(uriMatcher.match(uri) == MANGA_ID){
            where = "_ID="+ uri.getLastPathSegment();
        }
        SQLiteDatabase db = favoriteMangaSQLiteHelper.getWritableDatabase();
        counter = db.update(FAVORITEMANGAS_TABLE, values, where, selectionArgs);

        return counter;
    }
}
