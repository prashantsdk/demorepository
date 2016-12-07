package com.blueplanet.smartcookieteacher.ui.controllers;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.AfterLoginActivity;
import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.BalancePointModelClass;
import com.blueplanet.smartcookieteacher.models.LoginDetailModel;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.models.TestPro;
import com.blueplanet.smartcookieteacher.models.TestProduction;
import com.blueplanet.smartcookieteacher.models.User;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.GPSTracker;
import com.blueplanet.smartcookieteacher.ui.LoginFragment;
import com.blueplanet.smartcookieteacher.ui.RegistrationActivity;
import com.blueplanet.smartcookieteacher.ui.RegistrationFragment;
import com.blueplanet.smartcookieteacher.utils.SmartCookieSharedPreferences;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;


/**
 * Created by 1311 on 24-11-2015.
 */
public class LoginFragmentController implements OnClickListener, IEventListener, AdapterView.OnItemSelectedListener {

    private LoginFragment _loginFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private EditText etxtpoints;
    private ImageView imgclearpoints;
    private Spinner spinner, spinnerPhone;
    GPSTracker gps;

    /**
     * constructor
     *
     * @param loginFragment
     * @param view
     */
    public LoginFragmentController(LoginFragment loginFragment, View view) {

        _loginFragment = loginFragment;
        _view = view;
        etxtpoints = (EditText) _view.findViewById(R.id.etxtpoints);
        imgclearpoints = (ImageView) _view.findViewById(R.id.imgclearpoints);
        spinnerPhone = (Spinner) _view.findViewById(R.id.spinnerPhone);

        // create class object
        gps = new GPSTracker(_loginFragment.getActivity());
        CheckBox cbRememberMe = (CheckBox) _view.findViewById(R.id.cb_remember_me);


        if ((NetworkManager.isNetworkAvailable()) == false) {
            _loginFragment.showNetworkToast(false);
        }
    }

    private void _registerEventListeners() {
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);

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
                        (NotifierFactory.EVENT_NOTIFIER_LOGIN);
        eventNotifier.unRegisterListener(this);

