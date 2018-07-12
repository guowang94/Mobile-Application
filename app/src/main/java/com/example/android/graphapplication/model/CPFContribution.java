package com.example.android.graphapplication.model;

public class CPFContribution {

    private String title, value;

    public CPFContribution(String title, String value) {
        this.title = title;
        this.value = value;
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
        return "CPFContribution{" +
                "title='" + title + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
