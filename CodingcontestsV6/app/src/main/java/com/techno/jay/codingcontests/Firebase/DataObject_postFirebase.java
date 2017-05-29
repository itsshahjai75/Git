package com.techno.jay.codingcontests.Firebase;

/**
 * Created by Jai on 08-Nov-15.
 */

public class DataObject_postFirebase {

    public DataObject_postFirebase() {
    }

    public static final int NO_IMAGE = 0;
    public static final int WITH_IMAGE = 1;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Long getContestid() {
        return contestid;
    }

    public void setContestid(Long contestid) {
        this.contestid = contestid;
    }

    public Long getResource_id() {
        return resource_id;
    }

    public void setResource_id(Long resource_id) {
        this.resource_id = resource_id;
    }

    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    public String getReminderstatus() {
        return reminderstatus;
    }

    public void setReminderstatus(String reminderstatus) {
        this.reminderstatus = reminderstatus;
    }

    public int getType() {
       mType=NO_IMAGE;
        return mType;
    }
    public void setType(int type) {
        this.mType = type;
    }

    private int mType;
    private String duration;
    private String end;
    private String start;
    private String event;
    private String href;
    private Long contestid;
    private Long resource_id;
    private String resource_name,reminderstatus;

    public DataObject_postFirebase(String duration, String end, String start, String event, String href, Long contestid, Long resource_id, String resource_name, String reminderstatus) {
        this.duration = duration;
        this.end = end;
        this.start = start;
        this.event = event;
        this.href = href;
        this.contestid = contestid;
        this.resource_id =resource_id ;
        this.resource_name = resource_name;
        this.reminderstatus = reminderstatus;

    }
}