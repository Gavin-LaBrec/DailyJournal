package com.example.dailyjournal;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

public class Entry {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Entry(String date, String improveText, String gratitudeText) {
        this.date = date;
        this.improveText = improveText;
        this.gratitudeText = gratitudeText;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "date=" + date +
                ", improveText='" + improveText + '\'' +
                ", gratitudeText='" + gratitudeText + '\'' +
                '}';
    }

    public static String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImproveText() {
        return improveText;
    }

    public void setImproveText(String improveText) {
        this.improveText = improveText;
    }

    public String getGratitudeText() {
        return gratitudeText;
    }

    public void setGratitudeText(String gratitudeText) {
        this.gratitudeText = gratitudeText;
    }

    private static String date;
    private String improveText;
    private String gratitudeText;
}
