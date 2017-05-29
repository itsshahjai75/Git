package com.barapp.simulator.Object_Getter_Setter;

/**
 * Created by Shanni on 1/31/2017.
 */

public class RateComment_Object {

    public String id , profileimg_url ,username ,rating ,description;

    public RateComment_Object(String id, String profileimg_url, String username, String rating, String description) {
        this.id = id;
        this.profileimg_url = profileimg_url;
        this.username = username;
        this.rating = rating;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfileimg_url() {
        return profileimg_url;
    }

    public void setProfileimg_url(String profileimg_url) {
        this.profileimg_url = profileimg_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
