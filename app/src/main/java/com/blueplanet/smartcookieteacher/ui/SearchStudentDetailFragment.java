package com.blueplanet.smartcookieteacher.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.featurecontroller.SearchStudentFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.SearchStudent;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.ui.controllers.SearchStudentDetailFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.StudentProfileFragmentController;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;


/**
 * Created by Priyanka on 15-03-2018.
 */
public class SearchStudentDetailFragment extends Fragment {

    private TextView _gender, _dob, _age, _schoolName, _division, _hobbies,_subject, _subjectText, _txtemail,_txtphone;
    private TextView _address, _city, _country;
    private TextView _stdName, _stdPRN, _claSsName;
    private ImageView iv_studentPhoto;
    private View _view;
    private CustomButton _btnAssign,_btnactivitywise,_btnsubjectwise;
    private SearchStudentDetailFragmentController _controller;
    private SearchStudent _student;
    private final String _TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.student_profile1, null);
        _initUI();
        _controller = new SearchStudentDetailFragmentController(this, _view);
        _registerUIListeners();
        _setStudentDetailsOnUI();
        return _view;
    }

    private void _initUI() {
        iv_studentPhoto = _view.findViewById(R.id.iv_studentPhoto);
        _stdName =  _view.findViewById(R.id.txt_firstname);
        _schoolName =  _view.findViewById(R.id.txt_colgtname);
        _stdPRN =  _view.findViewById(R.id.txt_prn);
        _gender =  _view.findViewById(R.id.txt_gender);
        _dob =  _view.findViewById(R.id.txt_dob);
        _age =  _view.findViewById(R.id.txt_Age);
        _claSsName =  _view.findViewById(R.id.txt_className);
        _division =  _view.findViewById(R.id.txt_div);
        _hobbies =  _view.findViewById(R.id.txt_hobbies);
        _address =  _view.findViewById(R.id.txt_Address);
        _city =  _view.findViewById(R.id.txt_city);
        _country =  _view.findViewById(R.id.txt_country);
        _btnAssign =  _view.findViewById(R.id.btn_assignPoints);
        _btnactivitywise=  _view.findViewById(R.id.btn_activitywise);
        _btnsubjectwise =  _view.findViewById(R.id.btn_suvbjecwise);
        _subject =  _view.findViewById(R.id.txt_sub);
        _txtemail=  _view.findViewById(R.id.txt_email);
        _txtphone=  _view.findViewById(R.id.txt_mobile);
        _subjectText=  _view.findViewById(R.id.txtsub);

        _subject.setVisibility(View.GONE);
        _subjectText.setVisibility(View.GONE);
    }

    private void _setStudentDetailsOnUI() {

        _student = SearchStudentFeatureController.getInstance().get_selectedSearchStudent();

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
                  //  _setsubjectNameOnUI(_student);
                    _setemailNameOnUI(_student);
                    String timage = _student.get_searchimg();

                    if (timage != null && timage.length() > 0) {

                        final String imageName =  timage;
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

    private void _setClassNameOnUI(SearchStudent student) {
        String className = student.get_stdClass();
        if (!(TextUtils.isEmpty(className)) && className.equalsIgnoreCase("null")) {
            _claSsName.setText("N/A");
        } else {
            _claSsName.setText(className);
        }
    }

    private void _setCountryOnUI(SearchStudent student) {
        String country = student.get_stdCountry();
        if (!(TextUtils.isEmpty(country)) && country.equalsIgnoreCase("null")) {
            _country.setText("N/A");
        } else {
            _country.setText(country);
        }
    }

    private void _setCityOnUI(SearchStudent student) {
        String city = student.get_stdCity();
        if (!(TextUtils.isEmpty(city)) && city.equalsIgnoreCase("null") && city.length() == 0) {
            _city.setText("N/A");
        } else {
            _city.setText(city);
        }
    }

    private void _setAddressOnUI(SearchStudent student) {
        String y = student.get_stdAddress().toString();
        if (!(TextUtils.isEmpty(y)) && y.equalsIgnoreCase("null")) {
            _address.setText("N/A");
        } else {
            _address.setText(y);

        }
    }

    private void _setHobbiesOnUI(SearchStudent student) {
        String x = student.get_stdHobbies().toString();
        if (!(TextUtils.isEmpty(x)) && x.equalsIgnoreCase("null")) {
            _hobbies.setText("N/A");
        } else {
            _hobbies.setText(x);
        }

    }

    private void _setAgeOnUI(SearchStudent student) {
        String age = student.get_stdAge().toString();
        if (!TextUtils.isEmpty(age) && age.equalsIgnoreCase("0")) {
            _age.setText("N/A");
        } else {
            _age.setText(age);
        }


    }

    private void _setDivisionOnUI(SearchStudent student) {

        String div = student.get_stdDiv();
        if (!(TextUtils.isEmpty(div)) && div.equalsIgnoreCase("null")) {
            _division.setText("N/A");
        } else {
            _division.setText(div);

        }
    }

    private void _setDOBOnUI(SearchStudent student) {

        String dob = student.get_stdDOB();
        if (!(TextUtils.isEmpty(dob)) && dob.equalsIgnoreCase("null")) {
            _dob.setText("N/A");
        } else {
            _dob.setText(dob);

        }
    }

    private void _setGenderOnUI(SearchStudent student) {
        String gender = student.get_stdGender();
        if (!(TextUtils.isEmpty(gender)) && gender.equalsIgnoreCase("null")) {
            _gender.setText("N/A");
        } else {
            _gender.setText(gender);

        }
    }

    private void _setStdPRNOnUI(SearchStudent student) {
        String prn = student.get_searchPrn();
        if (!(TextUtils.isEmpty(prn)) && prn.equalsIgnoreCase("null")) {
            _stdPRN.setText("N/A");
        } else {
            _stdPRN.setText(prn);

        }
    }

    private void _setStudNameOnUI(SearchStudent student) {
        String name = student.get_studentname();
        if (!(TextUtils.isEmpty(name)) && name.equalsIgnoreCase("null")) {
            _stdName.setText("N/A");
        } else {
            _stdName.setText(name);

        }
    }

    private void _setSchoolNameOnUI(SearchStudent student) {
        String name = student.get_stdSchoolName();
        if (!(TextUtils.isEmpty(name)) && name.equalsIgnoreCase("null")) {
            _schoolName.setText("N/A");
        } else {
            _schoolName.setText(name);

        }

    }
   /* private void _setsubjectNameOnUI(SearchStudent student) {
        String subject = student.get_stdsubname();
        if (!(TextUtils.isEmpty(subject)) && subject.equalsIgnoreCase("null")) {
            _subject.setText("N/A");
        } else {
            _subject.setText(subject);

        }
    }*/
    private void _setemailNameOnUI(SearchStudent student) {
        String email = student.get_stdEmail();
        if (!(TextUtils.isEmpty(email)) && email.equalsIgnoreCase("null")) {
            _txtemail.setText("N/A");
        } else {
            _txtemail.setText(email);

        }}
/*    private void _setephoneNameOnUI(Student student) {
        String email = student.get();
        if (!(TextUtils.isEmpty(email)) && email.equalsIgnoreCase("null")) {
            _txtemail.setText("N/A");
        } else {
            _txtemail.setText(email);

        }

    }*/

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
