package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;


import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.Coupon_display;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 1311 on 12-01-2016.
 */
public class GetDisplayCupon extends SmartCookieTeacherService {
    private String ab_key = "123";
    private String _cat_id, _distance;
    double       _lat, _log;

    private final String _TAG = this.getClass().getSimpleName();

    public GetDisplayCupon(String cat_id, String distance, double lat, double log) {
        _cat_id = cat_id;
        _distance = distance;
        _lat = lat;
        _log = log;
    }

    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
               WebserviceConstants.BASE_URL + WebserviceConstants.Display_COUPON;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_CATOGARIE_ID, _cat_id);
            requestBody.put(WebserviceConstants.KEY_DISTANCE, _distance);
            requestBody.put(WebserviceConstants.KEY_LATITUDE, _lat);
            requestBody.put(WebserviceConstants.KEY_LONGITUIDE, _log);

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
        Coupon_display _coupon_display = null;

        ArrayList<Coupon_display> coupon_list = new ArrayList<>();
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
                    int coupon_id = jsonObject.optInt(WebserviceConstants.KEY_COUPON_ID);
                    String sp_product = jsonObject.optString(WebserviceConstants.KEY_SP_PRODUCT);
                    String points_per_product = jsonObject.optString(WebserviceConstants.KEY_POINTS_PER_PRODUCT);
                    String currency = jsonObject.optString(WebserviceConstants.KEY_CURRENCY);
                    String sp_img_path = jsonObject.optString(WebserviceConstants.KEY_PRO_IMG);
                    String category = jsonObject.optString(WebserviceConstants.KEY_CATEGORY);
                    String discount = jsonObject.optString(WebserviceConstants.KEY_DISCOUNT);
                    String SPcOMP = jsonObject.optString(WebserviceConstants.KEY_SP_COMPANY);


                    _coupon_display = new Coupon_display(coupon_id, sp_product, points_per_product, currency, sp_img_path, category,discount,SPcOMP);
                    coupon_list.add(_coupon_display);

                }
                responseObject = new ServerResponse(errorCode, coupon_list);


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
        notifier.eventNotify(EventTypes.EVENT_DISPLAY_COUPON_LIST_RECEVIED, eventObject);

    }
}

