package com.blueplanet.smartcookieteacher.communication;

import android.util.Log;


import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by web on 09-07-2015.
 *
 * @author dhanashree.ghayal
 */
public abstract class BaseWebservice implements IEventListener {
    protected int _requestType = HTTPRequestType.HTTP_POST;
    protected String _requestBody = null;
    private Map<String, String> _requestHeaders = null;
    private String requestUrl = null;
    private boolean _showErrors = false;

    protected String jsonResponse = null;
    protected ServerResponse serverResponse = null;

    private String _tag = this.getClass().getName();

    /**
     * method to set show errors flag true
     */
    protected void showErrors() {
        _showErrors = true;
    }

    /**
     * method to form server request
     *
     * @return request url in string form
     */
    protected abstract String formRequest();

    /**
     * method to form server request body
     *
     * @return request url in string form
     */
    protected abstract JSONObject formRequestBody();

    /**
     * method to form server request headers
     *
     * @return request url in string form
     */
    protected abstract Map<String, String> formHeaders();

    /**
     * method to parse server response
     */
    protected abstract void parseResponse(String responseJSONString);

    /**
     * method to start actual server communication
     *
     * @return 0 for successful server communication, else 1
     */
    public int send() {

        if (requestUrl == null) {
            requestUrl = formRequest();
            JSONObject requestBodyJson = formRequestBody();
            if (requestBodyJson != null) {
                _requestBody = requestBodyJson.toString();
            }
            // _requestHeaders = formHeaders( );
        }

        Log.i(_tag, "Request URL = " + requestUrl);
        Log.i(_tag, "Request Body = " + _requestBody);

        if (requestUrl == null) {
            fireEvent(null);
            return 1;
        }

       HTTPCommunication httpCommunication = new HTTPCommunication();

            try {

                /**
                 * if (network available) { perform operation if(operation failed or empty response){
                 * error handling }else{ do nothing. application specific handling of the response to be
                 * done in the application specific base class. } }else{ show error }
                 *
                 */
                NetworkManager networkManager = new NetworkManager();
                if (networkManager.isConnected()) {
                    serverResponse =
                            httpCommunication.performOperation(_requestType, requestUrl,
                                /*_requestHeaders*/null, _requestBody);
                } else {

                    serverResponse =
                            new ServerResponse(WebserviceConstants.FAILURE, new ErrorInfo(
                                    HTTPConstants.HTTP_NO_NETWORK, null, null));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        return 0;
    }

    /*
     * private void showDialog(String title, String message, int buttonCount){
     * if(!_isErrorDialogGettingProcessed){ _isErrorDialogGettingProcessed = true; _errorDialog =
     * new ErrorDialog(VMEServer.this, title,message, buttonCount); } }
     */

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
        // in case of retry, start commn thread again.
        // in case of no-retry notify null
        // add new error notifier.

        switch (eventType) {

            case EventTypes.EVENT_ON_ERROR_RETRY: {
                send();
            }
            break;
            case EventTypes.EVENT_ON_ERROR_CANCEL: {
                Thread eventNotifyThread = new Thread(new Runnable() {
                    public void run() {
                        fireEvent(null);
                    }
                });
                eventNotifyThread.start();
            }
            break;
        }
        return EventState.EVENT_PROCESSED;
    }

    public abstract void fireEvent(Object eventObject);

}
