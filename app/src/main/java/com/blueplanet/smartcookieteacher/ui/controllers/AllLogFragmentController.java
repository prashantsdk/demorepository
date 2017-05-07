package com.blueplanet.smartcookieteacher.ui.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.models.Teacher;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.notification.IEventListener;
import com.blueplanet.smartcookieteacher.ui.AdminFragment;
import com.blueplanet.smartcookieteacher.ui.AllLogFragment;
import com.blueplanet.smartcookieteacher.ui.BluePointFragment;
import com.blueplanet.smartcookieteacher.ui.BuyCouponLogFragment;
import com.blueplanet.smartcookieteacher.ui.GenerateCoupFragment;
import com.blueplanet.smartcookieteacher.ui.RewardPointFragment;

/**
 * Created by 1311 on 08-08-2016.
 */
public class AllLogFragmentController implements IEventListener, View.OnClickListener {

    private AllLogFragment _Fragment;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private String _teacherId, _schoolId;
    private Teacher _teacher;
    private Button _btnPoint, _btnRewardPoint, _btnallStudent, _btnBuyPoint;

    /**
     * constructor
     *
     * @param
     * @param view
     */
    public AllLogFragmentController(AllLogFragment Fragment, View view) {

        _Fragment = Fragment;
        _view = view;

        if ((NetworkManager.isNetworkAvailable()) == false) {
            _Fragment.showNetworkToast(false);
        }
    }

    public void clear() {

        if (_Fragment != null) {
            _Fragment = null;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btnReward:
                // DashboardFeatureController.getInstance().clearTeacherpoint();
                _loadFragment(R.id.content_frame, new RewardPointFragment());

                break;

            case R.id.btnThanQ:
                // DashboardFeatureController.getInstance().clearTeacherpoint();
                _loadFragment(R.id.content_frame, new BluePointFragment());

                break;

            case R.id.btngenerate:
                // DashboardFeatureController.getInstance().clearTeacherpoint();
                _loadFragment(R.id.content_frame, new GenerateCoupFragment());

                break;

            case R.id.btnAdminThanq:
                // DashboardFeatureController.getInstance().clearTeacherpoint();
                _loadFragment(R.id.content_frame, new AdminFragment());

                break;
            case R.id.btnBuy:
                // DashboardFeatureController.getInstance().clearTeacherpoint();
                _loadFragment(R.id.content_frame, new BuyCouponLogFragment());

                break;
            default:
                break;
        }
    }

    private void _loadFragment(int id, Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = _Fragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack("StudentDetailFragment");
        _Fragment.getActivity().setTitle("Reward Point Log");
        ft.commitAllowingStateLoss();
    }

    @Override
    public int eventNotify(int eventType, Object eventObject) {
        return 0;
    }
}
