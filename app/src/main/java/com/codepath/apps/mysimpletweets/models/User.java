package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by annechen on 6/27/16.
 */

public class User {
    //list attributes
    private String name;
    private String screenName;
    private String profileImageUrl;
    private String tagline;
    private long uid;
    private int followersCount;
    private int friendsCount;

    public int getFriendsCount() {
        return friendsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public String getTagline() {
        return tagline;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public long getUid() {
        return uid;
    }

    //deserialize the user JSON => User
    public static User fromJSON(JSONObject json) {
        User u = new User();

        //Extract and fill values from the JSON
        try {
            u.name = json.getString("name");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url");
            u.uid = json.getLong("id");
            u.tagline = json.getString("description");
            u.followersCount = json.getInt("followers_count");
            u.friendsCount = json.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Return a user
        return u;
    }
}
