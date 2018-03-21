package com.blueplanet.smartcookieteacher.ui.controllers;

import android.util.Log;
import android.view.View;


import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.RewardPointLogFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectFeatureController;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.RewardPointFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 07-01-2016.
 */
public class RewardPointFragmentController implements IEventListener {

    private RewardPointFragment _rePointFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private RewardPointLog _rewardPointLog;
    private ArrayList<RewardPointLog> _rewardList;
    private String _teacherId, _schoolId;
    private Teacher _teacher;

    /**
     * constructur for reward list
     */


    public RewardPointFragmentController(RewardPointFragment rewardPointFragment, View view) {

        _rePointFragment = rewardPointFragment;
        _view = view;
        _teacher = LoginFeatureController.getInstance().getTeacher();
       // _rewardList = RewardPointLogFeatureController.getInstance().getRewardFromDB();

        _rewardList = RewardPointLogFeatureController.getInstance().get_RewardLogData();


        int count = _rewardList.size();
        int temp =count;

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
        }

    }

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

        if (_rePointFragment != null) {
            _rePointFragment = null;
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
    private void _fetchRewardListFromServer(String teacherId, String schoolId) {
        _registerStudentEventListeners();

        RewardPointLogFeatureController.getInstance().getRewardListFromServer(teacherId, schoolId);
        _rePointFragment.showOrHideProgressBar(true);
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
            case EventTypes.EVENT_UI_TEACHER_REWARD_POINT_RECEVIED:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);

                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _rePointFragment.showOrHideProgressBar(false);

                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */
                    _rewardList = RewardPointLogFeatureController.getInstance().getRewardPointList();
                    _rePointFragment.setVisibilityOfListView(true);
                    _rePointFragment.refreshListview();

                   // _rewardPointLog=RewardPointLogFeatureController.getInstance().getRewardPointList();
                   // RewardPointLogFeatureController.getInstance().saveRewardPointLogIntoDB(_rewardList);

                }
                break;

            case EventTypes.EVENT_UI_NO_TEACHER_REWARD_POINT_RECEVIED:

                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event1.unRegisterListener(this);

                _rePointFragment.showOrHideProgressBar(false);
                _rePointFragment.showNoRewardListMessage(false);

                break;

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);

                _rePointFragment.showOrHideProgressBar(false);
                break;
            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                _rePointFragment.showOrHideProgressBar(false);
                _rePointFragment.showNetworkToast(false);
                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;

    }
}
