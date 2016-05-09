package com.josermando.apps.mangareader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.josermando.apps.mangareader.R;
import com.josermando.apps.mangareader.model.Chapter;

import java.util.List;

/**
 * Created by Josermando Peralta on 4/19/2016.
 */
public class ChapterAdapter extends ArrayAdapter<Chapter>{

    public ChapterAdapter(Context context, List<Chapter> chapters) {
        super(context, 0, chapters);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Getting data item for the position
        Chapter chapter = getItem(position);
        //Recycling view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_chapter, parent, false);
        }



        return super.getView(position, convertView, parent);
    }
}
