
package com.example.admin.eventsapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class EventModel {

    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("objects")
    @Expose
    private List<Object> objects = new ArrayList<Object>();

    /**
     * 
     * @return
     *     The meta
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * 
     * @param meta
     *     The meta
     */
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    /**
     * 
     * @return
     *     The objects
     */
    public List<Object> getObjects() {
        return objects;
    }

    /**
     * 
     * @param objects
     *     The objects
     */
    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

}
