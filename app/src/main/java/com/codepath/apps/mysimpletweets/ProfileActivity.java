package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity{
    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApplication.getRestClient();

        //Get the screen name from Timeline Activity
        String screenName = getIntent().getStringExtra("screen_name");
        if (savedInstanceState == null) {

            newUser(screenName);

            //Create the user timeline fragment
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);

            //Display user timeline fragment within the activity dynamically
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit(); //changes the fragments
        }
    }

    public void newUser(final String screenName) {
        if (screenName != null && !screenName.isEmpty()) {

            client.getNewUserInfo(screenName, new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    Toast.makeText(ProfileActivity.this, screenName, Toast.LENGTH_SHORT).show();
                    Toast.makeText(ProfileActivity.this, user.getName(), Toast.LENGTH_SHORT).show();
                    populateProfileHeader(user);
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(ProfileActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            client.getUserInfo(new JsonHttpResponseHandler() {
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    populateProfileHeader(user);
                }

                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Toast.makeText(ProfileActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void populateProfileHeader(User user) {
        //Current user's account info
        TextView tvName = (TextView) findViewById(R.id.tvFullName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());

        tvFollowers.setText(user.getFollowersCount()+ " Followers");
        tvFollowing.setText(user.getFriendsCount() + " Following");

        ivProfileImage.setImageResource(android.R.color.transparent);

        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }
}
