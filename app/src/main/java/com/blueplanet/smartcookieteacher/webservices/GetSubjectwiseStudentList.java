package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.Subjectwise_student;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 1311 on 04-02-2016.
 */
public class GetSubjectwiseStudentList extends SmartCookieTeacherService {

    private String _t_id, _studentId;
    private String _divisionId,_semesterId,_branchesId,_departmentId,_courseLevel,_subjectCode;
    private final String _TAG = this.getClass().getSimpleName();

    public GetSubjectwiseStudentList(String t_id, String studentId, String divisionId,String semesterId,String branchesId,
                                     String departmentId,
                                     String courseLevel,String subjectCode) {
        _t_id = t_id;
        _studentId = studentId;
        _divisionId=divisionId;
        _semesterId=semesterId;
        _branchesId=branchesId;
        _departmentId=departmentId;
        _courseLevel=courseLevel;
        _subjectCode=subjectCode;
    }
    @Override
    protected String formRequest() {

        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.SUBJECTWISE_STUDENT;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_TID, _t_id);
            requestBody.put(WebserviceConstants.KEY_SCHOOLID, _studentId);
            requestBody.put(WebserviceConstants.KEY_DIVISION_ID, _divisionId);
            requestBody.put(WebserviceConstants.KEY_SEMESTER_ID, _semesterId);
            requestBody.put(WebserviceConstants.KEY_BRANCHES_ID, _branchesId);
            requestBody.put(WebserviceConstants.KEY_DEPARTMENT_ID, _departmentId);
            requestBody.put(WebserviceConstants.KEY_COURSE_LEVEL, _courseLevel);
            requestBody.put(WebserviceConstants.KEY_SUBJECT_CODE, _subjectCode);

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

        Subjectwise_student subject = null;
        ArrayList<Subjectwise_student> subjectList = new ArrayList<>();
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


                    String sId = jsonObject.optString(WebserviceConstants.KEY_STUDENT_ID);
                    String sFullName = jsonObject.optString(WebserviceConstants.KEY_STUDENT_FULL_NAME);
                    String sFname = jsonObject.optString(WebserviceConstants.KEY_STUD_FIRST_NAME);

                    String sLastName = jsonObject.optString(WebserviceConstants.KEY_STUD_LAST_NAME);
                    String sImage = jsonObject.optString(WebserviceConstants.KEY_STUDE_IMG);
                    subject = new Subjectwise_student(sId,sFullName,sFname,sLastName,sImage);
                    subjectList.add(subject);

                }
                responseObject = new ServerResponse(errorCode, subjectList);
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
        notifier.eventNotify(EventTypes.EVENT_SUBJECTWISE_STUDENTLIST_RECEIVED, eventObject);
    }
    }

