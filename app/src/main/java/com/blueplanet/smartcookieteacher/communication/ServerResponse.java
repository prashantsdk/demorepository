package com.blueplanet.smartcookieteacher.communication;

/**
 * Created by web on 30-06-2015.
 * @author dhanashree.ghayal
 */
public class ServerResponse {
    private int _errorCode;
    private String _statusMessage;

    private Object _responseObject;

    public ServerResponse(int errorCode, Object responseObject) {

        _errorCode = errorCode;
        _responseObject = responseObject;
    }

    public ServerResponse(int errorCode, Object responseObject, String statusMessage) {

        _errorCode = errorCode;
        _responseObject = responseObject;
        _statusMessage = statusMessage;
    }

    public int getErrorCode() {
        return _errorCode;
    }

    public String getStatusMessage() {
        return _statusMessage;
    }

    public Object getResponseObject() {
        return _responseObject;
    }

    public void setResponseObject(Object responseObject) {
        _responseObject = responseObject;
    }

}
