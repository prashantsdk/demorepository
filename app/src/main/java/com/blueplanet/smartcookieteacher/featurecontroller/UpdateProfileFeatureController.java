package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.UpdateProfile;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by Sayali on 3/23/2017.
 */
public class UpdateProfileFeatureController implements IEventListener {



    private static UpdateProfileFeatureController _profileFeatureController = null;
    private final String _TAG = this.getClass().getSimpleName();
    public String parentImage = null;

    private UpdateProfileFeatureController() {

    }

    public static UpdateProfileFeatureController getInstance() {
        if (_profileFeatureController == null) {
            _profileFeatureController = new UpdateProfileFeatureController();
        }
        return _profileFeatureController;
    }


    public void updateProfileInfo(String email, String fname, String lname,String dob,String address,String city,String country,
                                  String gender,String passward,String phone, String state,String studentId,
                                  String countrycode, String memberID,String Key) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
        UpdateProfile update = new UpdateProfile( email,  fname,  lname,dob,address,city,country,gender,passward,phone,state,studentId,countrycode,memberID,Key);
        update.send();
    }


    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        EventNotifier eventNotifierReviews =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifierReviews.unRegisterListener(this);

        Log.i(_TAG, " " + eventType);
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = serverResponse.getErrorCode();
        Log.i(_TAG, "Error code id:" + errorCode);
        Object responseObject = serverResponse.getResponseObject();

        switch (eventType) {

            case EventTypes.EVENT_TEACHER_UPDATE_PROFILE:
                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In success ");
                    /**success*/

                    EventNotifier eventNotifierUI =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_TEACHER_UI_UPDATE_PROFILE,
                            serverResponse);

                } else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();
                    Log.i(_TAG, "In failure ");
                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        EventNotifier eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_TEACHER_UI_NOT_UPDATE_PROFILE,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        EventNotifier eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                serverResponse);

                    } else {

                        EventNotifier eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_UNAUTHORIZED,
                                serverResponse);
                    }
                }

                break;


            /**success*/

            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }
        return EventState.EVENT_PROCESSED;

    }


    public String getParentImage() {
        return parentImage;
    }

    public void setParentImage(String parentImage) {
        this.parentImage = parentImage;
    }
}
