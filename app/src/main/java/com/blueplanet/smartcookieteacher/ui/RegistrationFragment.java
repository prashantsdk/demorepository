package com.blueplanet.smartcookieteacher.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.ui.controllers.LoginFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.RegistrationFragmentController;

/**
 * Created by 1311 on 17-02-2016.
 */
public class RegistrationFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private View _view;
    private CustomEditText edtfirst, edtlast, edtplone, _edtEmail, _edtPassword, txtphone, txtmiddlename;
    private CustomButton _btnRegister, mClearData;
    private RegistrationFragmentController _fragController = null;
    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private CustomTextView _tvPleaseWait, _tvMemberLogin;
    private Spinner spinner, spinnerPhone;
    String[] userOption = {"Select Login Type", "Email", "Mobile-No", "EmployeeID", "MemberID"};
    String[] numberOptn = {"+91", "+1"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.registration, null);
        _initUI();
        _fragController = new RegistrationFragmentController(this, _view);
        _registerUIListner();
        ArrayAdapter phone = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, numberOptn);
        phone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhone.setAdapter(phone);
        return _view;
    }

    private void _initUI() {
        edtfirst = (CustomEditText) _view.findViewById(R.id.edt_firstname);
        edtlast = (CustomEditText) _view.findViewById(R.id.edt_lastName);
        _edtEmail = (CustomEditText) _view.findViewById(R.id.edt_emailId);
        _edtPassword = (CustomEditText) _view.findViewById(R.id.edt_password);
        txtmiddlename = (CustomEditText) _view.findViewById(R.id.edt_middleName);


        edtplone = (CustomEditText) _view.findViewById(R.id.edtPhone);
        _btnRegister = (CustomButton) _view.findViewById(R.id.btn_register);

        mClearData = _view.findViewById(R.id.btn_clear);
        _rlProgressbar = (RelativeLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (CustomTextView) _view.findViewById(R.id.tv_please_wait);
        _tvMemberLogin = (CustomTextView) _view.findViewById(R.id.tv_member_login);
        spinnerPhone = (Spinner) _view.findViewById(R.id.spinnerPhone);
    }

    private void _registerUIListner() {
        _btnRegister.setOnClickListener(_fragController);
        mClearData.setOnClickListener(_fragController);
        _tvMemberLogin.setOnClickListener(_fragController);
        spinnerPhone.setOnItemSelectedListener(this);

    }

    public void showNetworkToast(final boolean isNetworkAvailable) {
        getActivity().runOnUiThread(new Runnable() {
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

    /**
     * function to hide soft input keyboard
     */
    public void hideSoftKeyboard() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(_view.getWindowToken(), 0);

            }
        });

    }

    public void invalidinputMessage(final boolean flag) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag == false) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.invalid_input),
                            Toast.LENGTH_LONG).show();
                }
            }
        });


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
                    _rlProgressbar.setVisibility(View.VISIBLE);
                    _progressbar.setVisibility(View.VISIBLE);
                    _tvPleaseWait.setVisibility(View.VISIBLE);
                } else {
                    _rlProgressbar.setVisibility(View.GONE);
                    _progressbar.setVisibility(View.GONE);
                    _tvPleaseWait.setVisibility(View.GONE);


                }
            }
        });

    }

    public void showTypePhone(final int position) {

        spinnerPhone.setSelection(position);
        spinnerPhone.setSelection(position);
        String selState = (String) spinner.getSelectedItem();
        LoginFeatureController.getInstance().set_phoneNo(selState);

        if (selState.equalsIgnoreCase("+91")) {


            // LoginFeatureController.getInstance().setUserEmailType(true);
        } else if (selState.equalsIgnoreCase("+1")) {

            //   LoginFeatureController.getInstance().setUserEmailType(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_fragController != null) {
            _fragController.clear();
            _fragController = null;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
