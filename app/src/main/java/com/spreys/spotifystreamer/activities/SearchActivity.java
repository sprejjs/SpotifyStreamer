package com.spreys.spotifystreamer.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.spreys.spotifystreamer.IActivity;
import com.spreys.spotifystreamer.MyApplication;
import com.spreys.spotifystreamer.R;
import com.spreys.spotifystreamer.fragments.PlayTrackFragment;
import com.spreys.spotifystreamer.fragments.TopTracksFragment;

public class SearchActivity extends AppCompatActivity implements IActivity {
    private boolean mTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach);

        mTablet = findViewById(R.id.top_tracks_container) != null;
    }

    public void onItemSelected(int position) {

        MyApplication mApplication = (MyApplication)getApplication();

        if(mTablet) {
            //Customise action bar
            ActionBar bar = getSupportActionBar();
            assert bar != null;
            bar.setTitle(R.string.top_tracks_title);
            bar.setSubtitle(getIntent().getStringExtra(mApplication.searchResultArtists.get(position).name));

            //Create the fragment
            TopTracksFragment fragment = new TopTracksFragment();

            Bundle args = new Bundle();
            args.putString(TopTracksFragment.KEY_ARTIST_ID, mApplication.searchResultArtists.get(position).id);

            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.top_tracks_container, fragment)
                    .commit();
        } else {
            //Navigate to the next activity
            Intent intent = new Intent(this, TopTracksActivity.class);
            intent.putExtra(TopTracksActivity.EXTRA_ARTIST_ID, mApplication.searchResultArtists.get(position).id);
            intent.putExtra(TopTracksActivity.EXTRA_ARTIST_NAME, mApplication.searchResultArtists.get(position).name);
            startActivity(intent);
        }

        //Clear the top tracks cache
        mApplication.topTracks = null;
    }

    public void onTrackSelected(int position){
        PlayTrackFragment fragment = new PlayTrackFragment();

        Bundle args = new Bundle();
        args.putString(PlayTrackFragment.KEY_TOP_TRACK_ID, ((MyApplication)getApplication()).topTracks.get(position).id);

        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        fragment.show(getSupportFragmentManager(), null);
    }
}
