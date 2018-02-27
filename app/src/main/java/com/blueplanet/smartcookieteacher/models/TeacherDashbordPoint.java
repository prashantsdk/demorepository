package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 14-12-2015.
 */
public class TeacherDashbordPoint {


    private int _greenpoint = -1;
    private int _waterpoint = -1;
    private int _bluepoint = -1;
    private int _brownpoint = -1;



    public TeacherDashbordPoint(int greenpoint, int wateroint , int bluepoint, int brownpoint) {
        _greenpoint = greenpoint;

        _waterpoint = wateroint;
        _bluepoint = bluepoint;

        _brownpoint = brownpoint;
    }

    public int get_greenpoint() {
        return _greenpoint;
    }

    public int get_waterpoint() {
        return _waterpoint;
    }

    public int get_bluepoint() {
        return _bluepoint;
    }

    public int get_brownpoint() {
        return _brownpoint;
    }

    /*   public int get_bluepoint() {
        return _bluepoint;
    }

    public int get_brownpoint() {
        return _brownpoint;
    }

    public int get_waterpoint() {
        return _waterpoint;
    }

    public int get_greenpoint() {
        return _greenpoint;
    }

*/


}
