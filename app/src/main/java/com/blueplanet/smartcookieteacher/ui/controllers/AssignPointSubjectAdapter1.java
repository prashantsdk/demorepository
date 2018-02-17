package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.AssignPointFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.subFeaturecontroller;
import com.blueplanet.smartcookieteacher.models.SubNameCode;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragment;

import java.util.ArrayList;

/**
 * Created by Sayali on 5/24/2017.
 */
public class AssignPointSubjectAdapter1 extends BaseAdapter {

    private AssignPointFragment _assignPointFragment;
    private ArrayList<SubNameCode> _subList;


    //private CustomTextView _textView;

    private final String _TAG = this.getClass().getSimpleName();
    private RadioButton _textView[];

    private boolean[] itemChecked;

    public AssignPointSubjectAdapter1(AssignPointFragment assignPointFragment,
                                      AssignPointFragmentController assignPointFragmentController,
                                      ArrayList<SubNameCode> activityList
    ) {

        _assignPointFragment = assignPointFragment;



       // _subList = subFeaturecontroller.getInstance().get_subjList();
        _subList =activityList;
        if (_ActivityListPopulated(_subList)) {
            _textView = new RadioButton[_subList.size()];
            itemChecked = new boolean[_subList.size()];
        }
    }


    @Override
    public int getCount() {
        if (_ActivityListPopulated(_subList)) {
            return _subList.size();
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
            convertView = inflatorInflater.inflate(R.layout.new_assign_subj, null
            );
        }
        if (convertView != null) {
            if (_subList != null && _subList.size() > 0) {


                _textView[position] = (RadioButton) convertView.findViewById(R.id.yes);


                _textView[position].setText(_subList.get(position).get_subname());


                _textView[position].setClickable(true);

                _textView[position].setTextColor(_assignPointFragment.getResources().getColor(R.color.blue_circle));




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

                        String subId = _subList.get(position).get_subcode();
                        Log.i(_TAG, "Activity id is: " + subId);
                        if (subId != null) {

                           /* ActivityListFeatureController.getInstance().
                                    setSeletedActivityId(activityId);*/

                            AssignPointFeatureController.getInstance().

                                    set_seletedSubjectId(subId);

                        }


                    }
                });


            }
        }
        return convertView;


    }


    private boolean _ActivityListPopulated(ArrayList<SubNameCode> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        _subList = subFeaturecontroller.getInstance().get_subjList();


    }
}
