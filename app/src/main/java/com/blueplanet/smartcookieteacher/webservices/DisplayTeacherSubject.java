package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.featurecontroller.DisplaySubjectFeatureController;
import com.blueplanet.smartcookieteacher.models.DisplayTeacSubjectModel;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sayali on 3/15/2017.
 */
public class DisplayTeacherSubject  extends SmartCookieTeacherService {
    private String _t_id, _studentId,_keyname;

    private final String _TAG = this.getClass().getSimpleName();

    /**
     * constructor
     *
     * @param studentId
     */
    public DisplayTeacherSubject( String studentId,String keyname) {


        _studentId = studentId;
        _keyname=keyname;
    }

    @Override
    protected String formRequest() {

        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.TEACHER_DISPLAY_SUBJECT_WEB_SERVICE;

    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {

            requestBody.put(WebserviceConstants.KEY_SCHOOLID, _studentId);
            requestBody.put(WebserviceConstants.KEY_TEACHER_SUBJECT_KEY_NAME, _keyname);

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



        ArrayList<DisplayTeacSubjectModel> subList = new ArrayList<>();
        try {
            objResponseJSON = new JSONObject(responseJSONString);

            statusCode = objResponseJSON.getInt(WebserviceConstants.KEY_STATUS_CODE);
            statusMessage =
                    objResponseJSON.getString(WebserviceConstants.KEY_STATUS_MESSAGE);

            if (statusCode == HTTPConstants.HTTP_COMM_SUCCESS) {
                DisplayTeacSubjectModel _subModel = null;
                // success
                JSONArray responseData = objResponseJSON.optJSONArray(WebserviceConstants.KEY_POSTS);
                for (int i = 0; i < responseData.length(); i++) {
                    JSONObject jsonObject = responseData.optJSONObject(i);
                    String name = jsonObject.optString(WebserviceConstants.KEY_TEACHER_SUBJECT_NAME);
                    String code= jsonObject.optString(WebserviceConstants.KEY_TEACHER_SUBJECT_CODE);
                    String  semesterid= jsonObject.optString(WebserviceConstants.KEY_TEACHER_SUBJECT_SEMESTER_ID);
                    String courselevel = jsonObject.optString(WebserviceConstants.KEY_TEACHER_SUBJECT_COURSE_LEVEL);
                    String year = jsonObject.optString(WebserviceConstants.KEY_TEACHER_SUBJECT_YEAR);

                    _subModel = new DisplayTeacSubjectModel(name, code, semesterid, courselevel,year);

                        if (DisplaySubjectFeatureController.getInstance().getSearchedTeacher() == null) {
                            subList.add(_subModel);
                        } else if (!DisplaySubjectFeatureController.getInstance().getSearchedTeacher().toString().contains(code)) {
                            subList.add(_subModel);
                        } else {

                        }



                }
                Log.i(_TAG, "" + _subModel);
                responseObject = new ServerResponse(errorCode, subList);


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
        notifier.eventNotify(EventTypes.EVENT_TEACHER_SUBJECT, eventObject);

    }
}
