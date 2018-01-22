package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;


import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 1311 on 26-11-2015.
 */
public class GetStudentList extends SmartCookieTeacherService {

    private String _t_id, _studentId;
    private int _inputId = -1;
    private final String _TAG = this.getClass().getSimpleName();


    public GetStudentList(String t_id, String studentId, int inputId) {
        _t_id = t_id;
        _studentId = studentId;
        _inputId = inputId;
    }

    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.GET_STUDENT_INFO;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_TID, _t_id);
            requestBody.put(WebserviceConstants.KEY_SCHOOLID, _studentId);
            requestBody.put(WebserviceConstants.KEY_INPUT_ID, _inputId);

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

        Student student = null;
        ArrayList<Student> studentList = new ArrayList<>();
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


                    int sId = jsonObject.optInt(WebserviceConstants.KEY_SID);
                    String sName = jsonObject.optString(WebserviceConstants.KEY_SNAME);
                    String sFname = jsonObject.optString(WebserviceConstants.KEY_SFNAME);
                    Log.i(_TAG,"Father Name"+sFname);

                    String sSchoolName = jsonObject.optString(WebserviceConstants.KEY_SSCHOOLNMAE);

                    String sClassName = jsonObject.optString(WebserviceConstants.KEY_SCLASSNAME);
                    String tMName = jsonObject.optString(WebserviceConstants.KEY_SADDRESS);

                    String SGender = jsonObject.optString(WebserviceConstants.KEY_SGENDER);
                    String Sdob = jsonObject.optString(WebserviceConstants.KEY_SDOB);

                    String sAge = jsonObject.optString(WebserviceConstants.KEY_SAGE);

                    String Scity = jsonObject.optString(WebserviceConstants.KEY_SCITY);
                    String sEmail = jsonObject.optString(WebserviceConstants.KEY_SEMAIL);
                    String sPRN = jsonObject.optString(WebserviceConstants.KEY_SPRN);

                    String sSchoolId = jsonObject.optString(WebserviceConstants.KEY_SSCHOOLID);
                    String sDate = jsonObject.optString(WebserviceConstants.KEY_SDATE);
                    String sDiv = jsonObject.optString(WebserviceConstants.KEY_SDIV);

                    String sHobbies = jsonObject.optString(WebserviceConstants.KEY_SHOBBIES);
                    String sCountry = jsonObject.optString(WebserviceConstants.KEY_SCOUNTRY);
                    String sTeacherName = jsonObject.optString(WebserviceConstants.KEY_SCLASSTEACHERNAME);
                    String sImagePath = jsonObject.optString(WebserviceConstants.KEY_SIMGPATH);
                    int totalStudentCount = jsonObject.optInt(WebserviceConstants.KEY_STUDENT_TOTAL_COUNT);
                    String ssubcode = jsonObject.optString(WebserviceConstants.KEY_STUDENT_SUBCODE);
                    String subname = jsonObject.optString(WebserviceConstants.KEY_STUDENT_SUBNAME);

                    int inputId = jsonObject.optInt(WebserviceConstants.KEY_INPUT_ID);

                    student = new Student(sId, sName, sFname, sSchoolName, sClassName, tMName, SGender,
                            Sdob, sAge, Scity, sEmail, sPRN, sSchoolId, sDate, sDiv, sHobbies, sCountry,
                            sTeacherName, sImagePath, inputId, totalStudentCount,ssubcode,subname);
                    studentList.add(student);





                }


                responseObject = new ServerResponse(errorCode, studentList);
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
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
        notifier.eventNotify(EventTypes.EVENT_STUDENT_LIST_RECEIVED, eventObject);
    }
}
