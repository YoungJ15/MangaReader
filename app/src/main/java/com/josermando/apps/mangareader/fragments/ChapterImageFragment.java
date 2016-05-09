package com.josermando.apps.mangareader.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.josermando.apps.mangareader.R;
import com.josermando.apps.mangareader.adapters.RecyclerImageAdapter;
import com.josermando.apps.mangareader.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Josermando Peralta on 4/14/2016.
 */
public class ChapterImageFragment extends Fragment{

    public RecyclerImageAdapter recyclerImageAdapter;
    private RecyclerView recyclerView;
    private List<Image> imageList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_list_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_image);
        String chapterID = getArguments().getString("chapterID");
        new FetchImageTask().execute(chapterID);

        return view;
    }

    private void setUpRecyclerAdapter(List<Image> values){

        recyclerImageAdapter = new RecyclerImageAdapter(getActivity(), values);
        recyclerView.setAdapter(recyclerImageAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public class FetchImageTask extends AsyncTask<String, Void, List<Image>>{

        private final String LOG_TAG = FetchImageTask.class.getSimpleName();

        private List<Image> getImageListFromJSON(String imageJSONString) throws JSONException {
            Image image = null;
            JSONObject IMAGEJSON = new JSONObject(imageJSONString);
            JSONArray JSONImageArray = null;
            JSONArray JSONImageArray2 = null;

            JSONImageArray = IMAGEJSON.getJSONArray("images");
            for(int i =0; i < JSONImageArray.length(); i++){
                JSONImageArray2 = JSONImageArray.getJSONArray(i);
                image = new Image();

                image.setPageNumber(JSONImageArray2.getString(0));
                image.setUrl(JSONImageArray2.getString(1));

                imageList.add(image);
            }
            if(image != null){
                Log.v(LOG_TAG + " Page Number: ", image.getPageNumber());
                Log.v(LOG_TAG + " Image URL: ", image.getUrl());
            }
            Collections.reverse(imageList);
            return imageList;
        }

        @Override
        protected List<Image> doInBackground(String... params) {
            Log.v(LOG_TAG, "Pararms count: " + params.length);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            InputStream inputStream = null;
            //This variable will contain the raw JSON Response
            String ImageJSONString = null;

            try {
                //Contructing the URL for the query and the other constant query parameter
                final String BASE_URL = "https://www.mangaeden.com/api/chapter/" + params[0];

                Uri builtUri = Uri.parse(BASE_URL).buildUpon().build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built Uri and URL: " + url);
                //Creating the Request and opening the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                //Reading the input into a String
                Log.v(LOG_TAG + " Response Code: ", String.valueOf(urlConnection.getResponseCode()));
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                } else {
                    return null;
                }

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                ImageJSONString = buffer.toString();
                Log.v(LOG_TAG, "Chapter JSON String: " + ImageJSONString);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;

            } finally {
                //Closing resources
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getImageListFromJSON(ImageJSONString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Image> images) {
            if(images!= null){
                Log.v(LOG_TAG+" Response Value: ", String.valueOf(images));
                for(Image image: images){
                    setUpRecyclerAdapter(images);
                    recyclerImageAdapter.getItemCount();
                }
                Log.v(LOG_TAG+" Size del MangaList: ", String.valueOf(imageList.size()));
            }
        }
    }
}
