package com.blueplanet.smartcookieteacher.featurecontroller;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.utils.SmartCookieSharedPreferences;
import com.blueplanet.smartcookieteacher.webservices.ForgetPassward;
import com.blueplanet.smartcookieteacher.webservices.GetPointShare;
import com.blueplanet.smartcookieteacher.webservices.TeacherLogin;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by 1311 on 03-08-2016.
 */
public class PointShareFeatureController implements IEventListener {

    private static PointShareFeatureController _pointShareFeatureController = null;


    /**
     * function to get single instance of this class
     *
     * @return _loginFeatureController
     */
    public static PointShareFeatureController getInstance() {
        if (_pointShareFeatureController == null) {
            _pointShareFeatureController = new PointShareFeatureController();
        }
        return _pointShareFeatureController;
    }

    /**
     * function to call teacher login ws
     *
     *
     */
    public void FetchPointSharePoints(String tID,String tid2, String point,String reason,String scID) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GetPointShare pointshare = new GetPointShare(tID,tid2,point,reason,scID);
        pointshare.send();

    }


    /**
     * make constructor private
     */
    private PointShareFeatureController() {

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
            case EventTypes.EVENT_POINT_SHARE:

                if (errorCode == WebserviceConstants.SUCCESS) {


                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_POINT_SHARE,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {



                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_POINT_SHARE,
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

