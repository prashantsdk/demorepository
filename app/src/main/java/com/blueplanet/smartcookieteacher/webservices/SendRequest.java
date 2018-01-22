package com.blueplanet.smartcookieteacher.webservices;

import android.util.Log;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.communication.SmartCookieTeacherService;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sayali on 8/30/2017.
 */
public class SendRequest extends SmartCookieTeacherService {


    private String _sendreMemberId, _senderEntityId, _recivEntityId, _countrycode, _mobile, _email, _fname, _mname, _lanme, _platform, _sendstatus, _invitationName;
    private final String _TAG = this.getClass().getSimpleName();

    public SendRequest(String sendreMemberId, String senderEntityId, String recivEntityId, String countrycode, String mobile, String email, String fname
            , String mname, String lanme, String platform, String sendstatus, String invitationName) {
        _sendreMemberId = sendreMemberId;
        _senderEntityId = senderEntityId;
        _recivEntityId = recivEntityId;
        _countrycode = countrycode;
        _mobile = mobile;
        _email = email;
        _fname = fname;
        _mname = mname;
        _lanme = lanme;
        _platform = platform;
        _sendstatus = sendstatus;
        _invitationName = invitationName;

    }

    @Override
    protected String formRequest() {

        return WebserviceConstants.HTTP_BASE_URL +
                WebserviceConstants.BASE_URL + WebserviceConstants.SEND_REQUEST_WEB_SERVICE;
    }

    @Override
    protected JSONObject formRequestBody() {

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put(WebserviceConstants.KEY_SEND_REQUEST_MEMBER_ID, _sendreMemberId);
            requestBody.put(WebserviceConstants.KEY_SEND_REQUEST_SENDER_ID, _senderEntityId);
            requestBody.put(WebserviceConstants.KEY_SEND_REQUEST_RECIVER_ID, _recivEntityId);
            requestBody.put(WebserviceConstants.KEY_SEND_REQUEST_COUNTRYCODE, _countrycode);
            requestBody.put(WebserviceConstants.KEY_SEND_REQUEST_MOBILENO, _mobile);
            requestBody.put(WebserviceConstants.KEY_SEND_REQUEST_EMAILID, _email);
            requestBody.put(WebserviceConstants.KEY_SEND_REQUEST_FIRSTNAME, _fname);
            requestBody.put(WebserviceConstants.KEY_SEND_REQUEST_MIDDLENAME, _mname);
            requestBody.put(WebserviceConstants.KEY_SEND_REQUEST_LASTNAME, _lanme);
            requestBody.put(WebserviceConstants.KEY_SEND_REQUEST_PLATFORM, _platform);
            requestBody.put(WebserviceConstants.KEY_SEND_REQUEST_SENDSTATUS, _sendstatus);
            requestBody.put(WebserviceConstants.KEY_SEND_REQUEST_INVITATYION_SENDER_NAME, _invitationName);


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
            statusCode = objResponseJSON.getInt(WebserviceConstants.KEY_STATUS_CODE);
            statusMessage =
                    objResponseJSON.getString(WebserviceConstants.KEY_STATUS_MESSAGE);

            if (statusCode == HTTPConstants.HTTP_COMM_SUCCESS) {
                // success

               /* JSONArray responseData = objResponseJSON.optJSONArray(WebserviceConstants.KEY_POSTS);
                for (int i = 0; i < responseData.length(); i++) {
                    JSONObject jsonObject = responseData.optJSONObject(i);
                  //  String json = jsonObject.toJson(obj);
                   dd=responseJSONString.toString();

                }*/
                responseObject = new ServerResponse(errorCode, null);

            } else {
                // failure
                errorCode = WebserviceConstants.FAILURE;
                responseObject =
                        new ServerResponse(errorCode, new ErrorInfo(statusCode, statusMessage,
                                null));
            }
            fireEvent(responseObject);

        }  catch (JSONException jsonException) {
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
        notifier.eventNotify(EventTypes.EVENT_SEND_REQUEST, eventObject);

    }
}
