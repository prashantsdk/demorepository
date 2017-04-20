package com.blueplanet.smartcookieteacher.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.ui.controllers.StudentListFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.StudentProfileFragmentController;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;


/**
 * Created by 1311 on 30-01-2016.
 */
public class StudentDetailFragment extends Fragment {

    private TextView _gender, _dob, _age, _schoolName, _division, _hobbies;
    private TextView _address, _city, _country;
    private TextView _stdName, _stdPRN, _claSsName;
    private ImageView iv_studentPhoto;
    private View _view;
    private CustomButton _btnAssign,_btnactivitywise,_btnsubjectwise;
    private StudentProfileFragmentController _controller;
    private Student _student;
    private final String _TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.student_profile1, null);
        _initUI();
        _controller = new StudentProfileFragmentController(this, _view);
        _registerUIListeners();
        _setStudentDetailsOnUI();

        return _view;

    }

    private void _initUI() {
        iv_studentPhoto = (ImageView) _view.findViewById(R.id.iv_studentPhoto);
        _stdName = (TextView) _view.findViewById(R.id.txt_firstname);
        _schoolName = (TextView) _view.findViewById(R.id.txt_colgtname);
        _stdPRN = (TextView) _view.findViewById(R.id.txt_prn);
        _gender = (TextView) _view.findViewById(R.id.txt_gender);
        _dob = (TextView) _view.findViewById(R.id.txt_dob);
        _age = (TextView) _view.findViewById(R.id.txt_Age);

        _claSsName = (TextView) _view.findViewById(R.id.txt_className);
        _division = (TextView) _view.findViewById(R.id.txt_div);

        _hobbies = (TextView) _view.findViewById(R.id.txt_hobbies);

        _address = (TextView) _view.findViewById(R.id.txt_Address);
        _city = (TextView) _view.findViewById(R.id.txt_city);
        _country = (TextView) _view.findViewById(R.id.txt_country);
        _btnAssign = (CustomButton) _view.findViewById(R.id.btn_assignPoints);
        _btnactivitywise= (CustomButton) _view.findViewById(R.id.btn_activitywise);
        _btnsubjectwise = (CustomButton) _view.findViewById(R.id.btn_suvbjecwise);
    }

    private void _setStudentDetailsOnUI() {

        _student = StudentFeatureController.getInstance().getSelectedStudent();

        if (_student != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    _setStudNameOnUI(_student);
                    _setSchoolNameOnUI(_student);
                    _setStdPRNOnUI(_student);
                    _setGenderOnUI(_student);
                    _setDOBOnUI(_student);
                    _setDivisionOnUI(_student);
                    _setAgeOnUI(_student);
                    _setHobbiesOnUI(_student);
                    _setAddressOnUI(_student);
                    _setCityOnUI(_student);
                    _setCountryOnUI(_student);
                    _setClassNameOnUI(_student);

                    String timage = _student.get_stdImageUrl();

                    if (timage != null && timage.length() > 0) {

                        final String imageName = com.blueplanet.smartcookieteacher.webservices.WebserviceConstants.IMAGE_BASE_URL + timage;
                        Log.i(_TAG, imageName);

                        SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, iv_studentPhoto,
                                IImageLoader.NORMAL_POSTER);

                        SmartCookieImageLoader.getInstance().display();

                    }

                }
            });

        }

    }

    private void _registerUIListeners() {

        _btnAssign.setOnClickListener(_controller);
        _btnactivitywise.setOnClickListener(_controller);
        _btnsubjectwise.setOnClickListener(_controller);
    }

    private void _setClassNameOnUI(Student student) {
        String className = student.get_stdClass();
        if (!(TextUtils.isEmpty(className)) && className.equalsIgnoreCase("null")) {
            _claSsName.setText("N/A");
        } else {
            _claSsName.setText(className);
        }
    }

    private void _setCountryOnUI(Student student) {
        String country = student.get_stdCountry();
        if (!(TextUtils.isEmpty(country)) && country.equalsIgnoreCase("null")) {
            _country.setText("N/A");
        } else {
            _country.setText(country);
        }
    }

    private void _setCityOnUI(Student student) {
        String city = student.get_stdCity();
        if (!(TextUtils.isEmpty(city)) && city.equalsIgnoreCase("null") && city.length() == 0) {
            _city.setText("N/A");
        } else {
            _city.setText(city);
        }
    }

    private void _setAddressOnUI(Student student) {
        String y = student.get_stdAddress().toString();
        if (!(TextUtils.isEmpty(y)) && y.equalsIgnoreCase("null")) {
            _address.setText("N/A");
        } else {
            _address.setText(y);

        }
    }

    private void _setHobbiesOnUI(Student student) {
        String x = student.get_stdHobbies().toString();
        if (!(TextUtils.isEmpty(x)) && x.equalsIgnoreCase("null")) {
            _hobbies.setText("N/A");
        } else {
            _hobbies.setText(x);
        }

    }

    private void _setAgeOnUI(Student student) {
        String age = student.get_stdAge().toString();
        if (!TextUtils.isEmpty(age) && age.equalsIgnoreCase("0")) {
            _age.setText("N/A");
        } else {
            _age.setText(age);
        }


    }

    private void _setDivisionOnUI(Student student) {

        String div = student.get_stdDiv();
        if (!(TextUtils.isEmpty(div)) && div.equalsIgnoreCase("null")) {
            _division.setText("N/A");
        } else {
            _division.setText(div);

        }
    }

    private void _setDOBOnUI(Student student) {

        String dob = student.get_stdDOB();
        if (!(TextUtils.isEmpty(dob)) && dob.equalsIgnoreCase("null")) {
            _dob.setText("N/A");
        } else {
            _dob.setText(dob);

        }
    }

    private void _setGenderOnUI(Student student) {
        String gender = student.get_stdGender();
        if (!(TextUtils.isEmpty(gender)) && gender.equalsIgnoreCase("null")) {
            _gender.setText("N/A");
        } else {
            _gender.setText(gender);

        }
    }

    private void _setStdPRNOnUI(Student student) {
        String prn = student.get_stdPRN();
        if (!(TextUtils.isEmpty(prn)) && prn.equalsIgnoreCase("null")) {
            _stdPRN.setText("N/A");
        } else {
            _stdPRN.setText(prn);

        }
    }

    private void _setStudNameOnUI(Student student) {
        String name = student.get_stdName();
        if (!(TextUtils.isEmpty(name)) && name.equalsIgnoreCase("null")) {
            _stdName.setText("N/A");
        } else {
            _stdName.setText(name);

        }
    }

    private void _setSchoolNameOnUI(Student student) {
        String name = student.get_stdSchoolName();
        if (!(TextUtils.isEmpty(name)) && name.equalsIgnoreCase("null")) {
            _schoolName.setText("N/A");
        } else {
            _schoolName.setText(name);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        StudentFeatureController.getInstance().setSelectedStudent(null);
        if (_controller != null) {
            _controller.close();

            _controller = null;
        }
    }
}
