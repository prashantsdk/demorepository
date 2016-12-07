package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.AdminThanqFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.RewardPointLogFeatureController;
import com.blueplanet.smartcookieteacher.models.AdminThankqPoint;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.ui.AdminFragment;
import com.blueplanet.smartcookieteacher.ui.RewardPointFragment;

import java.util.ArrayList;

/**
 * Created by 1311 on 14-07-2016.
 */
public class AdminThankqAdapter  extends BaseAdapter {

    private AdminFragment _adFragment;
    private AdminThanqFragmentController _contr;
    private ArrayList<AdminThankqPoint> adminlist;
    private final String _TAG = this.getClass().getSimpleName();
    private TextView _txtName, txtDate, _txtMonth, _txtActivity, txtPoint;


    public AdminThankqAdapter(AdminFragment adFragment
    ) {

        _adFragment = adFragment;
        adminlist = AdminThanqFeatureController.getInstance().get_adminthanqlist();

    }

    @Override
    public int getCount() {
        if (_RewardListPopulated(adminlist)) {
            return adminlist.size();
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
            convertView = inflatorInflater.inflate(R.layout.admin_point_adapter, null
            );
        }
        if (convertView != null) {
            if (_RewardListPopulated(adminlist)) {

                String date = adminlist.get(position).get_date();
                Log.i(_TAG, "Date" + date);
                String[] items1 = date.split("/");
                String date1=null, month=null, year=null;
                if (items1.length == 3) {
                    date1 = items1[0];
                    month = items1[1];
                    year = items1[2];
                } else {
                    String[] items2 = date.split("-");
                    if(items2.length==3)
                    {
                        date1 = items2[0];
                        month = items2[1];
                        year = items2[2];
                    }
                }
                String month1 = _returnMonthOfDate(month);

                _txtMonth = (TextView) convertView.findViewById(R.id.tv_mon);
                _txtMonth.setText(month1);

                txtDate = (TextView) convertView
                        .findViewById(R.id.txtDateRewd);
                txtDate.setText(date1);


                _txtName = (TextView) convertView
                        .findViewById(R.id.txtNameRewd);
                _txtName.setText(adminlist.get(position).get_name());

                _txtActivity = (TextView) convertView
                        .findViewById(R.id.txtActivityRewd);
                _txtActivity.setText(adminlist.get(position).get_get_reason());


                txtPoint = (TextView) convertView
                        .findViewById(R.id.txtPointRewd);
                txtPoint.setText(adminlist.get(position).get_point());

            }
        }
        return convertView;

    }



    private boolean _RewardListPopulated(ArrayList<AdminThankqPoint> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;

        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        adminlist = AdminThanqFeatureController.getInstance().get_adminthanqlist();

    }

    private String _returnMonthOfDate(String month) {

        if (!(TextUtils.isEmpty(month))) {

            if (month.equalsIgnoreCase("01")) {
                return "JAN";
            } else if (month.equalsIgnoreCase("02")) {
                return "FEB";
            } else if (month.equalsIgnoreCase("03")) {
                return "MAR";
            } else if (month.equalsIgnoreCase("04")) {
                return "APRIL";
            } else if (month.equalsIgnoreCase("05")) {
                return "MAY";
            } else if (month.equalsIgnoreCase("06")) {
                return "JUNE";
            } else if (month.equalsIgnoreCase("07")) {
                return "JULY";
            } else if (month.equalsIgnoreCase("08")) {
                return "AUG";
            } else if (month.equalsIgnoreCase("09")) {
                return "SEPT";
            } else if (month.equalsIgnoreCase("10")) {
                return "OCT";
            } else if (month.equalsIgnoreCase("11")) {
                return "NOV";
            } else if (month.equalsIgnoreCase("12")) {
                return "DEC";
            }

        }
        return null;
    }
}
