package com.spreys.spotifystreamer.fragments;

import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.spreys.spotifystreamer.MyApplication;
import com.spreys.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created with Android Studio
 *
 * @author vspreys
 *         Date: 21/06/15
 *         Project: Spotify Streamer
 *         Contact by: vlad@spreys.com
 */
public class PlayTrackFragment extends DialogFragment {
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

        prepareView(view);

        return view;
    }

    private void prepareView(final View view) {
        //Fill up text views
        ((TextView)view.findViewById(R.id.play_track_artist_name)).setText(mTrack.artists.get(0).name);
        ((TextView)view.findViewById(R.id.play_track_album_name)).setText(mTrack.album.name);
        ((TextView)view.findViewById(R.id.play_track_name)).setText(mTrack.name);

        //Reset play button
        Button playButton = ((Button)view.findViewById(R.id.btn_play));
        playButton.setText(getString(R.string.btn_play));
        playButton.setEnabled(false);

        //Disable previous/next button if required
        Button previousButton = (Button)view.findViewById(R.id.btn_previous);
        previousButton.setEnabled(
                ((MyApplication)getActivity().getApplication()).getPreviousTrack(mTrack) != null
        );

        Button nextButton = (Button)view.findViewById(R.id.btn_next);
        nextButton.setEnabled(
                ((MyApplication)getActivity().getApplication()).getNextTrack(mTrack) != null
        );

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

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPlayer.isPlaying()) {
                    play();
                } else {
                    mPlayer.pause();
                    ((Button) view.findViewById(R.id.btn_play))
                            .setText(getString(R.string.btn_play));
                }
            }
        });

        view.findViewById(R.id.btn_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrack = ((MyApplication)getActivity().getApplication()).getPreviousTrack(mTrack);
                prepareView(getView());
            }
        });

        view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTrack = ((MyApplication)getActivity().getApplication()).getNextTrack(mTrack);
                prepareView(getView());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayer.stop();
        mPlayer = null;
    }

    private void prepareMediaPlayer() {
        //Prepare player
        try {
            mPlayer.reset();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setDataSource(mTrack.preview_url);
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (mPlayer != null) {
                        getView().findViewById(R.id.btn_play).setEnabled(true);
                        ((TextView)getView().findViewById(R.id.play_track_current_time))
                                .setText(String.valueOf("0:00"));
                        ((TextView)getView().findViewById(R.id.play_track_max_length))
                                .setText(String.valueOf("0:" + mPlayer.getDuration() / 1000));

                        //Start playing automatically
                        play();
                    }
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
        ((Button)getView().findViewById(R.id.btn_play)).setText("||");
    }

    private void initiateSeekBar(){

        //Configure seek bar
        final SeekBar seekBar = (SeekBar) getView().findViewById(R.id.play_track_seek_bar);
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
                    ((TextView) getView().findViewById(R.id.play_track_current_time))
                            .setText(String.valueOf("0:" + currentTimeString));
                }
                mHandler.postDelayed(this, 50);
            }
        });
    }
}
