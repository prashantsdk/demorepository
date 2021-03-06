package com.blueplanet.smartcookieteacher.ui.controllers;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
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
import com.blueplanet.smartcookieteacher.utils.HelperClass;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by Sayali on 8/30/2017.
 */
public class SendRequestController implements IEventListener, View.OnClickListener {

    int ecolor = Color.RED;
    ForegroundColorSpan fgcspan;
    SpannableStringBuilder ssbuilder;

    private SendRequestFragment _Fragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private Teacher _teacher;
    private ArrayList<ShairPointModel> _sharePointlist;
    private String _teacherId, _schoolId;
    private int _lastInputId = 0;
    private Spinner spinner, spinner1, spinnercolr, spinnerPhone;
    private CustomEditText _first_name, _middleName, _lastName_, _Email, _phone, _selectTS;
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
        spinnerPhone = (Spinner) _view.findViewById(R.id.spinnerPhone);
        _txt_toastMsg = (CustomTextView) _view.findViewById(R.id.toast_msg);

        inItFindViewByIDs();

        initListener();
        if (_teacher != null) {

            _teacher = LoginFeatureController.getInstance().getTeacher();
            if (_teacher != null) {
                _teacherId = _teacher.get_tId();
                _schoolId = _teacher.get_tSchool_id();
                //_fetchteachershairPointFromServer(_teacherId, _schoolId);
            }

        }
    }

    private void inItFindViewByIDs() {

        _first_name = (CustomEditText) _view.findViewById(R.id.edt_first_name);
        _middleName = (CustomEditText) _view.findViewById(R.id.edt_middle_name);
        _lastName_ = (CustomEditText) _view.findViewById(R.id.edt_last);
        _Email = (CustomEditText) _view.findViewById(R.id.edt_email);
        _phone = (CustomEditText) _view.findViewById(R.id.edt_phone);

    }

    private void initListener() {
        _Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                HelperClass.Is_Valid_Email(_Email);
            }
        });

        _phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void _registerStudentEventListeners() {

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
        _registerNetworkListeners();
        _Fragment.showOrHideProgressBar(true);
        SendRequestFeatureController.getInstance().getSendRequestListFromServer(sendreMemberId, senderEntityId, recivEntityId, countrycode, mobile, email, fname
                , mname, lanme, platform, sendstatus, invitationName);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_send:
                // _regFragment.hideSoftKeyboard();

                if (NetworkManager.isNetworkAvailable()) {


                    String Fname = _first_name.getText().toString();
                    String mname = _middleName.getText().toString();
                    String lname = _lastName_.getText().toString();
                    String email = _Email.getText().toString();
                    String Phone = _phone.getText().toString();


                    String senderEntityId = "103";

                    String platform = "android";

                    String requestStatus = "request_sent";
                    _teacherId = String.valueOf(_teacher.getId());
                    String entityType = spinner.getSelectedItem().toString();
                    String countrycode = spinnerPhone.getSelectedItem().toString();

                    String tComplName = _teacher.get_tCompleteName();

                    String sourse = LoginFeatureController.getInstance().getDevicedetail();

                    if ((!TextUtils.isEmpty(Fname)) &&
                            (!TextUtils.isEmpty(mname)) &&
                            (!TextUtils.isEmpty(lname)) &&
                            (!TextUtils.isEmpty(email)) &&
                            (!TextUtils.isEmpty(Phone)) &&
                            (checkPhoneNoLenght(Phone)) &&
                            (phoneNoShouldNotStartWithZero(Phone, countrycode))
                            ) {


                        if (HelperClass.Is_Valid_Email(_Email)) {

                            if (entityType == "Teacher") {
                                String reciverEntityId = "103";
                                _fetchSendRequestFromServer(_teacherId, senderEntityId, reciverEntityId, countrycode, Phone, email, Fname, mname, lname, platform,
                                        requestStatus, tComplName);
                            } else

                            {
                                if (entityType == "Student") {
                                    String reciverEntityId = "105";

                                    _fetchSendRequestFromServer(_teacherId, senderEntityId, reciverEntityId, countrycode, Phone, email, Fname, mname, lname, platform,
                                            requestStatus, tComplName);
                                }
                            }

                        } else {
                            Toast.makeText(_Fragment.getActivity(), "Invalid Email format", Toast.LENGTH_SHORT).show();
                        }

                    } else if ((TextUtils.isEmpty(Fname))) {
                        Toast.makeText(MainApplication.getContext(),

                                "Please enter first name",
                                Toast.LENGTH_SHORT).show();
                    } else if ((TextUtils.isEmpty(mname))) {
                        Toast.makeText(MainApplication.getContext(),

                                "Please enter Middle name",
                                Toast.LENGTH_SHORT).show();
                    } else if ((TextUtils.isEmpty(lname))) {
                        Toast.makeText(MainApplication.getContext(),

                                "Please enter Last name",
                                Toast.LENGTH_SHORT).show();
                    } else if ((TextUtils.isEmpty(email))) {
                        Toast.makeText(MainApplication.getContext(),

                                "Please enter email id",
                                Toast.LENGTH_SHORT).show();
                    } else if ((TextUtils.isEmpty(Phone))) {
                        Toast.makeText(MainApplication.getContext(),

                                "Please enter Phone No.",
                                Toast.LENGTH_SHORT).show();
                    } else if ((!checkPhoneNoLenght(Phone))) {
                        Toast.makeText(MainApplication.getContext(),

                                "Mobile no.must be 10 digits",
                                Toast.LENGTH_SHORT).show();
                    } else if ((!phoneNoShouldNotStartWithZero(Phone, countrycode))) {
                        if (countrycode.equals("+91")) {
                            Toast.makeText(MainApplication.getContext(),
                                    "Mobile No.Should not start 0 to 5 digits", Toast.LENGTH_SHORT).show();

                        }
                        if (countrycode.equals("+1")) {
                            Toast.makeText(MainApplication.getContext(),
                                    "Mobile No.Should not start 0 to 1 digits", Toast.LENGTH_SHORT).show();

                        }
                    }


                } else {
                    _Fragment.showNetworkToast(false);
                }
                break;

            case R.id.btn_cancel:
                //  _Fragment.getActivity().finish();

                _first_name.getText().clear();
                _middleName.getText().clear();
                _lastName_.getText().clear();
                _Email.getText().clear();
                _phone.getText().clear();

                break;


            default:
                break;

        }
    }


    private boolean checkPhoneNoLenght(String mPhone) {

        if (mPhone.length() < 10) {
            return false;
        }
        return true;
    }


    private boolean phoneNoShouldNotStartWithZero(String mPhone, String mCountryCode) {

        boolean flag = false;
        if (mCountryCode.equals("+91")) {
            if ((mPhone.startsWith("0")) ||
                    (mPhone.startsWith("1")) ||
                    (mPhone.startsWith("2")) ||
                    (mPhone.startsWith("3")) ||
                    (mPhone.startsWith("4")) ||
                    (mPhone.startsWith("5"))) {

                flag = false;
            } else {
                flag = true;
            }
        }
        if (mCountryCode.equals("+1")) {

            if ((mPhone.startsWith("0") || (mPhone.startsWith("1")))) {
                flag = false;
            } else {
                flag = true;
            }

        }

        return flag;
    }


    @Override
    public int eventNotify(int eventType, Object eventObject) {
        int eventState = EventState.EVENT_PROCESSED;
        ServerResponse serverResponse = (ServerResponse) eventObject;
        int errorCode = -1;

        if (serverResponse != null) {
            errorCode = serverResponse.getErrorCode();
        }
        if (eventType == 281) {
            eventType = 282;
        }


        switch (eventType) {
            case EventTypes.EVENT_UI_SEND_REQUEST:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {

                    _Fragment.showOrHideProgressBar(false);
                    _Fragment.sendRequestPoint();

                } else {

                    _Fragment.showOrHideProgressBar(false);
                    _Fragment.sendRequestAlredyExist();

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
                // Toast.makeText(_Fragment.getActivity(),"There is some problem to send request..!!",Toast.LENGTH_SHORT).show();


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
                //Toast.makeText(_Fragment.getActivity(),"user already exists..!!",Toast.LENGTH_SHORT).show();


                break;
            case EventTypes.EVENT_INVALID_INPUT:
                EventNotifier eventNetwork3 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNetwork3.unRegisterListener(this);

                _Fragment.showOrHideProgressBar(false);
                //_Fragment.sendRequestInvalidinput();
                _txt_toastMsg.setText("invalid input Please check it..!!");
                //Toast.makeText(_Fragment.getActivity(),"invalid input Please check it..!!",Toast.LENGTH_SHORT).show();


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
