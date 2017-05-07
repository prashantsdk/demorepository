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
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.subFeaturecontroller;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragment;
import com.blueplanet.smartcookieteacher.ui.StudentListFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;


import java.util.ArrayList;

/**
 * Created by 1311 on 25-11-2015.
 */
public class StudentListFragmentController implements IEventListener,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private StudentListFragment _StudentListFragment;
    private View _View;
    private final String _TAG = this.getClass().getSimpleName();
    private Teacher _teacher;
    private ArrayList<Student> _studentList;
    private String _teacherId, _schoolId;
    private int _lastInputId = 0;


    /**
     * constructur for student list
     */

    public StudentListFragmentController(StudentListFragment StudentListFragment, View View) {
        _StudentListFragment = StudentListFragment;
        _View = View;
        _teacher = LoginFeatureController.getInstance().getTeacher();
        _studentList = StudentFeatureController.getInstance().getStudentList();

        if (_teacher != null) {
            _teacherId = _teacher.get_tId();
            _schoolId = _teacher.get_tSchool_id();
        }
        if (_studentList != null && _studentList.size() > 0) {
            _updateUI();
        }

        if (_teacher != null) {
            _teacherId = _teacher.get_tId();
            _schoolId = _teacher.get_tSchool_id();
            _fetchSubjectFromServer(_teacherId, _schoolId);
        }
    }

    /**
     * webservice call to fetch teacher subject from server
     *
     * @param teacherId
     * @param schoolId
     */
    private void _fetchSubjectFromServer(String teacherId, String schoolId) {
        _registerEventListeners();
        SubjectFeatureController.getInstance().fetchTeacherSubjectFromServer(teacherId, schoolId);
    }

    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

    }

    /**
     * function to show student list if already present
     */
    private void _updateUI() {
        _StudentListFragment.showOrHideProgressBar(false);
        _StudentListFragment.setVisibilityOfListView(true);

    }

    public void clear() {
        _unRegisterEventListeners();

        if (_StudentListFragment != null) {
            _StudentListFragment = null;
        }
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


    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = -1;

        if (serverResponse != null) {
            errorCode = serverResponse.getErrorCode();
        }

        switch (eventType) {
            case EventTypes.EVENT_UI_STUDENT_LIST_RECEIVED:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _StudentListFragment.showOrHideProgressBar(false);
                    /**
                     * get student list before refreshing listview avoid runtime exception
                     */
                    _studentList = StudentFeatureController.getInstance().getStudentList();
                    _StudentListFragment.setVisibilityOfListView(true);
                    _StudentListFragment.refreshListview();

                }
                break;

            case EventTypes.EVENT_UI_NO_STUDENT_LIST_RECEIVED:
                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_STUDENT);
                event1.unRegisterListener(this);

                _StudentListFragment.showNoStudentListMessage(false);
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

                _StudentListFragment.showNetworkToast(false);
                break;
            default:
                eventState = EventState.EVENT_IGNORED;

                break;

        }

        return EventState.EVENT_PROCESSED;

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        ListView lv = _StudentListFragment.getListview();
        if (_studentList != null && _studentList.size() > 0) {
            if (scrollState == SCROLL_STATE_IDLE) {
                Log.i(_TAG, "SCROLL_STATE_IDLE");

                int lastVisiblePosition = lv.getLastVisiblePosition();
                int totalCount = _studentList.get(lastVisiblePosition).getTotalCount();

                if (_studentList.size() < totalCount) {

                    Log.i(_TAG, "Student list size is: " + _studentList.size());
                    Log.i(_TAG, "Student list Total count is: " + totalCount);

                    int id = (_studentList.size() - 1);
                    _lastInputId = _studentList.get(id).getInputId();
                    Log.i(_TAG, "Student list input id is: " + _lastInputId);
                    Log.i(_TAG, "TeacherID: " + _teacherId);
                    Log.i(_TAG, "SchoolId: " + _schoolId);
                    if (_lastInputId != -1 && !(TextUtils.isEmpty(_teacherId)) && !(TextUtils.isEmpty(_schoolId))) {
                        Log.i(_TAG, "Student list webservice called");
                        _registerStudentEventListeners();

                        StudentFeatureController.getInstance().getStudentListFromServer(_teacherId, _schoolId, _lastInputId);
                        Log.i(_TAG, "Students webservice called");

                            }
                        }

                        //StudentFeatureController.getInstance().getStudentListFromServer(_teacherId, _schoolId, _lastInputId);
                    }
                }
            }


    private boolean _isStudentListPopulated(ArrayList<Student> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        ArrayList<Student> filteredList = StudentFeatureController.getInstance().getFilteredList();

        if (filteredList != null && filteredList.size() > 0) {
            Student s = filteredList.get(position);
            StudentFeatureController.getInstance().setSelectedStudent(s);
        } else if (_studentList != null && _studentList.size() > 0) {
            Student s = _studentList.get(position);
            StudentFeatureController.getInstance().setSelectedStudent(s);

        }
        _StudentListFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _StudentListFragment.hideSoftKeyboard();
            }
        });
        subFeaturecontroller.getInstance()._clearSubjectList();

        _loadFragment(R.id.content_frame, new AssignPointFragment());
    }

    private void _loadFragment(int id, Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = _StudentListFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack("StudentListFragment");
        ft.commit();
    }
}
