package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 24-02-2016.
 */
public class BalancePointModelClass {

    static private String _couValue = null;

    public BalancePointModelClass(String couValue) {

        _couValue=couValue;
    }

    public static String get_couValue() {
        return _couValue;
    }

    public static void set_couValue(String _couValue) {
        BalancePointModelClass._couValue = _couValue;
    }
}
