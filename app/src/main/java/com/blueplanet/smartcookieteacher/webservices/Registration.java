package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;


import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 1311 on 08-12-2015.
 */
public class Registration extends SmartCookieTeacherService {

    private String _userFname,Lname,Email,Pass,Phone;
    private final String _TAG = this.getClass().getSimpleName();
    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.REGISTRATION;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {

            requestBody.put(WebserviceConstants.KEY_USER_FNAME, _userFname);
            requestBody.put(WebserviceConstants.KEY_USER_LNAME, Lname);
            requestBody.put(WebserviceConstants.KEY_USER_EMAIL, Email);
            requestBody.put(WebserviceConstants.KEY_USER_PASS, Pass);
            requestBody.put(WebserviceConstants.KEY_USER_PHONE, Phone);


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
                }
                responseObject = new ServerResponse(errorCode, null);

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
        notifier.eventNotify(EventTypes.EVENT_TEACHER_ASSIGN_POINT, eventObject);
    }
    }

