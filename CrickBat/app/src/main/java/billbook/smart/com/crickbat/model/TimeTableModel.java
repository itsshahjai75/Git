package billbook.smart.com.crickbat.model;

/**
 * Created by Jay on 27-Aug-16.
 */
public class TimeTableModel {

    String date,details,time,venue;

    public TimeTableModel() {
    }

    public TimeTableModel(String date, String details, String time, String venue) {
        this.date = date;
        this.details = details;
        this.time = time;
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
