package com.spreys.spotifystreamer.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.spreys.spotifystreamer.MyApplication;
import com.spreys.spotifystreamer.R;
import com.spreys.spotifystreamer.fragments.TopTracksFragment;

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

        //Customise action bar
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setTitle(R.string.top_tracks_title);
        bar.setSubtitle(getIntent().getStringExtra(EXTRA_ARTIST_NAME));


        //Create the fragment
        TopTracksFragment fragment = new TopTracksFragment();

        Bundle args = new Bundle();
        args.putString(TopTracksFragment.KEY_ARTIST_ID, getIntent().getStringExtra(EXTRA_ARTIST_ID));

        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.top_tracks_container, fragment)
                .commit();
    }
}
