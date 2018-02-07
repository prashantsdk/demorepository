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
import com.blueplanet.smartcookieteacher.featurecontroller.AssignPointFeatureController;
import com.blueplanet.smartcookieteacher.models.SubNameCode;
import com.blueplanet.smartcookieteacher.ui.AssignPointFragment;

import java.security.PrivateKey;
import java.util.ArrayList;


/**
 * Created by 1311 on 10-02-2016.
 */
public class AssignPointSubjectAdapter extends BaseAdapter {
    private AssignPointFragment _assignPointFragment;
    private AssignPointFragmentController _assignPointFragmentController;
    private ArrayList<SubNameCode> _subList;

    private final String _TAG = this.getClass().getSimpleName();
    private CustomTextView _textView[];


    public AssignPointSubjectAdapter(AssignPointFragment assignPointFragment,
                                     AssignPointFragmentController assignPointFragmentController,
                                     ArrayList<SubNameCode> subList) {

        _assignPointFragment = assignPointFragment;
        _assignPointFragmentController = assignPointFragmentController;
        _subList = subList;


        if (_SubjectListPopulated(_subList)) {
            _textView = new CustomTextView[_subList.size()];
        }

    }
    @Override
    public int getCount() {
        if (_SubjectListPopulated(_subList)) {
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
    public View getView(final  int  position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflatorInflater = (LayoutInflater) MainApplication
                    .getContext().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflatorInflater.inflate(R.layout.activity_list_item, null
            );
        }

        if (convertView != null) {
            if (_SubjectListPopulated(_subList)) {

                RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.rl_main);
                RelativeLayout.LayoutParams relativeLayoutParams;

                _textView[position] = new CustomTextView(_assignPointFragment.getActivity());
                relativeLayoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                _textView[position].setText(_subList.get(position).get_subname());

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

                _textView[position].setOnClickListener(
                        new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _textView[position].setTextColor(_assignPointFragment.getResources().getColor(R.color.red_solid));

                        String subCode = _subList.get(position).get_subcode();
                        Log.i(_TAG, "subject id is: " + subCode);
                        AssignPointFeatureController.getInstance().
                                set_seletedSubjectId(subCode);




                    }
                });


            }
        }


        return convertView;
    }



    private boolean _SubjectListPopulated(ArrayList<SubNameCode> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }
}
