package arihantmart.techno.arihantmart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by Jai on 08-Nov-15.
 */

public class DataObject_Itemlist {

    private String mItemname;
    private String mItemprice;
    private String mBitmap;
    private String mSaving;
    private String mitemid;

    DataObject_Itemlist(String mitemname, String mitemprice, String bitmap, String msaving,String Itemid){
        mItemname = mitemname;
        mItemprice = mitemprice;
        mBitmap = bitmap;
        mSaving=msaving;
        mitemid=Itemid;

    }
    public String getmitemname() {
        return mItemname;
    }

    public void setmitemname(String mItemname) {
        this.mItemname = mItemname;
    }

    public String getmitemprice() {
        return mItemprice;
    }

    public void setmitemprice(String mItemprice) {
        this.mItemprice = mItemprice;
    }

    public String getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(String mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getmSaving() {
        return mSaving;
    }

    public void setmSaving(String mSaving) {
        this.mSaving = mSaving;
    }


    public String getmitemid() {
        return mitemid;
    }

    public void setmitemid(String mitemid) {
        this.mitemid = mitemid;
    }


    /*public Bitmap getmBitmap_img() {

            byte[] imageAsBytes = Base64.decode(mBitmap, Base64.DEFAULT);
            Bitmap btmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

            bitmap_img = btmp;
            return bitmap_img;

    }

    public void setBitmap_img(Bitmap bitmap_img) {

        byte[] imageAsBytes = Base64.decode(mBitmap, Base64.DEFAULT);
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