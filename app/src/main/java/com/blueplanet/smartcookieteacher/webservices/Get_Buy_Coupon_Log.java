package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.AddCart;
import com.blueplanet.smartcookieteacher.models.Buy_Coupon_log;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 1311 on 21-03-2016.
 */
public class Get_Buy_Coupon_Log extends SmartCookieTeacherService {

    private String _entity, _userid,_couFlag;
    private final String _TAG = this.getClass().getSimpleName();
    public Get_Buy_Coupon_Log( String entity, String userid,String couFlag
    ) {

        _entity = entity;
        _userid = userid;
        _couFlag=couFlag;

    }


    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.BUY_COUPON_LOG;
    }

    @Override
    protected JSONObject formRequestBody() {

        JSONObject requestBody = new JSONObject();
        try {

            requestBody.put(WebserviceConstants.KEY_ENTITY, _entity);
            requestBody.put(WebserviceConstants.KEY_USER_ID, _userid);
            requestBody.put(WebserviceConstants.KEY_COUPON_FLAG, _couFlag);


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
        Buy_Coupon_log _buyCou = null;
        ArrayList<Buy_Coupon_log> buyCouList = new ArrayList<>();


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

                    String sp_coupon_id = jsonObject.optString(WebserviceConstants.KEY_COUP_ID);
                    Log.i(_TAG,"couID"+sp_coupon_id);
                    String name = jsonObject.optString(WebserviceConstants.KEY_COUPLOG_NAME);
                    String image = jsonObject.optString(WebserviceConstants.KEY_COULOG_IMAGE);
                    String points = jsonObject.optString(WebserviceConstants.KEY_COUPLOG_POINTS);
                    String validity = jsonObject.optString(WebserviceConstants.KEY_COUPLOG_VALIDITY);
                    String code = jsonObject.optString(WebserviceConstants.KEY_COUPLOG_CODE);


                 _buyCou = new Buy_Coupon_log(name, image, points,validity,code);
                    buyCouList.add(_buyCou);


                }
                responseObject = new ServerResponse(errorCode, buyCouList);

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
        notifier.eventNotify(EventTypes.EVENT_BUY_LOG_COUPON, eventObject);
    }
    }

