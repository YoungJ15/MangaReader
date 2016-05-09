package com.josermando.apps.mangareader.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.josermando.apps.mangareader.R;
import com.josermando.apps.mangareader.fragments.MangaChapterFragment;
import com.josermando.apps.mangareader.model.Manga;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Josermando Peralta on 4/19/2016.
 */
public class RecyclerMangaAdapter extends RecyclerView.Adapter<RecyclerMangaAdapter.MyViewHolder>{

    private List<Manga> mangaList;
    private LayoutInflater mInflater;
    Context context;

    public RecyclerMangaAdapter(Context context, List<Manga> mangaList) {
        this.mangaList = mangaList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_manga, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Manga currentObject = mangaList.get(position);
        context = holder.ivMangaImage.getContext();
        holder.setData(currentObject, position, context);
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivMangaImage;
        TextView tvMangaName;
        int position;
        Manga currentManga;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivMangaImage = (ImageView) itemView.findViewById(R.id.list_item_manga_image);
            tvMangaName  = (TextView) itemView.findViewById(R.id.list_item_manga_textview);
            ivMangaImage.setOnClickListener(this);
            tvMangaName.setOnClickListener(this);
        }

        public void setData(Manga currentManga, int position, Context context){

            this.tvMangaName.setText(currentManga.getName());
            downloadImage("https://cdn.mangaeden.com/mangasimg/" ,currentManga.getImgSrc(), context);
            this.position = position;
            this.currentManga = currentManga;

        }
        public void downloadImage(String urlSource, String urlImage , Context context){
            Picasso.with(context).
                    load(urlSource+urlImage).
                    fit().
                    centerCrop().
                    error(R.mipmap.ic_launcher).
                    into(ivMangaImage);
        }

        @Override
        public void onClick(View v) {
            FragmentManager manager = ((Activity) context).getFragmentManager();
            String mangaID = currentManga.getId();
            Log.v("Valor del ID ",mangaID);
            Bundle bundle = new Bundle();
            bundle.putString("mangaID", mangaID);

            MangaChapterFragment mangaChapterFragment = new MangaChapterFragment();
            mangaChapterFragment.setArguments(bundle);
            manager.popBackStack();
            manager.beginTransaction().replace(R.id.site_container, mangaChapterFragment).addToBackStack("Manga").commit();
        }
    }
}
