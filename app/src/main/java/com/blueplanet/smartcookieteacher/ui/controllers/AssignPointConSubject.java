package com.blueplanet.smartcookieteacher.ui.controllers;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.ActivityListFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.AssignPointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DashboardFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.subFeaturecontroller;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.models.SubNameCode;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TeacherActivity;
import com.blueplanet.smartcookieteacher.models.TeacherDashbordPoint;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragmentsubject;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sayali on 3/12/2017.
 */
public class AssignPointConSubject implements View.OnClickListener, IEventListener, AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    private AssignPointFragmentsubject _assignPointFragment;
    private View _view;
    private Teacher _teacher;
    private ArrayList<TeacherActivity> _activityList;
    private ArrayList<TeacherSubject> _subList;
    private ArrayList<Student> _stusubList;
    private String _teacherId, _schoolId;
    private ArrayList<Student> _studentList = null;

    //private AssignPointListAdapter1 _adapter = null;
    private AssignPointSubjectwiseAdapter _adapter = null;
    //private AssignPointListAdapter1 _adapter = null;
    private GridView _grid;
    private ListView listView;
    private AssignPointSubjectAdapter _subAdapter;
    private SeekBar seekpointsbar;
    private CustomTextView txtseekPoint;
    private int Seekvalue;
    private String _points = null;
    private final String _TAG = this.getClass().getSimpleName();
    private String selectedActivityId;
    private String selectedSubjectId;
    private String _activityType = null;
    private ArrayList<String> a = new ArrayList<String>();
    private ArrayList<Student> subName;
    int _txtsubName;
    private ArrayList<String> stusubList;
    String selprn;
    private ArrayList<SubNameCode> _subNameCodeList;
    String prn;
    private TextView txt_point;
    private EditText txt_mark, _comment;
    private EditText _txt_gradePoint, txt_point2;

    private ArrayList<TeacherSubject> _subjectList;
    private Spinner spinner, spinner1;
    String countrycode = "", logintype = "";


    public AssignPointConSubject(AssignPointFragmentsubject assignPointFragment, View view) {

        _assignPointFragment = assignPointFragment;
        _view = view;
        _studentList = StudentFeatureController.getInstance().getStudentList();

        _teacher = LoginFeatureController.getInstance().getTeacher();
        txtseekPoint = (CustomTextView) _view.findViewById(R.id.txtassignedPoints);
        txt_point = (TextView) _view.findViewById(R.id.txt_point);
        txt_mark = (EditText) _view.findViewById(R.id.txt_point1);
        _txt_gradePoint = (EditText) _view.findViewById(R.id.txt_gradePoint);
        _comment = (EditText) _view.findViewById(R.id.txt_comment);
        txt_point2 = (EditText) _view.findViewById(R.id.txt_point2);
        spinner = (Spinner) _view.findViewById(R.id.spinner);
        spinner1 = (Spinner) _view.findViewById(R.id.spinner2);

        _activityList = ActivityListFeatureController.getInstance().getActivitylistInfoFromDB(_activityType);
        //  _grid = (GridView) _view.findViewById(R.id.grid);
        listView = (ListView) _view.findViewById(R.id.grid);

        if (_teacher != null) {
            _teacherId = _teacher.get_tId();
            _schoolId = _teacher.get_tSchool_id();
            String id = _teacher.get_tId();
            _fetchSubjectListFromServer(_teacherId, _schoolId);

            _teacherId = _teacher.get_tId();
            _schoolId = _teacher.get_tSchool_id();


            //  selprn = AssignPointFeatureController.getInstance().get_selectedPrn();
          /* Student s = StudentFeatureController.getInstance().getSelectedStudent();
           prn=s.get_stdPRN();
           subFeaturecontroller.getInstance().fetchSubjectFromServer(_teacherId, _schoolId,prn);*/

        }


    }

    private boolean _isActivityListPopulated(ArrayList<TeacherActivity> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;

    }

    private String getDate() {
        SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd");
        String date = date1.format(new Date());
        return date;
    }

    @Override
    public void onClick(final View view) {

        int id = view.getId();
      /*  _lvActivities = (ListView) _view.findViewById(R.id.lstActivity);
        final ImageView imgCircle = (ImageView) _view.findViewById(R.id.imgSelectedOption);
        final RelativeLayout _rl4Option = (RelativeLayout) _view.findViewById(R.id.rel4Option);*/
        switch (id) {
            /*case R.id.txtGeneralAssignPoints:
                _activityType = ApplicationConstants.KEY_GENERAL_ACTIVITY;
                AssignPointFeatureController.getInstance().setIsStudyClicked(false);
                _activityList = ActivityListFeatureController.getInstance().getActivitylistInfoFromDB(_activityType);
                _assignPointFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        imgCircle.setImageResource(R.drawable.circle_general);
                        // _assignPointFragment.showOrHideRl4Option(false);
                        imgCircle.setVisibility(View.VISIBLE);
                        _rl4Option.setVisibility(View.GONE);


                        // _activityList = ActivityListFeatureController.
                        // getInstance().getGeneralActivityList();
                        _adapter = new AssignPointListAdapter(_assignPointFragment,
                                AssignPointFragmentController.this, _activityList);

                        _lvActivities.setAdapter(_adapter);
                        _lvActivities.setVisibility(View.VISIBLE);
                        //_lvActivities.setOnItemClickListener(AssignPointFragmentController.this);
                    }
                });


                break;

            case R.id.txtSportsAssingnedPoints:
                _activityType = ApplicationConstants.KEY_SPORTS;
                AssignPointFeatureController.getInstance().setIsStudyClicked(false);
                _activityList = ActivityListFeatureController.getInstance().getActivitylistInfoFromDB(_activityType);
                _assignPointFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imgCircle.setImageResource(R.drawable.circle_sport);
                        // _assignPointFragment.showOrHideRl4Option(false);
                        imgCircle.setVisibility(View.VISIBLE);
                        _rl4Option.setVisibility(View.GONE);

                        // _activityList = ActivityListFeatureController.
                        //    getInstance().get_sportsActivityList();
                        _adapter = new AssignPointListAdapter(_assignPointFragment,
                                AssignPointFragmentController.this, _activityList);

                        _lvActivities.setAdapter(_adapter);
                        _lvActivities.setVisibility(View.VISIBLE);
                        //_lvActivities.setOnItemClickListener(AssignPointFragmentController.this);


                    }
                });

                break;
            case R.id.txtArtAssignpoints:
                _activityType = ApplicationConstants.KEY_ART;
                _activityList = ActivityListFeatureController.getInstance().getActivitylistInfoFromDB(_activityType);
                AssignPointFeatureController.getInstance().setIsStudyClicked(false);
                _assignPointFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imgCircle.setImageResource(R.drawable.circle_art);
                        // _assignPointFragment.showOrHideRl4Option(false);
                        imgCircle.setVisibility(View.VISIBLE);
                        _rl4Option.setVisibility(View.GONE);


                        //  _activityList = ActivityListFeatureController.
                        //   getInstance().get_artActivityList();
                        _adapter = new AssignPointListAdapter(_assignPointFragment,
                                AssignPointFragmentController.this, _activityList);

                        _lvActivities.setAdapter(_adapter);
                        _lvActivities.setVisibility(View.VISIBLE);
                        // _lvActivities.setOnItemClickListener(AssignPointFragmentController.this);

                    }
                });

                break;
            case R.id.txtStudyAssignPoints:
                AssignPointFeatureController.getInstance().setIsStudyClicked(true);
                *//*Student s = StudentFeatureController.getInstance().getSelectedStudent();
                prn=s.get_stdPRN();
                subFeaturecontroller.getInstance().fetchSubjectFromServer(_teacherId, _schoolId, prn);*//*


               *//* if (_teacher != null) {
                    _teacherId = _teacher.get_tId();
                    _schoolId = _teacher.get_tSchool_id();


                    //  selprn = AssignPointFeatureController.getInstance().get_selectedPrn();
                    Student s=AssignPointFeatureController.getInstance().get_selectedStudent();
                    String prn=s.get_stdPRN();
                    subFeaturecontroller.getInstance().fetchSubjectFromServer(_teacherId, _schoolId,prn);}*//*

                _assignPointFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imgCircle.setImageResource(R.drawable.circle_study);


                        // _assignPointFragment.showOrHideRl4Option(false);
                        imgCircle.setVisibility(View.VISIBLE);
                        _rl4Option.setVisibility(View.GONE);
                       *//*_subList = SubjectFeatureController.getInstance().get_subjectList();
                        _subAdapter = new AssignPointSubjectAdapter(_assignPointFragment,
                                AssignPointFragmentController.this, _subList);*//*

                       *//* stusubList = AssignPointFeatureController.getInstance().get_selectedSubjsct();
                        _subAdapter = new AssignPointSubjectAdapter(_assignPointFragment, AssignPointFragmentController.this, stusubList);*//*

                       *//* if (_teacher != null) {
                            _teacherId = _teacher.get_tId();
                            _schoolId = _teacher.get_tSchool_id();


                          //  selprn = AssignPointFeatureController.getInstance().get_selectedPrn();
                            Student s=AssignPointFeatureController.getInstance().get_selectedStudent();
                            String prn=s.get_stdPRN();
                            subFeaturecontroller.getInstance().fetchSubjectFromServer(_teacherId, _schoolId,prn);

                        }*//*

                        _subNameCodeList = subFeaturecontroller.getInstance().get_subjList();
                        _subAdapter = new AssignPointSubjectAdapter(_assignPointFragment,
                                AssignPointFragmentController.this, _subNameCodeList);

                        _lvActivities.setAdapter(_subAdapter);
                        _lvActivities.setVisibility(View.VISIBLE);


                        // _lvActivities.setOnItemClickListener(AssignPointFragmentController.this);

                    }
                });

                break;
            case R.id.txtoptionselected:
                _assignPointFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        imgCircle.setImageResource(R.drawable.circle_general);

                        _rl4Option.setVisibility(View.VISIBLE);
                        imgCircle.setVisibility(View.GONE);
                        _lvActivities.setVisibility(View.GONE);

                    }
                });
                break;
*/

            case R.id.btnsubmitassignpoints:

                Student student = AssignPointFeatureController.getInstance().get_selectedStudent();
                TeacherDashbordPoint _tepoint = DashboardFeatureController.getInstance().getTeacherpoint();
                selectedActivityId = ActivityListFeatureController.getInstance().getSeletedActivityId();
                ////selectedSubjectId = SubjectFeatureController.getInstance().get_seletedSubjectId();
                //   selectedSubjectId = AssignPointFeatureController.getInstance().get_seletedSubjectId();
                selectedSubjectId = SubjectFeatureController.getInstance().get_seletedSubjectId();


                Log.i(_TAG, "Selected subject is : " + selectedSubjectId);
                boolean isStudyClicked = AssignPointFeatureController.getInstance().isStudyClicked();

                String prnNO = student.get_stdPRN();

                Log.i(_TAG, "Value of prn is: " + prnNO);
                String methodID = "1";

                String usertype = AssignPointFeatureController.getInstance().get_emailID();
                //String activityID = selectedActivityName;
                Log.i(_TAG, "Value of activity id is: " + selectedActivityId);
                String subjectId = "0";
                String activityId = "0";
                //String activityId = "0";
                //String rewardValue = _points;

                logintype = spinner.getSelectedItem().toString();
                String rewardValue = txt_point.getText().toString();
                String rewardValue1 = txt_mark.getText().toString();
                String rewardValue2 = txt_point2.getText().toString();
                String grade = spinner1.getSelectedItem().toString();
                String commentPoint=_comment.getText().toString();

                Log.i(_TAG, "Value of points is: " + _points);
                String date = getDate();
                Log.i(_TAG, "Value of date is: " + date);
                String greenPoint = String.valueOf(_tepoint.get_greenpoint());


                if (logintype.equals(WebserviceConstants.VAL_USER_TYPE_GUGMENT)) {

                    methodID = "1";


                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                            activityId, selectedSubjectId, rewardValue, date,commentPoint);
                    clearActivityList();
                } else if (logintype.equals(WebserviceConstants.VAL_USER_TYPE_MARK)) {

                    methodID = "2";
                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                            activityId, selectedSubjectId, rewardValue1, date,commentPoint);
                    clearActivityList();

                } else if (logintype.equals(WebserviceConstants.VAL_USER_TYPE_GRADE)) {

                    if (grade.equals(WebserviceConstants.VAL_USER_TYPE_GRADE_A)) {

                        methodID = "3";
                        String rewardValue3 = "A";
                        _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                activityId, selectedSubjectId, rewardValue3, date,commentPoint);
                        clearActivityList();


                    } else if (grade.equals(WebserviceConstants.VAL_USER_TYPE_GRADE_B)) {
                        methodID = "3";
                        String rewardValue3 = "B";
                        _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                activityId, selectedSubjectId, rewardValue3, date,commentPoint);
                        clearActivityList();

                    } else if (grade.equals(WebserviceConstants.VAL_USER_TYPE_GRADE_C)) {
                        methodID = "3";
                        String rewardValue3 = "C";
                        _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                selectedActivityId, subjectId, rewardValue3, date,commentPoint);
                        clearActivityList();
                    } else if (grade.equals(WebserviceConstants.VAL_USER_TYPE_GRADE_D)) {
                        methodID = "3";
                        String rewardValue3 = "D";
                        _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                selectedActivityId, subjectId, rewardValue3, date,commentPoint);
                        clearActivityList();
                    }

                } else if (logintype.equals(WebserviceConstants.VAL_USER_TYPE_PERSENTILE)) {
                    methodID = "4";
                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                            selectedActivityId, subjectId, rewardValue2, date,commentPoint);
                    clearActivityList();

                }
