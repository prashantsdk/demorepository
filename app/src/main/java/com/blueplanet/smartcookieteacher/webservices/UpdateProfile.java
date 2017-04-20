package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sayali on 3/23/2017.
 */
public class UpdateProfile extends SmartCookieTeacherService {

    private String _pId, _name, _phone, _country, _state, _city, _gender, _dob, _add, _pas, _qual, _occup, _img, _passward, _address;
    private String _email, _tid, _fname, _lname,_studentId,_countrycode,_memberID,_Key;
    private final String _TAG = this.getClass().getSimpleName();

    /**
     * constructor
     *
     * @param
     */
    public UpdateProfile(String email, String fname, String lname, String dob, String address, String city, String country,
                         String gender, String passward, String phone, String state,String studentId,String countrycode,
                         String memberID,String Key) {
        _email = email;
        _fname = fname;
        _lname = lname;
        _dob = dob;
        _address = address;
        _city = city;
        _country = country;
        _gender = gender;
        _passward = passward;
        _state = state;
        _phone = phone;
        _studentId = studentId;
        _countrycode=countrycode;
        _memberID=memberID;
        _Key=Key;

    }


    @Override
    protected String formRequest() {


        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.TEACHER_UPDATE_PROFILE;
    }

    @Override
    protected JSONObject formRequestBody() {

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_TEACHER_EMAIL, _email);
            //   requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_ID, _tid);
            requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_FIRST_NAME, _fname);
            requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_LAST_NAME, _lname);

            requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_DOB, _dob);
            requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_ADDRESS, _address);
            requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_CITY, _city);
            requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_COUNTRY, _country);
            //  requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_STATE, _lname);
            requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_GENDER, _gender);

            requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_PASSWARD, _passward);

            requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_STATE, _state);
            requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_PHONE, _phone);
            requestBody.put(WebserviceConstants.KEY_SCHOOLID, _studentId);

            requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_MEMBERID, _memberID);
            requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_KEY, _Key);
            requestBody.put(WebserviceConstants.KEY_TEACHER_UPDATE_COUNTRYCODE, _countrycode);

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
        Log.i(_TAG, "In parseResponse" + responseJSONString.toString());
        try {
            objResponseJSON = new JSONObject(responseJSONString);
            String obj = objResponseJSON.toString();
            statusCode = objResponseJSON.getInt(WebserviceConstants.KEY_STATUS_CODE);
            statusMessage =
                    objResponseJSON.getString(WebserviceConstants.KEY_STATUS_MESSAGE);

            Log.i(_TAG, responseJSONString.toString());

            if (statusCode == HTTPConstants.HTTP_COMM_SUCCESS) {
                JSONArray responseData = objResponseJSON.optJSONArray(WebserviceConstants.KEY_POSTS);


                responseObject = new ServerResponse(errorCode, responseData);

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
            Log.i(_TAG, "In exception");
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
        notifier.eventNotify(EventTypes.EVENT_TEACHER_UPDATE_PROFILE, eventObject);

    }
}

