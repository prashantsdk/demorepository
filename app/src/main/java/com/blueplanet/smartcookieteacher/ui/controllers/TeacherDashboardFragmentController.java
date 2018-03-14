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
import com.blueplanet.smartcookieteacher.featurecontroller.DashboardFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.ErrorFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.subFeaturecontroller;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.BluePointFragment;
import com.blueplanet.smartcookieteacher.ui.RewardPointFragment;
import com.blueplanet.smartcookieteacher.ui.StudentDetailFragment;
import com.blueplanet.smartcookieteacher.ui.TeacherDashboardFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 16-12-2015.
 */
public class TeacherDashboardFragmentController implements IEventListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener, View.OnClickListener {

    private TeacherDashboardFragment _teacherDashboardFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private Teacher _teacher;
    private ArrayList<Student> _studentList = null;
    private String _teacherId, _schoolId;
    private  Student stu;


    public TeacherDashboardFragmentController(TeacherDashboardFragment teacherFragment,
                                              View view) {
        _teacherDashboardFragment = teacherFragment;
        _view = view;
        _teacher = LoginFeatureController.getInstance().getTeacher();

        if (_teacher != null && NetworkManager.isNetworkAvailable()) {
            _teacherId = _teacher.get_tId();
            _schoolId = _teacher.get_tSchool_id();
            String id = _teacher.get_tId();
            _fetchPointFromServer(_teacherId,_schoolId);

        } else {
            _teacherDashboardFragment.setDashboardDataOnUI();
        }

    }

    public void close() {

        _unRegisterEventListeners();
        if (_teacherDashboardFragment != null) {
            _teacherDashboardFragment = null;

        }
    }

    /**
     * webservice call to fetch student list from server
     *
     * @param teacherId
     * @param schoolId
     */
    private void _fetchStudentListFromServer(String teacherId, String schoolId, int lastInputId) {
        _studentEventListeners();
        StudentFeatureController.getInstance().getStudentListFromServer(teacherId, schoolId, lastInputId);
    }

    private void _studentEventListeners() {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

    }

