package com.blueplanet.smartcookieteacher.communication;

import android.util.Log;


import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.utils.SmartCookieSharedPreferences;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;



import org.apache.http.HttpStatus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by web on 09-07-2015.
 *
 * @author dhanashree.ghayal
 */
public abstract class SmartCookieTeacherService extends BaseWebservice implements IEventListener {
    @Override
    protected abstract String formRequest();

    private String _tag = this.getClass().getName();

    /**
     * Method to set request type
     */
    protected void setRequestType(int requestType) {
        _requestType = requestType;
    }

    /**
     * method to form server request body
     *
     * @return request body in string form
     */
    protected abstract JSONObject formRequestBody();

    /**
     * method to form server request headers
     *
     * @return requestHeaders
     */
    protected Map<String, String> formHeaders() {

        Map<String, String> requestHeaders = new HashMap<String, String>();
        String length = String.valueOf(_requestBody.length());
        return requestHeaders;
    }


    @Override
    protected abstract void parseResponse(String responseJSONString);


    @Override
    public abstract void fireEvent(Object eventObject);

    @Override
    public int send() {
        Thread requestExecutor = new Thread(new Runnable() {

            public void run() {
                SmartCookieTeacherService.super.send();
                if (serverResponse != null) {
                    if (serverResponse.getErrorCode() == WebserviceConstants.SUCCESS) {
                        jsonResponse = (String) serverResponse.getResponseObject();
                        if (jsonResponse != null && jsonResponse.length() > 0) {
                            Log.i(_tag, jsonResponse);
                            // we have some valid response. check it for validity
                            try {

                                /**
                                 * Common error codes and success codes are not present in the
                                 * current web services' implementation Hence, common handling of
                                 * errors can not be done in the base class. Individual web service
                                 * classes have to do error handling.
                                 */
                                parseResponse(jsonResponse);
                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                        }
                    } else {
                        ErrorInfo errorInfo = (ErrorInfo) serverResponse.getResponseObject();
                        String errorMessage = _getErrorMessage(errorInfo.getErrorCode());
                        errorInfo.setErrorMessage(errorMessage);
                        serverResponse.setResponseObject(errorInfo);
                        fireEvent(serverResponse);

                    }


                }
            }
        });

        requestExecutor.start();
        return 0;

    }

    private String _getErrorMessage(int errorCode) {
        String errorMessage = null;
        switch (errorCode) {
            case HTTPConstants.HTTP_NO_NETWORK:
                errorMessage =
                        MainApplication.getContext().getString(
                                R.string.ERROR_TITLE_NO_INTERNET_CONNECTIVITY);
                break;

            case HttpStatus.SC_REQUEST_TIMEOUT:
            case HttpStatus.SC_GATEWAY_TIMEOUT:
                errorMessage =
                        MainApplication.getContext()
                                .getString(R.string.msg_the_request_timed_out);

            case HttpStatus.SC_NO_CONTENT:
                errorMessage =
                        MainApplication.getContext()
                                .getString(R.string.ERROR_HTTP_COM_NO_CONTENT);
                break;

            case HttpStatus.SC_BAD_REQUEST:
                errorMessage =
                        MainApplication.getContext().getString(
                                R.string.ERROR_HTTP_COMM_ERR_BAD_REQUEST);
                break;

            case HttpStatus.SC_UNAUTHORIZED:
                errorMessage =
                        MainApplication.getContext().getString(
                                R.string.ERROR_HTTP_COMM_ERR_UNAUTHORIZED);
                break;

            case HttpStatus.SC_FORBIDDEN:
                errorMessage =
                        MainApplication.getContext().getString(
                                R.string.ERROR_HTTP_COMM_ERR_FORBIDDEN);
                break;

            case HttpStatus.SC_NOT_FOUND:
                errorMessage =
                        MainApplication.getContext().getString(
                                R.string.ERROR_HTTP_COMM_ERR_NOT_FOUND);
                break;

            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                errorMessage =
                        MainApplication.getContext().getString(
                                R.string.ERROR_HTTP_INTERNAL_SERVER_ERROR);
                break;

            case HttpStatus.SC_BAD_GATEWAY:
                errorMessage =
                        MainApplication.getContext().getString(
                                R.string.ERROR_HTTP_COMM_ERR_BAD_GETWAY);
                break;

            case HttpStatus.SC_SERVICE_UNAVAILABLE:
                errorMessage =
                        MainApplication.getContext().getString(
                                R.string.ERROR_HTTP_COMM_ERR_SERVICE_UNAVAILABLE);
                break;

            default:
                errorMessage =
                        MainApplication.getContext().getString(R.string.ERROR_DESC_NOT_FOUND);
                break;

        }
        return errorMessage;
    }

    /**
     * This method is called when any registered event is fired. Need to be implemented by all sub
     * classes.
     *
     * @param eventType   Constant indicating type of Event
     * @param eventObject Object containing extra information regarding event.
     * @return One of the Event state EVENT_CONSUMED/ EVENT_PROCESSED/ EVENT_IGNORED from
     * {@link EventState}
     */
    public int eventNotify(int eventType, Object eventObject) {

        return EventState.EVENT_PROCESSED;
    }


}
