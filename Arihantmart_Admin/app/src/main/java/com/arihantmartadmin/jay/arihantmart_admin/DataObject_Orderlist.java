package com.arihantmartadmin.jay.arihantmart_admin;

/**
 * Created by Jai on 08-Nov-15.
 */

public class DataObject_Orderlist {

    private String mOrderno;
    private String mOrderdate;
    private String mOrderItems;
    private String mOrderTotalamt;
    private String mOrderSavings;

    DataObject_Orderlist(String morderno, String morderdate, String morderitems, String mordertotalamt, String mordersavings){
        mOrderno = morderno;
        mOrderdate = morderdate;
        mOrderItems = morderitems;
        mOrderTotalamt=mordertotalamt;
        mOrderSavings=mordersavings;

    }
    public String getmorderno() {
        return mOrderno;
    }

    public void setmorderno(String mOrderno) {
        this.mOrderno = mOrderno;
    }

    public String getmorderdate() {
        return mOrderdate;
    }

    public void setmorderdate(String mOrderdate) {
        this.mOrderdate = mOrderdate;
    }

    public String getmorderitems() {
        return mOrderItems;
    }

    public void setmorderitems(String mOrderItems) {
        this.mOrderItems = mOrderItems;
    }

    public String getmordertotalamt() {
        return mOrderTotalamt;
    }

    public void setmordertotalamt(String mOrderTotalamt) {
        this.mOrderTotalamt = mOrderTotalamt;
    }


    public String getmordersavings() {
        return mOrderSavings;
    }

    public void setmordersavings(String mOrderSavings) {
        this.mOrderSavings = mOrderSavings;
    }


    /*public Bitmap getmBitmap_img() {

            byte[] imageAsBytes = Base64.decode(mOrderItems, Base64.DEFAULT);
            Bitmap btmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

            bitmap_img = btmp;
            return bitmap_img;

    }

    public void setBitmap_img(Bitmap bitmap_img) {

        byte[] imageAsBytes = Base64.decode(mOrderItems, Base64.DEFAULT);
        Bitmap btmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

        bitmap_img=btmp;

        this.bitmap_img = bitmap_img;
    }*/


    /*
    public String getmcartid() {
        return mcart_id;
    }

    public void setmcartid(String mcart_id) {
        this.mcart_id = mcart_id;
    }


    public String getmaddedtime() {
        return maddedtime;
    }

    public void setmaddedtime(String maddedtime) {
        this.maddedtime = maddedtime;
    }

    public String getmquantity() {
        return mquantity;
    }

    public void setmquantity(String mquantity) {
        this.mquantity = mquantity;
    }*/

}