    /**
     * webservice call to fetch teacher points from server
     *
     * @param techerId
     */
    private void _fetchPointFromServer(String techerId,String studentId) {
        _registerNetworkListeners();
        _registerEventListeners();
        DashboardFeatureController.getInstance().fetchTeacherPointFromServer(techerId, studentId);
    }

    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

    }


    private void _registerNetworkListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(com.blueplanet.smartcookieteacher.notification.NotifierFactory.EVENT_NOTIFIER_NETWORK);
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

        EventNotifier eventNetwork1 =
                NotifierFactory.getInstance().getNotifier
                        (NotifierFactory.EVENT_NOTIFIER_STUDENT);
        eventNetwork1.unRegisterListener(this);
    }

    private boolean _isStudentListPopulated(ArrayList<Student> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }
    private void _ErrorWev(String t_id,String studentId,String type,String description,String date,String datetime,String usertype,String name,String phone,String email,
                           String appname,String subroutinename, String line,String status,String webmethodname,String webservice,String proname) {
        _registerEventListeners();
        _registerNetworkListeners();
      //  _loginFragment.showOrHideProgressBar(true);
        ErrorFeatureController.getInstance().getErrorListFromServer(t_id, studentId, type, description, date, datetime, usertype, name, phone, email,
                appname, subroutinename,  line, status, webmethodname, webservice, proname);
    }

    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        ServerResponse serverResponse = (com.blueplanet.smartcookieteacher.communication.ServerResponse) eventObject;
        int errorCode = -1;

        if (serverResponse != null) {
            errorCode = serverResponse.getErrorCode();
        }

        switch (eventType) {
            case EventTypes.EVENT_UI_TEACHER_POINT_RECEIVED:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier

                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In EVENT_UI_TEACHER_POINT_RECEIVED");
                    _teacherDashboardFragment.setDashboardDataOnUI();
                    Log.i("LoginFragmentController", "IN EVENT_UI_NO_LOGIN_RESPONSE");

                /*    _teacher = LoginFeatureController.getInstance().getTeacher();
                    String type="TeacherLogin";
                    String descr="Unable To login";
                    String date="2017.08.29";
                    String datetime="";
                    String usertype="teacher";
                    String name="Shubhangi";
                    String phone="8888406762";
                    String email="sayalir@roseland.com";
                    String appname="Smart Teacher";
                    String subtype="";
                    String line="45";
                    String status="not Found";
                    String method="";
                    String websename="login_teacher_V4.php";
                    String proname="Sayali Raghojiwar";
                    if (_teacher != null) {
                        _teacherId = String.valueOf(_teacher.getId());
                        _schoolId = _teacher.get_tSchool_id();

                        _ErrorWev(_teacherId,_schoolId,type,descr,date,datetime,usertype,name,phone,email,appname,subtype,line,status,method,websename,proname);




                        //  selprn = AssignPointFeatureController.getInstance().get_selectedPrn();
          *//* Student s = StudentFeatureController.getInstance().getSelectedStudent();
           prn=s.get_stdPRN();
           subFeaturecontroller.getInstance().fetchSubjectFromServer(_teacherId, _schoolId,prn);*//*

                    }*/

                   int id = StudentFeatureController.getInstance().getLastInputId();

                    _studentList = StudentFeatureController.getInstance().getStudentList();


                    if (!(_isStudentListPopulated(_studentList))) {
                        _fetchStudentListFromServer(_teacherId, _schoolId, id);

                    }

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

            case EventTypes.EVENT_UI_STUDENT_LIST_RECEIVED:
                EventNotifier eventNotifier1 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
                eventNotifier1.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    /**
                     * get student list before refreshing listview avoid runtime exception
                     */
                    _studentList = StudentFeatureController.getInstance().getStudentList();
                    _teacherDashboardFragment.refreshListview();
                    _teacherDashboardFragment._showDataOnUI();


                }
                break;

            case EventTypes.EVENT_UI_NO_STUDENT_LIST_RECEIVED:
                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_STUDENT);
                event1.unRegisterListener(this);
                _teacherDashboardFragment.showOrHideProgressBar(false);
                _teacherDashboardFragment.showNoStudentListMessage(false);



                break;
            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;


    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        ListView lv = _teacherDashboardFragment.getListview();
        if (_studentList != null && _studentList.size() > 0) {
            if (scrollState == SCROLL_STATE_IDLE) {
                Log.i(_TAG, "SCROLL_STATE_IDLE");

                int lastVisiblePosition = lv.getLastVisiblePosition();
                int totalCount = _studentList.get(lastVisiblePosition).getTotalCount();

                if (_studentList.size() < totalCount) {

                    Log.i(_TAG, "Student list size is: " + _studentList.size());
                    Log.i(_TAG, "Student list Total count is: " + totalCount);

                    int id = (_studentList.size() - 1);
                    int lastInputId = _studentList.get(id).getInputId();
                    Log.i(_TAG, "Student list input id is: " + lastInputId);
                    if (lastInputId != -1 && !(TextUtils.isEmpty(_teacherId)) && !(TextUtils.isEmpty(_schoolId))) {
                        StudentFeatureController.getInstance().setLastInputId(lastInputId);
                        Log.i(_TAG, "Student list webservice called");
                        _studentEventListeners();

                        StudentFeatureController.getInstance().getStudentListFromServer(_teacherId, _schoolId, lastInputId);
                    }

                }

            }
        }


    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private void _loadFragment(int id, Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
         //DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(true);
        FragmentManager fm = _teacherDashboardFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack("TeacherDashboardFragment");
        _teacherDashboardFragment.getActivity().setTitle("Student Profile");
        ft.commit();

    }

    private void _loadFragmentGreenPoint(int id, Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = _teacherDashboardFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack("TeacherDashboardFragment");
        ft.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //subFeaturecontroller.getInstance()._clearSubjectList();
        _studentList = StudentFeatureController.getInstance().getStudentList();
        if (_studentList != null && _studentList.size() > 0) {
            Log.i(_TAG, "In onItemClick");
            Student s = _studentList.get(position);
            StudentFeatureController.getInstance().setSelectedStudent(s);
            subFeaturecontroller.getInstance()._clearSubjectList();
            _loadFragment(R.id.content_frame, new StudentDetailFragment());


        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.greenpoint:
               // _loadFragmentGreenPoint(R.id.content_frame, new RewardPointFragment());
            case R.id.bluepoint:
             //   _loadFragmentGreenPoint(R.id.content_frame, new BluePointFragment());
            default:
                break;
        }
    }


}