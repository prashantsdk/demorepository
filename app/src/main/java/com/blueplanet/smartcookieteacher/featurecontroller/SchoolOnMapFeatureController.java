package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.SchoolOnMapModel;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.SchoolMapService;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;


/**
 * Created by 2017 on 11/23/2015.
 * singleton class for handling login related activities
 *
 * @author pramod.shelke
 */
public class SchoolOnMapFeatureController implements IEventListener {

    public static SchoolOnMapFeatureController _schoolOnMapFeatureController = null;
    private ArrayList<SchoolOnMapModel> arr_data = null;
    private SchoolOnMapModel onMapModel = null;
    private final String _TAG = this.getClass().getSimpleName();
    //boolean flag = false;
    private int input_id = 0;

    /**
     * method to get object of this class
     *
     * @return _loginFeatureController
     */
    public static SchoolOnMapFeatureController getInstance() {
        if (_schoolOnMapFeatureController == null) {
            _schoolOnMapFeatureController = new SchoolOnMapFeatureController();
        }
        return _schoolOnMapFeatureController;
    }

    /**
     * private constructor for singleton class
     */
    private SchoolOnMapFeatureController() {

    }

    public int getInput_id() {
        return input_id;
    }

    public void setInput_id(int input_id) {
        this.input_id = input_id;
    }

    public void getSchoolListFromServer(int ip_id, String slat, String slong,String entity,String place_name,String loc_type,
                                          String distance,String range_type) {
        _registerEventListeners();
        SchoolMapService schoolMapService = new SchoolMapService(ip_id,slat, slong , entity,place_name,loc_type,distance,range_type);
        schoolMapService.send();
    }
    /**
     * function to register event listeners
     */
    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOCATION);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
    }

    public void setSeletedSchool(SchoolOnMapModel onMapModel) {

        this.onMapModel = onMapModel;
    }

    public SchoolOnMapModel getSeletedSchool() {
        return onMapModel;
    }

    public ArrayList<SchoolOnMapModel> getSchools() {

        return arr_data;
    }

    public void clearSchoolList() {
        if (arr_data != null && arr_data.size() > 0) {
            arr_data.clear();
            arr_data = null;
        }
    }

    public void setSelectedSchoolNull() {
        if (arr_data != null) {
            arr_data = null;
        }
    }

    public void logout() {
        clearSchoolList();
        setSelectedSchoolNull();
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


            case EventTypes.EVENT_SCHOOL_ON_MAP_RESPONCE_RECIEVED:
                if (errorCode == WebserviceConstants.SUCCESS) {
                    /**success*/
                    arr_data = (ArrayList<SchoolOnMapModel>) responseObject;

                    EventNotifier eventNotifier =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_LOCATION);
                    eventNotifier.eventNotifyOnThread(EventTypes.EVENT_UI_SCHOOL_ON_MAP_RESPONCE_RECIEVED,
                            serverResponse);


                }else{

                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();
                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        EventNotifier eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_LOCATION);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NO_SCHOOL_ON_MAP_RESPONCE_RECIEVED,
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

