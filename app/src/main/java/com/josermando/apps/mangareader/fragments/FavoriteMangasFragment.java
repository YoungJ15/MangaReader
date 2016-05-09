package com.josermando.apps.mangareader.fragments;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.josermando.apps.mangareader.R;
import com.josermando.apps.mangareader.adapters.RecyclerFavoriteMangaAdapter;
import com.josermando.apps.mangareader.model.Manga;
import com.josermando.apps.mangareader.providers.FavoriteMangaProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josermando Peralta on 4/28/2016.
 */
public class FavoriteMangasFragment extends Fragment {

    public RecyclerFavoriteMangaAdapter recyclerFavoriteMangaAdapter;
    private RecyclerView recyclerView;
    private List<Manga> mangaList = new ArrayList<>();
    private ProgressBar progressBar;

    Manga manga = null;
    String [] projection = new String[]{
            FavoriteMangaProvider.FavoriteMangas.NAME_COL
    };

    Uri favoriteMangasUri = FavoriteMangaProvider.CONTENT_URI;
    ContentResolver contentResolver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResolver = getActivity().getContentResolver();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.favorite_manga_list_fragment, container, false);
        setHasOptionsMenu(true);
        progressBar = (ProgressBar) view.findViewById(R.id.favorite_task_progress);
        progressBar.setProgress(0);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_favorite_manga);

        queryFavoriteMangas();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setUpRecyclerAdapter(List<Manga> values){

        recyclerFavoriteMangaAdapter = new RecyclerFavoriteMangaAdapter(getActivity(), values);
        recyclerView.setAdapter(recyclerFavoriteMangaAdapter);

        GridLayoutManager mGridLayoutManagerVertical = new GridLayoutManager(getActivity(), 3);
        mGridLayoutManagerVertical.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mGridLayoutManagerVertical);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void queryFavoriteMangas(){
        ContentValues values = new ContentValues();

        values.put(FavoriteMangaProvider.FavoriteMangas.ID_COL, "ClienteN");
        values.put(FavoriteMangaProvider.FavoriteMangas.NAME_COL, "999111222");
        values.put(FavoriteMangaProvider.FavoriteMangas.IMG_COL, "nuevo@email.com");

        contentResolver.insert(FavoriteMangaProvider.CONTENT_URI, values);
        Log.v("Insertando valores: ", values.toString());


        //Query
        Cursor cursor = contentResolver.query(favoriteMangasUri, projection, null, null, null);
        if(cursor.moveToFirst()){

        /**    int idCol = cursor.getColumnIndex(FavoriteMangaProvider.FavoriteMangas.ID_COL);
            int nameCol = cursor.getColumnIndex(FavoriteMangaProvider.FavoriteMangas.NAME_COL);
            int imgCol = cursor.getColumnIndex(FavoriteMangaProvider.FavoriteMangas.IMG_COL);
        */
            do{
                manga = new Manga();
                manga.setId(cursor.getString(0));
                manga.setName(cursor.getString(0));
                manga.setImgSrc(cursor.getString(0));

                mangaList.add(manga);
            }
            while(cursor.moveToNext());
        }
        setUpRecyclerAdapter(mangaList);
    }




}
