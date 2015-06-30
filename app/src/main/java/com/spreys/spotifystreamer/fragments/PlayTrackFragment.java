package com.spreys.spotifystreamer.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spreys.spotifystreamer.MyApplication;
import com.spreys.spotifystreamer.R;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.play_track_fragment, container, false);

        final Track track = ((MyApplication)getActivity().getApplication())
                .getTrackById(getArguments().getString(KEY_TOP_TRACK_ID));

        //Fill up text views
        ((TextView)view.findViewById(R.id.play_track_artist_name)).setText(track.artists.get(0).name);
        ((TextView)view.findViewById(R.id.play_track_album_name)).setText(track.album.name);
        ((TextView)view.findViewById(R.id.play_track_name)).setText(track.name);
        ((TextView)view.findViewById(R.id.play_track_max_length)).setText(String.valueOf(track.duration_ms * 1000));

        //Download the artwork
        if(track.album.images.size() > 0){

            ImageView imageView = (ImageView)view.findViewById(R.id.play_track_artwork);
            //Clear re-used image
            imageView.setImageDrawable(null);

            //Download a new image with Picasso
            Picasso.with(getActivity()).load(track.album.images.get(0).url).into(imageView);
        } else {
            ((ImageView)view.findViewById(R.id.play_track_artwork))
                    .setImageResource(R.drawable.no_image);
        }

        view.findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(track.preview_url);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}
