package com.example.android.graphapplication.viewHolder;

public class SummaryViewHolder {

    public static final int SECTION_HEADER = 0;
    public static final int CONTENT = 1;

    private int image;
    private String title;
    private String value;
    private String sectionTitle;
    private int cellType;

    public SummaryViewHolder(int image, String title, String value, int cellType) {
        this.image = image;
        this.title = title;
        this.value = value;
        this.cellType = cellType;
    }

    public SummaryViewHolder(String sectionTitle, int cellType) {
        this.sectionTitle = sectionTitle;
        this.cellType = cellType;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public int getCellType() {
        return cellType;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
}
