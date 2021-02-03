package com.diablo.jayson.kicksv1.UI.AddActivity;

public class DurationExample {

    private long durationTimeMs;
    private String durationName;

    public DurationExample() {
    }

    public DurationExample(long durationTimeMs, String durationName) {
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
