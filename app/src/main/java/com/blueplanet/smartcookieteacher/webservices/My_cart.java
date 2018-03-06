package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.AddCart;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Priyanka on 01-03-2018.
 */
public class My_cart extends SmartCookieTeacherService {

    private String _entity;
    private String _userid;
    private final String _TAG = this.getClass().getSimpleName();

    public My_cart(String user_id, String entity_id) {
        _userid = user_id;
        _entity = entity_id;
    }

    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.COUPON_MY_CART;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_USER_ID, _userid);
            requestBody.put(WebserviceConstants.KEY_ENTITY_FP, _entity);

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
        AddCart cart = null;
        ArrayList<AddCart> cardList = new ArrayList<>();


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
                    String coupon_selid = jsonObject.optString(WebserviceConstants.KEY_SEL_ID);
                    String coupon_pointsPerPro = jsonObject.optString(WebserviceConstants.KEY_COUP_POINTS_PER);
                    String coupon_validity = jsonObject.optString(WebserviceConstants.KEY_COUP_VALIDITY);
                    String coupon_name = jsonObject.optString(WebserviceConstants.KEY_COUP_NAME);
                    String coupon_address = jsonObject.optString(WebserviceConstants.KEY_COUP_ADDRESS);
                    String coupon_image = jsonObject.optString(WebserviceConstants.KEY_COUP_IMAGE);

                    cart = new AddCart(sp_coupon_id, coupon_selid, coupon_pointsPerPro, coupon_validity,coupon_name,coupon_address,coupon_image);
                    cardList.add(cart);

                }
                responseObject = new ServerResponse(errorCode, cardList);

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
        notifier.eventNotify(EventTypes.EVENT_MY_CART, eventObject);
    }
}
