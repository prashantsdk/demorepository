package com.blueplanet.smartcookieteacher.ui.controllers;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.ActivityListFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.AssignPointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DashboardFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.ArtActivity;
import com.blueplanet.smartcookieteacher.models.GeneralActivity;
import com.blueplanet.smartcookieteacher.models.SportActivity;
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
import com.blueplanet.smartcookieteacher.ui.ApplicationConstants;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragment;
import com.blueplanet.smartcookieteacher.utils.CommonFunctions;
import com.blueplanet.smartcookieteacher.utils.JSONfunctions;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by 1311 on 28-12-2015.
 */
public class AssignPointFragmentController implements OnClickListener, IEventListener, OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {
    private AssignPointFragment _assignPointFragment;
    private View _view;
    private Teacher _teacher;
    private ArrayList<TeacherActivity> _activityList;
    private ArrayList<TeacherSubject> _subList;
    private ArrayList<Student> _stusubList;
    private String _teacherId, _schoolId;
    private ArrayList<Student> _studentList = null;
    // private AutoGridView _lvActivities = null;
    private GridView _lvActivities = null;
    //private AssignPointListAdapter _adapter = null;

    private GeneralActivitListAdapter generalActivitListAdapter = null;
    private SportListAdapter sportListAdapter = null;
    private ArtActivityListAdapter artActivityListAdapter = null;

    private AssignPointSubjectAdapter1 _subadapter;
    private CustomTextView txtseekPoint;
    private int Seekvalue;
    private String _points = null;
    private final String _TAG = this.getClass().getSimpleName();
    private String selectedActivityId;
    private String selectedSubjectId;
    private String _activityType = null;
    private ArrayList<String> a = new ArrayList<String>();
    private ArrayList<SubNameCode> _subNameCodeList;
    String prn;
    String countrycode = "", logintype = "";
    private Spinner spinner, spinner1, spinnercolr;

    private TextView txt_point;
    private EditText txt_mark;
    JSONfunctions js = new JSONfunctions();

    private EditText _txt_gradePoint, txt_point2, _comment;

    private TextView txtMark;
    private CustomTextView txtbackbutton;
    RelativeLayout _rl4Option;
    ProgressDialog mProgressDialog;


    boolean resultFlag = false;

    boolean activityResultFlag = false;

    SubNameCode _namesub = null;

    boolean checkFlagStatus;
    ArrayList<SubNameCode> subNameCodelist = new ArrayList<>();


    private ArrayList<ArtActivity> artActivities = new ArrayList<>();
    private ArrayList<GeneralActivity> generalActivities = new ArrayList<>();
    private ArrayList<SportActivity> sportActivities = new ArrayList<>();


    public AssignPointFragmentController(AssignPointFragment assignPointFragment, View view) {

        _assignPointFragment = assignPointFragment;
        _view = view;
        spinner = (Spinner) _view.findViewById(R.id.spinner);
        spinner1 = (Spinner) _view.findViewById(R.id.spinner2);
        spinnercolr = (Spinner) _view.findViewById(R.id.spinnercolor);

        txt_point = (TextView) _view.findViewById(R.id.txt_point);
        txt_mark = (EditText) _view.findViewById(R.id.txt_point1);
        _comment = (EditText) _view.findViewById(R.id.txt_comment);
        _txt_gradePoint = (EditText) _view.findViewById(R.id.txt_gradePoint);

        txtMark = (TextView) _view.findViewById(R.id.txt_markPoint);
        txt_point2 = (EditText) _view.findViewById(R.id.txt_point2);
        _studentList = StudentFeatureController.getInstance().getStudentList();
        txtbackbutton = (CustomTextView) _view.findViewById(R.id.txtbackbutton);

        _teacher = LoginFeatureController.getInstance().getTeacher();
        txtseekPoint = (CustomTextView) _view.findViewById(R.id.txtassignedPoints);


        if (_teacher != null) {
            _teacherId = _teacher.get_tId();
            _schoolId = _teacher.get_tSchool_id();
            String id = _teacher.get_tId();
            // _fetchActivityListFromServer(_schoolId);

            new FetachActivityListFromServer().execute();

        }

    }

    private class FetachActivityListFromServer extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {
            super.onPreExecute();


            mProgressDialog = new ProgressDialog(_assignPointFragment.getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();

        }


