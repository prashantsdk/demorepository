package com.blueplanet.smartcookieteacher.ui.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.featurecontroller.RegistrationFeatureController;
import com.blueplanet.smartcookieteacher.models.RegisModel;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.LoginFragment;
import com.blueplanet.smartcookieteacher.ui.RegistrationFragment;
import com.blueplanet.smartcookieteacher.utils.HelperClass;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by 1311 on 17-02-2016.
 */
public class RegistrationFragmentController implements IEventListener, View.OnClickListener {

    private RegistrationFragment _regFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private RegisModel _register;
    private EditText email;
    private Spinner spinnerPhone;


    /**
     * constructur for reward list
     */


    public RegistrationFragmentController(RegistrationFragment regFragment, View view) {

        _regFragment = regFragment;
        _view = view;

        email = (EditText) _view.findViewById(R.id.edt_emailId);
        spinnerPhone = (Spinner) _view.findViewById(R.id.spinnerPhone);

        initListener();

    }


    private void initListener() {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                HelperClass.Is_Valid_Email(email);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void _registerEventListeners() {

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

        if (_regFragment != null) {
            _regFragment = null;
        }
    }

    /**
     * webservice call to fetch reward list from server
     *
     * @
     */
    private void _fetchRegistrationServer(String fname, String lname, String email, String pass, String phone, String mname, String countrycode, String type, String sourse) {
        _registerEventListeners();
        _regFragment.showOrHideProgressBar(true);
        RegistrationFeatureController.getInstance().fetchRegistrationServer(fname, lname, email, pass, phone, mname, countrycode, type, sourse);
        _regFragment.hideSoftKeyboard();
        //_regFragment.showOrHideProgressBar(true);


    }

    private boolean validateEmail() {
        String eamail = email.getText().toString().trim();

        if (eamail.isEmpty() || !isValidEmail(eamail)) {

            return false;
        } else {

        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_register:
                // _regFragment.hideSoftKeyboard();

                if (NetworkManager.isNetworkAvailable()) {

                    EditText fname = (EditText) _view.findViewById(R.id.edt_firstname);
                    EditText lname = (EditText) _view.findViewById(R.id.edt_lastName);
                    EditText email = (EditText) _view.findViewById(R.id.edt_emailId);
                    EditText tpassword = (EditText) _view.findViewById(R.id.edt_password);
                    EditText phone = (EditText) _view.findViewById(R.id.edtPhone);
                    EditText middle = (EditText) _view.findViewById(R.id.edt_middleName);


                    String Fname = fname.getText().toString();
                    String Lname = lname.getText().toString();
                    String Email = email.getText().toString();

                    String password = tpassword.getText().toString();
                    String Phone = phone.getText().toString();
                    String middlename = middle.getText().toString();

                    String type = "teacher";
                    String sourse = "Android";
                    String countrycode = spinnerPhone.getSelectedItem().toString();


                    if ((!TextUtils.isEmpty(Fname)) &&
                            (!TextUtils.isEmpty(middlename)) &&
                            (!TextUtils.isEmpty(Lname)) &&
                            (!TextUtils.isEmpty(Phone)) &&
                            (!TextUtils.isEmpty(Email)) &&
                            (!TextUtils.isEmpty(countrycode)) &&
                            (!TextUtils.isEmpty(password)) &&
                            (checkPhoneNoLenght(Phone)) &&
                            (checkPasswordLenth(password)) &&
                            (phoneNoShouldNotStartWithZero(Phone, countrycode))) {

                        if (HelperClass.Is_Valid_Email(email)) {


                            _fetchRegistrationServer(Fname, Lname, Email, password, Phone, middlename, countrycode, type, sourse);

                        } else {

                            Toast.makeText(MainApplication.getContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                        }

                    } else if (TextUtils.isEmpty(Fname)) {
                        Toast.makeText(MainApplication.getContext(),
                                "Please enter your First Name",
                                Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(middlename)) {
                        Toast.makeText(MainApplication.getContext(),
                                "Please enter your Middle Name",
                                Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(Lname)) {

                        Toast.makeText(MainApplication.getContext(),
                                "Please enter your Last Name",
                                Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(Phone)) {

                        Toast.makeText(MainApplication.getContext(),
                                "Please enter your Mobile No.",
                                Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(Email)) {
                        Toast.makeText(MainApplication.getContext(),
                                "Please enter your Email id",
                                Toast.LENGTH_SHORT).show();

                    } else if (TextUtils.isEmpty(countrycode)) {
                        Toast.makeText(MainApplication.getContext(),
                                "Please enter your Country code",
                                Toast.LENGTH_SHORT).show();

                    } else if (TextUtils.isEmpty(password)) {
                        Toast.makeText(MainApplication.getContext(),
                                "Please enter your Password",
                                Toast.LENGTH_SHORT).show();

                    } else if ((!checkPhoneNoLenght(Phone))) {
                        Toast.makeText(MainApplication.getContext(),
                                "Mobile number must be 10 digits",
                                Toast.LENGTH_SHORT).show();

                    } else if ((!checkPasswordLenth(password))) {

                        Toast.makeText(MainApplication.getContext(),
                                "Password length must be greater than 7",
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
                    _regFragment.showNetworkToast(false);
                }
                break;

            case R.id.tv_member_login:
                _regFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FragmentManager fm = _regFragment.getActivity()
                                .getSupportFragmentManager();
                        fm.popBackStack("LoginFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                });

                break;


            case R.id.btn_clear:

                EditText fname = (EditText) _view.findViewById(R.id.edt_firstname);
                EditText lname = (EditText) _view.findViewById(R.id.edt_lastName);
                EditText email = (EditText) _view.findViewById(R.id.edt_emailId);
                EditText tpassword = (EditText) _view.findViewById(R.id.edt_password);
                EditText phone = (EditText) _view.findViewById(R.id.edtPhone);
                EditText middle = (EditText) _view.findViewById(R.id.edt_middleName);


                fname.setText("");
                lname.setText("");
                email.setText("");
                tpassword.setText("");
                phone.setText("");
                middle.setText("");


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

    private boolean checkPasswordLenth(String mPassword) {

        if (mPassword.length() < 8) {
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

        switch (eventType) {
            case EventTypes.EVENT_UI_REGISTRATION_SUCCESS:
                EventNotifier eventNotifier =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotifier.unRegisterListener(this);

                if (errorCode == WebserviceConstants.SUCCESS) {
                    // _regFragment.showOrHideProgressBar(false);
                    /**
                     * get reward list before refreshing listview avoid runtime exception
                     */
                    _regFragment.showOrHideProgressBar(false);
                    _register = RegistrationFeatureController.getInstance().get_registration();
                    _regFragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          /*  Toast.makeText(MainApplication.getContext(),
                                    "Registration Successful! Thank you!",
                                    Toast.LENGTH_LONG).show();*/
                            AlertDialog alert;
                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    _regFragment.getActivity());
                            builder.setTitle("Congratulations!! You are successfully register");
                            builder.setCancelable(false);

                            builder.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                            _loadFragment(R.id.Registration_fragment_layout, new LoginFragment());

                                        }
                                    });

                            alert = builder.create();
                            alert.show();


                            FragmentManager fm = _regFragment.getActivity()
                                    .getSupportFragmentManager();
                            fm.popBackStack("LoginFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    });

                }
                break;

            case EventTypes.EVENT_UI_NOT_REGISTRATION_SUCCESS:

                EventNotifier event1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event1.unRegisterListener(this);
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(_regFragment.getActivity());
                dlgAlert.setMessage("There is some problem for Registration!");

                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();


                break;

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNetwork1 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork1.unRegisterListener(this);

                _regFragment.showOrHideProgressBar(false);
                break;
            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier eventNetwork =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNetwork.unRegisterListener(this);

                _regFragment.showOrHideProgressBar(false);
                //_rePointFragment.showNetworkToast(false);
                break;
            case EventTypes.EVENT_UI_INVALID_INPUT:

                EventNotifier event3 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event3.unRegisterListener(this);
                _regFragment.showOrHideProgressBar(false);
                _regFragment.invalidinputMessage(false);


                break;
            case EventTypes.EVENT_UI_BAD_REQUEST:


                EventNotifier event2 =
                        NotifierFactory.getInstance().getNotifier
                                (NotifierFactory.EVENT_NOTIFIER_TEACHER);
                event2.unRegisterListener(this);

                _regFragment.getActivity().runOnUiThread(new Runnable() {


                    @Override
                    public void run() {
                        AlertDialog alert;
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                _regFragment.getActivity());
                        builder.setTitle("You are already registered member of SmartTeacher!");
                        builder.setCancelable(false);

                        builder.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        _loadFragment(R.id.Registration_fragment_layout, new LoginFragment());

                                    }
                                });

                        alert = builder.create();
                        alert.show();
                        FragmentManager fm = _regFragment.getActivity()
                                .getSupportFragmentManager();
                        fm.popBackStack("LoginFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    }
                });
                break;
            default:
                eventState = EventState.EVENT_IGNORED;
                break;

        }

        return EventState.EVENT_PROCESSED;


    }

    private void _loadFragment(int id, Fragment fragment) {

        FragmentManager fm = _regFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack("LoginFragment");
        ft.commitAllowingStateLoss();
    }


}
