package com.blueplanet.smartcookieteacher.featurecontroller;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.GetPointShare;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;
import com.blueplanet.smartcookieteacher.webservices.logout;

/**
 * Created by 1311 on 22-09-2016.
 */
public class LogoutFeatureController implements IEventListener {

    private static LogoutFeatureController _logfeature = null;


    /**
     * function to get single instance of this class
     *
     * @return _loginFeatureController
     */
    public static LogoutFeatureController getInstance() {
        if (_logfeature == null) {
            _logfeature = new LogoutFeatureController();
        }
        return _logfeature;
    }

    /**
     * function to call teacher login ws
     *
     *
     */
    public void FetchLogoutData(int ID) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        logout _log = new logout(ID);
        _log.send();

    }


    /**
     * make constructor private
     */
    private LogoutFeatureController() {

    }


    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.unRegisterListener(this);

        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = serverResponse.getErrorCode();
        Object responseObject = serverResponse.getResponseObject();
        EventNotifier eventNotifierUI;

        switch (eventType) {
            case EventTypes.EVENT_LOGOUT:

                if (errorCode == WebserviceConstants.SUCCESS) {


                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_LOGOUT,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {


                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_NOT_UI_LOGOUT,
                                serverResponse);

                    }
                    else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                serverResponse);

                    }
                    else {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_UNAUTHORIZED,
                                serverResponse);
                    }
                }
                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }
        return EventState.EVENT_PROCESSED;
    }
}


