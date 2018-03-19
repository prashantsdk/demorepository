package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.featurecontroller.ActivityListFeatureController;
import com.blueplanet.smartcookieteacher.models.TeacherActivity;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragment;

import java.util.ArrayList;


/**
 * Created by 1311 on 04-01-2016.
 */
public class AssignPointListAdapter extends BaseAdapter {

    private AssignPointFragment _assignPointFragment;
    private AssignPointFragmentController _assignPointFragmentController;
    private ArrayList<TeacherActivity> _activityList;

    private final String _TAG = this.getClass().getSimpleName();
    private CustomTextView _textView[];


    public AssignPointListAdapter(AssignPointFragment assignPointFragment,
                                  AssignPointFragmentController assignPointFragmentController,
                                  ArrayList<TeacherActivity> activityList) {

        _assignPointFragment = assignPointFragment;
        _assignPointFragmentController = assignPointFragmentController;
        _activityList = activityList;


        if (_ActivityListPopulated(_activityList)) {
            _textView = new CustomTextView[_activityList.size()];
        }

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
        if (convertView == null) {
            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication

                    .getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflatorInflater.inflate(R.layout.activity_list_item, null
            );
        }
        if (convertView != null) {
            if (_ActivityListPopulated(_activityList)) {

                RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.rl_main);
                RelativeLayout.LayoutParams relativeLayoutParams;

                _textView[position] = new CustomTextView(_assignPointFragment.getActivity());
                relativeLayoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                _textView[position].setText(_activityList.get(position).getActivityName());

                if (position % 2 == 0) {
                    relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                    _textView[position].setPadding(15, 15, 0, 0);
                } else {
                    relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    _textView[position].setPadding(0, 15, 15, 0);
                }
                _textView[position].setTextSize(15);
                _textView[position].setClickable(true);

                _textView[position].setTextColor(_assignPointFragment.getResources().getColor(R.color.blue_circle));
                relativeLayout.addView(_textView[position], relativeLayoutParams);

                _textView[position].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _textView[position].setTextColor(_assignPointFragment.getResources().getColor(R.color.red_solid));

                        String activityId = _activityList.get(position).getActivityId();
                        Log.i(_TAG, "Activity id is: " + activityId);
                        if(activityId !=null){
                            ActivityListFeatureController.getInstance().
                                    setSeletedActivityId(activityId);
                        }



                    }
                });


            }
        }


        return convertView;
    }

    private boolean _ActivityListPopulated(ArrayList<TeacherActivity> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }


}
