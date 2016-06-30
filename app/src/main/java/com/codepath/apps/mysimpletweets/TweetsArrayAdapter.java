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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
        TextView tvTime;
        //ImageView ivProfileImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Get the tweet
        Tweet tweet = getItem(position);

        ViewHolder viewHolder;

        String time = getTimeDifference(tweet.getCreatedAt());

        //Find or inflate the template
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            //viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageView ivProfileImage= (ImageView) convertView.findViewById(R.id.ivProfileImage);

        //Populate the data into the subviews
        viewHolder.tvUsername.setText(tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvTime.setText(time);

        //tvBody.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent);

        //Clears out image for a recycle view
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        //Return the view to be returned into the list
        return convertView;
    }

    /**
     * Given a date String of the format given by the Twitter API, returns a display-formatted
     * String representing the relative time difference, e.g. "2m", "6d", "23 May", "1 Jan 14"
     * depending on how great the time difference between now and the given date is.
     * This, as of 2016-06-29, matches the behavior of the official Twitter app.
     */
    public String getTimeDifference(String pDate) {
        String time = "";
        int diffInDays = 0;
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat format = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        format.setLenient(true);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        String formattedDate = format.format(c.getTime());

        Date d1 = null;
        Date d2 = null;
        try {

            d1 = format.parse(formattedDate);
            d2 = format.parse(pDate);
            long diff = d1.getTime() - d2.getTime();

            diffInDays = (int) (diff / (1000 * 60 * 60 * 24));
            if (diffInDays > 0) {
                if (diffInDays == 1) {
                    time = (diffInDays + "d");
                } else if (diffInDays < 7) {
                    time = (diffInDays + "d");
                } else {
                    Calendar now = Calendar.getInstance();
                    now.setTime(d1);
                    Calendar then = Calendar.getInstance();
                    then.setTime(d2);
                    if (now.get(Calendar.YEAR) == then.get(Calendar.YEAR)) {
                        time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                                + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
                    } else {
                        time = String.valueOf(then.get(Calendar.DAY_OF_MONTH)) + " "
                                + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)
                                + " " + String.valueOf(then.get(Calendar.YEAR) - 2000);
                    }
                }
            } else {
                int diffHours = (int) (diff / (60 * 60 * 1000));
                if (diffHours > 0) {
                    if (diffHours == 1) {
                        time = (diffHours + "h");
                    } else {
                        time = (diffHours + "h");
                    }
                } else {
                    int diffMinutes = (int) ((diff / (60 * 1000) % 60));
                    if (diffMinutes < 1) {
                        int diffSeconds = (int) (diff / (1000));
                        time = (diffSeconds + "s");
                    } else if (diffMinutes == 1) {
                        time = (diffMinutes + "m");
                    } else {
                        time = (diffMinutes + "m");
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
