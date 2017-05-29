package com.barapp.simulator.Object_Getter_Setter;

/**
 * Created by Shanni on 1/23/2017.
 */

public class UserShopList_Object {

    public String id ,owner_id , img_url , product_name ,barname , product_price ,products ,products_prices , isPurchase ,quantity;

    public UserShopList_Object(String id , String owner_id , String img_url, String product_name, String barname, String product_price , String products , String products_prices , String isPurchase , String quantity) {
        this.id = id;
        this.owner_id = owner_id;
        this.img_url = img_url;
        this.product_name = product_name;
        this.barname = barname;
        this.product_price = product_price;

        this.products = products;
        this.products_prices = products_prices;
        this.isPurchase = isPurchase;
        this.quantity = quantity;
    }

    public String getId() { return id;}

    public void setId(String id) { this.id = id; }

    public String getProducts_prices() {
        return products_prices;
    }

    public void setProducts_prices(String products_prices) {
        this.products_prices = products_prices;
    }

    public String getIsPurchase() {
        return isPurchase;
    }

    public void setIsPurchase(String isPurchase) {
        this.isPurchase = isPurchase;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getOwner_id() { return owner_id;}

    public void setOwner_id(String owner_id) { this.owner_id = owner_id;}

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBarname() {
        return barname;
    }

    public void setBarname(String barname) {
        this.barname = barname;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getQuantity() {  return quantity; }

    public void setQuantity(String quantity) { this.quantity = quantity; }
}
