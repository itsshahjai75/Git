package com.barapp.simulator.Object_Getter_Setter;

import java.io.Serializable;

/**
 * Created by Shanni on 1/27/2017.
 */

public class Category_SubCategory_Object implements Serializable{

    String id ,image_url , wine_name , price;
    boolean is_selected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getWine_name() {
        return wine_name;
    }

    public void setWine_name(String wine_name) {
        this.wine_name = wine_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean is_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    public Category_SubCategory_Object(String id, String image_url, String wine_name, String price, boolean is_selected) {
        this.id = id;
        this.image_url = image_url;
        this.wine_name = wine_name;
        this.price = price;
        this.is_selected = is_selected;

    }


}
