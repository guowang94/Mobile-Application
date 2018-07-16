package com.example.android.graphapplication;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ReadFileData {
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
     * @param fileContent file that store the raw data
     * @return HashMap
     */
    public HashMap<String, String> splitFileContent(String fileContent) {
        String[] value = fileContent.split("//");
        HashMap<String, String> content = new HashMap<>();

        for (String val : value) {
            content.put(val.split(":")[0], val.split(":")[1].trim());
        }

        return content;
    }
}
