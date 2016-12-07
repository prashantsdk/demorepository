package com.blueplanet.smartcookieteacher.models;

/**
 * Created by 1311 on 09-01-2016.
 */
public class Category {

    private int _caID = -1;
    private String _categorie = null;


    public Category(int caID, String categorie){

       _caID=caID;
        _categorie=categorie;

    }

    public int get_caID() {
        return _caID;
    }

    public String get_categorie() {
        return _categorie;
    }


}
