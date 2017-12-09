package com.example.josh.socialnetwork.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JOSH on 20-10-2017.
 */

public class UserAccountSettings implements Parcelable {

    private String display_name;
    private String description;
    private long followers;
    private long posts;
    private long following;
    private String profile_photo;
    private String username;
    private String website;
    private String user_id;

    public UserAccountSettings(String display_name, String description, long followers,
                                long posts, long following, String profile_photo, String username,
                                    String website, String user_id) {
        this.display_name = display_name;
        this.description = description;
        this.followers = followers;
        this.posts = posts;
        this.following = following;
        this.profile_photo = profile_photo;
        this.username = username;
        this.website = website;
        this.user_id = user_id;
    }

    public UserAccountSettings() {

    }

    protected UserAccountSettings(Parcel in) {
        display_name = in.readString();
        description = in.readString();
        followers = in.readLong();
        posts = in.readLong();
        following = in.readLong();
        profile_photo = in.readString();
        username = in.readString();
        website = in.readString();
        user_id = in.readString();
    }

    public static final Creator<UserAccountSettings> CREATOR = new Creator<UserAccountSettings>() {
        @Override
        public UserAccountSettings createFromParcel(Parcel in) {
            return new UserAccountSettings(in);
        }

        @Override
        public UserAccountSettings[] newArray(int size) {
            return new UserAccountSettings[size];
        }
    };

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }

    public long getFollowing() {
        return following;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "UserAccountSettings{" +
                "display_name='" + display_name + '\'' +
                ", description='" + description + '\'' +
                ", followers=" + followers +
                ", posts=" + posts +
                ", following=" + following +
                ", profile_photo='" + profile_photo + '\'' +
                ", username='" + username + '\'' +
                ", website='" + website + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(display_name);
        parcel.writeString(description);
        parcel.writeLong(followers);
        parcel.writeLong(posts);
        parcel.writeLong(following);
        parcel.writeString(profile_photo);
        parcel.writeString(username);
        parcel.writeString(website);
        parcel.writeString(user_id);
    }
}
