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
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectwiseStudentController;
import com.blueplanet.smartcookieteacher.models.Subjectwise_student;
import com.blueplanet.smartcookieteacher.ui.SubjectwiseStudentFragment;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;


import java.util.ArrayList;

/**
 * Created by 1311 on 08-02-2016.
 */
public class SubjectwiseStudentAdapter extends BaseAdapter {

    private SubjectwiseStudentFragment _subjectFragment;
    private SubjectwiseStudentFragmentController _subjectController;
    private ArrayList<Subjectwise_student> _subjectwiseList;
    private final String _TAG = this.getClass().getSimpleName();
    private CustomTextView _txtName, txtPRN;
    private ImageView _stdimg;

    public SubjectwiseStudentAdapter(SubjectwiseStudentFragment subjectFragment,
                                     SubjectwiseStudentFragmentController subjectController,View _view) {
        _subjectFragment = subjectFragment;
        _subjectController = subjectController;
        _subjectwiseList = SubjectwiseStudentController.getInstance().get_subList();

    }

    @Override
    public int getCount() {
        if (_SubjectwiseListPopulated(_subjectwiseList)) {

            return _subjectwiseList.size();
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

            convertView = inflatorInflater.inflate(R.layout.subjectwise_student_item, null
            );
        }
        if (convertView != null) {
            if (_SubjectwiseListPopulated(_subjectwiseList)) {
                _txtName = (CustomTextView) convertView.findViewById(R.id.txt_studentName);
                txtPRN = (CustomTextView) convertView.findViewById(R.id.txt_prn);
                _stdimg = (ImageView) convertView.findViewById(R.id.iv_studentimg);


                _txtName.setText(_subjectwiseList.get(position).get_stdFullName());
                txtPRN.setText(_subjectwiseList.get(position).get_id());
                String timage = _subjectwiseList.get(position).get_stdImage();
                if (timage != null && timage.length() > 0) {

                    final String imageName = WebserviceConstants.IMAGE_BASE_URL + timage;
                    Log.i(_TAG, imageName);

                    SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, _stdimg,
                            IImageLoader.NORMAL_POSTER);

                    SmartCookieImageLoader.getInstance().display();

                }


            }

        }

        return convertView;
    }

    private boolean _SubjectwiseListPopulated(ArrayList<Subjectwise_student> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {


            return true;
        }
        return false;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        _subjectwiseList = SubjectwiseStudentController.getInstance().get_subList();

    }
}
