package com.blueplanet.smartcookieteacher.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.blueplanet.smartcookieteacher.R;
import com.blueplanet.smartcookieteacher.customcomponents.CustomTextView;
import com.blueplanet.smartcookieteacher.gcm.MainActivity;
import com.blueplanet.smartcookieteacher.models.LatAndLongModel;
import com.blueplanet.smartcookieteacher.ui.controllers.DisplayCategorieFragmentController;
import com.blueplanet.smartcookieteacher.ui.controllers.DisplayCouponAdapter;


/**
 * Created by 1311 on 09-01-2016.
 */

public class DisplayCategorieFragment extends Fragment {

    private View _view;
    private GridView _gridView;
    private CustomTextView _selectCategorie;
    private DisplayCategorieFragmentController _displayCategorieFragmentController;
    private DisplayCouponAdapter _couponAdapter;
    private TextView _tvNoCouponMessage;
    GPSTracker   gpsTracker;;
    double latitude = 0.0, longitude = 0.0;
    public static final int PERMISSION_REQUEST_CODE=23;
    String[] LOC_PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.display_categorie, null);
        _initUI();
        getActivity().setTitle("Buy Coupon");
        _displayCategorieFragmentController = new DisplayCategorieFragmentController(this, _view);
        _couponAdapter = new DisplayCouponAdapter(this, _displayCategorieFragmentController);

        _registerUIListeners();
        if (checkPermission()) {
            gpsTracker = new GPSTracker(getActivity());
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

        } else {

            requestPermission();
        }

        return _view;
    }

    private void _initUI() {
        _selectCategorie = (CustomTextView) _view.findViewById(R.id.txtCatogory);
        _gridView = (GridView) _view.findViewById(R.id.grid);
        _tvNoCouponMessage = (TextView) _view.findViewById(R.id.txtmessage);
    }

    private void _registerUIListeners() {
        _selectCategorie.setOnClickListener(_displayCategorieFragmentController);
        _gridView.setAdapter(_couponAdapter);
        _gridView.setOnItemClickListener(_displayCategorieFragmentController);

    }

    public void showOrHideErrorMessage(final boolean visibility) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (visibility) {
                    _tvNoCouponMessage.setVisibility(View.VISIBLE);
                    _gridView.setVisibility(View.GONE);
                } else {
                    _tvNoCouponMessage.setVisibility(View.GONE);
                    _gridView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void refreshGridview() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _couponAdapter.notifyDataSetChanged();

            }
        });
    }

    public void setNameOnCategoryTextView(final String name) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _selectCategorie.setText(name);
            }
        });

    }

    public void onDestroy() {
        super.onDestroy();
        if (_displayCategorieFragmentController != null) {
            _displayCategorieFragmentController.clear();
            _displayCategorieFragmentController = null;
        }
        if (_couponAdapter != null) {
            _couponAdapter = null;
        }


    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }


    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)){

            Toast.makeText(getActivity(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), LOC_PERMISSIONS, PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    gpsTracker = new GPSTracker(getActivity());
                    Toast.makeText(getActivity(), "Permission Granted, Now you can access location data", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getActivity(), "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

}
