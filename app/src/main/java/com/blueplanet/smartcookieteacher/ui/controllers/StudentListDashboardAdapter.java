package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.ui.TeacherDashboardFragment;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;


import java.util.ArrayList;

/**
 * Created by 1311 on 16-12-2015.
 */
public class StudentListDashboardAdapter extends BaseAdapter {

    private TeacherDashboardFragment _tTeacherDashboardFragment;
    private TeacherDashboardFragmentController _tTeacherDashboardFragmentController;
    private ArrayList<Student> _studentList;
    private final String _TAG = this.getClass().getSimpleName();
    private CustomTextView _txtStudentName, _txtClassName,_txtSubect;
    private ImageView _txtStudentImage;
    private RelativeLayout _rlDashboard;

    public StudentListDashboardAdapter(TeacherDashboardFragment TeacherDashboardFragment,
                                       TeacherDashboardFragmentController teacherDashboardFragmentController) {

        _tTeacherDashboardFragment = TeacherDashboardFragment;
        _tTeacherDashboardFragmentController = teacherDashboardFragmentController;
        _studentList = StudentFeatureController.getInstance().getStudentList();
        Log.i(_TAG, "stList" + _studentList);
        //_studentList = StudentFeatureController.getInstance().getStudentInfoFromDB();
    }


    @Override
    public int getCount() {
        if (_StudentListPopulated(_studentList)) {
            return _studentList.size();
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
            convertView = inflatorInflater.inflate(R.layout.student_list_item_dashboard, null
            );
        }
        if (convertView != null) {
            if (_StudentListPopulated(_studentList)) {
                _rlDashboard = (RelativeLayout) convertView.findViewById(R.id.rl_dashboard);
                _txtStudentImage = (ImageView) convertView.findViewById(R.id.studentPhoto);
                _txtStudentName = (CustomTextView) convertView
                        .findViewById(R.id.txtstudentName);
                _txtStudentName.setText(_studentList.get(position).get_stdName());

                _txtClassName = (CustomTextView) convertView.findViewById(R.id.txt_className);
                _txtSubect=(CustomTextView) convertView.findViewById(R.id.txt_subject_name);
                //_txtClassName.setText(_studentList.get(position).get_stdClass());


                String className = _studentList.get(position).get_stdClass();
                if (!(TextUtils.isEmpty(className)) && className.equalsIgnoreCase("null")) {
                    _txtClassName.setText("N/A");
                } else {
                    _txtClassName.setText(className);
                }


                String subjectName = _studentList.get(position).get_stdsubname();
                if (!(TextUtils.isEmpty(subjectName)) && subjectName.equalsIgnoreCase("null")) {
                    _txtSubect.setText("N/A");
                } else {
                    _txtSubect.setText(subjectName);
                }

                String imageurl = _studentList.get(position).get_stdImageUrl();
                if (imageurl != null && imageurl.length() > 0) {
                    final String imageName = WebserviceConstants.IMAGE_BASE_URL
                           + imageurl;

                    //final String imageName = imageurl;
                    Log.i(_TAG, imageName);

                    SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, _txtStudentImage,
                            IImageLoader.CIRCULAR_USER_POSTER);
                    SmartCookieImageLoader.getInstance().display();

                }

                _setColorToListItem(position);

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
        _studentList = StudentFeatureController.getInstance().getStudentList();

    }

    /**
     * function to set background color to listview item
     *
     * @param position
     */
    private void _setColorToListItem(int position) {
        if (position % 2 == 0) {
            _rlDashboard.setBackgroundColor(_tTeacherDashboardFragment.getResources().getColor(R.color.username_color));

        } else {
            _rlDashboard.setBackgroundColor(_tTeacherDashboardFragment.getResources().getColor(R.color.white_solid));
        }

    }

    public void close() {
        if (_tTeacherDashboardFragment != null) {
            _tTeacherDashboardFragment = null;

        }
    }


}