        @Override
        protected Void doInBackground(Void... voids) {

            artActivities.clear();
            sportActivities.clear();
            generalActivities.clear();

            if (_teacher != null) {
                _teacherId = _teacher.get_tId();
                _schoolId = _teacher.get_tSchool_id();
                String id = _teacher.get_tId();
                // _fetchActivityListFromServer(_schoolId);

                JSONObject jsonObjSend = new JSONObject();
                try {
                    jsonObjSend.put(WebserviceConstants.KEY_SCHOOLID, _schoolId);

                    String response = js.getJSONfromURL(
                            WebserviceConstants.HTTP_BASE_URL +
                                    WebserviceConstants.BASE_URL + WebserviceConstants.TEACHER_ACIVITY, jsonObjSend);

                    JSONArray responseData = null;
                    if (response != null) {
                        JSONObject json = new JSONObject(response);

                        responseData = json.optJSONArray(WebserviceConstants.KEY_POSTS);

                        if (responseData != null) {

                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject jsonObject = responseData.optJSONObject(i);

                                String sc_id = jsonObject.optString(WebserviceConstants.KEY_SC_ID);
                                String sc_list = jsonObject.optString(WebserviceConstants.KEY_SC_LIST);
                                String activityType = jsonObject.optString(WebserviceConstants.KEY_ACTIVITY_TYPE);

                                if (activityType.equals("Arts")) {
                                    ArtActivity artActivity = new ArtActivity(sc_id, sc_list);
                                    artActivities.add(artActivity);
                                }
                                if (activityType.equals("Sports")) {
                                    SportActivity sportActivity = new SportActivity(sc_id, sc_list);
                                    sportActivities.add(sportActivity);
                                }

                                if (activityType.equals("General Activity")) {
                                    GeneralActivity generalActivity = new GeneralActivity(sc_id, sc_list);
                                    generalActivities.add(generalActivity);
                                }


                            }
                            activityResultFlag = true;

                        } else {
                            activityResultFlag = false;
                        }

                    } else {
                        activityResultFlag = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mProgressDialog.dismiss();


            if (activityResultFlag == false) {

                Toast.makeText(_assignPointFragment.getActivity(), "Activity List not available", Toast.LENGTH_SHORT).show();

            }
           /* if (artActivities.size() < 0) {

                Toast.makeText(_assignPointFragment.getActivity(),"Art Activity list is empty",Toast.LENGTH_SHORT).show();
            }
            if (sportActivities.size() < 0) {

            }
            if (generalActivities.size() < 0) {

            }*/


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
        // _lvActivities = (AutoGridView) _view.findViewById(R.id.lstActivity);
        _lvActivities = (GridView) _view.findViewById(R.id.lstActivity);

        final ImageView imgCircle = (ImageView) _view.findViewById(R.id.imgSelectedOption);
        _rl4Option = (RelativeLayout) _view.findViewById(R.id.rel4Option);
        switch (id) {
            case R.id.txtGeneralAssignPoints:

                _activityType = ApplicationConstants.KEY_GENERAL_ACTIVITY;
                AssignPointFeatureController.getInstance().setIsStudyClicked(false);
                _activityList = ActivityListFeatureController.getInstance().getActivitylistInfoFromDB(_activityType);
                _assignPointFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtbackbutton.setVisibility((View.VISIBLE));


                        _rl4Option.setVisibility(View.GONE);

                     /*   _adapter = new AssignPointListAdapter1(_assignPointFragment,
                                AssignPointFragmentController.this, _activityList);
*/

                        // _lvActivities.setAdapter(_adapter);

                        generalActivitListAdapter = new GeneralActivitListAdapter(_assignPointFragment,
                                AssignPointFragmentController.this, generalActivities);


                        _lvActivities.setAdapter(generalActivitListAdapter);
                        _lvActivities.setVisibility(View.VISIBLE);

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
                        txtbackbutton.setVisibility((View.VISIBLE));


                        _rl4Option.setVisibility(View.GONE);


                     /*   _adapter = new AssignPointListAdapter1(_assignPointFragment,
                                AssignPointFragmentController.this, _activityList);

                        _lvActivities.setAdapter(_adapter);
                        */

                        sportListAdapter = new SportListAdapter(_assignPointFragment,
                                AssignPointFragmentController.this, sportActivities);

                        _lvActivities.setAdapter(sportListAdapter);

                        _lvActivities.setVisibility(View.VISIBLE);


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

                        txtbackbutton.setVisibility((View.VISIBLE));

                        _rl4Option.setVisibility(View.GONE);



                       /* _adapter = new AssignPointListAdapter1(_assignPointFragment,

                                AssignPointFragmentController.this, _activityList);

                        _lvActivities.setAdapter(_adapter);
                        */


                        artActivityListAdapter = new ArtActivityListAdapter(_assignPointFragment,

                                AssignPointFragmentController.this, artActivities);

                        _lvActivities.setAdapter(artActivityListAdapter);

                        _lvActivities.setVisibility(View.VISIBLE);


                    }
                });

                break;
            case R.id.txtStudyAssignPoints:
                AssignPointFeatureController.getInstance().setIsStudyClicked(true);


                _assignPointFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        new FetchStudentStudySubject().execute();


/*

                        _adapter = new AssignPointListAdapter1(_assignPointFragment,

                                AssignPointFragmentController.this, _activityList);

                        _lvActivities.setAdapter(_adapter);
                        _lvActivities.setVisibility(View.VISIBLE);
                        // _lvActivities.setOnItemClickListener(AssignPointFragmentController.this);
*/


                    }
                });
                break;
            //txtoptionselected
            case R.id.txtbackbutton:
                _assignPointFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        imgCircle.setImageResource(R.drawable.circle_general);
                        ActivityListFeatureController.getInstance().setSeletedActivityIDOne(false);


                        txtbackbutton.setVisibility((View.GONE));

                        _rl4Option.setVisibility(View.VISIBLE);
                        imgCircle.setVisibility(View.GONE);
                        _lvActivities.setVisibility(View.GONE);

                    }
                });
                break;


            case R.id.btnsubmitassignpoints:

              /*  if (Seekvalue == 0 || Seekvalue >= 101) {

                    _assignPointFragment.showpointSelected(false);
                }*/
                /*else
                if(selectedSubjectId==null) {

                    _assignPointFragment.ShowValidMesaage();




              }*/

                CommonFunctions.hideKeyboardFrom(_assignPointFragment.getActivity(), view);

                Student student = AssignPointFeatureController.getInstance().get_selectedStudent();
                selectedActivityId = ActivityListFeatureController.getInstance().getSeletedActivityId();
                ////selectedSubjectId = SubjectFeatureController.getInstance().get_seletedSubjectId();

                selectedSubjectId = AssignPointFeatureController.getInstance().get_seletedSubjectId();


                TeacherDashbordPoint point = DashboardFeatureController.getInstance().getTeacherpoint();

                int greenPoints = point.get_greenpoint();
                int brownPoints = point.get_brownpoint();
                int waterPoints = point.get_waterpoint();


                Log.i(_TAG, "Selected subject is : " + selectedSubjectId);
                boolean isStudyClicked = AssignPointFeatureController.getInstance().isStudyClicked();


                checkFlagStatus = ActivityListFeatureController.getInstance().getSelectedActivityIDOne();


         /*       && (!(Integer.parseInt(selectedPoints) < 0)
        } else if (Integer.parseInt(selectedPoints) == 0) {
            _genFragment.zeroValueValidation();
        }
*/
                if (checkFlagStatus == true) {

                    if (isStudyClicked) {
                        if (student != null /*&& !(TextUtils.isEmpty(selectedSubjectId)*/
                                ) {
                            if (selectedSubjectId != null) {


                                ActivityListFeatureController.getInstance().setSeletedActivityIDOne(false);

                                String prnNO = student.get_stdPRN();
                                Log.i(_TAG, "Value of prn is: " + prnNO);
                                String methodID = "1";
                                String activityId = "";
                                //  String rewardValue = _points;
                                Log.i(_TAG, "Value of points is: " + _points);

                                String date = getDate();
                                Log.i(_TAG, "Value of date is: " + date);
                                String grade = spinner1.getSelectedItem().toString();

                                //  String temp = spinnercolr.getSelectedItem().toString();
                                int pointTypePosition = spinnercolr.getSelectedItemPosition();

                                logintype = spinner.getSelectedItem().toString();


                                //String[] pointTypeArray = temp.split(" ");

                                String pointtype = "";
                                if (pointTypePosition == 0) {

                                    pointtype = "Greenpoint";
                                } else if (pointTypePosition == 1) {
                                    pointtype = "Waterpoint";
                                }


                                String rewardValue = txt_point.getText().toString();
                                String rewardValue1 = txt_mark.getText().toString();
                                String rewardValue2 = txt_point2.getText().toString();
                                String markPoint = txtMark.getText().toString();

                                String commentPoint = _comment.getText().toString();


                                if (logintype.equals(WebserviceConstants.VAL_USER_TYPE_GUGMENT)) {
                                    if (!rewardValue.isEmpty()) {
                                        methodID = "1";

                                        if (pointtype.equalsIgnoreCase("Greenpoint")) {


                                            if ((greenPoints > Integer.parseInt(rewardValue)) && (!(Integer.parseInt(rewardValue) == 0))) {


                                                _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                        activityId, selectedSubjectId, rewardValue, date, pointtype, commentPoint);
                                                clearActivityList();
                                            } else if (!(greenPoints > Integer.parseInt(rewardValue))) {


                                                Toast.makeText(_assignPointFragment.getActivity(), "Insufficient Reward Points", Toast.LENGTH_SHORT).show();

                                            } else if (Integer.parseInt(rewardValue) == 0) {
                                                _assignPointFragment.zeroValueValidation();
                                            }
                                        } else if (pointtype.equalsIgnoreCase("Waterpoint")) {


                                            if ((waterPoints > Integer.parseInt(rewardValue)) && (!(Integer.parseInt(rewardValue) == 0))) {


                                                _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                        activityId, selectedSubjectId, rewardValue, date, pointtype, commentPoint);
                                                clearActivityList();
                                            } else if (!(waterPoints > Integer.parseInt(rewardValue))) {


                                                Toast.makeText(_assignPointFragment.getActivity(), "Insufficient Purchase Points", Toast.LENGTH_SHORT).show();

                                            } else if (Integer.parseInt(rewardValue) == 0) {
                                                _assignPointFragment.zeroValueValidation();
                                            }
                                        }
                                    } else {


                                  /*  Toast.makeText(_assignPointFragment.getActivity().getApplicationContext(),
                                            _assignPointFragment.getActivity().getString(R.string.select_activity),
                                            Toast.LENGTH_LONG).show();  */

                                        ActivityListFeatureController.getInstance().setSeletedActivityIDOne(true);

                                        Toast.makeText(_assignPointFragment.getActivity(), "Enter the points", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (logintype.equals(WebserviceConstants.VAL_USER_TYPE_MARK)) {
                                    if (45 > Integer.parseInt(rewardValue1) && Integer.parseInt(rewardValue1) > 35) {


                                        //rewardValue1=Integer.parseInt("30");

                                        initListener();
                                        methodID = "2";
                                        _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                activityId, selectedSubjectId, String.valueOf(rewardValue1), date, pointtype, commentPoint);
                                        clearActivityList();
                                    }

                                } else if (logintype.equals(WebserviceConstants.VAL_USER_TYPE_GRADE)) {

                                    if (grade.equals(WebserviceConstants.VAL_USER_TYPE_GRADE_A)) {

                                        methodID = "3";
                                        String rewardValue3 = "A";
                                        _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                activityId, selectedSubjectId, rewardValue3, date, pointtype, commentPoint);
                                        clearActivityList();


                                    } else if (grade.equals(WebserviceConstants.VAL_USER_TYPE_GRADE_B)) {
                                        methodID = "3";
                                        String rewardValue3 = "B";
                                        _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                activityId, selectedSubjectId, rewardValue3, date, pointtype, commentPoint);
                                        clearActivityList();

                                    } else if (grade.equals(WebserviceConstants.VAL_USER_TYPE_GRADE_C)) {
                                        methodID = "3";
                                        String rewardValue3 = "C";
                                        _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                activityId, selectedSubjectId, rewardValue3, date, pointtype, commentPoint);
                                        clearActivityList();
                                    } else if (grade.equals(WebserviceConstants.VAL_USER_TYPE_GRADE_D)) {
                                        methodID = "3";
                                        String rewardValue3 = "D";
                                        _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                activityId, selectedSubjectId, rewardValue3, date, pointtype, commentPoint);
                                        clearActivityList();
                                    }

                                } else if (logintype.equals(WebserviceConstants.VAL_USER_TYPE_PERSENTILE)) {
                                    methodID = "4";
                                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                            activityId, selectedSubjectId, rewardValue2, date, pointtype, commentPoint);
                                    clearActivityList();

                                } else {


                                }
                            } else {
                                Toast.makeText(_assignPointFragment.getActivity().getApplicationContext(),
                                        _assignPointFragment.getActivity().getString(R.string.select_subject),
                                        Toast.LENGTH_LONG).show();
                                ActivityListFeatureController.getInstance().setSeletedActivityIDOne(true);

                            }

                        }
                    } else {
                        if (student != null) {

                            if (selectedActivityId != null) {

                                ActivityListFeatureController.getInstance().setSeletedActivityIDOne(false);


                                String pointtype = spinnercolr.getSelectedItem().toString();
                                int poinTypePosition = spinnercolr.getSelectedItemPosition();

                                String[] pointTypeArray = pointtype.split(" ");

                                String finalPointType = "";


                                if (poinTypePosition == 0) {

                                    finalPointType = "Greenpoint";
                                } else if (poinTypePosition == 1) {
                                    finalPointType = "Waterpoint";
                                }


                                String prnNO = student.get_stdPRN();

                                Log.i(_TAG, "Value of prn is: " + prnNO);
                                String methodID = "1";
                                //String activityID = selectedActivityName;
                                Log.i(_TAG, "Value of activity id is: " + selectedActivityId);
                                String subjectId = "0";
                                //String activityId = "0";
                                // String rewardValue = _points;
                                Log.i(_TAG, "Value of points is: " + _points);
                                String date = getDate();
                                Log.i(_TAG, "Value of date is: " + date);

                                String grade = spinner1.getSelectedItem().toString();
                                logintype = spinner.getSelectedItem().toString();
                                String rewardValue = txt_point.getText().toString();
                                String rewardValue1 = txt_mark.getText().toString();
                                String rewardValue2 = txt_point2.getText().toString();

                                String markValue = txtMark.getText().toString();
                                String commentPoint = _comment.getText().toString();


                                if (logintype.equals(WebserviceConstants.VAL_USER_TYPE_GUGMENT)) {
                                    methodID = "1";
                                    if ((selectedActivityId != null) &&
                                            (!rewardValue.isEmpty())) {


                                        if (finalPointType.equals("Greenpoint")) {

                                            if ((greenPoints > Integer.parseInt(rewardValue)) && (!(Integer.parseInt(rewardValue) == 0))) {


                                                _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                        selectedActivityId, subjectId, rewardValue, date, finalPointType, commentPoint);
                                                clearActivityList();


                                            } else if (!(greenPoints > Integer.parseInt(rewardValue))) {

                                                Toast.makeText(_assignPointFragment.getActivity(),
                                                        "Insufficient Reward Points", Toast.LENGTH_SHORT).show();
                                            } else if ((Integer.parseInt(rewardValue) == 0)) {
                                                _assignPointFragment.zeroValueValidation();
                                            }
                                        } else if (finalPointType.equals("Sponsor")) {

                                            if (brownPoints > Integer.parseInt(rewardValue)) {


                                                _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                        selectedActivityId, subjectId, rewardValue, date, finalPointType, commentPoint);
                                                clearActivityList();
                                            } else {
                                                Toast.makeText(_assignPointFragment.getActivity(),
                                                        "Insufficient brown  points", Toast.LENGTH_SHORT).show();


                                            }
                                        } else if (finalPointType.equals("Waterpoint")) {

                                            if ((waterPoints > Integer.parseInt(rewardValue)) && (!(Integer.parseInt(rewardValue) == 0))) {

                                                _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                        selectedActivityId, subjectId, rewardValue, date, finalPointType, commentPoint);
                                                clearActivityList();


                                            } else if (!(waterPoints > Integer.parseInt(rewardValue))) {

                                                Toast.makeText(_assignPointFragment.getActivity(),
                                                        "Insufficient Purchase  Points", Toast.LENGTH_SHORT).show();

                                            } else if ((Integer.parseInt(rewardValue) == 0)) {
                                                _assignPointFragment.zeroValueValidation();
                                            }

                                        } else {
                                            Toast.makeText(_assignPointFragment.getActivity(), "Method type not selected", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if ((selectedActivityId == null)) {

                                        Toast.makeText(_assignPointFragment.getActivity().getApplicationContext(),
                                                _assignPointFragment.getActivity().getString(R.string.select_activity),
                                                Toast.LENGTH_LONG).show();

                                    } else if (rewardValue.isEmpty()) {

                                        Toast.makeText(_assignPointFragment.getActivity(), "Enter the points", Toast.LENGTH_SHORT).show();
                                        ActivityListFeatureController.getInstance().setSeletedActivityIDOne(true);

                                    }

                                } else if (logintype.equals(WebserviceConstants.VAL_USER_TYPE_MARK)) {

                                    methodID = "2";
                                    if ((selectedActivityId != null)
                                            && (!rewardValue1.isEmpty())
                                            && (!markValue.isEmpty())) {


                                        if (finalPointType.equals("Greenpoint")) {

                                            if (greenPoints > Integer.parseInt(markValue)) {

                                                _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                        selectedActivityId, subjectId, markValue, date, finalPointType, commentPoint);
                                                clearActivityList();

                                            } else {

                                                Toast.makeText(_assignPointFragment.getActivity(), "Insufficent Reward Points", Toast.LENGTH_SHORT).show();
                                            }


                                        } else if (finalPointType.equals("Sponsor")) {

                                            if (brownPoints > Integer.parseInt(markValue)) {

                                                _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                        selectedActivityId, subjectId, markValue, date, finalPointType, commentPoint);
                                                clearActivityList();

                                            } else {

                                                Toast.makeText(_assignPointFragment.getActivity(), "Insufficient brown points", Toast.LENGTH_SHORT).show();
                                            }

                                        } else if (finalPointType.equals("Waterpoint")) {

                                            if (waterPoints > Integer.parseInt(markValue)) {

                                                _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                        selectedActivityId, subjectId, markValue, date, finalPointType, commentPoint);
                                                clearActivityList();

                                            } else {

                                                Toast.makeText(_assignPointFragment.getActivity(), "Insufficent Purchase Point", Toast.LENGTH_SHORT).show();
                                            }
                                        }


                                    } else if (selectedActivityId == null) {
                                        Toast.makeText(_assignPointFragment.getActivity().getApplicationContext(),
                                                _assignPointFragment.getActivity().getString(R.string.select_activity),
                                                Toast.LENGTH_LONG).show();

                                    } else if (rewardValue1.isEmpty()) {

                                        Toast.makeText(_assignPointFragment.getActivity(), "Enter the marks", Toast.LENGTH_SHORT).show();

                                    } else if (markValue.isEmpty()) {
                                        Toast.makeText(_assignPointFragment.getActivity(), "Enter the Points", Toast.LENGTH_SHORT).show();
                                    }

                                } else if (logintype.equals(WebserviceConstants.VAL_USER_TYPE_GRADE)) {

                                    if (grade.equals(WebserviceConstants.VAL_USER_TYPE_GRADE_A)) {

                                        if ((selectedActivityId != null)) {


                                            methodID = "3";
                                            String rewardValue3 = "60";

                                            if (finalPointType.equals("Greenpoint")) {

                                                if (greenPoints > Integer.parseInt(rewardValue3)) {

                                                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                            selectedActivityId, subjectId, rewardValue3, date, finalPointType, commentPoint);
                                                    clearActivityList();

                                                } else {
                                                    Toast.makeText(_assignPointFragment.getActivity(), "Insufficent Reward Points", Toast.LENGTH_SHORT).show();
                                                }
                                            } else if (finalPointType.equals("Sponsor")) {

                                                if (brownPoints > Integer.parseInt(rewardValue3)) {

                                                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                            selectedActivityId, subjectId, rewardValue3, date, finalPointType, commentPoint);
                                                    clearActivityList();

                                                } else {

                                                    Toast.makeText(_assignPointFragment.getActivity(), "Insufficent browen points", Toast.LENGTH_SHORT).show();
                                                }


                                            } else if (finalPointType.equals("Waterpoint")) {

                                                if (waterPoints > Integer.parseInt(rewardValue3)) {

                                                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                            selectedActivityId, subjectId, rewardValue3, date, finalPointType, commentPoint);
                                                    clearActivityList();

                                                } else {

                                                    Toast.makeText(_assignPointFragment.getActivity(), "Insuffient Purchase Points", Toast.LENGTH_SHORT).show();
                                                }
                                            }


                                        } else if (selectedActivityId == null) {

                                            Toast.makeText(_assignPointFragment.getActivity().getApplicationContext(),
                                                    _assignPointFragment.getActivity().getString(R.string.select_activity),
                                                    Toast.LENGTH_LONG).show();


                                        }

                                    } else if (grade.equals(WebserviceConstants.VAL_USER_TYPE_GRADE_B)) {

                                        methodID = "3";
                                        String rewardValue3 = "50";

                                        if (selectedActivityId != null) {


                                            if (finalPointType.equals("Greenpoint")) {

                                                if (greenPoints > Integer.parseInt(rewardValue3)) {

                                                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                            selectedActivityId, subjectId, rewardValue3, date, finalPointType, commentPoint);
                                                    clearActivityList();

                                                } else {
                                                    Toast.makeText(_assignPointFragment.getActivity(), "Insufficent Reward  Points", Toast.LENGTH_SHORT).show();
                                                }
                                            } else if (finalPointType.equals("Sponsor")) {

                                                if (brownPoints > Integer.parseInt(rewardValue3)) {

                                                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                            selectedActivityId, subjectId, rewardValue3, date, finalPointType, commentPoint);
                                                    clearActivityList();

                                                } else {

                                                    Toast.makeText(_assignPointFragment.getActivity(), "Insufficent browen points", Toast.LENGTH_SHORT).show();
                                                }


                                            } else if (finalPointType.equals("Waterpoint")) {

                                                if (waterPoints > Integer.parseInt(rewardValue3)) {

                                                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                            selectedActivityId, subjectId, rewardValue3, date, finalPointType, commentPoint);
                                                    clearActivityList();

                                                } else {

                                                    Toast.makeText(_assignPointFragment.getActivity(), "Insuffient Purchase Point", Toast.LENGTH_SHORT).show();
                                                }
                                            }


                                        } else {
                                            Toast.makeText(_assignPointFragment.getActivity().getApplicationContext(),
                                                    _assignPointFragment.getActivity().getString(R.string.select_activity),
                                                    Toast.LENGTH_LONG).show();

                                        }
                                    } else if (grade.equals(WebserviceConstants.VAL_USER_TYPE_GRADE_C)) {


                                        methodID = "3";
                                        String rewardValue3 = "40";

                                        if (selectedActivityId != null) {


                                            if (finalPointType.equals("Greenpoint")) {

                                                if (greenPoints > Integer.parseInt(rewardValue3)) {

                                                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                            selectedActivityId, subjectId, rewardValue3, date, finalPointType, commentPoint);
                                                    clearActivityList();

                                                } else {
                                                    Toast.makeText(_assignPointFragment.getActivity(), "Insufficent Reward Points", Toast.LENGTH_SHORT).show();
                                                }
                                            } else if (finalPointType.equals("Sponsor")) {

                                                if (brownPoints > Integer.parseInt(rewardValue3)) {

                                                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                            selectedActivityId, subjectId, rewardValue3, date, finalPointType, commentPoint);
                                                    clearActivityList();

                                                } else {

                                                    Toast.makeText(_assignPointFragment.getActivity(), "Insufficent browen points", Toast.LENGTH_SHORT).show();
                                                }


                                            } else if (finalPointType.equals("Waterpoint")) {

                                                if (waterPoints > Integer.parseInt(rewardValue3)) {

                                                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                            selectedActivityId, subjectId, rewardValue3, date, finalPointType, commentPoint);
                                                    clearActivityList();

                                                } else {

                                                    Toast.makeText(_assignPointFragment.getActivity(), "Insuffient Puchase Point", Toast.LENGTH_SHORT).show();
                                                }
                                            }


                                        } else {
                                            Toast.makeText(_assignPointFragment.getActivity().getApplicationContext(),
                                                    _assignPointFragment.getActivity().getString(R.string.select_activity),
                                                    Toast.LENGTH_LONG).show();

                                        }
                                    } else if (grade.equals(WebserviceConstants.VAL_USER_TYPE_GRADE_D)) {

                                        methodID = "3";
                                        String rewardValue3 = "30";

                                        if (selectedActivityId != null) {


                                            if (finalPointType.equals("Greenpoint")) {

                                                if (greenPoints > Integer.parseInt(rewardValue3)) {

                                                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                            selectedActivityId, subjectId, rewardValue3, date, finalPointType, commentPoint);
                                                    clearActivityList();

                                                } else {
                                                    Toast.makeText(_assignPointFragment.getActivity(), "Insufficent Reward Points", Toast.LENGTH_SHORT).show();
                                                }
                                            } else if (finalPointType.equals("Sponsor")) {

                                                if (brownPoints > Integer.parseInt(rewardValue3)) {

                                                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                            selectedActivityId, subjectId, rewardValue3, date, finalPointType, commentPoint);
                                                    clearActivityList();

                                                } else {

                                                    Toast.makeText(_assignPointFragment.getActivity(), "Insufficent browen points", Toast.LENGTH_SHORT).show();
                                                }


                                            } else if (finalPointType.equals("Waterpoint")) {

                                                if (waterPoints > Integer.parseInt(rewardValue3)) {

                                                    _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                            selectedActivityId, subjectId, rewardValue3, date, finalPointType, commentPoint);
                                                    clearActivityList();

                                                } else {

                                                    Toast.makeText(_assignPointFragment.getActivity(), "Insuffient Purchase Point", Toast.LENGTH_SHORT).show();
                                                }
                                            }


                                        } else {
                                            Toast.makeText(_assignPointFragment.getActivity().getApplicationContext(),
                                                    _assignPointFragment.getActivity().getString(R.string.select_activity),
                                                    Toast.LENGTH_LONG).show();

                                        }
                                    }

                                } else if (logintype.equals(WebserviceConstants.VAL_USER_TYPE_PERSENTILE)) {

                                    methodID = "4";
                                    if (selectedActivityId != null) {

                                        _fetchSubmitPointFromServer(_teacherId, _schoolId, prnNO, methodID,
                                                selectedActivityId, subjectId, rewardValue2, date, finalPointType, commentPoint);
                                        clearActivityList();


                                    } else {
                                        Toast.makeText(_assignPointFragment.getActivity().getApplicationContext(),
                                                _assignPointFragment.getActivity().getString(R.string.select_activity),
                                                Toast.LENGTH_LONG).show();

                                    }

                                }
                                //  ActivityListFeatureController.getInstance().
                                //  setSeletedActivityId(null);


                            } else {

                                ActivityListFeatureController.getInstance().setSeletedActivityIDOne(true);

                                Toast.makeText(_assignPointFragment.getActivity().getApplicationContext(),
                                        _assignPointFragment.getActivity().getString(R.string.select_subject),
                                        Toast.LENGTH_LONG).show();

                            }
                        }
                    }

                } else {

                    Toast.makeText(_assignPointFragment.getActivity(), "Please Select the subject", Toast.LENGTH_SHORT).show();
                }


                break;
            default:
                break;

        }


    }


    private void initListener() {
        txtMark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // HelperClass.Is_Valid_Email(txtMark);
                txt_point.setText("30");
            }
        });
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
    private void _fetchActivityListFromServer(String schoolId) {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        ActivityListFeatureController.getInstance().getACtivityListFromServer(schoolId);
    }

    /**
     * webservice call to fetch point and submit that point from server
     *
     * @param schoolId,teacherId,stPRN,methodId,activityId,subjectId,rewardValue,date
     */
    private void _fetchSubmitPointFromServer(String teacherId, String schoolId, String stPRN, String methodId, String activityId, String subjectId,
                                             String rewardValue, String date, String pointtype, String comment) {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        _registerNetworkListeners();
        AssignPointFeatureController.getInstance().getSubmitPointFromServer(teacherId, schoolId, stPRN, methodId, activityId,
                subjectId, rewardValue, date, pointtype, comment);

        _assignPointFragment.showOrHideProgressBar(true);
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

            case EventTypes.EVENT_UI_TEACHER_ACTIVITY_LIST_RECEIVED:
                EventNotifier eventNotifier2 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier2.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {

                    Student s = StudentFeatureController.getInstance().getSelectedStudent();

                    //  prn = s.get_stdPRN();
                    // subFeaturecontroller.getInstance().fetchSubjectFromServer(_teacherId, _schoolId, prn);
                }
                break;

            case EventTypes.EVENT_UI_TEACHER_NO_ACTIVITY_LIST_RECEIVED:
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

                    _assignPointFragment.showOrHideProgressBar(false);
                    _assignPointFragment.showpoinSubmitSucessfully(true);


                }


                break;

            case EventTypes.EVENT_TEACHER_SUBJECT_RULE_ENGINE_NOT_DEFINE:
                EventNotifier eventNotifier5 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier5.unRegisterListener(this);
                _assignPointFragment.showOrHideProgressBar(false);
                _assignPointFragment.ruleEngineNotDefined();

                break;


            case EventTypes.EVENT_UI_NO_TEACHER_ASSIGN_POINT_RECEIVED:
                EventNotifier event4 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event4.unRegisterListener(this);

                _assignPointFragment.showOrHideProgressBar(false);
                _assignPointFragment.showNoAListMessage(false);


                break;
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

        _studentList = StudentFeatureController.getInstance().getStudentList();

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
        }
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


    private class FetchStudentStudySubject extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();


            mProgressDialog = new ProgressDialog(_assignPointFragment.getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();

        }


        @Override
        protected Void doInBackground(Void... voids) {

            subNameCodelist.clear();
            if (_teacher != null) {
                _teacherId = _teacher.get_tId();
                _schoolId = _teacher.get_tSchool_id();


                Student s = AssignPointFeatureController.getInstance().get_selectedStudent();
                String prn = s.get_stdPRN();

                JSONObject jsonObjSend = new JSONObject();
                try {

                    jsonObjSend.put("t_id", _teacherId);
                    jsonObjSend.put("school_id", _schoolId);
                    jsonObjSend.put("std_PRN", prn);


                    String response = js.getJSONfromURL(
                            WebserviceConstants.HTTP_BASE_URL +
                                    WebserviceConstants.BASE_URL + WebserviceConstants.SUBWEBSERVICE, jsonObjSend);


                    if (response != null) {


                        JSONObject json = new JSONObject(response);

                        JSONArray responseData = null;
                        responseData = json.optJSONArray(WebserviceConstants.KEY_POSTS);
                        if (responseData != null) {

                            for (int i = 0; i < responseData.length(); i++) {
                                JSONObject jsonObject = responseData.optJSONObject(i);
                                String subname = jsonObject.optString(WebserviceConstants.SUBNAME);
                                String subcode = jsonObject.optString(WebserviceConstants.SUBCODE);


                                _namesub = new SubNameCode(subname, subcode);
                                subNameCodelist.add(_namesub);

                                resultFlag = true;

                            }
                        } else {
                            resultFlag = false;
                        }
                    }


                } catch (JSONException e) {

                    resultFlag = false;
                }

                /// subFeaturecontroller.getInstance().fetchSubjectFromServer(_teacherId, _schoolId, prn);


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mProgressDialog.dismiss();

            if (resultFlag == true) {


                txtbackbutton.setVisibility(View.VISIBLE);

                _rl4Option.setVisibility(View.GONE);


                //  _subNameCodeList = subFeaturecontroller.getInstance().get_subjList();


                _subadapter = new AssignPointSubjectAdapter1(_assignPointFragment,
                        AssignPointFragmentController.this, subNameCodelist);

                _lvActivities.setAdapter(_subadapter);
                _lvActivities.setVisibility(View.VISIBLE);

            }
            if (resultFlag == false) {

                Toast.makeText(_assignPointFragment.getActivity(), "Study subject list not available", Toast.LENGTH_LONG).show();

            }


        }
    }


}
