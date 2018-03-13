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
    private String balancePointType = null;

    public GenerateCoupon(String _couID, String _couPoint, String _couIssueDate, String _couValidityDate, String _couBalancePoint, String balancePointType) {
        this._couID = _couID;
        this._couPoint = _couPoint;
        this._couIssueDate = _couIssueDate;
        this._couValidityDate = _couValidityDate;
        this._couBalancePoint = _couBalancePoint;
        this.balancePointType = balancePointType;
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

    public String getBalancePointType() {
        return balancePointType;
    }
}
