package com.spreys.spotifystreamer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spreys.spotifystreamer.MyApplication;
import com.spreys.spotifystreamer.R;
import com.spreys.spotifystreamer.adapters.SearchResultsAdapter;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class SearchActivity extends AppCompatActivity {

    private MyApplication mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach);

        mApplication = (MyApplication)getApplication();
        //Repopulate data if available
        if(mApplication.searchResultArtists != null){
            addAdapter();
        }

        EditText searchView = (EditText)findViewById(R.id.activity_search_edit_text);
        final Context mContext = this;
        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //Dismiss the keyboard
                    InputMethodManager inputManager = (InputMethodManager)
                            mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(0, 0);

                    //Run the search task in a new thread.
                    new SearchTask(mContext).execute(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    public void addAdapter(){
        SearchResultsAdapter adapter = new SearchResultsAdapter(this, mApplication.searchResultArtists);

        final Context mContext = this;
        //Attach the adapter to the list view
        ListView listView = (ListView)findViewById(R.id.activity_search_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Clear the top tracks cache
                mApplication.topTracks = null;

                //Navigate to the next activity
                Intent intent = new Intent(mContext, TopTracksActivity.class);
                intent.putExtra(TopTracksActivity.EXTRA_ARTIST_ID, mApplication.searchResultArtists.get(position).id);
                intent.putExtra(TopTracksActivity.EXTRA_ARTIST_NAME, mApplication.searchResultArtists.get(position).name);
                startActivity(intent);
            }
        });
    }

    class SearchTask extends AsyncTask<String, Void, List<Artist>> {
        private Context mContext;

        SearchTask(Context context) {
            mContext = context;
        }

        @Override
        protected List<Artist> doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            ArtistsPager pager = spotify.searchArtists(params[0]);

            return pager.artists.items;
        }

        @Override
        protected void onPostExecute(final List<Artist> artists) {
            super.onPostExecute(artists);

            //Keep a copy of list in case the screen orientation changes
            mApplication.searchResultArtists = artists;

            //Check if artists been found
            if(artists.size() == 0) {
                Toast.makeText(mContext,
                        getResources().getString(R.string.err_no_artists_found),
                        Toast.LENGTH_SHORT)
                        .show();
            } else {
                addAdapter();
            }
        }
    }
}
