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
import com.blueplanet.smartcookieteacher.webservices.SuggetSponsorsList;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 2017 on 11/23/2015.
 * singleton class for handling login related activities
 *
 * @author pramod.shelke
 */
public class SuggestSponsorList_Choosed_LocFeatureController implements IEventListener {

    public static SuggestSponsorList_Choosed_LocFeatureController featureController = null;
    private final String _TAG = this.getClass().getSimpleName();

    private ArrayList<SuggestedSponsorModel> arr_sponsor = null;
    private SuggestedSponsorModel sponsor = null;

    /**
     * private constructor for singleton class
     */
    private SuggestSponsorList_Choosed_LocFeatureController() {

    }

    /**
     * method to get object of this class
     *
     * @return _loginFeatureController
     */
    public static SuggestSponsorList_Choosed_LocFeatureController getInstance() {
        if (featureController == null) {
            featureController = new SuggestSponsorList_Choosed_LocFeatureController();
        }
        return featureController;
    }

    public void getSuggestedSponsorFromServer(String ENTITY, String USER_ID, String LATITUDE, String LONGITUDE,
                                              String CATAGORY,String COUNTRY,String STATE,String CITY) {

        _registerEventListeners();
        SuggetSponsorsList sponsorsList = new SuggetSponsorsList(ENTITY,USER_ID,LATITUDE,LONGITUDE,CATAGORY,COUNTRY,
                                                                  STATE, CITY );
        sponsorsList.send();
    }

    /**
     * function to register event listeners
     */
    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
    }

    public void setSuggestedSponsorLike(int pos,SuggestedSponsorModel sponsorModel) {

        arr_sponsor.set(pos,sponsorModel);
    }

    public SuggestedSponsorModel getSuggestedSponsor() {
        return sponsor;
    }

    public void setSuggestedSponsor(SuggestedSponsorModel sponsor) {

        this.sponsor = sponsor;
    }

    public ArrayList<SuggestedSponsorModel> getSuggestedSponsorList() {

        return arr_sponsor;
    }

    public void clearLog() {
        if (arr_sponsor != null && arr_sponsor.size() > 0) {
            arr_sponsor.clear();
            arr_sponsor = null;
        }
    }

    public void setSelectedLogNull() {
        if (arr_sponsor != null) {
            arr_sponsor = null;
        }
    }

    public void logout() {
        clearLog();
        setSelectedLogNull();
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


            case EventTypes.EVENT_SUGGESTED_SPONSOR_RESPONCE_RECIEVED:
                if (errorCode == WebserviceConstants.SUCCESS) {
                    /**success*/
                    arr_sponsor = (ArrayList<SuggestedSponsorModel>) responseObject;

                    EventNotifier eventNotifier =
                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifier.eventNotifyOnThread(EventTypes.EVENT_UI_SUGGESTED_SPONSOR_RESPONCE_RECIEVED,
                            serverResponse);


                }else{

                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();
                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        EventNotifier eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NO_SUGGESTED_SPONSOR_RESPONCE_RECIEVED,
                                serverResponse);

                    } else if (statusCode == HTTPConstants.HTTP_COMM_ERR_BAD_REQUEST) {

                        EventNotifier  eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
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

