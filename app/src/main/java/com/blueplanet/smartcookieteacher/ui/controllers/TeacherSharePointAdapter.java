package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.AdminThanqFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.GenerateCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SharePointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.AdminThankqPoint;
import com.blueplanet.smartcookieteacher.models.GenerateCoupon;
import com.blueplanet.smartcookieteacher.models.ShairPointModel;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.ui.AdminFragment;
import com.blueplanet.smartcookieteacher.ui.CouponRedeemFragment;
import com.blueplanet.smartcookieteacher.ui.PointShareFragment;
import com.blueplanet.smartcookieteacher.ui.SharePointFragment;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by 1311 on 01-08-2016.
 */
public class TeacherSharePointAdapter extends BaseAdapter implements TextWatcher {
    private SharePointFragment _fragment;
    private SharePointFragmentController _contr;
    private ArrayList<ShairPointModel> teachrtsharelist;
    private final String _TAG = this.getClass().getSimpleName();
    private TextView _txtName, txtID, _txtPoint, _txtphone, txtemail;
    private CustomEditText txtSearch;
    private ArrayList<ShairPointModel> _filteredStudentList, _allStudList;
    private boolean _isFiltered = false;
    private CustomButton _btn_share;


    public TeacherSharePointAdapter(SharePointFragment fragment,View _view
    ) {

        _fragment = fragment;
        _filteredStudentList = SharePointFeatureController.getInstance().get_teachershair();
        _allStudList = new ArrayList<>();
        _allStudList.addAll(_filteredStudentList);
        txtSearch = (CustomEditText) _view.findViewById(R.id.etxtSearch);

    }

    @Override
    public int getCount() {
        if (_RewardListPopulated(_filteredStudentList)) {
            return _filteredStudentList.size();
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
            convertView = inflatorInflater.inflate(R.layout.shair_point_adapter, null
            );
        }
        if (convertView != null) {
            if (_RewardListPopulated(_filteredStudentList)) {


                if (_RewardListPopulated(_filteredStudentList)) {


                    _txtName = (TextView) convertView
                            .findViewById(R.id.txt_couName);

                    _txtName.setText(_filteredStudentList.get(position).get_teacherName());

                    txtID = (TextView) convertView
                            .findViewById(R.id.txt_couPoint);
                    txtID.setText(_filteredStudentList.get(position).get_teacherid());

                    _txtPoint = (TextView) convertView
                            .findViewById(R.id.txt_issue_Date);
                    _txtPoint.setText(_filteredStudentList.get(position).get_teacherbluePoint());


                    txtemail = (TextView) convertView
                            .findViewById(R.id.txtValidity_Date);
                    txtemail.setText(_filteredStudentList.get(position).get_teacheremail());



                    _btn_share = (CustomButton) convertView.findViewById(R.id.btn_share);
                    _btn_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShairPointModel teacher = _filteredStudentList.get(position);
                        SharePointFeatureController.getInstance().set_selectedteacher(teacher);
                        _fragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                _loadFragment(R.id.content_frame, new PointShareFragment());
                            }
                        });


                    }
                });
            }
            }
        }
        return convertView;

    }

    private void _loadFragment(int id, Fragment fragment) {
        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = _fragment.getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack(fragment.getTag());
        ft.commitAllowingStateLoss();
    }

    private boolean _RewardListPopulated(ArrayList<ShairPointModel> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;

        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (_isFiltered == true) {
            _filteredStudentList = SharePointFeatureController.getInstance().getFilteredList();
        } else {
            _filteredStudentList = SharePointFeatureController.getInstance().get_teachershair();
        }


    }
    private void _filterStudentList(String charText) {

        _filteredStudentList = new ArrayList<>();
        if (charText.length() == 0) {
            if (_filteredStudentList != null) {
                _filteredStudentList.clear();
                _isFiltered = false;
                _filteredStudentList = SharePointFeatureController.getInstance().get_teachershair();
            }
        } else {
            _allStudList = SharePointFeatureController.getInstance().get_teachershair();
            for (ShairPointModel s : _allStudList) {
                String studName = s.get_teacherName().toLowerCase();

                if (studName.contains(charText)) {
                    _isFiltered = true;
                    _filteredStudentList.add(s);
                    _filteredStudentList = new ArrayList<ShairPointModel>(new LinkedHashSet<ShairPointModel>(_filteredStudentList));
                    SharePointFeatureController.getInstance().setFilteredList(_filteredStudentList);
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }



    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String text = txtSearch.getText().toString();
        _filterStudentList(text.toLowerCase());
        notifyDataSetChanged();
    }


    @Override
    public void afterTextChanged(Editable s) {

    }
}

