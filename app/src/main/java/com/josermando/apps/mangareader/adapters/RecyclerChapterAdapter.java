package com.josermando.apps.mangareader.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.josermando.apps.mangareader.R;
import com.josermando.apps.mangareader.fragments.ChapterImageFragment;
import com.josermando.apps.mangareader.model.Chapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by Josermando Peralta on 4/19/2016.
 */
public class RecyclerChapterAdapter extends RecyclerView.Adapter<RecyclerChapterAdapter.MyViewHolder> {

    private List<Chapter> chapterList;
    private LayoutInflater mInflater;
    Context context;

    public RecyclerChapterAdapter(Context context, List<Chapter> chapterList) {
        this.chapterList = chapterList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_chapter, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Chapter currentObject = chapterList.get(position);
        context = holder.tvChapterName.getContext();
        holder.setData(currentObject, position);
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvChapterName;

        int position;
        Chapter currentChapter;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvChapterName  = (TextView) itemView.findViewById(R.id.list_item_chapter_textview);
            tvChapterName.setOnClickListener(this);
        }

        public void setData(Chapter currentChapter, int position){

            this.tvChapterName.setText("Chapter: "+currentChapter.getName());
            this.position = position;
            this.currentChapter = currentChapter;

        }

        @Override
        public void onClick(View v) {
            FragmentManager manager = ((Activity) context).getFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            String chapterID = currentChapter.getId();
            Log.v("Valor del ID ",chapterID);
            Bundle bundle = new Bundle();
            bundle.putString("chapterID", chapterID);

            ChapterImageFragment chapterImageFragment = new ChapterImageFragment();
            chapterImageFragment.setArguments(bundle);

            ft.replace(R.id.site_container, chapterImageFragment).addToBackStack("Chapter");
            ft.commit();
        }
    }
}
