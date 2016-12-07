package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;


import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.SchoolOnMapModel;
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
public class SchoolMapService extends SmartCookieTeacherService {


    private String latitude,longitude,distance,entity_type,place_name,loc_type,range_type;
    private int ip_id;
    private String _TAG = this.getClass().getSimpleName();


    public SchoolMapService(int ip_id, String latitude, String longitude, String entity_type, String place_name, String loc_type,
                            String distance, String range_type) {

        this.ip_id=ip_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.entity_type = entity_type;
        this.place_name = place_name;
        this.loc_type = loc_type;
        this.distance=distance;
        this.range_type = range_type;
    }

    @Override
    protected String formRequest() {

        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.MAP_SERVICE_API;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {

            requestBody.put(WebserviceConstants.KEY_MAP_IP_ID, ip_id);
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
                SchoolOnMapModel onMapModel = null;
                ArrayList<SchoolOnMapModel> arr_Sponsorlist = new ArrayList();
                Log.i(_TAG, responseJSONString.toString());
                // success
                JSONArray responseData = objResponseJSON.optJSONArray(WebserviceConstants.KEY_POSTS);
                for (int i = 0; i < responseData.length(); i++) {
                    JSONObject jsonObject = responseData.optJSONObject(i);

                    //JSONObject jsonObject=jsonObjectl.getJSONObject(WebserviceConstants.KEY_POST);
                    String KEY_SCHOOL_ID = jsonObject.optString(WebserviceConstants.KEY_SCHOOL_ID_ON_MAP);
                    String SCHOOL_NAME = jsonObject.optString(WebserviceConstants.KEY_SCHOOL_NAME_ON_MAP);
                    String SCHOOL_ADDRESS = jsonObject.optString(WebserviceConstants.KEY_SCHOOL_ADDRESS_ON_MAP);
                    String SCHOOL_CITY = jsonObject.optString(WebserviceConstants.KEY_SCHOOL_CITY_ON_MAP);
                    String SCHOOL_COUNTRY = jsonObject.optString(WebserviceConstants.KEY_SCHOOL_COUNTRY_ON_MAP);
                    String SCHOOL_LAT = jsonObject.optString(WebserviceConstants.KEY_SCHOOL_LAT_ON_MAP);
                    String SCHOOL_LONG = jsonObject.optString(WebserviceConstants.KEY_SCHOOL_LONG_ON_MAP);
                    String SCHOOL_DISTANCE = jsonObject.optString(WebserviceConstants.KEY_SCHOOL_DISTANCE_ON_MAP);
                    String KEY_SCHOOL_STUDENTS_COUNT = jsonObject.optString(WebserviceConstants.KEY_SCHOOL_STUDENTS_COUNT);
                    String KEY_SCHOOL_IMG_PATH = jsonObject.optString(WebserviceConstants.KEY_SCHOOL_IMG_PATH);
                    try {
                        SCHOOL_DISTANCE=SCHOOL_DISTANCE.substring(0,3);
                        if (SCHOOL_DISTANCE.endsWith(".")){
                            SCHOOL_DISTANCE=SCHOOL_DISTANCE.replace(".","").trim();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.d(_TAG,e.toString());
                    }

                    if (SCHOOL_DISTANCE.equals("0")){
                        SCHOOL_DISTANCE="0.1";
                    }


                    onMapModel=new SchoolOnMapModel(KEY_SCHOOL_ID,SCHOOL_NAME,SCHOOL_ADDRESS,SCHOOL_CITY,SCHOOL_COUNTRY,
                                                    SCHOOL_LAT,SCHOOL_LONG,SCHOOL_DISTANCE,KEY_SCHOOL_STUDENTS_COUNT,KEY_SCHOOL_IMG_PATH);
                   arr_Sponsorlist.add(onMapModel);
                }
                Log.i(_TAG, "" + onMapModel);
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
        notifier.eventNotify(EventTypes.EVENT_SCHOOL_ON_MAP_RESPONCE_RECIEVED, eventObject);

    }
}

