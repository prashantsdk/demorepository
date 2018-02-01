package com.blueplanet.smartcookieteacher.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.ui.controllers.AllSubjectFragmentControlle;
import com.blueplanet.smartcookieteacher.ui.controllers.TeacherAllSubAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.TeacherSubjectAdapter;
import com.blueplanet.smartcookieteacher.ui.controllers.TeacherSubjectFragmentController;

/**
 * Created by 1311 on 16-09-2016.
 */
public class AllSubjectFragment extends Fragment {

    private View _view;
    private CustomTextView _subName, divisionName, branchName, semesterName, departmentName;
    private TeacherSubject _TeacherSubject;
    private AllSubjectFragmentControlle _sListFragmentController = null;
    private final String _TAG = this.getClass().getSimpleName();
    private RelativeLayout _rlProgressbar;
    private ProgressBar _progressbar;
    private CustomTextView _tvPleaseWait;
    private ListView _listView;
    private TeacherAllSubAdapter _adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.all_sub, null);
        _initUI();
        getActivity().setTitle("All Subjects");
        _sListFragmentController = new AllSubjectFragmentControlle(this, _view);
        _adapter = new TeacherAllSubAdapter(this);
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

    public void refreshListview() {
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (_adapter == null) {
                            _adapter = new TeacherAllSubAdapter(AllSubjectFragment.this);
                        }
                        _adapter.notifyDataSetChanged();


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

    public void onDestroy() {
        super.onDestroy();
        if (_sListFragmentController != null) {

            _sListFragmentController.close();
            _sListFragmentController = null;
        }
        if (_adapter != null) {
            _adapter = null;

        }

    }



}
