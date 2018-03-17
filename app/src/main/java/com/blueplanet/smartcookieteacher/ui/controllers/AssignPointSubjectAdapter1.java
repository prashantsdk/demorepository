package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.content.SharedPreferences;
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
    String activityId = "";

    boolean checkFlag = false;
    private RadioButton selected = null;
    private SharedPreferences sp;


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

    public AssignPointSubjectAdapter1(ArrayList<SubNameCode> subNameCodelist) {
        _subList = subNameCodelist;
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

        View row = convertView;
        final AssignPointSubjectAdapter1.SpinnerHolder holder;

        if (row == null) {

            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            row = inflatorInflater.inflate(R.layout.new_assignpoint, null, false);

            holder = new AssignPointSubjectAdapter1.SpinnerHolder();

            if (_ActivityListPopulated(_subList)) {

                itemChecked = new boolean[_subList.size()];
            }

            holder._radioGrup = (RadioGroup) row.findViewById(R.id.group);


            holder._textView = (RadioButton) row.findViewById(R.id.yes);

            row.setTag(holder);

        } else {

            holder = (AssignPointSubjectAdapter1.SpinnerHolder) row.getTag();
        }






        holder._textView.setText(_subList.get(position).get_subname());


        holder._textView.setClickable(true);

        if (position == getCount()) {

            if (selected == null) {
                // holder._textView.setChecked(true);
                selected = holder._textView;
                // ActivityListFeatureController.getInstance().setSeletedActivityIDOne(false);

            }

        }



        holder._textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (selected != null) {

                    selected.setChecked(false);
                    holder._radioGrup.clearCheck();

                }
                holder._textView.setChecked(true);
                selected = holder._textView;


                activityId = _subList.get(position).get_subcode();



                if (activityId != null) {

                    ActivityListFeatureController.getInstance().setSeletedActivityIDOne(true);

                    AssignPointFeatureController.getInstance().
                            set_seletedSubjectId(activityId);


                }

            }


        });


        return row;


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

    private static class SpinnerHolder {
        RadioGroup _radioGrup;
        RadioButton _textView;
    }
}
