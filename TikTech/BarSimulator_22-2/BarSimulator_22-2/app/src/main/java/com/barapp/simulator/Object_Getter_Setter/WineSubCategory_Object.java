package com.barapp.simulator.Object_Getter_Setter;

import java.io.Serializable;

/**
 * Created by Shanni on 1/20/2017.
 */

public class WineSubCategory_Object implements Serializable {

    String id ,image_url , wine_subcategory_name , wine_subcategory_price;
    boolean is_selected;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getWine_subcategory_name() {
        return wine_subcategory_name;
    }

    public void setWine_subcategory_name(String wine_subcategory_name) {
        this.wine_subcategory_name = wine_subcategory_name;
    }

    public String getWine_subcategory_price() {
        return wine_subcategory_price;
    }

    public void setWine_subcategory_price(String wine_subcategory_price) {
        this.wine_subcategory_price = wine_subcategory_price;
    }

    public boolean is_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WineSubCategory_Object(String id , String image_url, String wine_subcategory_name, String wine_subcategory_price, boolean is_selected) {

        this.id = id;

        this.image_url = image_url;
        this.wine_subcategory_name = wine_subcategory_name;
        this.wine_subcategory_price = wine_subcategory_price;
        this.is_selected = is_selected;
    }
}
