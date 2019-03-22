package com.example.android.graphapplication.model;

public class PlanModel {

    private int id;
    private String planName;
    private String planType;
    private String paymentType;
    private int premiumStartAge;
    private float paymentAmount;
    private int planDuration;
    private int payoutAge;
    private float payoutAmount;
    private int payoutDuration;
    private String planStatus;
    private int isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public int getPremiumStartAge() {
        return premiumStartAge;
    }

    public void setPremiumStartAge(int premiumStartAge) {
        this.premiumStartAge = premiumStartAge;
    }

    public float getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public int getPlanDuration() {
        return planDuration;
    }

    public void setPlanDuration(int planDuration) {
        this.planDuration = planDuration;
    }

    public int getPayoutAge() {
        return payoutAge;
    }

    public void setPayoutAge(int payoutAge) {
        this.payoutAge = payoutAge;
    }

    public float getPayoutAmount() {
        return payoutAmount;
    }

    public void setPayoutAmount(float payoutAmount) {
        this.payoutAmount = payoutAmount;
    }

    public int getPayoutDuration() {
        return payoutDuration;
    }

    public void setPayoutDuration(int payoutDuration) {
        this.payoutDuration = payoutDuration;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return "PlanModel{" +
                "id=" + id +
                ", planName='" + planName + '\'' +
                ", planType='" + planType + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", premiumStartAge=" + premiumStartAge +
                ", paymentAmount=" + paymentAmount +
                ", planDuration=" + planDuration +
                ", payoutAge=" + payoutAge +
                ", payoutAmount=" + payoutAmount +
                ", payoutDuration=" + payoutDuration +
                ", planStatus='" + planStatus + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
