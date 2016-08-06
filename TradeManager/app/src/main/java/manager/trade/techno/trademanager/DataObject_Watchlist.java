package manager.trade.techno.trademanager;

/**
 * Created by Jai on 08-Nov-15.
 */

public class DataObject_Watchlist {


    public String getCompnay_code() {
        return company_code;
    }

    public void setCompnay_code (String company_code) {
        this.company_code = company_code;
    }

    public String getCompany_current_index() {
        return current_index;
    }

    public void setCompany_current_index(String company_full_name) {
        this.current_index = current_index;
    }

    public String getCompany_diff_index() {
        return diff_index;
    }

    public void setCompany_diff_index(String company_code) {
        this.diff_index = diff_index;
    }


    public String getCompany_diff_per_index() {
        return diff_per_index;
    }

    public void setCompany_diff_per_index(String diff_per_index) {
        this.diff_per_index = diff_per_index;
    }

    public String getCompany_time_index() {
        return time_index;
    }

    public void setCompany_time_index(String time_index) {
        this.time_index = time_index;
    }


    public String getCompany_preivous_close() {
        return preivous_close;
    }

    public void setCompany_preivous_close(String preivous_close) {
        this.preivous_close = preivous_close;
    }



    private String diff_index;
    private String current_index;
    private String company_code,diff_per_index,time_index,preivous_close;


    DataObject_Watchlist( String company_code,String current_index, String diff_index,String diff_per_index,String time_index,String preivous_close) {
        this.diff_index = diff_index;
        this.current_index = current_index;
        this.company_code = company_code;
        this.diff_per_index = diff_per_index;
        this.time_index = time_index;
        this.preivous_close = preivous_close;


    }
}