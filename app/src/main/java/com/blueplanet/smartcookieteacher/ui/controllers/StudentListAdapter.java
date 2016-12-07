package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.RadioGroup;
import android.widget.Toast;


import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomEditText;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.network.NetworkManager;
import com.blueplanet.smartcookieteacher.ui.StudentListFragment;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;


import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by 1311 on 27-11-2015.
 */
public class StudentListAdapter extends BaseAdapter implements TextWatcher {

    private StudentListFragment _StudentListFragment;
    private StudentListFragmentController _StudentListFragmentController;
    private ArrayList<Student> _filteredStudentList, _allStudList;
    private final String _TAG = this.getClass().getSimpleName();
    private CustomTextView _txtName, _txtPrn, txtimage;
    private ImageView _ivStudentPhoto;
    private CustomEditText txtSearch;
    private boolean _isFiltered = false;
    private CheckBox _checkBox;
    SparseBooleanArray mCheckStates;


    public StudentListAdapter(StudentListFragment StudentListFragment,
                              StudentListFragmentController StudentListFragmentController,
                              View _view) {

        _StudentListFragment = StudentListFragment;
       /* if ((NetworkManager.isNetworkAvailable()) == false) {
            _filteredStudentList = StudentFeatureController.getInstance().getStudentInfoFromDB();
            Log.i(_TAG,"StudentListdb");
        }else{
            _filteredStudentList = StudentFeatureController.getInstance().getStudentList();

            Log.i(_TAG,"StudentListweb");
        }*/

        _filteredStudentList = StudentFeatureController.getInstance().getStudentList();
        // _filteredStudentList = StudentFeatureController.getInstance().getStudentInfoFromDB();
        _allStudList = new ArrayList<>();
        _allStudList.addAll(_filteredStudentList);
        txtSearch = (CustomEditText) _view.findViewById(R.id.etxtSearch);
        //  _checkBox=(CheckBox)_view.findViewById(R.id.checkBox);


    }

    @Override
    public int getCount() {

        if (_StudentListPopulated(_filteredStudentList)) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication
                    .getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflatorInflater.inflate(R.layout.student_list_item, null
            );
        }
        if (convertView != null) {
            if (_StudentListPopulated(_filteredStudentList)) {
                _ivStudentPhoto = (ImageView) convertView.findViewById(R.id.iv_studentPhoto);
                _txtName = (CustomTextView) convertView
                        .findViewById(R.id.txt_studentName);


                _txtName.setText(_filteredStudentList.get(position).get_stdName());

                _txtPrn = (CustomTextView) convertView
                        .findViewById(R.id.txt_prn);
                _txtPrn.setText(_filteredStudentList.get(position).get_stdPRN());

               /* _checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

                String country = _filteredStudentList.get(position).get_stdName();


                if(_filteredStudentList !=null && country.equalsIgnoreCase("Yes")){
                    _checkBox.setChecked(true);
                }*/


                String imageurl = _filteredStudentList.get(position).get_stdImageUrl();
                if (imageurl != null && imageurl.length() > 0) {
                    final String imageName = WebserviceConstants.IMAGE_BASE_URL
                            + imageurl;
                    Log.i(_TAG, imageName);


                    SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, _ivStudentPhoto,
                            IImageLoader.CIRCULAR_USER_POSTER);
                    SmartCookieImageLoader.getInstance().display();
                }
            }
        }

        return convertView;
    }


    private boolean _StudentListPopulated(ArrayList<Student> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

        if (_isFiltered == true) {
            _filteredStudentList = StudentFeatureController.getInstance().getFilteredList();
        } else {
            _filteredStudentList = StudentFeatureController.getInstance().getStudentList();
        }

    }


    private void _filterStudentList(String charText) {

        _filteredStudentList = new ArrayList<>();
        if (charText.length() == 0) {
            if (_filteredStudentList != null) {
                _filteredStudentList.clear();
                _isFiltered = false;
                _filteredStudentList = StudentFeatureController.getInstance().getStudentList();
            }
        } else {
            _allStudList = StudentFeatureController.getInstance().getStudentList();
            for (Student s : _allStudList) {
                String studName = s.get_stdName().toLowerCase();

                if (studName.contains(charText)) {
                    _isFiltered = true;
                    _filteredStudentList.add(s);
                    _filteredStudentList = new ArrayList<Student>(new LinkedHashSet<Student>(_filteredStudentList));
                    StudentFeatureController.getInstance().setFilteredList(_filteredStudentList);
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
