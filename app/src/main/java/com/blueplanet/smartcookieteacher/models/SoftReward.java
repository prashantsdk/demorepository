package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 20-01-2017.
 */
public class SoftReward {

    private String _softid = null;
    private String _softRewardType = null;
    private String _range = null;
    private String _softimg= null;


    /**
     * constructor
     *
     *
     */
    public SoftReward(String softid, String softRewardType, String range,String softimg) {
        _softid = softid;
        _softRewardType = softRewardType;
        _range = range;
        _softimg = softimg;


    }

    public String get_softid() {
        return _softid;
    }

    public String get_softRewardType() {
        return _softRewardType;
    }

    public String get_softimg() {
        return _softimg;
    }

    public String get_range() {
        return _range;
    }
}
