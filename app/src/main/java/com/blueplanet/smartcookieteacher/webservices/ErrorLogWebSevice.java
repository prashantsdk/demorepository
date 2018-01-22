package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.models.ErrorLog;
import com.blueplanet.smartcookieteacher.models.RequestPointModel;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Sayali on 8/27/2017.
 */
public class ErrorLogWebSevice extends SmartCookieTeacherService {


    private String _t_id,_studentId,_type,_description,_date,_datetime,_usertype,_name,
            _phone,_email,_appname,_subroutinename,_line,_status,_webmethodname,_webservice,_proname;
    private final String _TAG = this.getClass().getSimpleName();

    /**
     * constructor
     *
     * @param studentId
     */
    public ErrorLogWebSevice(String t_id,String studentId,String type,String description,String date,String datetime,String usertype,String name,String phone,String email,
                             String appname,String subroutinename, String line,String status,String webmethodname,String webservice,String proname) {

        _t_id=t_id;
        _studentId = studentId;
        _type=type;
        _description=description;
        _date=date;
        _datetime=datetime;
        _usertype=usertype;
        _name=name;
        _phone=phone;
        _email=email;
        _appname=appname;
        _subroutinename=subroutinename;
        _line=line;
        _status=status;
        _webmethodname=webmethodname;
        _webservice=webservice;
        _proname=proname;

    }


    @Override
    protected String formRequest() {
        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.ERROE_LOG_WEB_SERVICE;
    }

    @Override
    protected JSONObject formRequestBody() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_ID, _t_id);
            requestBody.put(WebserviceConstants.KEY_SCHOOLID, _studentId);
            requestBody.put(WebserviceConstants.KEY_ERROR_TYPE, _type);
            requestBody.put(WebserviceConstants.KEY_ERROR_DESCRIPTION, _description);
            requestBody.put(WebserviceConstants.KEY_ERROR_DATE, _date);
            requestBody.put(WebserviceConstants.KEY_ERROR_DATETIME, _datetime);
            requestBody.put(WebserviceConstants.KEY_ERROR_USERTYPE, _usertype);
            requestBody.put(WebserviceConstants.KEY_ERROR_NAME, _name);
            requestBody.put(WebserviceConstants.KEY_ERROR_PHONE, _phone);
            requestBody.put(WebserviceConstants.KEY_ERROR_APP0NAME, _appname);
            requestBody.put(WebserviceConstants.KEY_ERROR_SUBROUTINENAME, _subroutinename);
            requestBody.put(WebserviceConstants.KEY_ERROR_LINE, _line);
            requestBody.put(WebserviceConstants.KEY_ERROR_STATUS, _status);
            requestBody.put(WebserviceConstants.KEY_ERROR_WEBMETHOD_NAME, _webmethodname);
            requestBody.put(WebserviceConstants.KEY_ERROR_WEBSERVICE_NAME, _webservice);
            requestBody.put(WebserviceConstants.KEY_ERROR_LASTPROGR_NAME, _proname);



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
        ErrorLog errorlog=null;
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

                    String Id = jsonObject.optString(WebserviceConstants.KEY_ERROR_ID);


                    errorlog = new ErrorLog(Id);

                }
                responseObject = new ServerResponse(errorCode, errorlog);

            }  else {
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
        notifier.eventNotify(EventTypes.EVENT_ERROR, eventObject);

    }
}
