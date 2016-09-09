package com.josermando.apps.mangareader.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.josermando.apps.mangareader.R;
import com.josermando.apps.mangareader.adapters.RecyclerChapterAdapter;
import com.josermando.apps.mangareader.model.Chapter;
import com.squareup.picasso.Picasso;

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
public class MangaChapterFragment extends Fragment {
    private RecyclerChapterAdapter recyclerChapterAdapter;
    private RecyclerView recyclerView;
    private ImageView imageViewChapter;
    private List<Chapter> chapterList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chapter_list_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_chapter);
        imageViewChapter = (ImageView) view.findViewById(R.id.imageViewChapter);

        Collections.reverse(chapterList);
        String mangaID = getArguments().getString("mangaID");
        if (chapterList.size() <= 0) {
            new FetchChapterTask().execute(mangaID);
        } else {
            setUpRecyclerView(chapterList);
        }
        return view;
    }

    public void setUpRecyclerView(List<Chapter> values) {
        recyclerChapterAdapter = new RecyclerChapterAdapter(getActivity(), values);
        recyclerView.setAdapter(recyclerChapterAdapter);
        downloadImage("https://cdn.mangaeden.com/mangasimg/",values.get(0).getImgUrl() , getActivity().getApplicationContext());

        //Layout Manager
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getActivity());
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void downloadImage(String urlSource, String urlImage, Context context) {
        Picasso.with(context).
                load(urlSource + urlImage).
                error(R.mipmap.ic_launcher).
                fit().
                into(imageViewChapter);
    }

    public class FetchChapterTask extends AsyncTask<String, Void, List<Chapter>> {

        private final String LOG_TAG = FetchChapterTask.class.getSimpleName();

        private List<Chapter> getChapterListFromJSON(String chapterjsonString) throws JSONException {
            Chapter chapter = null;
            JSONObject CHAPTERJSON = new JSONObject(chapterjsonString);
            JSONArray JSONChapterArray = null;
            JSONArray JSONChapterArray2 = null;

            JSONChapterArray = CHAPTERJSON.getJSONArray("chapters");

            String image = CHAPTERJSON.getString("image");
            for (int i = 0; i < JSONChapterArray.length(); i++) {
                JSONChapterArray2 = JSONChapterArray.getJSONArray(i);

                chapter = new Chapter();
                chapter.setName(JSONChapterArray2.getString(2));
                chapter.setId(JSONChapterArray2.getString(3));

                chapterList.add(chapter);
            }
            if (chapter != null) {
                Log.v(LOG_TAG + " Chapter Name: ", chapter.getName());
                Log.v(LOG_TAG + " Chapter ID: ", chapter.getId());
                chapter.setImgUrl(image);
                Log.v(LOG_TAG + " Manga Image: ", chapter.getImgUrl());
            }

            Collections.reverse(chapterList);
            return chapterList;
        }

        @Override
        protected List<Chapter> doInBackground(String... params) {
            Log.v(LOG_TAG, "Pararms count: " + params.length);

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            InputStream inputStream = null;
            //This variable will contain the raw JSON Response
            String ChapterJSONString = null;

            try {
                //Contructing the URL for the query and the other constant query parameter
                final String BASE_URL = "https://www.mangaeden.com/api/manga/" + params[0];

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
                ChapterJSONString = buffer.toString();
                Log.v(LOG_TAG, "Chapter JSON String: " + ChapterJSONString);
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
                return getChapterListFromJSON(ChapterJSONString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Chapter> strings) {
            if (strings != null) {
                Log.v(LOG_TAG + " Response Value: ", String.valueOf(strings));

                for (Chapter chapter : strings) {
                    setUpRecyclerView(strings);
                    recyclerChapterAdapter.getItemCount();
                }
                Log.v(LOG_TAG + " Size del MangaList: ", String.valueOf(chapterList.size()));
            }
        }

    }
}

