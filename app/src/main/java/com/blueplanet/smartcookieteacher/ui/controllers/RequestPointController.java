package com.blueplanet.smartcookieteacher.ui.controllers;

import android.util.Log;
import android.view.View;

import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.RequeatFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.RewardPointLogFeatureController;
import com.blueplanet.smartcookieteacher.models.RequestPointModel;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.RequestForPointFragment;
import com.blueplanet.smartcookieteacher.ui.RewardPointFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 09-11-2016.
 */
public class RequestPointController implements IEventListener{

    private RequestForPointFragment _requestPointFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private RewardPointLog _rewardPointLog;
    private ArrayList<RequestPointModel> _rewardList;
    private String teacher_id, _schoolId;
    private Teacher _teacher;

    /**
     * constructur for reward list
     */


    public RequestPointController(RequestForPointFragment requestPointFragment, View view) {

        _requestPointFragment = requestPointFragment;
        _view = view;
        _teacher = LoginFeatureController.getInstance().getTeacher();
        _rewardList = RequeatFeatureController.getInstance().getRewardPointList();

        _teacher = LoginFeatureController.getInstance().getTeacher();
        if (_teacher != null) {
            teacher_id = _teacher.get_tId();
            _schoolId = _teacher.get_tSchool_id();
            _fetchRequestListFromServer(teacher_id, _schoolId);
/*
        if ((_isRewardPopulated(_rewardList))) {
            Log.i(_TAG, "Subject list got from DB");

            Log.i(_TAG, "Subject list size:" + _rewardList.size());
            _rePointFragment.showOrHideProgressBar(false);
            _rePointFragment.refreshListview();
        } else {
            Log.i(_TAG, "Subject list got from webservice");
            _teacher = LoginFeatureController.getInstance().getTeacher();
            if (_teacher != null) {
                _teacherId = _teacher.get_tId();
                _schoolId = _teacher.get_tSchool_id();
                _fetchRewardListFromServer(_teacherId, _schoolId);

            }
        }*/

    }}

    private boolean _isRewardPopulated(ArrayList<RewardPointLog> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;

    }

    private void _registerStudentEventListeners() {

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

        if (_requestPointFragment != null) {
            _requestPointFragment = null;
        }
    }

    /*private void SaveLoginData() {
        RewardPointLog log=RewardPointLogFeatureController.getInstance().get();

        LoginFeatureController.getInstance().saveUserDataIntoDB(teach);
    }*/


    /**
     * webservice call to fetch reward list from server
     *
     * @param teacherId
     * @param schoolId
     */
    private void _fetchRequestListFromServer(String teacherId, String schoolId) {
        _registerStudentEventListeners();

        RequeatFeatureController.getInstance().getRewardListFromServer(teacherId, schoolId);
        _requestPointFragment.showOrHideProgressBar(true);
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
            case EventTypes.EVENT_UI_REQUEST:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _requestPointFragment.showOrHideProgressBar(false);
                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */
                    _rewardList = RequeatFeatureController.getInstance().getRewardPointList();
                    _requestPointFragment.setVisibilityOfListView(true);
                    _requestPointFragment.refreshListview();

                    // _rewardPointLog=RewardPointLogFeatureController.getInstance().getRewardPointList();
                    // RewardPointLogFeatureController.getInstance().saveRewardPointLogIntoDB(_rewardList);

                }
                break;

            case EventTypes.EVENT_NOT_UI_REQUEST:

                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event1.unRegisterListener(this);

                _requestPointFragment.showOrHideProgressBar(false);
                _requestPointFragment.showNoRewardListMessage(false);

                break;

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);

                _requestPointFragment.showOrHideProgressBar(false);
                break;
            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                _requestPointFragment.showOrHideProgressBar(false);
                _requestPointFragment.showNetworkToast(false);
                break;
            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;

    }



}
