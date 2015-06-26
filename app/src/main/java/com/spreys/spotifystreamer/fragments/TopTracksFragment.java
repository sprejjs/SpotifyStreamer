package com.spreys.spotifystreamer.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.spreys.spotifystreamer.MyApplication;
import com.spreys.spotifystreamer.R;
import com.spreys.spotifystreamer.adapters.TopTracksAdapter;

import java.util.HashMap;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

/**
 * Created with Android Studio
 *
 * @author vspreys
 *         Date: 27/06/15
 *         Project: Spotify Streamer
 *         Contact by: vlad@spreys.com
 */
public class TopTracksFragment extends Fragment {
    public static final String KEY_ARTIST_ID = "key_artist_id";
    private MyApplication mApplication;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_tracks_fragment, container, false);

        mApplication = (MyApplication)getActivity().getApplication();

        if(mApplication.topTracks != null){
            populateAdapter();
        } else {
            new GetTopTracksTask(getActivity()).execute(getArguments().getString(KEY_ARTIST_ID));
        }

        return view;
    }

    private void populateAdapter() {
        TopTracksAdapter adapter = new TopTracksAdapter(getActivity(), mApplication.topTracks);

        //Attach the adapter to the list view
        ListView listView = (ListView)getActivity().findViewById(R.id.activity_top_tracks_list_view);
        listView.setAdapter(adapter);
    }

    class GetTopTracksTask extends AsyncTask<String, Void, List<Track>> {
        private Context mContext;

        GetTopTracksTask(Context context) {
            mContext = context;
        }

        @Override
        protected List<Track> doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            HashMap<String, Object> options = new HashMap<>();
            options.put("country", "NZ");

            Tracks tracks = spotify.getArtistTopTrack(params[0], options);

            return tracks.tracks;
        }

        @Override
        protected void onPostExecute(final List<Track> tracks) {
            super.onPostExecute(tracks);

            mApplication.topTracks = tracks;
            populateAdapter();
        }
    }
}
