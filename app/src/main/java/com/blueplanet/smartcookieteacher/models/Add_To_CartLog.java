package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 14-03-2016.
 */
public class Add_To_CartLog
{
    private String _couImage = null;
    private String _couPointsPerProduct = null;
    private String _couponValidity = null;


    public Add_To_CartLog( String couImage,String couPointsPerProduct,String couponValidity){

        _couImage=couImage;
        _couPointsPerProduct=couPointsPerProduct;
        _couponValidity=couponValidity;

    }

    public String get_couImage() {
        return _couImage;
    }

    public String get_couPointsPerProduct() {
        return _couPointsPerProduct;
    }

    public String get_couponValidity() {
        return _couponValidity;
    }
}
