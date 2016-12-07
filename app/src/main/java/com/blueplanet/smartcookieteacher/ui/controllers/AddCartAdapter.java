package com.blueplanet.smartcookieteacher.ui.controllers;

import android.content.Context;
import android.text.TextUtils;
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
import com.blueplanet.smartcookieteacher.featurecontroller.AddToCartFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DisplayCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.GenerateCouponFeatureController;
import com.blueplanet.smartcookieteacher.models.AddCart;
import com.blueplanet.smartcookieteacher.models.Coupon_display;
import com.blueplanet.smartcookieteacher.models.GenerateCoupon;
import com.blueplanet.smartcookieteacher.ui.AddCartFragment;
import com.blueplanet.smartcookieteacher.ui.GenerateCouponFragment;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

import java.util.ArrayList;

/**
 * Created by 1311 on 15-03-2016.
 */
public class AddCartAdapter extends BaseAdapter {

    private AddCartFragment _addFragment;
    private AddCartFragmentController _controller;
    private ArrayList<AddCart> _coupDetailList;
    private final String _TAG = this.getClass().getSimpleName();
    private TextView _couName, _couId, _txtPoint, _txtOff, _txtvalidity;
    private String _couponPointsOff = null;
    private ImageView _couImg;

    public AddCartAdapter(AddCartFragment addFragment,
                          AddCartFragmentController controller,
                          View _view) {

        _addFragment = addFragment;
        _controller = controller;
        _coupDetailList = AddToCartFeatureController.getInstance().get_selectedCoupList();
        Log.i(_TAG, "Incoup" + _coupDetailList);

    }

    @Override
    public int getCount() {
        if (_genCoupListPopulated(_coupDetailList)) {
            return _coupDetailList.size();
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
            convertView = inflatorInflater.inflate(R.layout.add_to_cart, null
            );
        }
        if (convertView != null) {
            if (_genCoupListPopulated(_coupDetailList)) {
                _couName = (TextView) convertView.findViewById(R.id.txt_couName);
                _couName.setText(_coupDetailList.get(position).get_coupName());

                _couId = (TextView) convertView
                        .findViewById(R.id.txt_couId);
                _couId.setText(_coupDetailList.get(position).get_couId());

                _txtPoint = (TextView) convertView
                        .findViewById(R.id.txtPoint);

                String cPoint = _coupDetailList.get(position).get_coupoints() + "% OFF";
                if (TextUtils.isEmpty(cPoint) && cPoint.equalsIgnoreCase("null")) {
                    _txtPoint.setText("0");
                } else {
                    _txtPoint.setText(cPoint);
                }

                /*_txtOff = (TextView) convertView.findViewById(R.id.txt_pointOFF);

                _couponPointsOff = _coupDetailList.get(position).get_coupoints();

                _txtOff.setText("(ON " + _couponPointsOff + " POINTS)");*/

                _txtvalidity = (TextView) convertView.findViewById(R.id.txtValidity_Date);
                _txtvalidity.setText(_coupDetailList.get(position).get_coupValidity());
                _couImg=(ImageView) convertView.findViewById(R.id.img_coupon);


                String imageurl = _coupDetailList.get(position).get_coupimage();
                if (imageurl != null && imageurl.length() > 0) {
                  /*  final String imageName = WebserviceConstants.IMAGE_BASE_URL
                            + imageurl;*/
                    final String imageName = imageurl;

                    Log.i(_TAG, imageName);


                    SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, _couImg,
                            IImageLoader.CIRCULAR_USER_POSTER);
                    SmartCookieImageLoader.getInstance().display();
                }
            }


        }

        return convertView;
    }

    private boolean _genCoupListPopulated(ArrayList<AddCart> dummyList) {

        if (dummyList != null && dummyList.size() > 0) {
            return true;
        }
        return false;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        _coupDetailList = AddToCartFeatureController.getInstance().get_selectedCoupList();
    }
}
