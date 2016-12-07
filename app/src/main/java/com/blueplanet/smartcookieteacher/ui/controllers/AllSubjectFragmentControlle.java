package com.blueplanet.smartcookieteacher.ui.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LogoutFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectAllFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectwiseStudentController;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TeacherAllSubject;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.AllSubjectFragment;
import com.blueplanet.smartcookieteacher.ui.TeacherSubjectFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 16-09-2016.
 */
public class AllSubjectFragmentControlle implements IEventListener, AdapterView.OnItemClickListener {

    private AllSubjectFragment _teacherallSubjectFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private TeacherSubject _teacherSubject;
    private String _teacherId, _schoolId;
    private Teacher _teacher;
    private ArrayList<TeacherAllSubject> _subjectList;
    private ListView _substudentList;
    private SubjectwiseStudentAdapter _adapter = null;


    public AllSubjectFragmentControlle(AllSubjectFragment teacherallSubjectFragment,
                                       View view) {
        _teacherallSubjectFragment = teacherallSubjectFragment;
        _view = view;


        if ((NetworkManager.isNetworkAvailable()) == false) {
            _teacherallSubjectFragment.showNetworkToast(false);
        }

        /**
         * check if list is present in db, if not call websertvice
         */



           _subjectList = SubjectAllFeatureController.getInstance().get_subjectList();
        //  _subjectList= SubjectFeatureController.getInstance().getSubjectInfoFromDB();

        if ((_isSubjectListPopulated(_subjectList))) {

            Log.i(_TAG, "Subject list got from DB");
            Log.i(_TAG, "Subject list size:" + _subjectList.size());
            _teacherallSubjectFragment.showOrHideProgressBar(false);
            _teacherallSubjectFragment.refreshListview();
        } else if (NetworkManager.isNetworkAvailable() == true) {
            Log.i(_TAG, "Subject list got from webservice");
            _teacher = LoginFeatureController.getInstance().getTeacher();
            if (_teacher != null) {
                _teacherId = _teacher.get_tId();
                _schoolId = _teacher.get_tSchool_id();
                int id = _teacher.getId();
                _fetchSubjectFromServer(_teacherId, _schoolId);
                //_fetchLogoutListFromServer(id);

            }
        }


    }
    private void _fetchLogoutListFromServer(int Id) {


        LogoutFeatureController.getInstance().FetchLogoutData(Id);
    }

    public void close() {
        _unRegisterEventListeners();
        if (_teacherallSubjectFragment != null) {
            _teacherallSubjectFragment = null;
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
        _teacherallSubjectFragment.showOrHideProgressBar(true);
        TeacherAllSubject _substudent = SubjectAllFeatureController.getInstance().getTeachersubject();
        SubjectAllFeatureController.getInstance().fetchTeacherAllSubjectFromServer(teacherId, schoolId);
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
            case EventTypes.EVENT_UI_ALL_SUBJECT:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {

                    _teacherallSubjectFragment.showOrHideProgressBar(false);
                    //_teacherSubjectFragment.setSubjectDataOnUI();
                    _subjectList = SubjectAllFeatureController.getInstance().get_subjectList();
                    _teacherallSubjectFragment.refreshListview();
                    //  SaveLoginData();
                }
                break;
            case EventTypes.EVENT_UI_NOTALL_SUBJECT:
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


    public void set_subjectList(ArrayList<TeacherAllSubject> subjectList) {
        _subjectList = subjectList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        _subjectList = SubjectAllFeatureController.getInstance().get_subjectList();

        if (_subjectList != null && _subjectList.size() > 0) {


            TeacherAllSubject selectedSubject = _subjectList.get(position);
            SubjectAllFeatureController.getInstance().set_selectedSubject(selectedSubject);

            // TeacherSubject _substudent = SubjectFeatureController.getInstance().getTeachersubject();
            //  _loadFragment(R.id.content_frame, new SubjectwiseStudentFragment());

        }

    }

    private boolean _isSubjectListPopulated(ArrayList<TeacherAllSubject> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;

    }

    private void _loadFragment(int id, Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = _teacherallSubjectFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack(fragment.getTag());
        ft.commitAllowingStateLoss();
    }


}
