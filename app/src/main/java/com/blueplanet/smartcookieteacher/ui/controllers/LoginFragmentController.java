package com.blueplanet.smartcookieteacher.ui.controllers;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.AfterLoginActivity;
import com.blueplanet.smartcookieteacher.GlobalInterface;
import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.ErrorFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.UpdateGCMFeatureController;
import com.blueplanet.smartcookieteacher.gcm.GCMPreferences;
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
import com.blueplanet.smartcookieteacher.utils.SmartCookieSharedPreferences;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


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
    private String password;
    GPSTracker gpsTracker;
    private Teacher _teacher;
    private String _teacherId, _schoolId;
    public CallbackManager callbackManager = CallbackManager.Factory.create();
    public static final int PERMISSION_REQUEST_CODE = 23;
    String social_email = "", social_id = "", social_f_name = "", social_l_name = "", str_social_profile_pic = "";
    double latitude = 0.0, longitude = 0.0;

    String[] LOC_PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    GoogleCloudMessaging gcm;
    String[] social_name;
    URL social_profile_pic = null;

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
        if (checkPermission()) {
            gpsTracker = new GPSTracker(_loginFragment.getActivity());
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();


        } else {

            requestPermission();
        }

        CheckBox cbRememberMe = (CheckBox) _view.findViewById(R.id.cb_remember_me);


       /* if (checkPlayServices()) {
            registerInBackground();
        }*/


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
                               String os, String ipadddress, String countryCode, double lat, double log) {
        _registerEventListeners();
        _registerNetworkListeners();
        _loginFragment.showOrHideProgressBar(true);
        LoginFeatureController.getInstance().teacherLogin(username, password, usertype, colgCode, method, devicetype, details,
                os, ipadddress, countryCode, lat, log);
    }

    private void _ErrorWev(String t_id, String studentId, String type, String description, String date, String datetime, String usertype, String name, String phone, String email,
                           String appname, String subroutinename, String line, String status, String webmethodname, String webservice, String proname) {
        _registerEventListeners();
        _registerNetworkListeners();
        _loginFragment.showOrHideProgressBar(true);
        ErrorFeatureController.getInstance().getErrorListFromServer(t_id, studentId, type, description, date, datetime, usertype, name, phone, email,
                appname, subroutinename, line, status, webmethodname, webservice, proname);
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
                //_handleRememberMeClick();
                // create class object

                _loginFragment.hideSoftKeyboard();
                if (NetworkManager.isNetworkAvailable()) {
                    LoginDetailModel model = new LoginDetailModel();

                    EditText etUserName = (EditText) _view.findViewById(R.id.edt_username_login);
                    EditText etPassword = (EditText) _view.findViewById(R.id.edt_password);
                    EditText etUserMobile = (EditText) _view.findViewById(R.id.edt_phone);
                    EditText etcolgcode = (EditText) _view.findViewById(R.id.edt_colgCode);
                    EditText etprn = (EditText) _view.findViewById(R.id.edt_Id);
                    EditText etMemberId = (EditText) _view.findViewById(R.id.edt_memberid);
                    // boolean userTyp = LoginFeatureController.getInstance().is_boolenType();
                    String colgCode = "";
                    String countryCode = "";
                    String method = "Android";
                    LoginFeatureController.getInstance().setMethod(method);
                    String devicetype = "phone";
                    LoginFeatureController.getInstance().setDevicetype(devicetype);


                    String device_details = _loginFragment.getDeviceName();
                    LoginFeatureController.getInstance().setDevicedetail(device_details);

                    String platform_OS = _loginFragment.getAndroidVersion();
                    LoginFeatureController.getInstance().setPlatfom(platform_OS);
                    String ip_address = _loginFragment.getLocalIpAddress();
                    LoginFeatureController.getInstance().setIp(ip_address);
                    String usertype = LoginFeatureController.getInstance().get_emailID();
                    String usertphone = LoginFeatureController.getInstance().get_phoneNo();


                    if (usertype != null) {
                        if (!usertype.equals("Select Login Type")) {


                            if (usertype.equalsIgnoreCase("Email")) {
                                _handleRememberMeEmail();
                                String userName = etUserName.getText().toString();
                                LoginFeatureController.getInstance().setEmail(userName);

                                password = etPassword.getText().toString();
                                LoginFeatureController.getInstance().setPassword(password);
                                String collgcode = etprn.getText().toString();
                                LoginFeatureController.getInstance().setColgcode(collgcode);
                     /*   SmartCookieSharedPreferences.setUserNameInSharedPreference(userName);
                        SmartCookieSharedPreferences.setPasswordInSharedPreference(password);
                        SmartCookieSharedPreferences.setUserIDInSharedPreference(collgcode);*/


                                //String selStatephone = (String) spinner.getSelectedItem();
                                // LoginFeatureController.getInstance().set_userName(username);
                                //LoginFeatureController.getInstance().set_pasword(password);

                                if ((!TextUtils.isEmpty(userName)) && (!TextUtils.isEmpty(password)) && (!TextUtils.isEmpty(usertype)) && (!TextUtils.isEmpty(collgcode))) {
                                    //  SmartCookieSharedPreferences.setLoginFlag(true);

                                    _teacherLogin(userName, password, usertype, collgcode, method, devicetype, device_details, platform_OS, ip_address, countryCode, latitude, longitude);
                                } else {
                                    Toast.makeText(MainApplication.getContext(),
                                            "Please enter your all login credentials",
                                            Toast.LENGTH_SHORT).show();
                          /*  SmartCookieSharedPreferences.setUserNameInSharedPreference("");
                            SmartCookieSharedPreferences.setPasswordInSharedPreference("");
                            SmartCookieSharedPreferences.setUserIDInSharedPreference("");*/
                                }


                            } else if (usertype.equalsIgnoreCase("Mobile-No")) {
                                _handleRememberMeMobileNo();
                                password = "";
                                String mobileno = etUserMobile.getText().toString();
                                String password = etPassword.getText().toString();
                                String code = "91";
                                String collgcode = etprn.getText().toString();

                                //String selStatephone = (String) spinner.getSelectedItem();
                                // LoginFeatureController.getInstance().set_userName(username);
                                //LoginFeatureController.getInstance().set_pasword(password);

                     /*   if (!TextUtils.isEmpty(mobileno) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(usertype) || !TextUtils.isEmpty(colgCode) && mobileno.equalsIgnoreCase("0" ) ){
                            //  SmartCookieSharedPreferences.setLoginFlag(true);
                            _teacherLogin(mobileno, password, usertype, colgCode, method, devicetype, device_details, platform_OS, ip_address, code);
                        } else if (TextUtils.isEmpty(mobileno) && TextUtils.isEmpty(password)) {
                            Toast.makeText(MainApplication.getContext(),
                                    "Please enter your credentials",
                                    Toast.LENGTH_SHORT).show();
                        }*/

                                if ((!TextUtils.isEmpty(mobileno)) && (!TextUtils.isEmpty(password)) && (!TextUtils.isEmpty(usertype)) && (!TextUtils.isEmpty(collgcode)) && (!mobileno.equals("0"))) {
                                    //  SmartCookieSharedPreferences.setLoginFlag(true);

                                    _teacherLogin(mobileno, password, usertype, collgcode, method, devicetype, device_details, platform_OS, ip_address, code, latitude, longitude);

                                } else
                                    Toast.makeText(MainApplication.getContext(),
                                            "Please enter your all login credentials",
                                            Toast.LENGTH_SHORT).show();

                                //_teacherLogin(mobileno, password, usertype, colgCode, method, devicetype, device_details, platform_OS, ip_address, code, latitude, longitude);


                            }
                            if (usertype.equalsIgnoreCase("EmployeeID")) {
                                _handleRememberMeClickPrn();
                                String code = etcolgcode.getText().toString();
                                String prn = etprn.getText().toString();
                                String password = etPassword.getText().toString();
                                //String selStatephone = (String) spinner.getSelectedItem();
                                // LoginFeatureController.getInstance().set_userName(username);
                                //LoginFeatureController.getInstance().set_pasword(password);

                                if ((!TextUtils.isEmpty(prn)) && (!TextUtils.isEmpty(password)) && (!TextUtils.isEmpty(usertype)) && (!TextUtils.isEmpty(code))) {
                                    //  SmartCookieSharedPreferences.setLoginFlag(true);

                                    _teacherLogin(code, password, usertype, prn, method, devicetype, device_details, platform_OS, ip_address, countryCode, latitude, longitude);
                                } else {
                                    Toast.makeText(MainApplication.getContext(),
                                            "Please enter your all login credentials",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                            if (usertype.equalsIgnoreCase("MemberID")) {
                                _handleRememberMeMemberID();
                                String userMemberID = etMemberId.getText().toString();
                                password = etPassword.getText().toString();
                                String collgcode = etprn.getText().toString();

                                //String selStatephone = (String) spinner.getSelectedItem();
                                // LoginFeatureController.getInstance().set_userName(username);
                                //LoginFeatureController.getInstance().set_pasword(password);

                                if ((!TextUtils.isEmpty(userMemberID)) && (!TextUtils.isEmpty(collgcode)) && (!TextUtils.isEmpty(password)) && (!TextUtils.isEmpty(usertype))) {
                                    //  SmartCookieSharedPreferences.setLoginFlag(true);

                                    _teacherLogin(userMemberID, password, usertype, collgcode, method, devicetype, device_details, platform_OS, ip_address, countryCode, latitude, longitude);
                                } else {

                                    Toast.makeText(MainApplication.getContext(),
                                            "Please enter your all login credentials",
                                            Toast.LENGTH_SHORT).show();

                                }


                            }

                        } else {
                            _loginFragment.selectLoginType();
                        }
                    } else {
                        _loginFragment.selectLoginType();
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
                SmartCookieSharedPreferences.setLoginType("1");

                //WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL2;
                testpro.set_url(WebserviceConstants.BASE_URL);
                testpro.setImageURL(WebserviceConstants.IMAGE_BASE_URL_PRODUCTION);
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
                SmartCookieSharedPreferences.setLoginType("2");
                testpro1.setImageURL(WebserviceConstants.IMAGE_BASE_URL_TEST);
                String a1 = testpro1.get_url();
                Toast.makeText(_loginFragment.getActivity(), "Testing",
                        Toast.LENGTH_LONG).show();


                break;


            case R.id.txtDev:
                TestPro testpro2 = new TestPro();
                WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL3;
                testpro2.set_url(WebserviceConstants.BASE_URL);
                SmartCookieSharedPreferences.setLoginType("3");
                String a2 = testpro2.get_url();
                Toast.makeText(_loginFragment.getActivity(), "Dev",
                        Toast.LENGTH_LONG).show();
                break;


            case R.id.tv_forgotPassword:
                EditText etUserName = (EditText) _view.findViewById(R.id.edt_username_login);
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
                // _loadFragment(R.id.fragment_layout, new RegistrationFragment());
                Intent intent = new Intent(_loginFragment.getActivity(), RegistrationActivity.class);
                _loginFragment.startActivity(intent);
                _loginFragment.getActivity().finish();
                break;

            case R.id.btnfacebook:
                AppRater.showRateDialog(_loginFragment.getActivity(), null);

              /*  new AppRate(_loginFragment.getActivity()).init();
                new AppRate(_loginFragment.getActivity())
                        .setMinDaysUntilPrompt(7)
                        .setMinLaunchesUntilPrompt(20)
                        .init();
                rateApp(MainApplication.getContext());*/
            /*    try {
                    _loginFragment.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + packageName)));
                } catch (android.content.ActivityNotFoundException e) {
                    _loginFragment.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
                }*/


                // Facebook_SignIn();
                break;
            default:
                break;
        }

    }

    public static void rateApp(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            viewInBrowser(context, "https://play.google.com/store/apps/details?id=" + context.getPackageName());
        }
    }

    public static void viewInBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (null != intent.resolveActivity(context.getPackageManager())) {
            context.startActivity(intent);
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
                    // SmartCookieSharedPreferences.setLoginFlag(true);

                    SaveLoginData();
                    _loginFragment.showOrHideProgressBar(false);
                    Teacher teacher = LoginFeatureController.getInstance().getTeacher();
                    //  _startAfterLoginActivity();
                    if (SmartCookieSharedPreferences.getDeviceRegisteredOnServer() == true) {
                        registerGCMtoServer(String.valueOf(teacher.getId()), SmartCookieSharedPreferences.getGCMSharedPreference(GlobalInterface.KEY_GCM));
                    } else {
                        _startAfterLoginActivity();
                    }


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
                _teacher = LoginFeatureController.getInstance().getTeacher();


                break;

            case EventTypes.EVENT_UI_GCM_RESPONCE_RECIEVED:
                EventNotifier eventNotifier6 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
                eventNotifier6.unRegisterListener(this);
                _startAfterLoginActivity();
                _loginFragment.showOrHideProgressBar(false);

                break;

            case EventTypes.EVENT_UI_NO_GCM_RESPONCE_RECIEVED:
                EventNotifier eventNotifier7 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
                eventNotifier7.unRegisterListener(this);
                _loginFragment.showOrHideProgressBar(false);
                _loginFragment.showGCMsmassage(false);
                break;
            case EventTypes.EVENT_CONFLCTLOGIN_RESPONSE:
                EventNotifier eventNotifier2 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_LOGIN);
                eventNotifier2.unRegisterListener(this);

                Log.i("LoginFragmentController", "IN EVENT_UI_NO_LOGIN_RESPONSE");


                _loginFragment.showOrHideProgressBar(false);

                //_loginFragment.showLoginErrorMessage();
                _loginFragment.showLoginConflictError();
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


    private void _handleRememberMeEmail() {

        EditText etUserName = (EditText) _view.findViewById(R.id.edt_username_login);
        EditText etPassword = (EditText) _view.findViewById(R.id.edt_password);
        EditText etPrnNo = _view.findViewById(R.id.edt_Id);
        CheckBox cbRememberMe = (CheckBox) _view.findViewById(R.id.cb_remember_me);

        String userName = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        String prn = etPrnNo.getText().toString();


        if (cbRememberMe.isChecked()) {

            Log.i(_TAG, "In remember me checked");

         /*   SmartCookieSharedPreferences.setRememberMeFlag(true);
            SmartCookieSharedPreferences.setUserName(userName);
            SmartCookieSharedPreferences.setPassowrdKey(password);
            SmartCookieSharedPreferences.setPRNKey(prn);
            */

            SmartCookieSharedPreferences.setEmailRememberMe(true);
            SmartCookieSharedPreferences.setEmailID(userName);
            SmartCookieSharedPreferences.setEmailPassword(password);
            SmartCookieSharedPreferences.setEmailPrn(prn);

            SmartCookieSharedPreferences.setLoginFlag(true);

            LoginFeatureController.getInstance().deleteUserFromDB(null);

            /**
             * save user data into DB
             */
            User user = new User(userName, password, "true", prn, "1", "2");
            LoginFeatureController.getInstance().saveUserDataIntoDB(user);
        } else {

            Log.i(_TAG, "In remember me un-checked");
            SmartCookieSharedPreferences.setRememberMeFlag(false);


            SmartCookieSharedPreferences.setEmailRememberMe(false);

            SmartCookieSharedPreferences.setUserName("");
            SmartCookieSharedPreferences.setPassowrdKey("");

            /**
             * delete user data into DB
             */
            LoginFeatureController.getInstance().deleteUserFromDB(userName);
        }

    }

    private void _handleRememberMeMobileNo() {

/*
        EditText etUserName = (EditText) _view.findViewById(R.id.edt_username);
        EditText etPassword = (EditText) _view.findViewById(R.id.edt_password);*/

        CheckBox cbRememberMe = (CheckBox) _view.findViewById(R.id.cb_remember_me);
        EditText etPassword = (EditText) _view.findViewById(R.id.edt_password);
        EditText etPrnNo = _view.findViewById(R.id.edt_Id);
        EditText etUserMobile = (EditText) _view.findViewById(R.id.edt_phone);

       /* String userName = etUserName.getText().toString();
        String password = etPassword.getText().toString();*/

        String mobileno = etUserMobile.getText().toString();
        String password = etPassword.getText().toString();
        String prn = etPrnNo.getText().toString();


        String code = "91";


        if (cbRememberMe.isChecked()) {

          /*  Log.i(_TAG, "In remember me checked");
            SmartCookieSharedPreferences.setRememberMeFlag(true);
            SmartCookieSharedPreferences.setUserName(mobileno);
            SmartCookieSharedPreferences.setPassowrdKey(password);
*/

            SmartCookieSharedPreferences.setMobileRemberMe("true");
            SmartCookieSharedPreferences.setMobileNo(mobileno);
            SmartCookieSharedPreferences.setMobilePrn(prn);
            SmartCookieSharedPreferences.setMobilePassword(password);

            SmartCookieSharedPreferences.setLoginFlag(true);

            LoginFeatureController.getInstance().deleteUserFromDB(null);
            /**
             * save user data into DB
             */
            User user = new User(mobileno, password, "true", prn, "1");
            LoginFeatureController.getInstance().saveUserDataIntoDB(user);
        } else {

            Log.i(_TAG, "In remember me un-checked");
            SmartCookieSharedPreferences.setRememberMeFlag(false);

            SmartCookieSharedPreferences.setMobileRemberMe("false");

            SmartCookieSharedPreferences.setUserName("");
            SmartCookieSharedPreferences.setPassowrdKey("");

            /**
             * delete user data into DB
             */
            LoginFeatureController.getInstance().deleteUserFromDB(mobileno);
        }

    }

    private void _handleRememberMeClickPrn() {

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
            SmartCookieSharedPreferences.setUserName(code);
            SmartCookieSharedPreferences.setPRNKey(prn);
            SmartCookieSharedPreferences.setPassowrdKey(password);

            SmartCookieSharedPreferences.setLoginFlag(true);

            LoginFeatureController.getInstance().deleteUserFromDB(null);


            /**
             * save user data into DB
             */


            User user = new User(code, password, "true", prn);
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

    private void _handleRememberMeMemberID() {

        CheckBox cbRememberMe = (CheckBox) _view.findViewById(R.id.cb_remember_me);

        EditText etPassword = (EditText) _view.findViewById(R.id.edt_password);
        EditText etcolgcode = (EditText) _view.findViewById(R.id.edt_memberid);
        EditText etprn = (EditText) _view.findViewById(R.id.edt_Id);

        String code = etcolgcode.getText().toString();
        String prn = etprn.getText().toString();
        String password = etPassword.getText().toString();

        if (cbRememberMe.isChecked()) {

         /*   Log.i(_TAG, "In remember me checked");
            SmartCookieSharedPreferences.setRememberMeFlag(true);
            SmartCookieSharedPreferences.setUserName(code);
            SmartCookieSharedPreferences.setPRNKey(prn);
            SmartCookieSharedPreferences.setPassowrdKey(password);*/


            SmartCookieSharedPreferences.setMemberIDRemberMe("true");
            SmartCookieSharedPreferences.setMemberIdMemberId(code);
            SmartCookieSharedPreferences.setMemberIdPrn(prn);
            SmartCookieSharedPreferences.setMemberIdPassword(password);

            SmartCookieSharedPreferences.setLoginFlag(true);

            LoginFeatureController.getInstance().deleteUserFromDB(null);
            /**
             * save user data into DB
             */
            User user = new User(code, password, "true", prn, "1", "2", "3");
            LoginFeatureController.getInstance().saveUserDataIntoDB(user);
        } else {

            Log.i(_TAG, "In remember me un-checked");
            SmartCookieSharedPreferences.setRememberMeFlag(false);

            SmartCookieSharedPreferences.setMemberIDRemberMe("false");
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

        // if (position  0) {
        _loginFragment.showType(position);
        _loginFragment._isRememberMeClicked(position);

        // }
        //_loginFragment.showTypePhone(position);
        // _loginFragment.showTypePhone(position);

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void registerGCMtoServer(String stud_id, String Gcm_id) {
        _registerEventListeners();
        _registerNetworkListeners();
        UpdateGCMFeatureController.getInstance().registerGCMOnServer(stud_id, Gcm_id);
        _loginFragment.showOrHideProgressBar(true);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(_loginFragment.getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(_loginFragment.getActivity(), resultCode, GCMPreferences.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(_TAG, "This device is not supported.");
                _loginFragment.getActivity().finish();
            }
            return false;
        }
        return true;
    }

    private void Facebook_SignIn() {
        LoginManager.getInstance().logInWithReadPermissions(_loginFragment, Arrays.asList("email"));

        LoginManager.getInstance().registerCallback(_loginFragment.callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                SmartCookieSharedPreferences.setFbLogin(true);
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        // Bundle bFacebookData = getFacebookData(object);

                        if (object.has("email")) {
                            social_email = object.optString("email");
                        }
                        if (object.has("id")) {
                            social_id = object.optString("id");
                        }
                        if (object.has("name")) {
                            String fb_name = object.optString("name");
                            social_name = fb_name.split(" ");

                            try {
                                social_f_name = social_name[0].trim();
                                social_l_name = social_name[1].trim();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                        try {
                            social_profile_pic = new URL("https://graph.facebook.com/" + social_id + "/picture?type=small");
                            Log.i("profile_pic", social_profile_pic + "");
                            str_social_profile_pic = social_profile_pic.toString();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                      /*  if (!social_email.equals("")) {
                            getMoreDetails();
                            getSociallogindataFromServer(WebserviceConstants.VAL_SOCIAL_LG_FB_LOGIN, social_f_name, social_l_name, social_email, social_id, "", "",
                                    str_social_profile_pic, "", countrycode, method, devicetype, devicedetails, platformos,
                                    ipaddress, String.valueOf(lattitude), String.valueOf(longitude));
                        } else {
                            HelperClass.OpenAlertDialog("Please Try to login facebook with your email Id.", _loginFragment.getActivity());
                            Fb_LogOut();
                        }*/


                    }

                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender"); // Parámetros que pedimos a facebook

                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }


    public void handleSignInResult(GoogleSignInResult result) {
        Log.d("TAG", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            SmartCookieSharedPreferences.setGplusLogin(true);
            GoogleSignInAccount acct = result.getSignInAccount();


            String strgmail_id = "";

            try {
                social_email = acct.getEmail().toString();
                String name = acct.getDisplayName().toString();
                social_name = name.split(" ");
                social_f_name = social_name[0].trim();
                social_l_name = social_name[1].trim();
                strgmail_id = acct.getId().toString();
                Uri s1 = acct.getPhotoUrl();
                //String s=s1;
                social_profile_pic = new URL(s1.toString());
                Log.i("profile_pic", social_profile_pic + "");
                str_social_profile_pic = social_profile_pic.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                SmartCookieSharedPreferences.setGplusLogin(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

           /* getMoreDetails();
            getSociallogindataFromServer(WebserviceConstants.VAL_SOCIAL_LG_GPLUS_LOGIN, social_f_name, social_l_name, social_email, "", strgmail_id, "",
                    str_social_profile_pic, "", countrycode, method, devicetype, devicedetails, platformos,
                    ipaddress, String.valueOf(lattitude), String.valueOf(longitude));*/

        } else {
            // Signed out, show unauthenticated UI.
            SmartCookieSharedPreferences.setGplusLogin(false);
        }
    }

    public void Fb_LogOut() {

        LoginManager.getInstance().logOut();
    }

    private void registerInBackground() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                String msg = "";
                try {
                    if (gcm == null) {

                       /* if(_loginFragment.getActivity() != null) {


                            gcm = GoogleCloudMessaging.getInstance(_loginFragment.getActivity());
                        }*/
                    }

                    String Gcm_Id = "";
                    Gcm_Id = gcm.register(GCMPreferences.GOOGLE_SENDER_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + Gcm_Id);
                    msg = "Device registered, registration ID=" + Gcm_Id;

                    //    APA91bHU-Y5iGxs1K9a94p8Ygn0sqiZm4KTQwmnQZj26OoLJYXTFxHBFDeWDfrkz61kDZ5Y-hRA8CAV3hzaCK5JHqbsjCfzVR9KHGO98837XLRurAdOYARJXNwRsnwZ4KhXBPZCb2xaZ
                    SmartCookieSharedPreferences.setGCMSharedPreference(WebserviceConstants.KEY_GCM, Gcm_Id);
                    SmartCookieSharedPreferences.setDeviceRegisteredOnServer(true);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                    SmartCookieSharedPreferences.setDeviceRegisteredOnServer(false);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);


                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                /*Toast.makeText(getApplicationContext(),
                        "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                        .show();*/
            }
        }.execute(null, null, null);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(_loginFragment.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }


    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(_loginFragment.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(_loginFragment.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {

            Toast.makeText(_loginFragment.getActivity(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {
            ActivityCompat.requestPermissions(_loginFragment.getActivity(), LOC_PERMISSIONS, PERMISSION_REQUEST_CODE);
        }
    }

    public static class AppRater {
        private final static String APP_TITLE = "Smart Teacher";
        private final static String APP_PNAME = "com.blueplanet.smartcookieteacher";

        private final static int DAYS_UNTIL_PROMPT = 3;
        private final static int LAUNCHES_UNTIL_PROMPT = 7;

        public void app_launched(Context mContext) {
            SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
            if (prefs.getBoolean("dontshowagain", false)) {
                return;
            }

            SharedPreferences.Editor editor = prefs.edit();

            // Increment launch counter
            long launch_count = prefs.getLong("launch_count", 0) + 1;
            editor.putLong("launch_count", launch_count);

            // Get date of first launch
            Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
            if (date_firstLaunch == 0) {
                date_firstLaunch = System.currentTimeMillis();
                editor.putLong("date_firstlaunch", date_firstLaunch);
            }

            // Wait at least n days before opening dialog
            if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
                if (System.currentTimeMillis() >= date_firstLaunch +
                        (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                    showRateDialog(mContext, editor);
                }
            }

            editor.commit();
        }

        public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
            final Dialog dialog = new Dialog(mContext);
            dialog.setTitle("Rate " + APP_TITLE);

            LinearLayout ll = new LinearLayout(mContext);
            ll.setOrientation(LinearLayout.VERTICAL);


            TextView tv = new TextView(mContext);
            tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
            tv.setWidth(840);
            ll.setBackgroundResource(R.color.colorPrimaryDark_transparent);
            tv.setPadding(1, 0, 1, 5);
            ll.addView(tv);

            Button b1 = new Button(mContext);
            b1.setText("Rate " + APP_TITLE);
            b1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                    dialog.dismiss();
                }
            });
            ll.addView(b1);

            Button b2 = new Button(mContext);
            b2.setText("Remind me later");
            b2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ll.addView(b2);

            Button b3 = new Button(mContext);
            b3.setText("No, thanks");
            b3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (editor != null) {
                        editor.putBoolean("dontshowagain", true);
                        editor.commit();
                    }
                    dialog.dismiss();
                }
            });
            ll.addView(b3);

            dialog.setContentView(ll);
            dialog.show();
        }
    }
}