package com.example.android.graphapplication.model;

public class CommonTitleModel {

    private String title;

    public CommonTitleModel(String title) {
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
        return "CommonTitleModel{" +
                "title='" + title + '\'' +
                '}';
    }
}
