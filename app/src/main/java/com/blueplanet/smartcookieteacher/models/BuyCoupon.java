package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 24-01-2016.
 */
public class BuyCoupon {

    private String _uid = null;
    private String _couponCode = null;
    private String _couponPoint = null;
    private String _buycouponid = null;
    private String _remainingPoint= null;

    public BuyCoupon( String uid,String couponCode,String couponPoint,String buycouponid,String remainingPoint){

        _uid=uid;
        _couponCode=couponCode;
        _couponPoint=couponPoint;
        _buycouponid=buycouponid;
_buycouponid=buycouponid;

    }

    public String get_uid() {
        return _uid;
    }

    public String get_couponCode() {
        return _couponCode;
    }

    public String get_couponPoint() {
        return _couponPoint;
    }

    public String get_buycouponid() {
        return _buycouponid;
    }

    public String get_remainingPoint() {
        return _remainingPoint;
    }
}
