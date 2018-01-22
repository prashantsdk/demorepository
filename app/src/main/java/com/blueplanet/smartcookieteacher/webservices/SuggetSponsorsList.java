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
 * Created by 2017 on 11/25/2015.
 * Get login info
 * Author pramod.shelke
 */
public class SuggetSponsorsList extends SmartCookieTeacherService {



    String ENTITY="";
    String USER_ID="";
    String LATITUDE="";
    String LONGITUDE="";
    String CATAGORY="";
    String COUNTRY="";
    String STATE="";
    String CITY="";
    private String _TAG = this.getClass().getSimpleName();

    public SuggetSponsorsList(String ENTITY, String USER_ID, String LATITUDE, String LONGITUDE,
                              String CATAGORY, String COUNTRY, String STATE, String CITY) {

        this.ENTITY = ENTITY;
        this.USER_ID = USER_ID;
        this.LATITUDE = LATITUDE;
        this.LONGITUDE = LONGITUDE;
        this.CATAGORY = CATAGORY;
        this.COUNTRY = COUNTRY;
        this.STATE = STATE;
        this.CITY = CITY;


    }

    @Override
    protected String formRequest() {

        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.STUDENT_SUGGEST_SPONSOR_LIST;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_SUGGEST_SPONSOR_ENTITY, ENTITY);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_SPONSOR_USER_ID, USER_ID);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_SPONSOR_LATITUDE, LATITUDE);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_SPONSOR_LONGITUDE, LONGITUDE);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_SPONSOR_CATAGORY, CATAGORY);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_SPONSOR_IS_LOC, "");
            requestBody.put(WebserviceConstants.KEY_SUGGEST_SPONSOR_COUNTRY, COUNTRY);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_SPONSOR_STATE, STATE);
            requestBody.put(WebserviceConstants.KEY_SUGGEST_SPONSOR_CITY, CITY);
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

            if (statusCode == HTTPConstants.HTTP_COMM_SUCCESS) {

                // success
                JSONArray responseData = objResponseJSON.optJSONArray(WebserviceConstants.KEY_POSTS);
                for (int i = 0; i < responseData.length(); i++) {
                    JSONObject jsonObject = responseData.optJSONObject(i);
                    String KEY_SUGGEST_SPONSOR_ID = jsonObject.optString(WebserviceConstants.KEY_SUGGEST_SPONSOR_ID);
                    String KEY_SUGGEST_SPONSOR_NAME = jsonObject.optString(WebserviceConstants.KEY_SUGGEST_SPONSOR_NAME);
                    String KEY_SUGGEST_SPONSOR_ADDRESS = jsonObject.optString(WebserviceConstants.KEY_SUGGEST_SPONSOR_ADDRESS);
                    String KEY_SUGGEST_SPONSOR_EMAIL = jsonObject.optString(WebserviceConstants.KEY_SUGGEST_SPONSOR_EMAIL);
                    int KEY_SUGGEST_SPONSOR_LIKES = jsonObject.optInt(WebserviceConstants.KEY_SUGGEST_SPONSOR_LIKES);
                    String KEY_SUGGEST_SPONSOR_KILOMETERS = jsonObject.optString(WebserviceConstants.KEY_SUGGEST_SPONSOR_KILOMETERS);
                    String KEY_SUGGEST_SPONSOR_LIKE_STATUS = jsonObject.optString(WebserviceConstants.KEY_SUGGEST_SPONSOR_LIKE_STATUS);
                    KEY_SUGGEST_SPONSOR_KILOMETERS=KEY_SUGGEST_SPONSOR_KILOMETERS.replace("'","");

                    current_loc_model=new SuggestedSponsorModel(KEY_SUGGEST_SPONSOR_ID,KEY_SUGGEST_SPONSOR_NAME,
                            KEY_SUGGEST_SPONSOR_ADDRESS,KEY_SUGGEST_SPONSOR_EMAIL,KEY_SUGGEST_SPONSOR_LIKES,KEY_SUGGEST_SPONSOR_KILOMETERS,
                            KEY_SUGGEST_SPONSOR_LIKE_STATUS);

                    arr_Sponsorlist.add(current_loc_model);
                }
                responseObject = new ServerResponse(errorCode, arr_Sponsorlist);

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
        notifier.eventNotify(EventTypes.EVENT_SUGGESTED_SPONSOR_RESPONCE_RECIEVED, eventObject);
    }
}

