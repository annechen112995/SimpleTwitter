package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by annechen on 6/27/16.
 */

//Takes the tweets objects created and turns them into views.
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, ArrayList<Tweet> tweets) {
        super(context, R.layout.item_tweet, tweets);
    }

    private static class ViewHolder {
        TextView tvUsername;
        TextView tvBody;
        //ImageView ivProfileImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get the tweet
        Tweet tweet = getItem(position);

        ViewHolder viewHolder;

        //Find or inflate the template
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            //viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageView ivProfileImage= (ImageView) convertView.findViewById(R.id.ivProfileImage);

        //Populate the data into the subviews
        viewHolder.tvUsername.setText(tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getBody());

        //tvBody.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent);

        //Clears out image for a recycle view
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        //Return the view to be returned into the list
        return convertView;
    }
}
