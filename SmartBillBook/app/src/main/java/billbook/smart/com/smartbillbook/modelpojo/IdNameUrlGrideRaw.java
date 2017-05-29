package billbook.smart.com.smartbillbook.modelpojo;

/**
 * Created by Shanni on 11/5/2016.
 */

public class IdNameUrlGrideRaw {

    String id,title,url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public IdNameUrlGrideRaw(String id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }
}
