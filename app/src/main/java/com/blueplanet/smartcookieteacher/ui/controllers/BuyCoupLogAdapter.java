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
import com.blueplanet.smartcookieteacher.customcomponents.CustomButton;
import com.blueplanet.smartcookieteacher.featurecontroller.BuyCouLogFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.BuyCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.RewardPointLogFeatureController;
import com.blueplanet.smartcookieteacher.models.Buy_Coupon_log;
import com.blueplanet.smartcookieteacher.models.RewardPointLog;
import com.blueplanet.smartcookieteacher.ui.BuyCouponLogFragment;
import com.blueplanet.smartcookieteacher.ui.RewardPointFragment;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 22-03-2016.
 */
public class BuyCoupLogAdapter extends BaseAdapter {

    private BuyCouponLogFragment _buy_coupon_log;
    private BuyCouLogFragmentController _logFragmentController;
    private ArrayList<Buy_Coupon_log> couLogList;
    private final String _TAG = this.getClass().getSimpleName();
    private TextView _txtcoupName, txtPoint, _txtValidity, txtcode;
    private ImageView _imgcoupBuy;

    public BuyCoupLogAdapter(BuyCouponLogFragment buy_coupon_log
                             ) {

        _buy_coupon_log = buy_coupon_log;

        couLogList = BuyCouLogFeatureController.getInstance().get_buyCouLog();

    }

    @Override
    public int getCount() {
        if (_couListPopulated(couLogList)) {
            return couLogList.size();
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
            convertView = inflatorInflater.inflate(R.layout.buy_coupon_log_activity, null
            );
        }
        if (convertView != null) {
            if (_couListPopulated(couLogList)) {
                _txtcoupName = (TextView) convertView.findViewById(R.id.txt_buycouName);
                _txtcoupName.setText(couLogList.get(position).get_couponLogName());
                Log.i(_TAG, "Point" + _txtcoupName.getText());
                txtcode = (TextView) convertView
                        .findViewById(R.id.txt_couCode);
                txtcode.setText(couLogList.get(position).get_couponLogcode());

                txtPoint = (TextView) convertView
                        .findViewById(R.id.txt_point);
                txtPoint.setText(couLogList.get(position).get_couLogPointsPerProduct());
                Log.i(_TAG, "Point" + txtPoint.getText());

                _txtValidity = (TextView) convertView.findViewById(R.id.txtValidity);
                _txtValidity.setText(couLogList.get(position).get_couponLogValidity());
                _imgcoupBuy = (ImageView) convertView.findViewById(R.id.img_coupon_buy);
                String imageurl = couLogList.get(position).get_couLogImage();
                if (imageurl != null && imageurl.length() > 0) {
                    final String imageName = imageurl;
                    Log.i(_TAG, imageName);
                    SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, _imgcoupBuy,
                            IImageLoader.CIRCULAR_USER_POSTER);
                    SmartCookieImageLoader.getInstance().display();
                }

            }


        }

        return convertView;
    }


    private boolean _couListPopulated(ArrayList<Buy_Coupon_log> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;

        }
        return false;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        couLogList = BuyCouLogFeatureController.getInstance().get_buyCouLog();


    }
}
