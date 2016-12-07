package com.blueplanet.smartcookieteacher.ui.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.CoordinatorFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SharePointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.subFeaturecontroller;
import com.blueplanet.smartcookieteacher.models.CoordinatorModel;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragment;
import com.blueplanet.smartcookieteacher.ui.CoordinatorFragment;
import com.blueplanet.smartcookieteacher.ui.StudentListFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 08-08-2016.
 */
public class CoordinatorFragmentController implements IEventListener, AdapterView.OnItemClickListener {
    private CoordinatorFragment _Fragment;
    private View _View;
    private final String _TAG = this.getClass().getSimpleName();
    private Teacher _teacher;
    private ArrayList<CoordinatorModel> _studList;
    private String _teacherId, _schoolId;
    private int _lastInputId = 0;

    int currentFirstVisibleItem = 0;
    int currentVisibleItemCount = 0;
    int totalItemCount = 0;
    int currentScrollState = 0;
    boolean loadingMore = false;
    /**
     * constructur for student list
     */

    public CoordinatorFragmentController(CoordinatorFragment Fragment, View View) {
        _Fragment = Fragment;
        _View = View;
        _teacher = LoginFeatureController.getInstance().getTeacher();
        _studList = CoordinatorFeatureController.getInstance().getStudentList();


        if (_teacher != null) {
            _teacherId = _teacher.get_tId();
            _schoolId = _teacher.get_tSchool_id();
        }
        if (_studList != null && _studList.size() > 0) {
            _updateUI();
        }

        if (_teacher != null) {
            _teacherId = _teacher.get_tId();
            _schoolId = _teacher.get_tSchool_id();
            int id = StudentFeatureController.getInstance().getLastInputId();
            _fetchStudentCoordinatorFromServer(_teacherId, _schoolId,id);
        }

       /* if ((_isCoordinatorPopulated(_studList))) {
            Log.i(_TAG, "Subject list got from DB");

            Log.i(_TAG, "Subject list size:" + _studList.size());
            _Fragment.showOrHideProgressBar(false);
            _Fragment.refreshListview();
        } else {
            Log.i(_TAG, "Subject list got from webservice");
            _teacher = LoginFeatureController.getInstance().getTeacher();
            int id = StudentFeatureController.getInstance().getLastInputId();
            if (_teacher != null) {
                _teacherId = _teacher.get_tId();
                _schoolId = _teacher.get_tSchool_id();
                _fetchStudentCoordinatorFromServer(_teacherId, _schoolId,id);
              //  _Fragment.refreshListview();

            }
        }*/
    }

    private void _updateUI() {
        _Fragment.showOrHideProgressBar(false);
        _Fragment.setVisibilityOfListView(true);

    }

    private boolean _isCoordinatorPopulated(ArrayList<CoordinatorModel> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;

    }
    private void _registerStudentEventListeners() {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

    }

    private void _unRegisterEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier
                        (NotifierFactory.EVENT_NOTIFIER_STUDENT);
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
    private void _fetchStudentCoordinatorFromServer(String teacherId, String schoolId,int inputId) {
        _registerStudentEventListeners();

        CoordinatorFeatureController.getInstance().getStudentCoordiListFromServer(teacherId, schoolId, inputId);
        _Fragment.showOrHideProgressBar(true);
    }
    private boolean _isStudentListPopulated(ArrayList<CoordinatorModel> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
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
            case EventTypes.EVENT_UI_COORDINATOR:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _Fragment.showOrHideProgressBar(false);
                    /**
                     * get student list before refreshing listview avoid runtime exception
                     */
                    _studList = CoordinatorFeatureController.getInstance().getStudentList();


                    _Fragment.setVisibilityOfListView(true);
                    _Fragment.refreshListview();

                }
                break;

            case EventTypes.EVENT_UI_NOT_COORDINATOR:
                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_STUDENT);
                event1.unRegisterListener(this);

                _Fragment.showNoStudentListMessage(false);
                break;

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);
                break;
            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                _Fragment.showNetworkToast(false);
                break;
            default:
                eventState = EventState.EVENT_IGNORED;

                break;

        }

        return EventState.EVENT_PROCESSED;

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<CoordinatorModel> filteredList = CoordinatorFeatureController.getInstance().getFilteredList();


        if (filteredList != null && filteredList.size() > 0) {
            CoordinatorModel s = filteredList.get(position);
            CoordinatorFeatureController.getInstance().set_selectedCoordiStudent(s);
        } else if (_studList != null && _studList.size() > 0) {
            CoordinatorModel s = _studList.get(position);
            CoordinatorFeatureController.getInstance().set_selectedCoordiStudent(s);

        }
        _Fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _Fragment.hideSoftKeyboard();
            }
        });
        // subFeaturecontroller.getInstance()._clearSubjectList();
    }
    private void _loadFragment(int id, Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = _Fragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack("StudentListFragment");
        ft.commit();
    }


       /* @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            ListView lv = _Fragment.getListview();
            if (_studList != null && _studList.size() > 0) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    Log.i(_TAG, "SCROLL_STATE_IDLE");

                    int lastVisiblePosition = lv.getLastVisiblePosition();
                    int totalCount = _studList.get(lastVisiblePosition).getTotalCount();

                    if (_studList.size() < totalCount) {

                        Log.i(_TAG, "Student list size is: " + _studList.size());
                        Log.i(_TAG, "Student list Total count is: " + totalCount);

                        int id = (_studList.size() - 1);
                        _lastInputId = _studList.get(id).getInputId();
                        Log.i(_TAG, "Student list input id is: " + _lastInputId);
                        Log.i(_TAG, "TeacherID: " + _teacherId);
                        Log.i(_TAG, "SchoolId: " + _schoolId);
                        if (_lastInputId != -1 && !(TextUtils.isEmpty(_teacherId)) && !(TextUtils.isEmpty(_schoolId))) {
                            Log.i(_TAG, "Student list webservice called");
                            _registerStudentEventListeners();

                            CoordinatorFeatureController.getInstance().getStudentCoordiListFromServer(_teacherId, _schoolId, _lastInputId);
                            Log.i(_TAG, "Students webservice called");

                        }
                    }

                    //StudentFeatureController.getInstance().getStudentListFromServer(_teacherId, _schoolId, _lastInputId);
                }
            }
        }*/


/*

   @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        ListView lv = _Fragment.getListview();
        if (_studList != null && _studList.size() > 0) {
            if (scrollState == SCROLL_STATE_IDLE) {
                Log.i(_TAG, "SCROLL_STATE_IDLE");

                int lastVisiblePosition = lv.getLastVisiblePosition();
                int totalCount = _studList.get(lastVisiblePosition).getTotalCount();

                if (_studList.size() < totalCount) {

                    Log.i(_TAG, "Student list size is: " + _studList.size());
                    Log.i(_TAG, "Student list Total count is: " + totalCount);

                    int id = (_studList.size() - 1);
                    int lastInputId = _studList.get(id).getInputId();
                    Log.i(_TAG, "Student list input id is: " + lastInputId);
                    if (lastInputId != -1 && !(TextUtils.isEmpty(_teacherId)) && !(TextUtils.isEmpty(_schoolId))) {
                        CoordinatorFeatureController.getInstance().setLastInputId(lastInputId);
                        Log.i(_TAG, "Student list webservice called");
                        _registerStudentEventListeners();

                        CoordinatorFeatureController.getInstance().getStudentCoordiListFromServer(_teacherId, _schoolId, lastInputId);
                    }

                }

            }
        }
        }

*/



}
