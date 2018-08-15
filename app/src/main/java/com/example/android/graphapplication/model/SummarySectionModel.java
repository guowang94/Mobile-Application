package com.example.android.graphapplication.model;

import java.util.List;

public class SummarySectionModel {
    private String title;
    private List<SummaryModel> summaryModelList;

    public SummarySectionModel(String title, List<SummaryModel> summaryModelList) {
        this.title = title;
        this.summaryModelList = summaryModelList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SummaryModel> getSummaryModelList() {
        return summaryModelList;
    }

    public void setSummaryModelList(List<SummaryModel> summaryModelList) {
        this.summaryModelList = summaryModelList;
    }
}
