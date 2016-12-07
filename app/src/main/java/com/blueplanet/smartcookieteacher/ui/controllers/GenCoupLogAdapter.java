package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.blueplanet.smartcookieteacher.MainApplication;
import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.GenerateCouLogFeatureController;
import com.blueplanet.smartcookieteacher.models.GenerateCouponLog;
import com.blueplanet.smartcookieteacher.ui.GenerateCoupFragment;
import java.util.ArrayList;

/**
 * Created by 1311 on 05-04-2016.
 */
public class GenCoupLogAdapter extends BaseAdapter {

    private GenerateCoupFragment _coupon_log;
    private GenerateCoupLogFragmentController _logFragmentController;
    private ArrayList<GenerateCouponLog> couLog;
    private final String _TAG = this.getClass().getSimpleName();
    private TextView _txtcoupName, txtPoint, _txtValidity, txtcode, _txtcoupCode, txtIsshu;
    private ImageView _imgcoupBuy;

    public GenCoupLogAdapter(GenerateCoupFragment coupon_log                             ) {

        _coupon_log = coupon_log;

        couLog = GenerateCouLogFeatureController.getInstance().get_genCouLog();

    }

    @Override
    public int getCount() {
        if (_couListPopulated(couLog)) {
            return couLog.size();
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
            convertView = inflatorInflater.inflate(R.layout.gen_coup_log_item, null
            );
        }
        if (convertView != null) {
            if (_couListPopulated(couLog)) {
                txtcode = (TextView) convertView.findViewById(R.id.txt_couCode);
                txtcode.setText(couLog.get(position).get_gencoupon_id());


                txtPoint = (TextView) convertView
                        .findViewById(R.id.txt_couPoint);
                txtPoint.setText(couLog.get(position).get_couponPoint());

                txtIsshu = (TextView) convertView
                        .findViewById(R.id.txt_issue_Date);
                txtIsshu.setText(couLog.get(position).get_gencoupon_issue_date());


                _txtValidity = (TextView) convertView.findViewById(R.id.txtValidity_Date);
                _txtValidity.setText(couLog.get(position).get_generate_validity_date());

            }


        }

        return convertView;
    }

    private boolean _couListPopulated(ArrayList<GenerateCouponLog> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;

        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        couLog = GenerateCouLogFeatureController.getInstance().get_genCouLog();


    }
}
