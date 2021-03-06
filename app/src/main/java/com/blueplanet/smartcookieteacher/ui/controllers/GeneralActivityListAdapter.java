package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.ActivityListFeatureController;
import com.blueplanet.smartcookieteacher.models.GeneralActivity;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragment;

import java.util.ArrayList;

/**
 * Created by prashantj on 2/19/2018.
 */

public class GeneralActivityListAdapter extends BaseAdapter {


    private AssignPointFragment _assignPointFragment;
    private AssignPointFragmentController _assignPointFragmentController;
    private ArrayList<GeneralActivity> _activityList;


    //private CustomTextView _textView;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();


    String activityId = "";

    boolean checkFlag = false;
    private RadioButton selected = null;
    private SharedPreferences sp;

    private RadioButton _textView[];
    private RadioGroup _radioGrup;

    private boolean[] itemChecked;


    public GeneralActivityListAdapter(AssignPointFragment assignPointFragment,
                                      AssignPointFragmentController assignPointFragmentController,
                                      ArrayList<GeneralActivity> activityList
    ) {

        _assignPointFragment = assignPointFragment;
        _assignPointFragmentController = assignPointFragmentController;

        _activityList = activityList;

        if (_ActivityListPopulated(_activityList)) {
            _textView = new RadioButton[_activityList.size()];
            itemChecked = new boolean[_activityList.size()];
        }
    }

    public GeneralActivityListAdapter(ArrayList<GeneralActivity> generalActivities) {
        _activityList = generalActivities;
    }


    @Override
    public int getCount() {
        if (_ActivityListPopulated(_activityList)) {

            return _activityList.size();
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
        final GeneralActivityListAdapter.SpinnerHolder holder;

        if (row == null) {

            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            row = inflatorInflater.inflate(R.layout.new_assignpoint, null, false);

            holder = new GeneralActivityListAdapter.SpinnerHolder();

            if (_ActivityListPopulated(_activityList)) {

                itemChecked = new boolean[_activityList.size()];
            }

            holder._radioGrup = (RadioGroup) row.findViewById(R.id.group);


            holder._textView = (RadioButton) row.findViewById(R.id.yes);

            row.setTag(holder);

        } else {

            holder = (GeneralActivityListAdapter.SpinnerHolder) row.getTag();
        }


        holder._textView.setText(_activityList.get(position).getActivityName());


        holder._textView.setClickable(true);

        // holder._textView.setTextColor(_assignPointFragment.getResources().getColor(R.color.blue_circle));
        //  ActivityListFeatureController.getInstance().setSeletedActivityIDOne(false);

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




                activityId = _activityList.get(position).getActivityId();

                if ((activityId != "")) {

                    ActivityListFeatureController.getInstance().setSeletedActivityIDOne(true);

                    ActivityListFeatureController.getInstance().
                            setSeletedActivityId(activityId);

                }

            }


        });


        return row;


    }


    private boolean _ActivityListPopulated(ArrayList<GeneralActivity> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        // _activityList = ActivityListFeatureController.getInstance().get_teacherActivityList();


    }

    private static class SpinnerHolder {
        RadioGroup _radioGrup;
        RadioButton _textView;
    }
}
