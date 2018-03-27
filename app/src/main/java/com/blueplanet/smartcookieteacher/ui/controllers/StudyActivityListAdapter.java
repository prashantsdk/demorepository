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
import com.blueplanet.smartcookieteacher.models.ArtActivity;
import com.blueplanet.smartcookieteacher.models.StudyActivity;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragment;
import com.blueplanet.smartcookieteacher.ui.SearchAssignPointFragment;

import java.util.ArrayList;

/**
 * Created by Priyanka on 3/27/2018.
 */

public class StudyActivityListAdapter extends BaseAdapter {

    private AssignPointFragment _assignPointFragment;
    private AssignPointFragmentController _assignPointFragmentController;
    private ArrayList<StudyActivity> _activityList;
    private boolean[] itemChecked;
    String activityId = "";
    private RadioButton selected = null;
    private SharedPreferences sp;

    public StudyActivityListAdapter(AssignPointFragment assignPointFragment,
                                    AssignPointFragmentController assignPointFragmentController,
                                    ArrayList<StudyActivity> activityList) {

        _assignPointFragment = assignPointFragment;
        _assignPointFragmentController = assignPointFragmentController;
        _activityList = activityList;
        sp = _assignPointFragment.getActivity().getSharedPreferences("SPD", Context.MODE_PRIVATE);
    }

    public StudyActivityListAdapter(ArrayList<StudyActivity> studyActivities) {
        _activityList = studyActivities;
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
        final SpinnerHolder holder;

        if (row == null) {

            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            row = inflatorInflater.inflate(R.layout.new_assignpoint, null, false);

            holder = new SpinnerHolder();

            if (_ActivityListPopulated(_activityList)) {
                itemChecked = new boolean[_activityList.size()];
            }
            holder._radioGrup = row.findViewById(R.id.group);
            holder._textView = row.findViewById(R.id.yes);

            row.setTag(holder);

        } else {
            holder = (SpinnerHolder) row.getTag();
        }

        holder._textView.setText(_activityList.get(position).getActivityName());
        holder._textView.setClickable(true);

        if (position == getCount()) {

            if (selected == null) {
                selected = holder._textView;
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

                if (activityId != null) {
                    ActivityListFeatureController.getInstance().setSeletedActivityIDOne(true);
                    ActivityListFeatureController.getInstance().
                            setSeletedActivityId(activityId);
                }
            }


        });

        return row;
    }

    private boolean _ActivityListPopulated(ArrayList<StudyActivity> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private static class SpinnerHolder {
        RadioGroup _radioGrup;
        RadioButton _textView;
    }
}