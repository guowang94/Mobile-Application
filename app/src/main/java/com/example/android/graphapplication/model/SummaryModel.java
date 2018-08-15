package com.example.android.graphapplication.model;

public class SummaryModel {
    private int image;
    private String title, value;

    public SummaryModel(int image, String title, String value) {
        this.image = image;
        this.title = title;
        this.value = value;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
