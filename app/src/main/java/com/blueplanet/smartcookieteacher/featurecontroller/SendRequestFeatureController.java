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
import com.blueplanet.smartcookieteacher.webservices.GetAcceptRequest;
import com.blueplanet.smartcookieteacher.webservices.SendRequest;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by Sayali on 8/30/2017.
 */
public class SendRequestFeatureController implements IEventListener {

    private static SendRequestFeatureController _AcceptFeatureController = null;

    private final String _TAG = this.getClass().getSimpleName();

    public String get_selectColor() {
        return _selectColor;
    }

    public void set_selectColor(String _selectColor) {
        this._selectColor = _selectColor;
    }

    private String _selectColor = null;
    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */
    public static SendRequestFeatureController getInstance() {

        if (_AcceptFeatureController == null) {

            _AcceptFeatureController = new SendRequestFeatureController();
        }
        return _AcceptFeatureController;


    }



    /**
     * make constructor private
     */
    private SendRequestFeatureController() {

    }

    /**
     * webservice to fetch reward log from server
     *
     *
     */


    public void getSendRequestListFromServer(String sendreMemberId, String senderEntityId, String recivEntityId, String countrycode, String mobile, String email, String fname
            , String mname, String lanme, String platform, String sendstatus, String invitationName) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        SendRequest sendrequest = new SendRequest( sendreMemberId,  senderEntityId,  recivEntityId,  countrycode,  mobile,  email,  fname
                ,  mname,  lanme,  platform,  sendstatus,  invitationName);
        sendrequest.send();
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
       // int someConstant = WebserviceConstants.SUCCESS;

        EventNotifier eventNotifierUI;

        switch (eventType) {

            case EventTypes.EVENT_SEND_REQUEST:

                if (errorCode == WebserviceConstants.SUCCESS) {

                    eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_SEND_REQUEST,
                            serverResponse);

                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =

                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_SEND_REQUEST,
                                serverResponse);

                    }  else if (statusCode == HTTPConstants.HTTP_COMM_CONFLICT) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_CONFLCTLOGIN_RESPONSE,
                                serverResponse);


                    }else if (statusCode == HTTPConstants.HTTP_INVALID_INPUT) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_INVALID_INPUT,
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
