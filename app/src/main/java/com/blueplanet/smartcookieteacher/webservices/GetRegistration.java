package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.BlueLog;
import com.blueplanet.smartcookieteacher.models.RegisModel;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 1311 on 17-02-2016.
 */
public class GetRegistration extends SmartCookieTeacherService {

    private String _email, _tPassword;
    private String _userFname,_Lname,_Email,_Pass,_Phone,_mname,_countrycode,_type,_sourse;
    private final String _TAG = this.getClass().getSimpleName();

    public GetRegistration(String userFname, String Lname,String Email, String Pass,String Phone,String mname,String countrycode,String type,String sourse) {

        _userFname=userFname;
        _Lname=Lname;
        _Email=Email;
        _Pass=Pass;
        _Phone=Phone;
        _mname=mname;
        _countrycode=countrycode;
        _type=type;
        _sourse=sourse;
    }

    @Override
    protected String formRequest() {

        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.NEW_REGISTRATION;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_USER_FNAME, _userFname);
            requestBody.put(WebserviceConstants.KEY_USER_LNAME, _mname);
            requestBody.put(WebserviceConstants.KEY_USER_LNAME, _Lname);
            requestBody.put(WebserviceConstants.KEY_USER_EMAIL, _Email);
            requestBody.put(WebserviceConstants.KEY_USER_PASS, _Pass);
            requestBody.put(WebserviceConstants.KEY_USER_PHONE, _Phone);
            requestBody.put(WebserviceConstants.KEY_USER_PHONE_CODE, _countrycode);
            requestBody.put(WebserviceConstants.KEY_USER_TYPE, _type);
            requestBody.put(WebserviceConstants.KEY_USER_PLAT_SOURSE, _sourse);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Log.i(_TAG, requestBody.toString());
        return requestBody;
    }

    @Override
    protected void parseResponse(String responseJSONString) {
        int errorCode = WebserviceConstants.SUCCESS;
        ServerResponse responseObject = null;
        JSONObject objResponseJSON;
        int statusCode = -1;
        String statusMessage = null;
        RegisModel _register = null;


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
                    String tEmail = jsonObject.optString(WebserviceConstants.KEY_MEMBER_ID);
                    String tPass = jsonObject.optString(WebserviceConstants.KEY_TEACHER__ID);
                    _register = new RegisModel(tEmail, tPass);

                }
                responseObject = new ServerResponse(errorCode, _register);


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
        notifier.eventNotify(EventTypes.EVENT_REGISTRATION_SUCCESS, eventObject);

    }
    }

