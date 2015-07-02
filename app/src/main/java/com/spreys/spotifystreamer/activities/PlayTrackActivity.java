package com.spreys.spotifystreamer.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.spreys.spotifystreamer.R;
import com.spreys.spotifystreamer.fragments.PlayTrackFragment;

/**
 * Created with Android Studio
 *
 * @author vspreys
 *         Date: 30/06/15
 *         Project: Spotify Streamer
 *         Contact by: vlad@spreys.com
 */
public class PlayTrackActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        //Create the fragment
        PlayTrackFragment fragment = new PlayTrackFragment();

        Bundle args = new Bundle();
        args.putString(PlayTrackFragment.KEY_TOP_TRACK_ID, getIntent().getStringExtra(PlayTrackFragment.KEY_TOP_TRACK_ID));

        fragment.setArguments(args);

        fragment.show(getSupportFragmentManager(), null);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.play_track_container, fragment)
//                .commit();
    }
}
