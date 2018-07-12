package com.example.android.graphapplication.recyclerList.Model;

public class SummaryBalance {

    private String title, value;
    private int image;

    public SummaryBalance(String title, String value, int image) {
        this.title = title;
        this.value = value;
        this.image = image;
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

    @Override
    public String toString() {
        return "SummaryBalance{" +
                "title='" + title + '\'' +
                ", value='" + value + '\'' +
                ", image=" + image +
                '}';
    }
}
