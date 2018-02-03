package com.blueplanet.smartcookieteacher.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.LoginDetailModel;
import com.blueplanet.smartcookieteacher.models.TestPro;
import com.blueplanet.smartcookieteacher.models.User;
import com.blueplanet.smartcookieteacher.ui.controllers.LoginFragmentController;
import com.blueplanet.smartcookieteacher.ui.customactionbar.UserSession;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * Created by 1311 on 24-11-2015.
 */
public class LoginFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private View _view;
    private CustomEditText _etUserName, _etPassword;
    private Button _btnLogin, _btnsignup, btnRegis, btnfb;
    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private CustomTextView _tvPleaseWait, txtp;
    private LoginFragmentController _loginFragmentController = null;
    private CheckBox _rememberMe;
    private User user;
    private TestPro testpro;
    public User url;
    public String strurl;
    private CustomTextView _test, _production, tv_forgotPassword, _dev;
    private EditText etxtpoints;
    private ImageView imgclearpoints;
    private Spinner spinner, spinnerPhone;
    String[] userOption = {"Select Login Type", "Email", "Mobile-No", "EmployeeID", "MemberID"};
    String[] numberOptn = {"+91", "+1"};
    private LinearLayout ll_userphone, ll_phone, ll_prn, ll_ID, _l1memberID;

    public int _urlTP = 0;
    private final String _TAG = this.getClass().getSimpleName();
    private String selState, str;

    GPSTracker gpsTracker;
    double latitude = 0.0, longitude = 0.0;
    public static final int PERMISSION_REQUEST_CODE = 23;
    String[] LOC_PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    EditText etUserMobile;
    Date d = new Date();
    public CallbackManager callbackManager = CallbackManager.Factory.create();
    public static final int RC_SIGN_IN = 9001;
    public static Context context;
    Context pContext;
    //private CallbackManager callbackManager;
    //

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        _view = inflater.inflate(R.layout.mobile_teacher_login, null);

        FacebookSdk.sdkInitialize(getActivity());
        context = this.getActivity();
        _initUI();

        _loginFragmentController = new LoginFragmentController(this, _view);


        if (checkPermission()) {
            gpsTracker = new GPSTracker(getActivity());
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

        } else {

            requestPermission();
        }

        try {
            MainApplication.enableGPS();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // create class object


        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, userOption);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        ArrayAdapter phone = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, numberOptn);
        phone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhone.setAdapter(phone);
        getAndroidVersion();
        getDeviceName();
        //  getDeviceId();
        getLocalIpAddress();
        isTabletDevice(getActivity());
        isTablet(getContext());
        _registerUIListeners();
        _isRememberMeClicked();
        isTabletDevice();
        return _view;
    }

    /**
     * function to implement remember me functionality
     */
    private void _isRememberMeClicked() {
        // boolean rememberMe = SmartCookieSharedPreferences.getRememberMeFlag();

            /*final String userName = SmartCookieSharedPreferences.getUserName();
            final String passowrd = SmartCookieSharedPreferences.getPasswordKey();*/

        user = LoginFeatureController.getInstance().getUserInfoFromDB();

        if (user != null) {
            final String userName = user.getUserName();
            final String passowrd = user.getPassword1();


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _etUserName.setText(userName);
                    _etPassword.setText(passowrd);
                    UserSession.setName(userName);
                    _rememberMe.setChecked(true);


                }
            });
        } else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    _etUserName.setText("");
                    _etPassword.setText("");
                    _rememberMe.setChecked(false);
                }
            });
        }
    }

    private void _initUI() {

        _etUserName = (CustomEditText) _view.findViewById(R.id.edt_username);

        _etPassword = (CustomEditText) _view.findViewById(R.id.edt_password);

        _btnLogin = (Button) _view.findViewById(R.id.btn_login);
        btnRegis = (Button) _view.findViewById(R.id.btnRegis);
        btnfb = (Button) _view.findViewById(R.id.btnfacebook);
        _rlProgressbar = (RelativeLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (CustomTextView) _view.findViewById(R.id.tv_please_wait);
        txtp = (CustomTextView) _view.findViewById(R.id.tv_forgotPassword);


        //_btnsignup=(Button)_view.findViewById(R.id.btn_signup);
        // _btnsignup = (CustomButton) _view.findViewById(R.id.btn_signup);
        _rememberMe = (CheckBox) _view.findViewById(R.id.cb_remember_me);
        _test = (CustomTextView) _view.findViewById(R.id.txttest);
        _production = (CustomTextView) _view.findViewById(R.id.txtproduction);
        _dev = (CustomTextView) _view.findViewById(R.id.txtDev);
        tv_forgotPassword = (CustomTextView) _view.findViewById(R.id.tv_forgotPassword);


        spinner = (Spinner) _view.findViewById(R.id.spinner);
        spinnerPhone = (Spinner) _view.findViewById(R.id.spinnerPhone);
        ll_userphone = (LinearLayout) _view.findViewById(R.id.ll_username);
        ll_phone = (LinearLayout) _view.findViewById(R.id.ll_phone);
        ll_prn = (LinearLayout) _view.findViewById(R.id.ll_prn);
        ll_ID = (LinearLayout) _view.findViewById(R.id.ll_ID);
        _l1memberID = (LinearLayout) _view.findViewById(R.id.ll_MemerId);
        EditText etUserMobile = (EditText) _view.findViewById(R.id.edt_phone);
        // imgclearpoints = (ImageView) _view.findViewById(R.id.imgclearpoints);
        //  etxtpoints = (EditText) _view.findViewById(R.id.etxtpoints);
    }

    /**
     * function to register UI Listeners
     */
    private void _registerUIListeners() {

        _btnLogin.setOnClickListener(_loginFragmentController);
        btnRegis.setOnClickListener(_loginFragmentController);
        //
        //
        btnfb.setOnClickListener(_loginFragmentController);
        //_btnsignup.setOnClickListener(_loginFragmentController);


        _test.setOnClickListener(_loginFragmentController);
        _production.setOnClickListener(_loginFragmentController);
        _dev.setOnClickListener(_loginFragmentController);
        // _btntest.setOnClickListener(_loginFragmentController);


        // _btnproduction.setOnClickListener(_loginFragmentController);
        tv_forgotPassword.setOnClickListener(_loginFragmentController);

        spinner.setOnItemSelectedListener(_loginFragmentController);
        spinnerPhone.setOnItemSelectedListener(this);
        //  etxtpoints.setOnClickListener(_loginFragmentController);
        //imgclearpoints.setOnClickListener(_loginFragmentController);
    }

    /**
     * function to show or hide loading spinner
     *
     * @param visibility
     */
    public void showOrHideProgressBar(final boolean visibility) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {
                    _rlProgressbar.setVisibility(VISIBLE);
                    _progressbar.setVisibility(VISIBLE);
                    _tvPleaseWait.setVisibility(VISIBLE);
                } else {
                    _rlProgressbar.setVisibility(GONE);
                    _progressbar.setVisibility(GONE);
                    _tvPleaseWait.setVisibility(GONE);


                }
            }
        });

    }

    public void showGCMsmassage(final boolean visibility) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (visibility) {

                } else {
                    Toast.makeText(getActivity(), "Sorry! We are Unable to Register GCM", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }

    public void showType(final int position) {

        spinner.setSelection(position);
        spinner.setSelection(position);
        selState = (String) spinner.getSelectedItem();

        LoginFeatureController.getInstance().set_emailID(selState);


        Log.i(_TAG, "In selected item" + selState);
        if (selState.equalsIgnoreCase("Email")) {
            ll_userphone.setVisibility(View.VISIBLE);
            ll_phone.setVisibility(View.INVISIBLE);
            ll_prn.setVisibility(View.INVISIBLE);
            ll_ID.setVisibility(View.VISIBLE);
            _l1memberID.setVisibility(View.INVISIBLE);

            ll_userphone.requestFocus();

            // LoginFeatureController.getInstance().setUserEmailType(true);
        } else if (selState.equalsIgnoreCase("Mobile-No")) {
            ll_userphone.setVisibility(View.INVISIBLE);
            ll_phone.setVisibility(View.VISIBLE);
            ll_prn.setVisibility(View.INVISIBLE);
            ll_ID.setVisibility(View.VISIBLE);
            _l1memberID.setVisibility(View.INVISIBLE);
            ll_phone.requestFocus();

            //   LoginFeatureController.getInstance().setUserEmailType(false);
        } else if (selState.equalsIgnoreCase("EmployeeID")) {
            ll_prn.setVisibility(View.VISIBLE);
            ll_ID.setVisibility(View.VISIBLE);
            ll_userphone.setVisibility(View.INVISIBLE);
            ll_phone.setVisibility(View.INVISIBLE);
            _l1memberID.setVisibility(View.INVISIBLE);


            //LoginFeatureController.getInstance().setUserEmailType(false);

        } else if (selState.equalsIgnoreCase("MemberID")) {
            ll_prn.setVisibility(View.INVISIBLE);
            ll_ID.setVisibility(View.VISIBLE);
            ll_userphone.setVisibility(View.INVISIBLE);
            ll_phone.setVisibility(View.INVISIBLE);
            _l1memberID.setVisibility(View.VISIBLE);

            _l1memberID.requestFocus();

            //LoginFeatureController.getInstance().setUserEmailType(false);

        }
    }

    public void showTypePhone(final int position) {

        spinnerPhone.setSelection(position);
        spinnerPhone.setSelection(position);
        String selState = (String) spinner.getSelectedItem();
        LoginFeatureController.getInstance().set_phoneNo(selState);

        if (selState.equalsIgnoreCase("+91")) {

            ll_userphone.setVisibility(View.VISIBLE);
            ll_phone.setVisibility(View.INVISIBLE);
            ll_prn.setVisibility(View.INVISIBLE);
            ll_ID.setVisibility(View.GONE);
            // LoginFeatureController.getInstance().setUserEmailType(true);
        } else if (selState.equalsIgnoreCase("+1")) {
            ll_userphone.setVisibility(View.INVISIBLE);
            ll_phone.setVisibility(View.VISIBLE);
            ll_prn.setVisibility(View.INVISIBLE);
            ll_ID.setVisibility(View.GONE);
            //   LoginFeatureController.getInstance().setUserEmailType(false);
        }
    }

    /**
     * function to hide soft input keyboard
     */
    public void hideSoftKeyboard() {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(_view.getWindowToken(), 0);

                    }
                });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            _loginFragmentController.handleSignInResult(result);
        } else {

            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void showNetworkToast(final boolean isNetworkAvailable) {
        getActivity().runOnUiThread(new Runnable()


        {
            @Override
            public void run() {
                if (isNetworkAvailable) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.network_available),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.network_not_available),
                            Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    public void showLoginErrorMessage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.str_no_login_response),
                        Toast.LENGTH_LONG).show();
            }
        });

    }



  /*  public void showTestProduction(final boolean tePro) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tePro ) {

                        TestPro testpro = new TestPro();
                        WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL;
                        testpro.set_url(WebserviceConstants.BASE_URL);
                     String a = testpro.get_url();
                        Toast.makeText(getActivity(), a.toString(),
                                Toast.LENGTH_LONG).show();

                } else {
                    TestPro testpro = new TestPro();
                    WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL1;
                    testpro.set_url(WebserviceConstants.BASE_URL);
                    String a =  testpro.get_url();
                    Toast.makeText(getActivity(),a.toString(),
                            Toast.LENGTH_LONG).show();

                }

            }
        });


    }*/

    public void onDestroy() {
        super.onDestroy();
        if (_loginFragmentController != null) {
            _loginFragmentController.clear();
            _loginFragmentController = null;
        }

    }


   /**/

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.showTypePhone(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private static boolean isTabletDevice(Context activityContext) {

        boolean device_large = ((activityContext.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
        DisplayMetrics metrics = new DisplayMetrics();
        Activity activity = (Activity) activityContext;
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (device_large) {
            //Tablet
            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_TV) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_HIGH) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_280) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_400) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_XXHIGH) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_560) {
                return true;
            } else if (metrics.densityDpi == DisplayMetrics.DENSITY_XXXHIGH) {
                return true;
            }
        } else {
            //Mobile

            //Log.i(_TAG, "In selected item" + selState);


        }
        return false;
    }


    private boolean isTabletDevice() {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            // honeycomb
            // test screen size, use reflection because isLayoutSizeAtLeast is only available since 11
            Configuration con = getResources().getConfiguration();
            try {
                Method mIsLayoutSizeAtLeast = con.getClass().getMethod("isLayoutSizeAtLeast");
                Boolean r = (Boolean) mIsLayoutSizeAtLeast.invoke(con, 0x00000004); // Configuration.SCREENLAYOUT_SIZE_XLARGE
                return r;
            } catch (Exception x) {
                return false;
            }
        }
        return false;
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
        //  str = Boolean.toString(xlarge);
    }

    public String getDeviceName() {
        LoginDetailModel modelName = new LoginDetailModel();
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        modelName.set_modelName(model);


        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public String getAndroidVersion() {
        //LoginDetailModel version = new LoginDetailModel();
        return android.os.Build.VERSION.RELEASE;

        //version.set_version(RELEASE);
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


 /*   private String getDeviceId() {
        String deviceId = "";

        final TelephonyManager mTelephony = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null) {
            deviceId = mTelephony.getDeviceId();

        } else {
            deviceId = Settings.Secure.getString(getActivity().getApplicationContext()
                    .getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }*/

    public String getLocalIpAddress() {
        try {
            LoginDetailModel ipadd = new LoginDetailModel();

            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {

                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        ipadd.set_ipAdd(ip);
                        Log.i(_TAG, "***** IP=" + ip);

                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(_TAG, ex.toString());
        }
        return null;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }


    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {

            Toast.makeText(getActivity(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), LOC_PERMISSIONS, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    gpsTracker = new GPSTracker(getActivity());
                    Toast.makeText(getActivity(), "Permission Granted, Now you can access location data", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getActivity(), "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


}


   /* @Override
    public void onClick(View v) {

        _btnproduction.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {
                TestPro testpro = new TestPro();
                WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL;
                testpro.set_url(WebserviceConstants.BASE_URL);
                String a = testpro.get_url();
                Toast.makeText(getActivity(), a.toString(),
                        Toast.LENGTH_LONG).show();
            }

        });

        _btntest.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {
                TestPro testpro = new TestPro();
                WebserviceConstants.BASE_URL = WebserviceConstants.BASE_URL1;
                testpro.set_url(WebserviceConstants.BASE_URL);
                String a =  testpro.get_url();
                Toast.makeText(getActivity(),a.toString(),
                        Toast.LENGTH_LONG).show();
            }

        });
    }*/

