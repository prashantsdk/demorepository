package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.RequestPointModel;
import com.blueplanet.smartcookieteacher.models.SubNameCode;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 1311 on 12-11-2016.
 */
public class AcceptRequest extends SmartCookieTeacherService {


    private String _t_id,_studentId,_studentprn;
    private final String _TAG = this.getClass().getSimpleName();

    /**
     * constructor
     *
     * @param studentId
     */
    public AcceptRequest(String t_id,String studentId) {

        _t_id=t_id;
        _studentId = studentId;

    }


    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.ACCEPT_REQUEST_LOG_WEB_SERVICE;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_TID, _t_id);
            requestBody.put(WebserviceConstants.KEY_SCHOOLID, _studentId);



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
        RequestPointModel _requestpoint = null;

        ArrayList<RequestPointModel> requestPointList = new ArrayList<>();
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

                    String NAME = jsonObject.optString(WebserviceConstants.KEY_REQUEST_NAME);
                    String POINTS = jsonObject.optString(WebserviceConstants.KEY_REQUEST_POINTS);
                    String DATE = jsonObject.optString(WebserviceConstants.KEY_REQUEST_DATE);
                    String REASON = jsonObject.optString(WebserviceConstants.KEY_REQUEST_REASON);
                    String image = jsonObject.optString(WebserviceConstants.KEY_REQUEST_IMAGE);
                    String type = jsonObject.optString(WebserviceConstants.KEY_REQUEST_TYPE);
                    String stuprn = jsonObject.optString(WebserviceConstants.KEY_REQUEST_STUDENT_PRN);
                    String reasonid = jsonObject.optString(WebserviceConstants.KEY_REQUEST_STUDENT_REASON_ID);
                    _requestpoint = new RequestPointModel(NAME, POINTS,DATE,REASON,image,type,stuprn,reasonid);
                    requestPointList.add(_requestpoint);
                }
                responseObject = new ServerResponse(errorCode, requestPointList);

            }  else {
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
        notifier.eventNotify(EventTypes.EVENT_ACCEPT_REQUEST, eventObject);

    }
}
