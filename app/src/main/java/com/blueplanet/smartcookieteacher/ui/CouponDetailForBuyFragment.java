package com.blueplanet.smartcookieteacher.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.featurecontroller.BuyCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DashboardFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DisplayCouponFeatureController;
import com.blueplanet.smartcookieteacher.featurecontroller.DrawerFeatureController;
import com.blueplanet.smartcookieteacher.models.BuyCoupon;
import com.blueplanet.smartcookieteacher.models.Coupon_display;
import com.blueplanet.smartcookieteacher.models.TeacherDashbordPoint;
import com.blueplanet.smartcookieteacher.ui.controllers.CouponDetailFragmentController;
import com.blueplanet.smartcookieteacher.utils.IImageLoader;
import com.blueplanet.smartcookieteacher.utils.SmartCookieImageLoader;
import com.blueplanet.smartcookieteacher.webservices.WebserviceConstants;

/**
 * Created by 1311 on 22-02-2016.
 */
public class CouponDetailForBuyFragment extends Fragment {
    private ImageView _couponImage;

    private TextView _txtpoint, _txtName, txt30,_txtcompname;
    private View _view;
    private CouponDetailFragmentController _controller;
    private final String _TAG = this.getClass().getSimpleName();
    private TextView txt_buy, txt_addtocart;
    private String _couponPoints = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        _view = inflater.inflate(R.layout.coupon_detail_for_buy, null);
        _initUI();
        setHasOptionsMenu(true);
        _controller = new CouponDetailFragmentController(this, _view);
        setCouponDataOnUI();
        _registerUIListner();
        return _view;
    }

    private void _initUI() {

        _couponImage = (ImageView) _view.findViewById(R.id.coupon_img);
        _txtName = (TextView) _view
                .findViewById(R.id.txtName);
        _txtpoint = (TextView) _view
                .findViewById(R.id.txtoff);
        txt30 = (TextView) _view.findViewById(R.id.txtPoint);

        txt_buy = (TextView) _view.findViewById(R.id.txt_Buy);
        txt_addtocart = (TextView) _view.findViewById(R.id.txt_card);
        _txtcompname = (TextView) _view.findViewById(R.id.txtcompname);

    }

    private void _registerUIListner() {
        txt_buy.setOnClickListener(_controller);
        txt_addtocart.setOnClickListener(_controller);

    }


    public void setCouponDataOnUI() {

        final Coupon_display coupon = DisplayCouponFeatureController.getInstance().get_selectedCoupon();
        if (coupon != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(_TAG, "In setCouponDataOnUI");
                    _txtName.setText(coupon.get_sp_product());
                    _txtcompname.setText(coupon.getComp());

                    String cPoint = coupon.get_discount() + "% OFF";
                    if (!TextUtils.isEmpty(cPoint) && cPoint.equalsIgnoreCase("null")) {
                        _txtpoint.setText("0");
                    } else {
                        _txtpoint.setText(cPoint);
                    }
                    _couponPoints = coupon.get_points_per_product();

                    txt30.setText("(ON " + _couponPoints + " POINTS)");


                    String couponImage = coupon.get_image_path();
                    if (couponImage != null && couponImage.length() > 0) {
                        final String imageName = couponImage;
                        Log.i(_TAG, imageName);

                        SmartCookieImageLoader.getInstance().setImageLoaderData(imageName, _couponImage,
                                IImageLoader.CIRCULAR_USER_POSTER);
                        SmartCookieImageLoader.getInstance().display();
                    }

                }
            });
        }
    }

    public void setCouponCodeOnUI() {
        final BuyCoupon buycoup = BuyCouponFeatureController.getInstance().get_selectedCoup();
        if (buycoup != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // _couponCode.setText(buycoup.get_couponCode());
                }
            });
        }

    }

    public void showNoStudentListMessage(final boolean flag) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag == false) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getActivity().getString(R.string.no_students_available),
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private int _getRemainingPoints() {
        TeacherDashbordPoint point = DashboardFeatureController.getInstance().getTeacherpoint();
        int remainingPoints = -1;
        if (point != null) {
            int greenPoint = point.get_greenpoint();
            if (greenPoint != -1) {
                int couponPoints = Integer.parseInt(_couponPoints);
                remainingPoints = greenPoint - couponPoints;
            }

        }
        return remainingPoints;
    }

    public void showCouponBuyUnsuccessfulMessage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(),
                        getActivity().getString(R.string.coupon_buy_unsuccessful),
                        Toast.LENGTH_LONG).show();

            }
        });

    }

    public void onDestroy() {

        super.onDestroy();
        if (_controller != null) {
            _controller.clear();
            _controller = null;
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.my_cart, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == R.id.action_add) {
            _loadFragment(R.id.content_frame, new AddCartFragment());

        } else {

        }
        return super.onOptionsItemSelected(item);
    }

    private void _loadFragment(int id, Fragment fragment) {

        DrawerFeatureController.getInstance().setIsFragmentOpenedFromDrawer(false);
        FragmentManager fm = getActivity().getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack("TeacherDashboardFragment");

        ft.commit();
    }

}










