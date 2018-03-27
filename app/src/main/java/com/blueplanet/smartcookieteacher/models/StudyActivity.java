package com.blueplanet.smartcookieteacher.models;

/**
 * Created by Priyanka on 3/27/2018.
 */

public class StudyActivity {

    private String activityId;
    private String activityName;

    public StudyActivity(String activityId, String activityName) {
        this.activityId = activityId;
        this.activityName = activityName;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }
}
