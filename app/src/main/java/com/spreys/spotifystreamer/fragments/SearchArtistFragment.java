package com.spreys.spotifystreamer.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spreys.spotifystreamer.MyApplication;
import com.spreys.spotifystreamer.R;
import com.spreys.spotifystreamer.activities.SearchActivity;
import com.spreys.spotifystreamer.activities.TopTracksActivity;
import com.spreys.spotifystreamer.adapters.SearchResultsAdapter;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

/**
 * Created with Android Studio
 *
 * @author vspreys
 *         Date: 21/06/15
 *         Project: Spotify Streamer
 *         Contact by: vlad@spreys.com
 */
public class SearchArtistFragment extends Fragment {

    private MyApplication mApplication;

    @Override
    public void onResume() {
        super.onResume();

        mApplication = (MyApplication)getActivity().getApplication();
        //Repopulate data if available
        if(mApplication.searchResultArtists != null){
            addAdapter();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        EditText searchView = (EditText)view.findViewById(R.id.activity_search_edit_text);
        final Context mContext = getActivity();
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
        return view;
    }

    public void addAdapter(){
        SearchResultsAdapter adapter = new SearchResultsAdapter(getActivity(), mApplication.searchResultArtists);

        final Context mContext = getActivity();
        //Attach the adapter to the list view
        ListView listView = (ListView)getActivity().findViewById(R.id.activity_search_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SearchActivity)getActivity()).onItemSelected(position);
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
