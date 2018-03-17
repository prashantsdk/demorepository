package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.SearchStudentFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.SearchStudent;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragment;
import com.blueplanet.smartcookieteacher.ui.SearchAssignPointFragment;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;

import java.util.ArrayList;

/**
 * Created by Priyanka on 15-03-2018.
 */

public class SearchStudentListGalleryAdapter extends BaseAdapter {

    private SearchAssignPointFragment _assignPointFragment = null;
    private SearchAssignPointFragmentController _assignPointController;
    private ArrayList<SearchStudent> _studentList = null;
    private final String _TAG = this.getClass().getSimpleName();
    private View _view;
    private SearchStudent _student;

    public SearchStudentListGalleryAdapter(SearchAssignPointFragment assignPointFragment,
                                           SearchAssignPointFragmentController assignPointController,
                                           View view) {
        _assignPointFragment = assignPointFragment;
        _assignPointController = assignPointController;

        _view = view;
        _studentList = SearchStudentFeatureController.getInstance().getSearchedStudents();
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
            convertView = inflatorInflater.inflate(R.layout.sliderimage, null
            );
        }
        if (convertView != null) {
            if (_StudentListPopulated(_studentList)) {

                SearchStudent student = _studentList.get(position);
                ImageView studentImage = (ImageView) convertView.findViewById(R.id.imgStripView);

                String imageurl = student.get_searchimg();
                if (imageurl != null && imageurl.length() > 0) {
                    final String imageName =  imageurl;
                    Log.i(_TAG, imageName);

                    SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, studentImage,
                            IImageLoader.CIRCULAR_USER_POSTER);
                    SmartCookieImageLoader.getInstance().display();
                }
            }
        }

        return convertView;
    }

    private boolean _StudentListPopulated(ArrayList<SearchStudent> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }
}
