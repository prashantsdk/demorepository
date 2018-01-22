package com.blueplanet.smartcookieteacher.models;

/**
 * Created by Sayali on 7/24/2017.
 */
public class SelectedCategoryModelClass {

    static private String _SelectedValue = null;

    public SelectedCategoryModelClass(String SelectedValue) {

        _SelectedValue=SelectedValue;
    }

    public static String get_SelectedValue() {
        return _SelectedValue;
    }

    public static void set_SelectedValue(String _SelectedValue) {
        SelectedCategoryModelClass._SelectedValue = _SelectedValue;
    }
}
