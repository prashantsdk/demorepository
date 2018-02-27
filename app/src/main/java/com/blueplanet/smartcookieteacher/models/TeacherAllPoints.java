package com.blueplanet.smartcookieteacher.models;

/**
 * Created by prashantj on 2/14/2018.
 */

public class TeacherAllPoints {

    public int _greenpoint = -1;
    public int _waterpoint = -1;
    public int _bluepoint = -1;
    public int _brownpoint = -1;


    public TeacherAllPoints(int _greenpoint, int _waterpoint, int _bluepoint, int _brownpoint) {
        this._greenpoint = _greenpoint;
        this._waterpoint = _waterpoint;
        this._bluepoint = _bluepoint;
        this._brownpoint = _brownpoint;
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
}
