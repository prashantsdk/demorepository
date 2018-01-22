package com.blueplanet.smartcookieteacher.ui.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SendRequestFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SharePointFeatureController;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.SendRequestFragment;
import com.blueplanet.smartcookieteacher.ui.SharePointFragment;
import com.blueplanet.smartcookieteacher.webservices.SendRequest;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Sayali on 8/30/2017.
 */
public class SendRequestController implements IEventListener, View.OnClickListener {


    private SendRequestFragment _Fragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private Teacher _teacher;
    private ArrayList<ShairPointModel> _sharePointlist;
    private String _teacherId, _schoolId;
    private int _lastInputId = 0;
    private Spinner spinner, spinner1, spinnercolr;
    private CustomEditText _first_name, _middleName, _lastName_, _Email, _phone,_selectTS;
    private CustomTextView _txt_toastMsg;

    /**
     * constructur for student list
     */


    public SendRequestController(SendRequestFragment Fragment, View View) {

        _Fragment = Fragment;
        _view = View;
        _teacher = LoginFeatureController.getInstance().getTeacher();
        _sharePointlist = SharePointFeatureController.getInstance().get_teachershair();

        spinner = (Spinner) _view.findViewById(R.id.spinner);
        _txt_toastMsg=(CustomTextView)_view.findViewById(R.id.toast_msg);
        if (_teacher != null) {

            _teacher = LoginFeatureController.getInstance().getTeacher();
            if (_teacher != null) {
                _teacherId = _teacher.get_tId();
                _schoolId = _teacher.get_tSchool_id();
                //_fetchteachershairPointFromServer(_teacherId, _schoolId);
            }

        }
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
     */
    private void _fetchSendRequestFromServer(String sendreMemberId, String senderEntityId, String recivEntityId, String countrycode, String mobile, String email, String fname
            , String mname, String lanme, String platform, String sendstatus, String invitationName) {
        _registerStudentEventListeners();

        SendRequestFeatureController.getInstance().getSendRequestListFromServer(sendreMemberId, senderEntityId, recivEntityId, countrycode, mobile, email, fname
                , mname, lanme, platform, sendstatus, invitationName);
        _Fragment.showOrHideProgressBar(true);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_send:
                // _regFragment.hideSoftKeyboard();

                if (NetworkManager.isNetworkAvailable()) {
                    _first_name = (CustomEditText) _view.findViewById(R.id.edt_first_name);
                    _middleName = (CustomEditText) _view.findViewById(R.id.edt_middle_name);
                    _lastName_ = (CustomEditText) _view.findViewById(R.id.edt_last);
                    _Email = (CustomEditText) _view.findViewById(R.id.edt_email);
                    _phone = (CustomEditText) _view.findViewById(R.id.edt_phone);


                    String Fname = _first_name.getText().toString();
                    String mname = _middleName.getText().toString();
                    String lname = _lastName_.getText().toString();
                    String email = _Email.getText().toString();
                    String Phone = _phone.getText().toString();


                    String senderEntityId = "103";

                    String platform = "android";
                    String countrycode = "91";
                    String requestStatus = "request_sent";
                    _teacherId = String.valueOf(_teacher.getId());
                    String entityType = spinner.getSelectedItem().toString();

                    String tComplName = _teacher.get_tCompleteName();

                    String sourse = LoginFeatureController.getInstance().getDevicedetail();

                    if (!TextUtils.isEmpty(Fname) && !TextUtils.isEmpty(mname) && !TextUtils.isEmpty(lname) || !TextUtils.isEmpty(email)
                            || !TextUtils.isEmpty(Phone)) {

                        if (entityType == "Teacher")
                        {
                            String reciverEntityId = "103";
                            _fetchSendRequestFromServer(_teacherId, senderEntityId, reciverEntityId, countrycode, Phone, email, Fname, mname, lname, platform,
                                    requestStatus, tComplName);
                        }else

                        {
                            if (entityType == "Student")
                            {
                                String reciverEntityId = "105";

                                _fetchSendRequestFromServer(_teacherId, senderEntityId, reciverEntityId, countrycode, Phone, email, Fname, mname, lname, platform,
                                        requestStatus, tComplName);
                            }
                        }


                    }else {
                        Toast.makeText(MainApplication.getContext(),

                                "Please enter all fields",
                                Toast.LENGTH_SHORT).show();
                    }


                } else {
                    _Fragment.showNetworkToast(false);
                }
                break;


            default:
                break;

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

        switch (eventType) {            case EventTypes.EVENT_UI_SEND_REQUEST:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    _Fragment.showOrHideProgressBar(false);
                    //_Fragment.sendRequestPoint();

                    _txt_toastMsg.setText("Request Send Successfully..!!");

                }
                break;

            case EventTypes.EVENT_UI_NOT_SEND_REQUEST:

                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event1.unRegisterListener(this);

                _Fragment.showOrHideProgressBar(false);
                //_Fragment.sendRequestProblem();
                _txt_toastMsg.setText("There is some problem to send request..!!");

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
            case EventTypes.EVENT_CONFLCTLOGIN_RESPONSE:
                EventNotifier eventNetwork2 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNetwork2.unRegisterListener(this);

                _Fragment.showOrHideProgressBar(false);
             //   _Fragment.sendRequestAlredyExist();
                _txt_toastMsg.setText("user already exists..!!");

                break;
            case EventTypes.EVENT_INVALID_INPUT:
                EventNotifier eventNetwork3 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNetwork3.unRegisterListener(this);

                _Fragment.showOrHideProgressBar(false);
                //_Fragment.sendRequestInvalidinput();
                _txt_toastMsg.setText("invalid input Please check it..!!");

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


}
