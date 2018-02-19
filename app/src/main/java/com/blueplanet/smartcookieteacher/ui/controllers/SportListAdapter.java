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
import com.blueplanet.smartcookieteacher.models.SportActivity;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragment;

import java.util.ArrayList;

/**
 * Created by prashantj on 2/19/2018.
 */

public class SportListAdapter extends BaseAdapter {


    private AssignPointFragment _assignPointFragment;
    private AssignPointFragmentController _assignPointFragmentController;
    private ArrayList<SportActivity> _activityList;


    //private CustomTextView _textView;
    private View _view;
    private final String _TAG = this.getClass().getSimpleName();
    private RadioButton _textView[];
    private RadioGroup _radioGrup;

    private boolean[] itemChecked;


    public SportListAdapter(AssignPointFragment assignPointFragment,
                            AssignPointFragmentController assignPointFragmentController,
                            ArrayList<SportActivity> activityList
    ) {

        _assignPointFragment = assignPointFragment;
        _assignPointFragmentController = assignPointFragmentController;

        _activityList = activityList;

        if (_ActivityListPopulated(_activityList)) {
            _textView = new RadioButton[_activityList.size()];
            itemChecked = new boolean[_activityList.size()];
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
            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflatorInflater.inflate(R.layout.new_assignpoint, null, false);
        }
        if (convertView != null) {
            if (_activityList != null && _activityList.size() > 0) {


                _radioGrup = (RadioGroup) convertView.findViewById(R.id.group);


                _textView[position] = (RadioButton) convertView.findViewById(R.id.yes);
                _textView[position].setText(_activityList.get(position).getActivityName());


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

                        _textView[position].setTextColor(_assignPointFragment.getResources().getColor(R.color.red_solid));
                        _textView[position].setChecked(true);
                        String activityId = _activityList.get(position).getActivityId();
                        Log.i(_TAG, "Activity id is: " + activityId);
                        if (activityId != null) {
                            ActivityListFeatureController.getInstance().
                                    setSeletedActivityId(activityId);
                        }
                    }
                });


            }
        }
        return convertView;


    }


    private boolean _ActivityListPopulated(ArrayList<SportActivity> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        //  _activityList = ActivityListFeatureController.getInstance().get_teacherActivityList();


    }
}
