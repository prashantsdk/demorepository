package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 04-04-2016.
 */
public class GenerateCouponLog {


    private String _couponPoint = null;
    private String _gencoupon_id = null;
    private String _gencoupon_status = null;

    private String _generate_validity_date = null;
    private String _gencoupon_issue_date= null;

    public GenerateCouponLog(String couponPoint, String gencoupon_id, String gencoupon_status,
                             String generate_validity_date,String gencoupon_issue_date) {

        _couponPoint = couponPoint;
        _gencoupon_id = gencoupon_id;
        _gencoupon_status = gencoupon_status;

        _generate_validity_date = generate_validity_date;
        _gencoupon_issue_date = gencoupon_issue_date;
    }


    public String get_couponPoint() {
        return _couponPoint;
    }

    public String get_gencoupon_id() {
        return _gencoupon_id;
    }

    public String get_gencoupon_issue_date() {
        return _gencoupon_issue_date;
    }

    public String get_gencoupon_status() {
        return _gencoupon_status;
    }

    public String get_generate_validity_date() {
        return _generate_validity_date;
    }
}