        EventNotifier eventNetwork =
                NotifierFactory.getInstance().getNotifier
                        (NotifierFactory.EVENT_NOTIFIER_NETWORK);
        eventNetwork.unRegisterListener(this);
    }

    private void _teacherLogin(String username, String password, String usertype, String colgCode, String method, String devicetype, String details,
                               String os, String ipadddress, String countryCode) {
        _registerEventListeners();
        _registerNetworkListeners();
        _loginFragment.showOrHideProgressBar(true);
        LoginFeatureController.getInstance().teacherLogin(username, password, usertype, colgCode, method, devicetype, details,
                os, ipadddress, countryCode);
    }

    private void _forgetpassward(String entity, String email) {
        _registerEventListeners();
        _registerNetworkListeners();
        // _loginFragment.showOrHideProgressBar(true);
        LoginFeatureController.getInstance().frogetPassword(entity, email);
    }

    /**
     * function to do clearing tasks
     */
    public void clear() {
        _unRegisterEventListeners();
        if (_loginFragment != null) {
            _loginFragment = null;
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.btn_login:
                StudentFeatureController.getInstance().deletestudentFromDB(null);
                // _handleRememberMeClick();
                // create class object

                _loginFragment.hideSoftKeyboard();
                if (NetworkManager.isNetworkAvailable()) {
                    LoginDetailModel model = new LoginDetailModel();

                    EditText etUserName = (EditText) _view.findViewById(R.id.edt_username);
                    EditText etPassword = (EditText) _view.findViewById(R.id.edt_password);
                    EditText etUserMobile = (EditText) _view.findViewById(R.id.edt_phone);
                    EditText etcolgcode = (EditText) _view.findViewById(R.id.edt_colgCode);
                    EditText etprn = (EditText) _view.findViewById(R.id.edt_Id);
                    // boolean userTyp = LoginFeatureController.getInstance().is_boolenType();
                    String colgCode = "";
                    String countryCode = "";
                    String method = "Android";
                    String devicetype = "phone";
                    String device_details = _loginFragment.getDeviceName();
                    String platform_OS = _loginFragment.getAndroidVersion();
                    String ip_address = _loginFragment.getLocalIpAddress();
                    String usertype = LoginFeatureController.getInstance().get_emailID();
                    String usertphone = LoginFeatureController.getInstance().get_phoneNo();

                    if (usertype.equalsIgnoreCase("Email")) {
                        _handleRememberMeClick();
                        String userName = etUserName.getText().toString();
                        String password = etPassword.getText().toString();


                        //String selStatephone = (String) spinner.getSelectedItem();
                        // LoginFeatureController.getInstance().set_userName(username);
                        //LoginFeatureController.getInstance().set_pasword(password);

                        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(usertype) || !TextUtils.isEmpty(colgCode)) {
                            //  SmartCookieSharedPreferences.setLoginFlag(true);
                            _teacherLogin(userName, password, usertype, colgCode, method, devicetype, device_details, platform_OS, ip_address, countryCode);
                        } else if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(password)) {
                            Toast.makeText(MainApplication.getContext(),
                                    "Please enter your credentials",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else if (usertype.equalsIgnoreCase("Mobile-No")) {
                          _handleRememberMeClickEmp();
                        String mobileno = etUserMobile.getText().toString();
                        String password = etPassword.getText().toString();
                        String code = "91";

                        //String selStatephone = (String) spinner.getSelectedItem();
                        // LoginFeatureController.getInstance().set_userName(username);
                        //LoginFeatureController.getInstance().set_pasword(password);

                        if (!TextUtils.isEmpty(mobileno) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(usertype) || !TextUtils.isEmpty(colgCode)) {
                            //  SmartCookieSharedPreferences.setLoginFlag(true);
                            _teacherLogin(mobileno, password, usertype, colgCode, method, devicetype, device_details, platform_OS, ip_address, code);
                        } else if (TextUtils.isEmpty(mobileno) && TextUtils.isEmpty(password)) {
                            Toast.makeText(MainApplication.getContext(),
                                    "Please enter your credentials",
                                    Toast.LENGTH_SHORT).show();
                        }


                    } else if (usertype.equalsIgnoreCase("EmployeeID")) {
                        _handleRememberMeClickPrn();
                        String code = etcolgcode.getText().toString();
                        String prn = etprn.getText().toString();
                        String password = etPassword.getText().toString();
                        //String selStatephone = (String) spinner.getSelectedItem();
                        // LoginFeatureController.getInstance().set_userName(username);
                        //LoginFeatureController.getInstance().set_pasword(password);

                        if (!TextUtils.isEmpty(prn) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(usertype) || !TextUtils.isEmpty(colgCode)) {
                            //  SmartCookieSharedPreferences.setLoginFlag(true);
                            _teacherLogin(prn, password, usertype, code, method, devicetype, device_details, platform_OS, ip_address, countryCode);
                        } else if (TextUtils.isEmpty(prn) && TextUtils.isEmpty(password)) {
                            Toast.makeText(MainApplication.getContext(),
                                    "Please enter your credentials",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            _loginFragment.showNetworkToast(false);
                        }

                    }
                } else {
                    _loginFragment.showNetworkToast(false);
                }



             /*  if (NetworkManager.isNetworkAvailable()) {
                    EditText etUserName = (EditText) _view.findViewById(R.id.edt_username);
                    EditText etPassword = (EditText) _view.findViewById(R.id.edt_password);
                   // boolean userTyp = LoginFeatureController.getInstance().is_boolenType();
                    String colgCode="";
                    String usertype=LoginFeatureController.getInstance().get_emailID();



                    String userName = etUserName.getText().toString();
                    String password = etPassword.getText().toString();
                    //String selStatephone = (String) spinner.getSelectedItem();

                    if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)&& !TextUtils.isEmpty(usertype)|| !TextUtils.isEmpty(colgCode)) {
                      //  SmartCookieSharedPreferences.setLoginFlag(true);
                        _teacherLogin(userName, password,usertype,colgCode);
                    } else if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(password)) {
                        Toast.makeText(MainApplication.getContext(),
                                "Please enter your credentials",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    _loginFragment.showNetworkToast(false);
                }*/
                break;

           /* case R.id.btn_signup:
                _loadFragment(R.id.fragment_layout, new RegistrationFragment());
                break;*/
            case R.id.txtproduction:
                //  _loginFragment.showTestProduction(true);

                TestPro testpro = new TestPro();
                TestProduction tp = new TestProduction();
                WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL;
                WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL2;
                //WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL2;
                testpro.set_url(WebserviceConstants.BASE_URL);
                String a = testpro.get_url();
                Toast.makeText(_loginFragment.getActivity(), "Production",
                        Toast.LENGTH_LONG).show();
                tp.set_production("Production");
                Log.i(_TAG, "Value");


                break;

            case R.id.txttest:
                // _loginFragment.showTestProduction(false);
                TestPro testpro1 = new TestPro();
                WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL1;
                testpro1.set_url(WebserviceConstants.BASE_URL);
                String a1 = testpro1.get_url();
                Toast.makeText(_loginFragment.getActivity(), "Testing",
                        Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_forgotPassword:
                EditText etUserName = (EditText) _view.findViewById(R.id.edt_username);
                String userName = etUserName.getText().toString();
                String entity = "2";

                if (!TextUtils.isEmpty(userName)) {
                    _forgetpassward(entity, userName);
                    Toast.makeText(MainApplication.getContext(),
                            "Please check email",
                            Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(MainApplication.getContext(),
                            "Please enter your passward",
                            Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.etxtpoints:
                Log.i(_TAG, "ON clicked");
                final CharSequence[] items = {"Email-Id", "Phone no", "Employe Id", "Facebook", "linkedin",
                        "google plus"};

                _showData(items, "Select Points", null, null);
                break;
            case R.id.imgclearpoints:
                etxtpoints.setText("");
                imgclearpoints.setVisibility(View.GONE);
                break;

            case R.id.btnRegis:
                Intent intent = new Intent(_loginFragment.getActivity(), RegistrationActivity.class);
                _loginFragment.startActivity(intent);
                _loginFragment.getActivity().finish();
                break;

            default:
                break;
        }

    }


    void _showData(final CharSequence[] items, final String msg,
                   final EditText txt, final TextView lbl) {
        _loginFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                AlertDialog.Builder builder3 = new AlertDialog.Builder(
                        _loginFragment.getActivity());

                builder3.setTitle(msg).setItems(items,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (lbl == null) {
                                    String Alert = items[which].toString();
                                    // Toast.makeText(getActivity(), Alert,
                                    // Toast.LENGTH_SHORT).show();

                                    etxtpoints.setText(Alert);
                                    BalancePointModelClass.set_couValue(Alert);
                                    Log.i(_TAG, "point" + BalancePointModelClass.get_couValue());

                                    imgclearpoints.setVisibility(View.VISIBLE);

                                } else {
                                    String Alert = items[which].toString();
                                    // Toast.makeText(getActivity(), Alert,
                                    // Toast.LENGTH_SHORT).show();

                                    etxtpoints.setText(Alert);
                                    imgclearpoints.setVisibility(View.VISIBLE);
                                }
                            }

                        });
                builder3.show();
            }
        });

    }

    private void _loadFragment(int id, Fragment fragment) {

        FragmentManager fm = _loginFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack("LoginFragment");
        ft.commitAllowingStateLoss();
    }

    private void _startAfterLoginActivity() {
        Intent intent = new Intent(_loginFragment.getActivity(),
                AfterLoginActivity.class);
        _loginFragment.startActivity(intent);
        _loginFragment.getActivity().finish();
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
            case EventTypes.EVENT_UI_LOGIN_SUCCESSFUL:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_LOGIN);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In EVENT_UI_LOGIN_SUCCESSFUL");
                    //SmartCookieSharedPreferences.setLoginFlag(true);
                    _loginFragment.showOrHideProgressBar(false);
                    _startAfterLoginActivity();
                    SaveLoginData();
                }
                break;
            case EventTypes.EVENT_UI_NO_LOGIN_RESPONSE:
                EventNotifier eventNotifier1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_LOGIN);
                eventNotifier1.unRegisterListener(this);

                Log.i("LoginFragmentController", "IN EVENT_UI_NO_LOGIN_RESPONSE");
                _loginFragment.showOrHideProgressBar(false);
                _loginFragment.showLoginErrorMessage();
                break;
            case EventTypes.EVENT_CONFLCTLOGIN_RESPONSE:
                EventNotifier eventNotifier2 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_LOGIN);
                eventNotifier2.unRegisterListener(this);

                Log.i("LoginFragmentController", "IN EVENT_UI_NO_LOGIN_RESPONSE");
                _loginFragment.showOrHideProgressBar(false);
                _loginFragment.showLoginErrorMessage();
                break;
            case EventTypes.EVENT_UI_FORGET_PEASSWARD:
                EventNotifier eventNotifier3 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_LOGIN);
                eventNotifier3.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    Log.i(_TAG, "In EVENT_UI_FORGET_PASS");
                    // _loginFragment.showOrHideProgressBar(true);

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

                _loginFragment.showNetworkToast(false);
                break;
            case EventTypes.EVENT_UI_REGISTRATION_CONFLICT:

                EventNotifier event2 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event2.unRegisterListener(this);

                _loginFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainApplication.getContext(),
                                "Please check userid and passward..!!",
                                Toast.LENGTH_LONG).show();
                    }
                });
                break;

            default:
                eventState = EventState.EVENT_IGNORED;
                break;


        }

        return EventState.EVENT_PROCESSED;


    }

    private void SaveLoginData() {
        Teacher teach = LoginFeatureController.getInstance().getTeacher();
        LoginFeatureController.getInstance().saveUserDataIntoDB(teach);
    }


    private void _handleRememberMeClick() {

        EditText etUserName = (EditText) _view.findViewById(R.id.edt_username);
        EditText etPassword = (EditText) _view.findViewById(R.id.edt_password);
        CheckBox cbRememberMe = (CheckBox) _view.findViewById(R.id.cb_remember_me);

        String userName = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        String prn="";
        if (cbRememberMe.isChecked()) {
            Log.i(_TAG, "In remember me checked");

            SmartCookieSharedPreferences.setRememberMeFlag(true);
            SmartCookieSharedPreferences.setUserName(userName);
            SmartCookieSharedPreferences.setPassowrdKey(password);
            SmartCookieSharedPreferences.setLoginFlag(true);
            LoginFeatureController.getInstance().deleteUserFromDB(null);

            /**
             * save user data into DB
             */
            User user = new User(userName, password, true,prn);
            LoginFeatureController.getInstance().saveUserDataIntoDB(user);
        } else {

            Log.i(_TAG, "In remember me un-checked");
            SmartCookieSharedPreferences.setRememberMeFlag(false);
            SmartCookieSharedPreferences.setUserName("");
            SmartCookieSharedPreferences.setPassowrdKey("");

            /**
             * delete user data into DB
             */
            LoginFeatureController.getInstance().deleteUserFromDB(userName);
        }

    }

    private void _handleRememberMeClickEmp() {
/*
        EditText etUserName = (EditText) _view.findViewById(R.id.edt_username);
        EditText etPassword = (EditText) _view.findViewById(R.id.edt_password);*/
        CheckBox cbRememberMe = (CheckBox) _view.findViewById(R.id.cb_remember_me);

        EditText etPassword = (EditText) _view.findViewById(R.id.edt_password);
        EditText etUserMobile = (EditText) _view.findViewById(R.id.edt_phone);
       /* String userName = etUserName.getText().toString();
        String password = etPassword.getText().toString();*/

        String mobileno = etUserMobile.getText().toString();
        String password = etPassword.getText().toString();
        String code = "91";
        String prn="";

        if (cbRememberMe.isChecked()) {
            Log.i(_TAG, "In remember me checked");
            SmartCookieSharedPreferences.setRememberMeFlag(true);
            SmartCookieSharedPreferences.setUserName(mobileno);
            SmartCookieSharedPreferences.setPassowrdKey(password);
            SmartCookieSharedPreferences.setLoginFlag(true);
            LoginFeatureController.getInstance().deleteUserFromDB(null);
            /**
             * save user data into DB
             */
            User user = new User(mobileno, password, true,prn);
            LoginFeatureController.getInstance().saveUserDataIntoDB(user);
        } else {

            Log.i(_TAG, "In remember me un-checked");
            SmartCookieSharedPreferences.setRememberMeFlag(false);
            SmartCookieSharedPreferences.setUserName("");
            SmartCookieSharedPreferences.setPassowrdKey("");

            /**
             * delete user data into DB
             */
            LoginFeatureController.getInstance().deleteUserFromDB(mobileno);
        }

    }
    private void _handleRememberMeClickPrn() {
/*
        EditText etUserName = (EditText) _view.findViewById(R.id.edt_username);
        EditText etPassword = (EditText) _view.findViewById(R.id.edt_password);*/
        CheckBox cbRememberMe = (CheckBox) _view.findViewById(R.id.cb_remember_me);

        EditText etPassword = (EditText) _view.findViewById(R.id.edt_password);

        EditText etcolgcode = (EditText) _view.findViewById(R.id.edt_colgCode);
        EditText etprn = (EditText) _view.findViewById(R.id.edt_Id);

        String code = etcolgcode.getText().toString();
        String prn = etprn.getText().toString();
        String password = etPassword.getText().toString();

        if (cbRememberMe.isChecked()) {
            Log.i(_TAG, "In remember me checked");
            SmartCookieSharedPreferences.setRememberMeFlag(true);
            SmartCookieSharedPreferences.setUserName(prn);
            SmartCookieSharedPreferences.setPRNKey(code);
            SmartCookieSharedPreferences.setPassowrdKey(password);


            SmartCookieSharedPreferences.setLoginFlag(true);
            LoginFeatureController.getInstance().deleteUserFromDB(null);
            /**
             * save user data into DB
             */
            User user = new User(code, prn, true,password);
            LoginFeatureController.getInstance().saveUserDataIntoDB(user);
        } else {

            Log.i(_TAG, "In remember me un-checked");
            SmartCookieSharedPreferences.setRememberMeFlag(false);
            SmartCookieSharedPreferences.setUserName("");
            SmartCookieSharedPreferences.setPassowrdKey("");

            /**
             * delete user data into DB
             */
            LoginFeatureController.getInstance().deleteUserFromDB(prn);
        }

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        _loginFragment.showType(position);
        //_loginFragment.showTypePhone(position);
        // _loginFragment.showTypePhone(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}

