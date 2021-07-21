package com.diablo.jayson.kicksv1.UI.AddActivity;

public class Duration {

    private long durationTimeMs;
    private String durationName;

    public Duration() {
    }

    public Duration(long durationTimeMs, String durationName) {
        this.durationTimeMs = durationTimeMs;
        this.durationName = durationName;
    }

    public long getDurationTimeMs() {
        return durationTimeMs;
    }

    public void setDurationTimeMs(long durationTimeMs) {
        this.durationTimeMs = durationTimeMs;
    }

    public String getDurationName() {
        return durationName;
    }

    public void setDurationName(String durationName) {
        this.durationName = durationName;
    }
}
