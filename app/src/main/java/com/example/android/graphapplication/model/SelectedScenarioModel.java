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

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public int getScenarioType() {
        return scenarioType;
    }

    public void setScenarioType(int scenarioType) {
        this.scenarioType = scenarioType;
    }

    public String getPoAge() {
        return poAge;
    }

    public void setPoAge(String poAge) {
        this.poAge = poAge;
    }

    public String getPoAmount() {
        return poAmount;
    }

    public void setPoAmount(String poAmount) {
        this.poAmount = poAmount;
    }

    public String getPoDuration() {
        return poDuration;
    }

    public void setPoDuration(String poDuration) {
        this.poDuration = poDuration;
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
