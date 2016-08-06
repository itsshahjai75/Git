package com.arihantmartadmin.jay.arihantmart_admin;

/**
 * Created by Jai on 08-Nov-15.
 */

/**
 * Created by Jai on 08-Nov-15.
 */

public class DataObject_ShortOrderlist {

    private String mOrderno;
    private String mOrderdate;
    private String mOrderUseremail;
    private String mOrderTotalamt;
    private String mOrderSavings;
    private String mOrderStatus;

    DataObject_ShortOrderlist(String morderno, String morderdate, String morderuseremail, String mordertotalamt, String mordersavings, String morderstaus){
        mOrderno = morderno;
        mOrderdate = morderdate;
        mOrderUseremail = morderuseremail;
        mOrderTotalamt=mordertotalamt;
        mOrderSavings=mordersavings;
        mOrderStatus=morderstaus;

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

    public String getmorderuseremail() {
        return mOrderUseremail;
    }

    public void setmorderuseremail(String mOrderUseremail) {
        this.mOrderUseremail = mOrderUseremail;
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

            byte[] imageAsBytes = Base64.decode(mOrderUseremail, Base64.DEFAULT);
            Bitmap btmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

            bitmap_img = btmp;
            return bitmap_img;

    }

    public void setBitmap_img(Bitmap bitmap_img) {

        byte[] imageAsBytes = Base64.decode(mOrderUseremail, Base64.DEFAULT);
        Bitmap btmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

        bitmap_img=btmp;

        this.bitmap_img = bitmap_img;
    }*/



    public String getmorderstaus() {
        return mOrderStatus;
    }

    public void setmorderstaus(String mcart_id) {
        this.mOrderStatus = mOrderStatus;
    }


      /*public String getmaddedtime() {
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