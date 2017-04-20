package com.blueplanet.smartcookieteacher.ui.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SharePointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SoftRewardFeatureController;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.models.SoftReward;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.SharePointFragment;
import com.blueplanet.smartcookieteacher.ui.SoftRewardFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 22-01-2017.
 */
public class SofrRewardController implements IEventListener,AbsListView.OnScrollListener, AdapterView.OnItemClickListener  {

    private SoftRewardFragment _fragment;
    private View _View;
    private final String _TAG = this.getClass().getSimpleName();
    private Teacher _teacher;
    private ArrayList<SoftReward> _softRewardList;
    private String _teacherId, _schoolId,_user;
    private int _lastInputId = 0;


    /**
     * constructur for student list
     */



    public SofrRewardController(SoftRewardFragment fragment, View View) {

        _fragment = fragment;
        _View = View;
        _teacher = LoginFeatureController.getInstance().getTeacher();
        _softRewardList = SoftRewardFeatureController.getInstance().get_soft();

       /* if ((_isRewardPopulated(_sharePointlist))) {
            Log.i(_TAG, "Subject list got from DB");

            Log.i(_TAG, "Subject list size:" + _sharePointlist.size());
            _shairpointFragment.showOrHideProgressBar(false);
            _shairpointFragment.refreshListview();
        } else {
            Log.i(_TAG, "Subject list got from webservice");
            _teacher = LoginFeatureController.getInstance().getTeacher();
            if (_teacher != null) {
                _teacherId = _teacher.get_tId();
                _schoolId = _teacher.get_tSchool_id();
                _fetchteachershairPointFromServer(_teacherId, _schoolId);

            }
        }
*/

        if (_teacher != null) {

            _teacher = LoginFeatureController.getInstance().getTeacher();
            if (_teacher != null) {
                _teacherId = _teacher.get_tId();
                _schoolId = _teacher.get_tSchool_id();
                _user= "Teacher";
                _fetchsoftRewatdFromServer(_user);
            }

        }
    }

    private boolean _isRewardPopulated(ArrayList<ShairPointModel> list) {
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

        if (_fragment != null) {
            _fragment = null;
        }
    }
    /**
     * webservice call to fetch reward list from server
     *
     * @param
     */
    private void _fetchsoftRewatdFromServer(String user) {
        _registerStudentEventListeners();

        SoftRewardFeatureController.getInstance().getteacherSoftRewardListFromServer(user);
        _fragment.showOrHideProgressBar(true);
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
            case EventTypes.SOFT_REWARD_UI:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _fragment.showOrHideProgressBar(false);
                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */
                    _softRewardList = SoftRewardFeatureController.getInstance().get_soft();
                    _fragment.setVisibilityOfListView(true);
                    _fragment.refreshListview();

                    // _rewardPointLog=RewardPointLogFeatureController.getInstance().getRewardPointList();
                    // RewardPointLogFeatureController.getInstance().saveRewardPointLogIntoDB(_rewardList);

                }
                break;

            case EventTypes.SOFT_REWARD_NOT_UI:

                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event1.unRegisterListener(this);

                _fragment.showOrHideProgressBar(false);
                //    _shairpointFragment.showNotEnoughPoint();

                break;

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);

                _fragment.showOrHideProgressBar(false);
                break;
            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                _fragment.showOrHideProgressBar(false);
                _fragment.showNetworkToast(false);
                break;
            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<SoftReward> filteredList = SoftRewardFeatureController.getInstance().getFilteredList();


        if (filteredList != null && filteredList.size() > 0) {
            SoftReward s = filteredList.get(position);
            SoftRewardFeatureController.getInstance().set_selectedteacher(s);
        } else if (_softRewardList != null && _softRewardList.size() > 0) {
            SoftReward s = _softRewardList.get(position);
            SoftRewardFeatureController.getInstance().set_selectedteacher(s);

        }
        _fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _fragment.hideSoftKeyboard();
            }
        });
        // subFeaturecontroller.getInstance()._clearSubjectList();

    }
    private void _loadFragment(int id, Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = _fragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack("StudentListFragment");
        ft.commit();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
