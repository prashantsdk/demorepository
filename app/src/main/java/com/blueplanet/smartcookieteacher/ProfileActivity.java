package com.blueplanet.smartcookieteacher;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.UpdateProfileFeatureController;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity implements IEventListener, View.OnClickListener{

    private CustomEditText _firstName, _middle, _lastName, _dob, _gender, _email, _add, _country, _city, _phone, _pasword;
    private CustomButton _btnUpdate, _btnCancel;
    private FloatingActionButton _btnAction;
    private RoundedImageView _parentImg;
    private Spinner spinner;
    String base64 = "";
    String[] numberOptn = {"+91", "+1"};
    RelativeLayout _layout;
    String _schoolId;
    private Teacher _teacher;
    private String tId;
    private String userChoosenTask;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    byte[] bb = null;
    private Bitmap bm;
    private final String _TAG = getClass().getSimpleName();
    private ProgressDialog progressDialog;
    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    static final int DATE_PICKER_ID = 1111;
    private int year;
    private int month;
    private int day;

    Calendar c = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener pickerListener =
            new DatePickerDialog.OnDateSetListener() {

                // when dialog box is closed, below method will be called.
                @Override
                public void onDateSet(DatePicker view, int selectedYear,
                                      int selectedMonth, int selectedDay) {
                    year = selectedYear;
                    month = selectedMonth;
                    day = selectedDay;
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.DAY_OF_MONTH, day);
                    long diff = (c.getTimeInMillis() - cal.getTimeInMillis())
                            / (24 * 60 * 60 * 1000);
                    diff = diff / 365;
                    month += 1;
                    _dob.setText(day + "/" + month + "/" + year);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_profile);
        _Init();
        _InitListeners();
        if (NetworkManager.isNetworkAvailable()) {
            fetchUserProfileFromServer();
        } else {
            WebserviceConstants.showNetworkMsg(this);
        }
        _editableFieldsFalse();
        handleButtonClick();
    }

    private void fetchUserProfileFromServer() {
        _registerListeners();
        progressDialog = WebserviceConstants.showProgress(this, "Please wait...");
        progressDialog.show();
        _teacher = LoginFeatureController.getInstance().getTeacher();
        LoginFeatureController.getInstance().FetchUserProfile(String.valueOf(_teacher.get_tId()), _teacher.get_tSchool_id());
    }

    private void _Init() {
        _layout = findViewById(R.id.pro);
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

        tId = _teacher.get_tId();
        String countrycode = "91";
        int memberID = _teacher.getId();
        String _PhoneCode = String.valueOf(memberID);
        String Key = "member-id";
        _registerListeners();
        //sayali
        // onClick of button perform this simplest code.
        if (email.matches(emailPattern)) {
            if(phone.length() == 10) {
                UpdateProfileFeatureController.getInstance().updateProfileInfo(tId, email, fname, mname, lname, dob, add, city, country, gender, pas, phone, state, _schoolId,
                        countrycode, _PhoneCode, Key, img);
            }else{
                Toast.makeText(getApplicationContext(),"Invalid phone number", Toast.LENGTH_SHORT).show();
            }
        }
        else {
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

                  //  regmodel = UpdateProfileFeatureController.getInstance().getRemodel();
                  //  setTeacherInfo(regmodel);

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

                    if(progressDialog != null)
                        progressDialog.dismiss();

                    _teacher = LoginFeatureController.getInstance().getTeacher();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayBasicInfo(_teacher);
                            _editableFieldsFalse();
                        }
                    });
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

    private void _setFirstNameOnUI(Teacher teacher) {
        final String first = teacher.get_tName();
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
           // if(Lirst == "N/A")
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
           // _pasword.setText(passw);
                for(int i = 1; i<=passw.length();i++) {
                    _pasword.setText(_pasword.getText().toString()+" *");
                }
        }
    }

    private void displayBasicInfo(Teacher teacher) {
        _setFirstNameOnUI(teacher);
        _setMiddleNameOnUI(teacher);
        _setLastNameOnUI(teacher);
        _DobNameOnUI(teacher);
        _GenderNameOnUI(teacher);
        _EmailNameOnUI(teacher);
        _AddrNameOnUI(teacher);
        _cityNameOnUI(teacher);
        _countryNameOnUI(teacher);
        passwNameOnUI(teacher);
        String phone = String.valueOf(teacher.get_tPhone());
        _phone.setText(phone);
        String img = teacher.get_tPC();
        String url1 = WebserviceConstants.IMAGE_BASE_URL + img;
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(url1, _parentImg);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.edt_dob) {
            showDialog(DATE_PICKER_ID);
        }
    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                DatePickerDialog dialog = new DatePickerDialog(this, pickerListener, year, month, day);
                //return new DatePickerDialog(this, pickerListener, year, month, day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dialog;
        }
        return null;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
            Toast.makeText(getApplicationContext(), "To access Camera permission, Please allow in App Settings for camera functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openOptoinDialog();
                } else {

                    final Snackbar snackbar;
                    snackbar = Snackbar.make(_layout, "Permission Denied, You cannot access camera.", Snackbar.LENGTH_LONG);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    snackbar.show();
                }
                break;
        }
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
        Log.i("Image", base64);
        return base64;
    }

    private void openOptoinDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
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
            e.printStackTrace();
        }
        return false;
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }
}
