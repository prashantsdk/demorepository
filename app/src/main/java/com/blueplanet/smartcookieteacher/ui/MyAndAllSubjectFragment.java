package com.blueplanet.smartcookieteacher.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.ui.controllers.TeacherSubjectAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.TeacherSubjectFragmentController;

/**
 * Created by 1311 on 18-09-2016.
 */
public class MyAndAllSubjectFragment extends Fragment {

    private View _view;
    private CustomTextView _subName, divisionName, branchName, semesterName, departmentName;
    private TeacherSubject _TeacherSubject;
    private TeacherSubjectFragmentController _sListFragmentController = null;
    private final String _TAG = this.getClass().getSimpleName();
    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private CustomTextView _tvPleaseWait;
    private ListView _listView;
    private TeacherSubjectAdapter _adapter;
    private CustomButton mysub, allsub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.teacher_subject_list, null);
        _initUI();
        // _sListFragmentController = new TeacherSubjectFragmentController(this, _view);
        // _adapter = new TeacherSubjectAdapter(this);
        _registerUIListeners();
        return _view;
    }

    /**
     * function to do UI initialization tasks
     */
    private void _initUI() {

        _listView = (ListView) _view.findViewById(R.id.lv_subject);
        _rlProgressbar = (RelativeLayout) _view
                .findViewById(R.id.rl_progressBar);
        _progressbar = (ProgressBar) _view.findViewById(R.id.progressbar);
        _tvPleaseWait = (CustomTextView) _view.findViewById(R.id.tv_please_wait);
        mysub = (CustomButton) _view.findViewById(R.id.txtmy);
        allsub = (CustomButton) _view.findViewById(R.id.txtall);
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

    public ListView get_listView() {
        return _listView;
    }

    private void _registerUIListeners() {
        _listView.setAdapter(_adapter);
        _listView.setOnItemClickListener(_sListFragmentController);

        // _studentListView.setOnScrollListener(_sListFragmentController);
        //_studentListView.setOnItemClickListener(_sListFragmentController);


    }

}




