package com.example.android.graphapplication.model;

import java.util.List;

public class SelectedScenarioSectionModel {
    private String title;
    private List<SelectedScenarioModel> selectedScenarioModelList;

    public SelectedScenarioSectionModel(String title, List<SelectedScenarioModel> selectedScenarioModelList) {
        this.title = title;
        this.selectedScenarioModelList = selectedScenarioModelList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SelectedScenarioModel> getSelectedScenarioModelList() {
        return selectedScenarioModelList;
    }

    public void setSelectedScenarioModelList(List<SelectedScenarioModel> scenarioModelList) {
        this.selectedScenarioModelList = scenarioModelList;
    }
}
