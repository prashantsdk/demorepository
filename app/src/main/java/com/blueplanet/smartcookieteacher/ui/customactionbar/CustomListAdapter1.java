package com.blueplanet.smartcookieteacher.ui.customactionbar;

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
import com.blueplanet.smartcookieteacher.featurecontroller.StudentFeatureController;
import com.blueplanet.smartcookieteacher.models.Student;
import com.blueplanet.smartcookieteacher.ui.StudentListFragment;
import com.blueplanet.smartcookieteacher.ui.controllers.StudentListFragmentController;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;


import java.util.ArrayList;

/**
 * Created by 1311 on 28-12-2015.
 */
public class CustomListAdapter1 extends BaseAdapter {
    private StudentListFragment _StudentListFragment;
    private StudentListFragmentController _StudentListFragmentController;
    private ArrayList<Student> _studentList;
    private final String _TAG = this.getClass().getSimpleName();
    private CustomTextView _txtName, _txtPrn, txtimage;
    private ImageView _ivStudentPhoto;



    public CustomListAdapter1(StudentListFragment StudentListFragment,
                              StudentListFragmentController StudentListFragmentController) {

        _StudentListFragment = StudentListFragment;
        _StudentListFragmentController = StudentListFragmentController;
        _studentList = StudentFeatureController.getInstance().getStudentList();
    }


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
            convertView = inflatorInflater.inflate(R.layout.student_list_item, null
            );
        }
        if (convertView != null) {
            if (_StudentListPopulated(_studentList)) {
                _ivStudentPhoto = (ImageView) convertView.findViewById(R.id.iv_studentPhoto);
                _txtName = (CustomTextView) convertView
                        .findViewById(R.id.txt_studentName);

                _txtName.setText(_studentList.get(position).get_stdName());

                _txtPrn = (CustomTextView) convertView
                        .findViewById(R.id.txt_prn);
                _txtPrn.setText(_studentList.get(position).get_stdPRN());



                String imageurl = _studentList.get(position).get_stdImageUrl();
                if (imageurl != null && imageurl.length() > 0) {
                    final String imageName = imageurl;
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
        _studentList = StudentFeatureController.getInstance().getStudentList();
    }
}
