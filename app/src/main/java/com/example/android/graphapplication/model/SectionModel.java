package com.example.android.graphapplication.model;

import java.util.List;

public class SectionModel {
    private String title;
    private List<SummaryModel> itemList;

    public SectionModel(String title, List<SummaryModel> itemList) {
        this.title = title;
        this.itemList = itemList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SummaryModel> getItemList() {
        return itemList;
    }

    public void setItemList(List<SummaryModel> itemList) {
        this.itemList = itemList;
    }
}
