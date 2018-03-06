package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.NewRegistrationModel;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Priyanka on 3/05/2018.
 */
public class DisplayProfile extends SmartCookieTeacherService {

    private String user_id;
    private String _email, _tid, _fname, _lname,_studentId,_countrycode,_memberID,_Key,_mname;
    private final String _TAG = this.getClass().getSimpleName();

    /**
     * constructor
     *
     * @param
     */
    public DisplayProfile(String user_id) {
        this.user_id = user_id;
    }


    @Override
    protected String formRequest() {


        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.TEACHER_DISPLAY_PROFILE;
    }

    @Override
    protected JSONObject formRequestBody() {

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_USER_ID, user_id);

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
       // NewRegistrationModel _regmodel = null;

        //ArrayList<NewRegistrationModel> reglist = new ArrayList<>();
        try {
            objResponseJSON = new JSONObject(responseJSONString);
            String obj = objResponseJSON.toString();
            statusCode = objResponseJSON.getInt(WebserviceConstants.KEY_STATUS_CODE);
            statusMessage =
                    objResponseJSON.getString(WebserviceConstants.KEY_STATUS_MESSAGE);

            Log.i(_TAG, responseJSONString.toString());
            NewRegistrationModel _regmodel = null;
            if (statusCode == HTTPConstants.HTTP_COMM_SUCCESS) {
                // success
                JSONArray responseData = objResponseJSON.optJSONArray(WebserviceConstants.KEY_POSTS);
                for (int i = 0; i < responseData.length(); i++) {
                    JSONObject jsonObject = responseData.optJSONObject(i);
                    String userIdname = jsonObject.optString(WebserviceConstants.KEY_USER_MID);
                    String compname = jsonObject.optString(WebserviceConstants.KEY_USER_COMPNAME);
                    String fname = jsonObject.optString(WebserviceConstants.KEY_USER_REG_FNAME);
                    String mname = jsonObject.optString(WebserviceConstants.KEY_USER_REG_MNAME);
                    String lname = jsonObject.optString(WebserviceConstants.KEY_USER_REG_lNAME);
                    String address = jsonObject.optString(WebserviceConstants.KEY_USER_REG_ADDRESS);
                    String city = jsonObject.optString(WebserviceConstants.KEY_USER_REG_CITY);
                    String country = jsonObject.optString(WebserviceConstants.KEY_USER_REG_COUNTEY);
                    String state = jsonObject.optString(WebserviceConstants.KEY_USER_REG_STATE);
                    String phone= jsonObject.optString(WebserviceConstants.KEY_USER_REG_PHONE);
                    String regpassward = jsonObject.optString(WebserviceConstants.KEY_USER_REG_PASSWARD);
                    String countryucode = jsonObject.optString(WebserviceConstants.KEY_USER_REG_COUNTRYCODE);
                    String email = jsonObject.optString(WebserviceConstants.KEY_USER_REG_EMAIL);
                    String imgpath = jsonObject.optString(WebserviceConstants.KEY_USER_REG_IMGPATH);
                    String imgname = jsonObject.optString(WebserviceConstants.KEY_USER_REG_IMGNAME);
                   /* String gender = jsonObject.optString(WebserviceConstants.KEY_USER_GENDER);
                    String dob = jsonObject.optString(WebserviceConstants.KEY_USER_DOB);*/

                    _regmodel = new NewRegistrationModel(userIdname,compname, fname, mname,lname,address,city,country,state,phone,regpassward,countryucode,email,imgpath, imgname/*,dob,gender*/);

                }
                responseObject = new ServerResponse(errorCode, _regmodel);

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
        notifier.eventNotify(EventTypes.EVENT_TEACHER_DISPLAY_PROFILE, eventObject);

    }
}

