package com.barapp.simulator.Object_Getter_Setter;

/**
 * Created by Shanni on 1/17/2017.
 */

public class OwnerMenu_Object {


    String  id , product_img , unit_price ,  product_name , quantity  , products , product_prices ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProducts() { return products; }

    public String getProduct_prices() {
        return product_prices;
    }

    public void setProduct_prices(String product_prices) {
        this.product_prices = product_prices;
    }

    public void setProducts(String products) { this.products = products; }

    public OwnerMenu_Object(String id, String product_img, String unit_price, String product_name, String quantity , String products , String product_prices) {
        this.id = id;
        this.product_img = product_img;
        this.unit_price = unit_price;

        this.product_name = product_name;
        this.quantity = quantity;
        this.products = products;
        this.product_prices = product_prices;
    }

}
