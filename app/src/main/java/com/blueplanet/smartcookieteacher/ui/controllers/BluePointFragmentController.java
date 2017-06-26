package com.blueplanet.smartcookieteacher.ui.controllers;

import android.util.Log;
import android.view.View;

import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.BluePointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.RewardPointLogFeatureController;
import com.blueplanet.smartcookieteacher.models.BlueLog;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.BluePointFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;


/**
 * Created by 1311 on 13-02-2016.
 */
public class BluePointFragmentController implements IEventListener {


    private BluePointFragment _blueFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private BlueLog _blueLog;
    private ArrayList<BlueLog> _bluePointList;
    private String _teacherId, _schoolId;
    private Teacher _teacher;

    /**
     * constructur for reward list
     */


    public BluePointFragmentController(BluePointFragment blueFragment, View view) {

        _blueFragment = blueFragment;
        _view = view;
        _teacher = LoginFeatureController.getInstance().getTeacher();
        //_rewardList = RewardPointLogFeatureController.getInstance().getRewardFromDB();
       _bluePointList = BluePointFeatureController.getInstance().getBlueFromDB();
        if ((_isBluePopulated(_bluePointList))) {
            Log.i(_TAG, "Subject list got from DB");

            Log.i(_TAG, "Subject list size:" + _bluePointList.size());
            _blueFragment.showOrHideProgressBar(false);
            _blueFragment.refreshListview();
        } else {
            Log.i(_TAG, "Subject list got from webservice");
            _teacher = LoginFeatureController.getInstance().getTeacher();
            if (_teacher != null) {
                _teacherId = _teacher.get_tId();
                _schoolId=_teacher.get_tSchool_id();
                _teacher = LoginFeatureController.getInstance().getTeacher();
                _fetchBlueListFromServer(_teacherId, _schoolId);

            }
        }

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

        if (_blueFragment != null) {
            _blueFragment = null;
        }
    }

    /**
     * webservice call to fetch reward list from server
     *
     * @param teacherId
     * @param schoolId
     */
    private void _fetchBlueListFromServer(String teacherId, String schoolId) {
        Log.i(_TAG, "Blue point Webservice called");
        _registerEventListeners();
        BluePointFeatureController.getInstance().getBluePointFromServer(teacherId, schoolId);
        _blueFragment.showOrHideProgressBar(true);
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
            case EventTypes.EVENT_UI_BLUE_POINT_SUCCESS:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In EVENT_UI_BLUE_POINT_SUCCESS");
                    _blueFragment.showOrHideProgressBar(false);
                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */
                    _bluePointList = BluePointFeatureController.getInstance().get_bluepoint();
                    _blueFragment.showNoBluePointMessage(false);
                    _blueFragment.setVisibilityOfListView(true);
                    _blueFragment.refreshListview();

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

                _blueFragment.showNetworkToast(false);
                break;

            case EventTypes.EVENT_UI_NOT_BLUE_POINT_SUCCESS:
                EventNotifier event2 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event2.unRegisterListener(this);
                _blueFragment.showNoBluePointMessage(true);
                _blueFragment.showOrHideProgressBar(false);
                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;
    }
}
