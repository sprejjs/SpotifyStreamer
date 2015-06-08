package com.spreys.spotifystreamer.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

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
 *         Date: 9/06/15
 *         Project: Spotify Streamer
 *         Contact by: vlad@spreys.com
 */
public class TopTracksActivity extends AppCompatActivity {
    public final static String EXTRA_ARTIST_ID = "extra_artist_id";
    public final static String EXTRA_ARTIST_NAME = "extra_artist_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);

        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setTitle(R.string.top_tracks_title);
        bar.setSubtitle(getIntent().getStringExtra(EXTRA_ARTIST_NAME));

        new GetTopTracksTask(this).execute(getIntent().getStringExtra(EXTRA_ARTIST_ID));
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

            TopTracksAdapter adapter = new TopTracksAdapter(mContext, tracks);

            //Attach the adapter to the list view
            ListView listView = (ListView)findViewById(R.id.activity_top_tracks_list_view);
            listView.setAdapter(adapter);
        }
    }
}
