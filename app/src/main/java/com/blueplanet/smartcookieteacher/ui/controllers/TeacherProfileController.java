package com.blueplanet.smartcookieteacher.ui.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragment;
import com.blueplanet.smartcookieteacher.ui.StudentDetailFragment;

/**
 * Created by 1311 on 01-12-2016.
 */
public class TeacherProfileController implements View.OnClickListener, IEventListener, AdapterView.OnItemSelectedListener {

    private StudentDetailFragment _detailFragment;
    private View _view;
    private Teacher _teacher;
    private String _teacherId, _schoolId;

    public TeacherProfileController(StudentDetailFragment detailFragment, View view) {

        _detailFragment = detailFragment;
        _view = view;
        //_teacher = LoginFeatureController.getInstance().getTeacher();



    }

    public void close() {
        if (_detailFragment != null) {
            _detailFragment = null;
        }
    }

    @Override
    public void onClick(final View view) {

        int id = view.getId();
        switch (id) {
            case R.id.btn_assignPoints:
                _loadFragment(R.id.content_frame, new AssignPointFragment());


                break;

            default:
                break;

        }

    }

    private void _loadFragment(int id, Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = _detailFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack("StudentDetailFragment");
        _detailFragment.getActivity().setTitle("Assign Point");
        ft.commitAllowingStateLoss();
    }

    @Override
    public int eventNotify(int eventType, Object eventObject) {
        return 0;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
