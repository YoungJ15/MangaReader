package com.josermando.apps.mangareader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.josermando.apps.mangareader.R;
import com.josermando.apps.mangareader.model.Manga;

import java.util.List;

/**
 * Created by Josermando Peralta on 4/19/2016.
 */
public class MangaAdapter extends ArrayAdapter<Manga> {


    public MangaAdapter(Context context, List<Manga> mangas) {
        super(context, 0, mangas);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Getting data item for the position
        Manga manga = getItem(position);
        //Recycling view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_manga, parent, false);
        }

        //Lookup view for data population
        ImageView ivMangaImage = (ImageView) convertView.findViewById(R.id.list_item_manga_image);
        TextView tvMangaName = (TextView) convertView.findViewById(R.id.list_item_manga_textview);
        //TextView tvMangaID = (TextView) convertView.findViewById(R.id.list_item_mangaID_textview);

        //Populate data into  the view using the data object
        tvMangaName.setText(manga.getName());
        //tvMangaID.setText(manga.getId());
        //Return the completed view to render the screen
        return convertView;
    }


}
