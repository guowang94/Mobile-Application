package com.example.android.graphapplication;

import android.content.Context;
import android.util.Log;

import com.example.android.graphapplication.constants.KeyConstants;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class DAOFile {
    /**
     * This method will return the file content
     *
     * @param context of the application
     * @param filename name of the file
     * @return String
     */
    public String readFile(Context context, String filename) {
        try {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method will split the File Content into key and value
     *
     * @param fileContent file that store raw data
     * @return HashMap
     */
    public HashMap<String, String> splitFileContent(String fileContent) {
        Log.d("", "splitFileContent: " + fileContent);
        String[] value = fileContent.split("//");
        HashMap<String, String> content = new HashMap<>();

        for (String val : value) {
            content.put(val.split(":")[0], val.split(":")[1].trim());
        }

        return content;
    }

    /**
     * This method will save the fileContent
     * @param fileContent
     * @param context
     */
    public void saveDate(String fileContent, Context context) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(
                    KeyConstants.FILE_USER_INFO, MODE_PRIVATE);
            fileOutputStream.write(fileContent.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
