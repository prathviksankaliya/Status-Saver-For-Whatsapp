package com.itcraftsolution.statussaverforwhatsappdownload.Models;

import android.net.Uri;

import java.io.File;

public class Statues {

    private String name , path ;
    private Uri uri;
    private File filename;


    public Statues(String name, String path, File filename, Uri uri) {
        this.name = name;
        this.path = path;
        this.filename = filename;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getFilename() {
        return filename;
    }

    public void setFilename(File filename) {
        this.filename = filename;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
