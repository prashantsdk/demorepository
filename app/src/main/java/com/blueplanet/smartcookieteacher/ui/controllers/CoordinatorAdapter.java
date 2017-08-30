package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.CoordinatorFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.CoordinatorModel;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.ui.CoordinatorFragment;
import com.blueplanet.smartcookieteacher.ui.StudentListFragment;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by 1311 on 09-08-2016.
 */
public class CoordinatorAdapter extends BaseAdapter implements TextWatcher {


    private CoordinatorFragment _fragment;
    private CoordinatorFragmentController _Controller;
    private ArrayList<CoordinatorModel> _filteredSTuCoorList, _allStudCoorList;
    private final String _TAG = this.getClass().getSimpleName();
    private CustomTextView _txtName, _txtPrn, txtimage;
    private ImageView _ivStudentPhoto;
    private CustomEditText txtSearch;
    private boolean _isFiltered = false;
    private CheckBox _checkBox;
    SparseBooleanArray mCheckStates;


    public CoordinatorAdapter(CoordinatorFragment fragment,
                              CoordinatorFragmentController Controller,
                              View _view) {

        _fragment = fragment;
        _Controller=Controller;

        _filteredSTuCoorList = CoordinatorFeatureController.getInstance().getStudentList();

        // _filteredStudentList = StudentFeatureController.getInstance().getStudentInfoFromDB();
        _allStudCoorList = new ArrayList<>();
        _allStudCoorList.addAll(_filteredSTuCoorList);
        txtSearch = (CustomEditText) _view.findViewById(R.id.etxtSearch);
          _checkBox=(CheckBox)_view.findViewById(R.id.checkBox);


    }
    @Override
    public int getCount() {
        if (_StudentCooListPopulated(_filteredSTuCoorList)) {
            return _filteredSTuCoorList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication
                    .getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflatorInflater.inflate(R.layout.adapter, null
            );
        }
        if (convertView != null) {
            if (_StudentCooListPopulated(_filteredSTuCoorList)) {
                _ivStudentPhoto = (ImageView) convertView.findViewById(R.id.iv_studentPhoto);
                _txtName = (CustomTextView) convertView
                        .findViewById(R.id.txt_studentName);


                _txtName.setText(_filteredSTuCoorList.get(position).get_stdName());

                _txtPrn = (CustomTextView) convertView
                        .findViewById(R.id.txt_prn);
                _txtPrn.setText(_filteredSTuCoorList.get(position).get_stdPRN());

                _checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

                String status = _filteredSTuCoorList.get(position).get_coordinator();
                Log.i(_TAG, "Status Yes" + status);


                if(_filteredSTuCoorList !=null && status.equalsIgnoreCase("Y")){


                        _checkBox.setChecked(true);

                    }else
                if(_filteredSTuCoorList !=null && status.equalsIgnoreCase("N")){
                    _checkBox.setChecked(false);
                }

                }
            String imageurl = _filteredSTuCoorList.get(position).get_stdImageUrl();
                if (imageurl != null && imageurl.length() > 0) {
                    final String imageName = WebserviceConstants.IMAGE_BASE_URL
                            + imageurl;
                    Log.i(_TAG, imageName);


                    SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, _ivStudentPhoto,
                            IImageLoader.CIRCULAR_USER_POSTER);
                    SmartCookieImageLoader.getInstance().display();
                }
            }


        return convertView;
    }


    private boolean _StudentCooListPopulated(ArrayList<CoordinatorModel> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        if (_isFiltered == true) {
            _filteredSTuCoorList = CoordinatorFeatureController.getInstance().getFilteredList();
        } else {
            _filteredSTuCoorList = CoordinatorFeatureController.getInstance().getStudentList();
        }

    }


    private void _filterStudentList(String charText) {

        _filteredSTuCoorList = new ArrayList<>();
        if (charText.length() == 0) {
            if (_filteredSTuCoorList != null) {
                _filteredSTuCoorList.clear();
                _isFiltered = false;
                _filteredSTuCoorList = CoordinatorFeatureController.getInstance().getStudentList();
            }
        } else {
            _allStudCoorList = CoordinatorFeatureController.getInstance().getStudentList();
            for (CoordinatorModel s : _allStudCoorList) {
                String studName = s.get_stdName().toLowerCase();

                if (studName.contains(charText)) {
                    _isFiltered = true;
                    _filteredSTuCoorList.add(s);
                    _filteredSTuCoorList = new ArrayList<CoordinatorModel>(new LinkedHashSet<CoordinatorModel>(_filteredSTuCoorList));

                    CoordinatorFeatureController.getInstance().setFilteredList(_filteredSTuCoorList);
                }
            }
        }
    }

    public void close() {
        if (_fragment != null) {
            _fragment = null;

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
