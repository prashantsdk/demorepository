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


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 2017 on 11/25/2015.
 * Get login info
 * Author pramod.shelke
 */
public class UpdateGCM extends SmartCookieTeacherService {


    String Stud_id="",Stud_GCM_id="";
    private String _TAG = this.getClass().getSimpleName();



    public UpdateGCM(String Stud_id, String Stud_GCM_id) {


        this.Stud_id = Stud_id;
        this.Stud_GCM_id = Stud_GCM_id;


    }

    @Override
    protected String formRequest() {

        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.


                STUDENT_UPDATE_GCM;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_GCM_STUD_ID, Stud_id);
            requestBody.put(WebserviceConstants.KEY_GCM_ID, Stud_GCM_id);
            requestBody.put(WebserviceConstants.KEY_GCM_ENTITY_ID, 103);

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
        Log.i(_TAG, responseJSONString.toString());
        Log.i(_TAG, "In parseResponse");

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


                    String TRANSN_FLAG="False";
                    String REQUEST_ID="0";
                    String TRANSN_ID="0";

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
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
        notifier.eventNotify(EventTypes.EVENT_GCM_RESPONCE_RECIEVED, eventObject);

    }
}

