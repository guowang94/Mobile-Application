package com.example.android.graphapplication.viewHolder;

public class CommonTitleViewHolder {

    private String title;

    public CommonTitleViewHolder(String title) {
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
        return "CommonTitleViewHolder{" +
                "title='" + title + '\'' +
                '}';
    }
}
