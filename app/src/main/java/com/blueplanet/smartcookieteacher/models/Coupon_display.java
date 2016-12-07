package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 18-01-2016.
 */
public class Coupon_display {

    private int _coupoin_id = -1;
    private String _sp_product = null;
    private String _points_per_product = null;
    private String _currency = null;
    private String _image_path = null;
    private String _sp_categorie = null;
    private  String _discount=null;
    private  String comp=null;



    public Coupon_display(int coupoin_id,String sp_product,String points_per_product,
                    String currency,String image_path,String sp_categorie,String discount,String compe){


        _coupoin_id=coupoin_id;
        _sp_product=sp_product;
        _points_per_product=points_per_product;
        _currency=currency;
        _image_path=image_path;
        _sp_categorie=sp_categorie;
        _discount=discount;
        comp=compe;


    }

    public int get_coupoin_id() {
        return _coupoin_id;
    }

    public String get_sp_product() {
        return _sp_product;
    }

    public String get_points_per_product() {
        return _points_per_product;
    }

    public String get_currency() {
        return _currency;
    }

    public String get_image_path() {
        return _image_path;
    }

    public String get_sp_categorie() {
        return _sp_categorie;
    }

    public String get_discount() {
        return _discount;
    }

    public String getComp() {
        return comp;
    }
}