/*
                    if (isStudyClicked == true) {
                        if (student != null && !(TextUtils.isEmpty(selectedSubjectId))
                                && !TextUtils.isEmpty(_points)) {

                            String prnNO = student.get_stdPRN();
                            Log.i(_TAG, "Value of prn is: " + prnNO);
                            String methodID = "1";
                            String activityId = "";
                            String rewardValue = _points;
                            Log.i(_TAG, "Value of points is: " + _points);

                            String date = getDate();
                            Log.i(_TAG, "Value of date is: " + date);

                            _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO,
                                    methodID, activityId, selectedSubjectId, rewardValue, date);

                            // clearSubjectList();
                           *//* SubjectFeatureController.getInstance().
                                    set_seletedSubjectId(null);*//*

                            subFeaturecontroller.getInstance()._clearSubjectList();
                        }

                    } else {
                        if (student != null && !(TextUtils.isEmpty(selectedActivityId))
                                && !TextUtils.isEmpty(_points)) {


                            String prnNO = student.get_stdPRN();

                            Log.i(_TAG, "Value of prn is: " + prnNO);
                            String methodID = "1";
                            //String activityID = selectedActivityName;
                            Log.i(_TAG, "Value of activity id is: " + selectedActivityId);
                            String subjectId = "0";
                            //String activityId = "0";
                            String rewardValue = _points;
                            Log.i(_TAG, "Value of points is: " + _points);
                            String date = getDate();
                            Log.i(_TAG, "Value of date is: " + date);
                            _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                    selectedActivityId, subjectId, rewardValue, date);
                            clearActivityList();

                            //  ActivityListFeatureController.getInstance().
                            //  setSeletedActivityId(null);


                        }
                    }*/
        }


    }

    private boolean _isAcivityPopulated(ArrayList<TeacherActivity> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;

    }

    private void _studentEventListeners() {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

    }

    /**
     * webservice call to fetch activity list from server
     *
     * @param schoolId
     */
    private void _fetchSubjectListFromServer(String teacherId, String schoolId) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        SubjectFeatureController.getInstance().fetchTeacherSubjectFromServer(teacherId, schoolId);
    }

    /**
     * webservice call to fetch point and submit that point from server
     *
     * @param schoolId,teacherId,stPRN,methodId,activityId,subjectId,rewardValue,date
     */
    private void _fetchSubmitPointFromServer(String teacherId, String schoolId, String stPRN, String methodId, String activityId, String subjectId,
                                             String rewardValue, String date, String comment) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
        String dd = "pointtype";
        _registerNetworkListeners();
        AssignPointFeatureController.getInstance().getSubmitPointFromServer(teacherId, schoolId, stPRN, methodId, activityId,
                subjectId, rewardValue, date, dd, comment);
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

        EventNotifier eventNotifier1 =
                NotifierFactory.getInstance().getNotifier
                        (NotifierFactory.EVENT_NOTIFIER_STUDENT);
        eventNotifier1.unRegisterListener(this);


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
                    _assignPointFragment.refreshListview();


                }
                break;

            case EventTypes.EVENT_UI_NO_STUDENT_LIST_RECEIVED:
                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_STUDENT);
                event1.unRegisterListener(this);

                _assignPointFragment.showNoStudentListMessage(false);
                break;

            case EventTypes.EVENT_UI_TEACHER_SUBJECT_RECEIVED:
                EventNotifier eventNotifier2 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier2.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {

                    Student s = StudentFeatureController.getInstance().getSelectedStudent();
                    prn = s.get_stdPRN();
                    //subFeaturecontroller.getInstance().fetchSubjectFromServer(_teacherId, _schoolId, prn);

                    _activityList = ActivityListFeatureController.getInstance().get_teacherActivityList();
                    _subjectList = SubjectFeatureController.getInstance().get_subjectList();

                    _assignPointFragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            _adapter = new AssignPointSubjectwiseAdapter(_assignPointFragment,
                                    AssignPointConSubject.this, _subjectList);


                            listView.setAdapter(_adapter);
                            listView.setVisibility(View.VISIBLE);

                        }
                    });


                }
                break;

            case EventTypes.EVENT_UI_TEACHER_SUBJECT_NOT_RECEIVED:
                EventNotifier event2 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event2.unRegisterListener(this);

                _assignPointFragment.showNoActivityListMessage(false);
                break;

            case EventTypes.EVENT_UISUBJECT:
                EventNotifier eventNotifier3 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier3.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {


                }
                break;

            case EventTypes.EVENT_UI_NOT_SUBJECT:
                EventNotifier event3 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event3.unRegisterListener(this);

                _assignPointFragment.showNoActivityListMessage(false);
                break;
            case EventTypes.EVENT_UI_TEACHER_ASSIGN_POINT_RECEIVED:
                EventNotifier eventNotifier4 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier4.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _assignPointFragment.showpoinSubmitSucessfully(true);
                }
                break;
            case EventTypes.EVENT_UI_NO_TEACHER_ASSIGN_POINT_RECEIVED:
                EventNotifier eventNotifier5 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier5.unRegisterListener(this);


                _assignPointFragment.showNotEnoughPoint();

            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;


    }

    public void clear() {
        _unRegisterEventListeners();

        if (_assignPointFragment != null) {
            _assignPointFragment = null;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // _assignPointFragment.showType(position);
        _studentList = StudentFeatureController.getInstance().getStudentList();
       /* switch (parent.getId()){
            case R.id.grid:*/
        if (_studentList != null && _studentList.size() > 0) {
            Student student = _studentList.get(position);
            if (student != null) {

                String name = student.get_stdName();
                String txtsubName = student.get_stdsubname();
                String stuPRN = student.get_stdPRN();
                String subCode = student.get_stdsubcode();
                a.add(txtsubName);
                a.add(subCode);


                // int foo = Integer.parseInt(txtsubName);
                _assignPointFragment.setStudentNameOnUI(name);
                AssignPointFeatureController.getInstance().set_selectedStudent(student);
                //    AssignPointFeatureController.getInstance().set_selectedPrn(stuPRN);


                //    AssignPointFeatureController.getInstance().set_selestusubList(a);
                AssignPointFeatureController.getInstance().set_selectedSubjsct(a);


            }
        }   /*  break;
            case R.id.spinner:
                _assignPointFragment.showType(position);
                break;
            case R.id.spinner2:

                break;
        }*/
    }

    public void clearActivityList() {


        if (_activityList != null) {
            _activityList.clear();
        }
    }

    public void clearSubjectList() {


        if (_subNameCodeList != null) {
            _subNameCodeList.clear();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {
        //seekpointsbar = (SeekBar) _view.findViewById(R.id.seekassigpoints);
        txtseekPoint = (CustomTextView) _view.findViewById(R.id.txtassignedPoints);

        _assignPointFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Seekvalue = seekBar.getProgress();
                _points = String.valueOf(Seekvalue); // this is the string
                // that will be put
                // above the slider
                txtseekPoint.setText(_points + " Points");

            }
        });


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


}
