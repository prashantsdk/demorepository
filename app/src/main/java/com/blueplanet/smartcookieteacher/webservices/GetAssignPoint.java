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
 * Created by 1311 on 29-12-2015.
 */
public class GetAssignPoint extends SmartCookieTeacherService {

    private String _t_id, _studentId;
    private String _std_prn, _method_id, _subject_id, _reward_value,
            _user_date,_pointtype,_comment;
    private String _activity_id = null;

    private final String _TAG = this.getClass().getSimpleName();


    public GetAssignPoint(String t_id, String studentId, String std_prn, String method_id, String activity_id,
                          String subject_id, String reward_value, String user_date,String pointtype,String comment) {
        _t_id = t_id;
        _studentId = studentId;
        _std_prn = std_prn;
        _method_id = method_id;
        _activity_id = activity_id;
        _subject_id = subject_id;
        _reward_value = reward_value;
        _user_date = user_date;
        _pointtype=pointtype;
        _comment=comment;
    }

    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.TEACHER_ASSIGN_POINT;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_TID, _t_id);
            requestBody.put(WebserviceConstants.KEY_SCHOOLID, _studentId);
            requestBody.put(WebserviceConstants.KEY_USER_STD_PRN, _std_prn);
            requestBody.put(WebserviceConstants.KEY_METHOD_ID, _method_id);
            requestBody.put(WebserviceConstants.KEY_ACTIVITY_ID, _activity_id);
            requestBody.put(WebserviceConstants.KEY_SUBJECT_IDI, _subject_id);
            requestBody.put(WebserviceConstants.KEY_REWARD_VALUE, _reward_value);
            requestBody.put(WebserviceConstants.KEY_USER_DATE, _user_date);
            requestBody.put(WebserviceConstants.KEY_USER_POINT_TYPE, _pointtype);
            requestBody.put(WebserviceConstants.KEY_USER_POINT_COMMENT, _comment);


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
        String dd=null;
        Log.i(_TAG, responseJSONString.toString());
        try {
            objResponseJSON = new JSONObject(responseJSONString);
            statusCode = objResponseJSON.getInt(WebserviceConstants.KEY_STATUS_CODE);
            statusMessage =
                    objResponseJSON.getString(WebserviceConstants.KEY_STATUS_MESSAGE);

            if (statusCode == HTTPConstants.HTTP_COMM_SUCCESS) {
                // success
                dd=responseJSONString.toString();
               /* JSONArray responseData = objResponseJSON.optJSONArray(WebserviceConstants.KEY_POSTS);
                for (int i = 0; i < responseData.length(); i++) {
                    JSONObject jsonObject = responseData.optJSONObject(i);
                  //  String json = jsonObject.toJson(obj);
                   dd=responseJSONString.toString();

                }*/
                responseObject = new ServerResponse(errorCode, dd);

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

