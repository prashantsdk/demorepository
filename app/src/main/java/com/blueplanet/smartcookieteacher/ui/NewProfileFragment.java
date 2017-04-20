package com.blueplanet.smartcookieteacher.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.ui.controllers.AddCartAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.AddCartFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.NewProfileController;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by 1311 on 24-01-2017.
 */
public class NewProfileFragment extends Fragment {



    private View _view;
    private CustomEditText _Name, _qulification, _dob, _age, _gender, _qual, _occup, _email, _add, _country, _state, _phone, _pasword, _confirmPas;
    //Parent parentInfo = null;
    Teacher _teacher=null;
   // private ProfileFragmnetController _profileCOntroller;

    private NewProfileController _profileCOntroller;
    private CustomButton _btnUpdate, _btnCancel;
    private FloatingActionButton _btnAction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.new_profile, null);
        _initUI();

        displayBasicInfo();
        _profileCOntroller = new NewProfileController(this, _view);


        _registerClickLIstner();


        return _view;
    }

 /*   @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _view = inflater.inflate(R.layout.add_to_cart_item, null);
        _initUI();
        displayBasicInfo();
        _profileCOntroller = new NewProfileController(this, _view);


        _registerClickLIstner();


    }*/

    /*  @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          _view = inflater.inflate(R.layout.new_profile, null);
          _initUI();
          displayBasicInfo();
          _profileCOntroller = new NewProfileController(this, _view);


          _registerClickLIstner();


          return _view;
      }
  */
    private void _registerClickLIstner() {
       _btnUpdate.setOnClickListener(_profileCOntroller);

     //   _btnAction.setOnClickListener(_profileCOntroller);*/


    }

    private void displayBasicInfo() {
        _teacher = LoginFeatureController.getInstance().getTeacher();
        String first = _teacher.get_tCompleteName();
        String dob = _teacher.get_tDOB();
      //  int  age = _teacher.get_tAge();
        String gender = _teacher.get_tGender();
        String qual = _teacher.get_tQualification();

        String eamil = _teacher.get_tEmail();
        String add = _teacher.get_tAddress();
        String country = _teacher.get_tCountry();
        String state = _teacher.get_tAddress();
       // int phone = _teacher.get_tPhone();
        String pas = _teacher.get_tPassword();
        String img = _teacher.get_tPC();
    //    String url1 = WebserviceConstants.BASE_URL + WebserviceConstants.SMART_COOKIE_IMAGE_URL + img;
        _Name.setText(first);

        _dob.setText(dob);
    //    _age.setText(age);
        _gender.setText(gender);
        _qulification.setText(qual);
        _email.setText(eamil);
     //   _phone.setText(phone);
      /*  _add.setText(add);
        _country.setText(country);
        _state.setText(state);
        _phone.setText(phone);
        _pasword.setText(pas);*/

/*        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url1, teachrImage);*/


    }

    private void _initUI() {
        _Name = (CustomEditText) _view.findViewById(R.id.txt_name);
        _dob = (CustomEditText) _view.findViewById(R.id.Txt_dob);
        _age= (CustomEditText) _view.findViewById(R.id.Txt_age);
        _gender = (CustomEditText) _view.findViewById(R.id.txt_gender);
        _qulification = (CustomEditText) _view.findViewById(R.id.txt_qulification);
        _email= (CustomEditText) _view.findViewById(R.id.txt_email);
        _phone= (CustomEditText) _view.findViewById(R.id.txt_phone);
        _btnUpdate = (CustomButton) _view.findViewById(R.id.btn_update);
       /* _email = (CustomEditText) _view.findViewById(R.id.edt_emaili);
        _add = (CustomEditText) _view.findViewById(R.id.edt_addres);
        _country = (CustomEditText) _view.findViewById(R.id.edt_country);
        _state = (CustomEditText) _view.findViewById(R.id.edt_state);
        _phone = (CustomEditText) _view.findViewById(R.id.edt_phone);
        _pasword = (CustomEditText) _view.findViewById(R.id.edt_pasword);
        _btnUpdate = (CustomButton) _view.findViewById(R.id.btn_update);
        _btnAction = (FloatingActionButton) _view.findViewById(R.id.fab_editable);*/

        //_confirmPas = (CustomEditText) _view.findViewById(R.id.edt_password);
    }
}
