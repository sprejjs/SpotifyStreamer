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

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created with Android Studio
 * @author vspreys
 * Date: 8/06/15
 * Project: Spotify Streamer
 * Contact by: vlad@spreys.com
 */
public class SearchResultsAdapter extends ArrayAdapter<Artist> {
    private List<Artist> artists;
    private Context mContext;

    public SearchResultsAdapter(Context context, List<Artist> artists) {
        super(context, 0, artists);
        this.artists = artists;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_item, parent, false);
        }

        Artist artist = artists.get(position);

        //Set title
        TextView textView = (TextView)convertView.findViewById(R.id.search_result_item_name);
        textView.setText(artist.name);

        //Set image
        if(artist.images.size() > 0){

            ImageView imageView = (ImageView)convertView.findViewById(R.id.search_result_item_image);
            //Clear re-used image
            imageView.setImageDrawable(null);

            //Download a new image with Picasso
            Picasso.with(mContext).load(artist.images.get(0).url).into(imageView);
        } else {
            ((ImageView)convertView.findViewById(R.id.search_result_item_image))
                    .setImageResource(R.drawable.no_image);
        }

        return convertView;
    }
}
