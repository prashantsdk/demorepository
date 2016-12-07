package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectAllFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectFeatureController;
import com.blueplanet.smartcookieteacher.models.TeacherAllSubject;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.ui.AllSubjectFragment;
import com.blueplanet.smartcookieteacher.ui.TeacherSubjectFragment;

import java.util.ArrayList;

/**
 * Created by 1311 on 16-09-2016.
 */
public class TeacherAllSubAdapter extends BaseAdapter {

    private AllSubjectFragment _subjectFragment;
    private ArrayList<TeacherAllSubject> _subjectList;
    private final String _TAG = this.getClass().getSimpleName();
    private CustomTextView _txt_studentNam, _txtDivision, _txtBranch, _txtsemester, _txtDepartment;

    public TeacherAllSubAdapter(AllSubjectFragment subjectFragment) {

        _subjectFragment = subjectFragment;

        _subjectList = SubjectAllFeatureController.getInstance().get_subjectList();
    }

    @Override
    public int getCount() {
        if (_SubjectListPopulated(_subjectList)) {
            return _subjectList.size();

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
            convertView = inflatorInflater.inflate(R.layout.all_sub_item, null
            );
        }
        if (convertView != null) {
            if (_SubjectListPopulated(_subjectList)) {
                _txt_studentNam = (CustomTextView) convertView.findViewById(R.id.txt_studentNam);
                _txtDivision = (CustomTextView) convertView.findViewById(R.id.txt_DivisionName);
                _txtBranch = (CustomTextView) convertView.findViewById(R.id.txt_branchName);
                _txtsemester = (CustomTextView) convertView.findViewById(R.id.txt_semesterName);
                _txtDepartment = (CustomTextView) convertView.findViewById(R.id.txt_departmentName);
                _txt_studentNam.setText(_subjectList.get(position).get_tsubjectName());
                _txtDivision.setText(_subjectList.get(position).get_tDivisionID());
                _txtBranch.setText(_subjectList.get(position).get_tBranchID());
                Log.i(_TAG, "branch" + _subjectList.get(position).get_tBranchID().toString());
                _txtsemester.setText(_subjectList.get(position).get_tSemesterID());
                Log.i(_TAG, "Semes" + _txtsemester.getText());
                _txtDepartment.setText(_subjectList.get(position).get_tDepartmentID());


            }
        }

        return convertView;
    }

    private boolean _SubjectListPopulated(ArrayList<TeacherAllSubject> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        _subjectList = SubjectAllFeatureController.getInstance().get_subjectList();
    }

}
