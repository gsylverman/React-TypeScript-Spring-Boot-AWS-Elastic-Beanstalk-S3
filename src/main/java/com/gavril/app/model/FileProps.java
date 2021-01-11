package com.gavril.app.model;

public class FileProps {
    private int id;
    private String name;
    private String fullName;
    private String url;

    public FileProps(int id, String name, String fullName, String url) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.url = url;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}