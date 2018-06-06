package com.example.android.graphapplication.Validations;

import android.util.Log;

import java.util.regex.Pattern;

public class Validation {

    private static final String TAG = "Validation";

    String charRegex = "[A-Za-z ]{2,}";

    public boolean matchCharOnly(String str) {
        return Pattern.matches(charRegex, str);
    }
}