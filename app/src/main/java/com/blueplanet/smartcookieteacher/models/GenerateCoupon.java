package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 23-02-2016.
 */
public class GenerateCoupon {

    private String _couID = null;
    private String _couPoint = null;
    private String _couIssueDate = null;
    private String _couValidityDate = null;
    private String _couBalancePoint = null;


    public GenerateCoupon(String couID, String couPoint, String couIssueDate, String couValidityDate, String couBalancePoint) {

        _couID = couID;
        _couPoint = couPoint;
        _couIssueDate = couIssueDate;
        _couValidityDate = couValidityDate;
        _couBalancePoint = couBalancePoint;

    }

    public String get_couID() {
        return _couID;
    }

    public String get_couPoint() {
        return _couPoint;
    }

    public String get_couIssueDate() {
        return _couIssueDate;
    }

    public String get_couValidityDate() {
        return _couValidityDate;
    }

    public String get_couBalancePoint() {
        return _couBalancePoint;
    }


}
