package com.example.android.graphapplication.model;

public class CommonModel {

    private int id;
    private String name;
    private String type;
    private int age;
    private String description;
    private String status;
    private float amount;
    private int duration;
    private int noIncomeStatus;
    private int isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public int getNoIncomeStatus() {
        return noIncomeStatus;
    }

    public void setNoIncomeStatus(int noIncomeStatus) {
        this.noIncomeStatus = noIncomeStatus;
    }

    @Override
    public String toString() {
        return "CommonModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", age=" + age +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", duration=" + duration +
                ", noIncomeStatus=" + noIncomeStatus +
                ", isSelected=" + isSelected +
                '}';
    }
}
