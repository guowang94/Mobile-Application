package com.example.android.graphapplication.model;

public class SelectedScenarioModel {

    public static final int OTHER_SCENARIO = 0;
    public static final int PLAN_SCENARIO = 1;
    public static final int SECTION_HEADER = 2;

    private int scenarioType;
    private String title;
    private String type;
    private String age;
    private String amount;
    private String duration;
    private String poAge;
    private String poAmount;
    private String poDuration;
    private String planStatus;
    private String sectionTitle;

    public SelectedScenarioModel(String title, String type, String age, String amount, String duration, int scenarioType) {
        this.title = title;
        this.type = type;
        this.age = age;
        this.amount = amount;
        this.duration = duration;
        this.scenarioType = scenarioType;
    }

    public SelectedScenarioModel(String title, String type, String age, String amount, String duration, String poAge, String poAmount, String poDuration, String planStatus, int scenarioType) {
        this.title = title;
        this.type = type;
        this.age = age;
        this.amount = amount;
        this.duration = duration;
        this.poAge = poAge;
        this.poAmount = poAmount;
        this.poDuration = poDuration;
        this.planStatus = planStatus;
        this.scenarioType = scenarioType;
    }

    public SelectedScenarioModel(String sectionTitle, int scenarioType) {
        this.scenarioType = scenarioType;
        this.sectionTitle = sectionTitle;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public int getScenarioType() {
        return scenarioType;
    }

    public String getPoAge() {
        return poAge;
    }

    public String getPoAmount() {
        return poAmount;
    }

    public String getPoDuration() {
        return poDuration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
