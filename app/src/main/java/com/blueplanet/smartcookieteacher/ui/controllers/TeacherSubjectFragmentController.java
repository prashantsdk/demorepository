package com.blueplanet.smartcookieteacher.ui.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectwiseStudentController;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.SubjectwiseStudentFragment;
import com.blueplanet.smartcookieteacher.ui.TeacherSubjectFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;


import java.util.ArrayList;

/**
 * Created by 1311 on 21-12-2015.
 */
public class TeacherSubjectFragmentController implements IEventListener, AdapterView.OnItemClickListener {

    private TeacherSubjectFragment _teacherSubjectFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private TeacherSubject _teacherSubject;
    private String _teacherId, _schoolId;
    private Teacher _teacher;
    private ArrayList<TeacherSubject> _subjectList;
    private ListView _substudentList;
    private SubjectwiseStudentAdapter _adapter = null;


    public TeacherSubjectFragmentController(TeacherSubjectFragment teacherSubjectFragment,
                                            View view) {
        _teacherSubjectFragment = teacherSubjectFragment;
        _view = view;


        if ((NetworkManager.isNetworkAvailable()) == false) {
            _teacherSubjectFragment.showNetworkToast(false);
        }

        /**
         * check if list is present in db, if not call websertvice
         */
       // _subjectList = SubjectFeatureController.getInstance().getSubjectInfoFromDB();
        _subjectList = SubjectFeatureController.getInstance().get_subjectList();
        //  _subjectList= SubjectFeatureController.getInstance().getSubjectInfoFromDB();

        if ((_isSubjectListPopulated(_subjectList))) {

            Log.i(_TAG, "Subject list got from DB");
            Log.i(_TAG, "Subject list size:" + _subjectList.size());
            _teacherSubjectFragment.showOrHideProgressBar(false);
            _teacherSubjectFragment.refreshListview();
        } else if (NetworkManager.isNetworkAvailable() == true) {
            Log.i(_TAG, "Subject list got from webservice");
            _teacher = LoginFeatureController.getInstance().getTeacher();
            if (_teacher != null) {
                _teacherId = _teacher.get_tId();
                _schoolId = _teacher.get_tSchool_id();
                _fetchSubjectFromServer(_teacherId, _schoolId);


            }
        }

    }



    public void close() {
        _unRegisterEventListeners();
        if (_teacherSubjectFragment != null) {
            _teacherSubjectFragment = null;
        }
    }


    /**
     * webservice call to fetch teacher subject from server
     *
     * @param teacherId
     * @param schoolId
     */
    private void _fetchSubjectFromServer(String teacherId, String schoolId) {
        _registerNetworkListeners();
        _registerEventListeners();
        _teacherSubjectFragment.showOrHideProgressBar(true);
        TeacherSubject _substudent = SubjectFeatureController.getInstance().getTeachersubject();
        SubjectFeatureController.getInstance().fetchTeacherSubjectFromServer(teacherId, schoolId);
    }

    /**
     * webservice call to fetch subjectwise student list from server
     *
     * @param
     * @param
     */
    private void _fetchSubjectListFromServer(String t_id, String studentId, String divisionId, String semesterId, String branchesId,
                                             String departmentId,
                                             String courseLevel, String subjectCode) {
        _registerNetworkListeners();
        _registerEventListeners();
        SubjectwiseStudentController.getInstance().getSubjectwisestudentListFromServer(t_id, studentId, divisionId, semesterId, branchesId,
                departmentId, courseLevel, subjectCode);
    }


    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

    }

    private void _registerNetworkListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_NETWORK);
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

    @Override
    public int eventNotify(int eventType, Object eventObject) {

        int eventState = EventState.EVENT_PROCESSED;
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = -1;

        if (serverResponse != null) {
            errorCode = serverResponse.getErrorCode();
        }

        switch (eventType) {
            case EventTypes.EVENT_UI_TEACHER_SUBJECT_RECEIVED:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {

                    _teacherSubjectFragment.showOrHideProgressBar(false);
                    //_teacherSubjectFragment.setSubjectDataOnUI();
                    _subjectList = SubjectFeatureController.getInstance().get_subjectList();
                    _teacherSubjectFragment.refreshListview();
                    //  SaveLoginData();
                }
                break;
            case EventTypes.EVENT_UI_TEACHER_SUBJECT_NOT_RECEIVED:
                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event1.unRegisterListener(this);

                //_StudentListFragment.showNoStudentListMessage(false);
                break;
            // say
            case EventTypes.EVENT_UI_SUBJECTWISE_STUDENTLIST_RECEIVED:
                EventNotifier eventNotifier1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier1.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {


                }
                break;
            case EventTypes.EVENT_UI_NO_SUBJECTWISE_STUDENTLIST_RECEIVED:
                EventNotifier event2 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event2.unRegisterListener(this);

                //_StudentListFragment.showNoStudentListMessage(false);
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

                // _teacherSubjectFragment.showNetworkToast(false);
                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }
        return EventState.EVENT_PROCESSED;
    }


    public void set_subjectList(ArrayList<TeacherSubject> subjectList) {
        _subjectList = subjectList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        _subjectList = SubjectFeatureController.getInstance().get_subjectList();

        if (_subjectList != null && _subjectList.size() > 0) {


            TeacherSubject selectedSubject = _subjectList.get(position);
            SubjectFeatureController.getInstance().set_selectedSubject(selectedSubject);

             TeacherSubject _substudent = SubjectFeatureController.getInstance().getTeachersubject();
              _loadFragment(R.id.content_frame, new SubjectwiseStudentFragment());

        }

    }

    private boolean _isSubjectListPopulated(ArrayList<TeacherSubject> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;

    }

    private void _loadFragment(int id, Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = _teacherSubjectFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack(fragment.getTag());
        ft.commitAllowingStateLoss();
    }
}
