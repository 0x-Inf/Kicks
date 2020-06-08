package com.diablo.jayson.kicksv1.UI.AddKick;

import java.io.Serializable;

public class AddActivityPeopleData implements Serializable {

    private int activityMinRequiredPeople;
    private int activityMaxRequiredPeople;
    private int activityMinAge;
    private int activityMaxAge;
    private boolean isActivityPrivate;

    public AddActivityPeopleData() {
    }

    public AddActivityPeopleData(int activityMinRequiredPeople, int activityMaxRequiredPeople, int activityMinAge, int activityMaxAge, boolean isActivityPrivate) {
        this.activityMinRequiredPeople = activityMinRequiredPeople;
        this.activityMaxRequiredPeople = activityMaxRequiredPeople;
        this.activityMinAge = activityMinAge;
        this.activityMaxAge = activityMaxAge;
        this.isActivityPrivate = isActivityPrivate;
    }

    public int getActivityMinRequiredPeople() {
        return activityMinRequiredPeople;
    }

    public void setActivityMinRequiredPeople(int activityMinRequiredPeople) {
        this.activityMinRequiredPeople = activityMinRequiredPeople;
    }

    public int getActivityMaxRequiredPeople() {
        return activityMaxRequiredPeople;
    }

    public void setActivityMaxRequiredPeople(int activityMaxRequiredPeople) {
        this.activityMaxRequiredPeople = activityMaxRequiredPeople;
    }

    public int getActivityMinAge() {
        return activityMinAge;
    }

    public void setActivityMinAge(int activityMinAge) {
        this.activityMinAge = activityMinAge;
    }

    public int getActivityMaxAge() {
        return activityMaxAge;
    }

    public void setActivityMaxAge(int activityMaxAge) {
        this.activityMaxAge = activityMaxAge;
    }

    public boolean isActivityPrivate() {
        return isActivityPrivate;
    }

    public void setActivityPrivate(boolean activityPrivate) {
        isActivityPrivate = activityPrivate;
    }
}
