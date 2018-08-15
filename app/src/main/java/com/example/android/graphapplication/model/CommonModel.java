package com.example.android.graphapplication.model;

public class CommonModel {

    private String title;

    public CommonModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "CommonModel{" +
                "title='" + title + '\'' +
                '}';
    }
}
