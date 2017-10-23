package com.example.josh.socialnetwork.models;

/**
 * Created by JOSH on 20-10-2017.
 */

public class UserAccountSettings {

    private String display_name;
    private String description;
    private long followers;
    private long posts;
    private long following;
    private String profile_photo;
    private String username;
    private String website;

    public UserAccountSettings(String description, String display_name, long followers, long following,
                               long posts, String profile_photo, String username, String website) {
        this.display_name = display_name;
        this.description = description;
        this.followers = followers;
        this.posts = posts;
        this.following = following;
        this.profile_photo = profile_photo;
        this.username = username;
        this.website = website;
    }

    public UserAccountSettings() {

    }

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
}
