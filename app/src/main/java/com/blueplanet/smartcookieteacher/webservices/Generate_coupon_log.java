package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.Buy_Coupon_log;
import com.blueplanet.smartcookieteacher.models.GenerateCouponLog;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 1311 on 04-04-2016.
 */
public class Generate_coupon_log extends SmartCookieTeacherService {


    private String _tID;
    private final String _TAG = this.getClass().getSimpleName();
    public Generate_coupon_log( String tID
    ) {

        _tID = tID;

    }
    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.GENERATE_COUPON_LOG_WEB_SERVICE;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_TId, _tID);

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
        GenerateCouponLog _genCou = null;
        ArrayList<GenerateCouponLog> genCouList = new ArrayList<>();


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

                    String sp_coupon_point = jsonObject.optString(WebserviceConstants.KEY_COULOG_COUPONPOINT);
                    String gen_coup_id = jsonObject.optString(WebserviceConstants.KEY_GENCOUPON_ID);
                    String gen_coup_status= jsonObject.optString(WebserviceConstants.KEY_COUPON_STATUS);
                    String gen_coup_validity_date = jsonObject.optString(WebserviceConstants.KEY_COUP_VALIDITY_DATE);
                    String gen_coup_isshu= jsonObject.optString(WebserviceConstants.KEY_COUP_ISSUE_DATE);


                    _genCou = new GenerateCouponLog(sp_coupon_point, gen_coup_id, gen_coup_status,gen_coup_validity_date,
                            gen_coup_isshu);
                    genCouList.add(_genCou);


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
        notifier.eventNotify(EventTypes.EVENT_GENERATE_COUPON_LOG, eventObject);
    }
    }

