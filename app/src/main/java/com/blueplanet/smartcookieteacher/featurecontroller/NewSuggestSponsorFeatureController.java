package com.blueplanet.smartcookieteacher.featurecontroller;

import android.util.Log;

import com.blueplanet.smartcookieteacher.DatabaseManager.IPersistence;
import com.blueplanet.smartcookieteacher.DatabaseManager.PersistenceFactory;
import com.blueplanet.smartcookieteacher.DatabaseManager.SmartTeacherDatabaseMasterTable;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.models.BlueLog;
import com.blueplanet.smartcookieteacher.models.SuggestedSponsorModel;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.GetBluePoint;
import com.blueplanet.smartcookieteacher.webservices.SuggetSponsorsList;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Sayali on 7/20/2017.
 */
public class NewSuggestSponsorFeatureController implements IEventListener {


    private static NewSuggestSponsorFeatureController _blurpoint = null;
    private ArrayList<SuggestedSponsorModel> SponsorList= new ArrayList<>();
    private final String _TAG = this.getClass().getSimpleName();
    /**
     * function to get single instance of this class
     *
     * @return _dashboardFeatureController
     */

    public  static NewSuggestSponsorFeatureController getInstance(){

        if (_blurpoint == null) {

            _blurpoint = new NewSuggestSponsorFeatureController();
        }
        return _blurpoint;

    }

    public static NewSuggestSponsorFeatureController get_blurpoint() {
        return _blurpoint;
    }



    private NewSuggestSponsorFeatureController(){

    }

    /**
     * webservice to fetch reward log from server
     *
     */


    public void _fetchSuggestSponsorListFromServer(String ENTITY, String USER_ID, String LATITUDE, String LONGITUDE,
                                                   String CATAGORY, String COUNTRY, String STATE, String CITY) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        SuggetSponsorsList sponsorList = new SuggetSponsorsList(ENTITY, USER_ID, LATITUDE, LONGITUDE, CATAGORY, COUNTRY,
                STATE, CITY);
        sponsorList.send();

    }

    public ArrayList<SuggestedSponsorModel> get_bluepoint() {
        return SponsorList;
    }
    private void _clearBlueList() {
        if (SponsorList != null && SponsorList.size() > 0) {
            SponsorList.clear();
        }}





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

            case EventTypes.EVENT_SUGGESTED_SPONSOR_RESPONCE_RECIEVED:

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In EVENT_SPONSOR_POINT_SUCCESS");

                    SponsorList = (ArrayList<SuggestedSponsorModel>) responseObject;
                    Log.i(_TAG, "size is:" + SponsorList.size());


                    eventNotifierUI =

                            NotifierFactory.getInstance().getNotifier(
                                    NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_SUGGESTED_SPONSOR_RESPONCE_RECIEVED,
                            serverResponse);
                }else {
                    ErrorInfo errorInfo = (ErrorInfo) responseObject;
                    int statusCode = errorInfo.getErrorCode();

                    if (statusCode == HTTPConstants.HTTP_COM_NO_CONTENT) {

                        eventNotifierUI =
                                NotifierFactory.getInstance().getNotifier(
                                        NotifierFactory.EVENT_NOTIFIER_TEACHER);
                        eventNotifierUI.eventNotifyOnThread(EventTypes.EVENT_UI_NOT_BLUE_POINT_SUCCESS,
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
