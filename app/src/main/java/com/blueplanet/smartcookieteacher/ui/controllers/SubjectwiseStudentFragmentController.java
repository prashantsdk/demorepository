package com.blueplanet.smartcookieteacher.ui.controllers;

import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;


import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.RewardPointLogFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectwiseStudentController;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.models.Subjectwise_student;
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
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 05-02-2016.
 */
public class SubjectwiseStudentFragmentController implements IEventListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    private SubjectwiseStudentFragment _subjectFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private Subjectwise_student _subStudent;
    private ArrayList<Subjectwise_student> _subStudentList = null;
    private String _teacherId, _schoolId;
    private Teacher _teacher;
    private ArrayList<TeacherSubject> _subjectList;
    private TeacherSubject _teacherSub;


    public SubjectwiseStudentFragmentController(SubjectwiseStudentFragment subjectFragment, View view) {

        _subjectFragment = subjectFragment;
        _view = view;
        _subStudentList=SubjectwiseStudentController.getInstance().get_subList();
        if ((NetworkManager.isNetworkAvailable()) == false) {
            _subjectFragment.showNetworkToast(false);
        }
        _teacher = LoginFeatureController.getInstance().getTeacher();
        _teacherSub=SubjectFeatureController.getInstance().get_selectedSubject();

        String divID = _teacherSub.get_tDivisionID();
        String semID = _teacherSub.get_tSemesterID();
        String branchID = _teacherSub.get_tBranchID();
        String departID = _teacherSub.get_tDepartmentID();
        String courseID = _teacherSub.get_tCoursLevel();

        String subCode = String.valueOf(_teacherSub.get_tSubjectCode());
        if (_teacher != null) {
            _teacherId = _teacher.get_tId();
            _schoolId = _teacher.get_tSchool_id();
        }
        _fetchSubjectListFromServer(_teacherId, _schoolId, divID, semID, branchID, departID, courseID, subCode);

    }

    public void clear() {
        _unRegisterEventListeners();
        if (_subjectFragment != null) {
            _subjectFragment = null;
        }
    }


    /**
     * webservice call to fetch student list from server
     *
     * @param
     * @param
     */
    private void _fetchSubjectListFromServer(String t_id, String studentId, String divisionId, String semesterId, String branchesId,
                                             String departmentId,
                                             String courseLevel, String subjectCode) {
        _subStudenttEventListeners();
        SubjectwiseStudentController.getInstance().getSubjectwisestudentListFromServer(t_id, studentId, divisionId, semesterId, branchesId,
                departmentId, courseLevel, subjectCode);
    }

    private void _subStudenttEventListeners() {

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
            case EventTypes.EVENT_UI_SUBJECTWISE_STUDENTLIST_RECEIVED:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In EVENT_UI_SUBJECTWISE_STUDENTLIST_RECEIVED");
                    _subjectFragment.showOrHideProgressBar(false);

                    _subStudentList=SubjectwiseStudentController.getInstance().get_subList();
                    _subjectFragment.setVisibilityOfListView(true);
                    _subjectFragment.refreshListview();

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


                break;

            case EventTypes.EVENT_UI_NO_SUBJECTWISE_STUDENTLIST_RECEIVED:
                EventNotifier eventNotifier1 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier1.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    /**
                     * get student list before refreshing listview avoid runtime exception
                     */
                    // _studentList = StudentFeatureController.getInstance().getStudentList();
                    // SubjectwiseStudentFragment.refreshListview();


                }
                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}


