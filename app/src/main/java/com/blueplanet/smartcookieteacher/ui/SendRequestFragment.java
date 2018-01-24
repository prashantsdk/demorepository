package com.blueplanet.smartcookieteacher.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.AssignPointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SendRequestFeatureController;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.ui.controllers.SendRequestController;
import com.blueplanet.smartcookieteacher.ui.controllers.SharePointFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.TeacherSharePointAdapter;

import java.util.ArrayList;

/**
 * Created by Sayali on 8/29/2017.
 */
public class SendRequestFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private TextView _tvPleaseWait;
    private ListView _teacherListView;
    private SendRequestController _sendRequestController = null;

    private AutoCompleteTextView etxtSearch;
    ArrayList<Student> studentList, filteredList;
    private CustomEditText _first_name,_middleName,_lastName_,_Email,_phone,_selectTS;
    private CustomButton _btnsend,_btncansel;
    private Student _student;
    private final String _TAG = this.getClass().getSimpleName();
    private ArrayList<Student> _studentList = null;
    private View _view;
    private CustomTextView _txt_toastMsg;
    private Teacher _teacher;
    private Spinner spinner, spinnerPhone;
    String[] userOption = {"Teacher","Student"};
    String[] numberOptn = {"+91", "+1"};
    private String selState, str;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.send_request, null);
        _initUI();
        _sendRequestController = new SendRequestController(this, _view);
        _registerUIListeners();

        ArrayAdapter phone = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, numberOptn);
        phone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhone.setAdapter(phone);

        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, userOption);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);


        return _view;

    }

    private void _initUI() {
        _rlProgressbar = (RelativeLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (CustomTextView) _view.findViewById(R.id.tv_please_wait);
        _first_name = (CustomEditText) _view.findViewById(R.id.edt_first_name);
        _middleName = (CustomEditText) _view.findViewById(R.id.edt_middle_name);
        _lastName_ = (CustomEditText) _view.findViewById(R.id.edt_last);
        _Email = (CustomEditText) _view.findViewById(R.id.edt_email);
        _phone = (CustomEditText) _view.findViewById(R.id.edt_phone);
        _btnsend=(CustomButton) _view.findViewById(R.id.btn_send);
        _btncansel=(CustomButton) _view.findViewById(R.id.btn_cancel);
        spinnerPhone = (Spinner) _view.findViewById(R.id.spinnerPhone);
        spinner = (Spinner) _view.findViewById(R.id.spinner);
        _txt_toastMsg=(CustomTextView)_view.findViewById(R.id.toast_msg);

    }

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

    private void _registerUIListeners() {

        _btnsend.setOnClickListener(_sendRequestController);
        _btncansel.setOnClickListener(_sendRequestController);

    }

    public void setVisibilityOfListView(final boolean visibility) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {
                    _teacherListView.setVisibility(View.VISIBLE);

                } else {
                    _teacherListView.setVisibility(View.GONE);

                }
            }
        });
    }


    public void sendRequestPoint() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.Request_send),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
    public void sendRequestProblem() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.Request_send_problem),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void sendRequestAlredyExist() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.Request_send_already_exists),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
    public void sendRequestInvalidinput() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.Request_send_invalid_input),
                        Toast.LENGTH_LONG).show();
            }
        });

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

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_view.getWindowToken(), 0);

    }

    public void showType(int position) {
        spinner.setSelection(position);
        spinner.setSelection(position);
        selState = spinner.getSelectedItem().toString();

        SendRequestFeatureController.getInstance().set_selectColor(selState);


        Log.i(_TAG, "In selected item" + selState);

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner:
                this.showType(position);
                break;

            default:
                break;
        }
    }

    public void onDestroy() {

        super.onDestroy();
        if (_sendRequestController != null) {
            _sendRequestController.clear();
            _sendRequestController = null;
        }


    }


}
