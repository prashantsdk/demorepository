package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.SuggestedSponsorModel;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sayali on 7/21/2017.
 */
public class SuggestNewSponsor extends SmartCookieTeacherService {


    String USER_MEM_ID="", VENDOR_NAME="", VENDOR_CATEGORY="", VENDOR_EMAIL="", VENDOR_PHONE="",
            VENDOR_IMAGE="", VENDOR_IMAGE_BASE64="", VENDOR_ADDRESS="", VENDOR_CITY="",
            VENDOR_STATE="", VENDOR_COUNTRY="",VENDOR_LAT="",VENDOR_LONG="",VENDOR_ENTITY="",VERSION_VENDER="",PLATFORM_SOURSE="",
            _PLATFORM_LATITUDE="",_PLATFORM_LOGITITUDE=""
    ;


    private String _TAG = this.getClass().getSimpleName();



    public SuggestNewSponsor(String USER_MEM_ID,String  VENDOR_NAME,String VENDOR_CATEGORY,String  VENDOR_EMAIL,String  VENDOR_PHONE,String
            VENDOR_IMAGE,String  VENDOR_IMAGE_BASE64,String  VENDOR_ADDRESS,String  VENDOR_CITY,String
                                     VENDOR_STATE,String  VENDOR_COUNTRY,String VENDOR_LAT,String VENDOR_LONG,String VENDOR_ENTITY,
                           String  _VERSION_VENDER,String _PLATFORM_SOURSE,String PLATFORM_LATITUDE,String PLATFORM_LOGITITUDE) {


        this.USER_MEM_ID = USER_MEM_ID;
        this.VENDOR_NAME = VENDOR_NAME;
        this.VENDOR_EMAIL = VENDOR_EMAIL;
        this.VENDOR_PHONE = VENDOR_PHONE;
        this.VENDOR_IMAGE = VENDOR_IMAGE;
        this.VENDOR_IMAGE_BASE64 = VENDOR_IMAGE_BASE64;
        this.VENDOR_ADDRESS = VENDOR_ADDRESS;
        this.VENDOR_CITY = VENDOR_CITY;
        this.VENDOR_STATE = VENDOR_STATE;
        this.VENDOR_COUNTRY = VENDOR_COUNTRY;

        this.VENDOR_LAT = VENDOR_LAT;
        this.VENDOR_LONG = VENDOR_LONG;
        this.VENDOR_ENTITY = VENDOR_ENTITY;
        this.VENDOR_CATEGORY=VENDOR_CATEGORY;
        this.VERSION_VENDER=_VERSION_VENDER;
        this.PLATFORM_SOURSE=_PLATFORM_SOURSE;
        this._PLATFORM_LATITUDE=PLATFORM_LATITUDE;
        this._PLATFORM_LOGITITUDE=PLATFORM_LOGITITUDE;

    }
    @Override
    protected String formRequest() {

        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.STUDENT_SUGGEST_NEW_SPONSOR;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {


            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_USER_ID,USER_MEM_ID);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_NAME,VENDOR_NAME);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_CATEGORY,VENDOR_CATEGORY);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_PHONE,VENDOR_PHONE);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_EMAIL,VENDOR_EMAIL);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_ADDRESS,VENDOR_ADDRESS);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_CITY,VENDOR_CITY);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_STATE,VENDOR_STATE);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_COUNTRY,VENDOR_COUNTRY);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_LAT,VENDOR_LAT);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_LONG,VENDOR_LONG);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_ENTITY,VENDOR_ENTITY);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_IMAGE,VENDOR_IMAGE);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_IMAGE_BASE_64,VENDOR_IMAGE_BASE64);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_PLATFORM_SOURSE,VERSION_VENDER);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_VERSION,PLATFORM_SOURSE);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_LATITIDE,_PLATFORM_LATITUDE);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_NEW_SPONSOR_LOGITITUDE,_PLATFORM_LOGITITUDE);



        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestBody;
    }

    @Override
    protected void parseResponse(String responseJSONString) {
        int errorCode = WebserviceConstants.SUCCESS;
        ServerResponse responseObject = null;
        JSONObject objResponseJSON;
        int statusCode = -1;
        String statusMessage = null;
        SuggestedSponsorModel current_loc_model = null;
        ArrayList<SuggestedSponsorModel> arr_Sponsorlist = new ArrayList();
        Log.i(_TAG, responseJSONString.toString());
        try {
            objResponseJSON = new JSONObject(responseJSONString);

            statusCode = objResponseJSON.getInt(WebserviceConstants.KEY_STATUS_CODE);
            statusMessage =
                    objResponseJSON.getString(WebserviceConstants.KEY_STATUS_MESSAGE);

            Log.i(_TAG, responseJSONString.toString());

            if (statusCode == HTTPConstants.HTTP_COMM_SUCCESS) {
                Log.i(_TAG, responseJSONString.toString());
                // success

                String Successmsg=responseJSONString.toString();

                /**
                 send sponsor object as response
                 */


                responseObject = new ServerResponse(errorCode, Successmsg);
            } else {
                // failure
                errorCode = WebserviceConstants.FAILURE;
                responseObject =
                        new ServerResponse(errorCode, new ErrorInfo(statusCode, statusMessage,
                                null));
            }
            fireEvent(responseObject);

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void fireEvent(Object eventObject) {
        if (eventObject == null) {
            eventObject =
                    new ServerResponse(WebserviceConstants.FAILURE, new ErrorInfo(
                            HTTPConstants.HTTP_COMM_ERR_NETWORK_TIMEOUT, MainApplication
                            .getContext().getString(R.string.msg_the_request_timed_out),
                            null));
        }

        EventNotifier notifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        notifier.eventNotify(EventTypes.EVENT_SUGGEST_NEW_SPONSOR_RESPONCE_RECIEVED, eventObject);
    }
}



