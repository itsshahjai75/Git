package com.barapp.simulator.Object_Getter_Setter;

/**
 * Created by Shanni on 1/21/2017.
 */

public class PurchaseDetailList_Object {

    String id ,wine_name ,wine_price , wine_image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWine_name() {
        return wine_name;
    }

    public void setWine_name(String wine_name) {
        this.wine_name = wine_name;
    }

    public String getWine_price() {
        return wine_price;
    }

    public void setWine_price(String wine_price) {
        this.wine_price = wine_price;
    }

    public String getWine_image() {
        return wine_image;
    }

    public void setWine_image(String wine_image) {
        this.wine_image = wine_image;
    }

    public PurchaseDetailList_Object(String id, String wine_name, String wine_price, String wine_image) {
        this.id = id;
        this.wine_name = wine_name;
        this.wine_price = wine_price;

        this.wine_image = wine_image;
    }
}
