package com.example.android.graphapplication.model;

import java.util.List;

public class ExportModel {

    public static final int REPORT_TITLE = 0;
    public static final int REPORT_HEADER = 1;
    public static final int REPORT_TOTAL_SECTION = 2;
    public static final int CLIENT_DETAIL = 3;
    public static final int MILESTONE_DETAIL = 4;
    public static final int PLAN_DETAIL = 5;
    public static final int PLAN_TYPE_DETAIL = 6;
    public static final int ROW_VALUE_WITH_COLOR = 7;
    public static final int REPORT_TOTAL_SECTION_WITH_COLOR = 8;

    private String header;
    private String title;
    private String value;
    private int exportType;

    //Milestone Details
    private String milestoneName;
    private String milestoneType;
    private String milestoneAgeRange;
    private String milestoneStatus;
    private String milestoneAmount;

    //Plan Details
    private String planName;
    private String paymentAgeRange;
    private String premiumStatus;
    private String totalPremiumPayment;
    private String payoutAgeRange;
    private String payoutStatus;
    private String totalPayout;
    private List<ExportModel> planTypeList;

    /**
     * This constructor is for (Report Title's timestamp / Report Header)
     *
     * @param header
     * @param exportType
     */
    public ExportModel(String header, int exportType) {
        this.header = header;
        this.exportType = exportType;
    }

    /**
     * This constructor is for (Total Section & Total Value / Client Detail's title & value / Plan Detail's title & value)
     *
     * @param title
     * @param value
     * @param exportType
     */
    public ExportModel(String title, String value, int exportType) {
        this.title = title;
        this.value = value;
        this.exportType = exportType;
    }

    public ExportModel(String milestoneName, String milestoneType, String milestoneAgeRange, String milestoneStatus, String milestoneAmount, int exportType) {
        this.milestoneName = milestoneName;
        this.milestoneType = milestoneType;
        this.milestoneAgeRange = milestoneAgeRange;
        this.milestoneStatus = milestoneStatus;
        this.milestoneAmount = milestoneAmount;
        this.exportType = exportType;
    }

    public ExportModel(String planName, String paymentAgeRange, String premiumStatus, String totalPremiumPayment, String payoutAgeRange, String payoutStatus, String totalPayout, List<ExportModel> planTypeList, int exportType) {
        this.planName = planName;
        this.paymentAgeRange = paymentAgeRange;
        this.premiumStatus = premiumStatus;
        this.totalPremiumPayment = totalPremiumPayment;
        this.payoutAgeRange = payoutAgeRange;
        this.payoutStatus = payoutStatus;
        this.totalPayout = totalPayout;
        this.planTypeList = planTypeList;
        this.exportType = exportType;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getExportType() {
        return exportType;
    }

    public void setExportType(int exportType) {
        this.exportType = exportType;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public String getMilestoneType() {
        return milestoneType;
    }

    public void setMilestoneType(String milestoneType) {
        this.milestoneType = milestoneType;
    }

    public String getMilestoneAgeRange() {
        return milestoneAgeRange;
    }

    public void setMilestoneAgeRange(String milestoneAgeRange) {
        this.milestoneAgeRange = milestoneAgeRange;
    }

    public String getMilestoneStatus() {
        return milestoneStatus;
    }

    public void setMilestoneStatus(String milestoneStatus) {
        this.milestoneStatus = milestoneStatus;
    }

    public String getMilestoneAmount() {
        return milestoneAmount;
    }

    public void setMilestoneAmount(String milestoneAmount) {
        this.milestoneAmount = milestoneAmount;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPaymentAgeRange() {
        return paymentAgeRange;
    }

    public void setPaymentAgeRange(String paymentAgeRange) {
        this.paymentAgeRange = paymentAgeRange;
    }

    public String getPremiumStatus() {
        return premiumStatus;
    }

    public void setPremiumStatus(String premiumStatus) {
        this.premiumStatus = premiumStatus;
    }

    public String getTotalPremiumPayment() {
        return totalPremiumPayment;
    }

    public void setTotalPremiumPayment(String totalPremiumPayment) {
        this.totalPremiumPayment = totalPremiumPayment;
    }

    public String getPayoutAgeRange() {
        return payoutAgeRange;
    }

    public void setPayoutAgeRange(String payoutAgeRange) {
        this.payoutAgeRange = payoutAgeRange;
    }

    public String getPayoutStatus() {
        return payoutStatus;
    }

    public void setPayoutStatus(String payoutStatus) {
        this.payoutStatus = payoutStatus;
    }

    public String getTotalPayout() {
        return totalPayout;
    }

    public void setTotalPayout(String totalPayout) {
        this.totalPayout = totalPayout;
    }

    public List<ExportModel> getPlanTypeList() {
        return planTypeList;
    }

    public void setPlanTypeList(List<ExportModel> planTypeList) {
        this.planTypeList = planTypeList;
    }
}
