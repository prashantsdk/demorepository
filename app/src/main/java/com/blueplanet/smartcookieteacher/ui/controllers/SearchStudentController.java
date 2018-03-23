package com.blueplanet.smartcookieteacher.ui.controllers;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ErrorInfo;
import com.blueplanet.smartcookieteacher.communication.HTTPConstants;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.DisplaySubjectFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SearchStudentFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.subFeaturecontroller;
import com.blueplanet.smartcookieteacher.models.DisplayTeacSubjectModel;
import com.blueplanet.smartcookieteacher.models.SearchStudent;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragment;
import com.blueplanet.smartcookieteacher.ui.DisplaySubjectFragment;
import com.blueplanet.smartcookieteacher.ui.SearchAssignPointFragment;
import com.blueplanet.smartcookieteacher.ui.SearchStudentDetailFragment;
import com.blueplanet.smartcookieteacher.ui.SearchStudentFragment;
import com.blueplanet.smartcookieteacher.utils.CommonFunctions;
import com.blueplanet.smartcookieteacher.utils.DelayAutoCompleteTextView;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sayali on 12/1/2017.
 */
public class SearchStudentController implements View.OnClickListener,IEventListener,AdapterView.OnItemClickListener {


    static int maxlenghth=0;
    private final String _TAG = this.getClass().getSimpleName();
    ListView liststudent;
    ImageView imgCross;
    DelayAutoCompleteTextView etxtSearch;
    String snamekey="";
    private SearchStudentFragment _subjectFragment = null;

    private View view;
    private String _teacherId, _schoolId;
    private Teacher _teacher;
    private ProgressBar indicator;
    private SearchStudentAdapter searchStudentAdapter;

    /**
     * constructor
     *
     * @param
     * @param view
     */

    public SearchStudentController(final SearchStudentFragment subjectFragment, View view, final SearchStudentAdapter searchStudentAdapter) {
        _subjectFragment = subjectFragment;
        this.view = view;
        this.searchStudentAdapter =  searchStudentAdapter;
        _initUI();
        _teacher = LoginFeatureController.getInstance().getTeacher();
        if (_teacher != null) {
            _teacherId = _teacher.get_tId();
            _schoolId = _teacher.get_tSchool_id();
        }

        // getFriendsFromLocalDB();
        etxtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                int l = s.length();

                ArrayList<SearchStudent> array_stud = SearchStudentFeatureController.getInstance().getSearchedStudents();

                if (array_stud != null) {
                    _subjectFragment.notifydatasetchanged(s);
                }

                if (maxlenghth < l) {
                    maxlenghth = l;

                    if (maxlenghth > 0) {
                        if (maxlenghth >= 1) {
                            snamekey = s.toString();
                            _schoolId = _teacher.get_tSchool_id();
                           /* if(maxlenghth >= 3){
                                progressDialog = WebserviceConstants.showProgress(_subjectFragment.getActivity(), "Loading...");
                                progressDialog.show();
                            }*/
                           // SearchFriends(snamekey,_schoolId,"0");
                        }
                        imgCross.setVisibility(View.VISIBLE);
                    } else {
                        SearchStudentFeatureController.getInstance().clearArray();
                        imgCross.setVisibility(View.INVISIBLE);
                    }
                }
                if (l == 0) {
                    maxlenghth = 0;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                if(etxtSearch.getText().length() == 0) {
                    DisplaySubjectFeatureController.getInstance().clearArray();

                    DisplaySubjectFeatureController.getInstance().getSearchedTeacher();
                    SearchStudentFeatureController.getInstance().clearArray();

                    if (searchStudentAdapter != null) {
                        searchStudentAdapter.notifyDataSetChanged();
                    }
                }else if(s.length() > 0){
                    SearchStudentFeatureController.getInstance().clearArray();
                    indicator.setVisibility(View.VISIBLE);
                    SearchFriends(s.toString(),_schoolId,"0");
                }
            }
        });
    }


    private void _initUI() {
        liststudent =  view.findViewById(R.id.lststudentlist);
        etxtSearch = view.findViewById(R.id.etxtSearch_new);
        imgCross = view.findViewById(R.id.imgcross_new);
        indicator = view.findViewById(R.id.pb_loading_indicator);
    }

    private void LoadMyTeachers(){
        // arr_teacher = TeachersFeatureController.getInstance().getTeachers();
        //       _FriendsFragment.showMyTeachers(arr_teacher);


    }

    private void SearchFriends(String name_key,String schoolId,String offset) {

      //  priyanka changes
      /* progressDialog = WebserviceConstants.showProgress(_subjectFragment.getActivity(), "Loading students..");
       progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();*/

        _registerEventListeners();
        _registerNetworkListener();
        SearchStudentFeatureController.getInstance().searchStudentFromServer(name_key,schoolId,offset);
        //_subjectFragment.showOrHideLoadingSpinner(true);
    }



    /**
     * function to register event listeners
     */

    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
    }


    /**
     * function to register network listener
     */

    private void _registerNetworkListener() {
        EventNotifier notifierSubscribe =
                NotifierFactory.getInstance().getNotifier(
                        NotifierFactory.EVENT_NOTIFIER_NETWORK);
        notifierSubscribe.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
    }

    /**
     * method to do all clearing tasks
     */

    public void close() {
        if (_subjectFragment != null) {
            _subjectFragment = null;
        }
    }

    /**
     * webservice to get shop list from server
     */

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.imgcross_new:
                etxtSearch.setText("");
                maxlenghth = 0;
            //    CommonFunctions.hideKeyboardFrom(_subjectFragment.getActivity(),v);
                DisplaySubjectFeatureController.getInstance().clearArray();

                DisplaySubjectFeatureController.getInstance().getSearchedTeacher();
                SearchStudentFeatureController.getInstance().clearArray();

                if(searchStudentAdapter != null) {
                    searchStudentAdapter.notifyDataSetChanged();
                }

                break;
            /*case R.id.searchStudent:

                CommonFunctions.hideKeyboardFrom(_subjectFragment.getActivity(),v);

                final String offset = "0";
                String name = etxtSearch.getText().toString().trim();
                if(name.length() > 0) {
                    progressDialog = WebserviceConstants.showProgress(_subjectFragment.getActivity(), "Loading...");
                    progressDialog.show();

                    if (NetworkManager.isNetworkAvailable()) {
                        SearchFriends(name, _schoolId, offset);
                    } else
                        CommonFunctions.showNetworkMsg(_subjectFragment.getActivity());
                }else{
                    _subjectFragment.searchCriteriaMessage();
                }
                break;*/

            case R.id.parent_layout_friends:
                _subjectFragment._hidekbd();

                break;
        }
    }
