package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.SponsorOnMapModel;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 2017 on 11/25/2015.
 * Get login info
 * Author pramod.shelke
 */
public class SponsorMapService extends SmartCookieTeacherService {


    private String latitude, longitude, distance, entity_type, place_name, loc_type, range_type;
    private String _TAG = this.getClass().getSimpleName();
    private int input_id;

    public SponsorMapService(int input_id, String latitude, String longitude, String entity_type, String place_name, String loc_type,
                             String distance, String range_type) {

        this.input_id = input_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.entity_type = entity_type;
        this.place_name = place_name;
        this.loc_type = loc_type;
        this.distance = distance;
        this.range_type = range_type;
    }

    @Override
    protected String formRequest() {


        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL +
                WebserviceConstants.MAP_SERVICE_API;


    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {


            requestBody.put(WebserviceConstants.KEY_MAP_IP_ID, input_id);
            requestBody.put(WebserviceConstants.KEY_LATT, latitude);
            requestBody.put(WebserviceConstants.KEY_LONGG, longitude);
            requestBody.put(WebserviceConstants.KEY_ENTITY_TYPE, entity_type);
            requestBody.put(WebserviceConstants.KEY_PLACE_NAME, place_name);
            requestBody.put(WebserviceConstants.KEY_LOC_TYPE, loc_type);
            requestBody.put(WebserviceConstants.KEY_DISTANCEE, distance);
            requestBody.put(WebserviceConstants.KEY_DISTANCE_TYPE, range_type);


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
                SponsorOnMapModel sponsorOnMapModel = null;
                ArrayList<SponsorOnMapModel> arr_Sponsorlist = new ArrayList();
                Log.i(_TAG, responseJSONString.toString());
                // success
                JSONArray responseData = objResponseJSON.optJSONArray(WebserviceConstants.KEY_POSTS);
                for (int i = 0; i < responseData.length(); i++) {
                    JSONObject jsonObject = responseData.optJSONObject(i);

                    JSONObject jsonObject1 = jsonObject.getJSONObject("post");
                    String KEY_SPONSOR_ID = jsonObject1.optString(WebserviceConstants.KEY_SPONSOR_IDD);
                    String SPONSOR_NAME = jsonObject1.optString(WebserviceConstants.KEY_SPONSOR_NAMEE);
                    String SPONSOR_ADDRESS = jsonObject1.optString(WebserviceConstants.KEY_SPONSOR_ADDRESSS);
                    String SPONSOR_CITY = jsonObject1.optString(WebserviceConstants.KEY_SPONSOR_CITYY);
                    String SPONSOR_COUNTRY = jsonObject1.optString(WebserviceConstants.KEY_SPONSOR_COUNTRYY);
                    String SPONSOR_LAT = jsonObject1.optString(WebserviceConstants.KEY_SPONSOR_LATT);
                    String SPONSOR_LONG = jsonObject1.optString(WebserviceConstants.KEY_SPONSOR_LONGG);
                    String SPONSOR_DISTANCE = jsonObject1.optString(WebserviceConstants.KEY_SPONSOR_DISTANCEE);
                    String KEY_SPONSOR_CATEGORY = jsonObject1.optString(WebserviceConstants.KEY_SPONSOR_CATEGORYY);
                    String KEY_SPONSOR_IMG_PATH = jsonObject1.optString(WebserviceConstants.KEY_SPONSOR_IMG_PATHH);

                    String shopName = jsonObject1.optString(WebserviceConstants.KEY_SPONSOR_SHOP_NAME);
                    String shopPhoneNo = jsonObject1.optString(WebserviceConstants.KEY_SPONSOR_SHOP_PHONE);
                    String shopMaxDiscount = jsonObject1.optString(WebserviceConstants.KEY_SPONSOR_SHOP_MAX_DISCOUNT);

                    try {
                        SPONSOR_DISTANCE = SPONSOR_DISTANCE.substring(0, 3);
                        if (SPONSOR_DISTANCE.endsWith(".")) {
                            SPONSOR_DISTANCE = SPONSOR_DISTANCE.replace(".", "").trim();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(_TAG, e.toString());
                    }

                    if (SPONSOR_DISTANCE.equals("0")) {
                        SPONSOR_DISTANCE = "0.1";
                    }
//

                    sponsorOnMapModel = new SponsorOnMapModel(KEY_SPONSOR_ID, SPONSOR_NAME, SPONSOR_ADDRESS, SPONSOR_CITY, SPONSOR_COUNTRY,
                            SPONSOR_LAT, SPONSOR_LONG, SPONSOR_DISTANCE, KEY_SPONSOR_CATEGORY, KEY_SPONSOR_IMG_PATH,shopName,shopPhoneNo,shopMaxDiscount);
                    arr_Sponsorlist.add(sponsorOnMapModel);


                }
                Log.i(_TAG, "" + sponsorOnMapModel);
                /**
                 send sponsor object as response
                 */
                responseObject = new ServerResponse(errorCode, arr_Sponsorlist);
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
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
        notifier.eventNotify(EventTypes.EVENT_SPONSOR_ON_MAP_RESPONCE_RECIEVED, eventObject);

    }
}

