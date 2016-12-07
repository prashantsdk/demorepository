package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.SponsorOnMapModel;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.SponsorMapService;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;


/**
 * Created by 2017 on 11/23/2015.
 * singleton class for handling login related activities
 *
 * @author pramod.shelke
 */
public class SponsorsOnMapFeatureController implements IEventListener {

    public static SponsorsOnMapFeatureController _SponsorsFeatureController = null;
    private ArrayList<SponsorOnMapModel> arr_data = null;
    private SponsorOnMapModel onMapModel = null;
    private final String _TAG = this.getClass().getSimpleName();
    boolean flag = false;
    private int input_id = 0;
    /**
     * method to get object of this class
     *
     * @return _loginFeatureController
     */
    public static SponsorsOnMapFeatureController getInstance() {
        if (_SponsorsFeatureController == null) {
            _SponsorsFeatureController = new SponsorsOnMapFeatureController();
        }
        return _SponsorsFeatureController;
    }

    /**
     * private constructor for singleton class
     */
    private SponsorsOnMapFeatureController() {

    }

    public int getInput_id() {
        return input_id;
    }

    public void setInput_id(int input_id) {
        this.input_id = input_id;
    }

    public void getSponsorListFromServer(int ip_id, String slat, String slong,String entity,String place_name,String loc_type,
                                          String distance,String range_type) {
        _registerEventListeners();
        SponsorMapService sponsorMapService = new SponsorMapService(ip_id,slat, slong , entity,place_name,loc_type,distance,range_type);
        sponsorMapService.send();
    }

    /**
     * function to register event listeners
     */
    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
    }

    public void setSeletedSponsor(SponsorOnMapModel onMapModel) {

        this.onMapModel = onMapModel;
    }

    public SponsorOnMapModel getSeletedSponsor() {
        return onMapModel;
    }

    public ArrayList<SponsorOnMapModel> getSponsors() {

        return arr_data;
    }

    public void clearSponsorList() {
        if (arr_data != null && arr_data.size() > 0) {
            arr_data.clear();
            arr_data = null;
        }
    }

    public void setSelectedSponsorNull() {
        if (arr_data != null) {
            arr_data = null;
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
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
        eventNotifierReviews.unRegisterListener(this);

        Log.i(_TAG, " " + eventType);
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = serverResponse.getErrorCode();
        Log.i(_TAG, "Error code id:" + errorCode);
        Object responseObject = serverResponse.getResponseObject();

        switch (eventType) {


            case EventTypes.EVENT_SPONSOR_ON_MAP_RESPONCE_RECIEVED:
                if (errorCode == WebserviceConstants.SUCCESS) {
                    /**success*/
                    arr_data = (ArrayList<SponsorOnMapModel>) responseObject;
                    if (arr_data!=null){
                        setInput_id(arr_data.size());
                    }
                    EventNotifier eventNotifier =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_LOCATION);
                    eventNotifier.eventNotifyOnThread(EventTypes.EVENT_UI_SPONSOR_ON_MAP_RESPONCE_RECIEVED,
                            serverResponse);


                }else{

                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();
                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        EventNotifier eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_LOCATION);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NO_SPONSOR_ON_MAP_RESPONCE_RECIEVED,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        EventNotifier  eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_LOCATION);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_BAD_REQUEST,
                                serverResponse);

                    } else {

                        EventNotifier  eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_LOCATION);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_UNAUTHORIZED,
                                serverResponse);
                    }
                }

                break;
        }
        return EventState.EVENT_PROCESSED;
    }
}

