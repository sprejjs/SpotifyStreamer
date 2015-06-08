package com.spreys.spotifystreamer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spreys.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created with Android Studio
 * @author vspreys
 * Date: 8/06/15
 * Project: Spotify Streamer
 * Contact by: vlad@spreys.com
 */
public class TopTracksAdapter extends ArrayAdapter<Track> {
    private List<Track> tracks;
    private Context mContext;

    public TopTracksAdapter(Context context, List<Track> tracks) {
        super(context, 0, tracks);
        this.tracks = tracks;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.track_item, parent, false);
        }

        Track track = tracks.get(position);

        //Set title
        ((TextView)convertView.findViewById(R.id.track_item_album)).setText(track.album.name);
        ((TextView)convertView.findViewById(R.id.track_item_title)).setText(track.name);

        //Set image
        if(track.album.images.size() > 0){

            ImageView imageView = (ImageView)convertView.findViewById(R.id.track_item_thumbnail);
            //Clear re-used image
            imageView.setImageDrawable(null);

            //Download a new image with Picasso
            Picasso.with(mContext).load(track.album.images.get(0).url).into(imageView);
        } else {
            ((ImageView)convertView.findViewById(R.id.track_item_thumbnail))
                    .setImageResource(R.drawable.no_image);
        }

        return convertView;
    }
}
