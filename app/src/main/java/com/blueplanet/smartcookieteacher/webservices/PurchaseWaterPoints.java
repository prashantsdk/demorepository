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
public class PurchaseWaterPoints extends SmartCookieTeacherService {


    String Stud_coupon_id="",Stud_Prn="",School_Id="";
    private String _TAG = this.getClass().getSimpleName();



    public PurchaseWaterPoints(String Stud_coupon_id, String Stud_Prn, String School_Id) {

        this.Stud_coupon_id=Stud_coupon_id;
        this.Stud_Prn = Stud_Prn;
        this.School_Id = School_Id;



    }

    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.PURCHASEWATERPOINTS;

    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_PURCHASE_WATER_POINTS_COUPON_ID, Stud_coupon_id);
            requestBody.put(WebserviceConstants.KEY_PURCHASE_WATER_POINTS_STD_PRN, Stud_Prn);
            requestBody.put(WebserviceConstants.KEY_PURCHASE_WATER_POINTS_SCHOOL_ID,School_Id );


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

                String Successmsg = responseJSONString.toString();
                /**
                 send sponsor object as response
                 */


                String TRANSN_FLAG = "False";
                String REQUEST_ID = "0";
                String TRANSN_ID = "0";


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
        notifier.eventNotify(EventTypes.EVENT_STUDENT_WATER_POINTS_PURCHASED_RESPONCE_RECIEVED, eventObject);
    }
}

