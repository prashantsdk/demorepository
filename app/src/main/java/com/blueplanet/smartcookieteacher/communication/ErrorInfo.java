package com.blueplanet.smartcookieteacher.communication;

/**
 * Created by web on 30-06-2015.
 * @author dhanashree.ghayal
 */
public class ErrorInfo {
    private int _errorCode;

    private String _errorMessage;

    private String _errorTitle;

    /**
     * @param _errorCode
     * @param _errorMessage
     * @param _errorTitle
     */
    public ErrorInfo(int _errorCode, String _errorMessage, String _errorTitle) {
        this._errorCode = _errorCode;
        this._errorMessage = _errorMessage;
        this._errorTitle = _errorTitle;
    }

    public void setErrorMessage(String errorMessage) {
        _errorMessage = errorMessage;
    }

    /**
     * @return the _errorCode
     */
    public int getErrorCode() {
        return _errorCode;
    }

    /**
     * @return the _errorMessage
     */
    public String getErrorMessage() {
        return _errorMessage;
    }

    /**
     * @return the _errorTitle
     */
    public String getErrorTitle() {
        return _errorTitle;
    }


}
