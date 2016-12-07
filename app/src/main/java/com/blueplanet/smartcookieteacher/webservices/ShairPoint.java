package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 1311 on 27-07-2016.
 */
public class ShairPoint extends SmartCookieTeacherService {
    private String _teacherID, _schoolID;
    private final String _TAG = this.getClass().getSimpleName();

    /**
     * constructor
     *
     * @param tid,schoolid
     */
    public ShairPoint(String tid, String schoolID) {
        _teacherID = tid;
        _schoolID = schoolID;
    }


    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.SHAREPOINT_WEB_SERVICE;
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


            ArrayList<ShairPointModel> teachershairpointlist = new ArrayList<>();

            if (statusCode == HTTPConstants.HTTP_COMM_SUCCESS) {
                // success
                JSONArray responseData = objResponseJSON.optJSONArray(WebserviceConstants.KEY_POSTS);
                for (int i = 0; i < responseData.length(); i++) {
                    JSONObject jsonObject = responseData.optJSONObject(i);


                    String teacherID = jsonObject.optString(WebserviceConstants.kEY_TEACHERID);
                    String tname = jsonObject.optString(WebserviceConstants.KEY_TEACHERNAME);
                    String temail = jsonObject.optString(WebserviceConstants.KEY_EMAILID);
                    String tmno = jsonObject.optString(WebserviceConstants.KEY_MOBILENO);
                    String tbalancebluepoint = jsonObject.optString(WebserviceConstants.KEY_BALANCE_BLUEPOINT);


                    ShairPointModel shairpoint = new ShairPointModel(teacherID, tname, temail, tmno,
                            tbalancebluepoint);

                    teachershairpointlist.add(shairpoint);

                }
                responseObject = new ServerResponse(errorCode, teachershairpointlist);
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
        notifier.eventNotify(EventTypes.EVENT_SHAIR_POINT, eventObject);

    }
    }

