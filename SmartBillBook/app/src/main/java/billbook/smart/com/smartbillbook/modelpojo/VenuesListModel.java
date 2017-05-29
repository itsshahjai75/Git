package billbook.smart.com.smartbillbook.modelpojo;

/**
 * Created by Testing on 18-Oct-16.
 */

public class VenuesListModel {
    String billnumber,partyname,email ,mobileno,amount ,note,paid ,timestamp;

    public String getBillnumber() {
        return billnumber;
    }

    public void setBillnumber(String billnumber) {
        this.billnumber = billnumber;
    }

    public String getPartyname() {
        return partyname;
    }

    public void setPartyname(String partyname) {
        this.partyname = partyname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public VenuesListModel(String billnumber, String partyname, String email, String mobileno, String amount, String note, String paid, String timestamp) {
        this.billnumber = billnumber;
        this.partyname = partyname;
        this.email = email;
        this.mobileno = mobileno;
        this.amount = amount;
        this.note = note;
        this.paid = paid;
        this.timestamp = timestamp;
    }
}