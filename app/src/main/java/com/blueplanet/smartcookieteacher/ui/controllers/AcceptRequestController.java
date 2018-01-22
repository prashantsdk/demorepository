package com.blueplanet.smartcookieteacher.ui.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.AcceptRequestFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SharePointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectFeatureController;
import com.blueplanet.smartcookieteacher.models.RequestPointModel;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.AcceptRequestFragment;
import com.blueplanet.smartcookieteacher.ui.SharePointFragment;
import com.blueplanet.smartcookieteacher.ui.SubjectwiseStudentFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Sayali on 8/23/2017.
 */
public class AcceptRequestController implements IEventListener,AbsListView.OnScrollListener, AdapterView.OnItemClickListener  {


    private AcceptRequestFragment _Fragment;
    private View _View;
    private final String _TAG = this.getClass().getSimpleName();
    private Teacher _teacher;
    private ArrayList<RequestPointModel> _RequestPointlist;
    private String _teacherId, _schoolId;
    private int _lastInputId = 0;


    /**
     * constructur for student list
     */



    public AcceptRequestController(AcceptRequestFragment Fragment, View View) {

        _Fragment = Fragment;
        _View = View;
        _teacher = LoginFeatureController.getInstance().getTeacher();
        _RequestPointlist = AcceptRequestFeatureController.getInstance().get_PointLogList();

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
                _fetchRequestPointFromServer(_teacherId, _schoolId);
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

        if (_Fragment != null) {
            _Fragment = null;
        }
    }
    /**
     * webservice call to fetch reward list from server
     *
     * @param teacherId
     * @param schoolId
     */
    private void _fetchRequestPointFromServer(String teacherId, String schoolId) {
        _registerStudentEventListeners();

        AcceptRequestFeatureController.getInstance().getRequestPointListFromServer(teacherId, schoolId);
        _Fragment.showOrHideProgressBar(true);
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
            case EventTypes.EVENT_UI_ACCEPT_REQUEST:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _Fragment.showOrHideProgressBar(false);
                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */
                    _RequestPointlist = AcceptRequestFeatureController.getInstance().get_PointLogList();
                    _Fragment.setVisibilityOfListView(true);
                    _Fragment.refreshListview();

                    // _rewardPointLog=RewardPointLogFeatureController.getInstance().getRewardPointList();
                    // RewardPointLogFeatureController.getInstance().saveRewardPointLogIntoDB(_rewardList);

                }
                break;

            case EventTypes.EVENT_NOT_UI_ACCEPT_REQUEST:

                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event1.unRegisterListener(this);

                _Fragment.showOrHideProgressBar(false);
                //    _shairpointFragment.showNotEnoughPoint();

                break;

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);

                _Fragment.showOrHideProgressBar(false);
                break;
            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                _Fragment.showOrHideProgressBar(false);
                _Fragment.showNetworkToast(false);
                break;
            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;

    }



    private void _loadFragment(int id, Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = _Fragment.getActivity().getSupportFragmentManager();
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
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        _RequestPointlist = AcceptRequestFeatureController.getInstance().get_PointLogList();

        if (_RequestPointlist != null && _RequestPointlist.size() > 0) {


            RequestPointModel selectedRequest = _RequestPointlist.get(position);
            AcceptRequestFeatureController.getInstance().set_selectedRequest(selectedRequest);

            TeacherSubject _substudent = SubjectFeatureController.getInstance().getTeachersubject();
            _loadFragment(R.id.content_frame, new SubjectwiseStudentFragment());

        }

    }

    }
