package com.blueplanet.smartcookieteacher.webservices;

import android.text.TextUtils;
import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.utils.SmartCookieSharedPreferences;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 1311 on 24-11-2015.
 *
 * @author dhanashree.ghayal
 */
public class TeacherLogin extends SmartCookieTeacherService {


    private String _username, _userPassword, _type, _colgcode, _method, _devicetype, _details, _os, _ipadddress,  _countrycode;
    private final String _TAG = this.getClass().getSimpleName();
    double       _lat, _log;

    public TeacherLogin(String username, String userPassword, String type, String colgcode, String method, String devicetype, String details, String os, String ipadddress, String countrycode,
                        double lat, double log) {

        _username = username;
        _userPassword = userPassword;
        _type = type;
        _colgcode = colgcode;
        _method = method;
        _devicetype = devicetype;
        _details = details;
        _os = os;
        _ipadddress = ipadddress;
        _countrycode = countrycode;
        _lat=lat;
        _log=log;
    }

    @Override
    protected String formRequest() {
        // _loginFragment.showTestProduction(true);
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.TEACHER_LOGIN;
    }

    @Override
    protected JSONObject formRequestBody() {

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_USERNAME, _username);
            requestBody.put(WebserviceConstants.KEY_USER_PASSWORD, _userPassword);
            requestBody.put(WebserviceConstants.KEY_USERTYPE, _type);
            requestBody.put(WebserviceConstants.KEY_COLLEDGE_CODE, _colgcode);
            requestBody.put(WebserviceConstants.KEY_METHOD, _method);
            requestBody.put(WebserviceConstants.KEY_DEVICE_TYPE, _devicetype);
            requestBody.put(WebserviceConstants.KEY_DEVICE_DETAIL, _details);
            requestBody.put(WebserviceConstants.KEY_PLATFORM_OS, _os);
            requestBody.put(WebserviceConstants.KEY_iPADDRESS, _ipadddress);
            requestBody.put(WebserviceConstants.KEY_LATITUDE_LOGIN, _lat);
            requestBody.put(WebserviceConstants.KEY_LONGITUDE_LOGIN, _log);
            requestBody.put(WebserviceConstants.KEY_COUNTRY_CODE, _countrycode);


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

