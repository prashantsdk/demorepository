package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 21-03-2016.
 */
public class Buy_Coupon_log {

    private String _couponLogName = null;
    private String _couLogImage = null;
    private String _couLogPointsPerProduct = null;
    private String _couponLogValidity = null;
    private String _couponLogcode = null;

    public Buy_Coupon_log(String couponLogName, String couLogImage, String couLogPointsPerProduct, String couponLogValidity,
                          String couponLogcode) {

        _couponLogName = couponLogName;
        _couLogImage = couLogImage;
        _couLogPointsPerProduct = couLogPointsPerProduct;
        _couponLogValidity = couponLogValidity;

        _couponLogcode = couponLogcode;
    }

    public String get_couLogImage() {
        return _couLogImage;
    }

    public String get_couLogPointsPerProduct() {
        return _couLogPointsPerProduct;
    }

    public String get_couponLogValidity() {
        return _couponLogValidity;
    }

    public String get_couponLogName() {
        return _couponLogName;
    }

    public String get_couponLogcode() {
        return _couponLogcode;
    }
}
