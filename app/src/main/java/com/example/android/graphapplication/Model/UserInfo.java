package com.example.android.graphapplication.Model;

public class UserInfo {

    private String title, value;
    private int image;

    public UserInfo(String title, String value, int image) {
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
        return "UserInfo{" +
                "title='" + title + '\'' +
                ", value='" + value + '\'' +
                ", image=" + image +
                '}';
    }
}
