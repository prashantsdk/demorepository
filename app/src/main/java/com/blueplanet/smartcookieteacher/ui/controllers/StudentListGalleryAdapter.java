package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.SearchStudentFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.SearchStudent;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragment;

import java.util.ArrayList;

/**
 * Created by 1311 on 30-12-2015.
 */
public class StudentListGalleryAdapter extends BaseAdapter {

    private com.blueplanet.smartcookieteacher.ui.AssignPointFragment _assignPointFragment = null;
    private com.blueplanet.smartcookieteacher.ui.controllers.AssignPointFragmentController _assignPointController;
    private ArrayList<com.blueplanet.smartcookieteacher.models.Student> _studentList = null;
    private final String _TAG = this.getClass().getSimpleName();
    private View _view;
    private com.blueplanet.smartcookieteacher.models.Student _student;

    public StudentListGalleryAdapter(AssignPointFragment assignPointFragment,
                                     AssignPointFragmentController assignPointController,
                                     View view) {
        _assignPointFragment = assignPointFragment;
        _assignPointController = assignPointController;

        _view = view;
        _studentList = StudentFeatureController.getInstance().getStudentList();
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
            LayoutInflater inflatorInflater = (LayoutInflater) com.blueplanet.smartcookieteacher.MainApplication
                    .getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflatorInflater.inflate(R.layout.sliderimage, null
            );
        }
        if (convertView != null) {
            if (_StudentListPopulated(_studentList)) {

                com.blueplanet.smartcookieteacher.models.Student student = _studentList.get(position);
                ImageView studentImage = (ImageView) convertView.findViewById(R.id.imgStripView);

                String imageurl = student.get_stdImageUrl();
                if (imageurl != null && imageurl.length() > 0) {
                    final String imageName = com.blueplanet.smartcookieteacher.webservices.WebserviceConstants.IMAGE_BASE_URL
                            + imageurl;
                    Log.i(_TAG, imageName);

                    com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, studentImage,
                            com.blueplanet.smartcookieteacher.utils.IImageLoader.CIRCULAR_USER_POSTER);
                    com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader.getInstance().display();
                }
            }
        }

        return convertView;
    }

    private boolean _StudentListPopulated(ArrayList<com.blueplanet.smartcookieteacher.models.Student> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }
}
