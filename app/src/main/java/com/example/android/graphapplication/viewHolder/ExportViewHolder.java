package com.example.android.graphapplication.viewHolder;

import java.util.List;

public class ExportViewHolder {

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
    private List<ExportViewHolder> planTypeList;

    /**
     * This constructor is for (Report Title's timestamp / Report Header)
     *
     * @param header (Report Title's timestamp / Report Header)
     * @param exportType ExportType
     */
    public ExportViewHolder(String header, int exportType) {
        this.header = header;
        this.exportType = exportType;
    }

    /**
     * This constructor is for (Total Section & Total Value / Client Detail's title & value / Plan Detail's title & value)
     *
     * @param title Title
     * @param value Value
     * @param exportType ExportType
     */
    public ExportViewHolder(String title, String value, int exportType) {
        this.title = title;
        this.value = value;
        this.exportType = exportType;
    }

    public ExportViewHolder(String milestoneName, String milestoneType, String milestoneAgeRange, String milestoneStatus, String milestoneAmount, int exportType) {
        this.milestoneName = milestoneName;
        this.milestoneType = milestoneType;
        this.milestoneAgeRange = milestoneAgeRange;
        this.milestoneStatus = milestoneStatus;
        this.milestoneAmount = milestoneAmount;
        this.exportType = exportType;
    }

    public ExportViewHolder(String planName, String paymentAgeRange, String premiumStatus, String totalPremiumPayment, String payoutAgeRange, String payoutStatus, String totalPayout, List<ExportViewHolder> planTypeList, int exportType) {
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

//    public void setTitle(String title) {
//        this.title = title;
//    }

    public String getValue() {
        return value;
    }

//    public void setValue(String value) {
//        this.value = value;
//    }

    public int getExportType() {
        return exportType;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public String getMilestoneType() {
        return milestoneType;
    }

    public String getMilestoneAgeRange() {
        return milestoneAgeRange;
    }

    public String getMilestoneStatus() {
        return milestoneStatus;
    }

    public String getMilestoneAmount() {
        return milestoneAmount;
    }

    public String getPlanName() {
        return planName;
    }

    public String getPaymentAgeRange() {
        return paymentAgeRange;
    }

    public String getPremiumStatus() {
        return premiumStatus;
    }

    public String getTotalPremiumPayment() {
        return totalPremiumPayment;
    }

    public String getPayoutAgeRange() {
        return payoutAgeRange;
    }

    public String getPayoutStatus() {
        return payoutStatus;
    }

    public String getTotalPayout() {
        return totalPayout;
    }

    public List<ExportViewHolder> getPlanTypeList() {
        return planTypeList;
    }
}
