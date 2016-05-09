package com.josermando.apps.mangareader.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.josermando.apps.mangareader.R;
import com.josermando.apps.mangareader.model.Image;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Josermando Peralta on 4/19/2016.
 */
public class RecyclerImageAdapter extends RecyclerView.Adapter<RecyclerImageAdapter.MyViewHolder> {

    private List<Image> imageList;
    private LayoutInflater mInflater;
    Context context;

    public RecyclerImageAdapter(Context context, List<Image> imageList) {
        //Collections.reverse(imageList);
        this.imageList = imageList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_image, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Image currentObject = imageList.get(position);
        context = holder.ivImage.getContext();
        holder.setData(currentObject, position, context);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        PhotoView ivImage;
        int position;
        Image currentChapter;


        public MyViewHolder(View itemView) {
            super(itemView);
            ivImage = (PhotoView ) itemView.findViewById(R.id.list_item_image);
        }

        public void setData(Image currentImage, int position, Context context){
            downloadImage("https://cdn.mangaeden.com/mangasimg/" ,currentImage.getUrl(), context);
            this.position = position;
            this.currentChapter = currentImage;

        }
        public void downloadImage(String urlSource, String urlImage , Context context){
            Picasso.with(context).
                    load(urlSource+urlImage).
                    error(R.mipmap.ic_launcher).
                    into(ivImage);
        }

    }
}
