package com.example.android.graphapplication.model;

import java.util.List;

public class ScenarioSectionModel {
    private String title;
    private List<ScenarioModel> scenarioModelList;

    public ScenarioSectionModel(String title, List<ScenarioModel> scenarioModelList) {
        this.title = title;
        this.scenarioModelList = scenarioModelList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ScenarioModel> getScenarioModelList() {
        return scenarioModelList;
    }

    public void setScenarioModelList(List<ScenarioModel> scenarioModelList) {
        this.scenarioModelList = scenarioModelList;
    }
}
