package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;


import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 1311 on 19-12-2015.
 * webservice to get subjects for teacher
 */
public class GetTeacherSubjects extends SmartCookieTeacherService {

    private String _teacherID, _schoolID;
    private final String _TAG = this.getClass().getSimpleName();

    /**
     * constructor
     *
     * @param tid,schoolid
     */
    public GetTeacherSubjects(String tid, String schoolID) {
        _teacherID = tid;
        _schoolID = schoolID;
    }


    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.TEACHER_SUBJECT;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_TID, _teacherID);
            requestBody.put(WebserviceConstants.KEY_SCHOOLID, _schoolID);

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


            ArrayList<TeacherSubject> subjectList = new ArrayList<>();

            if (statusCode == HTTPConstants.HTTP_COMM_SUCCESS) {
                // success
                JSONArray responseData = objResponseJSON.optJSONArray(WebserviceConstants.KEY_POSTS);
                for (int i = 0; i < responseData.length(); i++) {
                    JSONObject jsonObject = responseData.optJSONObject(i);

                    int tsubID = jsonObject.optInt(WebserviceConstants.KEY_SUBJECT_ID);
                    String tschoolID = jsonObject.optString(WebserviceConstants.KEY_SCHOOL_ID);
                    String tsubcode = jsonObject.optString(WebserviceConstants.KEY_SUBJECT_CODE);
                    String tsubName = jsonObject.optString(WebserviceConstants.KEY_SUBJECT_NAME);
                    String tsubDivisionId = jsonObject.optString(WebserviceConstants.KEY_DIVISION_ID);
                    String tsubSemesterID = jsonObject.optString(WebserviceConstants.KEY_SEMESTER_ID);
                    Log.i(_TAG, "SEMID in ws" + tsubSemesterID);
                    String tsubBranchID = jsonObject.optString(WebserviceConstants.KEY_BRANCHES_ID);
                    String tsubDepartmentID = jsonObject.optString(WebserviceConstants.KEY_DEPARTMENT_ID);
                    String tsubCourseLevelID = jsonObject.optString(WebserviceConstants.KEY_COURSE_LEVEL);

                    TeacherSubject teacherSubject = new TeacherSubject(tsubID, tschoolID, tsubcode, tsubName,
                            tsubDivisionId, tsubSemesterID, tsubBranchID, tsubDepartmentID, tsubCourseLevelID);

                    subjectList.add(teacherSubject);

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
        notifier.eventNotify(EventTypes.EVENT_TEACHER_SUBJECT_RECEIVED, eventObject);

    }
}
