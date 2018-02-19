package com.blueplanet.smartcookieteacher.models;

/**
 * Created by prashantj on 2/19/2018.
 */

public class ArtActivity {

      public String activityId;
      public String activityName;

    public ArtActivity(String activityId, String activityName) {
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
