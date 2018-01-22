package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.SuggestedSponsorModel;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.SuggestNewSponsor;
import com.blueplanet.smartcookieteacher.webservices.SuggetSponsorsList;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Sayali on 7/21/2017.
 */
public class SuggestNewSponsorFeatureController implements IEventListener {


    private static SuggestNewSponsorFeatureController _blurpoint = null;

    private final String _TAG = this.getClass().getSimpleName();
    private String _emailID = null;

    public String get_emailID() {
        return _emailID;
    }

    public void set_emailID(String _emailID) {
        this._emailID = _emailID;
    }

    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */

    public  static SuggestNewSponsorFeatureController getInstance(){

        if (_blurpoint == null) {

            _blurpoint = new SuggestNewSponsorFeatureController();
        }
        return _blurpoint;

    }

    public static SuggestNewSponsorFeatureController get_blurpoint() {
        return _blurpoint;
    }



    private SuggestNewSponsorFeatureController(){

    }

    /**
     * webservice to fetch reward log from server
     *
     */


    public void SuggestNewSponsor(String USER_MEM_ID,String  VENDOR_NAME,String VENDOR_CATEGORY,String  VENDOR_EMAIL,String  VENDOR_PHONE,
                                  String VENDOR_IMAGE,String  VENDOR_IMAGE_BASE64,String  VENDOR_ADDRESS,String  VENDOR_CITY,
                                  String VENDOR_STATE,String  VENDOR_COUNTRY,String VENDOR_LAT,String VENDOR_LONG,String VENDOR_ENTITY, String  _VERSION_VENDER,String _PLATFORM_SOURSE,
                                  String PLATFORM_LATITUDE,String PLATFORM_LOGITITUDE) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
        SuggestNewSponsor suggestNewSponsor=new SuggestNewSponsor(USER_MEM_ID, VENDOR_NAME,VENDOR_CATEGORY, VENDOR_EMAIL, VENDOR_PHONE,
                VENDOR_IMAGE, VENDOR_IMAGE_BASE64, VENDOR_ADDRESS, VENDOR_CITY,
                VENDOR_STATE, VENDOR_COUNTRY,VENDOR_LAT,VENDOR_LONG,VENDOR_ENTITY, _VERSION_VENDER, _PLATFORM_SOURSE,PLATFORM_LATITUDE,PLATFORM_LOGITITUDE);

        suggestNewSponsor.send();
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

            case EventTypes.EVENT_SUGGEST_NEW_SPONSOR_RESPONCE_RECIEVED:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In EVENT_SPONSOR_POINT_SUCCESS");


                    eventNotifierUI =

                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_SUGGEST_NEW_SPONSOR_RESPONCE_RECIEVED,
                            serverResponse);
                }else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NO_SUGGEST_NEW_SPONSOR_RESPONCE_RECIEVED,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                serverResponse);

                    } else {

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
