package com.example.android.graphapplication.model;

public class ScenarioModel {

    private String title;
    private boolean isSelected;

    public ScenarioModel(String title) {
        this.title = title;
        this.isSelected = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
