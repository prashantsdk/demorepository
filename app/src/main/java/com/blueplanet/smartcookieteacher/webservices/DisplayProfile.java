package com.blueplanet.smartcookieteacher.webservices;

import android.text.TextUtils;
import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.NewRegistrationModel;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.utils.SmartCookieSharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Priyanka on 3/05/2018.
 */
public class DisplayProfile extends SmartCookieTeacherService {

    private String school_id, t_id;
    private String _email, _tid, _fname, _lname,_studentId,_countrycode,_memberID,_Key,_mname;
    private final String _TAG = this.getClass().getSimpleName();

    /**
     * constructor
     *
     * @param
     */
    public DisplayProfile(String t_id, String school_id) {
        this.t_id = t_id;
        this.school_id = school_id;
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
            requestBody.put(WebserviceConstants.KEY_TEACHER__ID, t_id);
            requestBody.put(WebserviceConstants.KEY_SCHOOL_ID, school_id);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Log.i(_TAG, requestBody.toString());
        return requestBody;
    }

 /*   @Override
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
                   *//* String gender = jsonObject.optString(WebserviceConstants.KEY_USER_GENDER);
                    String dob = jsonObject.optString(WebserviceConstants.KEY_USER_DOB);*//*

                    _regmodel = new NewRegistrationModel(userIdname,compname, fname, mname,lname,address,city,country,state,phone,regpassward,countryucode,email,imgpath, imgname*//*,dob,gender*//*);

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
    }*/

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

                    /*if (TextUtils.isEmpty(tLName) || tLName.equalsIgnoreCase("")) {
                        tLName = "N/A";
                    }*/
                    String tScName = jsonObject.optString(WebserviceConstants.KEY_TCURRENT_SCHOOL_NAME);
                    String tScId = jsonObject.optString(WebserviceConstants.KEY_TSCHOOL_ID);

                    int tStaffId = jsonObject.optInt(WebserviceConstants.KEY_TSTAFF_ID);
                    String tDepartment = jsonObject.optString(WebserviceConstants.KEY_TDEPARTMENT);
                    String tExperience = jsonObject.optString(WebserviceConstants.KEY_TEXPRIENCE);

                    String tSubject = jsonObject.optString(WebserviceConstants.KEY_TSUBJECT);
                    if (TextUtils.isEmpty(tSubject)) {
                        tSubject = null;

                    }
                    String tClass = jsonObject.optString(WebserviceConstants.KEY_CLASS);
                    if (TextUtils.isEmpty(tClass)) {
                        tClass = null;

                    }
                    String tQulification = jsonObject.optString(WebserviceConstants.KEY_TQULIFICATION);
                    if (TextUtils.isEmpty(tQulification)) {
                        tQulification = null;

                    }

                    String tAddress = jsonObject.optString(WebserviceConstants.KEY_TADDRESS);
                    String tCity = jsonObject.optString(WebserviceConstants.KEY_TCITY);
                    if (TextUtils.isEmpty(tCity)) {
                        tCity = null;

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
                        tTempAddress = null;

                    }
                    String tPerVillage = jsonObject.optString(WebserviceConstants.KEY_PERMANENT_VILLAGE);
                    if (TextUtils.isEmpty(tPerVillage)) {
                        tPerVillage = null;

                    }
                    String tPerTaluka = jsonObject.optString(WebserviceConstants.KEY_PERMANENT_TALUKA);
                    if (TextUtils.isEmpty(tPerTaluka)) {
                        tPerTaluka = null;

                    }

                    String tPerDistrict = jsonObject.optString(WebserviceConstants.KEY_PERMANENT_DISTRICT);
                    if (TextUtils.isEmpty(tPerDistrict)) {
                        tPerDistrict = null;

                    }
                    int tPerPincode = jsonObject.optInt(WebserviceConstants.KEY_PERMANENT_PINCODE);
                    String tDotAppoinment = jsonObject.optString(WebserviceConstants.KEY_DOT_OF_APPOINTMENT);

                    if (TextUtils.isEmpty(tDotAppoinment)) {
                        tDotAppoinment = null;

                    }

                    String tAppoinmentPid = jsonObject.optString(WebserviceConstants.KEYAPPOINTMENT_TYPE_PID);
                    if (TextUtils.isEmpty(tAppoinmentPid)) {
                        tAppoinmentPid = null;

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

                   // LoginFeatureController.setTeacher(teacher);
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
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        notifier.eventNotify(EventTypes.EVENT_TEACHER_DISPLAY_PROFILE, eventObject);

    }
}

