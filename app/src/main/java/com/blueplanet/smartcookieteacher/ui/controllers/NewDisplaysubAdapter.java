package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.DisplaySubjectFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.RewardPointLogFeatureController;
import com.blueplanet.smartcookieteacher.models.DisplayTeacSubjectModel;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.ui.DisplaySubjectFragment;
import com.blueplanet.smartcookieteacher.ui.RewardPointFragment;

import java.util.ArrayList;

/**
 * Created by Sayali on 3/15/2017.
 */
public class NewDisplaysubAdapter extends BaseAdapter implements Filterable {

    private DisplaySubjectFragment _Fragment;
    private Display_subject_controller _reRewardPointFragmentController;
    private ArrayList<DisplayTeacSubjectModel> subjectList;
    private final String _TAG = this.getClass().getSimpleName();
    private TextView _txtName, txtDate, _txtMonth, _txtActivity, txtPoint;

    public ArrayList<DisplayTeacSubjectModel> filterlist=null;
    private FriendsFilter filter;
    public NewDisplaysubAdapter(DisplaySubjectFragment Fragment
    ) {

        _Fragment = Fragment;
        subjectList = DisplaySubjectFeatureController.getInstance().getSearchedTeacher();

    }

    @Override
    public int getCount() {
        if (_RewardListPopulated(subjectList)) {
            return subjectList.size();
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
            convertView = inflatorInflater.inflate(R.layout.display_subject_adapter, null
            );
        }
        if (convertView != null) {
            if (_RewardListPopulated(subjectList)) {

/*
                txtmypoints=(TextView)convertView.findViewById(R.id.txtMypoints);
                name = (TextView) convertView.findViewById(R.id.name);

                _txtName = (TextView) convertView
                        .findViewById(R.id.txtNameReward);
                _txtName.setText(subjectList.get(position).get_stuDate());

                _txtActivity = (TextView) convertView
                        .findViewById(R.id.txtActivityReward);
                _txtActivity.setText(subjectList.get(position).get_reason());


                txtPoint = (TextView) convertView
                        .findViewById(R.id.txtPointReward);
                txtPoint.setText(subjectList.get(position).get_points());*/

            }
        }
        return convertView;


    }


    private boolean _RewardListPopulated(ArrayList<DisplayTeacSubjectModel> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;

        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        subjectList = DisplaySubjectFeatureController.getInstance().getSearchedTeacher();

    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FriendsFilter();
        }
        return filter;
    }

    private class FriendsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<DisplayTeacSubjectModel> filteredItems =
                        new ArrayList<DisplayTeacSubjectModel>();
                for (int i = 0, l = filterlist.size(); i < l; i++) {
                    // ArrayList<HashMap<String, String>> p =
                    // originalList.get(i);
                    DisplayTeacSubjectModel p = filterlist.get(i);

                    ArrayList<String> arrayList=new ArrayList<String>();
                    arrayList.add(p.get_subname());
                    if (arrayList.toString().toLowerCase().contains(constraint))
                        filteredItems.add(p);

                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else {
                synchronized (this) {
                    result.values = filterlist;
                    result.count = filterlist.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // TODO Auto-generated method stub
            subjectList = (ArrayList<DisplayTeacSubjectModel>) results.values;
            notifyDataSetChanged();

        }
    }


}
