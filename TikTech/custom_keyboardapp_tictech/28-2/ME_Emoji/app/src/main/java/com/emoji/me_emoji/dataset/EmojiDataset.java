package com.emoji.me_emoji.dataset;

import java.io.File;

/**
 * Created by Android Developer on 2/24/2017.
 */

public class EmojiDataset {

    String url="";
    File file=new File("");
    String filepath="";

    public EmojiDataset() {
        this.url = "";
        this.file = new File("");
        this.filepath = "";
    }

    public EmojiDataset(String url, File file, String filepath) {
        this.url = url;
        this.file = file;
        this.filepath = filepath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public String toString() {
        return "EmojiDataset{" +
                "url='" + url + '\'' +
                ", file=" + file +
                ", filepath='" + filepath + '\'' +
                '}';
    }
}
