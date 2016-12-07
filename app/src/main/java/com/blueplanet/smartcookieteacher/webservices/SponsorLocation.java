package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.BlueLog;
import com.blueplanet.smartcookieteacher.models.BuyCoupon;
import com.blueplanet.smartcookieteacher.models.LocationModel;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by AviK297 on 7/4/2016.
 */
public class SponsorLocation extends SmartCookieTeacherService {
    private String _lati, _longni, _dist;
    private final String _TAG = this.getClass().getSimpleName();
    //ArrayList<LocationModel> _locationDetails = new ArrayList<LocationModel>();

    public SponsorLocation(String lat, String longi, String dist) {
        _lati = lat;
        _longni = longi;
        _dist = dist;

    }


    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.SPONSOR_LOCATION_URL;

    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_LAT, _lati);
            requestBody.put(WebserviceConstants.KEY_LONG, _longni);
            requestBody.put(WebserviceConstants.KEY_DISTANCE1, _dist);


        } catch (JSONException jsonException) {
            jsonException.printStackTrace();

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

        LocationModel locatnetails = null;
        ArrayList<LocationModel> _locationDetails = new ArrayList<>();
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

                    String sponorId = jsonObject.optString(WebserviceConstants.KEY_SPONSOR_ID);
                    String sponsorName = jsonObject.optString(WebserviceConstants.KEY_SPONSOR_NAME);
                    String address = jsonObject.optString(WebserviceConstants.KEY_SPONSOR_ADDRESS);
                    String city = jsonObject.optString(WebserviceConstants.KEY_SPONSOR_CITY);
                    String country = jsonObject.optString(WebserviceConstants.KEY_SPONSOR_COUNTRY);
                    String latitude = jsonObject.optString(WebserviceConstants.KEY_SPONSOR_LAT);

                    String longnit = jsonObject.optString(WebserviceConstants.KEY_SPONSOR_LONG);
                    String distance = jsonObject.optString((WebserviceConstants.KEY_SPONSOR_DISTANCE));
                    String category = jsonObject.optString(WebserviceConstants.KEY_SPONSOR_CATEGORY);
                    String image = jsonObject.optString((WebserviceConstants.KEY_SPONSOR_IMG_PATH));


                    Log.i(_TAG, "In success");
                    locatnetails = new LocationModel(sponorId, sponsorName, address, city, country, latitude, longnit, distance, category, image);

                    _locationDetails.add(locatnetails);

                }


                /**
                 send carray list object as response
                 */
                responseObject = new ServerResponse(errorCode, _locationDetails);

            } else {
                // failure
                Log.i(_TAG, "In failure");
                errorCode = WebserviceConstants.FAILURE;
                responseObject =
                        new ServerResponse(errorCode, new ErrorInfo(statusCode, statusMessage,
                                null));
            }
            fireEvent(responseObject);

        } catch (JSONException jsonException) {
            Log.i(_TAG, "In exception");
            jsonException.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
            Log.i(_TAG, "In exception");
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
        notifier.eventNotify(EventTypes.EVENT_LOCATION_RECIEVED_SUCCESSFULL, eventObject);
    }
}
