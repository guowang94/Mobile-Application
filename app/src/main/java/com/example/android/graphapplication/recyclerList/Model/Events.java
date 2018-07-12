package com.example.android.graphapplication.recyclerList.Model;

public class Events {

    private String title;

    public Events(String title) {
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
        return "Events{" +
                "title='" + title + '\'' +
                '}';
    }
}
