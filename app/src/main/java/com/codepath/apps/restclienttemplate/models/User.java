package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public String name;
    public String screenName;
    public boolean verified;
    public String description;
    public int followerCount;
    public int friends;
    public String profileImageUrl;

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url_https");
        user.verified = jsonObject.getBoolean("verified");
        user.description = jsonObject.getString("description");
        user.followerCount = jsonObject.getInt("followers_count");
        user.friends = jsonObject.getInt("friends_count");

        return user;

    }



}
