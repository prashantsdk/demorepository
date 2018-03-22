package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.GenerateCouponFeatureController;
import com.blueplanet.smartcookieteacher.models.GenerateCoupon;
import com.blueplanet.smartcookieteacher.ui.CouponRedeemFragment;
import com.blueplanet.smartcookieteacher.ui.GenerateCouponFragment;

import java.util.ArrayList;

/**
 * Created by 1311 on 29-02-2016.
 */
public class GenerateCouponAdapter extends BaseAdapter {

    private GenerateCouponFragment _genFragment;
    private GenerateCouponFragmentController _genCouponController;
    private ArrayList<GenerateCoupon> _genCouList;
    private final String _TAG = this.getClass().getSimpleName();
    private TextView _txtcoupName, _txtcouPoint, txtiCoupDate;
    private ImageView coupImg;
    private TextView _txtiessueDate, txtvalidityDate;

    private CustomButton _btnredeem;

    public GenerateCouponAdapter(GenerateCouponFragment genFragment,
                                 GenerateCouponFragmentController genCouponController,
                                 View _view) {
        _genFragment = genFragment;
        _genCouponController = genCouponController;
      //  _genCouList = GenerateCouponFeatureController.getInstance().get_genCouList();
        _genCouList = GenerateCouponFeatureController.getInstance().getServerCouponList();

    }

    @Override
    public int getCount() {
        if (_genCoupListPopulated(_genCouList)) {
            return _genCouList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication
                    .getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflatorInflater.inflate(R.layout.gen_coupon_item, null
            );
        }
        if (convertView != null) {
            if (_genCoupListPopulated(_genCouList)) {

                _txtcoupName = (TextView) convertView.findViewById(R.id.txt_couName);
                _txtcoupName.setText(_genCouList.get(position).get_couID());

                _txtcouPoint = (TextView) convertView.findViewById(R.id.txt_couPoint);
                _txtcouPoint.setText(_genCouList.get(position).get_couPoint());
                GenerateCoupon couponPoint = _genCouList.get(position);
                GenerateCouponFeatureController.getInstance().set_genpoint(couponPoint);

                _txtiessueDate = (TextView) convertView.findViewById(R.id.txt_issue_Date);
                _txtiessueDate.setText(_genCouList.get(position).get_couIssueDate());

                txtvalidityDate = (TextView) convertView.findViewById(R.id.txtValidity_Date);
                txtvalidityDate.setText(_genCouList.get(position).get_couValidityDate());
                GenerateCoupon couponValidity = _genCouList.get(position);
                GenerateCouponFeatureController.getInstance().set_genValidity(couponValidity);

                _btnredeem = (CustomButton) convertView.findViewById(R.id.btn_generate);
                _btnredeem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _genFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                GenerateCoupon coupon = _genCouList.get(position);
                                GenerateCouponFeatureController.getInstance().setGeneratedCoupon(coupon);
                                _loadFragment(R.id.content_frame, new CouponRedeemFragment());
                            }
                        });
                    }
                });
            }
        }
        return convertView;
    }

    private void _loadFragment(int id, Fragment fragment) {
        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = _genFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack(fragment.getTag());
        ft.commitAllowingStateLoss();
    }

    private boolean _genCoupListPopulated(ArrayList<GenerateCoupon> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
     //   _genCouList = GenerateCouponFeatureController.getInstance().get_genCouList();
        _genCouList = GenerateCouponFeatureController.getInstance().getServerCouponList();
    }
}
