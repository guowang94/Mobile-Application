package com.example.android.graphapplication.viewHolder;

import java.util.List;

public class ScenarioSectionViewHolder {
    private String title;
    private List<ScenarioViewHolder> mScenarioViewHolderList;

    public ScenarioSectionViewHolder(String title, List<ScenarioViewHolder> scenarioViewHolderList) {
        this.title = title;
        this.mScenarioViewHolderList = scenarioViewHolderList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ScenarioViewHolder> getScenarioViewHolderList() {
        return mScenarioViewHolderList;
    }
}
