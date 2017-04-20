package com.blueplanet.smartcookieteacher.ui.controllers;

import android.view.View;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.communication.ServerResponse;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.featurecontroller.RegistrationFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.RewardPointLogFeatureController;
import com.blueplanet.smartcookieteacher.models.RegisModel;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.notification.EventNotifier;
import com.blueplanet.smartcookieteacher.notification.EventState;
import com.blueplanet.smartcookieteacher.notification.EventTypes;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.notification.ListenerPriority;
import com.blueplanet.smartcookieteacher.notification.NotifierFactory;
import com.blueplanet.smartcookieteacher.ui.NewProfileFragment;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 24-01-2017.
 */
public class NewProfileController implements IEventListener, View.OnClickListener {

    private CustomEditText _firstName, _lastName, _dob, _age, _gender, _qual, _occup, _email, _add, _country, _state, _phone, _pasword, _confirmPas;
    private View _view;
    private NewProfileFragment _profileFragment;
    private RegisModel _register;


    public NewProfileController(NewProfileFragment fragmnt, View v) {
        _profileFragment = fragmnt;
        _view = v;
     /*   _firstName = (CustomEditText) _view.findViewById(R.id.edt_first_name);
        _lastName = (CustomEditText) _view.findViewById(R.id.edt_last_name);
        _dob = (CustomEditText) _view.findViewById(R.id.edt_dob);
        _age = (CustomEditText) _view.findViewById(R.id.edt_age);

        _gender = (CustomEditText) _view.findViewById(R.id.edt_gender);
        _qual = (CustomEditText) _view.findViewById(R.id.edt_qualifi);
        _occup = (CustomEditText) _view.findViewById(R.id.edt_occup);
        _email = (CustomEditText) _view.findViewById(R.id.edt_emaili);
        _add = (CustomEditText) _view.findViewById(R.id.edt_addres);
        _country = (CustomEditText) _view.findViewById(R.id.edt_country);
        _state = (CustomEditText) _view.findViewById(R.id.edt_state);
        _phone = (CustomEditText) _view.findViewById(R.id.edt_phone);
        _pasword = (CustomEditText) _view.findViewById(R.id.edt_pasword);*/
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_update:
                _handleUpdateProfile();
            //    Toast.makeText(_profileFragment.getActivity(), "Update Successfully..!!", Toast.LENGTH_SHORT).show();

            default:
                break;

        }
    }

    private void _editableFieldsTrue() {
        _firstName.setEnabled(true);
        _lastName.setEnabled(true);
        _dob.setEnabled(true);
        _age.setEnabled(true);
        _gender.setEnabled(true);
        _qual.setEnabled(true);
        _occup.setEnabled(true);
        _email.setEnabled(true);
        _add.setEnabled(true);
        _country.setEnabled(true);
        _state.setEnabled(true);
        _phone.setEnabled(true);
        _pasword.setEnabled(true);


    }

    private void _handleUpdateProfile() {


    }

    private boolean _isRewardPopulated(ArrayList<RewardPointLog> list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;

    }
    private void _registerStudentEventListeners() {

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

        if (_profileFragment != null) {
            _profileFragment = null;
        }
    }

    /*private void SaveLoginData() {
        RewardPointLog log=RewardPointLogFeatureController.getInstance().get();

        LoginFeatureController.getInstance().saveUserDataIntoDB(teach);
    }*/


    /**
     * webservice call to fetch reward list from server
     *
     * @param teacherId
     * @param schoolId
     */
    private void _fetchRewardListFromServer(String teacherId, String schoolId) {
        _registerStudentEventListeners();

        RewardPointLogFeatureController.getInstance().getRewardListFromServer(teacherId, schoolId);
       // _profileFragment.showOrHideProgressBar(true);

    }

    @Override
    public int eventNotify(int eventType, Object eventObject) {
        return 0;
    }
}
