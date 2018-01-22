package com.blueplanet.smartcookieteacher.featurecontroller;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.RegisModel;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.GetRegistration;
import com.blueplanet.smartcookieteacher.webservices.GetTeacherSubjects;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 17-02-2016.
 */
public class RegistrationFeatureController implements IEventListener {


    private static RegistrationFeatureController _regFeatureController = null;
    private RegisModel _registration = null;
    private final String _TAG = this.getClass().getSimpleName();

    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */
    public static RegistrationFeatureController getInstance() {

        if (_regFeatureController == null) {
            _regFeatureController = new RegistrationFeatureController();
        }
        return _regFeatureController;


    }

    /**
     * make constructor private
     */
    private RegistrationFeatureController() {

    }

    /**
     * webservice to fetch teacher subjects from server
     *
     *
     */
    public void fetchRegistrationServer(String fname, String lname,String email,String pass,String phone,String mname,String countrycode,String type,String sourse) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        GetRegistration getRegis = new GetRegistration(fname, lname,email,pass,phone,mname,countrycode,type,sourse);
        getRegis.send();

    }
    public void clearTeacherRegis() {
        if (_registration != null) {
            _registration = null;
        }
    }
    public RegisModel get_registration() {

        return _registration;
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

            case EventTypes.EVENT_REGISTRATION_SUCCESS:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _registration = (RegisModel) responseObject;

                    eventNotifierUI =

                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_REGISTRATION_SUCCESS,
                            serverResponse);
                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_REGISTRATION_SUCCESS,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                serverResponse);

                    } else if(statusCode == HTTPConstants.HTTP_COMM_CONFLICT){
                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                serverResponse);
                    }
                    else if(statusCode == HTTPConstants.HTTP_INVALID_INPUT){
                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_INVALID_INPUT,
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
