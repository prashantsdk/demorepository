package com.blueplanet.smartcookieteacher.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.blueplanet.smartcookieteacher.ui.controllers.SharePointFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.SofrRewardController;
import com.blueplanet.smartcookieteacher.ui.controllers.SoftRewardAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.TeacherSharePointAdapter;

import java.util.ArrayList;

/**
 * Created by 1311 on 22-01-2017.
 */
public class SoftRewardFragment extends Fragment {
    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private TextView _tvPleaseWait;
    private ListView _teacherListView;
    private SofrRewardController _softRewardContoller = null;

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
    private SoftRewardAdapter _adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.soft_reward_item, null);
        _initUI();
        _softRewardContoller = new SofrRewardController(this, _view);
        _adapter = new SoftRewardAdapter(this,_view);


        _registerUIListeners();
        return _view;

    }

    private void _initUI() {
        _rlProgressbar = (RelativeLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (CustomTextView) _view.findViewById(R.id.tv_please_wait);
        _teacherListView = (ListView) _view.findViewById(R.id.lv_student);
        _txt_teacherName =(TextView) _view.findViewById(R.id.teacherName);
        _etTextSearch = (CustomEditText) _view.findViewById(R.id.etxtSearch);

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
        _teacherListView.setAdapter(_adapter);
        _teacherListView.setOnScrollListener(_softRewardContoller);
        _teacherListView.setOnItemClickListener(_softRewardContoller);
        _etTextSearch.addTextChangedListener(_adapter);

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

    public void showNotEnoughPoint() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.share_point),
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
        if (_softRewardContoller != null) {
            _softRewardContoller.clear();
            _softRewardContoller = null;
        }
        if (_adapter != null) {
            _adapter = null;
        }

    }

}
