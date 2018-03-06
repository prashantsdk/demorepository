package com.blueplanet.smartcookieteacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.UpdateProfileFeatureController;
import com.blueplanet.smartcookieteacher.models.NewRegistrationModel;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;

public class ProfileActivity extends AppCompatActivity implements IEventListener, View.OnClickListener{

    private CustomEditText _firstName, _middle, _lastName, _dob, _age, _gender, _qual, _occup, _email, _add, _country, _city, _state, _phone, _pasword, _confirmPas;
    private CustomButton _btnUpdate, _btnCancel;
    private FloatingActionButton _btnAction;
    private ImageView _parentImg;
    private Spinner spinner;
    String base64 = "";
    String[] numberOptn = {"+91", "+1"};
    CoordinatorLayout _layout;
    String _schoolId;
    private Teacher _teacher;
    private Teacher teacher;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    byte[] bb = null;
    private final String _TAG = getClass().getSimpleName();
    NewRegistrationModel regmodel = null;
    private String parentId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_profile);
        _Init();
        _InitListeners();
        fetchUserProfileFromServer();
       // displayBasicInfo();
        _editableFieldsFalse();
        handleButtonClick();
    }

    private void fetchUserProfileFromServer() {

       // LoginFeatureController.getInstance().FetchUserProfile(String.valueOf(teacher.getId()));
        progressDialog = WebserviceConstants.showProgress(this, "Please wait...");
        progressDialog.show();
        LoginFeatureController.getInstance().FetchUserProfile(String.valueOf(teacher.getId()));
    }

    private void _Init() {
        _firstName = findViewById(R.id.edt_first_name);
        _middle = findViewById(R.id.edt_middle_name);
        _lastName =  findViewById(R.id.edt_last_name);
        _dob =  findViewById(R.id.edt_dob);
        _gender =  findViewById(R.id.edt_gender);
        _email =  findViewById(R.id.edt_emaili);
        _add =  findViewById(R.id.edt_addres);
        _city =  findViewById(R.id.edt_city);
        _country =  findViewById(R.id.edt_country);
        _phone =  findViewById(R.id.edt_phone);
        _pasword =  findViewById(R.id.edt_pasword);
        _btnUpdate =  findViewById(R.id.btn_update);
        _btnCancel =  findViewById(R.id.btn_cancel);
        _btnAction =  findViewById(R.id.fab_editable);
        _parentImg =  findViewById(R.id.Sponsor_image2);
        spinner =  findViewById(R.id.register_spin);
    }

    private void _InitListeners() {
        _dob.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    private void handleButtonClick() {
        _btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkManager.isNetworkAvailable()) {
                    _handleUpdateEvents();
                } else {
                    showNetworkMsg();
                }

            }
        });
        _btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        _btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Editable", Toast.LENGTH_SHORT).show();
                _editableFieldsTrue();
                _btnUpdate.setEnabled(true);

            }
        });
        /*_parentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    openOptoinDialog();
                } else {
                    requestPermission();
                }
            }
        });*/
    }

    private void showNetworkMsg() {
        final Snackbar snackbar;
        snackbar = Snackbar.make(_layout, "No Connection.Please check your network\n                setting and try again.", Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.snack_back));

        snackbar.show();
    }

    private void _editableFieldsTrue() {
        _firstName.setEnabled(true);
        _middle.setEnabled(true);
        _lastName.setEnabled(true);
        _dob.setEnabled(true);
        _gender.setEnabled(true);
        _email.setEnabled(true);
        _add.setEnabled(true);
        _city.setEnabled(true);
        _country.setEnabled(true);
        _phone.setEnabled(true);
        _pasword.setEnabled(true);
    }

    private void _editableFieldsFalse() {
        _firstName.setEnabled(false);
        _middle.setEnabled(false);
        _lastName.setEnabled(false);
        _dob.setEnabled(false);
        _gender.setEnabled(false);
        _email.setEnabled(false);
        _add.setEnabled(false);
        _city.setEnabled(false);
        _country.setEnabled(false);
        _phone.setEnabled(false);
        _pasword.setEnabled(false);
        _btnUpdate.setEnabled(false);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, numberOptn);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

    }

    private void _handleUpdateEvents() {
        String img = getBase64();
        String dob = _dob.getText().toString();
        String gender = _gender.getText().toString();
        String email = _email.getText().toString();
        String city = _city.getText().toString();
        String add = _add.getText().toString();
        String country = _country.getText().toString();
        String phone = _phone.getText().toString();
        String pas = _pasword.getText().toString();
        String fname = _firstName.getText().toString();
        String lname = _lastName.getText().toString();
        String mname = _middle.getText().toString();

        String state = "";
        _schoolId = _teacher.get_tSchool_id();

        String countrycode = "91";
        int memberID = _teacher.getId();
        String _PhoneCode = String.valueOf(memberID);
        String Key = "member-id";

        _registerListeners();
        //sayali
        // onClick of button perform this simplest code.
        if (email.matches(emailPattern)) {
            UpdateProfileFeatureController.getInstance().updateProfileInfo(email, fname, mname, lname, dob, add, city, country, gender, pas, phone, state, _schoolId,
                    countrycode, _PhoneCode, Key, img);
        }
        else {
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
        }
    }

    private String getBase64() {
        _parentImg.buildDrawingCache();
        Bitmap bmap = _parentImg.getDrawingCache();


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.JPEG, 0, bos);
        bb = bos.toByteArray();
        base64 = Base64.encodeToString(bb, Base64.DEFAULT);

        Log.i("Image", base64);
        return base64;
    }

    private void _registerListeners() {

        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

        EventNotifier notifierSubscribe =
                NotifierFactory.getInstance().getNotifier(
                        NotifierFactory.EVENT_NOTIFIER_NETWORK);
        notifierSubscribe.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
    }

    private void back() {
        Intent i = new Intent(this, AfterLoginActivity.class);
        startActivity(i);
        finish();
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


            case EventTypes.EVENT_TEACHER_UI_UPDATE_PROFILE:
                EventNotifier eventsponsorlist =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventsponsorlist.unRegisterListener(this);
                if (errorCode == WebserviceConstants.SUCCESS) {

                    regmodel = UpdateProfileFeatureController.getInstance().getRemodel();

                  //  setTeacherInfo(regmodel);

                    Log.i(_TAG, "Value of reg is: ");
                    showProfileUpdateMsg(true);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _editableFieldsFalse();
                        }
                    });

                    EventNotifier eventNotifier =
                            NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);

                } else {
                    showProfileUpdateMsg(false);
                }
                break;

            case EventTypes.EVENT_TEACHER_UI_NOT_UPDATE_PROFILE:
                EventNotifier eventnosponsorlist =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventnosponsorlist.unRegisterListener(this);
                showProfileUpdateMsg(false);
                break;

            case EventTypes.EVENT_TEACHER_UI_DISPLAY_PROFILE:
                EventNotifier eventprofile =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventprofile.unRegisterListener(this);
                if (errorCode == WebserviceConstants.SUCCESS) {

                    progressDialog.dismiss();

                    regmodel = UpdateProfileFeatureController.getInstance().getRemodel();

                    displayBasicInfo(regmodel);
                    //  setTeacherInfo(regmodel);

                    Log.i(_TAG, "Value of reg is: ");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _editableFieldsFalse();
                        }
                    });

                   /* EventNotifier eventNotifier =
                            NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                    eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);*/
                }
                break;

            case EventTypes.EVENT_TEACHER_UI_NOT_DISPLAY_PROFILE:
                EventNotifier eventNotDisplay =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventNotDisplay.unRegisterListener(this);

                break;

            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNotifiernetwork =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNotifiernetwork.unRegisterListener(this);

                break;

            case EventTypes.EVENT_NETWORK_UNAVAILABLE:
                EventNotifier event =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_NETWORK);
                event.unRegisterListener(this);

                //   _homeFragment.showOrHideLoadingSpinner(false);
                //   _homeFragment.showNetworkMessage(false);


                break;
            case EventTypes.EVENT_UI_LOGIN_SUCCESSFUL:
                EventNotifier evenlist =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
                evenlist.unRegisterListener(this);
                if (errorCode == WebserviceConstants.SUCCESS) {
                   /* showOrHideLoadingSpinner(false);
                    Intent i = new Intent(this, AfterLoginActivity.class);
                    startActivity(i);
                    finish();*/
                } else {
                    // showProfileUpdateMsg(false);
                }
                break;
            case EventTypes.EVENT_UI_NO_LOGIN_RESPONSE:
                EventNotifier eventnosponsorlist1 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
                eventnosponsorlist1.unRegisterListener(this);
                break;
            case EventTypes.EVENT_UI_BAD_REQUEST:
                EventNotifier eventNotifier1 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
                eventNotifier1.unRegisterListener(this);
                break;
            case EventTypes.EVENT_UI_UNAUTHORIZED:
                EventNotifier eventNotifier2 =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
                eventNotifier2.unRegisterListener(this);
                break;
            default:
                eventState = EventState.EVENT_IGNORED;
                break;
        }
        return EventState.EVENT_PROCESSED;
    }

    private void showProfileUpdateMsg(final boolean flag) {
        if (flag == true) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Profile not updated,Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void _setFirstNameOnUI(NewRegistrationModel teacher) {
        String first = teacher.get_fname();
        if (!(TextUtils.isEmpty(first)) && first.equalsIgnoreCase("null")) {
            _firstName.setText("");
        } else {
            _firstName.setText(first);
        }
    }

    private void _setMiddleNameOnUI(NewRegistrationModel teacher) {
        String middle = teacher.get_mname();
        if (!(TextUtils.isEmpty(middle)) && middle.equalsIgnoreCase("null")) {
            _middle.setText("");
        } else {
            _middle.setText(middle);
        }
    }

    private void _setLastNameOnUI(NewRegistrationModel teacher) {
        String Lirst = teacher.get_lname();
        if (!(TextUtils.isEmpty(Lirst)) && Lirst.equalsIgnoreCase("null")) {
            _lastName.setText("");
        } else {
            _lastName.setText(Lirst);
        }
    }

    private void _DobNameOnUI(NewRegistrationModel teacher) {
        String dob = teacher.get_dob();
        if (!(TextUtils.isEmpty(dob)) && dob.equalsIgnoreCase("null")) {
            _dob.setText("");
        } else {
            _dob.setText(dob);
        }
    }

    private void _GenderNameOnUI(NewRegistrationModel teacher) {
        String gender = teacher.get_gender();
        if (!(TextUtils.isEmpty(gender)) && gender.equalsIgnoreCase("null")) {
            _gender.setText("");
        } else {
            _gender.setText(gender);
        }
    }

    private void _EmailNameOnUI(NewRegistrationModel teacher) {
        String email = teacher.get_email();
        if (!(TextUtils.isEmpty(email)) && email.equalsIgnoreCase("null")) {
            _email.setText("");
        } else {
            _email.setText(email);
        }
    }

    private void _AddrNameOnUI(NewRegistrationModel teacher) {
        String addre = teacher.get_address();
        if (!(TextUtils.isEmpty(addre)) && addre.equalsIgnoreCase("null")) {
            _add.setText("");
        } else {
            _add.setText(addre);
        }
    }

    private void _cityNameOnUI(NewRegistrationModel teacher) {
        String city7 = teacher.get_city();
        if (!(TextUtils.isEmpty(city7)) && city7.equalsIgnoreCase("null")) {
            _city.setText("");
        } else {
            _city.setText(city7);
        }
    }

    private void _countryNameOnUI(NewRegistrationModel teacher) {
        String contry = teacher.get_country();
        if (!(TextUtils.isEmpty(contry)) && contry.equalsIgnoreCase("null")) {
            _country.setText("");
        } else {
            _country.setText(contry);
        }
    }

    private void passwNameOnUI(NewRegistrationModel teacher) {
        String passw = teacher.get_password();
        if (!(TextUtils.isEmpty(passw)) && passw.equalsIgnoreCase("null")) {
            _pasword.setText("");
        } else {
            _pasword.setText(passw);
        }
    }

    private void displayBasicInfo(NewRegistrationModel regmodel) {

        _setFirstNameOnUI(regmodel);
        _setMiddleNameOnUI(regmodel);
        _setLastNameOnUI(regmodel);
        _DobNameOnUI(regmodel);
        _GenderNameOnUI(regmodel);
        _EmailNameOnUI(regmodel);
        _AddrNameOnUI(regmodel);
        _cityNameOnUI(regmodel);
        _countryNameOnUI(regmodel);
        passwNameOnUI(regmodel);
        String phone = String.valueOf(regmodel.get_phone());
        _phone.setText(phone);
        String img = regmodel.get_imgpath();
        String url1 = WebserviceConstants.IMAGE_BASE_URL + img;
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(url1, _parentImg);
    }
}