        try {
            objResponseJSON = new JSONObject(responseJSONString);

            statusCode = objResponseJSON.getInt(WebserviceConstants.KEY_STATUS_CODE);
            statusMessage =
                    objResponseJSON.getString(WebserviceConstants.KEY_STATUS_MESSAGE);

            Teacher teacher = null;
            if (statusCode == HTTPConstants.HTTP_COMM_SUCCESS) {
                // success
                JSONArray responseData = objResponseJSON.optJSONArray(WebserviceConstants.KEY_POSTS);
                for (int i = 0; i < responseData.length(); i++) {
                    JSONObject jsonObject = responseData.optJSONObject(i);

                    int id = jsonObject.optInt(WebserviceConstants.KEY_ID);
                    String tid = jsonObject.optString(WebserviceConstants.KEY_TID);
                    String tcompName = jsonObject.optString(WebserviceConstants.KEY_TCOMPL_Name);
                    String tName = jsonObject.optString(WebserviceConstants.KEY_TNAME);
                    String tMName = jsonObject.optString(WebserviceConstants.KEY_TMIDDLE_NAME);

                    String tLName = jsonObject.optString(WebserviceConstants.KEY_TLAST_NAME);

                    if (TextUtils.isEmpty(tLName) || tLName.equalsIgnoreCase("")) {
                        tLName = "N/A";
                    }
                    String tScName = jsonObject.optString(WebserviceConstants.KEY_TCURRENT_SCHOOL_NAME);
                    String tScId = jsonObject.optString(WebserviceConstants.KEY_TSCHOOL_ID);

                    int tStaffId = jsonObject.optInt(WebserviceConstants.KEY_TSTAFF_ID);
                    String tDepartment = jsonObject.optString(WebserviceConstants.KEY_TDEPARTMENT);
                    String tExperience = jsonObject.optString(WebserviceConstants.KEY_TEXPRIENCE);

                    String tSubject = jsonObject.optString(WebserviceConstants.KEY_TSUBJECT);
                    if (TextUtils.isEmpty(tSubject)) {
                        tLName = null;

                    }
                    String tClass = jsonObject.optString(WebserviceConstants.KEY_CLASS);
                    if (TextUtils.isEmpty(tClass)) {
                        tLName = null;

                    }
                    String tQulification = jsonObject.optString(WebserviceConstants.KEY_TQULIFICATION);
                    if (TextUtils.isEmpty(tQulification)) {
                        tLName = null;

                    }

                    String tAddress = jsonObject.optString(WebserviceConstants.KEY_TADDRESS);
                    String tCity = jsonObject.optString(WebserviceConstants.KEY_TCITY);
                    if (TextUtils.isEmpty(tCity)) {
                        tLName = null;

                    }
                    String tDOB = jsonObject.optString(WebserviceConstants.KEY_TDOB);


                    int tAge = jsonObject.optInt(WebserviceConstants.KEY_TAGE);
                    String tGender = jsonObject.optString(WebserviceConstants.KEY_GENDER);
                    String tCountry = jsonObject.optString(WebserviceConstants.KEY_TCOUNTRY);

                    String tEmail = jsonObject.optString(WebserviceConstants.KEY_TEMAIL);
                    String tInterEmail = jsonObject.optString(WebserviceConstants.KEY_TINTERNAL_EMAIL);
                    String tPassword = jsonObject.optString(WebserviceConstants.KEY_TPASSWORD);

                    String tDate = jsonObject.optString(WebserviceConstants.KEY_DATE);
                    String tKeyPc = jsonObject.optString(WebserviceConstants.KEY_PC);
                    String tPhone = jsonObject.optString(WebserviceConstants.KEY_PHONE);

                    int tLandline = jsonObject.optInt(WebserviceConstants.KEY_TLANDLINE);
                    int tBalancePoint = jsonObject.optInt(WebserviceConstants.KEY_BALANCE_POINT);
                    int tUsedPoint = jsonObject.optInt(WebserviceConstants.KEY_TUSED_POINT);

                    int tState = jsonObject.optInt(WebserviceConstants.KEY_TSTATE);
                    int tBalanceBluePoint = jsonObject.optInt(WebserviceConstants.KEY_BALANCE_BLUE_POINT);
                    int tUsedBluePoint = jsonObject.optInt(WebserviceConstants.KEY_USED_BLUE_POINT);

                    String tBatchId = jsonObject.optString(WebserviceConstants.KEY_BATCH_ID);
                    String tErrorRec = jsonObject.optString(WebserviceConstants.KEY_ERROR_RECORDS);
                    String tSendStatus = jsonObject.optString(WebserviceConstants.KEY_SEND_UNSEND_STATUS);

                    String tTempAddress = jsonObject.optString(WebserviceConstants.KEY_TEMP_ADDRESS);
                    if (TextUtils.isEmpty(tTempAddress)) {
                        tLName = null;

                    }
                    String tPerVillage = jsonObject.optString(WebserviceConstants.KEY_PERMANENT_VILLAGE);
                    if (TextUtils.isEmpty(tPerVillage)) {
                        tLName = null;

                    }
                    String tPerTaluka = jsonObject.optString(WebserviceConstants.KEY_PERMANENT_TALUKA);
                    if (TextUtils.isEmpty(tPerTaluka)) {
                        tLName = null;

                    }

                    String tPerDistrict = jsonObject.optString(WebserviceConstants.KEY_PERMANENT_DISTRICT);
                    if (TextUtils.isEmpty(tPerDistrict)) {
                        tLName = null;

                    }
                    int tPerPincode = jsonObject.optInt(WebserviceConstants.KEY_PERMANENT_PINCODE);
                    String tDotAppoinment = jsonObject.optString(WebserviceConstants.KEY_DOT_OF_APPOINTMENT);

                    if (TextUtils.isEmpty(tDotAppoinment)) {
                        tLName = null;

                    }

                    String tAppoinmentPid = jsonObject.optString(WebserviceConstants.KEYAPPOINTMENT_TYPE_PID);
                    if (TextUtils.isEmpty(tAppoinmentPid)) {
                        tLName = null;

                    }

                    String tEmpTypeId = jsonObject.optString(WebserviceConstants.KEY_EMP_TYPE_PID);
                    String CollageMnemonic = jsonObject.optString(WebserviceConstants.KEY_COLLAGE_MNEMONIC);

                    teacher = new Teacher(id, tid, tcompName, tName, tMName, tLName, tScName, tScId,
                            tStaffId, tDepartment, tExperience, tSubject, tClass, tQulification, tAddress,
                            tCity, tDOB, tAge, tGender, tCountry, tEmail, tInterEmail, tPassword,
                            tDate, tKeyPc, tPhone, tLandline, tUsedPoint, tBalancePoint, tUsedBluePoint, tBatchId,
                            tState, tBalanceBluePoint, tErrorRec, tSendStatus,
                            tTempAddress, tPerVillage, tPerTaluka, tPerDistrict,
                            tPerPincode, tDotAppoinment, tAppoinmentPid, tEmpTypeId, CollageMnemonic);

                    SmartCookieSharedPreferences.setUserID(tid);

                }
                responseObject = new ServerResponse(errorCode, teacher);
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
            SmartCookieSharedPreferences.setLoginFlag(false);
        } catch (Exception exception) {
            exception.printStackTrace();
            SmartCookieSharedPreferences.setLoginFlag(false);
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
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
        notifier.eventNotify(EventTypes.EVENT_LOGIN_SUCCESSFUL, eventObject);

    }

}

