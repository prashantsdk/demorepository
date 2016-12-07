package com.blueplanet.smartcookieteacher.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.ui.controllers.SubjectwiseStudentAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.SubjectwiseStudentFragmentController;



/**
 * Created by 1311 on 05-02-2016.
 */
public class SubjectwiseStudentFragment extends Fragment {

    private View _view;
    private final String _TAG = this.getClass().getSimpleName();

    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private SubjectwiseStudentFragmentController _fragController;
    private TextView _tvPleaseWait;
    private ListView _listView;
    private SubjectwiseStudentAdapter _substudentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.subjectwise_student_list, null);
        _initUI();
        _fragController = new SubjectwiseStudentFragmentController(this, _view);
        _substudentAdapter = new SubjectwiseStudentAdapter(this, _fragController, _view);
        _registerUIListeners();
        return _view;

    }
    private void _initUI() {

        _rlProgressbar = (RelativeLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (TextView) _view.findViewById(R.id.tv_please_wait);
        _listView = (ListView) _view.findViewById(R.id.lv_subjectwise);

    }

    /**
     * function to register UI Listeners
     */
    private void _registerUIListeners() {
        _listView.setAdapter(_substudentAdapter);
        _listView.setOnScrollListener(_fragController);
        _listView.setOnItemClickListener(_fragController);


    }

    public void setVisibilityOfListView(final boolean visibility) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {
                    _listView.setVisibility(View.VISIBLE);
                } else {
                    _listView.setVisibility(View.GONE);
                }
            }
        });
    }

    public void refreshListview() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _substudentAdapter.notifyDataSetChanged();

            }
        });
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
    /**
     * function to hide soft input keyboard
     */
    public void hideSoftKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_view.getWindowToken(), 0);

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
    public void showNoStudentListMessage(final boolean flag) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag == false) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.no_students_available),
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_fragController != null) {
            _fragController.clear();
            _fragController = null;
        }
        if (_substudentAdapter != null) {
            _substudentAdapter = null;

        }


    }
}
