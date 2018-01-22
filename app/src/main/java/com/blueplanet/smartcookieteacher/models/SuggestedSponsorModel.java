package com.blueplanet.smartcookieteacher.models;

/**
 * Created by Avik1 on 20/02/2016.
 */
public class SuggestedSponsorModel {

   private String SPONSOR_ID =null;
    private  String SPONSOR_NAME = null;
    private   String SPONSOR_ADDRESS = null;
    private   String SPONSOR_EMAIL = null;
    private int SPONSOR_LIKES = 0;
    private  String SPONSOR_KILOMETERS = null;
    private String SPONSOR_LIKE_STATUS = null;
    private int id;





    public SuggestedSponsorModel(String _SPONSOR_ID, String _SPONSOR_NAME, String _SPONSOR_ADDRESS, String _SPONSOR_EMAIL,
                                 int _SPONSOR_LIKES, String _SPONSOR_KILOMETERS, String _SPONSOR_LIKE_STATUS){



        SPONSOR_ID=_SPONSOR_ID;
        SPONSOR_NAME=_SPONSOR_NAME;
        SPONSOR_ADDRESS=_SPONSOR_ADDRESS;
        SPONSOR_EMAIL=_SPONSOR_EMAIL;
        SPONSOR_LIKES=_SPONSOR_LIKES;
        SPONSOR_KILOMETERS=_SPONSOR_KILOMETERS;
        SPONSOR_LIKE_STATUS=_SPONSOR_LIKE_STATUS;

    }

    public String getSPONSOR_ID() {
        return SPONSOR_ID;
    }

    public String getSPONSOR_NAME() {
        return SPONSOR_NAME;
    }

    public String getSPONSOR_ADDRESS() {
        return SPONSOR_ADDRESS;
    }

    public String getSPONSOR_EMAIL() {
        return SPONSOR_EMAIL;
    }

    public int getSPONSOR_LIKES() {
        return SPONSOR_LIKES;
    }

    public String getSPONSOR_KILOMETERS() {
        return SPONSOR_KILOMETERS;
    }

    public String getSPONSOR_LIKE_STATUS() {
        return SPONSOR_LIKE_STATUS;
    }
}
