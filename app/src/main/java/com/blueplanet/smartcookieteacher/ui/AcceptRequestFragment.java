package com.blueplanet.smartcookieteacher.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.ui.controllers.AcceptRequestAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.AcceptRequestController;

import java.util.ArrayList;

/**
 * Created by Sayali on 8/21/2017.
 */
public class AcceptRequestFragment extends Fragment {

    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private TextView _tvPleaseWait;
    private ListView lv_request;
    private AcceptRequestController _Controller = null;

    private AutoCompleteTextView etxtSearch;
    ArrayList<Student> studentList, filteredList;
    private CustomEditText _etTextSearch;
    private CustomTextView _edtCount;
    private Student _student;
    private final String _TAG = this.getClass().getSimpleName();
    private ArrayList<Student> _studentList = null;
    private View _view;
    private TextView _txt_teacherName;
    private Teacher _teacher;
    private AcceptRequestAdapter _adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.request_request_item, null);
        _initUI();
        getActivity().setTitle("Accept Request");
        _Controller = new AcceptRequestController(this, _view);
        _adapter = new AcceptRequestAdapter(this, _view);


        _registerUIListeners();
        return _view;

    }

    private void _initUI() {
        _rlProgressbar = (RelativeLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (CustomTextView) _view.findViewById(R.id.tv_please_wait);
        lv_request = (ListView) _view.findViewById(R.id.lv_request);
        _txt_teacherName = (TextView) _view.findViewById(R.id.teacherName);


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
        lv_request.setAdapter(_adapter);
        lv_request.setOnScrollListener(_Controller);
        lv_request.setOnItemClickListener(_Controller);


    }

    public void setVisibilityOfListView(final boolean visibility) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {
                    lv_request.setVisibility(View.VISIBLE);
                } else {
                    lv_request.setVisibility(View.GONE);

                }
            }
        });
    }


    public void refreshListview() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (_adapter == null) {
                    // _adapter = new TeacherSharePointAdapter(SharePointFragment.this);
                }
                _adapter.notifyDataSetChanged();


            }
        });
    }

    public void showAcceptRequestPoint() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.Request_Accept),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void pendingRequestForPoints() {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity(), "No Any Pending Request", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDeclineRequestPoint() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.Request_Decline),
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


    public void onDestroy() {

        super.onDestroy();
        if (_Controller != null) {
            _Controller.clear();
            _Controller = null;
        }
        if (_adapter != null) {
            _adapter = null;
        }


    }


}
