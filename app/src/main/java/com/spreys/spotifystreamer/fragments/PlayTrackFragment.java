package com.spreys.spotifystreamer.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.spreys.spotifystreamer.MyApplication;
import com.spreys.spotifystreamer.R;
import com.spreys.spotifystreamer.activities.PlayTrackActivity;
import com.spreys.spotifystreamer.activities.TopTracksActivity;
import com.spreys.spotifystreamer.adapters.SearchResultsAdapter;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created with Android Studio
 *
 * @author vspreys
 *         Date: 21/06/15
 *         Project: Spotify Streamer
 *         Contact by: vlad@spreys.com
 */
public class PlayTrackFragment extends Fragment {
    public static final String KEY_TOP_TRACK_ID = "top_track_id";
    private Handler mHandler = new Handler();
    private MediaPlayer mPlayer;
    private Track mTrack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.play_track_fragment, container, false);

        mTrack = ((MyApplication)getActivity().getApplication())
                .getTrackById(getArguments().getString(KEY_TOP_TRACK_ID));
        mPlayer = new MediaPlayer();

        //Fill up text views
        ((TextView)view.findViewById(R.id.play_track_artist_name)).setText(mTrack.artists.get(0).name);
        ((TextView)view.findViewById(R.id.play_track_album_name)).setText(mTrack.album.name);
        ((TextView)view.findViewById(R.id.play_track_name)).setText(mTrack.name);

        //Download the artwork
        if(mTrack.album.images.size() > 0){

            ImageView imageView = (ImageView)view.findViewById(R.id.play_track_artwork);
            //Clear re-used image
            imageView.setImageDrawable(null);

            //Download a new image with Picasso
            Picasso.with(getActivity()).load(mTrack.album.images.get(0).url).into(imageView);
        } else {
            ((ImageView)view.findViewById(R.id.play_track_artwork))
                    .setImageResource(R.drawable.no_image);
        }

        prepareMediaPlayer();

        view.findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mPlayer.isPlaying()){
                    play();
                } else {
                    mPlayer.pause();
                    ((Button)view.findViewById(R.id.btn_play))
                            .setText(getString(R.string.btn_play));
                }
            }
        });

        return view;
    }

    private void prepareMediaPlayer() {
        //Prepare player
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(mTrack.preview_url);
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    getActivity().findViewById(R.id.btn_play).setEnabled(true);
                    ((TextView)getActivity().findViewById(R.id.play_track_current_time))
                            .setText(String.valueOf("0:00"));
                    ((TextView)getActivity().findViewById(R.id.play_track_max_length))
                            .setText(String.valueOf("0:" + mPlayer.getDuration() / 1000));
                }
            });
            mPlayer.prepare();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void play(){
        //Resume playing
        mPlayer.start();
        initiateSeekBar();
        ((Button)getActivity().findViewById(R.id.btn_play)).setText("||");
    }

    private void initiateSeekBar(){

        //Configure seek bar
        final SeekBar seekBar = (SeekBar) getActivity().findViewById(R.id.play_track_seek_bar);
        seekBar.setMax(mPlayer.getDuration());

        mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mPlayer != null) {
                    int mCurrentPosition = mPlayer.getCurrentPosition();
                    seekBar.setProgress(mCurrentPosition);

                    int currentTimeInt = mPlayer.getCurrentPosition() / 1000;
                    String currentTimeString;
                    if (currentTimeInt < 10 ) {
                        currentTimeString = "0" + currentTimeInt;
                    } else {
                        currentTimeString = String.valueOf(currentTimeInt);
                    }
                    ((TextView) getActivity().findViewById(R.id.play_track_current_time))
                            .setText(String.valueOf("0:" + currentTimeString));
                }
                mHandler.postDelayed(this, 50);
            }
        });
    }
}
