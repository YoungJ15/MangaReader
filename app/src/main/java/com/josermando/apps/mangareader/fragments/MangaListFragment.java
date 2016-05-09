package com.josermando.apps.mangareader.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.josermando.apps.mangareader.R;
import com.josermando.apps.mangareader.adapters.RecyclerMangaAdapter;
import com.josermando.apps.mangareader.model.Manga;

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
import java.util.List;


/**
 * Created by Josermando Peralta on 4/14/2016.
 */
public class MangaListFragment extends Fragment {

    public RecyclerMangaAdapter recyclerMangaAdapter;
    private RecyclerView recyclerView;
    private List<Manga> mangaList = new ArrayList<>();
    private ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manga_list_fragment, container, false);
        setHasOptionsMenu(true);
        progressBar = (ProgressBar) view.findViewById(R.id.task_progress);
        progressBar.setProgress(0);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_manga);
        if(mangaList.size() <= 0) {
            new FetchMangaTask().execute("0");
        }
        else{
            setUpRecyclerAdapter(mangaList);
        }
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                final List<Manga> filteredMangaList = filterList(mangaList, query);
                setUpRecyclerAdapter(filteredMangaList);
                recyclerView.scrollToPosition(0);
                return true;
            }
        });
    }

    public List<Manga> filterList(List<Manga> list, String query){
        query = query.toLowerCase();
        final List<Manga> filteredList = new ArrayList<>();
        for(Manga manga: list){
            final String text = manga.getName().toLowerCase();
            if(text.contains(query)){
                filteredList.add(manga);

            }
            else{
                continue;
            }

        }
        return filteredList;
    }

    private void setUpRecyclerAdapter(List<Manga> values){

        recyclerMangaAdapter = new RecyclerMangaAdapter(getActivity(), values);
        recyclerView.setAdapter(recyclerMangaAdapter);

        GridLayoutManager mGridLayoutManagerVertical = new GridLayoutManager(getActivity(), 3);
        mGridLayoutManagerVertical.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mGridLayoutManagerVertical);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public class FetchMangaTask extends AsyncTask<String, Integer, List<Manga>> {

        private final String LOG_TAG = FetchMangaTask.class.getSimpleName();

        @Override
        protected List<Manga> doInBackground(String... params) {
            Log.v(LOG_TAG, "Pararms count: " + params.length);

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            InputStream inputStream ;
            //This variable will contain the raw JSON Response
            String MangaJSONString = null;

            try {
                //Contructing the URL for the query and the other constant query parameter
                final String BASE_URL = "https://www.mangaeden.com/api/list/0";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter("p",params[0]).build();

                URL url = new URL(builtUri.toString().replaceAll("%2F","/"));
                Log.v(LOG_TAG,"Built Uri and URL: "+url);
                //Creating the Request and opening the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                //Reading the input into a String
                Log.v(LOG_TAG + " Response Code: ", String.valueOf(urlConnection.getResponseCode()));
                if(urlConnection.getResponseCode() == 200){
                    inputStream = urlConnection.getInputStream();
                }
                else{
                    return null;
                }

                StringBuilder buffer = new StringBuilder();
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
                MangaJSONString = buffer.toString();
                Log.v(LOG_TAG, "Manga JSON String: " + MangaJSONString);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;

            }finally {
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
                return getMangaInfoFromJSON(MangaJSONString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        private List<Manga> getMangaInfoFromJSON(String mangajsonString) throws JSONException {

            Manga mangaObject = null;
            JSONObject MANGAJSON = new JSONObject(mangajsonString);
            JSONArray JSONMangaArray;
            JSONObject JSONManga;

            JSONMangaArray = MANGAJSON.getJSONArray("manga");
            for(int i=0; i<JSONMangaArray.length();i++) {
                JSONManga = JSONMangaArray.getJSONObject(i);

                mangaObject = new Manga();
                mangaObject.setName(JSONManga.getString("t"));
                mangaObject.setId(JSONManga.getString("i"));
                mangaObject.setImgSrc(JSONManga.getString("im"));

                mangaList.add(mangaObject);

                Log.v(LOG_TAG+" JSONManga Value: "+i, JSONManga.toString());
                Log.v(LOG_TAG+ " ResultList value: ",mangaList.get(i).toString());
            }
            if(mangaObject!= null) {
                Log.v(LOG_TAG + " ResultList Manga: ", mangaObject.getName());
                Log.v(LOG_TAG + " ResultList Manga ID: ", mangaObject.getId());
                Log.v(LOG_TAG + " ResultList Manga Img: ", String.valueOf(mangaObject.getImgSrc()));
            }

            return mangaList;
        }

        @Override
        protected void onPostExecute( List<Manga> strings) {
            if(strings!= null){
                Log.v(LOG_TAG+" Response Value: ", String.valueOf(strings));
                for(Manga manga: strings){
                   setUpRecyclerAdapter(strings);
                    recyclerMangaAdapter.getItemCount();
               }
                Log.v(LOG_TAG+" Size del MangaList: ", String.valueOf(mangaList.size()));
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(progressBar != null){
                progressBar.setProgress(values[0]);
            }

        }
    }
}
