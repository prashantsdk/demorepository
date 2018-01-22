package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.BluePointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.NewSuggestSponsorFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SuggestSponsorList_Choosed_LocFeatureController;
import com.blueplanet.smartcookieteacher.models.BlueLog;
import com.blueplanet.smartcookieteacher.models.SuggestedSponsorModel;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.BluePointFragment;
import com.blueplanet.smartcookieteacher.ui.GPSTracker;
import com.blueplanet.smartcookieteacher.ui.SugestSponserFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Sayali on 7/20/2017.
 */
public class SuggestSponsorController implements IEventListener {


    private SugestSponserFragment _Fragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private BlueLog _blueLog;
    private ArrayList<SuggestedSponsorModel> _sonsorList;
    private String _teacherId, _schoolId,entityId;

    private Teacher _teacher;
    double latitude = 0.0, longitude = 0.0;
    GPSTracker gpsTracker;

    /**
     * constructur for reward list
     */


    public SuggestSponsorController(SugestSponserFragment Fragment, View view) {

        _Fragment = Fragment;
        _view = view;
        _teacher = LoginFeatureController.getInstance().getTeacher();
        String entity = "3";
        //_rewardList = RewardPointLogFeatureController.getInstance().getRewardFromDB();
        _sonsorList = NewSuggestSponsorFeatureController.getInstance().get_bluepoint();
        _teacher = LoginFeatureController.getInstance().getTeacher();
        gpsTracker = new GPSTracker(_Fragment.getActivity());
        if (gpsTracker.canGetLocation()) {

            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

            String lat = String.valueOf(latitude);
            String lon = String.valueOf(longitude);

        if (_teacher != null) {
            _teacherId = _teacher.get_tId();
            entityId = String.valueOf(_teacher.getId());
            String catrgory = "";
            _fetchSuggestSponsorListFromServer(entity, entityId, catrgory, lat,lon,"", "", "");

        }}

   /*     if (_teacher != null) {
            _teacherId = _teacher.get_tId();

            _schoolId = _teacher.get_tSchool_id();
            _fetchBlueListFromServer(_teacherId, _schoolId);
        }*/

    }

    private boolean _isBluePopulated(ArrayList<BlueLog> list) {
        if (list != null && list.size() > 0) {
            return true;
        }

        return false;

    }


    private void _registerEventListeners() {


        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

    }

    private void _unRegisterEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier
                        (NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.unRegisterListener(this);

        EventNotifier eventNetwork =
                NotifierFactory.getInstance().getNotifier
                        (NotifierFactory.EVENT_NOTIFIER_NETWORK);
        eventNetwork.unRegisterListener(this);
    }

    public void clear() {
        _unRegisterEventListeners();

        if (_Fragment != null) {
            _Fragment = null;
        }
    }

    /**
     * webservice call to fetch reward list from server
     *
     * @param
     */
    private void _fetchSuggestSponsorListFromServer(String ENTITY, String USER_ID, String LATITUDE, String LONGITUDE,
                                                    String CATAGORY, String COUNTRY, String STATE, String CITY) {
        Log.i(_TAG, "Blue point Webservice called");
        _registerEventListeners();
        NewSuggestSponsorFeatureController.getInstance()._fetchSuggestSponsorListFromServer(ENTITY, USER_ID, LATITUDE, LONGITUDE, CATAGORY, COUNTRY,
                STATE, CITY);
        //  _Fragment.showOrHideProgressBar(true);
    }


    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = -1;

        if (serverResponse != null) {
            errorCode = serverResponse.getErrorCode();
        }

        switch (eventType) {
            case EventTypes.EVENT_UI_SUGGESTED_SPONSOR_RESPONCE_RECIEVED:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In EVENT_UI_BLUE_POINT_SUCCESS");

                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */
                    _sonsorList = NewSuggestSponsorFeatureController.getInstance().get_bluepoint();
                    _Fragment.setVisibilityOfListView(true);
                    _Fragment.refreshListview();

                    _Fragment.SuggeatCouponSuccessfullyPoint();

                }
                break;

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                break;

            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);

                _Fragment.showNetworkToast(false);
                break;

            case EventTypes.EVENT_UI_NO_SUGGESTED_SPONSOR_RESPONCE_RECIEVED:
                EventNotifier event2 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event2.unRegisterListener(this);
                // _Fragment.showNoBluePointMessage(true);

                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;
    }
}

