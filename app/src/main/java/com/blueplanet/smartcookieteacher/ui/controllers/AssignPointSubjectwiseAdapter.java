package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.ActivityListFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.SubjectFeatureController;
import com.blueplanet.smartcookieteacher.models.TeacherActivity;
import com.blueplanet.smartcookieteacher.models.TeacherSubject;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragment;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragmentsubject;

import java.util.ArrayList;

/**
 * Created by Sayali on 3/12/2017.
 */
public class AssignPointSubjectwiseAdapter extends BaseAdapter {


    private AssignPointFragmentsubject _assignPointFragment;
    private AssignPointConSubject _assignPointFragmentController;
   // private ArrayList<TeacherActivity> _activityList;
    private ArrayList<TeacherSubject> _subjectList;

    //private CustomTextView _textView;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private RadioButton _textView[];
    private RadioGroup _radioGrup;
    private boolean[] itemChecked;


    public AssignPointSubjectwiseAdapter(AssignPointFragmentsubject assignPointFragment,
                                         AssignPointConSubject assignPointFragmentController,
                                   ArrayList<TeacherSubject> activityList
    ) {

        _assignPointFragment = assignPointFragment;
        _assignPointFragmentController = assignPointFragmentController;

        //_activityList = activityList;
      //  _activityList= ActivityListFeatureController.getInstance().get_teacherActivityList();
        _subjectList = SubjectFeatureController.getInstance().get_subjectList();
        if (_ActivityListPopulated(_subjectList)) {
            _textView = new RadioButton[_subjectList.size()];
            itemChecked = new boolean[_subjectList.size()];
        }
    }


    @Override
    public int getCount() {
        if (_ActivityListPopulated(_subjectList)) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication
                    .getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflatorInflater.inflate(R.layout.new_assignpoint, null


            );
        }
        if (convertView != null) {
            if (_subjectList != null && _subjectList.size() > 0) {


                _radioGrup = (RadioGroup) convertView.findViewById(R.id.group);

                _textView[position] = (RadioButton) convertView.findViewById(R.id.yes);


                _textView[position].setText(_subjectList.get(position).get_tsubjectName());


                _textView[position].setClickable(true);

                _textView[position].setTextColor(_assignPointFragment.getResources().getColor(R.color.blue_circle));

                //      _radioGrup.addView(_textView[position]);

                // _radioGrup.addView(relativeLayout);
                _textView[position].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            for (int i = 0; i < itemChecked.length; i++) {
                                _textView[i].setChecked(false);
                                _textView[i].setTextColor(_assignPointFragment.getResources().getColor(R.color.blue_circle));

                            }

                        } catch (Exception e) {

                        }

                        _textView[position].setChecked(true);

                        _textView[position].setTextColor(_assignPointFragment.getResources().getColor(R.color.red_solid));

                        String subjectId = _subjectList.get(position).get_tsubjectName();
                        Log.i(_TAG, "Activity id is: " + subjectId);
                        if (subjectId != null) {
                            /*ActivityListFeatureController.getInstance().
                                    setSeletedActivityId(activityId)*/;

                            SubjectFeatureController.getInstance().set_seletedSubjectId(subjectId);
                        }


                    }
                });


            }
        }
        return convertView;


    }


    private boolean _ActivityListPopulated(ArrayList<TeacherSubject> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        _subjectList  = SubjectFeatureController.getInstance().get_subjectList();


    }

}


