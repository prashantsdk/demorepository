package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.NearBySponsor;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;


/**
 * Created by 2017 on 11/23/2015.
 * singleton class for handling login related activities
 *
 * @author pramod.shelke
 */
public class SponsorsFeatureController implements IEventListener {

    public static SponsorsFeatureController _SponsorsFeatureController = null;
    private ArrayList<NearBySponsor> arr_nearbySponsor = null;
    private NearBySponsor nearBySponsor = null;
    private final String _TAG = this.getClass().getSimpleName();
    boolean flag = false;

    /**
     * method to get object of this class
     *
     * @return _loginFeatureController
     */
    public static SponsorsFeatureController getInstance() {
        if (_SponsorsFeatureController == null) {
            _SponsorsFeatureController = new SponsorsFeatureController();
        }
        return _SponsorsFeatureController;
    }

    /**
     * private constructor for singleton class
     */
    private SponsorsFeatureController() {

    }


    public void getSponsorListFromServer( String slat, String slong,String distance) {
        _registerEventListeners();
       /* StudentSponsors GetSponsorsList = new StudentSponsors(slat, slong , distance);

        GetSponsorsList.send();*/
    }

    /**
     * function to register event listeners
     */
    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
    }

    public void setSeletedSponsor(NearBySponsor nearBySponsor) {

        this.nearBySponsor = nearBySponsor;
    }

    public NearBySponsor getSeletedSponsor() {
        return nearBySponsor;
    }

    public ArrayList<NearBySponsor> getSponsors() {

        return arr_nearbySponsor;
    }

    public void clearSponsorList() {
        if (arr_nearbySponsor != null && arr_nearbySponsor.size() > 0) {
            arr_nearbySponsor.clear();
            arr_nearbySponsor = null;
        }
    }

    public void setSelectedSponsorNull() {
        if (arr_nearbySponsor != null) {
            arr_nearbySponsor = null;
        }
    }

    public void logout() {
        clearSponsorList();
        setSelectedSponsorNull();
    }

    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        EventNotifier eventNotifierReviews =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
        eventNotifierReviews.unRegisterListener(this);

        Log.i(_TAG, " " + eventType);
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = serverResponse.getErrorCode();
        Log.i(_TAG, "Error code id:" + errorCode);
        Object responseObject = serverResponse.getResponseObject();

        switch (eventType) {


            case EventTypes.EVENT_SCHOOL_ON_MAP_RESPONCE_RECIEVED:
                if (errorCode == WebserviceConstants.SUCCESS) {
                    /**success*/
                    arr_nearbySponsor = (ArrayList<NearBySponsor>) responseObject;

                    EventNotifier eventNotifier =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_STUDENT);
                    eventNotifier.eventNotifyOnThread(EventTypes.EVENT_UI_SPONSOR_RESPONCE_RECIEVED,
                            serverResponse);


                }else{

                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();
                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        EventNotifier eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_STUDENT);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NO_SPONSOR_RESPONCE_RECIEVED,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        EventNotifier  eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_STUDENT);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                serverResponse);

                    } else {

                        EventNotifier  eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_STUDENT);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_UNAUTHORIZED,
                                serverResponse);
                    }
                }

                break;
        }
        return EventState.EVENT_PROCESSED;
    }
}

