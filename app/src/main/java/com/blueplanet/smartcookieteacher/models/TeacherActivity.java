package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 31-12-2015.
 */
public class TeacherActivity {

    private String _activityId = null;
    private String _activityName = null;
    private String _activity_type = null;

    /**
     * constructor
     *
     * @param sc_id
     * @param sc_list
     * @param activity_type
     */
    public TeacherActivity(String sc_id, String sc_list, String activity_type) {
        _activityId = sc_id;
        _activityName = sc_list;
        _activity_type = activity_type;

    }

    public String getActivityId() {
        return _activityId;
    }

    public String getActivityName() {
        return _activityName;
    }

    public String getActivityType() {
        return _activity_type;
    }

}
