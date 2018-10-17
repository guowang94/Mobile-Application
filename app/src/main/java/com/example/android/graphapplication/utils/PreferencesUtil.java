package com.example.android.graphapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.graphapplication.constants.KeyConstants;

public class PreferencesUtil {

    public static void firstTimeAskingPermission(Context context, String permission, boolean isFirstTime){
        SharedPreferences sharedPreference = context.getSharedPreferences(KeyConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        sharedPreference.edit().putBoolean(permission, isFirstTime).apply();
    }
    public static boolean isFirstTimeAskingPermission(Context context, String permission){
        return context.getSharedPreferences(KeyConstants.PREFS_FILE_NAME, Context.MODE_PRIVATE).getBoolean(permission, true);
    }
}
