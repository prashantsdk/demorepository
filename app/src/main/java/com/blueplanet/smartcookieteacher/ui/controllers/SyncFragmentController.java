package com.blueplanet.smartcookieteacher.ui.controllers;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.BluePointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DashboardFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.LoginFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.RewardPointLogFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.ui.LoginFragment;
import com.blueplanet.smartcookieteacher.ui.SyncFragment;

/**
 * Created by 1311 on 21-04-2016.
 */
public class SyncFragmentController implements View.OnClickListener {

    private SyncFragment _syncFragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private String _teacherId, _schoolId;
    private Teacher _teacher;
    private Button _btnPoint, _btnRewardPoint,_btnallStudent,_btnthankuPoint;

    /**
     * constructor
     *
     * @param
     * @param view
     */
    public SyncFragmentController(SyncFragment syncFragment, View view) {

        _syncFragment = syncFragment;
        _view = view;

        if ((NetworkManager.isNetworkAvailable()) == false) {
            _syncFragment.showNetworkToast(false);
        }
    }

    public void clear() {

        if (_syncFragment != null) {
            _syncFragment = null;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btnPointSyn:
                DashboardFeatureController.getInstance().clearTeacherpoint();
                _btnPoint = (Button) _view.findViewById(R.id.btnPointSyn);
                _teacher = LoginFeatureController.getInstance().getTeacher();

                if (_teacher != null && NetworkManager.isNetworkAvailable()) {
                    _teacherId = _teacher.get_tId();
                    _schoolId = _teacher.get_tSchool_id();
                    DashboardFeatureController.getInstance().fetchTeacherPointFromServer(_teacherId);

                   // _btnPoint.setBackgroundResource(R.color.cl_primary);
                    Toast.makeText(MainApplication.getContext(),
                            "Sync Successfully..!!",
                            Toast.LENGTH_SHORT).show();
                }
            case R.id.btnRewardSyn:


                RewardPointLogFeatureController.getInstance().clearRewardPointList();
              //  RewardPointLogFeatureController.getInstance().clearRewardPointList();
                _btnRewardPoint=(Button)_view.findViewById(R.id.btnRewardSyn);
                _teacher = LoginFeatureController.getInstance().getTeacher();
                if (_teacher != null) {
                    _teacherId = _teacher.get_tId();
                    _schoolId = _teacher.get_tSchool_id();
                    RewardPointLogFeatureController.getInstance().getRewardListFromServer(_teacherId, _schoolId);

                   // _btnRewardPoint.setBackgroundResource(R.color.cl_primary);
                    Toast.makeText(MainApplication.getContext(),
                            "Sync Successfully..!!",

                            Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.btnAllStudent:


                StudentFeatureController.getInstance().clearStudentList();
                _btnallStudent=(Button)_view.findViewById(R.id.btnAllStudent);
                _teacher = LoginFeatureController.getInstance().getTeacher();
                int id1 = StudentFeatureController.getInstance().getLastInputId();

                if (_teacher != null) {
                    _teacherId = _teacher.get_tId();
                    _schoolId = _teacher.get_tSchool_id();
                    StudentFeatureController.getInstance().getStudentListFromServer(_teacherId, _schoolId, id1);

                   // _btnallStudent.setBackgroundResource(R.color.cl_primary);
                    Toast.makeText(MainApplication.getContext(),
                            "Sync Successfully..!!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnMythankuSyn:


                BluePointFeatureController.getInstance().clearRewardPointList();
                //  RewardPointLogFeatureController.getInstance().clearRewardPointList();
                _btnthankuPoint=(Button)_view.findViewById(R.id.btnMythankuSyn);
                _teacher = LoginFeatureController.getInstance().getTeacher();
                if (_teacher != null) {
                    _teacherId = _teacher.get_tId();
                    _schoolId = _teacher.get_tSchool_id();
                    BluePointFeatureController.getInstance().getBluePointFromServer(_teacherId, _schoolId);

                    // _btnRewardPoint.setBackgroundResource(R.color.cl_primary);
                    Toast.makeText(MainApplication.getContext(),
                            "Sync Successfully..!!",

                            Toast.LENGTH_SHORT).show();

                }
                break;

            default:
                break;


    }}
}
