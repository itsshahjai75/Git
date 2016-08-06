package arihantmart.techno.arihantmart;

/**
 * Created by Jai on 08-Nov-15.
 */

public class DataObject_Dailyoffers {

    private String mOffertitle;
    private String mOffermessage;
    private String mOfferdate;


    DataObject_Dailyoffers(String mOffertitle, String mOffermessage, String mOfferdate) {
        this.mOffertitle = mOffertitle;
        this.mOffermessage = mOffermessage;
        this.mOfferdate = mOfferdate;


    }

    public String getmOffertitle() {
        return mOffertitle;
    }

    public void setmOffertitle(String mOffertitle) {
        this.mOffertitle = mOffertitle;
    }

    public String getmOffermessage() {
        return mOffermessage;
    }

    public void setmOffermessage(String mOffermessage) {
        this.mOffermessage = mOffermessage;
    }

    public String getmOfferdate() {
        return mOfferdate;
    }

    public void setmOfferdate(String mOfferdate) {
        this.mOfferdate = mOfferdate;
    }
}