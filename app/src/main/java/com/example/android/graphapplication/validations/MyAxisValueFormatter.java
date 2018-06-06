package com.example.android.graphapplication.validations;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.NumberFormat;
import java.util.Locale;

public class MyAxisValueFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String formattedValue = String.valueOf(NumberFormat.getCurrencyInstance(Locale.US).format(value));

        if (value <= -1000000000) {
            return formattedValue.substring(0, formattedValue.length() - 15) + "B";
        } else if (value > -1000000000 && value <= -1000000) {
            return formattedValue.substring(0, formattedValue.length() - 11) + "M";
        } else if (value > -1000000 && value <= -1000) {
            return formattedValue.substring(0, formattedValue.length() - 7) + "K";
        } else if (value > -1000 && value <= 1000) {
            return formattedValue.substring(0, formattedValue.length() - 3);
        } else if (value > 1000 && value <= 1000000) {
            return formattedValue.substring(0, formattedValue.length() - 7) + "K";
        } else if (value > 1000000 && value <= 1000000000) {
            return formattedValue.substring(0, formattedValue.length() - 11) + "M";
        } else {
            return formattedValue.substring(0, formattedValue.length() - 15) + "B";
        }
    }
}