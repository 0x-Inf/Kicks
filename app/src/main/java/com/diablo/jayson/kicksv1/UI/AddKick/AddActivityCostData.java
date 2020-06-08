package com.diablo.jayson.kicksv1.UI.AddKick;

import java.io.Serializable;

public class AddActivityCostData implements Serializable {

    private String activityCost;

    public AddActivityCostData() {
    }

    public AddActivityCostData(String activityCost) {
        this.activityCost = activityCost;
    }

    public String getActivityCost() {
        return activityCost;
    }

    public void setActivityCost(String activityCost) {
        this.activityCost = activityCost;
    }
}
