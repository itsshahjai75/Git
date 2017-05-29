package com.barapp.simulator.Object_Getter_Setter;

/**
 * Created by Shanni on 1/27/2017.
 */

public class Gallery_Object {

    String id , file_name ,file;

    public Gallery_Object(String id, String file_name, String file) {
        this.id = id;
        this.file_name = file_name;
        this.file = file;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