//sayali


    /*private void getFriendsFromLocalDB() {
        // TODO Auto-generated method stub

      //  MainApplication.dbhelper.getAllFriends(student.getS_PRN());
        _FriendsFragment.showMyFriends(SearchFriendsFeatureController.getInstance().getSearchedStudent());

    }
*/
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

        if (_subjectFragment != null) {
            _subjectFragment = null;
        }
    }
    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = -1;
        if (serverResponse != null) {
            errorCode = serverResponse.getErrorCode();
        }
        Log.i(_TAG, "" + eventType);
        switch (eventType) {
            case EventTypes.EVENT_UI_SEARCH_STUDENT:
                EventNotifier eventNotifierlogin =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifierlogin.unRegisterListener(this);
              //  _subjectFragment.showOrHideLoadingSpinner(false);
                if (errorCode == WebserviceConstants.SUCCESS) {

                    if(indicator != null){
                        _subjectFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                indicator.setVisibility(View.GONE);
                            }
                        });
                    }
                    ArrayList<SearchStudent> array_stud= SearchStudentFeatureController.getInstance().getSearchedStudents();
                    _subjectFragment.showMyFriends(array_stud);

                    _subjectFragment.showStudentisavailable(true);

                }else {
                    _subjectFragment.showStudentisavailable(false);
                }
                break;

            case EventTypes.EVENT_UI_NOT_SEARCH_STUDENT:
                EventNotifier eventNotifier5 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier5.unRegisterListener(this);

                if(indicator != null){
                    _subjectFragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            indicator.setVisibility(View.GONE);
                        }
                    });
                }
                _subjectFragment.showStudentisavailable(false);

                break;


            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNotifiernetwork =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNotifiernetwork.unRegisterListener(this);

                _subjectFragment.showNetworkMessage(true);
                break;

            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier event =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_NETWORK);
                event.unRegisterListener(this);
              //  _subjectFragment.showOrHideLoadingSpinner(false);
                _subjectFragment.showNetworkMessage(false);

                break;


            case EventTypes.EVENT_UI_BAD_REQUEST:
                EventNotifier eventNotifier1 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
                eventNotifier1.unRegisterListener(this);

             //   _subjectFragment.showOrHideLoadingSpinner(false);

                _subjectFragment.showbadRequestMessage();
                break;

            case EventTypes.EVENT_UI_UNAUTHORIZED:
                EventNotifier eventNotifier2 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_STUDENT);
                eventNotifier2.unRegisterListener(this);
             //   _subjectFragment.showOrHideLoadingSpinner(false);

                _subjectFragment.showStudentisavailable(false);
                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CommonFunctions.hideKeyboardFrom(_subjectFragment.getActivity(),view);

        ArrayList<SearchStudent> filteredList = SearchStudentFeatureController.getInstance().getSearchedStudents();

        if (filteredList != null && filteredList.size() > 0) {
            SearchStudent s = filteredList.get(position);
            SearchStudentFeatureController.getInstance().set_selectedSearchStudent(s);
        } else {/*if (_studentList != null && _studentList.size() > 0) {
            Student s = _studentList.get(position);
            StudentFeatureController.getInstance().setSelectedStudent(s);*/

        }
        _subjectFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //_subjectFragment.hideSoftKeyboard();
            }
        });
        subFeaturecontroller.getInstance()._clearSubjectList();

        SearchStudentDetailFragment fragment = new SearchStudentDetailFragment();
       /* AssignPointFragment fragment = new AssignPointFragment();
        Bundle args = new Bundle();
        args.putString("studentlist", "1");
        fragment.setArguments(args);*/
        _loadFragment(R.id.content_frame, fragment);
    }
    private void _loadFragment(int id, Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = _subjectFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
       // ft.addToBackStack(null);
        ft.addToBackStack("SearchStudentFragment");
        ft.commit();
    }
}
