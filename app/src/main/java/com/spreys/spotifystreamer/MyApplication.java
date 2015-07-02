package com.spreys.spotifystreamer;

import android.app.Application;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created with Android Studio
 *
 * @author vspreys
 *         Date: 9/06/15
 *         Project: Spotify Streamer
 *         Contact by: vlad@spreys.com
 */
public class MyApplication extends Application {
    public List<Artist> searchResultArtists;
    public List<Track> topTracks;

    public Track getTrackById(String id) {
        for (Track track : topTracks) {
            if(track.id.equals(id)) {
                return track;
            }
        }

        return null;
    }

    public Track getNextTrack(Track track) {
        int index = topTracks.indexOf(track);

        if(index + 1 < topTracks.size()) {
            return topTracks.get(index + 1);
        }

        return null;
    }

    public Track getPreviousTrack(Track track) {
        int index = topTracks.indexOf(track);

        if(index - 1 >= 0) {
            return topTracks.get(index - 1);
        }

        return null;
    }
}
