package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.GenerateCoupon;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 1311 on 23-02-2016.
 */
public class GetGenerateCoupon extends SmartCookieTeacherService {

    private String _t_id, _couPoint,_option,_studentId;
    private int _inputId = -1;
    private final String _TAG = this.getClass().getSimpleName();


    /**
     * constructor
     *
     * @param tid,schoolid
     */
    public GetGenerateCoupon(String tid, String couPoint,String option,String studentId) {
        _t_id = tid;
        _couPoint = couPoint;
        _option=option;
        _studentId = studentId;

    }


    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.GENERATE_COUPON_LOG;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_TID, _t_id);
            requestBody.put(WebserviceConstants.KEY_COUPON_POINT, _couPoint);
            requestBody.put(WebserviceConstants.KEY_COUPON_OPTION, _option);
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
        GenerateCoupon _genCoupon = null;
        ArrayList<GenerateCoupon> genCouList = new ArrayList<>();
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
                    String couID = jsonObject.optString(WebserviceConstants.KEY_COU_ID);
                    String couPoint = jsonObject.optString(WebserviceConstants.KEY_COU_POINT);
                    String couIssueDate = jsonObject.optString(WebserviceConstants.KEY_ISSUE_DATE);
                    String couValidityDate = jsonObject.optString(WebserviceConstants.KEY_VALIDITY_DATE);
                    String couBalancePoint = jsonObject.optString(WebserviceConstants.KEY_COU_BALANCE_POINT);

                    _genCoupon = new GenerateCoupon(couID, couPoint, couIssueDate, couValidityDate, couBalancePoint);
                    genCouList.add(_genCoupon);


                }
                responseObject = new ServerResponse(errorCode, genCouList);

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
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_COUPON);
        notifier.eventNotify(EventTypes.EVENT_GENERATE_COUPON_RECEVIED, eventObject);
    }
}
