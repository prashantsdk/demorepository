package com.blueplanet.smartcookieteacher;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.UpdateProfileFeatureController;
import com.blueplanet.smartcookieteacher.models.NewRegistrationModel;
import com.blueplanet.smartcookieteacher.models.Student;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by AviK297 on 7/15/2016.
 */
public class UpdateProfileActivity extends AppCompatActivity implements IEventListener, View.OnClickListener {

    private CustomEditText _firstName, _middle, _lastName, _dob, _age, _gender, _qual, _occup, _email, _add, _country, _city, _state, _phone, _pasword, _confirmPas;
    private CustomButton _btnUpdate, _btnCancel;
    private FloatingActionButton _btnAction;
    private ImageView _parentImg;
    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
    private String userChoosenTask;
    private Bitmap bm;
    CoordinatorLayout _layout;
    String base64 = "";
    byte[] bb = null;
    private String parentId;
    private final String _TAG = getClass().getSimpleName();
    private ProgressBar progrees;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public String parentImage = null;
    Teacher teacher = null;
    private Teacher _teacher;
    String _schoolId;
    private Spinner spinner, spinnerPhone;
    String[] numberOptn = {"+91", "+1"};
    private TextView pDisplayDate;
    private Button pPickDate;
    private int pYear;
    private int pMonth;
    private int pDay;
    Calendar myCalendar = Calendar.getInstance();


    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    /**
     * This integer will uniquely define the dialog to be used for displaying date picker.
     */
    static final int DATE_DIALOG_ID = 0;
    NewRegistrationModel regmodel = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_profile);
        _initUI();
        _teacher = LoginFeatureController.getInstance().getTeacher();
        regmodel = UpdateProfileFeatureController.getInstance().getRemodel();
        displayBasicInfo();
        _editableFieldsFalse();
        handleButtonClick();



      /*  String user = LoginFeatureController.getInstance().get_emailID();
        String method = LoginFeatureController.getInstance().getMethod();
        String code = LoginFeatureController.getInstance().getColgcode();
        String colgcode ="coep";
        String devicedetail = LoginFeatureController.getInstance().getDevicedetail();
        String devicetype = LoginFeatureController.getInstance().getDevicetype();
        String ip = LoginFeatureController.getInstance().getIp();
        String platform = LoginFeatureController.getInstance().getPlatfom();
        String usertype = LoginFeatureController.getInstance().get_emailID();
        String countrycode = "";
        String teacherEmail = LoginFeatureController.getInstance().getEmail();
        String userid="4198005007";
        String userpass="dayaram123";
        String usertypeteacher="EmployeeID";

        String teacherpassword = LoginFeatureController.getInstance().getPassword();

        ;
        EventNotifier eventNotifier =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
        eventNotifier.registerListener(this, ListenerPriority.PRIORITY_MEDIUM);
        LoginFeatureController.getInstance().teacherLogin(userid, userpass, usertypeteacher, colgcode, method, devicetype, devicedetail,
                platform, ip, countrycode);*/

       /* pDisplayDate = (TextView) findViewById(R.id.displayDate);
        pPickDate = (Button) findViewById(R.id.pickDate);
*/
        /** Listener for click event of the button */
        /*pPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });*/

        /** Get the current date */
        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);

        /** Display the current date in the TextView */
        //  updateDisplay();

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
        _parentImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    openOptoinDialog();
                } else {
                    requestPermission();
                }
            }
        });

        _dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(UpdateProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void requestPermission() {


        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            Toast.makeText(getApplicationContext(), "To access Camera permission, Please allow in App Settings for camera functionality.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    private void back() {
        Intent i = new Intent(this, AfterLoginActivity.class);
        startActivity(i);
        finish();
    }

    private void showNetworkMsg() {
        final Snackbar snackbar;
        snackbar = Snackbar.make(_layout, "No Connection.Please check your network\n                setting and try again.", Snackbar.LENGTH_SHORT);/*.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

             });
            snackbar.setActionTextColor(Color.WHITE);*/
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.snack_back));

        snackbar.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    openOptoinDialog();
                    //    Snackbar.make(_rel, "Permission Granted, Now you can access location data.", Snackbar.LENGTH_LONG).show();

                } else {

                    final Snackbar snackbar;
                    snackbar = Snackbar.make(_layout, "Permission Denied, You cannot access camera.", Snackbar.LENGTH_LONG);/*.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

             });
            snackbar.setActionTextColor(Color.WHITE);*/
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

                    snackbar.show();


                }
                break;
        }
    }

    private void _handleUpdateEvents() {
        regmodel = UpdateProfileFeatureController.getInstance().getRemodel();

        String img = getBase64();// ParentProfileFeatureController.getInstance().getParentImage();
        // String name = _firstName.getText().toString();
        String dob = _dob.getText().toString();
        //String age = _age.getText().toString();
        String gender = _gender.getText().toString();
        //  String qual = _qual.getText().toString();
        //String occup = _occup.getText().toString();
        String email = _email.getText().toString();
        String city = _city.getText().toString();
        String add = _add.getText().toString();
        String country = _country.getText().toString();
        //String state = _state.getText().toString();

        String phone = _phone.getText().toString();
        String pas = _pasword.getText().toString();
        // String tid = _pasword.getText().toString();
        String fname = _firstName.getText().toString();
        String lname = _lastName.getText().toString();
        String mname = _middle.getText().toString();
        //
        //
        // String phone = "";
        String state = "";
        _schoolId = _teacher.get_tSchool_id();

        String countrycode = "91";
        int memberID = _teacher.getId();
        String _PhoneCode = String.valueOf(memberID);
        String Key = "member-id";

        _registerListeners();
        //sayali
        // onClick of button perform this simplest code.
        if (email.matches(emailPattern))
        {
            UpdateProfileFeatureController.getInstance().updateProfileInfo(email, fname, mname, lname, dob, add, city, country, gender, pas, phone, state, _schoolId,
                    countrycode, _PhoneCode, Key, img);
           // Toast.makeText(getApplicationContext(), "successfully updated", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
        }




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

    private void _initUI() {
        // _layout = (CoordinatorLayout) findViewById(R.id.pro);
        //  progrees = (ProgressBar) findViewById(R.id.progress5);
        _firstName = (CustomEditText) findViewById(R.id.edt_first_name);
        _middle = (CustomEditText) findViewById(R.id.edt_middle_name);
        _lastName = (CustomEditText) findViewById(R.id.edt_last_name);
        _dob = (CustomEditText) findViewById(R.id.edt_dob);
        //  _age = (CustomEditText) findViewById(R.id.edt_age);
        _gender = (CustomEditText) findViewById(R.id.edt_gender);
       /* _qual = (CustomEditText) findViewById(R.id.edt_qualifi);
        _occup = (CustomEditText) findViewById(R.id.edt_occup);*/
        _email = (CustomEditText) findViewById(R.id.edt_emaili);
        _add = (CustomEditText) findViewById(R.id.edt_addres);
        _city = (CustomEditText) findViewById(R.id.edt_city);
        _country = (CustomEditText) findViewById(R.id.edt_country);
        // _state = (CustomEditText) findViewById(R.id.edt_state);
        _phone = (CustomEditText) findViewById(R.id.edt_phone);
        _pasword = (CustomEditText) findViewById(R.id.edt_pasword);

        _btnUpdate = (CustomButton) findViewById(R.id.btn_update);
        _btnCancel = (CustomButton) findViewById(R.id.btn_cancel);
        _btnAction = (FloatingActionButton) findViewById(R.id.fab_editable);
        _parentImg = (ImageView) findViewById(R.id.Sponsor_image2);
        spinner = (Spinner) findViewById(R.id.register_spin);


        //_confirmPas = (CustomEditText) _view.findViewById(R.id.edt_password);
    }

    private void _editableFieldsTrue() {
        _firstName.setEnabled(true);
        _middle.setEnabled(true);
        _lastName.setEnabled(true);
        _dob.setEnabled(true);
        //  _age.setEnabled(true);
        _gender.setEnabled(true);
       /* _qual.setEnabled(true);
        _occup.setEnabled(true);*/
        _email.setEnabled(true);
        _add.setEnabled(true);
        _city.setEnabled(true);
        _country.setEnabled(true);
        //_state.setEnabled(true);
        _phone.setEnabled(true);
        _pasword.setEnabled(true);


    }

    private void _editableFieldsFalse() {
        _firstName.setEnabled(false);
        _middle.setEnabled(true);
        _lastName.setEnabled(false);
        _dob.setEnabled(false);
        // _age.setEnabled(false);
        _gender.setEnabled(false);
       /* _qual.setEnabled(false);
        _occup.setEnabled(false);*/
        _email.setEnabled(false);
        _add.setEnabled(false);
        _city.setEnabled(false);
        _country.setEnabled(false);
        //_state.setEnabled(false);
        _phone.setEnabled(false);
        _pasword.setEnabled(false);
        _btnUpdate.setEnabled(false);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, numberOptn);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getApplicationContext().getContentResolver(), data.getData());


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        _parentImg.setImageBitmap(bm);
        getBase64();
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();


        } catch (IOException e) {
            e.printStackTrace();
        }
        _parentImg.setImageBitmap(thumbnail);
        getBase64();
    }

    private String getBase64() {
        _parentImg.buildDrawingCache();
        Bitmap bmap = _parentImg.getDrawingCache();


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.JPEG, 0, bos);
        bb = bos.toByteArray();
        base64 = Base64.encodeToString(bb, Base64.DEFAULT);
        //sayali// ParentProfileFeatureController.getInstance().setParentImage(base64);

        Log.i("Image", base64);
        return base64;
    }

    private void openOptoinDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //     boolean result= Utility.checkPermission(MainActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";

                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";

                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        hasPermissionInManifest(this, MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    public boolean hasPermissionInManifest(Context context, String permissionName) {
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermisisons = packageInfo.requestedPermissions;
            if (declaredPermisisons != null && declaredPermisisons.length > 0) {
                for (String p : declaredPermisisons) {
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return false;
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    private void displayBasicInfo() {
        // parentInfo = LoginFeatureController.getInstance().getParent();

        teacher = LoginFeatureController.getInstance().getTeacher();
        parentId = teacher.get_tId();
        _setFirstNameOnUI(teacher);
        _setMiddleNameOnUI(teacher);
        _setLastNameOnUI(teacher);
        _DobNameOnUI(teacher);
        //_dob.setText(teacher.get_tDOB());
        _GenderNameOnUI(teacher);
        _EmailNameOnUI(teacher);
        _AddrNameOnUI(teacher);
        _cityNameOnUI(teacher);
        _countryNameOnUI(teacher);
        passwNameOnUI(teacher);
        String phone = String.valueOf(teacher.get_tPhone());
        _phone.setText(phone);


       /* String first = teacher.get_tName();
        String lname = teacher.get_tLastName();
        String dob = teacher.get_tDOB();
        int age = teacher.get_tAge();
        String gender = teacher.get_tGender();
       *//* String qual = teacher.get_tQualification();
        String occu = teacher.get_tQualification();*//*
        String eamil = teacher.get_tEmail();
        String add = teacher.get_tAddress();
        String city = teacher.get_tCity();
        String country = teacher.get_tCountry();
        int state = teacher.get_tState();
        int phone = teacher.get_tPhone();
        String pas = teacher.get_tPassword();
        String img = teacher.get_tPC();
        String url1 = WebserviceConstants.IMAGE_BASE_URL + img;

        _firstName.setText(first);
        _lastName.setText(lname);
        _dob.setText(dob);
        // _age.setText(age);
        _gender.setText(gender);
        _email.setText(eamil);
        _add.setText(add);
        _city.setText(city);
        _country.setText(country);


        //    _state.setText(state);

          _phone.setText(phone);

        _pasword.setText(pas);*/
        String img = teacher.get_tPC();
        String url1 = WebserviceConstants.IMAGE_BASE_URL + img;
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(url1, _parentImg);


    }

    private void _setFirstNameOnUI(Teacher teacher) {
        String first = teacher.get_tName();
        if (!(TextUtils.isEmpty(first)) && first.equalsIgnoreCase("null")) {
            _firstName.setText("");
        } else {
            _firstName.setText(first);
        }
    }

    private void _setMiddleNameOnUI(Teacher teacher) {
        String middle = teacher.get_tMiddleName();
        if (!(TextUtils.isEmpty(middle)) && middle.equalsIgnoreCase("null")) {
            _middle.setText("");
        } else {
            _middle.setText(middle);
        }
    }

    private void _setLastNameOnUI(Teacher teacher) {
        String Lirst = teacher.get_tLastName();
        if (!(TextUtils.isEmpty(Lirst)) && Lirst.equalsIgnoreCase("null")) {
            _lastName.setText("");
        } else {
            _lastName.setText(Lirst);
        }
    }

    private void _DobNameOnUI(Teacher teacher) {
        String dob = teacher.get_tDOB();
        if (!(TextUtils.isEmpty(dob)) && dob.equalsIgnoreCase("null")) {
            _dob.setText("");
        } else {
            _dob.setText(dob);
        }
    }

    private void _GenderNameOnUI(Teacher teacher) {
        String gender = teacher.get_tGender();
        if (!(TextUtils.isEmpty(gender)) && gender.equalsIgnoreCase("null")) {
            _gender.setText("");
        } else {
            _gender.setText(gender);
        }
    }

    private void _EmailNameOnUI(Teacher teacher) {
        String email = teacher.get_tEmail();
        if (!(TextUtils.isEmpty(email)) && email.equalsIgnoreCase("null")) {
            _email.setText("");
        } else {
            _email.setText(email);
        }
    }

    private void _AddrNameOnUI(Teacher teacher) {
        String addre = teacher.get_tAddress();
        if (!(TextUtils.isEmpty(addre)) && addre.equalsIgnoreCase("null")) {
            _add.setText("");
        } else {
            _add.setText(addre);
        }
    }

    private void _cityNameOnUI(Teacher teacher) {
        String city7 = teacher.get_tCity();
        if (!(TextUtils.isEmpty(city7)) && city7.equalsIgnoreCase("null")) {
            _city.setText("");
        } else {
            _city.setText(city7);
        }
    }

    private void _countryNameOnUI(Teacher teacher) {
        String contry = teacher.get_tCountry();
        if (!(TextUtils.isEmpty(contry)) && contry.equalsIgnoreCase("null")) {
            _country.setText("");
        } else {
            _country.setText(contry);
        }
    }

    private void passwNameOnUI(Teacher teacher) {
        String passw = teacher.get_tPassword();
        if (!(TextUtils.isEmpty(passw)) && passw.equalsIgnoreCase("null")) {
            _pasword.setText("");
        } else {
            _pasword.setText(passw);
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


            case EventTypes.EVENT_TEACHER_UI_UPDATE_PROFILE:
                EventNotifier eventsponsorlist =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER);
                eventsponsorlist.unRegisterListener(this);
                if (errorCode == WebserviceConstants.SUCCESS) {
                    //Sayali

                    regmodel = UpdateProfileFeatureController.getInstance().getRemodel();
                    Log.i(_TAG, "Value of reg is: ");
                    showProfileUpdateMsg(true);


                   // showOrHideLoadingSpinner(true);


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


            case EventTypes.EVENT_NETWORK_AVAILABLE:
                EventNotifier eventNotifiernetwork =
                        NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_NETWORK);
                eventNotifiernetwork.unRegisterListener(this);

                //   _homeFragment.showNetworkMessage(true);
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
                //showProfileUpdateMsg(false);


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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventNotifier eventNotifiernetwork =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_NETWORK);
        eventNotifiernetwork.unRegisterListener(this);
        EventNotifier eventNotifierlogin =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_LOGIN);
        eventNotifierlogin.unRegisterListener(this);
        EventNotifier eventNotifierprofile =
                NotifierFactory.getInstance().getNotifier(NotifierFactory.EVENT_NOTIFIER_TEACHER
                );
        eventNotifierprofile.unRegisterListener(this);


    }

    public void showOrHideLoadingSpinner(boolean visibility) {
        if (visibility) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    progrees.setVisibility(View.VISIBLE);
                }
            });

        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    progrees.setVisibility(View.INVISIBLE);
                }
            });

        }

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        _dob.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View view) {


    }
}



