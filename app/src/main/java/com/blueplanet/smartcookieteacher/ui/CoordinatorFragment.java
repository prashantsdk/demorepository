package com.blueplanet.smartcookieteacher.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.blueplanet.smartcookieteacher.featurecontroller.CoordinatorFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.CoordinatorModel;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.ui.controllers.CoordinatorAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.CoordinatorFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.StudentListAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.StudentListFragmentController;

import java.util.ArrayList;

/**
 * Created by 1311 on 08-08-2016.
 */
public class CoordinatorFragment extends Fragment  {

    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private TextView _tvPleaseWait;
    private ListView _studentListView;
    private CoordinatorFragmentController _fagmentController = null;
    private CoordinatorAdapter _Adapter;
    private AutoCompleteTextView etxtSearch;
    ArrayList<Student> studentList, filteredList;
    private CustomEditText _etTextSearch;
    private CustomTextView _edtCount;
    private Student _student;
    private final String _TAG = this.getClass().getSimpleName();
    private ArrayList<CoordinatorModel> _studentList = null;
    private View _view;
    private TextView _txt_teacherName;
    private Teacher _teacher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.coordinator_layout, null);
        _initUI();
        _fagmentController = new CoordinatorFragmentController(this, _view);
        _Adapter = new CoordinatorAdapter(this,_fagmentController,_view);
       // _showDataOnUI();
        _setTeacherNameOnUI();
        _registerUIListeners();
        return _view;

    }

    private void _initUI() {
        _studentListView = (ListView) _view.findViewById(R.id.lv_student);

        _rlProgressbar = (RelativeLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (TextView) _view.findViewById(R.id.tv_please_wait);
        //   etxtSearch = (AutoCompleteTextView) _view.findViewById(R.id.etxtSearch);
        _etTextSearch = (CustomEditText) _view.findViewById(R.id.etxtSearch);
        _edtCount = (CustomTextView) _view.findViewById(R.id.count);
        _txt_teacherName =(TextView) _view.findViewById(R.id.teacherName);

    }

    public void setVisibilityOfListView(final boolean visibility) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {
                    _studentListView.setVisibility(View.VISIBLE);
                } else {
                    _studentListView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void _setTeacherNameOnUI(){


        _teacher = LoginFeatureController.getInstance().getTeacher();
        if (_teacher != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    _txt_teacherName.setText(_teacher.get_tCompleteName()+" - "+_teacher.get_tId());


                }
            });
        }
    }



    public ListView getListview() {

        return _studentListView;
    }

    /**
     * function to register UI Listeners
     */
    private void _registerUIListeners() {
        _studentListView.setAdapter(_Adapter);
       // _studentListView.setOnScrollListener(_fagmentController);
        _studentListView.setOnItemClickListener(_fagmentController);
        _etTextSearch.addTextChangedListener(_Adapter);





    }

    public void refreshListview() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                _Adapter.notifyDataSetChanged();
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

    private void _showDataOnUI() {

        _studentList = CoordinatorFeatureController.getInstance().getStudentList();
        if (_studentList != null && _studentList.size() > 0) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int totalCount = _studentList.get(0).getTotalCount();

                    _edtCount.setText(String.valueOf(totalCount));
                    Log.i(_TAG, "Total Count" + _edtCount);


                }
            });
        }

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

    public void onDestroy() {
        super.onDestroy();
        //   StudentFeatureController.getInstance().clearStudentList();
        if (_fagmentController != null) {
            _fagmentController.clear();
            _fagmentController = null;
        }
        if (_Adapter != null) {
            _Adapter.close();
            _Adapter = null;

        }

        CoordinatorFeatureController.getInstance().clearFilterList();
    }
}
