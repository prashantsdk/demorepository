package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;


import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.BuyCoupon;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 1311 on 24-01-2016.
 */
public class GetBuyCoupon extends SmartCookieTeacherService {

    private String _t_id, _studentId;
    private String _entity, _userid, _couponid, _remaining_point;


    private final String _TAG = this.getClass().getSimpleName();

    public GetBuyCoupon(String studentId, String entity, String userid, String couponid
                        ) {

        _studentId = studentId;
        _entity = entity;
        _userid = userid;
        _couponid = couponid;


    }

    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.BUY_COUPON;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_SCHOOLID, _studentId);
            requestBody.put(WebserviceConstants.KEY_ENTITY, _entity);
            requestBody.put(WebserviceConstants.KEY_USER_ID, _userid);
            requestBody.put(WebserviceConstants.KEY_COUPON_ID, _couponid);


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

        BuyCoupon _buyCoupon = null;

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

                    String sp_coupon_code = jsonObject.optString(WebserviceConstants.KEY_COUPON_CODE);
                    String coupon_uid = jsonObject.optString(WebserviceConstants.KEY_COUPON_UID);
                    String coupon_for_point = jsonObject.optString(WebserviceConstants.KEY_COUPON_FOR_POINTS);
                    String coupon_id = jsonObject.optString(WebserviceConstants.KEY_BUYCOUPON_ID);
                    String remainingPoint = jsonObject.optString(WebserviceConstants.KEY_BUYCOUPON_ID);
                    _buyCoupon = new BuyCoupon(sp_coupon_code, coupon_uid, coupon_for_point, coupon_id, remainingPoint);
                }

                responseObject = new ServerResponse(errorCode, _buyCoupon);


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
        notifier.eventNotify(EventTypes.EVENT_COUPON_BUY_SUCCESS, eventObject);

    }
}

