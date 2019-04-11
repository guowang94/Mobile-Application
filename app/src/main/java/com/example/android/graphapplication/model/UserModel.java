package com.example.android.graphapplication.model;

public class UserModel {

    private int id;
    private String name;
    private int age;
    private int expectedRetirementAge;
    private int expectancy;
    private String jobStatus;
    private String citizenship;
    private float monthlyIncome;
    private float expenses;
    private float initialAssets;
    private float totalAssets;
    private float ordinaryAccount;
    private float specialAccount;
    private float medisaveAccount;
    private int shortfallAge;
    private float shortfall;
    private float balance;
    private int increment;
    private int inflation;
    private int expensesExceededIncomeAge;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getExpectedRetirementAge() {
        return expectedRetirementAge;
    }

    public void setExpectedRetirementAge(int expectedRetirementAge) {
        this.expectedRetirementAge = expectedRetirementAge;
    }

    public int getExpectancy() {
        return expectancy;
    }

    public void setExpectancy(int expectancy) {
        this.expectancy = expectancy;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public float getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(float monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public float getExpenses() {
        return expenses;
    }

    public void setExpenses(float expenses) {
        this.expenses = expenses;
    }

    public float getInitialAssets() {
        return initialAssets;
    }

    public void setInitialAssets(float initialAssets) {
        this.initialAssets = initialAssets;
    }

    public float getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(float totalAssets) {
        this.totalAssets = totalAssets;
    }

    public float getOrdinaryAccount() {
        return ordinaryAccount;
    }

    public void setOrdinaryAccount(float ordinaryAccount) {
        this.ordinaryAccount = ordinaryAccount;
    }

    public float getSpecialAccount() {
        return specialAccount;
    }

    public void setSpecialAccount(float specialAccount) {
        this.specialAccount = specialAccount;
    }

    public float getMedisaveAccount() {
        return medisaveAccount;
    }

    public void setMedisaveAccount(float medisaveAccount) {
        this.medisaveAccount = medisaveAccount;
    }

    public int getShortfallAge() {
        return shortfallAge;
    }

    public void setShortfallAge(int shortfallAge) {
        this.shortfallAge = shortfallAge;
    }

    public float getShortfall() {
        return shortfall;
    }

    public void setShortfall(float shortfall) {
        this.shortfall = shortfall;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public int getInflation() {
        return inflation;
    }

    public void setInflation(int inflation) {
        this.inflation = inflation;
    }

    public int getExpensesExceededIncomeAge() {
        return expensesExceededIncomeAge;
    }

    public void setExpensesExceededIncomeAge(int expensesExceededIncomeAge) {
        this.expensesExceededIncomeAge = expensesExceededIncomeAge;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", expectedRetirementAge=" + expectedRetirementAge +
                ", expectancy=" + expectancy +
                ", jobStatus='" + jobStatus + '\'' +
                ", citizenship='" + citizenship + '\'' +
                ", monthlyIncome=" + monthlyIncome +
                ", expenses=" + expenses +
                ", initialAssets=" + initialAssets +
                ", totalAssets=" + totalAssets +
                ", ordinaryAccount=" + ordinaryAccount +
                ", specialAccount=" + specialAccount +
                ", medisaveAccount=" + medisaveAccount +
                ", shortfallAge=" + shortfallAge +
                ", shortfall=" + shortfall +
                ", balance=" + balance +
                ", increment=" + increment +
                ", inflation=" + inflation +
                ", expensesExceededIncomeAge=" + expensesExceededIncomeAge +
                '}';
    }
}
